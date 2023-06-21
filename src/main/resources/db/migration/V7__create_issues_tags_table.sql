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