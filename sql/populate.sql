use `issuetracker`;

SET FOREIGN_KEY_CHECKS = 0;
INSERT INTO `users` (`email`, `password`, `first_name`, `last_name`, `current_project`)
VALUES
		("evgeny@test.com",
        "{bcrypt}$2a$10$dooeahdr86sqvX/Fkqi7wevRfNON4uyVPWowu4OTDvbPVSas/PWla",
        "Evgeny", "Strelnikov", 0),
		("ryan@test.com",
        "{bcrypt}$2a$10$dooeahdr86sqvX/Fkqi7wevRfNON4uyVPWowu4OTDvbPVSas/PWla",
        "Ryan", "Edwards", 1),
        ("jared@test.com",
        "{bcrypt}$2a$10$dooeahdr86sqvX/Fkqi7wevRfNON4uyVPWowu4OTDvbPVSas/PWla",
        "Jared", "Dunn", 1),
        ("sandra@test.com","{bcrypt}$2a$10$1FY7wUp3KzJd5X4iBfMdu.Fq2MvbeSJdjb3iv/moTFxlWqRhxJ1DG",
        "Sandra", "Clarke", 2),
        ("alexandre@test.com","{bcrypt}$2a$10$1FY7wUp3KzJd5X4iBfMdu.Fq2MvbeSJdjb3iv/moTFxlWqRhxJ1DG",
        "Alexandre", "Olivier", 1),
        ("karim@test.com","{bcrypt}$2a$10$1FY7wUp3KzJd5X4iBfMdu.Fq2MvbeSJdjb3iv/moTFxlWqRhxJ1DG",
        "Karim", "Chandran", 1),
        ("jian@test.com","{bcrypt}$2a$10$1FY7wUp3KzJd5X4iBfMdu.Fq2MvbeSJdjb3iv/moTFxlWqRhxJ1DG",
        "Jian", "Yang", 1),
        ("larry@test.com","{bcrypt}$2a$10$1FY7wUp3KzJd5X4iBfMdu.Fq2MvbeSJdjb3iv/moTFxlWqRhxJ1DG",
        "Larry", "Hill", 2),
        ("monica@test.com","{bcrypt}$2a$10$1FY7wUp3KzJd5X4iBfMdu.Fq2MvbeSJdjb3iv/moTFxlWqRhxJ1DG",
        "Monica", "Hall", 2);
        
INSERT INTO `users_roles` (`user_id`, `role`)
VALUES
        (1 , 'ROOT');

INSERT INTO `projects_roles` (`user_id`, `project_id`, `role`)
VALUES
        (2, 1, 'ADMIN'),
        (3, 1, 'MANAGER'),
        (4, 1, 'VIEWER'),
        (5, 1, 'VIEWER'),
        (6, 1, 'VIEWER'),
        (7, 1, 'VIEWER'),

        (8, 2, 'ADMIN'),
        (9, 2, 'MANAGER'),
        (1, 2, 'MANAGER'),
        (2, 2, 'VIEWER'),
        (3, 2, 'VIEWER'),
        
        (3, 3, 'ADMIN'),
        (2, 3, 'MANAGER'),
        (1, 3, 'VIEWER');
        
INSERT INTO `issues_roles` (`user_id`, `issue_id`, `role`)
VALUES
		(2, 1, 'REPORTER'),
        (2, 2, 'REPORTER'),
        (2, 3, 'REPORTER'),
        (2, 4, 'REPORTER'),
        (7, 5, 'REPORTER'),
        (8, 6, 'REPORTER'),
        (3, 7, 'REPORTER'),
        (4, 8, 'REPORTER'),
        (7, 9, 'REPORTER'),
        (3, 10, 'REPORTER'),
        (5, 11, 'REPORTER'),
        (6, 12, 'REPORTER'),
        (4, 13, 'REPORTER'),
        (5, 14, 'REPORTER'),
        (8, 15, 'REPORTER'),
        (2, 16, 'REPORTER'),
        (4, 17, 'REPORTER'),
        (3, 18, 'REPORTER'),
        (5, 19, 'REPORTER'),
        (2, 20, 'REPORTER'),
        (7, 21, 'REPORTER'),
        (5, 22, 'REPORTER'),
        (5, 23, 'REPORTER'),
        (2, 24, 'REPORTER'),
        (3, 25, 'REPORTER'),
        (2, 26, 'REPORTER'),
        (3, 27, 'REPORTER'),
        (3, 28, 'REPORTER'),

        (7, 1, 'ASSIGNEE'),
        (5, 2, 'ASSIGNEE'),
        (6, 3, 'ASSIGNEE'),
        (8, 4, 'ASSIGNEE'),
        (3, 5, 'ASSIGNEE'),
        (5, 6, 'ASSIGNEE'),
        (3, 7, 'ASSIGNEE'),
        (4, 8, 'ASSIGNEE'),
        (5, 9, 'ASSIGNEE'),
        (3, 10, 'ASSIGNEE'),
        (3, 11, 'ASSIGNEE'),
        (5, 12, 'ASSIGNEE'),
        (6, 13, 'ASSIGNEE'),
        (3, 14, 'ASSIGNEE'),
        (8, 15, 'ASSIGNEE'),
        (4, 16, 'ASSIGNEE'),
        (3, 17, 'ASSIGNEE'),
        (4, 18, 'ASSIGNEE'),
        (5, 19, 'ASSIGNEE'),
        (3, 20, 'ASSIGNEE'),
        (7, 21, 'ASSIGNEE'),
        (3, 22, 'ASSIGNEE'),
        (5, 23, 'ASSIGNEE'),
        (4, 24, 'ASSIGNEE'),
        (7, 25, 'ASSIGNEE'),
        (4, 26, 'ASSIGNEE'),
        (3, 27, 'ASSIGNEE'),
        (3, 28, 'ASSIGNEE');

INSERT INTO `projects` (`project_key`, `name`,`description`,`start_date`, `issue_count`)
VALUES
        ("IT","Issuetracker",
        "Apllication for tacking development process and registering bugs",
        "2023-04-11", 28),
        ("TB","Technology Blog",
        "Dragt project for testing",
        "2023-01-18", 2),
        ("IAM","Investment activity monitor",
        "Dragt project for testing",
        "2023-06-23", 0);
        
INSERT INTO `issues` (`issue_key`, `name`,`description`,`status`, `type`, `priority`, `start_date`, `due_date`, `project_id`, `parent_issue`)
VALUES
		("IT-1","Develop a backend",
        "Develop application with REST API. It should perform CRUD operations",
		"P", "M", "M", "2023-04-11","2023-07-30",1, 0),
        ("IT-2", "Create issues database",
        "Develop a database with field required to account issues for a specific project",
        "D", "T", "H","2023-04-12","2023-04-20",1,1),
        ("IT-3", "Create projects database",
        "Develop a database with field required to account projects for an user",
        "D", "T", "H","2023-04-12","2023-04-20",1,1),
        ("IT-4","Design entity classes",
        "Design entity classes",
		"D", "T", "M", "2023-04-15","2023-04-20",1,1),
        ("IT-5","Design service layer",
        "Design servise layer",
		"R", "T", "M", "2023-04-16","2023-04-22",1,1),
        ("IT-6","Design rest controllers",
        "Design rest controllers",
		"R", "T", "H", "2023-04-20","2023-04-26",1,1),
        ("IT-7","Implement custom exception handling",
        "Cover basic exceptions which can happen during CRUD operations",
		"R", "T", "L", "2023-04-23","2023-04-27",1,1),
        ("IT-8","Implement HATEOAS",
        "Add HATEOAS links to get responses",
		"P", "T", "M", "2023-04-26","2023-04-29",1,1),
        
        
        ("IT-9","Design frontend with React",
        "To develop a frontend I choose to user react as a modern library for creating UI",
        "P", "M", "M", "2023-05-01","2023-07-30",1,0),
        ("IT-10","Develop a basic page layout",
        "Develop a basic page layout",
		"D", "T", "M", "2023-05-12","2023-05-14",1,9),
        ("IT-11","Choose style",
        "Look similar project and choose one to follow",
		"D", "T", "L", "2023-05-13","2023-05-14",1,9),
        ("IT-12","Design a project page",
        "It should has a list of projects and links to project details",
		"R", "T", "M", "2023-05-15","2023-05-18",1,9),
        ("IT-13","Design an issue page",
        "List of issues and ability to sort and filter",
		"P", "T", "M", "2023-05-18","2023-05-24",1,9),
        ("IT-14","Design a login and registration pages",
        "Registration form should has form validation",
		"D", "T", "L", "2023-04-26","2023-04-29",1,9),
        
        ("IT-15","Add security",
        "Use Spring security to secure rest endpoits. Develop a role model,",
        "R", "M", "M", "2023-05-14","2023-06-07",1,0),
        ("IT-16","Implement non flat user role model",
        "Roles: Project (ADMIN, MANAGER, VIEWER), ISSUE (ASSIGNEE, REPORTER, VIEWER)",
        "D", "T", "H", "2023-05-14","2023-06-07",1,15),
        ("IT-17","Login with JWT token",
        "Use JWT token for authentication",
        "P", "T", "H", "2023-05-14","2023-06-07",1,15),
        
        
        ("IT-19","Issue list unique key issue",
        "Warning: Each child in a list should have a unique 'key' prop.Check the render method of `IssueList",
        "O", "B", "L", "2023-06-21","2023-06-27",1,9),
        ("IT-20","Remove 'show all' toggle for MANAGERS",
        "MANAGER can see change all the issues in his project. Remove toggle conditionally (leave for VIEWERS)",
        "O", "B", "L", "2023-06-30","2023-07-10",1,9),
        ("IT-21","Add project cards to Your Work page",
        "Cards should include quantity of issues assigned to the users and general project information",
        "O", "T", "L", "2023-07-03","2023-07-15",1,9),
        ("IT-22","Make boards responsive",
        "Due to bug with ReactBeautiful Dnd, if put boards in a 'd-flex flex-row flex-nowrap overflow-auto container' it stop responding to move object to empty card",
        "O", "B", "L", "2023-07-11","2023-07-20",1,9),
        ("IT-23","Make a home page",
        "Make a home page with app overview and rest endpoints",
        "P", "T", "H", "2023-07-11","2023-07-15",1,9),
        ("IT-24","Done board should include only 10 last issues",
        "Done board could grow very fast",
        "P", "T", "M", "2023-07-09","2023-07-15",1,9),
        ("IT-25","Add pagination to the boards",
        "There is should be some restriction to the boards",
        "O", "T", "L", "2023-07-10","2023-07-30",1,9),
        ("IT-26","Prepare whole project to first BETA",
        "Clean code and make backend and frontend ready for first beta version",
        "O", "T", "H", "2023-07-11","2023-07-12",1,0),
        ("IT-27","Load to GitHub",
        "eview project and upload project to github",
        "O", "T", "H", "2023-07-11","2023-07-20",1,0),
        ("IT-28","Deplow app to the cloud",
        "Deploy app to the cloud",
        "O", "T", "H", "2023-07-11","2023-07-20",1,0),
        
        
        
        
        ("TB-1", "Identify targets of the project ",
        "What functionality should it has? ",
        "O", "T", "L", "2023-07-01","2023-10-01",2,0),
        ("TB-2", "Make a sketch of UI",
        "Choose a style and general arrangement of elements on screen",
        "O", "T", "L", "2023-07-01","2023-10-01",2,0);
        

INSERT INTO `tags` (`name`)
VALUES
        ("database"),
        ("design"),
        ("issues"),
        ("projects"),
        ("users"),
        ("UI");

INSERT INTO `issues_tags`
VALUES
        (1,1),
        (1,2),
        (1,3),
        (2,1),
        (2,2),
        (3,1);

SET FOREIGN_KEY_CHECKS = 1;