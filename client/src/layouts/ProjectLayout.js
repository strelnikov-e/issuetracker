import { Outlet, Link } from "react-router-dom";
import Button from "react-bootstrap/esm/Button";
import Stack from "react-bootstrap/Stack";
import { Plus } from "react-bootstrap-icons";
import Breadcrumbs from "../components/Breadcrumbs";

export default function ProjectLayout() {
  return (
    <div  className="container-lg">
      <Breadcrumbs/>
      <Stack direction="horizontal" gap={2} className="mb-4 ">
        <h4>Projects</h4>
        <Button
          valign="middle"
          variant="primary"
          as={Link}
          to="new"
          className="btn btn-sm ms-auto justify-content-center"
        >
          <Plus size={25} /> <span className="align-middle">Create </span>
        </Button>
        
      </Stack>
      
      <Outlet />
    </div>
  );
}
