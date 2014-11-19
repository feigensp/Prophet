-- phpMyAdmin SQL Dump
-- version 2.11.8.1deb5+lenny6
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 14. Dezember 2010 um 23:46
-- Server Version: 5.0.51
-- PHP-Version: 5.2.6-1+lenny9

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `markus_quelltextproj`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `answers`
--

CREATE TABLE IF NOT EXISTS `answers` (
  `subject` int(11) NOT NULL,
  `node` int(11) NOT NULL,
  `key` text NOT NULL,
  `value` text NOT NULL,
  PRIMARY KEY  (`subject`,`node`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `answers_meta`
--

CREATE TABLE IF NOT EXISTS `answers_meta` (
  `subject` int(11) NOT NULL,
  `node` int(11) NOT NULL,
  `time` int(11) NOT NULL,
  PRIMARY KEY  (`subject`,`node`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `experiments`
--

CREATE TABLE IF NOT EXISTS `experiments` (
  `id` int(11) NOT NULL auto_increment,
  `code` varchar(20) NOT NULL,
  `root_node` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `code` (`code`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `nodes`
--

CREATE TABLE IF NOT EXISTS `nodes` (
  `id` int(11) NOT NULL,
  `experiment` int(11) NOT NULL,
  `name` varchar(20) NOT NULL,
  `content` text NOT NULL,
  `notes` text NOT NULL,
  `previous` int(11) NOT NULL default '-1',
  `next` int(11) NOT NULL default '-1',
  PRIMARY KEY  (`id`),
  KEY `experiment` (`experiment`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `subject`
--

CREATE TABLE IF NOT EXISTS `subject` (
  `id` int(11) NOT NULL auto_increment,
  `experiment` int(20) NOT NULL,
  `code` varchar(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `experiment` (`experiment`,`code`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;
