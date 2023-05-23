use `issuetracker`;

SET FOREIGN_KEY_CHECKS = 0;

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