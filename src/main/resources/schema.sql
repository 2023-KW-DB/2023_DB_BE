CREATE TABLE if not exists `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `user_type` int NOT NULL,
  `email` varchar(255) NULL DEFAULT NULL,
  `phone_number` varchar(255) NULL DEFAULT NULL,
  `weight` double NOT NULL DEFAULT 0,
  `age` int NOT NULL DEFAULT 0,
  `last_accessed_at` datetime NULL DEFAULT NULL,
  `total_money` int NOT NULL DEFAULT 50000,
  `fcm_token` varchar(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE if not exists `bikestationinformation`  (
  `lendplace_id` varchar(255) NOT NULL,
  `statn_addr1` varchar(255) NULL DEFAULT NULL,
  `statn_addr2` varchar(255) DEFAULT NULL,
  `startn_lat` double NULL DEFAULT NULL,
  `startn_lnt` double NULL DEFAULT NULL,
  `max_stands` double NOT NULL DEFAULT 20,
  `station_status` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`lendplace_id`)
);


CREATE TABLE if not exists `board` (
   `id` int NOT NULL AUTO_INCREMENT,
  `category_id` int NOT NULL,
  `user_id` int NOT NULL,
  `views` int NOT NULL DEFAULT 0,
  `title` varchar(255) NOT NULL,
  `content` varchar(1024) NOT NULL,
  `notice` boolean NOT NULL DEFAULT FALSE,
  `file_name` varchar(255) NULL DEFAULT NULL,
  `url` varchar(255) NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT NULL,
  `updated_at` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `category_id`),
  CONSTRAINT `board_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);



CREATE TABLE if not exists `ticket`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `ticket_price` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE if not exists `coupon` (
	`value` varchar(255) UNIQUE NOT NULL,
	`is_used` int NULL DEFAULT NULL,
	`ticket_id` int NOT NULL,
    PRIMARY KEY (`value`),
    CONSTRAINT `coupon_ibfk_1` FOREIGN KEY (`ticket_id`) REFERENCES `ticket` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE if not exists `bike`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `lendplace_id` varchar(255) NULL DEFAULT NULL,
  `use_status` int NULL DEFAULT NULL,
  `bike_status` int NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  CONSTRAINT `bike_ibfk_1` FOREIGN KEY (`lendplace_id`) REFERENCES `bikestationinformation` (`lendplace_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
);



CREATE TABLE if not exists `comment`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `write_id` int NOT NULL,
  `category_id` int NOT NULL,
  `content` varchar(255) NOT NULL,
  `created_at` datetime NULL DEFAULT NULL,
  `updated_at` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`write_id`, `category_id`) REFERENCES `board` (`id`, `category_id`) ON DELETE CASCADE ON UPDATE CASCADE
);



CREATE TABLE if not exists `favorite`  (
  `user_id` int NOT NULL,
  `lendplace_id` varchar(255) NOT NULL,
  PRIMARY KEY(`user_id`, `lendplace_id`),
  CONSTRAINT `favorite_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `favorite_ibfk_2` FOREIGN KEY (`lendplace_id`) REFERENCES `bikestationinformation` (`lendplace_id`) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE if not exists `paymenthistory`  (
  `history_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `ticket_id` int NOT NULL,
  `is_used` int NULL DEFAULT NULL,
  `registration_at` datetime NULL DEFAULT NULL,
  PRIMARY KEY(`history_id`),
  CONSTRAINT `paymenthistory_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `paymenthistory_ibfk_2` FOREIGN KEY (`ticket_id`) REFERENCES `ticket` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE if not exists `userlog`  (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `bike_id` int NULL DEFAULT NULL,
  `history_id` int NULL DEFAULT NULL,
  `departure_station` varchar(255) NULL DEFAULT NULL,
  `arrival_station` varchar(255) NULL DEFAULT NULL,
  `departure_time` datetime NULL DEFAULT NULL,
  `arrival_time` datetime NULL DEFAULT NULL,
  `use_time` int NULL DEFAULT NULL,
  `use_distance` int NULL DEFAULT NULL,
  `return_status` int NULL DEFAULT NULL,
  PRIMARY KEY(`log_id`),
  CONSTRAINT `userlog_ibfk_1` FOREIGN KEY (`user_id`)
  REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `userlog_ibfk_2` FOREIGN KEY (`departure_station`)
  REFERENCES `bikestationinformation` (`lendplace_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `userlog_ibfk_3` FOREIGN KEY (`arrival_station`)
  REFERENCES `bikestationinformation` (`lendplace_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `userlog_ibfk_4` FOREIGN KEY (`bike_id`)
  REFERENCES `bike` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `userlog_ibfk_5` FOREIGN KEY (`history_id`)
  REFERENCES `paymenthistory` (`history_id`) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE if not exists `board_like` (
  `user_id` int NOT NULL,
    `category_id` int NOT NULL,
  `liked_id` int NOT NULL,
  UNIQUE (`user_id`, `liked_id`),
  CONSTRAINT `board_like_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `board_like_ibfk_2` FOREIGN KEY (`liked_id`, `category_id`) REFERENCES
    `board` (`id`, `category_id`) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE if not exists `comment_like` (
  `user_id` int NOT NULL,
  `liked_id` int NOT NULL,
  UNIQUE (`user_id`, `liked_id`),
  CONSTRAINT `comment_like_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `comment_like_ibfk_2` FOREIGN KEY (`liked_id`) REFERENCES
	`comment` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE if not exists `bikestationrating`  (
  `lendplace_id` varchar(255) NOT NULL,
  `user_id` int NOT NULL,
  `rating` int NOT NULL,
  `review` varchar(255) NULL DEFAULT NULL,
  CONSTRAINT `bikestationrating_ibfk_1` FOREIGN KEY (`lendplace_id`) REFERENCES `bikestationinformation` (`lendplace_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `bikestationrating_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE if not exists `emailauth`  (
  `user_id` int NOT NULL,
  `auth_num` int NOT NULL,
  `created_at` datetime NULL DEFAULT NULL,
  CONSTRAINT `emailauth_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);