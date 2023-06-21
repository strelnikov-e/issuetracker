create table `users`
(
`id` BIGINT NOT NULL AUTO_INCREMENT,
`email` varchar(50) NOT NULL UNIQUE,
`password` varchar(128) NOT NULL,
`enabled` boolean DEFAULT 1,
`first_name` varchar(50) NOT NULL,
`last_name` varchar(50) NOT NULL, 
`company_name` varchar(50),
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;