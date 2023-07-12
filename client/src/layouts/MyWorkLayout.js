import { Outlet, redirect, useNavigate } from "react-router-dom";
import { useContext, useEffect, useState } from "react";
import { NavDropdown } from "react-bootstrap";
import { Button } from "react-bootstrap";
import { Row, Col } from "react-bootstrap";

import { CreateProject, ProjectContext } from "../App";
import { ProjectsDropdown } from "../components/AppNavbar";
import Breadcrumbs from "../components/Breadcrumbs";
import { IssuesSideNavBar } from "../components/Sidebar";

export default function MyWorkLayout() {
  const { currentProject } = useContext(ProjectContext);
  const navigate = useNavigate();

  return (
    <>
      <div className="container-md">
        <Breadcrumbs />
        <h4>Your work</h4>

        <div className="">
          <Outlet />
        </div>
      </div>
    </>
  );
}
