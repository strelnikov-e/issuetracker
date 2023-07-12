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
import { Col } from "react-bootstrap";
import { Plus } from "react-bootstrap-icons";

function IssueBoard({ data, status, project, isLoading, error, url }) {
  let initialFormState = {
    name: "",
    project,
    status,
  };
  const queryClient = useQueryClient();
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
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [url] });
      setDraftIssue(initialFormState);
    },
  });

  return (
    <Col className="px-2 py-4">
      <Card style={{ width: "250px" }} className="bg-light" key={status}>
        <Card.Body className="px-1">
          <Card.Subtitle className="bg-light mb-2 px-2">{status}</Card.Subtitle>
          <Droppable droppableId={draftIssue.status}>
            {(provided) => (
              <ListGroup {...provided.droppableProps} ref={provided.innerRef}>
                {data.map((issue, index) => (
                  <Draggable
                    isDragDisabled={!issue._links.update}
                    draggableId={issue.id.toString()}
                    index={index}
                    key={issue.id}
                  >
                    {(provided) => (
                      <Container
                        className={"mb-1 border rounded " + (!issue._links.update? "bg-disabled" : "bg-white")}
                        {...provided.draggableProps}
                        {...provided.dragHandleProps}
                        ref={provided.innerRef}
                      >
                        <IssueCard data={issue} index={index} key={issue.id} url={url} />
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
          <Plus size={24} /> <span className="align-middle">Create issue</span>
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
    </Col>
  );
}

export default IssueBoard;
