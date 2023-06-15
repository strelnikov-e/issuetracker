import { Outlet , Link } from "react-router-dom";
import Button from "react-bootstrap/esm/Button";
import Stack from 'react-bootstrap/Stack';

export default function ProjectLayout() {
    return (
        <div>
            <Stack direction="horizontal" gap={2} className="mb-5">
                <h4>Projects</h4>
                <Button as={Link} to="new" size="md" className="btn btn-primary ms-auto">Create</Button>
            </Stack>
                
            <Outlet/>
        </div>
    )
}
    
