import { Outlet, useNavigate, useLocation, useParams } from "react-router-dom";
import { Button } from "react-bootstrap";
import { useContext } from "react";
import { ProjectContext } from "../App";

export default function IssuesLayout() {

    const { state } = useLocation();
    const { currentProject } = useContext(ProjectContext);
    
    const navigate = useNavigate();
    const project = state? state: {name: 'All projects', projectId: 0}
 

    return (
        <div>
            <div className="container-lg">
                <h4 className="mb-3">{currentProject?.name && "Project"}</h4>

                <div className="d-grid gap-2 d-md-block">
                    <form className="row align-items-center mb-5">
                        <div className="col-sm-5 md-4 col-xl-3 mb-2">
                            <input className="form-control" type="search" placeholder="Search" aria-label="Search"/>
                        </div>
                        <div className="col">
                            <div className="d-flex justify-content-end">
                                <Button onClick={() => navigate('/issues/new', { state: { projectId: currentProject.id}})} className="btn mb-2 justify-content-center">Create</Button>
                            </div>
                        </div>
                    </form>
                </div>

                <Outlet />
            </div>
        </div>
    )
}
