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