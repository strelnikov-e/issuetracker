import { useEffect, useState, useContext } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useQueryClient } from "@tanstack/react-query";

import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import { Dropdown } from "react-bootstrap";
import { Card } from "react-bootstrap";
import axios from "axios";
import dayjs from "dayjs";
import { UserBadge } from "../components/UserBadge";
import { useAuth } from "../hooks/useAuth";
import { useFetchUsers } from "../utils/Repositories";


import { useChangeProject, useFetchProjects } from "../utils/Repositories";
import { ProjectContext } from "../App";
import { useLocalStorage } from "../hooks/useLocalStorage";

export default function CreateProjectPage() {
  const { currentProject, setCurrentProject } = useContext(ProjectContext);
  const [user, setUser] = useLocalStorage("user");
  let initialFormState = {
    id: "",
    name: "",
    description: "",
    status: "OPEN",
    startDate: "",
    managers: [{ id: 0 }],
    admins: [user],
  };

  const draftUser = { id: 0, firstName: "", lastName: "" };

  const [project, setProject] = useState(initialFormState);
  const navigate = useNavigate();
  const { id } = useParams();

  const handleChange = (event) => {
    const { name, value } = event.target;
    setProject({ ...project, [name]: value });
  };

  const HandleChooseProject = (project) => {
    setCurrentProject(project);
    useChangeProject(project.id);
    setUser({ ...user, currentProject: project });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    project["startDate"] = event.target.startDate.value;
    if (project.id) {
      axios.put(`/api/projects/${project.id}`, project);
    } else {
      axios.post(`/api/projects`, project).then(data => HandleChooseProject(data.data));
    }

    setProject(initialFormState);
    navigate("/projects");
  };

  const title = <h4 className="mb-4">Create project</h4>;

  return (
    <Form onSubmit={handleSubmit}>
      {title}

      <Row className="g-5 mb-4">
        <Col md={6} lg={7}>
          <Row>
            <Form.Group className="mb-4" controlId="formProjectName">
              <Form.Label>Name*</Form.Label>
              <Form.Control
                className="sm"
                name="name"
                type="text"
                required
                value={project.name || ""}
                onChange={handleChange}
              />
            </Form.Group>
          </Row>

          <Row>
            <Form.Group className="mb-4" controlId="formProjectStartDate">
              <Form.Label>Start date</Form.Label>
              <Form.Control
                type="date"
                name="startDate"
                value={
                  project.startDate || new Date().toISOString().slice(0, 10)
                }
                onChange={handleChange}
              />
            </Form.Group>
          </Row>
          <Row>
            <Form.Group className="mb-4" controlId="formProjectDescription">
              <Form.Label>Description</Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                name="description"
                value={project.description || ""}
                onChange={handleChange}
              />
            </Form.Group>
          </Row>
        </Col>

        <Col md={6} lg={5}>
          <Card className="mt-4">
            <Card.Header>Information</Card.Header>
            <Card.Body>
              <Row className="py-3">
                <Col className="py-1 align-middle col-5">
                  {Array.from(project.admins).length > 1
                    ? "Administrators:"
                    : "Administrator:"}
                </Col>
                <Col>
                  <UserBadge user={user}></UserBadge>
                  {/* {project.admins.map((user) => (
                <div key={user.id} className="mb-2">
                <UserBadge user={user}></UserBadge>
                </div>
              ))} */}
                </Col>
              </Row>
              <Row className="border-top py-3">
                <Col className="col-5">
                  <div className="py-1 align-middle">
                    {Array.from(project.managers).length > 1
                      ? "Managers:"
                      : "Manager:"}
                  </div>
                </Col>
                <Col className="">
                  <UserBadge user={draftUser}></UserBadge>
                </Col>
              </Row>
            
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row>
        <Form.Group>
          <Button variant="primary me-2" type="submit">
            Submit
          </Button>
          <Button variant="secondary" onClick={() => navigate(-1)}>
            Cancel
          </Button>
        </Form.Group>
      </Row>
    </Form>
  );
}
