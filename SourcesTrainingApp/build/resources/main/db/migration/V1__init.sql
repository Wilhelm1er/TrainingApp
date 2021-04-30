/*============================================================================*/
/* DBMS: MySQL 5*/
/* Created on : 21/04/2021                                           */
/*============================================================================*/


/*============================================================================*/
/*                                  TABLES                                    */
/*============================================================================*/
CREATE TABLE `T_DISCIPLINE` ( 
  `ID`                            VARCHAR(10) NOT NULL,
  `NAME`                          VARCHAR(40),
  `DESCRIPTION`                   VARCHAR(255),
  `DOCUMENTS`                     VARCHAR(255),
CONSTRAINT `PK_T_DISCIPLINE` PRIMARY KEY (`ID`)
)  ENGINE=InnoDB
;

CREATE TABLE `T_ACTIVITY` ( 
  `ID`         VARCHAR(10) NOT NULL,
  `NAME`       VARCHAR(40),
  `DESCRIPTION` VARCHAR(255),
  `CREDITCOSTMIN` INT(10),
  `CREDITCOSTMAX` INT(10),
  `DISCIPLINE_FK` VARCHAR(10),
CONSTRAINT `PK_T_ACTIVITY` PRIMARY KEY (`ID`)
)  ENGINE=InnoDB
;

CREATE TABLE `T_EVENT` ( 
  `ID`         VARCHAR(10) NOT NULL,
  `NAME`       VARCHAR(40),
  `TIMETABLE` DATE,
  `CREDITCOST`  INT(10), 
  `DURATION` INT(10),  
  `DESCRIPTION` VARCHAR(255),
  `EQUIPMENT` VARCHAR(255), 
  `USER_FK` VARCHAR(10),      
  `ACTIVITY_FK` VARCHAR(10),
CONSTRAINT `PK_T_EVENT` PRIMARY KEY (`ID`)
)  ENGINE=InnoDB
;

CREATE TABLE `T_USER` ( 
  `ID`         VARCHAR(10) NOT NULL,
  `FIRSTNAME`  VARCHAR(40) NOT NULL,
  `LASTNAME`   VARCHAR(40) NOT NULL,
  `TELEPHONE`  VARCHAR(10),
  `STREET1`    VARCHAR(50),
  `STREET2`    VARCHAR(50),
  `CITY`       VARCHAR(25),
  `STATE`      VARCHAR(25),
  `ZIPCODE`    VARCHAR(10),
  `COUNTRY`    VARCHAR(25),
  `CREDIT`     INT(10),
  `STATUT`     VARCHAR(25) NOT NULL,
  `CREDIT_CARD_NUMBER` VARCHAR(25),
  `CREDIT_CARD_TYPE` VARCHAR(25),
  `CREDIT_CARD_EXPIRY_DATE` VARCHAR(10),
  `EMAIL`      VARCHAR(255),
  `PASSWORD`   VARCHAR(128) NOT NULL,
  `ROLE_FK`    INT,
CONSTRAINT `PK_T_USER` PRIMARY KEY (`ID`)
)  ENGINE=InnoDB
;

CREATE TABLE `T_COUNTER` ( 
  `NAME`       VARCHAR(10) NOT NULL,
  `VALUE`      INT,
CONSTRAINT `PK_T_COUNTER` PRIMARY KEY (`NAME`)
)  ENGINE=InnoDB
;


CREATE TABLE `T_ROLE` ( 
  `ID`         INT NOT NULL,
  `NAME`       VARCHAR(20),
CONSTRAINT `PK_T_ROLE` PRIMARY KEY (`ID`)
)  ENGINE=InnoDB
;

/*============================================================================*/
/*                               FOREIGN KEYS                                 */
/*============================================================================*/
ALTER TABLE `T_ACTIVITY`
    ADD CONSTRAINT `FK_DISCIPLINE_FK`
        FOREIGN KEY (`DISCIPLINE_FK`)
            REFERENCES `T_DISCIPLINE` (`ID`)
 ;
 

ALTER TABLE `T_EVENT`
    ADD CONSTRAINT `FK_ACTIVITY_FK`
        FOREIGN KEY (`ACTIVITY_FK`)
            REFERENCES `T_ACTIVITY` (`ID`)
 ;
 
ALTER TABLE `T_EVENT`
  ADD CONSTRAINT `FK_USER_FK`
        FOREIGN KEY (`USER_FK`)
            REFERENCES `T_USER` (`ID`)
 ;

ALTER TABLE `T_USER`
    ADD CONSTRAINT `FK_ROLE_FK`
        FOREIGN KEY (`ROLE_FK`)
            REFERENCES `T_ROLE` (`ID`)
 ;
 
-- Cleanup
DELETE FROM T_USER;
DELETE FROM T_EVENT;
DELETE FROM T_ACTIVITY;
DELETE FROM T_DISCIPLINE;
DELETE FROM T_COUNTER;
DELETE FROM T_ROLE;

-- Load
INSERT INTO T_DISCIPLINE VALUES ('BOXE', 'Boxe', 'Description', 'Documents' ),
('CARDIO', 'Cardio', 'Description', 'Documents' ),
('DANSE', 'Danse', 'Description', 'Documents' ),
('GYM', 'Gym', ' Description', 'Documents' ),
('YOGA', 'Yoga', 'Description ', 'Documents' ),
('MUSCU', 'Musculation', 'Description', 'Documents' );

INSERT INTO T_ACTIVITY VALUES ('COMB', 'Combat training', 'Description', '0', '2', 'BOXE'),
('BODY', 'Bodysculpt', 'Description', '0', '2', 'BOXE'),
('CARDBOX', 'Cardio boxe', 'Description', '0', '2', 'BOXE'),
('CARDCYCL', 'Cardio cycling', 'Description', '0', '2', 'CARDIO'),
('CARDPULS', 'Cardio pulse', 'Description', '0', '2', 'CARDIO'),
('CARDHIT', 'Cardio hit', 'Description', '0', '2', 'CARDIO'),
('CARDJUMP', 'Cardio jump', 'Description', '0', '2', 'CARDIO'),
('CARDTHREAD', 'Cardio threadmill', 'Description', '0', '2', 'CARDIO'),
('STEPSOFT', 'Step soft', 'Description', '0', '2', 'DANSE'),
('STEPXPRT', 'Step expert', 'Description', '0', '2', 'DANSE'),
('AEROSOFT', 'Aero soft', 'Description', '0', '2', 'DANSE'),
('AEROXPRT', 'Aero expert', 'Description', '0', '2', 'DANSE'),
('DANSBALL', 'Danse ballet', 'Description', '0', '2', 'DANSE'),
('DANSEMEGA', 'Danse megamix', 'Description', '0', '2', 'DANSE'),
('ABDOS', 'Abominaux', 'Description', '0', '2', 'GYM'),
('BODYTONE', 'Bodytone', 'Description', '0', '2', 'GYM'),
('GYMLGT', 'Gym light', 'Description', '0', '2', 'GYM'),
('GYMVIT', 'Gym vitality', 'Description', '0', '2', 'GYM'),
('PILATE', 'Pilate', 'Description', '0', '2', 'GYM'),
('STRETCH', 'Strecthing', 'Description', '0', '2', 'GYM'),
('HATHA', 'Hathayoga', 'Description', '0', '2', 'YOGA'),
('YINYOG', 'Yinyoga', 'Description', '0', '2', 'YOGA'),
('YOGDYN', 'Yoga dynamic', 'Description', '0', '2', 'YOGA'),
('ASHTANGA', 'Yoga ashtanga', 'Description', '0', '2', 'YOGA'),
('YOGMORN', 'Yoga morning', 'Description', '0', '2', 'YOGA'),
('YOGANID', 'Yoganidra', 'Description', '0', '2', 'YOGA'),
('VINYASA', 'Yoga vinyasa', 'Description', '0', '2', 'YOGA'),
('MUSCBSPLT', 'Body sculpt', 'Description', '0', '2', 'MUSCU'),
('MUSCBXPRT', 'Body expert', 'Description', '0', '2', 'MUSCU'),
('TRAINXPRT', 'Training expert', 'Description', '0', '2', 'MUSCU');

INSERT INTO T_ROLE VALUES('1','ROLE_ADMIN'),
('2','ROLE_COACH'),
('3','ROLE_ATHLETE');
