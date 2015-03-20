-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Mar 20, 2015 at 07:37 AM
-- Server version: 5.6.17
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `loms_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `order`
--

CREATE TABLE IF NOT EXISTS `order` (
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `order_id` int(10) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `company` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `ship_to_street` varchar(100) NOT NULL,
  `ship_to_city` varchar(50) NOT NULL,
  `ship_to_state` varchar(50) NOT NULL,
  `ship_to_postal` int(10) NOT NULL,
  `region` char(4) DEFAULT NULL,
  `order_status` char(1) DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `order_id` (`order_id`),
  KEY `order_id_2` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `order_item`
--

CREATE TABLE IF NOT EXISTS `order_item` (
  `order_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `price` double NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `order_quantity` int(11) NOT NULL,
  `confirm_quantity` int(11) DEFAULT NULL,
  PRIMARY KEY (`order_id`,`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
