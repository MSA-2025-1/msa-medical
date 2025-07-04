CREATE TABLE `TB_TREATMENT` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_uuid` varchar(36) NOT NULL,
  `doctor_uuid` varchar(36) NOT NULL,
  `status` varchar(4) NOT NULL,
  `treat_content` text NOT NULL,
  `treat_date` date NOT NULL DEFAULT current_timestamp(),
  `treat_write_date` datetime NOT NULL DEFAULT current_timestamp(),
  `reservation_date` datetime NOT NULL DEFAULT current_timestamp(),
  `payment_date` datetime DEFAULT NULL,
  `payment` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `TB_TREATMENT_TB_MEMBER_FK` (`patient_uuid`),
  KEY `TB_TREATMENT_TB_MEMBER_FK_1` (`doctor_uuid`),
  KEY `TB_TREATMENT_TB_STATUS_CODE_FK` (`status`),
  CONSTRAINT `TB_TREATMENT_TB_MEMBER_FK` FOREIGN KEY (`patient_uuid`) REFERENCES `TB_MEMBER` (`uuid`),
  CONSTRAINT `TB_TREATMENT_TB_MEMBER_FK_1` FOREIGN KEY (`doctor_uuid`) REFERENCES `TB_MEMBER` (`uuid`),
  CONSTRAINT `TB_TREATMENT_TB_STATUS_CODE_FK` FOREIGN KEY (`status`) REFERENCES `TB_STATUS_CODE` (`id`),
  CONSTRAINT `chk_status_prefix` CHECK (`status` like 'RE%')
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;