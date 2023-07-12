import React from "react";
import { NavLink, useNavigate } from "react-router-dom";
import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";
import { Dropdown } from "react-bootstrap";
import DropdownButton from "react-bootstrap/DropdownButton";

import Logo from "../logo.svg"

import AppNavbar from "./AppNavbar";

import { useAuth } from "../hooks/useAuth";
import { UserBadgeOnly } from "./UserBadge";

function AppNavbarBase() {
  const { user, logout } = useAuth();

  return (
    <Navbar bg="light" data-bs-theme="primary" expand="md" variant="light" className="shadow-sm border-bottom" style={{height: "55px"}}>
      <Container>
        <Navbar.Brand className="fs-4" href="/">

        <img src={Logo} alt="logo" width={27} height={27}/>
          <span className="ms-1 h4 fw-normal">issue</span><span className="h4 fw-semibold">Tracker</span> 
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav" className="">
          <Nav className="me-auto fs-5">
            {user && <AppNavbar />}
          </Nav>
          <Nav>
            {!user ? (
              <NavLink className="btn btn-outline-light text-dark border-0 fs-5" to="login">
                Login
              </NavLink>
            ) : (
              <DropdownButton
                variant="outline-light border-0"
                align="end"
                className="border-0"
                title=<span className="align-middle text-secondary">
                  <UserBadgeOnly user={user} />
                </span>
              >
              <Dropdown.Header>Welcome {user.firstName + " " + user.lastName}</Dropdown.Header>
                <Dropdown.Item>
                  <div onClick={logout}>
                    Logout
                  </div>
                </Dropdown.Item>
              </DropdownButton>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

export default AppNavbarBase;
