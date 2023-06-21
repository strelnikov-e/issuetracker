create table `issues`
(
`id` bigint not null auto_increment,
`issue_key` varchar(24) not null,
`name` varchar(128) not null,
`description` text,
`project_id` bigint,
`parent_issue` bigint default 0,
`status` char(1) default 'O' not null,
`type` char(1) default 'T' not null,
`priority` char(1) default 'M' not null,
`start_date` date,
`due_date` date,
`close_date` date,
PRIMARY KEY (`id`),
CONSTRAINT `FK_issues_project_id` FOREIGN KEY (`project_id`) REFERENCES `projects`(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;