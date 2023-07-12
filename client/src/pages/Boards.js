import { useLocation, useNavigate } from "react-router-dom";
import { useContext, useState } from "react";
import { DragDropContext } from "react-beautiful-dnd";
import axios from "axios";
import { useMutation, useQueryClient } from "@tanstack/react-query";
// pages and utilities
import { useFetchBoards, useFetchIssues } from "../utils/Repositories";
import IssueBoard from "../components/IssueBoard";
import { ProjectContext } from "../App";
import { useAuth } from "../hooks/useAuth";

import { Button } from "react-bootstrap";
import { Form } from "react-bootstrap";

import { Row, Table } from "react-bootstrap";
import { Toggle2On } from "react-bootstrap-icons";

export default function Boards() {
  const { logout } = useAuth();
  const navigate = useNavigate();
  const [showAll, setShowAll] = useState("/mywork");
  const [sort, setSort] = useState("startDate");
  const [order, setOrder] = useState("desc");

  const { currentProject } = useContext(ProjectContext);

  const url = `/api/issues${showAll}?project=${currentProject?.id}&sort=${sort},${order}`;
  const queryClient = useQueryClient();
  const { data, isLoading, error } = useFetchBoards(url, url);

  const update = useMutation({
    mutationFn: (issue) => {
      const statusUpdate = { status: issue.newStatus };
      return axios.patch(`/api/issues/${issue.id}`, statusUpdate);
    },
    onMutate: async (issueList) => {
      // Cancel any outgoing refetches (so they don't overwrite our optimistic update)
      await queryClient.cancelQueries({ queryKey: [url] });
      // Snapshot the previous value
      const previousIssues = queryClient.getQueryData([url]);
      // Optimistically update to the new value
      queryClient.setQueryData([url], (old) => {
        old.columns = issueList.columns.columns;
      });
      // Return a context object with the snapshotted value
      return { previousIssues };
    },
    onError: (err, updatedIssue, context) => {
      queryClient.setQueryData([url], context.previousIssues);
    },
    // Always refetch after error or success:
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: "issues" });
    },
  });

  const toggleShowAll = () => {
    if (showAll === "/mywork") {
      setShowAll("");
    } else {
      setShowAll("/mywork");
    }
  };

  function handleOnDragEnd(result) {
    const { destination, source, draggableId } = result;
    if (!destination) {
      return;
    }
    // same position as it was befor dnd - no action
    if (
      destination.droppableId === source.draggableId &&
      destination.index === source.index
    ) {
      return;
    }
    // console.log("HANDLE ON DRAG END: ", result)
    // temporar to prevent creating double issues
    if (destination.droppableId === source.droppableId) {
      return;
    }

    const start = data.columns[source.droppableId];
    const finish = data.columns[destination.droppableId];

    // // !to implement ordering in columns
    // if (start === finish) {
    //   const newIssueIds = Array.from(start.issueIds);
    //   newIssueIds.splice(source.index, 1);
    //   newIssueIds.splice(destination.index, 0, draggableId);

    //   const newColumn = {
    //     ...start,
    //     issueIds: newIssueIds,
    //   };
    //   const newState = {
    //     ...data,
    //     columns: {
    //       ...data.columns,
    //       [newColumn.id]: newColumn,
    //     },
    //   };
    //   setIssues(newState)
    //   return;
    // }
    //Moving from one list to another
    const startIssueIds = Array.from(start.issueIds);
    startIssueIds.splice(source.index, 1);
    const newStart = {
      ...start,
      issueIds: startIssueIds,
    };
    const finishIssueIds = Array.from(finish.issueIds);
    finishIssueIds.splice(destination.index, 0, draggableId);
    const newFinish = {
      ...finish,
      issueIds: finishIssueIds,
    };
    const newState = {
      ...data.issues,
      columns: {
        ...data.columns,
        [newStart.title]: newStart,
        [newFinish.title]: newFinish,
      },
    };
    update.mutate({
      id: draggableId,
      newStatus: destination.droppableId,
      columns: newState,
    });
  }

  // if (isLoading) return;
  // <>
  //   <Form className="px-2">
  //     <Form.Check
  //       type="switch"
  //       id="custom-switch"
  //       label="Check this switch"
  //       checked={showAll === ""}
  //       onChange={toggleShowAll}
  //     />
  //   </Form>
  //   <p>'Loading...'</p>;
  // </>;

  if (error) {
    if (error.response?.status === 401) {
      logout();
      // setUser(null)
      return navigate("/login", { replace: true });
    } else {
      return <p>An error occured: {error.message}</p>;
    }
  }
  {
    /* <DragDropContext onDragEnd={handleOnDragEnd}>
      <div className="row gy-3 gx-2">
        {data.columnOrder.map((column) => {
          const issueList = data.columns[column].issueIds.map(
            (id) => data.issues[id]
          );
          return (
            <IssueBoard
              key={column}
              data={issueList}
              project={currentProject}
              status={column}
            />
          );
        })}
      </div>
    </DragDropContext> */
  }
  // className="d-flex flex-row flex-nowrap overflow-auto"
  return (
    <>
      <Form className="px-2">
        <Form.Check
          type="switch"
          id="showAll"
          label="Show all"
          checked={showAll === ""}
          onChange={toggleShowAll}
        />
      </Form>

      {isLoading ? (
        <p>Loading...</p>
      ) : (
        <DragDropContext onDragEnd={handleOnDragEnd}>
          <div className="d-flex flex-row flex-nowrap" style={{margin: "1px"}}>
            {data.columnOrder.map((column) => {
              const issueList = data.columns[column].issueIds.map(
                (id) => data.issues[id]
              );
              return (
                <IssueBoard
                  key={column}
                  data={issueList}
                  project={currentProject}
                  status={column}
                  url={url}
                  isLoading={isLoading}
                  error={error}
                />
              );
            })}
          </div>
        </DragDropContext>
      )}
    </>
  );
}
