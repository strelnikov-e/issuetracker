use `issuetracker`;

SET FOREIGN_KEY_CHECKS = 0;
INSERT INTO `users` (`email`, `password`, `authorities`, `first_name`, `last_name`)
VALUES
        ("admin@mail.com",
        "{bcrypt}$2a$10$dooeahdr86sqvX/Fkqi7wevRfNON4uyVPWowu4OTDvbPVSas/PWla",
        "ROLE_ADMIN",
        "John", "Doe"),
        ("viewer@mail.com","{bcrypt}$2a$10$1FY7wUp3KzJd5X4iBfMdu.Fq2MvbeSJdjb3iv/moTFxlWqRhxJ1DG",
        "ROLE_USER",
        "Jane", "Doe");
        
INSERT INTO `authorities` (`authority`, `user_id`)
VALUES
        ("ROLE_STUDENT",1);
        
INSERT INTO `users_roles` (`user_id`, `type`)
VALUES
        (1 , 'ROOT');

INSERT INTO `projects_roles` (`user_id`, `project_id`, `type`)
VALUES
        (1, 1, 'ADMIN'),
        (1, 2, 'MANAGER'),
        (2, 1, 'VIEWER');
        
INSERT INTO `issues_roles` (`user_id`, `issue_id`, `type`)
VALUES
        (2, 1, 'ASSIGNEE'),
        (2, 2, 'REPORTER'),
        (2, 3, 'VIEWER');

INSERT INTO `projects` (`project_key`, `name`,`description`,`start_date`, `issue_count`)
VALUES
        ("BT","Bugtracker",
        "Training project with minimal functionality to track project issues and plan development process",
        "2023-04-11", 3),
        ("BLOG","Blog",
        "Demo blog project",
        "2023-04-22", 2);
        
INSERT INTO `issues` (`issue_key`, `name`,`description`,`assignee`,`status`, `type`, `priority`, `start_date`,`project_id`)
VALUES
        ("BT-1", "Create issues database",
        "Develop a database with field required to account issues for a specific project",
        "1","R", "T", "H","2023-04-12",1),
        ("BT-2", "Create projects database",
        "Develop a database with field required to account projects for an user",
        1,"P", "T", "H","2023-04-16",1),
        ("BT-3","Create users database",
        "Develop a database with user specific information",
        2,"P", "T", "L", "2023-05-02",1),
        ("BT-4","Design an UI",
        "Design a basic UI",
        2,"P", "T", "M", "2023-05-20",1),
        ("BLOG-1", "Create users database",
        "Develop a database with user specific information for blog",
        1,"O", "T", "M", "2023-05-22",2),
        ("BLOG-2", "Design user interface",
        "Develop a database with user specific information for blog",
        1,"O", "T", "M", "2023-05-29",2);
        

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