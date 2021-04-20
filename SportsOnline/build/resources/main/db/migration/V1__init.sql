/*============================================================================*/
/* DBMS: MySQL 5*/
/* Created on : 02/08/2019 15:28:19                                           */
/*============================================================================*/


/*============================================================================*/
/*                                  TABLES                                    */
/*============================================================================*/
CREATE TABLE `T_CATEGORY` ( 
  `ID`                            VARCHAR(10) NOT NULL,
  `NAME`                          VARCHAR(40),
  `DESCRIPTION`                   VARCHAR(255),
CONSTRAINT `PK_T_CATEGORY` PRIMARY KEY (`ID`)
)  ENGINE=InnoDB
;

CREATE TABLE `T_PRODUCT` ( 
  `ID`         VARCHAR(10) NOT NULL,
  `NAME`       VARCHAR(40),
  `DESCRIPTION` VARCHAR(255),
  `CATEGORY_FK` VARCHAR(10),
CONSTRAINT `PK_T_PRODUCT` PRIMARY KEY (`ID`)
)  ENGINE=InnoDB
;

CREATE TABLE `T_ITEM` ( 
  `ID`         VARCHAR(10) NOT NULL,
  `NAME`       VARCHAR(40),
  `BIRTH_DATE` DATE,
  `UNIT_COST`  DECIMAL(8,2),             
  `PRODUCT_FK` VARCHAR(10),
  `IMAGE_PATH` VARCHAR(255),
CONSTRAINT `PK_T_ITEM` PRIMARY KEY (`ID`)
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

CREATE TABLE `T_ORDER` ( 
  `ID`         BIGINT NOT NULL,
  `ORDER_DATE` TIMESTAMP(2) NOT NULL,
  `FIRSTNAME`  VARCHAR(50) NOT NULL,
  `LASTNAME`   VARCHAR(50) NOT NULL,
  `STREET1`    VARCHAR(50) NOT NULL,
  `STREET2`    VARCHAR(50),
  `CITY`       VARCHAR(25) NOT NULL,
  `STATE`      VARCHAR(25),
  `ZIPCODE`    VARCHAR(10) NOT NULL,
  `COUNTRY`    VARCHAR(25) NOT NULL,
  `CREDIT_CARD_NUMBER` VARCHAR(25),
  `CREDIT_CARD_TYPE` VARCHAR(25),
  `CREDIT_CARD_EXPIRY_DATE` VARCHAR(10),
  `USER_FK`    VARCHAR(10),
CONSTRAINT `PK_T_ORDER` PRIMARY KEY (`ID`)
)  ENGINE=InnoDB
;

CREATE TABLE `T_ORDER_LINE` ( 
  `ID`         BIGINT NOT NULL,
  `QUANTITY`   INT NOT NULL,
  `UNIT_COST`  DECIMAL(8,2),
  `ITEM_FK`    VARCHAR(10),
  `ORDER_FK`   BIGINT,
CONSTRAINT `PK_T_ORDER_LINE` PRIMARY KEY (`ID`)
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
ALTER TABLE `T_PRODUCT`
    ADD CONSTRAINT `FK_CATEGORY_FK`
        FOREIGN KEY (`CATEGORY_FK`)
            REFERENCES `T_CATEGORY` (`ID`)
 ;
 

ALTER TABLE `T_ITEM`
    ADD CONSTRAINT `FK_PRODUCT_FK`
        FOREIGN KEY (`PRODUCT_FK`)
            REFERENCES `T_PRODUCT` (`ID`)
 ;
 

ALTER TABLE `T_USER`
    ADD CONSTRAINT `FK_ROLE_FK`
        FOREIGN KEY (`ROLE_FK`)
            REFERENCES `T_ROLE` (`ID`)
 ;
 

ALTER TABLE `T_ORDER`
    ADD CONSTRAINT `FK_USER_FK`
        FOREIGN KEY (`USER_FK`)
            REFERENCES `T_USER` (`ID`)
 ;
 

ALTER TABLE `T_ORDER_LINE`
    ADD CONSTRAINT `FK_ITEM_FK`
        FOREIGN KEY (`ITEM_FK`)
            REFERENCES `T_ITEM` (`ID`)
 ;
 
ALTER TABLE `T_ORDER_LINE`
    ADD CONSTRAINT `FK_ORDER_FK`
        FOREIGN KEY (`ORDER_FK`)
            REFERENCES `T_ORDER` (`ID`)
 ;
  
 
-- Cleanup
DELETE FROM T_ORDER_LINE;
DELETE FROM T_ORDER;
DELETE FROM T_USER;
DELETE FROM T_ITEM;
DELETE FROM T_PRODUCT;
DELETE FROM T_CATEGORY;
DELETE FROM T_COUNTER;
DELETE FROM T_ROLE;

-- Load
INSERT INTO T_CATEGORY VALUES ('FISH', 'Fish', 'Any of numerous cold-blooded aquatic vertebrates characteristically having fins, gills, and a streamlined body' ),
('DOGS', 'Dogs', 'A domesticated carnivorous mammal related to the foxes and wolves and raised in a wide variety of breeds' ),
('REPTILES', 'Reptiles', 'Any of various cold-blooded, usually egg-laying vertebrates, such as a snake, lizard, crocodile, turtle' ),
('CATS', 'Cats', ' Small carnivorous mammal domesticated since early times as a catcher of rats and mice and as a pet and existing in several distinctive breeds and varieties' ),
('BIRDS', 'Birds', 'Any of the class Aves of warm-blooded, egg-laying, feathered vertebrates with forelimbs modified to form wings' );

INSERT INTO T_PRODUCT VALUES ('FISW01', 'Angelfish', 'Saltwater fish from Australia', 'FISH'),
('FISW02', 'Tiger Shark', 'Saltwater fish from Australia', 'FISH'),
('FIFW01', 'Koi', 'Freshwater fish from Japan', 'FISH'),
('FIFW02', 'Goldfish', 'Freshwater fish from China', 'FISH'),
('K9BD01', 'Bulldog', 'Friendly dog from England', 'DOGS'),
('K9PO02', 'Poodle', 'Cute dog from France', 'DOGS'),
('K9DL01', 'Dalmation', 'Great dog for a fire station', 'DOGS'),
('K9RT01', 'Golden Retriever', 'Great family dog', 'DOGS'),
('K9RT02', 'Labrador Retriever', 'Great hunting dog', 'DOGS'),
('K9CW01', 'Chihuahua', 'Great companion dog', 'DOGS'),
('RPSN01', 'Rattlesnake', 'Doubles as a watch dog', 'REPTILES'),
('RPLI02', 'Iguana', 'Friendly green friend', 'REPTILES'),
('FLDSH01', 'Manx', 'Great for reducing mouse populations', 'CATS'),
('FLDLH02', 'Persian', 'Friendly house cat, doubles as a princess', 'CATS'),
('AVCB01', 'Amazon Parrot', 'Great companion for up to 75 years', 'BIRDS'),
('AVSB02', 'Finch', 'Great stress reliever', 'BIRDS');

INSERT INTO `T_ITEM` VALUES ('EST1','Large','2020-12-15',10.00,'FISW01','fish1.jpg'),
('EST2','Thootless','2020-10-15',10.00,'FISW01','fish1.jpg'),
('EST3','Spotted','2020-11-15',12.00,'FISW02','fish4.jpg'),
('EST4','Spotless','2019-12-15',12.00,'FISW02','fish4.jpg'),
('EST5','Male Adult','2019-11-15',12.00,'FIFW01','fish3.jpg'),
('EST6','Female Adult','2020-08-15',12.00,'FIFW01','fish3.jpg'),
('EST7','Male Puppy','2018-12-15',12.00,'FIFW02','fish2.jpg'),
('EST8','Female Puppy','2019-08-15',12.00,'FIFW02','fish2.jpg'),
('EST9','Spotless Male Puppy','2018-12-15',22.00,'K9BD01','dog1.jpg'),
('EST10','Spotless Female Puppy','2020-01-15',22.00,'K9BD01','dog1.jpg'),
('EST11','Spotted Male Puppy','2020-02-15',32.00,'K9PO02','dog2.jpg'),
('EST12','Spotted Female Puppy','2020-03-15',32.00,'K9PO02','dog2.jpg'),
('EST13','Tailed','2019-11-15',62.00,'K9DL01','dog3.jpg'),
('EST14','Tailless','2019-01-15',62.00,'K9DL01','dog3.jpg'),
('EST15','Tailed','2019-05-15',82.00,'K9RT01','dog4.jpg'),
('EST16','Tailless','2019-06-15',82.00,'K9RT01','dog4.jpg'),
('EST17','Tailed','2019-07-15',100.00,'K9RT02','dog5.jpg'),
('EST18','Tailless','2019-04-15',100.00,'K9RT02','dog5.jpg'),
('EST19','Female Adult','2019-09-15',100.00,'K9CW01','dog6.jpg'),
('EST20','Female Adult','2019-02-15',110.00,'K9CW01','dog6.jpg'),
('EST21','Female Adult','2020-01-15',20.00,'RPSN01','snake1.jpg'),
('EST22','Male Adult','2020-11-15',20.00,'RPSN01','snake1.jpg'),
('EST211','Female Adult','2017-01-15',20.00,'RPLI02','lizard1.jpg'),
('EST221','Male Adult','2016-01-15',20.00,'RPLI02','lizard1.jpg'),
('EST23','Male Adult','2020-09-15',120.00,'FLDSH01','cat1.jpg'),
('EST24','Female Adult','2020-01-15',120.00,'FLDSH01','cat1.jpg'),
('EST231','Male Adult','2020-10-15',75.00,'FLDLH02','cat2.jpg'),
('EST241','Female Adult','2020-08-15',80.00,'FLDLH02','cat2.jpg'),
('EST25','Male Adult','2020-03-15',120.00,'AVCB01','bird2.jpg'),
('EST26','Female Adult','2020-09-15',120.00,'AVCB01','bird2.jpg'),
('EST27','Male Adult','2020-10-15',80.00,'AVSB02','bird1.jpg'),
('EST28','Female Adult','2019-02-15',70.00,'AVSB02','bird1.jpg');

INSERT INTO T_ROLE VALUES('1','ROLE_ADMIN'),
('2','ROLE_FRANCHISEE'),
('3','ROLE_USER');

INSERT INTO T_USER VALUES ('marc123', 'Marc', 'Fleury', '545 123 45', '65 Ritherdon Road', '', 'Los Angeles', 'LA', '56421', 'USA', '', '', '', '','$2a$10$id93M61FLpdk9sXOBzZlsuhNETXn7YsjyBZs3X09Ll7f5S3lqpee2','3'),
('bill000', 'Bill', 'Gates', '654 046 12', '27 West Side', 'Story', 'Alhabama', 'Texas', '8401', 'USA', '1231 4564 2222', 'Visa', '02/14', 'bill.gates@microsoft.com','$2a$10$id93M61FLpdk9sXOBzZlsuhNETXn7YsjyBZs3X09Ll7f5S3lqpee2','3'),
('job5', 'Steve', 'Jobs', '548 157 15', '154 Star Boulevard', '', 'San Francisco', 'WC', '5455', 'USA', '', '', '', 'steve.jobs@apple.com','$2a$10$id93M61FLpdk9sXOBzZlsuhNETXn7YsjyBZs3X09Ll7f5S3lqpee2','3'),
('stb01', 'Stéphane', 'Bruyère', '', '', '', '', '', '', '', '', '', '', 'stb@gmail.com','$2a$10$id93M61FLpdk9sXOBzZlsuhNETXn7YsjyBZs3X09Ll7f5S3lqpee2','1'),
('marco01', 'Marc', 'Zurckerberg', '', '', '', '', '', '', '', '', '', '', 'mz@facebook.com','$2a$10$id93M61FLpdk9sXOBzZlsuhNETXn7YsjyBZs3X09Ll7f5S3lqpee2','2'),
('jeff01', 'Jeff', 'Bezos', '', '', '', '', '', '', '', '', '', '', 'jeff@amazon.com','$2a$10$id93M61FLpdk9sXOBzZlsuhNETXn7YsjyBZs3X09Ll7f5S3lqpee2','2');
