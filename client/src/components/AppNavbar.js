import React, { useState, useContext } from "react";
import { NavLink, Link } from "react-router-dom";
import { useQueryClient } from "@tanstack/react-query";
import { NavDropdown } from "react-bootstrap";
import { FetchProjects } from "../utils/Repositories";
import { ProjectContext } from "../App";

function AppNavbar() {
  const uncheckIcon = (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width="16"
      height="16"
      fill="currentColor"
      className="bi bi-circle"
      viewBox="0 0 16 16"
    >
      <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z" />
    </svg>
  );
  const checkIcon = (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width="16"
      height="16"
      fill="currentColor"
      className="bi bi-check-circle-fill"
      viewBox="0 0 16 16"
    >
      <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zm-3.97-3.03a.75.75 0 0 0-1.08.022L7.477 9.417 5.384 7.323a.75.75 0 0 0-1.06 1.06L6.97 11.03a.75.75 0 0 0 1.079-.02l3.992-4.99a.75.75 0 0 0-.01-1.05z" />
    </svg>
  );

  const queryClient = useQueryClient();
  const [isOpen, setIsOpen] = useState(false);

  const { currentProject, setCurrentProject } = useContext(ProjectContext);
  const { data, isLoading, error } = FetchProjects();

  const handleChooseProject = (project) => {
    setCurrentProject(project);
    queryClient.invalidateQueries({
      queryKey: [`/api/issues?projectId=${currentProject.id}`],
    });
  };

  const ProjectsDropdown = () => {
    if (isLoading) return <p className="ms-3 mb-1 text-muted"> Loading...</p>;

    return (
      <>
        {data._embedded.projectList.map((project) => {
          if (currentProject === null) {
            handleChooseProject(project);
          }

          return (
            <NavDropdown.Item
              onClick={() => handleChooseProject(project)}
              key={project.id}
              className="fw-semibold text-secondary"
            >
              <span>
                {currentProject.id === project.id ? checkIcon : uncheckIcon}{" "}
              </span>
              {project.name}
            </NavDropdown.Item>
          );
        })}
      </>
    );
  };

  return (
    <>
      <NavDropdown
        id="nav-projects-dropdown"
        title="Projects"
        menuVariant="light"
        className="fs-5"
        variant="underline"
      >
        <ProjectsDropdown />

        <NavDropdown.Divider />
        <NavDropdown.Item as={Link} className="" to="projects">
          View all
        </NavDropdown.Item>
        <NavDropdown.Item as={Link} className="" to="/projects/new">
          Create
        </NavDropdown.Item>
      </NavDropdown>
      <NavLink className="nav-link" to="issues">
        Issues
      </NavLink>
      <NavLink className="nav-link" to={`boards`}>
        Boards
      </NavLink>
    </>
  );
}

export default AppNavbar;
