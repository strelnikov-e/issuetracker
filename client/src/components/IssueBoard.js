import { useState } from "react";
import axios from "axios";
import { useMutation, useQueryClient } from "@tanstack/react-query";

import Card from "react-bootstrap/Card";
import ListGroup from "react-bootstrap/ListGroup";
import IssueCard from "./IssueCard";
import Collapse from "react-bootstrap/esm/Collapse";
import Button from "react-bootstrap/esm/Button";
import Form from "react-bootstrap/Form";
import { Droppable } from "react-beautiful-dnd";
import { Draggable } from "react-beautiful-dnd";
import { Container } from "react-bootstrap";

function IssueBoard({ data, status, project }) {


  let initialFormState = {
    name: "",
    project,
    status,
  };
  const queryClient = useQueryClient();
  const queryKey = `/api/issues?projectId=${project.id}`
  const [isCreateFormOpen, setIsCreateFormOpen] = useState(false);
  const [draftIssue, setDraftIssue] = useState(initialFormState);

  const handleChange = (event) => {
    const name = event.target.value;
    setDraftIssue({ ...draftIssue, [event.target.name]: name });
  };

  const handleCreateDraftIssue = async (event) => {
    event.preventDefault();
    mutation.mutate();
    setIsCreateFormOpen(false);
  };

  const mutation = useMutation({
    mutationFn: () => {
      return axios.post(`/api/issues`, draftIssue);
    },
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: [queryKey] });
      setDraftIssue(initialFormState);
    },
  });

  return (
    <div  className="col">
      <Card style={{ width: "50" }} className="bg-light" key={status}>
        <Card.Body className="px-1">
          <Card.Subtitle className="bg-light mb-2 px-2">{status}</Card.Subtitle>
          <Droppable droppableId={draftIssue.status}>
            {(provided) => (
              <ListGroup {...provided.droppableProps} ref={provided.innerRef}>
                {data.map((issue, index) => (
                  <Draggable
                    draggableId={issue.id.toString()}
                    index={index}
                    key={issue.id}
                  >
                    {(provided) => (
                      <Container
                        className="mb-1 border rounded bg-white"
                        {...provided.draggableProps}
                        {...provided.dragHandleProps}
                        ref={provided.innerRef}
                      >
                        <IssueCard data={issue} index={index} key={issue.id} />
                      </Container>
                    )}
                  </Draggable>
                ))}
                {provided.placeholder}
              </ListGroup>
            )}
          </Droppable>
        </Card.Body>
        <Button
          className="btn btn-light text-muted text-start"
          onClick={() => setIsCreateFormOpen(!isCreateFormOpen)}
        >
          + Create issue
        </Button>
        <Collapse in={isCreateFormOpen}>
          <Form
            onSubmit={handleCreateDraftIssue}
            className="p-1"
            id="boards-create-issue-collapse"
          >
            <Form.Control
              name="name"
              type="text"
              value={draftIssue.name}
              onChange={handleChange}
              placeholder="What need to be done?"
            />
          </Form>
        </Collapse>
      </Card>
    </div>
  );
}

export default IssueBoard;
