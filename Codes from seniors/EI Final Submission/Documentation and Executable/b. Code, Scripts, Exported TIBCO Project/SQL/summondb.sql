-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 02, 2012 at 05:49 AM
-- Server version: 5.5.16
-- PHP Version: 5.3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `summondb`
--

-- --------------------------------------------------------

--
-- Table structure for table `summon_records`
--

CREATE TABLE IF NOT EXISTS `summon_records` (
  `id` varchar(100) NOT NULL,
  `carplate` varchar(100) DEFAULT NULL,
  `carparkid` varchar(100) DEFAULT NULL,
  `Stime` varchar(100) DEFAULT NULL,
  `fineapplicable` varchar(100) DEFAULT NULL,
  `seasonpark` varchar(100) DEFAULT NULL,
  `offence` varchar(100) DEFAULT NULL,
  `fineamt` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `summon_records`
--

INSERT INTO `summon_records` (`id`, `carplate`, `carparkid`, `Stime`, `fineapplicable`, `seasonpark`, `offence`, `fineamt`) VALUES
('3562c690-d8d1-47b8-a4a7-539cdb989d8d', 'GBA4567B', '2', '2012-04-01T15:19:30.193-04:30', '0', '0', '18(1)', '50'),
('54b3c0f1-50e3-4b08-a82c-309c4f1290fd', 'GBA4567B', '1', '2012-04-01T15:21:41.552-04:30', '0', '0', '13', '50'),
('781bd326-fc62-46f6-a0c9-710fa1161ce2', 'GBA4567B', '3', '2012-04-01T15:20:25.762-04:30', '0', '0', '18(1)', '50'),
('bdc3517d-79ea-4136-b176-57dd6c83d92a', 'GBA4567B', '1', '2012-04-01T15:21:23.251-04:30', '0', '0', '12', '80');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
