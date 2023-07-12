import { useEffect, useState, useContext } from "react";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import axios from "axios";
import { useQueryClient } from "@tanstack/react-query";
import dayjs from "dayjs";

import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import { Dropdown } from "react-bootstrap";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";

import { useAuth } from "../hooks/useAuth";
import { ProjectContext } from "../App";
import { Stack } from "react-bootstrap";
import { UserBadge } from "../components/UserBadge";
import { useFetchUsers } from "../utils/Repositories";
import Breadcrumbs from "../components/Breadcrumbs";

export default function IssueDetails() {
  const { currentProject } = useContext(ProjectContext);
  const url = `/api/issues?project=${currentProject.id}`;
  const queryClient = useQueryClient();
  const { id } = useParams();
  const { user } = useAuth();
  const { data, isLoading, error } = useFetchUsers(currentProject.id);

  const currentDate = new Date();

  let initialFormState = {
    id: "",
    name: "",
    description: "",
    project: { id: currentProject.id },
    status: "TODO",
    startDate: currentDate.toJSON().slice(0, 10),
    dueDate: new Date(new Date().setDate(new Date().getDate() + 7))
      .toJSON()
      .slice(0, 10),
    closeDate: "",
    assignee: { id: 0, firstName: "", lastName: "" },
  };

  const [draftIssue, setDraftIssue] = useState(initialFormState);
  const navigate = useNavigate();

  useEffect(() => {
    if (id !== "new") {
      axios.get(`/api/issues/${id}`).then((data) => setDraftIssue(data.data));
    }
  }, [id]);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setDraftIssue({ ...draftIssue, [name]: value });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    draftIssue["dueDate"] = event.target.dueDate.value;
    draftIssue["closeDate"] =
      draftIssue.status === "DONE"
        ? event.target.closeDate.value
        : draftIssue.closeDate;
    if (!draftIssue.id) {
      await axios.post(`/api/issues`, draftIssue);
    } else {
      await axios.put(`/api/issues/${draftIssue.id}`, draftIssue);
    }
    setDraftIssue(initialFormState);
    queryClient.invalidateQueries({ queryKey: [url] });
    navigate(-1);
  };

  const title = (
    <h4 className="mb-5">
      {draftIssue.id ? (
        <span className="h4 text-muted">
          {draftIssue.key} <span className="h4 text-dark"> Edit issue </span>{" "}
        </span>
      ) : (
        "Create issue"
      )}
    </h4>
  );

  const AssigneeDropdown = () => {
    if (isLoading) return <p>Loading...</p>;

    return (
      <>
        {data?._embedded?.userModelList.map((u) => (
          <Dropdown.Item
            key={u.id}
            onClick={() => setDraftIssue({ ...draftIssue, assignee: u })}
          >
            <UserBadge user={u}></UserBadge>
          </Dropdown.Item>
        ))}
      </>
    );
  };

  return (
    <div className="container-md">
      <Breadcrumbs />
      <Form onSubmit={handleSubmit}>
        {title}

        <Row className="g-5 mb-4">
          <Col md={7} lg={8}>
            <Row>
              <Form.Group className="mb-4" controlId="formIssueName">
                <Form.Label>Name*</Form.Label>
                <Form.Control
                  className="sm"
                  type="text"
                  name="name"
                  required
                  value={draftIssue.name || ""}
                  onChange={handleChange}
                />
              </Form.Group>
            </Row>

            <Row>
              <Col className="col-3">
                <Form.Group controlId="selectIssueType">
                  <Form.Label>Type</Form.Label>
                  <Form.Select
                    aria-label="selectIssueType"
                    name="type"
                    value={draftIssue.type || "TASK"}
                    onChange={handleChange}
                  >
                    <option value="TASK">Task</option>
                    <option value="BUG">Bug</option>
                    <option value="MILESTONE">Milestone</option>
                  </Form.Select>
                </Form.Group>
              </Col>

              <Col className="col-3">
                <Form.Group controlId="selectIssuePriority">
                  <Form.Label>Priority</Form.Label>
                  <Form.Select
                    aria-label="selectIssuePriority"
                    name="priority"
                    value={draftIssue.priority || "MEDIUM"}
                    onChange={handleChange}
                  >
                    <option value="MEDIUM">Medium</option>
                    <option value="HIGH">High</option>
                    <option value="LOW">Low</option>
                  </Form.Select>
                </Form.Group>
              </Col>

              <Col>
                <Form.Group className="ms-auto" controlId="formIssueDueDate">
                  <Form.Label>Due date</Form.Label>
                  <Form.Control
                    type="date"
                    name="dueDate"
                    value={
                      draftIssue.dueDate ||
                      new Date().toISOString().slice(0, 10)
                    }
                    onChange={handleChange}
                  />
                </Form.Group>
              </Col>
            </Row>

            <Row>
              <Col className="col-3">
                <Form.Group controlId="formIssueStartDate">
                  <Form.Label>Close date</Form.Label>
                  <Form.Select
                    aria-label="selectIssueStatus"
                    name="status"
                    value={draftIssue.status || "TODO"}
                    onChange={handleChange}
                  >
                    <option value="TODO">To do</option>
                    <option value="INPROGRESS">In progress</option>
                    <option value="INREVIEW">In review</option>
                    <option value="DONE">Done</option>
                  </Form.Select>
                </Form.Group>
                </Col>
                <Col className="col-3"></Col>
                <Col>
                {draftIssue.status === "DONE" && (
                  <Form.Group
                    className="ms-auto"
                    controlId="formIssueCloseDate"
                  >
                    <Form.Label>Close date</Form.Label>
                    <Form.Control
                      type="date"
                      name="closeDate"
                      value={
                        draftIssue.closeDate ||
                        new Date().toISOString().slice(0, 10)
                      }
                      onChange={handleChange}
                    />
                  </Form.Group>
                )}
              </Col>
            </Row>

            <Form.Group className="mb-4" controlId="formIssueDescription">
              <Form.Label>Description</Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                name="description"
                value={draftIssue.description || ""}
                onChange={handleChange}
              />
            </Form.Group>
          </Col>

          <Col md={5} lg={4} className="px-5 border-start">
            <Row className="mt-4 mb-4">
              <Col className="h5 p-2 border-bottom">Details</Col>
            </Row>
            <Row className="mb-3 ">
              <Col className="col-3 py-1 align-middle me-2">Reporter:</Col>
              <Col>
                {draftIssue.id ? (
                  <UserBadge user={draftIssue.reporter}></UserBadge>
                ) : (
                  <UserBadge user={user}></UserBadge>
                )}
              </Col>
            </Row>
            <Row className="mb-3">
              <Col className="py-1 align-middle col-3 me-2">Assignee:</Col>
              <Col>
                <Dropdown>
                  <Dropdown.Toggle
                    variant="link"
                    id="assignee dropdown"
                    color="light"
                    className="p-0"
                  >
                    <UserBadge user={draftIssue.assignee}></UserBadge>
                  </Dropdown.Toggle>

                  <Dropdown.Menu>
                    <AssigneeDropdown />
                    {draftIssue.status === "TODO" && (
                      <Dropdown.Item
                        onClick={() =>
                          setDraftIssue({
                            ...draftIssue,
                            assignee: { id: 0, firstName: "", lastName: "" },
                          })
                        }
                      >
                        <UserBadge
                          user={{ id: 0, firstName: "", lastName: "" }}
                        ></UserBadge>
                      </Dropdown.Item>
                    )}
                  </Dropdown.Menu>
                </Dropdown>
              </Col>
            </Row>
            <Row className="mb-3 py-1 align-middle">
              <Col className="col-3 me-2">Created:</Col>
              <Col className="text-muted">
                {dayjs(draftIssue.startDate).format("MMM D, YYYY")}
              </Col>
            </Row>
          </Col>
        </Row>

        <Row>
          <Stack direction="horizontal" gap={5} className="mb-4">
            <Form.Group>
              <Button variant="primary me-2" type="submit">
                Submit
              </Button>
              <Button
                className=""
                variant="secondary"
                onClick={() => navigate(-1)}
              >
                Cancel
              </Button>
            </Form.Group>
          </Stack>
        </Row>
      </Form>
    </div>
  );
}
