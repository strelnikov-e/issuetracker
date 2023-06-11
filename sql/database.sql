create database if not exists `issuetracker`;
use `issuetracker`;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `users`;

create table `users` 
(
`id` bigint not null auto_increment,
`username` varchar(50) not null,
`email` varchar(50),
`password` char(68),
`authorities` varchar(256),
`enabled` tinyint(1) default 1,
`first_name` varchar(50),
`last_name` varchar(50),
`company_name` varchar(50),
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `authorities`;

create table `authorities` 
(
`id` bigint not null auto_increment,
`authority` varchar(50) not null,
`user_id` bigint,
CONSTRAINT `FK_user_authorities` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



DROP TABLE IF EXISTS `projects`;

create table `projects` 
(
`id` bigint not null auto_increment,
`name` varchar(50) not null,
`project_key` varchar(16),
`description` text,
`manager` bigint,
`start_date` date,
`url` varchar(256),
`issue_count` int,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `issues`;

create table `issues` 
(
`id` bigint not null auto_increment,
`issue_key` varchar(24),
`name` varchar(68) not null,
`description` varchar(256),
`assignee` bigint,
`status` char(1),
`type` char(1),
`priority` char(1),
`start_date` date,
`due_date` date,
`close_date` date,
`project_id` bigint,
PRIMARY KEY (`id`),
CONSTRAINT `FK_issues_project_id` FOREIGN KEY (`project_id`) REFERENCES `projects`(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

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