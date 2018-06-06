-- --------------------------------------------------------
-- Хост:                         127.0.0.1
-- Версия сервера:               10.2.15-MariaDB - mariadb.org binary distribution
-- Операционная система:         Win64
-- HeidiSQL Версия:              9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Дамп структуры базы данных messenger
DROP DATABASE IF EXISTS `messenger`;
CREATE DATABASE IF NOT EXISTS `messenger` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `messenger`;

-- Дамп структуры для таблица messenger.conversations
DROP TABLE IF EXISTS `conversations`;
CREATE TABLE IF NOT EXISTS `conversations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_id` int(11) DEFAULT NULL,
  `title` varchar(40) DEFAULT NULL,
  `participants_id` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `idx_conversations_admin_id` (`admin_id`),
  CONSTRAINT `fk_conversations_users` FOREIGN KEY (`admin_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- Дамп данных таблицы messenger.conversations: ~14 rows (приблизительно)
DELETE FROM `conversations`;
/*!40000 ALTER TABLE `conversations` DISABLE KEYS */;
INSERT INTO `conversations` (`id`, `admin_id`, `title`, `participants_id`, `created_at`) VALUES
	(1, 5, 'HelloWorld', NULL, '2018-05-09 17:10:05'),
	(2, 2, 'Conference', NULL, '2018-05-19 14:27:04'),
	(8, 7, 'advsa', NULL, '2018-05-19 14:56:23'),
	(9, 7, 'qwert', NULL, '2018-05-27 14:56:15'),
	(10, 11, NULL, NULL, '2018-05-28 18:02:35'),
	(11, 9, NULL, NULL, '2018-05-28 18:03:12'),
	(12, 3, NULL, NULL, '2018-05-28 18:03:22'),
	(19, 9, 'conversation1', NULL, '2018-05-31 10:40:48'),
	(20, 9, 'dreamteam', NULL, '2018-05-31 17:03:29'),
	(21, 9, 'dreamteam2', NULL, '2018-05-31 17:04:05'),
	(22, 9, 'dreamteam3', NULL, '2018-05-31 17:05:06'),
	(23, 9, 'dreamteam3', NULL, '2018-05-31 17:06:03'),
	(25, 1, '', NULL, '2018-06-04 18:48:12'),
	(26, 9, '', NULL, '2018-06-06 18:08:20');
/*!40000 ALTER TABLE `conversations` ENABLE KEYS */;

-- Дамп структуры для таблица messenger.deleted_conversations
DROP TABLE IF EXISTS `deleted_conversations`;
CREATE TABLE IF NOT EXISTS `deleted_conversations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `conversation_id` int(11) DEFAULT NULL,
  `deleted_at` datetime DEFAULT current_timestamp(),
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_deleted_conversations_conversation_id` (`conversation_id`),
  CONSTRAINT `fk_deleted_conversations` FOREIGN KEY (`conversation_id`) REFERENCES `conversations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Дамп данных таблицы messenger.deleted_conversations: ~2 rows (приблизительно)
DELETE FROM `deleted_conversations`;
/*!40000 ALTER TABLE `deleted_conversations` DISABLE KEYS */;
INSERT INTO `deleted_conversations` (`id`, `conversation_id`, `deleted_at`, `user_id`) VALUES
	(1, 8, '2018-05-19 15:04:00', 8),
	(2, 1, '2018-05-19 15:26:57', 3);
/*!40000 ALTER TABLE `deleted_conversations` ENABLE KEYS */;

-- Дамп структуры для таблица messenger.messages
DROP TABLE IF EXISTS `messages`;
CREATE TABLE IF NOT EXISTS `messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `conversation_id` int(11) DEFAULT NULL,
  `from_id` int(11) DEFAULT NULL,
  `to_id` int(11) DEFAULT NULL,
  `message` longtext DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp(),
  `attachment_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_messages_conversation_id` (`conversation_id`),
  KEY `idx_messages_attachment_url` (`attachment_id`),
  CONSTRAINT `fk_messages_photos` FOREIGN KEY (`attachment_id`) REFERENCES `photos` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `messages_conversations` FOREIGN KEY (`conversation_id`) REFERENCES `conversations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- Дамп данных таблицы messenger.messages: ~27 rows (приблизительно)
DELETE FROM `messages`;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` (`id`, `conversation_id`, `from_id`, `to_id`, `message`, `created_at`, `attachment_id`) VALUES
	(1, 1, 5, 9, 'Hello', '2018-05-09 17:38:08', 3),
	(2, 2, 6, 2, 'asdfghjkl', '2018-05-19 14:27:05', 4),
	(8, 9, 7, 3, '"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."', '2018-05-28 18:07:45', NULL),
	(9, 10, 11, 9, '"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."', '2018-05-28 18:09:37', NULL),
	(10, 1, 8, 9, '"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."', '2018-05-28 18:10:37', NULL),
	(11, 1, 6, 9, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum', '2018-05-28 18:11:41', NULL),
	(12, 1, 2, 9, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum', '2018-05-28 18:11:53', NULL),
	(13, 1, 6, 9, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum', '2018-05-28 18:12:06', NULL),
	(14, 10, 9, 9, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum', '2018-05-28 18:12:11', NULL),
	(15, 11, 10, 9, '"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."', '2018-05-28 18:12:37', NULL),
	(16, 11, 9, 9, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum', '2018-05-28 18:13:02', NULL),
	(17, 11, 9, 9, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum', '2018-05-28 18:13:17', NULL),
	(18, 8, 3, 9, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum', '2018-05-28 18:17:45', NULL),
	(19, 8, 7, 9, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum', '2018-05-28 18:17:57', NULL),
	(20, 8, 9, 9, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum', '2018-05-28 18:18:12', NULL),
	(21, 21, 9, NULL, 'Conversation has started', '2018-05-31 17:04:08', 14),
	(22, 22, 9, NULL, 'Conversation has started', '2018-05-31 17:05:06', 14),
	(23, 23, 9, 9, 'Conversation has started', '2018-05-31 17:06:04', 14),
	(24, 10, 9, 9, 'wazzzzap', '2018-06-03 18:01:47', 14),
	(25, 11, 9, 9, 'ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,', '2018-06-03 18:11:15', 14),
	(26, 11, 9, 9, 'at non proident, sunt in culpa qui officia', '2018-06-03 18:12:10', 14),
	(27, 10, 9, 9, 'asdfsdaf', '2018-06-03 18:14:01', 14),
	(28, 10, 9, 9, 'sdfgdf', '2018-06-03 18:16:15', 14),
	(29, 10, 9, 9, 'dsgfgfd', '2018-06-03 18:17:52', 14),
	(37, 25, 1, 1, 'Conversation has started', '2018-06-04 18:48:12', 14),
	(38, 25, 1, 1, 'Здарова', '2018-06-04 18:48:19', 14),
	(39, 26, 9, 9, 'Conversation has started', '2018-06-06 18:08:23', 14);
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;

-- Дамп структуры для таблица messenger.participants
DROP TABLE IF EXISTS `participants`;
CREATE TABLE IF NOT EXISTS `participants` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `conversation_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `unread_messages` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_participants_conversation_id` (`conversation_id`),
  CONSTRAINT `participants_conversations` FOREIGN KEY (`conversation_id`) REFERENCES `conversations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8;

-- Дамп данных таблицы messenger.participants: ~37 rows (приблизительно)
DELETE FROM `participants`;
/*!40000 ALTER TABLE `participants` DISABLE KEYS */;
INSERT INTO `participants` (`id`, `conversation_id`, `user_id`, `unread_messages`) VALUES
	(1, 1, 2, 3),
	(2, 1, 3, 5),
	(3, 1, 5, 0),
	(4, 1, 6, 0),
	(5, 1, 8, 0),
	(6, 1, 7, 0),
	(8, 2, 6, 0),
	(19, 2, 2, 0),
	(20, 8, 3, 0),
	(21, 8, 7, 0),
	(23, 8, 8, 0),
	(24, 9, 3, 0),
	(25, 9, 7, 0),
	(26, 9, 6, 0),
	(27, 10, 9, 0),
	(28, 10, 11, 4),
	(29, 11, 9, 0),
	(30, 11, 10, 0),
	(31, 12, 3, 0),
	(32, 12, 5, 0),
	(33, 1, 9, 0),
	(34, 8, 9, 0),
	(40, 19, 14, 0),
	(41, 19, 17, 0),
	(42, 20, 14, 0),
	(43, 20, 17, 0),
	(44, 21, 14, 0),
	(45, 21, 17, 0),
	(46, 22, 14, 0),
	(47, 22, 17, 0),
	(48, 23, 9, 0),
	(49, 23, 14, 0),
	(50, 23, 17, 0),
	(57, 25, 1, 0),
	(58, 25, 8, 2),
	(59, 26, 9, 0),
	(60, 26, 10, 1);
/*!40000 ALTER TABLE `participants` ENABLE KEYS */;

-- Дамп структуры для таблица messenger.photos
DROP TABLE IF EXISTS `photos`;
CREATE TABLE IF NOT EXISTS `photos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- Дамп данных таблицы messenger.photos: ~17 rows (приблизительно)
DELETE FROM `photos`;
/*!40000 ALTER TABLE `photos` DISABLE KEYS */;
INSERT INTO `photos` (`id`, `url`) VALUES
	(1, 'img/defaults/image001.jpg'),
	(2, 'img/defaults/image002.jpg'),
	(3, 'img/defaults/image003.jpg'),
	(4, 'img/defaults/image004.jpg'),
	(5, 'img/defaults/image005.jpg'),
	(6, 'img/defaults/image006.jpg'),
	(7, 'img/defaults/image007.jpg'),
	(8, 'img/defaults/image008.jpg'),
	(9, 'img/defaults/image009.jpg'),
	(10, 'img/defaults/image010.jpg'),
	(11, 'img/defaults/image011.jpg'),
	(12, 'img/defaults/image012.jpg'),
	(13, 'img/profiles/my.jpg'),
	(14, ''),
	(16, 'Online'),
	(17, '10'),
	(18, '17');
/*!40000 ALTER TABLE `photos` ENABLE KEYS */;

-- Дамп структуры для таблица messenger.status
DROP TABLE IF EXISTS `status`;
CREATE TABLE IF NOT EXISTS `status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` enum('Online','Idle','Do Not Disturb','Offline') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- Дамп данных таблицы messenger.status: ~4 rows (приблизительно)
DELETE FROM `status`;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` (`id`, `value`) VALUES
	(1, 'Online'),
	(2, 'Idle'),
	(3, 'Do Not Disturb'),
	(4, 'Offline');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;

-- Дамп структуры для таблица messenger.users
DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `login` varchar(64) NOT NULL DEFAULT '',
  `password` varchar(64) DEFAULT NULL,
  `first_name` varchar(64) DEFAULT NULL,
  `last_name` varchar(64) DEFAULT NULL,
  `sex` enum('male','female','not specified') DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp(),
  `status` int(11) DEFAULT NULL,
  `avatar` int(11) DEFAULT NULL,
  `activation_code` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login` (`login`),
  UNIQUE KEY `email` (`email`),
  KEY `idx_users_avatar` (`avatar`),
  KEY `idx_users_status` (`status`),
  CONSTRAINT `fk_users_photos` FOREIGN KEY (`avatar`) REFERENCES `photos` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_status` FOREIGN KEY (`status`) REFERENCES `status` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- Дамп данных таблицы messenger.users: ~17 rows (приблизительно)
DELETE FROM `users`;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`, `email`, `login`, `password`, `first_name`, `last_name`, `sex`, `created_at`, `status`, `avatar`, `activation_code`) VALUES
	(1, 'best@people.math', 'BestGuy', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Sergey', 'Kovalenko', 'male', '1998-05-28 19:21:10', 1, 13, NULL),
	(2, 'wfcenzbu@emlpro.com', 'Gloria', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Anna', 'Emelyanova', 'female', '2018-05-08 17:05:46', 1, 1, NULL),
	(3, 'qxfuvsao@yomail.info', 'tanya1', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Tanya', 'Sergeta', 'female', '2018-05-08 17:16:37', 1, 4, NULL),
	(4, 'shhkhzjo@10mail.org', 'musko1', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Tanya', 'Musina', 'female', '2018-05-08 17:16:37', 1, 10, NULL),
	(5, 'fdsshhkjnma@10mail.org', 'Rubrum', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Yulia', 'Romanova', 'female', '2018-05-08 17:20:49', 4, 5, NULL),
	(6, 'glitecxt@emlhub.com', 'SirCat', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Elena', 'Dodina', 'female', '2018-05-08 17:24:16', 1, 5, NULL),
	(7, 'qweafcxt@emlhub.com', 'sulzh11', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Polina', 'Balaban', 'female', '2018-05-08 17:27:10', 1, 3, NULL),
	(8, 'kxsbzrtvb@emltmp.com', 'Hakayna', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Alina', 'Taganova', 'female', '2018-05-08 20:22:57', 1, 10, NULL),
	(9, 'sdtecftbxrok@dropmail.me', 'Molb11', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Vladislav', 'Li', 'male', '2018-05-08 21:04:05', 2, 1, NULL),
	(10, 'shhkeybz@10mail.org', 'Retro1', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Dmitriy', 'Lukin', 'male', '2018-05-08 21:09:40', 1, 2, NULL),
	(11, 'shfqzfff@10mail.org', 'Win111', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Oleg', 'Pankin', 'male', '2018-05-08 21:29:50', 1, 3, NULL),
	(12, 'qkarzrtvb@emltmp.com', 'Metro1', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Alexandr', 'Hvatov', 'male', '2018-05-28 18:16:00', 4, 3, NULL),
	(13, 'faligi@l0real.net', 'asdasdasd', 'd8a928b2043db77e340b523547bf16cb4aa483f0645fe0a290ed1f20aab76257', 'asdasdasd', 'asdasdasd', 'male', '2018-06-05 18:45:11', 1, 5, NULL),
	(14, 'asdasd@o3enzyme.com', 'asdasdasds', 'd8a928b2043db77e340b523547bf16cb4aa483f0645fe0a290ed1f20aab76257', 'asdasdasd', 'asdasdasd', 'male', '2018-06-05 18:48:43', 1, 11, NULL),
	(15, 'asdasd@o3enzye.com', 'asdasdasda', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'asdasda', 'asdasda', 'male', '2018-06-05 18:51:19', 1, 8, NULL),
	(23, 'asd@ASd.asda', 'asdasddsaa', 'd8a928b2043db77e340b523547bf16cb4aa483f0645fe0a290ed1f20aab76257', 'asdasddsaa', 'asdasdasda', 'male', '2018-06-06 16:34:00', 1, 3, NULL),
	(24, 'asd@sASd.asda', 'asdassddsaa', 'd8a928b2043db77e340b523547bf16cb4aa483f0645fe0a290ed1f20aab76257', 'asdasddsaa', 'asdasdasda', 'male', '2018-06-06 17:03:12', 1, 12, '755a746c-0cef-4b00-89ab-2c11bdfdd39a');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

-- Дамп структуры для триггер messenger.set_unread
DROP TRIGGER IF EXISTS `set_unread`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `set_unread` AFTER INSERT ON `messages` FOR EACH ROW update participants set unread_messages = unread_messages+1 where conversation_id=new.conversation_id and user_id!=new.from_id//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
