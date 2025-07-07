CREATE TABLE if not EXISTS TB_STATUS_GROUP (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE if not EXISTS TB_STATUS_CODE (
    id VARCHAR(4) PRIMARY KEY,
    group_id INT NOT NULL,
    name VARCHAR(20) NOT NULL,
    CONSTRAINT fk_tb_status_group_id
        FOREIGN KEY (group_id)
        REFERENCES TB_STATUS_GROUP(id)
);

CREATE table if not EXISTS TB_MEMBER (
    uuid VARCHAR(36) PRIMARY KEY,
    member_role VARCHAR(4) NOT NULL,
    name VARCHAR(20) NOT NULL,
    rrn VARCHAR(15) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL UNIQUE,
    create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_date DATETIME,
    delete_date DATETIME,
    CONSTRAINT fk_tb_status_code_id
        FOREIGN KEY (member_role)
        REFERENCES TB_STATUS_CODE(id),
    CONSTRAINT chk_member_role_prefix
        CHECK (member_role LIKE 'R%')
);

CREATE TABLE if not EXISTS TB_MEMBER_CREDENTIALS (
    user_uuid VARCHAR(36) PRIMARY KEY,
    id VARCHAR(20) NOT NULL,
    password VARCHAR(20) NOT NULL,
    create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_date DATETIME,
    delete_date DATETIME,
    CONSTRAINT fk_tb_member_uuid
        FOREIGN KEY (user_uuid)
        REFERENCES TB_MEMBER(uuid)
);

CREATE TABLE if not EXISTS TB_RECEPTION (
    UUID VARCHAR(36) DEFAULT (UUID()) PRIMARY KEY,
    PATIENT_UUID VARCHAR(36),
    DOCTOR_UUID VARCHAR(36),
    TURN INT NOT NULL AUTO_INCREMENT,
    STATUS VARCHAR(4) DEFAULT 'RE01',
    NAME VARCHAR(20),
    RRN VARCHAR(15),
    SYMPTOM VARCHAR(300),
    CREATE_DATE DATETIME DEFAULT CURRENT_TIMESTAMP,
    UPDATE_DATE DATETIME,
    CONSTRAINT FK_TB_RECEPTION_STATUS_CODE FOREIGN KEY (STATUS)
        REFERENCES TB_STATUS_CODE(id),
    CONSTRAINT FK_TB_RECEPTION_MEMBER_UUID FOREIGN KEY (PATIENT_UUID)
        REFERENCES TB_MEMBER(uuid),
    CONSTRAINT FK_TB_RECEPTION_DOCTOR_UUID FOREIGN KEY (DOCTOR_UUID)
        REFERENCES TB_MEMBER(uuid),
    UNIQUE (TURN)
);

create table if not EXISTS TB_RESERVATION (
        ID VARCHAR(36) default (UUID()) PRIMARY key,
        PATIENT_UUID VARCHAR(36),
        DOCTOR_UUID VARCHAR(36),
        STATUS VARCHAR(4),
        RESERVATION_DATE DATETIME,
        SYMPTOM VARCHAR(300),
        CONSTRAINT FK_TB_RESERVATION_STATUS_CODE FOREIGN KEY (STATUS)
        REFERENCES TB_STATUS_CODE(id),
    	CONSTRAINT FK_TB_RESERVATION_MEMBER_UUID FOREIGN KEY (PATIENT_UUID)
        REFERENCES TB_MEMBER(uuid),
    	CONSTRAINT FK_TB_RESERVATION_DOCTOR_UUID FOREIGN KEY (DOCTOR_UUID)
        REFERENCES TB_MEMBER(uuid)
);

CREATE TABLE if not EXISTS `TB_TREATMENT` (
  `id` int(11) AUTO_INCREMENT,
  `treat_content` text DEFAULT NULL,
  `treat_date` date NOT NULL DEFAULT current_timestamp(),
  `uuid` varchar(36) NOT NULL,
  `write_yn` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`id`),
  KEY `TB_TREATMENT_TB_RECEPTION_FK` (`uuid`),
  CONSTRAINT `TB_TREATMENT_TB_RECEPTION_FK` FOREIGN KEY (`uuid`) REFERENCES `TB_RECEPTION` (`UUID`)
);

CREATE TABLE if not EXISTS `TB_MEDICINE` (
  `code` varchar(10) NOT NULL,
  `name` varchar(300) NOT NULL,
  `create_date` datetime NOT NULL DEFAULT current_timestamp(),
  `update_date` datetime DEFAULT NULL,
  `company_name` varchar(100) NOT NULL,
  PRIMARY KEY (`code`)
);

CREATE TABLE if not EXISTS `TB_DISEASE` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `code` varchar(10) NOT NULL,
  `create_date` datetime NOT NULL DEFAULT current_timestamp(),
  `update_date` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
);

CREATE TABLE if not EXISTS `TB_PRESCRIPTION` (
  `code` varchar(10) NOT NULL,
  `treatment_id` int(11) NOT NULL,
  `volume` int(11) NOT NULL,
  KEY `TB_PRESCRIPTION_TB_MEDICINE_FK` (`code`),
  KEY `TB_PRESCRIPTION_TB_TREATMENT_FK` (`treatment_id`),
  CONSTRAINT `TB_PRESCRIPTION_TB_MEDICINE_FK` FOREIGN KEY (`code`) REFERENCES `TB_MEDICINE` (`code`),
  CONSTRAINT `TB_PRESCRIPTION_TB_TREATMENT_FK` FOREIGN KEY (`treatment_id`) REFERENCES `TB_TREATMENT` (`id`)
);

CREATE TABLE if not EXISTS `TB_DIAGNOSIS` (
  `id` int(11) NOT NULL,
  `treatment_id` int(11) NOT NULL,
  KEY `TB_DIAGNOSIS_TB_TREATMENT_FK` (`treatment_id`),
  KEY `TB_DIAGNOSIS_TB_DISEASE_FK` (`id`),
  CONSTRAINT `TB_DIAGNOSIS_TB_DISEASE_FK` FOREIGN KEY (`id`) REFERENCES `TB_DISEASE` (`id`),
  CONSTRAINT `TB_DIAGNOSIS_TB_TREATMENT_FK` FOREIGN KEY (`treatment_id`) REFERENCES `TB_TREATMENT` (`id`)
);

CREATE TABLE if not EXISTS `TB_ATTACHMENT` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `treatment_id` varchar(36) NOT NULL,
  `original_name` varchar(100) NOT NULL,
  `file_name` varchar(100) NOT NULL,
  `path` varchar(300) NOT NULL,
  `size` int(11) NOT NULL,
  `content_type` varchar(100) NOT NULL,
  `extension` varchar(10) NOT NULL,
  `create_date` datetime NOT NULL DEFAULT current_timestamp(),
  `update_date` datetime DEFAULT NULL,
  `delete_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `TB_ATTACHMENT_TB_TREATMENT_FK` (`treatment_id`),
  CONSTRAINT `TB_ATTACHMENT_TB_TREATMENT_FK` FOREIGN KEY (`id`) REFERENCES `TB_TREATMENT` (`id`)
);

CREATE TABLE if not EXISTS `TB_PAYMENT` (
  `id` int(11) AUTO_INCREMENT,
  `treatment_id` varchar(36) NOT NULL,
  `payment_status` varchar(4) NOT NULL,
  `payment` int(11) NOT NULL,
  `payment_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `TB_PAYMENT_TB_STATUS_CODE_FK` FOREIGN KEY (`payment_status`) REFERENCES `TB_STATUS_CODE` (`id`)
);

CREATE TABLE if not EXISTS `TB_CHAT_ROOM` (
  `room_id` int(11) NOT NULL AUTO_INCREMENT,
  `room_name` varchar(100) NOT NULL,
  `create_date` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`room_id`)
);

CREATE TABLE if not EXISTS `TB_CHAT_JOIN` (
  `room_id` int(11) NOT NULL,
  `uuid` varchar(36) NOT NULL,
  `join_time` datetime DEFAULT NULL,
  `out_time` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`, `room_id`),
  CONSTRAINT `TB_CHAT_JOIN_TB_CHAT_ROOM_FK` FOREIGN KEY (`room_id`) REFERENCES `TB_CHAT_ROOM` (`room_id`),
  CONSTRAINT `TB_CHAT_JOIN_TB_MEMBER_FK` FOREIGN KEY (`uuid`) REFERENCES `TB_MEMBER` (`uuid`)
);


CREATE TABLE if not EXISTS `TB_CHAT_MESSAGE` (
  `message_id` int(11) NOT NULL AUTO_INCREMENT,
  `room_id` int(11) NOT NULL,
  `uuid` varchar(36) NOT NULL,
  `content` text DEFAULT NULL,
  `create_date` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`message_id`),
  CONSTRAINT `TB_CHAT_MESSAGE_TB_CHAT_JOIN_FK` FOREIGN KEY (`uuid`, `room_id`) REFERENCES `TB_CHAT_JOIN` (`uuid`, `room_id`)
);

CREATE TABLE if not EXISTS `TB_CHAT_READ` (
  `room_id` int(11) NOT NULL,
  `uuid` varchar(36) NOT NULL,
  `message_id` int(11) NOT NULL,
  `read_time` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`room_id`,`uuid`,`message_id`),
  CONSTRAINT `TB_CHAT_READE_TB_CHAT_JOIN_FK` FOREIGN KEY (`uuid`, `room_id`) REFERENCES `TB_CHAT_JOIN` (`uuid`, `room_id`),
  CONSTRAINT `TB_CHAT_READE_TB_CHAT_MESSAGE_FK` FOREIGN KEY (`message_id`) REFERENCES `TB_CHAT_MESSAGE` (`message_id`)
);

