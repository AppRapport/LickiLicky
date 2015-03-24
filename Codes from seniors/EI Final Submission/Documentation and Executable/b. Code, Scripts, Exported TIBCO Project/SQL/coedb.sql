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
-- Database: `coedb`
--

-- --------------------------------------------------------

--
-- Table structure for table `userdatabase`
--

CREATE TABLE IF NOT EXISTS `userdatabase` (
  `CarplateNo` longtext NOT NULL,
  `CarOwnerName` longtext NOT NULL,
  `CarOwnerIC` longtext NOT NULL,
  `CarOwnerAddress` longtext NOT NULL,
  `CarType` longtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `userdatabase`
--

INSERT INTO `userdatabase` (`CarplateNo`, `CarOwnerName`, `CarOwnerIC`, `CarOwnerAddress`, `CarType`) VALUES
('GBA4567B', 'Tay Wen Bin', 'S1234567A', 'SMU SIS', 'Car');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
