import React, { useState, useEffect, useContext } from "react";
import { NavLink } from "react-router-dom";
import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";
import useAuth from "../utils/hooks/useAuth";
import { setAuthToken } from "../utils/SetGlobalAuthToken";
import { ProjectContext } from "../App";
import AppNavbar from "./AppNavbar";

function AppNavbarBase() {
  const { auth, setAuth } = useAuth();
  const [isOpen, setIsOpen] = useState(false);
  const [isAuth, setIsAuth] = useState(auth?.email ? true : false);
  const { currentProject, setCurrentProject } = useContext(ProjectContext);

  const toggle = () => setIsOpen(!isOpen);

  const handleLogout = () => {
    setAuth({});
    setAuthToken({});
    localStorage.setItem("accessToken", "");
    localStorage.setItem("email", "");
    localStorage.setItem("password", "");
    setCurrentProject(null)
  };

  useEffect(() => {
    setIsAuth(auth?.email ? true : false);
  }, [auth]);

  return (
    <Navbar expand="md" className="shadow">
      <Container>
        <Navbar.Brand className="fs-4" href="/">
          issue-tracker
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto fs-5" variant="underline">
            {isAuth && <AppNavbar />}
          </Nav>
          <Nav>
            {!isAuth ? (
              <NavLink className="nav-link fs-5" to="login">
                Login
              </NavLink>
            ) : (
              <NavLink className="nav-link fs-5" onClick={handleLogout}>
                Logout
              </NavLink>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

export default AppNavbarBase;
