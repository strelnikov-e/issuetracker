import { Outlet, Link } from "react-router-dom";
import Stack from "react-bootstrap/Stack";
import Breadcrumbs from "../components/Breadcrumbs";

export default function ProjectDetailsLayout() {

  return (
    <>
      
      <div className="container-sm">
        <Breadcrumbs />
        <Stack direction="horizontal" gap={2} className="mb-5">
          <h4>Project details</h4>
        </Stack>

        <Outlet />
      </div>
    </>
  );
}
