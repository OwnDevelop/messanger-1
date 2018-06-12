-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.2.10-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for messenger
DROP DATABASE IF EXISTS `messenger`;
CREATE DATABASE IF NOT EXISTS `messenger` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `messenger`;

-- Dumping structure for table messenger.conversations
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
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

-- Dumping data for table messenger.conversations: ~9 rows (approximately)
DELETE FROM `conversations`;
/*!40000 ALTER TABLE `conversations` DISABLE KEYS */;
INSERT INTO `conversations` (`id`, `admin_id`, `title`, `participants_id`, `created_at`) VALUES
	(27, 9, 'null', NULL, '2018-06-12 13:15:43'),
	(28, 2, 'null', NULL, '2018-06-12 13:17:56'),
	(29, 9, 'null', NULL, '2018-06-12 13:21:33'),
	(30, 9, 'null', NULL, '2018-06-12 13:25:58'),
	(31, 9, 'null', NULL, '2018-06-12 13:26:11'),
	(32, 9, 'TeamWork', NULL, '2018-06-12 13:37:39'),
	(33, 4, 'null', NULL, '2018-06-12 13:57:53'),
	(34, 1, 'null', NULL, '2018-06-12 14:07:24'),
	(35, 1, 'null', NULL, '2018-06-12 14:07:32');
/*!40000 ALTER TABLE `conversations` ENABLE KEYS */;

-- Dumping structure for table messenger.deleted_conversations
DROP TABLE IF EXISTS `deleted_conversations`;
CREATE TABLE IF NOT EXISTS `deleted_conversations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `conversation_id` int(11) DEFAULT NULL,
  `deleted_at` datetime DEFAULT current_timestamp(),
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_deleted_conversations_conversation_id` (`conversation_id`),
  CONSTRAINT `fk_deleted_conversations` FOREIGN KEY (`conversation_id`) REFERENCES `conversations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- Dumping data for table messenger.deleted_conversations: ~0 rows (approximately)
DELETE FROM `deleted_conversations`;
/*!40000 ALTER TABLE `deleted_conversations` DISABLE KEYS */;
INSERT INTO `deleted_conversations` (`id`, `conversation_id`, `deleted_at`, `user_id`) VALUES
	(3, 30, '2018-06-12 14:21:18', 6);
/*!40000 ALTER TABLE `deleted_conversations` ENABLE KEYS */;

-- Dumping structure for table messenger.messages
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
) ENGINE=InnoDB AUTO_INCREMENT=129 DEFAULT CHARSET=utf8;

-- Dumping data for table messenger.messages: ~35 rows (approximately)
DELETE FROM `messages`;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` (`id`, `conversation_id`, `from_id`, `to_id`, `message`, `created_at`, `attachment_id`) VALUES
	(94, 27, 9, 9, 'Conversation has started', '2018-06-12 13:15:43', 26),
	(95, 27, 9, 9, 'Hello', '2018-06-12 13:15:52', 26),
	(96, 27, 2, 9, 'Hey', '2018-06-12 13:16:38', 26),
	(97, 27, 2, 9, 'Если вам нужно запрограммировать программу для программирования программ, то как вы запрограммируете программу для программирования программ для программирования программ?', '2018-06-12 13:17:20', 26),
	(98, 28, 2, 2, 'Conversation has started', '2018-06-12 13:17:56', 26),
	(99, 28, 2, 2, 'Js', '2018-06-12 13:20:03', 63),
	(100, 28, 12, 2, 'А это css', '2018-06-12 13:20:51', 64),
	(101, 29, 9, 9, 'Conversation has started', '2018-06-12 13:21:33', 26),
	(102, 29, 11, 9, 'Разговаривать с самим собой через несколько аккаунтов не очень', '2018-06-12 13:22:54', 26),
	(103, 29, 11, 9, 'Все знают, что на днях Microsoft купила GitHub за 7,5 млрд $, заплатив примерно 73 миллионами своих акций. \r\n\r\nНо не все знают, что на фоне этой новости акции MS поднялись примерно на 0,9 % до 101,7 $', '2018-06-12 13:23:57', 26),
	(104, 29, 9, 9, 'Нужно больше мемов', '2018-06-12 13:25:13', 65),
	(105, 30, 9, 9, 'Conversation has started', '2018-06-12 13:25:58', 26),
	(106, 31, 9, 9, 'Conversation has started', '2018-06-12 13:26:12', 26),
	(107, 32, 9, 9, 'Conversation has started', '2018-06-12 13:37:41', 26),
	(108, 32, 9, 9, 'Отрывок из экзамена на прожект-менеджера:\r\n— Если 120 музыкантов играют Симфонию № 9 Бетховена за 40 минут, то за сколько минут её смогут сыграть 60 человек? За N принять количество человек, а за T', '2018-06-12 13:39:20', 26),
	(109, 33, 4, 3, 'Conversation has started', '2018-06-12 13:57:54', 26),
	(110, 33, 4, 3, 'Помогите— А как ты произносишь английское слово data: data или data? \r\n— Наверное, со мной что-то не так, раз я прочитал их сейчас по-разному. Но я абсолютно уверен, что правильно надо говорить data.', '2018-06-12 13:59:43', 26),
	(111, 33, 3, 3, 'Как выглядит наш рабочий код', '2018-06-12 14:02:04', 67),
	(112, 33, 4, 3, 'Скрины из вк то, что нужно', '2018-06-12 14:05:16', 26),
	(113, 33, 4, 3, 'Картинка', '2018-06-12 14:05:42', 68),
	(114, 34, 1, 1, 'Conversation has started', '2018-06-12 14:07:25', 26),
	(115, 35, 1, 1, 'Conversation has started', '2018-06-12 14:07:32', 26),
	(116, 35, 1, 1, 'Картинки для привлечения внимания', '2018-06-12 14:08:55', 69),
	(117, 34, 1, NULL, 'А кто-то не пьет', '2018-06-12 14:11:51', 70),
	(118, 32, 2, 9, 'И тут я понял, что программирование не мое', '2018-06-12 14:13:39', 26),
	(119, 32, 6, 9, 'Дедлаааайн', '2018-06-12 14:14:58', 26),
	(120, 32, 6, 9, 'Семь смертных undefined программиста:\r\n1. undefined\r\n2. undefined\r\n3. undefined\r\n4. undefined\r\n5. undefined\r\n6. undefined\r\n7. undefined', '2018-06-12 14:16:52', 26),
	(121, 31, 5, 9, 'Всего один маленький коммит', '2018-06-12 14:17:57', 71),
	(122, 30, 6, 9, 'Еerty', '2018-06-12 14:21:14', 26),
	(123, 30, 9, 9, 'Удаление', '2018-06-12 14:21:53', 26),
	(124, 32, 6, 9, 'Stop', '2018-06-12 14:25:02', 26),
	(125, 32, 6, 9, 'Попытка 2', '2018-06-12 14:25:56', 26),
	(126, 27, 9, 9, 'А это спрайты', '2018-06-12 14:29:00', 72),
	(127, 32, 9, 9, 'Спрайты используются для анимации', '2018-06-12 14:30:17', 73),
	(128, 32, 6, 9, 'Мы так игрульки писали', '2018-06-12 14:30:57', 26);
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;

-- Dumping structure for table messenger.participants
DROP TABLE IF EXISTS `participants`;
CREATE TABLE IF NOT EXISTS `participants` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `conversation_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `unread_messages` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_participants_conversation_id` (`conversation_id`),
  CONSTRAINT `participants_conversations` FOREIGN KEY (`conversation_id`) REFERENCES `conversations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8;

-- Dumping data for table messenger.participants: ~20 rows (approximately)
DELETE FROM `participants`;
/*!40000 ALTER TABLE `participants` DISABLE KEYS */;
INSERT INTO `participants` (`id`, `conversation_id`, `user_id`, `unread_messages`) VALUES
	(61, 27, 9, 0),
	(62, 27, 2, 1),
	(63, 28, 2, 0),
	(64, 28, 12, 0),
	(65, 29, 9, 0),
	(66, 29, 11, 0),
	(67, 30, 9, 0),
	(68, 30, 6, 1),
	(69, 31, 9, 0),
	(70, 31, 5, 0),
	(71, 32, 9, 6),
	(72, 32, 2, 8),
	(75, 33, 4, 0),
	(76, 33, 3, 2),
	(77, 34, 1, 0),
	(78, 34, 15, 2),
	(79, 35, 1, 0),
	(80, 35, 23, 2),
	(82, 32, 5, 4),
	(83, 32, 6, 0);
/*!40000 ALTER TABLE `participants` ENABLE KEYS */;

-- Dumping structure for table messenger.photos
DROP TABLE IF EXISTS `photos`;
CREATE TABLE IF NOT EXISTS `photos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8;

-- Dumping data for table messenger.photos: ~72 rows (approximately)
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
	(51, 'img/uploads/44c17c2f-cf45-4ba9-9d88-a2248f4b9d85.Безымянный.jpg'),
	(52, 'img/profiles/6d7d1d1f-24da-40b9-af4a-732aeaea9bcd.Capture001.png'),
	(53, 'img/profiles/956225ec-d2e4-47c9-8d10-1289857f8521.Capture001.png'),
	(54, 'img/profiles/d34d01f4-8729-431c-b171-77dc05d38c17.Capture001.png'),
	(55, 'img/profiles/71d58f19-81e3-4912-afb5-4285a051587c.Capture001.png'),
	(56, 'img/profiles/834c1dcc-b90f-4492-8548-5c1547a7f2e7.0wG6aDO8N9M (4).jpg'),
	(57, 'img/uploads/3044519c-0866-41ec-b213-e84daf36a4b3.0wG6aDO8N9M.jpeg'),
	(58, 'img/profiles/69706f96-9f27-4715-a7c0-d297348b74d0.0wG6aDO8N9M.jpeg'),
	(59, 'img/uploads/462a9d12-d3d4-4da6-8670-795c457e0eb1.0wG6aDO8N9M (3).jpg'),
	(60, 'img/uploads/9e029cac-483a-4a10-a7bb-10f0e0fe666d.5QbfRqmvQ0g.jpg'),
	(61, 'img/uploads/c8a85997-1dcc-4a0b-ba78-9ab71f1810dd.060b6RBX6Pk.jpg'),
	(62, 'img/uploads/bcfcebd1-f2d7-4783-8c07-03317fbcc65d.helicopter-wallpapers.jpg'),
	(63, 'img/uploads/093b7e5a-636e-4e03-acbc-381593e63be6.c8a85997-1dcc-4a0b-ba78-9ab71f1810dd.060b6RBX6Pk.jpg'),
	(64, 'img/uploads/8744c03c-44e7-409f-90c7-26e2bcce2dae.9e029cac-483a-4a10-a7bb-10f0e0fe666d.5QbfRqmvQ0g.jpg'),
	(65, 'img/uploads/2a5423e4-5e9c-411f-8e14-b4a63eef452a.44oJyyHYZpk.jpg'),
	(66, 'img/profiles/5f38e04b-460f-49b3-8875-a981146c47a7.0wG6aDO8N9M.jpeg'),
	(67, 'img/uploads/01394613-076d-47c5-a2cd-1deaa02c2d7e.lhnKfsoyF1g.jpg'),
	(68, 'img/uploads/1eb1ac30-8b80-4afc-8122-c5f5f2117d12.b4_PBBpp8LM.jpg'),
	(69, 'img/uploads/b749137c-17cd-410e-a3fe-da8b8348fade.7GufLZhlAEU.jpg'),
	(70, 'img/uploads/e41a9135-e29a-4c7b-827c-7100a06f7ad2.wN-taPQggGc.jpg'),
	(71, 'img/uploads/e2278cc7-7a09-445d-83ee-09e636154943.KbLLxEYF3dE.jpg'),
	(72, 'img/uploads/4b45b538-1d29-41cf-8dfc-f00e73a77347.16184d2e56b35c4fdaa8b4699f80cd51.png'),
	(73, 'img/uploads/456ed53a-051c-40e0-9bef-046cd032914f.16184d2e56b35c4fdaa8b4699f80cd51.png');
/*!40000 ALTER TABLE `photos` ENABLE KEYS */;

-- Dumping structure for table messenger.status
DROP TABLE IF EXISTS `status`;
CREATE TABLE IF NOT EXISTS `status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` enum('Online','Idle','Do Not Disturb','Offline') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- Dumping data for table messenger.status: ~0 rows (approximately)
DELETE FROM `status`;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` (`id`, `value`) VALUES
	(1, 'Online'),
	(2, 'Idle'),
	(3, 'Do Not Disturb'),
	(4, 'Offline');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;

-- Dumping structure for table messenger.users
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

-- Dumping data for table messenger.users: ~0 rows (approximately)
DELETE FROM `users`;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`, `email`, `login`, `password`, `first_name`, `last_name`, `sex`, `created_at`, `status`, `avatar`, `activation_code`) VALUES
	(1, 'best@people.math', 'BestGuy', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Sergey', 'Kovalenko', 'male', '1998-06-10 19:21:10', 4, 13, NULL),
	(2, 'wfcenzbu@emlpro.com', 'Gloria', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Anna', 'Emelyanova', 'female', '2018-05-08 17:05:46', 4, 1, NULL),
	(3, 'qxfuvsao@yomail.info', 'tanya1', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Tanya', 'Sergeta', 'female', '2018-05-08 17:16:37', 4, 66, NULL),
	(4, 'shhkhzjo@10mail.org', 'musko1', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Tanya', 'Musina', 'female', '2018-05-08 17:16:37', 4, 10, NULL),
	(5, 'fdsshhkjnma@10mail.org', 'Rubrum', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Yulia', 'Romanova', 'female', '2018-05-08 17:20:49', 4, 5, NULL),
	(6, 'glitecxt@emlhub.com', 'SirCat', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Elena', 'Dodina', 'female', '2018-05-08 17:24:16', 4, 5, NULL),
	(7, 'qweafcxt@emlhub.com', 'sulzh11', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Polina', 'Balaban', 'female', '2018-05-08 17:27:10', 4, 3, NULL),
	(8, 'kxsbzrtvb@emltmp.com', 'Hakayna', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Alina', 'Taganova', 'female', '2018-05-08 20:22:57', 4, 10, NULL),
	(9, 'sdtecftbxrok@dropmail.me', 'Molb11', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Vladislav', 'Li', 'male', '2018-05-08 21:04:05', 4, 1, NULL),
	(10, 'shhkeybz@10mail.org', 'Retro1', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Dmitriy', 'Lukin', 'male', '2018-05-08 21:09:40', 4, 2, NULL),
	(11, 'shfqzfff@10mail.org', 'Win111', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Oleg', 'Pankin', 'male', '2018-05-08 21:29:50', 4, 3, NULL),
	(12, 'qkarzrtvb@emltmp.com', 'Metro1', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Alexandr', 'Hvatov', 'male', '2018-05-28 18:16:00', 4, 3, NULL),
	(13, 'faligi@l0real.net', 'asdasdasd', 'd8a928b2043db77e340b523547bf16cb4aa483f0645fe0a290ed1f20aab76257', 'asdasdasd', 'asdasdasd', 'male', '2018-06-05 18:45:11', 4, 5, NULL),
	(14, 'asdasd@o3enzyme.com', 'asdasdasds', 'd8a928b2043db77e340b523547bf16cb4aa483f0645fe0a290ed1f20aab76257', 'asdasdasd', 'asdasdasd', 'male', '2018-06-05 18:48:43', 4, 11, NULL),
	(15, 'asdasd@o3enzye.com', 'asdasdasda', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'asdasda', 'asdasda', 'male', '2018-06-05 18:51:19', 4, 8, NULL),
	(23, 'asd@ASd.asda', 'asdasddsaa', 'd8a928b2043db77e340b523547bf16cb4aa483f0645fe0a290ed1f20aab76257', 'asdasddsaa', 'asdasdasda', 'male', '2018-06-06 16:34:00', 4, 3, NULL),
	(24, 'asd@sASd.asda', 'asdassddsaa', 'd8a928b2043db77e340b523547bf16cb4aa483f0645fe0a290ed1f20aab76257', 'asdasddsaa', 'asdasdasda', 'male', '2018-06-06 17:03:12', 4, 12, '755a746c-0cef-4b00-89ab-2c11bdfdd39a'),
	(25, 'asd@o3enzyme.com', 'aaaaaaa', 'e46240714b5db3a23eee60479a623efba4d633d27fe4f03c904b9e219a7fbe60', 'aaaaaaa', 'aaaaaaa', 'female', '2018-06-07 01:45:31', 4, 4, NULL),
	(26, 'aaaaaaas@o3enzyme.com', 'areyoukiddingme', 'd8a928b2043db77e340b523547bf16cb4aa483f0645fe0a290ed1f20aab76257', 'lastuser', 'verylastuser', 'male', '2018-06-07 02:41:51', 4, 11, NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

-- Dumping structure for trigger messenger.set_unread
DROP TRIGGER IF EXISTS `set_unread`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `set_unread` AFTER INSERT ON `messages` FOR EACH ROW update participants set unread_messages = unread_messages+1 where conversation_id=new.conversation_id and user_id!=new.from_id//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
