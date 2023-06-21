create table `users_roles`
(
`id` BIGINT PRIMARY KEY AUTO_INCREMENT,
`user_id` BIGINT NOT NULL,
`type` varchar(50) NOT NULL,
UNIQUE KEY `UK_users_roles` (`user_id`,`type`),
CONSTRAINT `FK_users_roles_id` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;