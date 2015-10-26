# send_log data set

# --- !Ups

CREATE TABLE `SendLog` (
	`id` int(10) unsigned NOT NULL AUTO_INCREMENT,
	`campaign_id` int(10) unsigned NOT NULL,
	`ip` varchar(255) NOT NULL,
	`ua` varchar(255) NOT NULL,
	`created` datetime NOT NULL,
	`deleted` tinyint(1) NOT NULL DEFAULT '0',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

# --- !Downs

DROP TABLE `SendLog`;