import React, { useState, useContext } from "react";
import { NavLink, Link } from "react-router-dom";
import { useQueryClient } from "@tanstack/react-query";
import { NavDropdown } from "react-bootstrap";
import { FetchProjects } from "../utils/Repositories";
import { ProjectContext } from "../App";
import { useEffect } from "react";
import useAuth from "../utils/hooks/useAuth";

function AppNavbar() {

  const queryClient = useQueryClient();
  const [isOpen, setIsOpen] = useState(false);
  const {auth} = useAuth();

  const { currentProject, setCurrentProject } = useContext(ProjectContext);
  const { data, isLoading, error } = FetchProjects();

  const handleChooseProject = (project) => {
    setCurrentProject(project);
    queryClient.invalidateQueries({
      queryKey: [`/api/issues?projectId=${currentProject.id}`],
    });
  };

  // useEffect(() => {
  //   setCurrentProject()
  // }, [auth])

  const ProjectsDropdown = () => {
    if (isLoading) return <p className="ms-3 mb-1 text-muted"> Loading...</p>;

    return (
      <>
        {data._embedded?.projectList?.map((project) => {
          if (currentProject === null) {
            handleChooseProject(project);
          }

          return (
            <NavDropdown.Item
              onClick={() => handleChooseProject(project)}
              key={project.id}
              className={currentProject.id === project.id ? "fw-semibold text-dark" : "fw-semibold text-secondary"}
            >
              {project.name}
            </NavDropdown.Item>
          );
        })}
        {data._embedded?.projectList && <NavDropdown.Divider />}
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
