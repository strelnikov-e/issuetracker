import { useContext, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import axios from "axios";
import dayjs from "dayjs";

import Table from "react-bootstrap/Table";
import Button from "react-bootstrap/esm/Button";
import ToggleButton from "react-bootstrap/ToggleButton";
import ButtonGroup from "react-bootstrap/ButtonGroup";
import Stack from "react-bootstrap/Stack";
import { Badge, Dropdown, DropdownButton, ListGroup } from "react-bootstrap";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import { Modal } from "react-bootstrap";

import {
  CaretDownFill,
  ChevronLeft,
  ChevronRight,
  List,
  Record,
  RecordFill,
  ThreeDots,
} from "react-bootstrap-icons";
import { CaretUpFill } from "react-bootstrap-icons";
import { CaretRightFill } from "react-bootstrap-icons";
import { CaretLeftFill } from "react-bootstrap-icons";
import { Plus } from "react-bootstrap-icons";
import { CheckCircle } from "react-bootstrap-icons";
import { Filter } from "react-bootstrap-icons";
import { ArrowDownUp } from "react-bootstrap-icons";

import { PriorityBadge } from "../components/PriorityBadge";
import { StatusBadge, StatusConverter } from "../components/StatusBadge";
import { TypeBadgeIcon } from "../components/TypeBadge";
import { useFetchIssues, useFetchMyWork } from "../utils/Repositories";
import { useFetchUsers } from "../utils/Repositories";
import { UserBadge } from "../components/UserBadge";
import { editIcon, trashIcon } from "../components/icons";
import { ProjectContext } from "../App";
import { useAuth } from "../hooks/useAuth";
import { Component } from "react";

export const issueLoader = async () => {
  const response = await axios.get("/api/issues");
  if (!response.ok) {
    throw Error("Could not fetch the issues");
  }
  return response;
};

export default function MyWork() {
  const { user } = useAuth();
  const { currentProject } = useContext(ProjectContext);
  const { logout } = useAuth();
  const navigate = useNavigate();
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [sort, setSort] = useState("status");
  const [order, setOrder] = useState("desc");

  const queryClient = useQueryClient();
  const { data, isLoading, error } = useFetchMyWork(
    currentProject?.id || 0,
    user.id,
    page,
    size,
    sort,
    order
  );
  const {
    data: users,
    isLoading: usersLoading,
    error: usersError,
  } = useFetchUsers(currentProject?.id);
  // we use the meta data returned in the response to disable
  // the "Next" button below
  const meta = data?.page || {};

  const handleSort = (param) => {
    if (sort !== param) {
      setSort(param);
    } else {
      setOrder(order === "desc" ? "asc" : "desc");
    }
    queryClient.invalidateQueries({
      queryKey: ["mywork", currentProject.id, page, size, sort, order],
    });
  };

  const SortIcon = ({ param }) => {
    if (sort === param) {
      if (order === "desc") {
        return <CaretDownFill className="ms-auto" />;
      } else {
        return <CaretUpFill className="ms-auto" />;
      }
    }
  };

  const IssueList = () => {
    if (isLoading) {
      return "Loading...";
    }
    if (error) {
      if (error.response?.status === 401) {
        logout();
        // setUser(null)
        return navigate("/login", { replace: true });
      } else {
        return <p>An error occured: {error.message}</p>;
      }
    }

    const ListEntry = ({ issue }) => {
      return (
        <ListGroup key={issue.status}>
          <ListGroup.Item
            as="li"
            key={issue.key}
            className="d-flex justify-content-between align-items-start border-0 mb-2"
            action
            onClick={() => navigate("/issues/" + issue.id)}
          >
            <div className="ms-2 me-auto">
              <div className="fw-bold">
                <span className="me-2 p-2">
                  <RecordFill
                    size={16}
                    color={
                      issue.priority === "HIGH"
                        ? "red"
                        : issue.priority === "MEDIUM"
                        ? "darkviolet"
                        : "navy"
                    }
                  />
                </span>{" "}
                {issue.name}
              </div>
              <span className="text-secondary ms-5">
                {issue.project.key} {" : "} {issue.project.name}
              </span>
            </div>
            <div className="">{StatusConverter(issue.status)}</div>
          </ListGroup.Item>
        </ListGroup>
      );
    };

    return (
      <>
        <h5 className="p-3 text-secondary bg-light">To do</h5>
        {data?._embedded?.issueModelList.map((issue) => (
          <>{issue.status === "TODO" && <ListEntry issue={issue} />}</>
        ))}
        <h5 className="p-3 text-secondary bg-light">In Progress</h5>
        {data?._embedded?.issueModelList.map((issue) => (
          <>{issue.status === "INPROGRESS" && <ListEntry issue={issue} />}</>
        ))}
        <h5 className="p-3 text-secondary bg-light">In Review</h5>
        {data?._embedded?.issueModelList.map((issue) => (
          <>{issue.status === "INREVIEW" && <ListEntry issue={issue} />}</>
        ))}

        {/* {data?._embedded?.issueModelList.map((issue) => (
          <>
            <h6>To do</h6>
            {issue.status === "TODO" && <ListEntry issue={issue} />}
            <h6>To do</h6>
            {issue.status === "INPROGRESS" && <ListEntry issue={issue} />}
            <h6>In progress</h6>
            {issue.status === "INTESTING" && <ListEntry issue={issue} />}
          </>
        ))} */}
      </>
    );
  };

  return (
    <>
      <div className=" p-3 mb-0">
        <div className="d-flex justify-content-end">

          <DropdownButton
            variant=""
            className="border-light"
            title=<span
              className={
                sort === "startDate"
                  ? "align-middle text-secondary"
                  : "align-middle text-dark"
              }
            >
              <ArrowDownUp /> <span className="align-middle">Group by</span>
            </span>
          >
            <Dropdown.Item
              className="text-muted border-bottom py-2 mb-2 mt-0"
              onClick={() => handleSort("startDate")}
            >
              None
            </Dropdown.Item>
            <Dropdown.Item
              className={sort === "priority" && "text-primary"}
              onClick={() => handleSort("priority")}
            >
              Priority
            </Dropdown.Item>
            <Dropdown.Item
              className={sort === "status" && "text-primary"}
              onClick={() => handleSort("status")}
            >
              Type
            </Dropdown.Item>
          </DropdownButton>
        </div>
      </div>
      <div className="mb-4">
        <IssueList />
      </div>

      <Stack direction="horizontal" gap={2}>
        <div className="me-auto">
          <Button
            variant=""
            className="border-0"
            onClick={() => setPage(page - 1)}
            disabled={page <= 0}
          >
            <ChevronLeft size={24} />
          </Button>
          <span valign="middle" className="align-middle">
            <span
              valign="middle"
              className="fs-5 px-3 py-2"
              style={{
                backgroundColor: "midnightblue",
                color: "white",
                borderRadius: 3,
              }}
            >
              {1 + meta.number || 0}
            </span>
          </span>
          <Button
            variant=""
            className="border-0"
            onClick={() => setPage(page + 1)}
            disabled={meta.totalPages - meta.number - 1 <= 0}
          >
            <ChevronRight size={24} />
          </Button>
        </div>
        Page size:
        <DropdownButton variant="outline" className="border-light" title={size}>
          <Dropdown.Item
            className={size === 5 && "text-primary"}
            onClick={() => setSize(5)}
          >
            5
          </Dropdown.Item>
          <Dropdown.Item
            className={size === 10 && "text-primary"}
            onClick={() => setSize(10)}
          >
            10
          </Dropdown.Item>
          <Dropdown.Item
            className={size === 15 && "text-primary"}
            onClick={() => setSize(15)}
          >
            15
          </Dropdown.Item>
          <Dropdown.Item
            className={size === 20 && "text-primary"}
            onClick={() => setSize(20)}
          >
            20
          </Dropdown.Item>
        </DropdownButton>
      </Stack>
    </>
  );
}
