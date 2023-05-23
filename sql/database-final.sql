create database if not exists `issuetracker`;
use `issuetracker`;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `users`;

create table `users` 
(
`id` BIGINT NOT NULL AUTO_INCREMENT,
`username` varchar(50) NOT NULL UNIQUE,
`email` varchar(50) NOT NULL UNIQUE,
`password` char(68) NOT NULL,
`enabled` boolean DEFAULT 1,
`first_name` varchar(50) NOT NULL,
`last_name` varchar(50) NOT NULL, 
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `projects`;

create table `projects` 
(
`id` bigint not null auto_increment,
`name` varchar(50) not null,
`description` text,
`status` char(4) default "OPEN",
`start_date` date,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `issues`;

create table `issues` 
(
`id` bigint not null auto_increment,
`name` varchar(50) not null,
`description` varchar(256),
`assignee` bigint,
`status` char(4) default "OPEN",
`start_date` date,
`project_id` bigint,
PRIMARY KEY (`id`),
CONSTRAINT `FK_issues_project_id` FOREIGN KEY (`project_id`) REFERENCES `projects`(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `users_roles`;

create table `users_roles` 
(
`id` BIGINT PRIMARY KEY AUTO_INCREMENT,
`user_id` BIGINT NOT NULL,
`type` varchar(50) NOT NULL,
UNIQUE KEY `UK_users_roles` (`user_id`,`type`),
CONSTRAINT `FK_users_roles_id` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `projects_roles`;

create table `projects_roles` 
(
`id` BIGINT PRIMARY KEY AUTO_INCREMENT,
`user_id` BIGINT NOT NULL,
`project_id` bigint NOT NULL,
`type` varchar(50) NOT NULL,
UNIQUE KEY `UK_project_roles` (`user_id`,`project_id`,`type`),
CONSTRAINT `FK_projects-roles_user_id` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`),
CONSTRAINT `FK_projects-roles_proj_id` FOREIGN KEY (`project_id`) REFERENCES `projects`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `issues_roles`;

create table `issues_roles` 
(
`id` BIGINT PRIMARY KEY AUTO_INCREMENT,
`user_id` BIGINT NOT NULL,
`issue_id` bigint NOT NULL,
`type` varchar(50) NOT NULL,
UNIQUE KEY `UK_issues_roles` (`user_id`,`issue_id`,`type`),
CONSTRAINT `FK_issues_roles_user_id` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`),
CONSTRAINT `FK_issues_roles_issue_id` FOREIGN KEY (`issue_id`) REFERENCES `issues`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `tags`;

create table `tags` 
(
`id` bigint NOT NULL auto_increment,
`name` varchar(50) NOT NULL UNIQUE,
primary key (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `issues_tags`;

CREATE TABLE `issues_tags` 
(
  `tag_id` bigint NOT NULL,
  `issue_id` bigint NOT NULL,
  PRIMARY KEY (`tag_id`,`issue_id`),
  CONSTRAINT `FK_issues_tags_TAG` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_issues_tags_ISSUE` FOREIGN KEY (`issue_id`) REFERENCES `issues` (`id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 0;