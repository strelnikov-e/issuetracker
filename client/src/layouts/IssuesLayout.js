import { Outlet, redirect, useNavigate } from "react-router-dom";
import { useContext, useEffect, useState } from "react";
import { NavDropdown } from "react-bootstrap";
import { Button } from "react-bootstrap";
import { Row, Col } from "react-bootstrap";

import { CreateProject, ProjectContext } from "../App";
import { ProjectsDropdown } from "../components/AppNavbar";
import Breadcrumbs from "../components/Breadcrumbs";
import { IssuesSideNavBar } from "../components/Sidebar";

export default function IssuesLayout() {
  const { currentProject } = useContext(ProjectContext);
  const navigate = useNavigate();

  const [windowSize, setWindowSize] = useState([window.innerWidth]);

  useEffect(() => {
    const handleWindowResize = () => {
      setWindowSize([window.innerWidth]);
    };

    window.addEventListener("resize", handleWindowResize);

    return () => {
      window.removeEventListener("resize", handleWindowResize);
    };
  }, []);

  useEffect(() => {
    if (
      !currentProject ||
      currentProject.id === null ||
      currentProject.id === 0
    ) {
      navigate("/projects");
    }
  }, []);

  if (
    !currentProject ||
    currentProject.id === null ||
    currentProject.id === 0
  ) {
    return (
      <div className="container-lg p-5">
        <CreateProject />
      </div>
    );
  }

  const Title = () => {
    if (!currentProject) {
      return "Loading";
    }
    const key = (
      <span className="text-muted fs-4 me-2">{currentProject?.key}</span>
    );
    const title = currentProject?.name;
    return (
      <>
        {key} {title}
      </>
    );
  };

  return (
    <>
      <Row>
        <Col className="col-2 col-md-1">
          {windowSize[0] > 767 && <IssuesSideNavBar />}
        </Col>
        <Col className="col-md-11">
          <div className="fluid">
            <Breadcrumbs />

            <NavDropdown
              id="nav-projects-dropdown"
              title={<Title />}
              menuVariant="light"
              className="btn btn-outline-light text-dark border-0 fs-4 mb-4"
            >
              <ProjectsDropdown divider={false} />
            </NavDropdown>

            <div className="">
              <Outlet />
              <div></div>
            </div>
          </div>
        </Col>
      </Row>
    </>
  );
}
