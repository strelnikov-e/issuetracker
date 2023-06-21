create table `projects`
(
`id` bigint not null auto_increment,
`name` varchar(50) not null,
`project_key` varchar(16) not null,
`description` text,
`start_date` date,
`url` varchar(256),
`issue_count` int,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
