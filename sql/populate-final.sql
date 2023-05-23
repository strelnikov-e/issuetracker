use `issuetracker`;

SET FOREIGN_KEY_CHECKS = 0;

INSERT INTO `users` (`username`,`email`,`password`, `enabled`, `first_name`,`last_name`)
VALUES
		("root","root@mail.com","{bcrypt}$2a$12$nfqMxM39kfD5mS04Y5ChcujGQPZBWyenO/rbqT79pRuALP.Zrx.H.",1 ,"root","user"),
        ("john","john@mail.com","{bcrypt}$2a$12$nfqMxM39kfD5mS04Y5ChcujGQPZBWyenO/rbqT79pRuALP.Zrx.H.",1 ,"John","Doe"),
        ("mary","mary@mail.com","{bcrypt}$2a$12$nfqMxM39kfD5mS04Y5ChcujGQPZBWyenO/rbqT79pRuALP.Zrx.H.",1 ,"Mary","Jane"),
        ("susan","susan@mail.com","{bcrypt}$2a$12$nfqMxM39kfD5mS04Y5ChcujGQPZBWyenO/rbqT79pRuALP.Zrx.H.",1 ,"Susan","Park");

INSERT INTO `projects` (`name`,`description`,`status`,`start_date`)
VALUES
        ("Bugtracker",
        "Training project with minimal functionality to track project issues and plan development process",
        "PROG","2023-04-11"),
        ("Blog",
        "Demo blog project",
        "OPEN","2023-04-22");
        
INSERT INTO `issues` (`name`,`description`,`assignee`,`status`,`start_date`,`project_id`)
VALUES
        ("Create issues database",
        "Develop a database with field required to account issues for a specific project",
        "1","OPEN","2023-04-11",1),
        ("Create projects database",
        "Develop a database with field required to account projects for an user",
        1,"OPEN","2023-04-11",1),
        ("Create users database",
        "Develop a database with user specific information",
        2,"OPEN","2023-04-11",1),
        ("Create users database",
        "Develop a database with user specific information for blog",
        1,"OPEN","2023-04-22",2),
        ("Design user interface",
        "Develop a database with user specific information for blog",
        1,"OPEN","2023-04-22",2);
        
INSERT INTO `users_roles` (`user_id`, `type`)
VALUES
		(1,"ROOT");

INSERT INTO `projects_roles` (`user_id`, `project_id`, `type`)
VALUES
		(2,1,"ADMIN"),
        (3,2,"ADMIN"),
        (4,1,"MANAGER");
        
INSERT INTO `issues_roles` (`user_id`, `issue_id`, `type`)
VALUES
		(2,1,"VIEWER"),
        (2,2,"VIEWER"),
        (2,3,"VIEWER"),
        (2,4,"ASSIGNEE"),
        (2,5,"REPORTER"),
        (3,1,"ASSIGNEE"),
        (3,2,"REPORTER"),
        (3,3,"VIEWER"),
        (3,4,"VIEWER"),
        (3,5,"VIEWER"),
        (4,1,"VIEWER"),
        (4,2,"VIEWER"),
        (4,3,"VIEWER");

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