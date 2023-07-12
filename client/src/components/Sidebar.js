import React, { useState } from "react";
import "@trendmicro/react-sidenav/dist/react-sidenav.css";

import SideNav, {
  Toggle,
  Nav,
  NavItem,
  NavIcon,
  NavText,
} from "@trendmicro/react-sidenav";
import { CardList, Kanban, KanbanFill, ListColumns, ListUl, PencilSquare, PersonFillGear } from "react-bootstrap-icons";
import { useLocation, useNavigate } from "react-router-dom";

export const SettingsSideNavBar = () => {
  const [state, setState] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  return (
    <SideNav
      onToggle={() => setState(!state)}
      expanded={state.isVisible}
      className="border"
    >
      <SideNav.Toggle
        onClick={() => {
          setState({ isVisible: !state.isVisible });
        }}
      />
      <SideNav.Nav defaultSelected={location.pathname.includes("access")? "access" : "details"}>
        <NavItem eventKey="details" onClick={() => navigate()}>
          <NavIcon>
            <PencilSquare size={24} />
          </NavIcon>
          <NavText >Details</NavText>
        </NavItem>
        <NavItem eventKey="access" onClick={() => navigate("access")}>
          <NavIcon>
            <PersonFillGear size={24} />
          </NavIcon>
          <NavText >Access</NavText>
        </NavItem>
      </SideNav.Nav>
    </SideNav>
  );
};

export const IssuesSideNavBar = () => {
  const [state, setState] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  return (
    <SideNav
      onToggle={() => setState(!state)}
      expanded={state.isVisible}
      className="border"
    >
      <SideNav.Toggle
        onClick={() => {
          setState({ isVisible: !state.isVisible });
        }}
      />
      <SideNav.Nav defaultSelected={location.pathname.includes("boards")? "issues" : "boards"}>
        <NavItem eventKey="issues" onClick={() => navigate("/boards")}>
          <NavIcon>
            <Kanban size={24} />
          </NavIcon>
          <NavText >Boards</NavText>
        </NavItem>
        <NavItem eventKey="boards" onClick={() => navigate("/issues")}>
          <NavIcon>
            <ListUl size={24} />
          </NavIcon>
          <NavText >List</NavText>
        </NavItem>
      </SideNav.Nav>
    </SideNav>
  );
};

