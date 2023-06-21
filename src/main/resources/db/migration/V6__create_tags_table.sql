create table `tags`
(
`id` bigint NOT NULL auto_increment,
`name` varchar(50) NOT NULL UNIQUE,
primary key (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

