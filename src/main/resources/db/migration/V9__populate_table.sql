SET FOREIGN_KEY_CHECKS = 0;
INSERT INTO `users` (`email`, `password`, `first_name`, `last_name`)
VALUES
		("root@mail.com",
        "{bcrypt}$2a$10$dooeahdr86sqvX/Fkqi7wevRfNON4uyVPWowu4OTDvbPVSas/PWla",
        "Monica", "Hall", 0),
        ("admin@mail.com",
        "{bcrypt}$2a$10$dooeahdr86sqvX/Fkqi7wevRfNON4uyVPWowu4OTDvbPVSas/PWla",
        "Richard", "Hendricks", 2),
        ("viewer@mail.com","{bcrypt}$2a$10$1FY7wUp3KzJd5X4iBfMdu.Fq2MvbeSJdjb3iv/moTFxlWqRhxJ1DG",
        "Jared", "Dunn", 1);

INSERT INTO `users_roles` (`user_id`, `type`)
VALUES
        (1 , 'ROOT');

INSERT INTO `projects_roles` (`user_id`, `project_id`, `type`)
VALUES
        (2, 1, 'ADMIN'),
        (2, 2, 'MANAGER'),
        (3, 1, 'VIEWER');

INSERT INTO `issues_roles` (`user_id`, `issue_id`, `type`)
VALUES
        (3, 3, 'VIEWER');

INSERT INTO `projects` (`project_key`, `name`,`description`,`start_date`, `issue_count`)
VALUES
        ("IT","Issuetracker",
        "Apllication for tacking development process and registering bugs",
        "2023-04-11", 4),
        ("BLOG","Blog",
        "Dragt project for testing",
        "2023-04-22", 2);

INSERT INTO `issues` (`issue_key`, `name`,`description`,`status`, `type`, `priority`, `start_date`,`project_id`)
VALUES
        ("BT-1", "Create issues database",
        "Develop a database with field required to account issues for a specific project",
        "R", "T", "H","2023-04-12",1),
        ("BT-2", "Create projects database",
        "Develop a database with field required to account projects for an user",
        "P", "T", "H","2023-04-16",1),
        ("BT-3","Create users database",
        "Develop a database with user specific information",
        "P", "T", "L", "2023-05-02",1),
        ("BT-4","Design an UI",
        "Design a basic UI",
		"P", "T", "M", "2023-05-20",1),
        ("BLOG-1", "Create users database",
        "Develop a database with user specific information for blog",
        "O", "T", "M", "2023-05-22",2),
        ("BLOG-2", "Design user interface",
        "Develop a database with user specific information for blog",
        "O", "T", "M", "2023-05-29",2);


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