/*============================================================================*/
/* DBMS: MySQL 5*/
/* Created on : 21/04/2021  à 20h00                                           */
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
  `DATE` 	DATE,
  `CREDITCOST`  INT(10), 
  `DURATION` INT(10),  
  `DESCRIPTION` VARCHAR(255),
  `INTENSITY` VARCHAR(40),
  `EQUIPMENT` VARCHAR(255), 
  `USER_FK` VARCHAR(10),      
  `ACTIVITY_FK` VARCHAR(10),
CONSTRAINT `PK_T_EVENT` PRIMARY KEY (`ID`)
)  ENGINE=InnoDB
;

CREATE TABLE `T_NOTATION` ( 
  `ID`         VARCHAR(10) NOT NULL,
  `DATE` DATE,
  `NOTE` INT(10),  
  `COMMENTS` VARCHAR(255), 
  `ATHLETE_FK` VARCHAR(10), 
  `COACH_FK` VARCHAR(10),      
  `EVENT_FK` VARCHAR(10),
CONSTRAINT `PK_T_NOTATION` PRIMARY KEY (`ID`)
)  ENGINE=InnoDB
;

CREATE TABLE `T_USER` ( 
  `ID`         VARCHAR(10) NOT NULL,
  `FIRSTNAME`  VARCHAR(40) NOT NULL,
  `LASTNAME`   VARCHAR(40) NOT NULL,
  `TELEPHONE`  VARCHAR(10),
  `ADDRESS1`   VARCHAR(50),
  `ADDRESS2`   VARCHAR(50),
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
INSERT INTO T_DISCIPLINE VALUES ('BOXE', 'Boxe', 'Renforcement du corps, canaliser son énergie et se surpasser', 'Brevet professionnel de la jeunesse, de l\'éducation populaire et du sport. Spécialité educateur sportif, mention Boxe' ),
('CARDIO', 'Cardio', 'Description', 'Documents' ),
('DANSE', 'Danse', 'Description', 'Documents' ),
('GYM', 'Gym', ' Description', 'Documents' ),
('YOGA', 'Yoga', 'Description ', 'Documents' ),
('MUSCU', 'Musculation', 'Description', 'Documents' );

INSERT INTO T_ACTIVITY VALUES ('COMB', 'Combat training', 'Mouvement de boxe, corde à sauter et travail de la silhouette ', '0', '2', 'BOXE'),
('BODY', 'Bodysculpt', 'Tonifier sa silhouette, decharger son stress et perdre du poids', '0', '2', 'BOXE'),
('CARDBOX', 'Cardio boxe', 'Travail cardio basé sur des mouvements d\'art martiaux', '0', '2', 'BOXE'),
('CARDCYCL', 'Cardio cycling', 'Entraînement en musique sur vélos stationnaires', '0', '2', 'CARDIO'),
('CARDPULS', 'Cardio pulse', 'Préparation physique complète en travaillant 5 qualités: force, agilité, coordination, explosivité, mobilité', '0', '2', 'CARDIO'),
('CARDHIT', 'Cardio hit', 'Travail de façon très courte, mais en prenant également des temps de repos court', '0', '2', 'CARDIO'),
('CARDJUMP', 'Cardio jump', 'Cours cardio sur mini-trampoline challenge les cuisses , les fessiers et les abdos', '0', '2', 'CARDIO'),
('CARDTHREAD', 'Cardio threadmill', 'Course à pied sur tapis, avec alternance des phases de travail en intensité et de récupération active adaptées à tous les niveaux', '0', '2', 'CARDIO'),
('STEPSOFT', 'Step soft', 'Successions de chorégraphies courtes, simples réalisées autour d\'un step', '0', '2', 'DANSE'),
('STEPXPRT', 'Step expert', 'Rythmé par plusieurs chansons, autour d’une seule chorégraphie à apprendre et mémoriser pendant le cours', '0', '2', 'DANSE'),
('AEROSOFT', 'Aero soft', 'Successions de chorégraphies courtes, simples réalisées sur des musiques tendances et actuelles', '0', '2', 'DANSE'),
('AEROXPRT', 'Aero expert', 'Rythmé par plusieurs chansons autour d\’une seule chorégraphie à apprendre et mémoriser pendant la séance', '0', '2', 'DANSE'),
('DANSBALL', 'Danse ballet', 'Sur des airs de danse classique, j\’évolue comme une vraie ballerine chorégraphies après chorégraphie', '0', '2', 'DANSE'),
('DANSEMEGA', 'Danse megamix', 'Cours de danse qui permet de brûler des calories sans vous en rendre compte car le plaisir de danser prendra le dessus', '0', '2', 'DANSE'),
('ABDOS', 'Abominaux', 'Permet de travailler l\’ensemble des muscles de la sangle abdominale sous la forme d\’exercices ciblés', '0', '2', 'GYM'),
('BODYTONE', 'Bodytone', 'Cours de renforcement musculaire dans lequel l\'utilisation d\’un ballon permet l\'alternance de mouvements dynamiques et d\’exercices de stabilité', '0', '2', 'GYM'),
('GYMLGT', 'Gym light', 'Permet de me tonifier et d\’entretenir mon corps quel que soit mon niveau sportif', '0', '2', 'GYM'),
('GYMVIT', 'Gym vitality', 'La GYMVITALITY développe les capacités physiques selon le rythme de chacun', '0', '2', 'GYM'),
('PILATE', 'Pilate', 'Travail autour du réalignement posturale et de l\'amélioration des mouvements de la vie quotidienne', '0', '2', 'GYM'),
('STRETCH', 'Strecthing', 'Soulage toutes les tensions du quotidien. Cette activité douce permet de retrouver de la souplesse et de travailler les articulations et les muscles de façon saine', '0', '2', 'GYM'),
('HATHA', 'Hathayoga', 'Le Hatha Yoga célèbre et entretient l\’équilibre : entre corps et esprit, mais aussi entre les différentes parties de votre corps', '0', '2', 'YOGA'),
('YINYOG', 'Yinyoga', 'Le Yin yoga se fonde sur les principes taoïstes anciens du yin et du yang. Le yin est stable et immuable, tandis que le yang est constamment en mouvement et en développement', '0', '2', 'YOGA'),
('YOGDYN', 'Yoga dynamic', 'Le yoga Ashtanga est une pratique de yoga dynamique où les postures à difficulté progressive s\'enchaînent à un rythme soutenu', '0', '2', 'YOGA'),
('ASHTANGA', 'Yoga ashtanga', 'Apprenez à connaître votre corps pour mieux le contrôler', '0', '2', 'YOGA'),
('YOGMORN', 'Yoga morning', 'Une mise en train matinale bienveillante et progressive', '0', '2', 'YOGA'),
('YOGANID', 'Yoganidra', 'Le Yoga Nidra, appelé aussi yoga du sommeil, est une séance de méditation et de relaxation de 50 minutes', '0', '2', 'YOGA'),
('VINYASA', 'Yoga vinyasa', 'Ce cours de yoga Vinyasa permet de détendre le corps et l\’esprit', '0', '2', 'YOGA'),
('MUSCBSPLT', 'Body sculpt', 'Complet, rapide et efficace, le BODYSCULPT favorise ma perte de poids et redessine mes muscles', '0', '2', 'MUSCU'),
('MUSCBXPRT', 'Body expert', 'Challenge ultime des sportifs complets, le cours de renforcement musculaire BODYXPERT est construit sur l\'enchaînement dynamique de différents exercices sollicitant l\'ensemble de mes groupes musculaires !', '0', '2', 'MUSCU'),
('TRAINXPRT', 'Training expert', 'Un entrainement complet et fonctionnel pour se depasser', '0', '2', 'MUSCU');

INSERT INTO T_ROLE VALUES('1','ROLE_ADMIN'),
('2','ROLE_COACH'),
('3','ROLE_ATHLETE');

INSERT INTO T_USER VALUES('MrRobot','Elliot', 'Alderson', '','','','','','','','100','VALIDE','','','','elliotalderson@protonmail.com','$2a$10$id93M61FLpdk9sXOBzZlsuhNETXn7YsjyBZs3X09Ll7f5S3lqpee2','1');
