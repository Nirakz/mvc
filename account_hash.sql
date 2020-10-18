-- phpMyAdmin SQL Dump
-- version 3.3.3
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 19, 2013 at 10:17 AM
-- Server version: 5.1.50
-- PHP Version: 5.3.14

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `web_chat`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

CREATE TABLE IF NOT EXISTS `account` (
  `id_account` varchar(5) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `avatar` varchar(45) DEFAULT NULL,
  `full_name` varchar(45) NOT NULL,
  PRIMARY KEY (`id_account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`id_account`, `username`, `password`, `avatar`, `full_name`) VALUES
('00000', 'admin', 'd033e22ae348aeb5660fc2140aec35850c4da997', '00000', 'AdminName'),
('00001', 'user1', 'b3daa77b4c04a9551b8781d03191fe098f325e67', '00001', 'User1Name'),
('00002', 'user2', 'a1881c06eec96db9901c7bbfe41c42a3f08e9cb4', '00002', 'User2Name'),
('00003', 'user3', '0b7f849446d3383546d15a480966084442cd2193', '00003', 'User3Name'),
('00004', 'user4', '06e6eef6adf2e5f54ea6c43c376d6d36605f810e', '00004', 'User4Name'),
('00005', 'user5', '7d112681b8dd80723871a87ff506286613fa9cf6', '00005', 'User5Name'),
('00006', 'user6', '312a46dc52117efa4e3096eda510370f01c83b27', '00006', 'User6Name');
