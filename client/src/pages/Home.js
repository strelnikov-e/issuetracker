import { Row } from "react-bootstrap";
import { Col } from "react-bootstrap";
import { Accordion } from "react-bootstrap";

import kanban from "../assets/kanban.png";
import roles from "../assets/roles.png";
import issues from "../assets/issues.png";
import yourwork from "../assets/yourwork.png";

export default function Home() {
  const Get = () => (
    <span className="me-1" style={{ color: "wheat" }}>
      GET
    </span>
  );

  const Post = () => (
    <span className="me-1" style={{ color: "salmon" }}>
      POST
    </span>
  );
  const Update = () => (
    <span className="me-1" style={{ color: "mediumvioletRed" }}>
      UPDATE
    </span>
  );
  const Patch = () => (
    <span className="me-1" style={{ color: "violet" }}>
      PATCH
    </span>
  );
  const Delete = () => (
    <span className="me-1" style={{ color: "orangered" }}>
      DELETE
    </span>
  );

  return (
    <div className="">
      <div className="container-lg">
        <Row>
          <h2 className="d-flex justify-content-start mb-5 mt-5 ">Features</h2>
          <Col className="col-12 col-lg-8 col-md-6 mb-5">
            <Accordion defaultActiveKey="0" flush>
              <Accordion.Item eventKey="0">
                <Accordion.Header className="fw-bold">
                  Visualize your workflow
                </Accordion.Header>
                <Accordion.Body>
                  <Row className="">
                    <Col className="align-middle">
                      <h4 className="mb-4">Kanban boards</h4>
                      <p>
                        Kanban boards are visual tools used to manage and track
                        work in progress within a project or workflow. Kanban
                        boards help teams gain visibility into their work,
                        prioritize tasks, identify bottlenecks, and ensure
                        smooth workflow management.
                      </p>
                    </Col>
                    <Col>
                      <img src={kanban} alt="kanbanImg" width={400} />
                    </Col>
                  </Row>
                </Accordion.Body>
              </Accordion.Item>
              <Accordion.Item eventKey="1">
                <Accordion.Header>Assign roles</Accordion.Header>
                <Accordion.Body>
                  <Row className="">
                    <Col className="align-middle">
                      <h4 className="mb-4">Access management</h4>
                      <p>
                        Control access to a project with roles by assigning
                        specific permissions and privileges to different users
                        based on their roles within the project. This ensures
                        that only authorized users have the appropriate level of
                        access and capabilities to perform their designated
                        tasks.
                      </p>
                    </Col>
                    <Col>
                      <img src={roles} alt="rolesImg" width={400} />
                    </Col>
                  </Row>
                </Accordion.Body>
              </Accordion.Item>
              <Accordion.Item eventKey="2">
                <Accordion.Header>
                  Control issues of your project
                </Accordion.Header>
                <Accordion.Body>
                  <Row className="">
                    <Col className="align-middle">
                      <h4 className="mb-4">Sort, filter and edit tasks</h4>
                      <p>
                        A page with a list of issues is a centralized and
                        organized display of problems, tasks, or topics that
                        need to be addressed within a project or workflow. It
                        serves as a comprehensive and visual representation of
                        the various challenges, bugs, enhancements, or ideas
                        that have been identified and logged by team members or
                        stakeholders.
                      </p>
                    </Col>
                    <Col>
                      <img src={issues} alt="issuesImg" width={400} />
                    </Col>
                  </Row>
                </Accordion.Body>
              </Accordion.Item>
              <Accordion.Item eventKey="3">
                <Accordion.Header>Check your assignements</Accordion.Header>
                <Accordion.Body>
                  <Row className="">
                    <Col className="align-middle">
                      <h4 className="mb-4">See your current work</h4>
                      <p>
                        A page with current tasks provides a centralized view of
                        the active and ongoing tasks within a project or
                        workflow. It serves as a dynamic and up-to-date
                        representation of the work that needs to be completed
                        you.
                      </p>
                    </Col>
                    <Col>
                      <img src={yourwork} alt="yourworkImg" width={400} />
                    </Col>
                  </Row>
                </Accordion.Body>
              </Accordion.Item>
            </Accordion>
          </Col>
          <Col>
            <div className="px-5">
              <h4 className="d-flex justify-content-start mb-5 ">
                About this project
              </h4>
              <h6 className="mb-4">
                Introducing an educational issue tracking app!
              </h6>
              <p>
                This app allows you to log and
                track tasks, bugs, or ideas, providing an overview of what needs
                to be addressed.
              </p>
              <p>
                Foster teamwork and collaboration by
                involving multiple users in issue management.
              </p>
              <p>
                Keep track of task statuses, due dates,
                and priority levels. With visual indicators, you'll always know
                the current state of each issue, enabling you to prioritize your
                efforts efficiently.
              </p>
            </div>
          </Col>
        </Row>
      </div>

      <div className="bg-dark p-0 text-light">
        <div className="container-lg">
          <h3 className="d-flex justify-content-start mt-5 py-5">
            REST endpoints
          </h3>

          <h5 className="bg-danger text-light p-2">Open endpoits</h5>
          <ul className="py-3">
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Get />
                /api/token
              </h6>
              <p>
                Get JWT token. Submit email and password inside request body.
              </p>
              <p>{'{"email": "***@mail.com", "password": "***"}'}</p>
            </li>
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Post />
                /api/users
              </h6>
              <p>Create new user.</p>
              <p>Required fields: email, password, firstName, lastName</p>
            </li>
          </ul>

          <h5 className="bg-danger text-light p-2">Require authentication</h5>
          <h5 style={{ color: "tomato" }}>User</h5>
          <ul className="py-3">
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Get /> /api/users
              </h6>
              <p>
                Get all users. Add project(id) to get users assigned to project.
                Id should be a number. Example: /api/users?project=1
              </p>
            </li>
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Get /> /api/users/details
              </h6>
              <p>Get details of current users.</p>
            </li>
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Update /> /api/users/:id
              </h6>
              <p>Update user by id.</p>
            </li>
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Patch /> /api/users/:id
              </h6>
              <p>Patch user by id.</p>
            </li>
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Delete /> /api/users/:id
              </h6>
              <p>Delete user by id.</p>
            </li>
          </ul>

          <h5 style={{ color: "tomato" }}>Roles</h5>
          <ul className="py-3">
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Get /> /api/roles/project
              </h6>
              <p>
                Get all roles for project. Add project(id) to get users assigned
                to project. Id should be a number. Example: /api/users?project=1
              </p>
            </li>
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Get /> /api/roles/:id
              </h6>
              <p>Get role by id.</p>
            </li>
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Post /> /api/roles
              </h6>
              <p>Create new role.</p>
            </li>
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Patch /> /api/roles/:id/role
              </h6>
              <p>Change role. Reques body should include new Role.</p>
            </li>
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Delete /> /api/roles/:id
              </h6>
              <p>Delete role by id.</p>
            </li>
          </ul>

          <h5 style={{ color: "tomato" }}>Projects</h5>
          <ul className="py-3">
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Get /> api/projects <span className="fw-normal text-light">(optional parameters: name, page, size, sort)</span> 
              </h6>
              <p>
                Get all projects. Request without parameters returns a page with all projects where user has assignment.
              </p>
            </li>
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Get /> /api/projects/:id
              </h6>
              <p>Get project by id.</p>
            </li>
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Post /> /api/projects
              </h6>
              <p>Create new project.</p>
            </li>
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Update /> /api/projects/:id
              </h6>
              <p>Update project.</p>
            </li>
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Delete /> /api/projects/:id
              </h6>
              <p>Delete project by id.</p>
            </li>
          </ul>

          <h5 style={{ color: "tomato" }}>Issues</h5>
          <ul className="py-3">
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Get />api/issues <span className="fw-normal text-light">(optional parameters: name, project, dueDate, type, status, priority, assignee(id), reporter(id), incomplete ,page, size, sort)</span> 
              </h6>
              <p>
                Get all issues. Request without parameters returns a page with all issues where user has assignment.
                <div>Parameters: name, type, status, priority, assignee and dueDate cannot be combined!</div> 
              </p>
            </li>
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Get />api/issues/assignee/:id <span className="fw-normal text-light">(optional parameters: project, incomplete ,page, size, sort)</span> 
              </h6>
              <p>
                Get all issues where user has role ASSIGNEE. Request without parameters returns issue page of all user projects.
              </p>
            </li>
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Get />api/issues/mywork <span className="fw-normal text-light">(optional parameters: project, incomplete ,page, size, sort)</span> 
              </h6>
              <p>
                Get all issues where user has role MANAGER or ASSIGNEE. Request without parameters returns issue page of all user projects.
              </p>
            </li>
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Get /> /api/issues/:id
              </h6>
              <p>Get issue by id.</p>
            </li>
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Post /> /api/issues
              </h6>
              <p>Create new issue.</p>
            </li>
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Update /> /api/projects/:id
              </h6>
              <p>Update issue.</p>
            </li>
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Patch /> /api/projects/:id
              </h6>
              <p>Patch issue.</p>
            </li>
            <li className="">
              <h6 className="font-monospace fw-semibold" style={{color: "moccasin"}}>
                <Delete /> /api/projects/:id
              </h6>
              <p>Delete issue by id.</p>
            </li>
          </ul>
        </div>
      </div>
    </div>
  );
}
