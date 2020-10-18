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
  PRIMARY KEY (`id_message`),
  KEY `id_account_idx` (`id_from`),
  KEY `fk_id_to_msg_acc_idx` (`id_to`),
  CONSTRAINT `fk_id_from_msg_acc` FOREIGN KEY (`id_from`) REFERENCES `account` (`id_account`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_id_to_msg_acc` FOREIGN KEY (`id_to`) REFERENCES `account` (`id_account`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=141 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (2,'00000','00001',0,'2013-07-30 07:46:33','hello user 1<div><br></div>'),(3,'00000','00001',0,'2013-07-30 07:56:04','111111111<br><br>'),(4,'00000','00001',0,'2013-07-30 07:57:55','1111111111<br><br>'),(5,'00000','00001',0,'2013-07-30 08:01:50','1111111111<br><br>'),(6,'00000','00005',0,'2013-07-30 08:05:33','11111111111<br><br>'),(7,'00000','00005',0,'2013-07-30 08:05:33','2222222<br><br>'),(8,'00000','00005',0,'2013-07-30 08:05:33','333333333<br><br>'),(9,'00000','00005',0,'2013-07-30 08:05:43','dsadasdas<br><br>'),(10,'00005','00000',0,'2013-07-30 08:06:03','dsadasdas<br><br>'),(11,'00005','00000',0,'2013-07-30 08:06:03','dsadas<br><br>'),(12,'00005','00000',0,'2013-07-30 08:06:53','fdksjhjfksdhf<br><br>'),(13,'00005','00000',0,'2013-07-30 08:06:53','fdsjklfjlskdfj<br><br>'),(14,'00005','00000',0,'2013-07-30 08:06:53','fdsjklfjkldsf<br><br>'),(15,'00005','00000',0,'2013-07-30 08:06:53','fjdslkfjklsdf<br><br>'),(16,'00005','00000',0,'2013-07-30 08:06:54','fjsdlkfjlksd<br><br>'),(17,'00005','00000',0,'2013-07-30 08:06:54','fsdjklfjdsk<br><br>'),(18,'00000','00005',0,'2013-07-30 08:07:04','sadasdas<br><br>'),(19,'00000','00005',0,'2013-07-30 08:07:04','dasdas<br><br>'),(20,'00000','00005',0,'2013-07-30 08:07:04','dasdas<br><br>'),(21,'00000','00005',0,'2013-07-30 08:07:04','dasdas<br><br>'),(22,'00000','00005',0,'2013-07-30 08:07:14','dasdas<br><br>'),(23,'00000','00005',0,'2013-07-30 08:07:14','dasdas<br><br>'),(24,'00005','00000',0,'2013-07-30 08:47:18','moi them vao tu user 5<br><br>'),(25,'00000','00005',0,'2013-07-30 08:47:18','moi them vao tu admin<br><br>'),(26,'00000','00005',0,'2013-07-30 08:52:24','hellllllo<br><br>'),(27,'00000','00005',0,'2013-07-30 09:08:57','hellllllo2<br><br>'),(28,'00000','00005',0,'2013-07-30 09:10:17','cxzczxcxz<br><br>'),(29,'00005','00000',0,'2013-07-30 09:10:58','dsadasd<br><br>'),(30,'00001','00005',0,'2013-07-30 09:20:28','ddsadasds<div><br></div>'),(31,'00001','00005',0,'2013-07-30 09:20:28','11111111111<div><br></div>'),(32,'00001','00005',0,'2013-07-30 09:20:28','2222222222<div><br></div>'),(33,'00001','00005',0,'2013-07-30 09:20:28','33333333333<div><br></div>'),(34,'00001','00005',0,'2013-07-30 09:20:38','44444444444<div><br></div>'),(35,'00005','00001',0,'2013-07-30 09:20:38','ewresdrfdsfds<br><br>'),(36,'00005','00001',0,'2013-07-30 09:20:48','fsdfsdfds<br><br>'),(37,'00005','00001',0,'2013-07-30 09:20:48','fsdfsdfds<br><br>'),(38,'00005','00001',0,'2013-07-30 09:20:48','fsdfsdfsdf<br><br>'),(39,'00005','00001',0,'2013-07-30 09:20:48','fdsfsdfdsf<br><br>'),(40,'00005','00001',0,'2013-07-30 09:21:08','jhghjghj<br><br>'),(41,'00001','00000',0,'2013-07-30 09:21:18','11111111111<div><br></div>'),(42,'00001','00000',0,'2013-07-30 09:21:18','22222222222222<div><br></div>'),(43,'00001','00000',0,'2013-07-30 09:21:18','333333333333<div><br></div>'),(44,'00001','00000',0,'2013-07-30 09:21:19','444444444444<div><br></div>'),(45,'00001','00000',0,'2013-07-30 09:21:19','5555555555<div><br></div>'),(46,'00000','00005',0,'2013-07-30 09:37:36','moi nhat<br><br>'),(47,'00005','00000',0,'2013-07-30 09:37:57','dasdas<br><br>'),(48,'00001','00000',0,'2013-07-30 09:40:34','dadasd<br><br>'),(49,'00000','00005',0,'2013-07-30 10:18:18','moi nhat<br><br>'),(50,'00000','00001',0,'2013-07-30 10:37:31','dsadasdasd<br><br>'),(51,'00000','00001',0,'2013-07-30 10:38:57','moiiiiiiiiiiiii<br><br>'),(52,'00000','00005',0,'2013-07-30 10:44:39','moi nhat 30/7<br><br>'),(53,'00000','00005',0,'2013-07-30 10:46:29','moi nhat nhat<br><br>'),(54,'00000','00005',0,'2013-07-30 10:52:15','moi moi moi moi<br><br>'),(55,'00000','00005',0,'2013-07-30 10:55:26','dlkasjhdkasjkd<br><br>'),(56,'00005','00000',0,'2013-07-30 10:58:06','xzczsad<br><br>'),(57,'00000','00005',0,'2013-07-31 02:47:40','31-7<br><br>'),(58,'00000','00001',0,'2013-07-31 02:56:10','moidkalsdjlkasjkl<br><br>'),(59,'00000','00005',0,'2013-07-31 02:58:23','dsadasdasdas<br><br>'),(60,'00000','00002',0,'2013-07-31 03:03:45','hello 2<br><br>'),(61,'00000','00002',0,'2013-07-31 03:09:00','hello 2222<br><br>'),(62,'00000','00002',0,'2013-07-31 03:09:30','hellllllllllllll<br><br>'),(63,'00000','00002',0,'2013-07-31 03:09:50','ddddddddddddd<br><br>'),(64,'00002','00000',0,'2013-07-31 03:13:00','zalo<div><br></div>'),(65,'00000','00003',0,'2013-07-31 03:27:27','<p>hello 3</p><p>&nbsp;</p>'),(66,'00000','00003',0,'2013-07-31 03:34:26','dsadasdasdsa<br><br>'),(67,'00003','00000',0,'2013-07-31 03:34:26','<p>dasdasdas</p><p>&nbsp;</p>'),(68,'00003','00000',0,'2013-07-31 03:34:36','<p>dasdas</p><p>&nbsp;</p>'),(69,'00003','00000',0,'2013-07-31 03:34:36','<p>dasdasdsa</p><p>&nbsp;</p>'),(70,'00000','00003',0,'2013-07-31 03:34:36','dsadasdsa<br><br>'),(71,'00000','00003',0,'2013-07-31 03:34:46','dasdas<br><br>'),(72,'00000','00003',0,'2013-07-31 03:34:46','dasdsa<br><br>'),(73,'00000','00003',0,'2013-07-31 03:34:46','dasdasds<br><br>'),(74,'00004','00000',0,'2013-07-31 03:35:26','<p>111111111111</p><p>&nbsp;</p>'),(75,'00000','00004',0,'2013-07-31 03:35:36','222222222222<br><br>'),(76,'00000','00004',0,'2013-07-31 03:36:06','ta da tro lai<br><br>'),(77,'00000','00003',0,'2013-07-31 03:50:57','aaaaaaaaa111111<br><br>'),(78,'00001','00000',0,'2013-07-31 04:10:08','dasdasd<br><br>'),(79,'00003','00001',0,'2013-07-31 04:27:22','11111111111<br><br>'),(80,'00003','00001',0,'2013-07-31 04:27:22','222222222<br><br>'),(81,'00001','00000',0,'2013-07-31 04:41:22','moi vua them<br><br>'),(82,'00003','00004',0,'2013-07-31 08:13:16','1111111111<br><br>'),(83,'00003','00004',0,'2013-07-31 08:49:23','22222222222<br><br>'),(84,'00003','00004',0,'2013-07-31 08:54:29','231232132<br><br>'),(85,'00003','00004',0,'2013-07-31 08:54:31','323213232<br><br>'),(86,'00003','00004',0,'2013-07-31 08:54:33','32322222222222222<br><br>'),(87,'00003','00004',0,'2013-07-31 08:55:28','offline mes 1<br><br>'),(88,'00003','00004',0,'2013-07-31 08:55:31','offline mes 2<br><br>'),(89,'00003','00004',0,'2013-07-31 09:01:03','offline mes 3<br><br>'),(90,'00003','00004',0,'2013-07-31 09:02:28','offline mes 4<br><br>'),(91,'00003','00002',0,'2013-07-31 09:08:00','offline 1<br><br>'),(92,'00003','00002',0,'2013-07-31 09:10:09','offline 2<br><br>'),(93,'00003','00002',0,'2013-07-31 09:15:07','offline 3<br><br>'),(94,'00003','00002',0,'2013-07-31 09:15:09','offline 4<br><br>'),(95,'00003','00002',0,'2013-07-31 09:18:02','oiffliner 5<br><br>'),(96,'00003','00002',0,'2013-07-31 09:18:04','offline 6<br><br>'),(97,'00003','00000',0,'2013-07-31 09:24:31','offline 1<br><br>'),(98,'00003','00000',0,'2013-07-31 09:24:32','offline 2<br><br>'),(99,'00003','00001',0,'2013-07-31 09:24:43','offline 1<br><br>'),(100,'00003','00001',0,'2013-07-31 09:24:45','offline 2<br><br>'),(101,'00003','00001',0,'2013-07-31 09:24:47','offline 3<br><br>'),(102,'00003','00004',0,'2013-07-31 09:24:57','offline 1<br><br>'),(103,'00003','00004',0,'2013-07-31 09:24:59','offline 2<br><br>'),(104,'00003','00004',0,'2013-07-31 09:25:00','offline 3<br><br>'),(105,'00003','00005',0,'2013-07-31 09:25:05','offline 1<br><br>'),(106,'00003','00005',0,'2013-07-31 09:25:07','offline 2<br><br>'),(107,'00003','00005',0,'2013-07-31 09:25:08','offline 3<br><br>'),(108,'00005','00001',0,'2013-07-31 09:33:58','offlineeeeeeee<br><br>'),(109,'00005','00001',0,'2013-07-31 09:33:59','djlskajdlkasj<br><br>'),(110,'00005','00001',0,'2013-07-31 09:34:00','dsajlkjdklsa<br><br>'),(111,'00005','00002',0,'2013-07-31 09:34:14','sadasdasd<br><br>'),(112,'00005','00002',0,'2013-07-31 09:34:15','dasdasda<br><br>'),(113,'00005','00003',0,'2013-07-31 09:34:17','dasdasdsa<br><br>'),(114,'00005','00003',0,'2013-07-31 09:34:18','dasdasdasd<br><br>'),(115,'00005','00004',0,'2013-07-31 09:34:21','dasdasdasdas<br><br>'),(116,'00005','00004',0,'2013-07-31 09:34:23','dasdasdasdasdasd<br><br>'),(117,'00005','00001',0,'2013-07-31 10:08:59','offline 1<br><br>'),(118,'00005','00001',0,'2013-07-31 10:09:01','offline 2<br><br>'),(119,'00004','00001',0,'2013-07-31 10:19:03','offlinedjalkkdj<br><br>'),(120,'00004','00001',0,'2013-07-31 10:19:04','dsajlkdjalksd<br><br>'),(121,'00004','00001',0,'2013-07-31 10:19:05','dsajlkdjlkasjd<br><br>'),(122,'00004','00001',0,'2013-07-31 10:19:06','dsajdlkjaskl<br><br>'),(123,'00004','00001',0,'2013-07-31 10:21:27','11111111111111<br><br>'),(124,'00004','00001',0,'2013-07-31 10:21:28','22222222222222<br><br>'),(125,'00004','00001',0,'2013-07-31 10:21:29','3333333333<br><br>'),(126,'00001','00004',0,'2013-07-31 10:23:43','<br><br>'),(127,'00001','00004',0,'2013-07-31 10:23:45','offline from user 1<br><br>'),(128,'00001','00004',0,'2013-07-31 10:23:46','offline from user 1<br><br>'),(129,'00001','00004',0,'2013-07-31 10:23:52','offline from user 1<br><br>'),(130,'00001','00004',0,'2013-07-31 10:26:09','dsffsdfsd<br><br>'),(131,'00001','00005',0,'2013-07-31 10:26:12','dsadasdas<br><br>'),(132,'00001','00005',0,'2013-07-31 10:26:23','offline from user 1<br><br>'),(133,'00001','00005',0,'2013-07-31 10:26:24','offline from user 1<br><br>'),(134,'00005','00001',0,'2013-07-31 10:39:02','dasdasdsa<br><br>'),(135,'00005','00001',0,'2013-07-31 10:39:03','dasddsada<br><br>'),(136,'00002','00005',0,'2013-07-31 10:39:36','offline 1<br><br>'),(137,'00002','00005',0,'2013-07-31 10:39:39','offline 2<br><br>'),(138,'00002','00005',0,'2013-07-31 10:41:36','dasdasdas<br><br>'),(139,'00002','00005',0,'2013-07-31 10:41:39','dasdasd<br><br>'),(140,'00005','00001',0,'2013-07-31 11:01:07','moi ne<br><br>');
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

-- Dump completed on 2013-07-31 18:08:15
