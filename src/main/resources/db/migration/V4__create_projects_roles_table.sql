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