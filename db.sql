CREATE DATABASE IF NOT EXISTS app;
use app;

CREATE TABLE IF NOT EXISTS `validation` (
  `tableName` varchar(45) NOT NULL,
  `columnName` varchar(45) NOT NULL,
  `validationStrategy` varchar(255) DEFAULT NULL,
  `strategyLogic` text,
  KEY `index1` (`tableName`,`columnName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
