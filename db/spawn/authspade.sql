-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Počítač: 127.0.0.1
-- Vytvořeno: Ned 21. dub 2024, 10:34
-- Verze serveru: 10.4.32-MariaDB
-- Verze PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Databáze: `authspade`
--
CREATE DATABASE IF NOT EXISTS `authspade` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `authspade`;

-- --------------------------------------------------------

--
-- Struktura tabulky `app_metadata`
--

CREATE TABLE `app_metadata` (
  `id` int(11) NOT NULL,
  `appDataKey` varchar(255) DEFAULT NULL,
  `appDataValue` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Vypisuji data pro tabulku `app_metadata`
--

INSERT INTO `app_metadata` (`id`, `appDataKey`, `appDataValue`) VALUES
(1, 'basics', '[{\r\n        \"version\": \"1.0.0\",\r\n        \"authors\": [{\r\n            \"name\": \"Ondřej Váně\",\r\n            \"email\": \"vaneo@students.zcu.cz\"\r\n        }],\r\n        \"description\": \"This application is used to detect presence of anti-patterns in project management tools data. Seven selected anti-patterns are implemented in this application.\"\r\n    }, {\r\n        \"version\": \"1.1.0\",\r\n        \"authors\": [{\r\n            \"name\": \"Ondřej Váně\",\r\n            \"email\": \"vaneo@students.zcu.cz\"\r\n        }],\r\n        \"description\": \"This application is used to detect presence of anti-patterns in project management tools data. Seven selected anti-patterns are implemented in this application.\"\r\n    }, {\r\n        \"version\": \"1.2.0\",\r\n        \"authors\": [{\r\n            \"name\": \"Petr Štěpánek\",\r\n            \"email\": \"petrs1@students.zcu.cz\"\r\n        }],\r\n        \"description\": \"This application is used to detect presence of anti-patterns in project management tools data. Ten selected anti-patterns are implemented in this application.\"\r\n    }, {\r\n        \"version\": \"2.0.0\",\r\n        \"authors\": [{\r\n            \"name\": \"Petr Štěpánek\",\r\n            \"email\": \"petrs1@students.zcu.cz\"\r\n        }, {\r\n            \"name\": \"Petr Urban\",\r\n            \"email\": \"urbanp@students.zcu.cz\"\r\n        }, {\r\n            \"name\": \"Jiří Trefil\",\r\n            \"email\": \"trefil@students.zcu.cz\"\r\n        }, {\r\n            \"name\": \"Václav Hrabík\",\r\n            \"email\": \"hrabikv@students.zcu.cz\"\r\n        }],\r\n        \"description\": \"Experience the next evolution of our application with version 2.0.0, featuring a revamped infrastructure, three distinct apps, MS SQL Server integration, a comprehensive user provider system, and contract testing for enhanced reliability.\"\r\n    }]');

-- --------------------------------------------------------

--
-- Struktura tabulky `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Vypisuji data pro tabulku `users`
--

INSERT INTO `users` (`id`, `email`, `name`, `password`) VALUES
(1, NULL, 'test', NULL);

--
-- Indexy pro exportované tabulky
--

--
-- Indexy pro tabulku `app_metadata`
--
ALTER TABLE `app_metadata`
  ADD PRIMARY KEY (`id`);

--
-- Indexy pro tabulku `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT pro tabulky
--

--
-- AUTO_INCREMENT pro tabulku `app_metadata`
--
ALTER TABLE `app_metadata`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pro tabulku `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
