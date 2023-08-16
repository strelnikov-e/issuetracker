You can try this application here https://issuetracker-production.up.railway.app
Also there is frontend: https://issuetracker-client-production.up.railway.app

This API allows you to log and track tasks, bugs, or ideas, providing an overview of what needs to be addressed.

You can keep track of task statuses, due dates, and priority levels. 
To start working with application you can register new user.
Application implements tree-like role model with project roles: ADMIN, MANAGER, VIEWER and issue roles: ASSIGNEE, REPORTER. ADMIN has absolute access, MANAGER can edit any issue in project and VIEWER can see list of issues only. ASSIGNEE role let user to edit issue.



Open endpoits:

- GET/api/token
Get JWT token. Submit email and password inside request body.
{"email": "***@mail.com", "password": "***"}

- POST/api/users
Create new user.
Required fields: email, password, firstName, lastName

Require authentication

User
- GET /api/users
Get all users. Add project(id) to get users assigned to project. Id should be a number. Example: /api/users?project=1

- GET /api/users/details
Get details of current users.

- UPDATE /api/users/:id
Update user by id.

- PATCH /api/users/:id
Patch user by id.

- DELETE /api/users/:id
Delete user by id.

Roles
- GET /api/roles/project
Get all roles for project. Add project(id) to get users assigned to project. Id should be a number. Example: /api/users?project=1

- GET /api/roles/:id
Get role by id.

- POST /api/roles
Create new role.

- PATCH /api/roles/:id/role
Change role. Reques body should include new Role.

- DELETE /api/roles/:id
Delete role by id.

Projects
- GET api/projects (optional parameters: name, page, size, sort)
Get all projects. Request without parameters returns a page with all projects where user has assignment.

- GET /api/projects/:id
Get project by id.

- POST /api/projects
Create new project.

- UPDATE /api/projects/:id
Update project.

- DELETE /api/projects/:id
Delete project by id.

Issues
- GETapi/issues (optional parameters: name, project, dueDate, type, status, priority, assignee(id), reporter(id), incomplete ,page, size, sort)
Get all issues. Request without parameters returns a page with all issues where user has assignment.
Parameters: name, type, status, priority, assignee and dueDate cannot be combined!

- GET api/issues/assignee/:id (optional parameters: project, incomplete ,page, size, sort)
Get all issues where user has role ASSIGNEE. Request without parameters returns issue page of all user projects.

- GET api/issues/mywork (optional parameters: project, incomplete ,page, size, sort)
Get all issues where user has role MANAGER or ASSIGNEE. Request without parameters returns issue page of all user projects.

- GET /api/issues/:id
Get issue by id.

- POST /api/issues
Create new issue.

- UPDATE /api/projects/:id
Update issue.

- PATCH /api/projects/:id
Patch issue.

- DELETE /api/projects/:id
Delete issue by id.
