CREATE DATABASE  IF NOT EXISTS `web_chat` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `web_chat`;
-- MySQL dump 10.13  Distrib 5.6.11, for Win64 (x86_64)
--
-- Host: localhost    Database: web_chat
-- ------------------------------------------------------
-- Server version	5.6.11

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `id_account` varchar(5) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `avatar` varchar(45) DEFAULT NULL,
  `full_name` varchar(45) NOT NULL,
  PRIMARY KEY (`id_account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES ('00000','admin','admin','00000','AdminName'),('00001','user1','user1','00001','User1Name'),('00002','user2','user2','00002','User2Name'),('00003','user3','user3','00003','User3Name'),('00004','user4','user4','00004','User4Name'),('00005','user5','user5','00005','User5Name');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message` (
  `id_message` int(11) NOT NULL AUTO_INCREMENT,
  `id_from` varchar(5) NOT NULL,
  `id_to` varchar(5) NOT NULL,
  `type` int(11) NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `content` text,
  `offline` tinyint(4) NOT NULL,
  PRIMARY KEY (`id_message`),
  KEY `id_account_idx` (`id_from`),
  KEY `fk_id_to_msg_acc_idx` (`id_to`),
  CONSTRAINT `fk_id_from_msg_acc` FOREIGN KEY (`id_from`) REFERENCES `account` (`id_account`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_id_to_msg_acc` FOREIGN KEY (`id_to`) REFERENCES `account` (`id_account`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=183 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (141,'00000','00001',0,'2013-08-01 03:08:40','1111111111<br><br>',0),(142,'00000','00001',0,'2013-08-01 03:08:41','22222222<br><br>',0),(143,'00000','00001',0,'2013-08-01 03:08:42','3333333333<br><br>',0),(144,'00001','00002',0,'2013-08-01 04:11:34','offline mes1<br><br>',0),(145,'00001','00002',0,'2013-08-01 04:11:34','offline mes2<br><br>',0),(146,'00001','00002',0,'2013-08-01 04:11:34','dsadasdqsa<br><br>',0),(147,'00001','00002',0,'2013-08-01 04:11:34','dasdasdas<br><br>',0),(148,'00001','00002',0,'2013-08-01 04:12:43','sadasdas<br><br>',0),(149,'00002','00001',0,'2013-08-01 04:14:08','hello hello<br><br>',0),(150,'00002','00001',0,'2013-08-01 04:14:08','dsadas<br><br>',0),(151,'00002','00001',0,'2013-08-01 04:14:08','dasdasd<br><br>',0),(152,'00002','00001',0,'2013-08-01 04:14:08','dasdas<br><br>',0),(153,'00001','00002',0,'2013-08-01 04:14:13','aaaaaaaaa<br><br>',0),(154,'00000','00005',0,'2013-08-01 04:29:39','offline 1<br><br>',0),(155,'00000','00005',0,'2013-08-01 04:29:39','offline 2<br><br>',0),(156,'00000','00005',0,'2013-08-01 04:27:29','online moi<br><br>',0),(157,'00000','00005',0,'2013-08-01 04:29:39','online moi<br><br>',0),(158,'00005','00004',0,'2013-08-01 04:30:49','offline from 5<br><br>',0),(159,'00005','00004',0,'2013-08-01 04:30:49','offline from 5 2<br><br>',0),(160,'00005','00004',0,'2013-08-01 04:30:49','offline from 5 3<br><br>',0),(161,'00004','00005',0,'2013-08-01 04:30:55','da nhan duoc<div><br></div>',0),(162,'00005','00003',0,'2013-08-01 04:32:41','<br><br>',0),(163,'00005','00003',0,'2013-08-01 04:32:41','offline from 5 2<br><br>',0),(164,'00005','00003',0,'2013-08-01 04:32:41','offline from 5 3<br><br>',0),(165,'00005','00003',0,'2013-08-01 04:32:41','offline from 5 5<br><br>',0),(166,'00000','00005',0,'2013-08-01 04:37:32','we we<br><br>',0),(167,'00000','00005',0,'2013-08-01 04:38:09','offline 3<br><br>',0),(168,'00000','00005',0,'2013-08-01 04:38:09','offline 4<br><br>',0),(169,'00000','00005',0,'2013-08-01 04:38:09','offline 5<br><br>',0),(170,'00005','00000',0,'2013-08-01 04:40:04','offline 1<br><br>',0),(171,'00005','00000',0,'2013-08-01 04:40:04','offline 2<br><br>',0),(172,'00005','00000',0,'2013-08-01 04:40:04','offline 6<br><br>',0),(173,'00005','00000',0,'2013-08-01 04:40:04','online moi<br><br>',0),(174,'00000','00005',0,'2013-08-01 04:47:09','online 2<br><br>',0),(175,'00000','00005',0,'2013-08-01 04:47:11','online 3<br><br>',0),(176,'00001','00003',0,'2013-08-01 05:01:21','offline from 1<br><br>',0),(177,'00001','00003',0,'2013-08-01 05:01:21','offline from saeas<br><br>',0),(178,'00001','00003',0,'2013-08-01 05:01:21','dsadasdasd<br><br>',0),(179,'00002','00003',0,'2013-08-01 05:01:22','dsadasdas<br><br>',0),(180,'00002','00003',0,'2013-08-01 05:01:22','dsadasdsa<br><br>',0),(181,'00002','00003',0,'2013-08-01 05:01:22','dasdasd<br><br>',0),(182,'00002','00003',0,'2013-08-01 05:01:22','dsadas<br><br>',0);
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message_offline`
--

DROP TABLE IF EXISTS `message_offline`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message_offline` (
  `id_message_offline` int(11) NOT NULL AUTO_INCREMENT,
  `id_from` varchar(5) NOT NULL,
  `id_to` varchar(5) NOT NULL,
  `type` int(11) NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `content` text,
  PRIMARY KEY (`id_message_offline`),
  KEY `fk_id_from_msg_off_acc_idx` (`id_from`),
  KEY `fk_id_to_msg_off_acc_idx` (`id_to`),
  CONSTRAINT `fk_id_from_msg_off_acc` FOREIGN KEY (`id_from`) REFERENCES `account` (`id_account`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_id_to_msg_off_acc` FOREIGN KEY (`id_to`) REFERENCES `account` (`id_account`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message_offline`
--

LOCK TABLES `message_offline` WRITE;
/*!40000 ALTER TABLE `message_offline` DISABLE KEYS */;
INSERT INTO `message_offline` VALUES (21,'00005','00001',0,'2013-07-31 10:08:59','offline 1<br><br>'),(22,'00005','00001',0,'2013-07-31 10:09:01','offline 2<br><br>'),(23,'00004','00001',0,'2013-07-31 10:21:27','11111111111111<br><br>'),(24,'00004','00001',0,'2013-07-31 10:21:28','22222222222222<br><br>'),(25,'00004','00001',0,'2013-07-31 10:21:29','3333333333<br><br>'),(26,'00001','00005',0,'2013-07-31 10:26:12','dsadasdas<br><br>'),(27,'00001','00005',0,'2013-07-31 10:26:23','offline from user 1<br><br>'),(28,'00001','00005',0,'2013-07-31 10:26:24','offline from user 1<br><br>'),(29,'00005','00001',0,'2013-07-31 10:39:02','dasdasdsa<br><br>'),(30,'00005','00001',0,'2013-07-31 10:39:03','dasddsada<br><br>'),(31,'00002','00005',0,'2013-07-31 10:39:36','offline 1<br><br>'),(32,'00002','00005',0,'2013-07-31 10:39:39','offline 2<br><br>'),(33,'00002','00005',0,'2013-07-31 10:41:36','dasdasdas<br><br>'),(34,'00002','00005',0,'2013-07-31 10:41:39','dasdasd<br><br>');
/*!40000 ALTER TABLE `message_offline` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-08-01 18:12:18
