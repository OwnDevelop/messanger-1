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
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8;

-- Дамп данных таблицы messenger.messages: ~44 rows (приблизительно)
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
	(39, 26, 9, 9, 'Conversation has started', '2018-06-06 18:08:23', 14),
	(40, 8, 9, 9, 'sd', '2018-06-06 21:05:33', 14),
	(41, 1, 9, 9, 'asdasd', '2018-06-06 21:21:49', 19),
	(42, 1, 9, 9, 'asdasdssssssssssssssss', '2018-06-06 21:24:52', 20),
	(43, 23, 9, 9, 'asdasdasss', '2018-06-06 21:25:41', 21),
	(44, 10, 9, 9, '', '2018-06-06 21:29:43', 22),
	(45, 8, 9, 9, 'asd33333', '2018-06-06 21:30:02', 23),
	(46, 8, 9, 9, '', '2018-06-06 21:33:20', 24),
	(47, 8, 9, 9, '', '2018-06-06 21:37:04', 25),
	(48, 1, 5, 9, '', '2018-06-06 23:15:20', 26),
	(49, 1, 5, 9, 'pic', '2018-06-06 23:16:24', 27),
	(50, 1, 5, 9, '', '2018-06-06 23:19:53', 28),
	(51, 1, 5, 9, 'pic2', '2018-06-06 23:20:29', 29),
	(52, 1, 5, 9, 'pic3', '2018-06-06 23:20:51', 30),
	(53, 1, 5, 9, 'pic4', '2018-06-06 23:26:00', 31),
	(54, 1, 5, 9, 'pic5', '2018-06-06 23:31:24', 32),
	(55, 1, 5, 9, 'pic 6', '2018-06-06 23:36:45', 33),
	(56, 1, 5, 9, '', '2018-06-06 23:38:05', 34),
	(57, 1, 5, 9, 'pic7', '2018-06-07 00:54:53', 35),
	(58, 1, 5, 9, '', '2018-06-07 00:55:06', 36),
	(59, 1, 5, 9, 'pic8', '2018-06-07 00:55:21', 37),
	(60, 1, 5, 9, 'sss', '2018-06-07 00:55:59', 38),
	(61, 1, 5, 9, 'gggggggggg\r\ng', '2018-06-07 00:56:22', 39),
	(62, 1, 5, 9, 'hhhhhhhhh', '2018-06-07 00:56:51', 40),
	(63, 1, 5, 9, '', '2018-06-07 00:57:04', 41),
	(64, 10, 9, 9, 'ss', '2018-06-07 00:59:30', 42),
	(65, 8, 9, 9, 's', '2018-06-07 00:59:57', 43),
	(66, 10, 9, 9, '', '2018-06-07 01:00:39', 44),
	(67, 1, 9, 9, '', '2018-06-07 01:01:06', 45),
	(77, 11, 9, 9, '', '2018-06-07 01:14:44', 46),
	(78, 10, 9, 9, 'pictest 2 ch nochi', '2018-06-07 02:35:50', 48),
	(79, 10, 9, 9, '11111', '2018-06-07 02:36:15', 49),
	(80, 23, 9, 9, '', '2018-06-07 02:36:42', 50),
	(81, 23, 9, 9, '', '2018-06-07 02:38:53', 51);
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
	(1, 1, 2, 22),
	(2, 1, 3, 24),
	(3, 1, 5, 1),
	(4, 1, 6, 19),
	(5, 1, 8, 19),
	(6, 1, 7, 19),
	(8, 2, 6, 0),
	(19, 2, 2, 0),
	(20, 8, 3, 5),
	(21, 8, 7, 5),
	(23, 8, 8, 5),
	(24, 9, 3, 0),
	(25, 9, 7, 0),
	(26, 9, 6, 0),
	(27, 10, 9, 0),
	(28, 10, 11, 9),
	(29, 11, 9, 0),
	(30, 11, 10, 1),
	(31, 12, 3, 0),
	(32, 12, 5, 0),
	(33, 1, 9, 16),
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
	(49, 23, 14, 3),
	(50, 23, 17, 3),
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
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;

-- Дамп данных таблицы messenger.photos: ~33 rows (приблизительно)
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
	(14, 'img/profiles/my.jpg'),
	(16, 'img/profiles/my.jpg'),
	(17, 'img/profiles/my.jpg'),
	(18, 'img/profiles/my.jpg'),
	(19, 'img/profiles/my.jpg'),
	(20, 'img/profiles/my.jpg'),
	(21, 'img/profiles/my.jpg'),
	(22, 'ec4e8670-cd18-42ea-bf5f-4ef8d19411fc.Безымянный.jpg'),
	(23, '71ce8f26-fd7c-436a-ac6a-b800615b95a1.Безымянный.jpg'),
	(24, '2a2f2bcd-bfc8-494c-bc74-62b72d50fca6.Безымянный.jpg'),
	(25, '5fccd710-a7ba-455b-bd30-82e40845ee67.Безымянный.jpg'),
	(26, ''),
	(27, 'cd5ed312-e61a-4416-9714-441823c14acd.Безымянный.jpg'),
	(28, '3aa8e569-f25f-4980-82cb-b1fe34d5d80b.Безымянный.jpg'),
	(29, '78285d30-54f0-466b-89df-1c816634c3b6.Безымянный.jpg'),
	(30, 'aadf774b-8030-4c73-9558-5fcbd7c772c8.Безымянный.jpg'),
	(31, 'e9857113-cc14-48c9-a6c3-f34d07e4b6cf.Безымянный.jpg'),
	(32, '070f0fbc-0f59-4064-952b-6b77abd715b5.Безымянный.jpg'),
	(33, 'img/uploads/5d024e2c-d603-4c70-8c23-166485b50fb5.Безымянный.jpg'),
	(34, 'img/uploads/d4ebca0a-4c3d-433e-83a1-69c89fc3599a.Безымянный.jpg'),
	(35, 'img/uploads/a3e74d71-2e75-4295-b17e-5cff1a300b9a.tea.jpg'),
	(36, 'img/uploads/e8a6dd4d-7b53-452d-bfc1-f29d71ef8976.tea.jpg'),
	(37, 'img/uploads/1b1056e8-65f1-42b5-81ef-176136c12d1b.tea.jpg'),
	(38, 'img/uploads/3e543a56-acc6-4abf-8bf0-04d5558f59c9.tea.jpg'),
	(39, 'img/uploads/a363da6e-d014-42c8-9087-0ab61b501ea6.tea.jpg'),
	(40, 'img/uploads/9189cd0f-97a6-4e6d-ac7e-d2b10d23ea3f.tea.jpg'),
	(41, 'img/uploads/1cbf57c1-dc68-4fd1-aba8-9063ea6f2978.tea.jpg'),
	(42, 'img/uploads/04ce65bc-a835-4ccc-81d1-a1d1c0fbaa25.tea.jpg'),
	(43, 'img/uploads/4d2a54ae-50da-4592-a643-ce69b697d8fe.Безымянный.jpg'),
	(44, 'img/uploads/0259c8e8-98f6-427e-9987-91f6e42474c9.Безымянный.jpg'),
	(45, 'img/uploads/6aa295d2-c20d-4e02-a00b-cf73260d8a6e.tea.jpg'),
	(46, 'img/uploads/39caf24c-0bdf-4e55-bc10-e1442ed5a9fc.Безымянный.jpg'),
	(47, 'img/uploads/e1167083-5ca3-4338-a845-7742cbfd9153.Безымянный.jpg'),
	(48, 'img/uploads/5053786c-e283-4c00-b58f-a321c7611f24.Безымянный.jpg'),
	(49, 'img/uploads/08a3ce39-ec39-456a-b9d5-76ad223e7da9.tea.jpg'),
	(50, 'img/uploads/34a2b2a8-4dc4-4249-ad7e-0a44158077be.tea.jpg'),
	(51, 'img/uploads/44c17c2f-cf45-4ba9-9d88-a2248f4b9d85.Безымянный.jpg');
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
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

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
	(24, 'asd@sASd.asda', 'asdassddsaa', 'd8a928b2043db77e340b523547bf16cb4aa483f0645fe0a290ed1f20aab76257', 'asdasddsaa', 'asdasdasda', 'male', '2018-06-06 17:03:12', 1, 12, '755a746c-0cef-4b00-89ab-2c11bdfdd39a'),
	(25, 'asd@o3enzyme.com', 'aaaaaaa', 'e46240714b5db3a23eee60479a623efba4d633d27fe4f03c904b9e219a7fbe60', 'aaaaaaa', 'aaaaaaa', 'female', '2018-06-07 01:45:31', 1, 4, NULL),
	(26, 'aaaaaaas@o3enzyme.com', 'areyoukiddingme', 'd8a928b2043db77e340b523547bf16cb4aa483f0645fe0a290ed1f20aab76257', 'lastuser', 'verylastuser', 'male', '2018-06-07 02:41:51', 1, 11, NULL);
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
