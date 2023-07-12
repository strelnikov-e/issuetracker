import { Outlet, Link } from "react-router-dom";
import { useEffect, useState } from "react";
import Button from "react-bootstrap/esm/Button";
import Stack from "react-bootstrap/Stack";
import { Plus } from "react-bootstrap-icons";
import SideNavBar, { SettingsSideNavBar } from "../components/Sidebar";
import { Col, Row } from "react-bootstrap";

import Breadcrumbs from "../components/Breadcrumbs";

export default function SettingsLayout() {

  const [windowSize, setWindowSize] = useState([
    window.innerWidth,
  ]);

  useEffect(() => {
    const handleWindowResize = () => {
      setWindowSize([window.innerWidth]);
    };

    window.addEventListener('resize', handleWindowResize);

    return () => {
      window.removeEventListener('resize', handleWindowResize);
    };
  }, []);


  return (
    <>
    <Row>
      <Col className="col-md-1">
      {windowSize[0] > 767 && (
            <SettingsSideNavBar />
        )}
        
      </Col>
      <Col className="col-12 col-md-10">
        <div className="container-sm">
          <Breadcrumbs />
        <Stack direction="horizontal" gap={2} className="mb-5">
          <h4>Project settings</h4>
        </Stack>
          <Outlet />
        </div>
      </Col>
      </Row>
    </>
  );
}
