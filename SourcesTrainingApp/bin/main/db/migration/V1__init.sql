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

CREATE TABLE `T_DISCIPLINE_REGISTRY` ( 
  `ID`                            BIGINT NOT NULL,
  `REGISTER_DATE`   TIMESTAMP(2) NOT NULL DEFAULT CURRENT_TIMESTAMP(2) ON UPDATE CURRENT_TIMESTAMP(2),
  `COACH_FK`                   	  VARCHAR(10),
  `DISCIPLINE_FK`                 VARCHAR(10),
  `Doc_STATUT` VARCHAR(10) DEFAULT NULL,
CONSTRAINT `PK_T_DISCIPLINE_REGISTRY` PRIMARY KEY (`ID`)
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
  `ID`         BIGINT NOT NULL,
  `NAME`       VARCHAR(40),
  `DATE` 	DATETIME(6),
  `CREDITCOST`  INT(10), 
  `DURATION` INT(10),  
  `DESCRIPTION` VARCHAR(255),
  `INTENSITY` INT(10),
  `EQUIPMENT` VARCHAR(255), 
  `VOIDABLE` INT(10) DEFAULT NULL,
  `COACH_FK` VARCHAR(10),      
  `ACTIVITY_FK` VARCHAR(10),
CONSTRAINT `PK_T_EVENT` PRIMARY KEY (`ID`)
)  ENGINE=InnoDB
;

CREATE TABLE `T_EVENT_REGISTRY` ( 
  `ID`                            BIGINT NOT NULL,
  `REGISTER_DATE`   TIMESTAMP(2) NOT NULL DEFAULT CURRENT_TIMESTAMP(2) ON UPDATE CURRENT_TIMESTAMP(2),
  `USER_FK`                   	  VARCHAR(10),
  `EVENT_FK`          	          BIGINT,
CONSTRAINT `PK_T_EVENT_REGISTRY` PRIMARY KEY (`ID`)
)  ENGINE=InnoDB
;

CREATE TABLE `T_NOTATION` ( 
  `ID`         	BIGINT NOT NULL,
  `NOTATION_DATE` 	TIMESTAMP(2) NOT NULL DEFAULT CURRENT_TIMESTAMP(2) ON UPDATE CURRENT_TIMESTAMP(2),
  `NOTE` 		INT(10),  
  `COMMENTS` 	VARCHAR(255), 
  `ATHLETE_FK` 	VARCHAR(10), 
  `COACH_FK` 	VARCHAR(10),      
  `EVENT_FK` 	BIGINT,
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
  `CREDIT` 	   DOUBLE DEFAULT NULL,
  `STATUT`     VARCHAR(25) NOT NULL,
  `EMAIL`      VARCHAR(255),
  `PASSWORD`   VARCHAR(128) NOT NULL,
  `ROLE_FK`    INT,
CONSTRAINT `PK_T_USER` PRIMARY KEY (`ID`)
)  ENGINE=InnoDB
;

CREATE TABLE `T_CREDIT_REGISTRY` ( 
  `ID`                            BIGINT NOT NULL,
  `MOUVEMENT_DATE`  TIMESTAMP(2) NOT NULL DEFAULT CURRENT_TIMESTAMP(2) ON UPDATE CURRENT_TIMESTAMP(2),
  `CREDIT`          	          INT(10),
  `USER_FK`                   	  VARCHAR(10),
CONSTRAINT `PK_T_CREDIT_REGISTRY` PRIMARY KEY (`ID`)
)  ENGINE=InnoDB
;

CREATE TABLE `T_BOOKMARK` ( 
  `ID`                            BIGINT NOT NULL,
  `ATHLETE_FK`          	      VARCHAR(10),
  `COACH_FK`                   	  VARCHAR(10),
CONSTRAINT `PK_T_BOOKMARK` PRIMARY KEY (`ID`)
)  ENGINE=InnoDB
;

CREATE TABLE `T_FILE` (
  `id` BIGINT(20) NOT NULL,
  `data` LONGBLOB,
  `DATE` DATETIME(6) NOT NULL,
  `name` VARCHAR(255) DEFAULT NULL,
  `type` VARCHAR(255) DEFAULT NULL,
  `USER_FK` VARCHAR(10) NOT NULL,
CONSTRAINT `PK_T_FILE` PRIMARY KEY (`ID`)
) ENGINE=InnoDB;

CREATE TABLE `T_MESSAGE` (
  `id` BIGINT(20) NOT NULL,
  `DATE` DATETIME(6) NOT NULL,
  `TEXTE` VARCHAR(255) DEFAULT NULL,
  `DISCUSSION_FK` BIGINT(20) NOT NULL,
  `RECIPIENT` VARCHAR(255) NOT NULL,
  `SENDER` VARCHAR(255) NOT NULL,
  CONSTRAINT `PK_T_MESSAGE` PRIMARY KEY (`ID`)
) ENGINE=InnoDB;

CREATE TABLE `T_DISCUSSION` (
  `id` BIGINT(20) NOT NULL,
  `CREATION_DATE` DATETIME(6) DEFAULT NULL,
  `SUBJECT` VARCHAR(255) DEFAULT NULL,
  `USER_FK` VARCHAR(255) NOT NULL,
  CONSTRAINT `PK_T_DISCUSSION` PRIMARY KEY (`ID`)
) ENGINE=InnoDB;

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
 
 ALTER TABLE `T_BOOKMARK`
    ADD CONSTRAINT `FK_ATHLETE_FK`
        FOREIGN KEY (`ATHLETE_FK`)
            REFERENCES `T_USER` (`ID`)
 ;
 
 ALTER TABLE `T_BOOKMARK`
    ADD CONSTRAINT `FK_COACH_FK`
        FOREIGN KEY (`COACH_FK`)
            REFERENCES `T_USER` (`ID`)
 ;
 
 ALTER TABLE `T_CREDIT_REGISTRY`
    ADD CONSTRAINT `FK_USER_FK`
        FOREIGN KEY (`USER_FK`)
            REFERENCES `T_USER` (`ID`)
 ;
 
ALTER TABLE `T_EVENT`
    ADD CONSTRAINT `FK_ACTIVITY_FK`
        FOREIGN KEY (`ACTIVITY_FK`)
            REFERENCES `T_ACTIVITY` (`ID`)
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
DELETE FROM T_DISCIPLINE_REGISTRY;
DELETE FROM T_EVENT_REGISTRY;
DELETE FROM T_CREDIT_REGISTRY;
DELETE FROM T_BOOKMARK;
DELETE FROM T_NOTATION;
DELETE FROM T_FILE;
DELETE FROM T_MESSAGE;
DELETE FROM T_DISCUSSION;
DELETE FROM T_COUNTER;
DELETE FROM T_ROLE;

-- Load
INSERT INTO T_DISCIPLINE VALUES ('BOXE', 'Boxe', 'Renforcement du corps, canaliser son énergie et se surpasser', 'Brevet professionnel de la jeunesse, de l\'éducation populaire et du sport. Spécialité educateur sportif, mention Boxe' ),
('CARDIO', 'Cardio', 'Transpirer, se défouler, perdre du poids ou améliorer vos performances en augmentant votre fréquence cardiaque', 'Documents' ),
('DANSE', 'Danse', 'Laissez libre cours à votre fibre artistique dans vos expressions corporelles et émotions', 'Diplôme d\'état ou certificat d\'aptitude aux fonctions de professeur de danse' ),
('GYM', 'Gym', ' Entretenez votre forme physique par des assouplissements, des étirements et des exercices de tonicité musculaire pour assurer l\'équilibre parfait du corps', 'Brevet d\’Etat AGFF - Activités Gymniques de la Forme et de la Force' ),
('YOGA', 'Yoga', 'Différents cours et exercices qui vous permettront de vous déconnecter du monde extérieur pour vous connecter à votre pratique', 'Professeurs de Yoga Certifiés (PYC)' ),
('MUSCU', 'Musculation', 'Exercices et cours élaborés par nos coachs professionnels qui mixent le travail musculaire et l\'entraînement cardio', 'Brevet professionnel de la jeunesse, de l\'éducation populaire et du sport. Option cours collectif ou haltérophilie-musculation' );

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

INSERT INTO T_USER VALUES('root','Elliot', 'Alderson', '','','','','','','','100','VALIDE','elliotalderson@protonmail.com','$2a$10$id93M61FLpdk9sXOBzZlsuhNETXn7YsjyBZs3X09Ll7f5S3lqpee2','1'),
('athlete1','nick', 'alberts', '','','','','','','','30','VALIDE','nick@gmail.com','$2a$10$id93M61FLpdk9sXOBzZlsuhNETXn7YsjyBZs3X09Ll7f5S3lqpee2','3'),
('athlete2','sam', 'dupont', '','','','','','','','12','VALIDE','samd@hotmail.com','$2a$10$id93M61FLpdk9sXOBzZlsuhNETXn7YsjyBZs3X09Ll7f5S3lqpee2','3'),
('athlete3','peter', 'parker', '','','','','','','','52','INVALIDE','peter@yopmail.com','$2a$10$id93M61FLpdk9sXOBzZlsuhNETXn7YsjyBZs3X09Ll7f5S3lqpee2','3'),
('coach1','andrew', 'wiggin', '','','','','','','','34','VALIDE','andy@protonmail.com','$2a$10$id93M61FLpdk9sXOBzZlsuhNETXn7YsjyBZs3X09Ll7f5S3lqpee2','2'),
('coach2','chuck', 'norris', '','','','','','','','45','VALIDE','chucky@hotmail.com','$2a$10$id93M61FLpdk9sXOBzZlsuhNETXn7YsjyBZs3X09Ll7f5S3lqpee2','2'),
('coach3','jean claude', 'vandamme', '','','','','','','','18','INVALIDE','jcvd@gmail.com','$2a$10$id93M61FLpdk9sXOBzZlsuhNETXn7YsjyBZs3X09Ll7f5S3lqpee2','2')
;

INSERT INTO T_DISCIPLINE_REGISTRY VALUES ('1','2021-05-25 22:40:06','coach1','BOXE','no'),
('2','2021-05-12 16:30:12','coach1','CARDIO','ok'),
('3','2021-05-22 02:45:00','coach2','GYM','ok'),
('4','2021-05-20 10:40:06','coach3','MUSCU','ok'),
('5','2021-05-12 17:30:12','coach1','YOGA','ok'),
('6','2021-05-22 16:45:00','coach2','BOXE','no'),
('7','2021-05-20 22:40:06','coach3','DANSE','ok');




