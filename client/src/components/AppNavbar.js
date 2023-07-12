import React, { useContext } from "react";
import { NavLink, Link } from "react-router-dom";

import { NavDropdown } from "react-bootstrap";

import { useChangeProject, useFetchProjects } from "../utils/Repositories";
import { ProjectContext } from "../App";
import { useLocalStorage } from "../hooks/useLocalStorage";
import { ChevronDown } from "react-bootstrap-icons";

export const ProjectsDropdown = ({ divider }) => {
  const { currentProject, setCurrentProject } = useContext(ProjectContext);
  const { data, isLoading, error } = useFetchProjects();
  const [user, setUser] = useLocalStorage("user");

  if (isLoading) return <p className="ms-3 mb-1 text-muted"> Loading...</p>;

  const HandleChooseProject = (project) => {
    setCurrentProject(project);
    useChangeProject(project.id);
    setUser({ ...user, currentProject: project });
  };

  return (
    <>
      {data._embedded?.projectModelList?.map((project) => {
        if (currentProject === null) {
          HandleChooseProject(project);
        }

        return (
          <NavDropdown.Item
            onClick={() => HandleChooseProject(project)}
            key={project?.id}
            className={
              currentProject?.id === project?.id
                ? "fw-semibold text-dark"
                : "fw-semibold text-secondary"
            }
          >
            {project.name}
          </NavDropdown.Item>
        );
      })}
      {divider && data._embedded?.projectModelList && <NavDropdown.Divider />}
    </>
  );
};

function AppNavbar() {
  const { currentProject, setCurrentProject } = useContext(ProjectContext);
  // const { user,  } = useAuth();

  return (
    <>
      <NavLink
        className={
          currentProject == null || currentProject.id == 0
            ? "nav-link disabled me-3"
            : "nav-link me-3"
        }
        to="mywork"
      >
        Your work
      </NavLink>
      <NavDropdown
        id="nav-projects-dropdown"
        title=<span className="fs-5">Projects <ChevronDown /></span>
        menuVariant="light"
        className="fs-5 me-3"
        variant="underline"
      >
        <ProjectsDropdown divider={true} />

        <NavDropdown.Item as={Link} className="" to="projects">
          View all
        </NavDropdown.Item>
        <NavDropdown.Item as={Link} className="" to="/projects/new">
          Create
        </NavDropdown.Item>
      </NavDropdown>

      <NavLink
        className={
          currentProject == null || currentProject.id == 0
            ? "nav-link disabled me-3"
            : "nav-link me-3"
        }
        to={`boards`}
      >
        Boards
      </NavLink>
    </>
  );
}

export default AppNavbar;
