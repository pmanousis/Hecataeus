
 CREATE TABLE DIAR_CATEGORIES  
   (	 CAT_ID  INTEGER, 
	 CAT_FIELD  VARCHAR(25), 
	 PED1  VARCHAR(25), 
	 PED2  VARCHAR(25), 
	 PED3  VARCHAR(25), 
	 TABLE_NAME  VARCHAR(25), 
	 YEAR  INTEGER, 
	 PED4  VARCHAR(25)
  ) ;

create table SPR_CATEGORIES
(
  CAT_RSC_CODE      VARCHAR(25) not null,
  CAT_RES_YEAR      NUMERIC(4) not null,
  CAT_ID            NUMERIC(7) not null,
  CAT_DESCR         VARCHAR(250) not null,
  CAT_LEVEL         NUMERIC(2) not null,
  CAT_LAST          NUMERIC(1) not null,
  CAT_STRCODE       VARCHAR(20) not null,
  CAT_UNIT          VARCHAR(25),
  CAT_TYPE          VARCHAR(25),
  CAT_BUTCHER       NUMERIC(1),
  CAT_FIELD         NUMERIC,
  CAT_CREATED_BY    VARCHAR(30),
  CAT_CHANGED_BY    VARCHAR(30),
  CAT_DATETIME_CREATED  DATETIME,
  CAT_DATETIME_CHANGED  DATETIME,
  CAT_CAT_ID        NUMERIC(7),
  CAT_CAT_RSC_CODE  VARCHAR(25),
  CAT_DESCR_ENG     VARCHAR(250),
  CAT_MCA_CODE      VARCHAR(25),
  CAT_EUROSTAT      VARCHAR(30),
  CAT_SHDESCR       VARCHAR(100),
  CAT_ORDER         NUMERIC,
  CAT_REPORTS_DESCR VARCHAR(250)
);



alter table SPR_CATEGORIES
  add constraint SPR_CATEGORIES_PK primary key (CAT_RES_YEAR, CAT_RSC_CODE, CAT_ID);
CREATE TABLE SPR_DIAR_2005_GEO1
(game VARCHAR(25),
gameuser VARCHAR(12),
nomos VARCHAR(25),
gamne VARCHAR(25),
flagup INTEGER,
uname VARCHAR(25),
ggcode VARCHAR(25),
gstrcode INTEGER,
gerocode INTEGER,
gtypecode VARCHAR(25),
gtaxicode VARCHAR(25),
gaaerot INTEGER,
g1epon VARCHAR(30),
g1onom VARCHAR(30),
g1patron VARCHAR(20),
g1etos INTEGER,
g1police VARCHAR(25),
g1company VARCHAR(120),
g1afm VARCHAR(25),
g1odos VARCHAR(50),
g1arith VARCHAR(25),
g1tcode VARCHAR(25),
g1tel VARCHAR(25),
g21q VARCHAR(25),
g21 VARCHAR(25),
g21b4n INTEGER,
g21g VARCHAR(25),
g21d VARCHAR(25),
g22epon VARCHAR(30),
g22onom VARCHAR(20),
g22patron VARCHAR(20),
g22etos INTEGER,
g22police VARCHAR(25),
g22gcode VARCHAR(25),
g22company VARCHAR(120),
g22odos VARCHAR(50),
g22arith VARCHAR(25),
g22tcode VARCHAR(25),
g22tel VARCHAR(25),
g22afm VARCHAR(25),
g31 VARCHAR(25),
g32 VARCHAR(25),
g32a VARCHAR(25),
g32b VARCHAR(25),
g33 VARCHAR(25),
g33a VARCHAR(25),
g41 VARCHAR(25),
g42 VARCHAR(25),
g501 INTEGER,
g502 INTEGER,
g503 INTEGER,
g504 INTEGER,
g505 INTEGER,
g506 INTEGER,
g507 INTEGER,
g508 INTEGER,
g509 INTEGER,
g510 INTEGER,
g611 INTEGER,
g612 INTEGER,
g613 INTEGER,
g614 INTEGER,
g615 VARCHAR(25),
g71 VARCHAR(25),
g72gcode VARCHAR(25),
g7216 INTEGER,
g7217 INTEGER,
g7218 INTEGER,
g7219 INTEGER);
CREATE TABLE SPR_DIAR_2005_GEO29
(game29 VARCHAR(25),
gameuser VARCHAR(12),
aa INTEGER,
g293 VARCHAR(25),
g294 VARCHAR(25),
g295 VARCHAR(25),
g296 VARCHAR(25),
g297 VARCHAR(25),
g298 INTEGER,
g299 INTEGER,
g2910 VARCHAR(25),
g2911 VARCHAR(25),
g2912 VARCHAR(25));
CREATE TABLE SPR_DIAR_2005_GEO2
(game VARCHAR(25),
gameuser VARCHAR(12),
g8013 INTEGER,
g8014 INTEGER,
g8015 INTEGER,
g8023 INTEGER,
g8024 INTEGER,
g8025 INTEGER,
g8033 INTEGER,
g8034 INTEGER,
g8035 INTEGER,
g8043 INTEGER,
g8044 INTEGER,
g8045 INTEGER,
g8053 INTEGER,
g8054 INTEGER,
g8055 INTEGER,
g8063 INTEGER,
g8064 INTEGER,
g8065 INTEGER,
g8073 INTEGER,
g8074 INTEGER,
g8075 INTEGER,
g8083 INTEGER,
g8084 INTEGER,
g8085 INTEGER,
g8093 INTEGER,
g8094 INTEGER,
g8095 INTEGER,
g8103 INTEGER,
g8104 INTEGER,
g8105 INTEGER,
g8113 INTEGER,
g8114 INTEGER,
g8115 INTEGER,
g8123 INTEGER,
g8124 INTEGER,
g8125 INTEGER,
g8133 INTEGER,
g8134 INTEGER,
g8135 INTEGER,
g8143 INTEGER,
g8144 INTEGER,
g8145 INTEGER,
g8153 INTEGER,
g8154 INTEGER,
g8155 INTEGER,
g8163 INTEGER,
g8164 INTEGER,
g8165 INTEGER,
g8173 INTEGER,
g8174 INTEGER,
g8175 INTEGER,
g8183 INTEGER,
g8184 INTEGER,
g8185 INTEGER,
g8193 INTEGER,
g8194 INTEGER,
g8195 INTEGER,
g8203 INTEGER,
g8204 INTEGER,
g8205 INTEGER,
g8213 INTEGER,
g8214 INTEGER,
g8215 INTEGER,
g8223 INTEGER,
g8224 INTEGER,
g8225 INTEGER,
g8233 INTEGER,
g8234 INTEGER,
g8235 INTEGER,
g8243 INTEGER,
g8244 INTEGER,
g8245 INTEGER,
g8253 INTEGER,
g8254 INTEGER,
g8255 INTEGER,
g8263 INTEGER,
g8264 INTEGER,
g8265 INTEGER,
g8273 INTEGER,
g8274 INTEGER,
g8275 INTEGER,
g8283 INTEGER,
g8284 INTEGER,
g8285 INTEGER,
g8293 INTEGER,
g8294 INTEGER,
g8295 INTEGER,
g8303 INTEGER,
g8304 INTEGER,
g8305 INTEGER,
g8313 INTEGER,
g8314 INTEGER,
g8315 INTEGER,
g8323 INTEGER,
g8324 INTEGER,
g8325 INTEGER,
g8333 INTEGER,
g8334 INTEGER,
g8335 INTEGER,
g8343 INTEGER,
g8344 INTEGER,
g8345 INTEGER,
g8353 INTEGER,
g8354 INTEGER,
g8355 INTEGER,
g8363 INTEGER,
g8364 INTEGER,
g8365 INTEGER,
g8373 INTEGER,
g8374 INTEGER,
g8375 INTEGER,
g8383 INTEGER,
g8384 INTEGER,
g8385 INTEGER,
g8393 INTEGER,
g8394 INTEGER,
g8395 INTEGER,
g8403 INTEGER,
g8404 INTEGER,
g8413 INTEGER,
g8414 INTEGER,
g8415 INTEGER,
g8423 INTEGER,
g8424 INTEGER,
g8425 INTEGER,
g8433 INTEGER,
g8434 INTEGER,
g8443 INTEGER,
g8444 INTEGER,
g8445 INTEGER,
g8453 INTEGER,
g8454 INTEGER,
g8463 INTEGER,
g8464 INTEGER,
g8465 INTEGER,
g8473 INTEGER,
g8474 INTEGER,
g8475 INTEGER,
g8483 INTEGER,
g8484 INTEGER,
g8485 INTEGER,
g8493 INTEGER,
g8494 INTEGER,
g8495 INTEGER,
g8503 INTEGER,
g8504 INTEGER,
g8505 INTEGER,
g8513 INTEGER,
g8514 INTEGER,
g8523 INTEGER,
g8524 INTEGER,
g8533 INTEGER,
g8534 INTEGER,
g8543 INTEGER,
g8544 INTEGER,
g8553 INTEGER,
g8554 INTEGER,
g8563 INTEGER,
g8564 INTEGER,
g8573 INTEGER,
g8574 INTEGER,
g8575 INTEGER,
g9583 INTEGER,
g9584 INTEGER,
g10593 INTEGER,
g10594 INTEGER,
g10603 INTEGER,
g10613 INTEGER,
g10614 INTEGER);
CREATE TABLE SPR_DIAR_2005_GEO30
(g30ame VARCHAR(25),
gameuser VARCHAR(12),
g301 INTEGER,
g302 VARCHAR(25),
g303 VARCHAR(25),
g304 INTEGER,
g305 INTEGER);
CREATE TABLE SPR_DIAR_2005_GEO3
(game VARCHAR(25),
gameuser VARCHAR(12),
g11623 INTEGER,
g11624 INTEGER,
g11633 INTEGER,
g11634 INTEGER,
g11643 INTEGER,
g11644 INTEGER,
g11653 INTEGER,
g11654 INTEGER,
g11663 INTEGER,
g11664 INTEGER,
g11673 INTEGER,
g11674 INTEGER,
g11683 INTEGER,
g11684 INTEGER,
g11693 INTEGER,
g11694 INTEGER,
g11703 INTEGER,
g11704 INTEGER,
g11713 INTEGER,
g11714 INTEGER,
g11723 INTEGER,
g11724 INTEGER,
g11733 INTEGER,
g11734 INTEGER,
g11743 INTEGER,
g11744 INTEGER,
g11753 INTEGER,
g11754 INTEGER,
g11763 INTEGER,
g11764 INTEGER,
g11773 INTEGER,
g11774 INTEGER,
g11783 INTEGER,
g11784 INTEGER,
g11793 INTEGER,
g11794 INTEGER,
g11803 INTEGER,
g11804 INTEGER,
g11813 INTEGER,
g11814 INTEGER,
g11823 INTEGER,
g11824 INTEGER,
g11833 INTEGER,
g11834 INTEGER,
g11843 INTEGER,
g11844 INTEGER,
g11853 INTEGER,
g11854 INTEGER,
g11863 INTEGER,
g11864 INTEGER,
g11873 INTEGER,
g11874 INTEGER,
g11883 INTEGER,
g11884 INTEGER,
g11893 INTEGER,
g11894 INTEGER,
g11903 INTEGER,
g11904 INTEGER,
g11913 INTEGER,
g11914 INTEGER,
g11923 INTEGER,
g11924 INTEGER,
g11933 INTEGER,
g11934 INTEGER,
g11943 INTEGER,
g11944 INTEGER,
g11953 INTEGER,
g11954 INTEGER,
g11963 INTEGER,
g11964 INTEGER,
g11973 INTEGER,
g11974 INTEGER,
g11983 INTEGER,
g11984 INTEGER,
g11993 INTEGER,
g11994 INTEGER,
g111003 INTEGER,
g111004 INTEGER,
g111013 INTEGER,
g111014 INTEGER);
CREATE TABLE SPR_DIAR_2005_GEO4
(game VARCHAR(25),
gameuser VARCHAR(12),
g1201 INTEGER,
g1302 INTEGER,
g1303 INTEGER,
g1304 INTEGER,
g1305 INTEGER,
g1306 INTEGER,
g1307 VARCHAR(25),
g1308 VARCHAR(25),
g1409 VARCHAR(25),
g1410 INTEGER,
g1411 INTEGER,
g1412 INTEGER,
g1413 INTEGER,
g1414 INTEGER,
g1415 INTEGER,
g1416 INTEGER,
g1417 INTEGER,
g1418 INTEGER,
g1419 INTEGER,
g1420 INTEGER,
g1421 INTEGER,
g1422 INTEGER,
g1423 INTEGER,
g1424 INTEGER,
g1425 INTEGER,
g1426 INTEGER,
g1527 VARCHAR(25),
g1528 INTEGER,
g1529 INTEGER,
g1530 INTEGER,
g1531 INTEGER,
g1532 INTEGER,
g1533 INTEGER,
g1634 VARCHAR(25),
g1635 VARCHAR(25),
g17013 INTEGER,
g17023 INTEGER,
g17033 INTEGER,
g17043 INTEGER,
g17053 INTEGER,
g17063 INTEGER,
g17073 INTEGER,
g17083 INTEGER,
g17093 INTEGER,
g17014 INTEGER,
g17024 INTEGER,
g17034 INTEGER,
g17044 INTEGER,
g17054 INTEGER,
g17064 INTEGER,
g17074 INTEGER,
g17084 INTEGER,
g17094 INTEGER,
g17015 INTEGER,
g17025 INTEGER,
g17035 INTEGER,
g17045 INTEGER,
g17055 INTEGER,
g17065 INTEGER,
g17075 INTEGER,
g17085 INTEGER,
g17095 INTEGER,
g17016 VARCHAR(25),
g17026 VARCHAR(25),
g17036 VARCHAR(25),
g17046 VARCHAR(25),
g17056 VARCHAR(25),
g17066 VARCHAR(25),
g17076 VARCHAR(25),
g17086 VARCHAR(25),
g17096 VARCHAR(25),
g1801 INTEGER,
g1902 INTEGER,
g2003 INTEGER,
g2004 INTEGER,
g2005 INTEGER,
g2006 INTEGER,
g2007 INTEGER,
g2008 INTEGER,
g2009 INTEGER,
g2010 INTEGER,
g2011 INTEGER,
g2012 INTEGER,
g2013 INTEGER,
g2014 INTEGER,
g2115 INTEGER,
g2116 INTEGER,
g2117 INTEGER,
g2118 INTEGER,
g2219 INTEGER,
g2220 INTEGER,
g2221 INTEGER,
g2322 INTEGER,
g2323 INTEGER,
g2324 INTEGER,
g2425 INTEGER,
g2426 INTEGER,
g2427 INTEGER,
g2428 INTEGER,
g2429 INTEGER,
g2430 INTEGER,
g2431 INTEGER,
g2432 INTEGER,
g2533 INTEGER,
g2534 INTEGER,
g2535 INTEGER,
g2636 INTEGER,
g27code VARCHAR(25),
g27code_2 VARCHAR(25),
g27other INTEGER,
g27other_2 INTEGER,
g312m INTEGER,
g312f INTEGER,
g312t INTEGER,
g313m INTEGER,
g313f INTEGER,
g313t INTEGER,
g322m INTEGER,
g322f INTEGER,
g322t INTEGER,
g323m INTEGER,
g323f INTEGER,
g323t INTEGER,
g33 VARCHAR(25),
g331 INTEGER,
g332 INTEGER,
g333 INTEGER,
g340 VARCHAR(25),
g341 VARCHAR(25),
g342 VARCHAR(25),
g343 VARCHAR(25),
g344 VARCHAR(25),
g345 VARCHAR(25),
g346 VARCHAR(25),
g347 VARCHAR(25),
g348 VARCHAR(25),
GFACE VARCHAR(25),
GDURATION VARCHAR(25),
GDATE DATE,
GCOOP VARCHAR(25),
notes VARCHAR(4000));
CREATE TABLE SPR_DIAR_2005_GEO73
(g73ame VARCHAR(25),
gameuser VARCHAR(12),
g73code VARCHAR(25),
g73ektasi INTEGER,
g73gcode VARCHAR(25));

  CREATE TABLE   SPR_MHT_DIARDECLN  
   (	 DEN_RES_YEAR  INTEGER NOT NULL  , 
	 DEN_RSC_CODE  VARCHAR(25) NOT NULL  , 
	 DEN_GED_CODE  VARCHAR(25) NOT NULL  , 
	 DEN_PRZ_CODE  VARCHAR(25) NOT NULL  , 
	 DEN_PRB_CODE  VARCHAR(30) NOT NULL  , 
	 DEN_CLA_CODE  VARCHAR(25) NOT NULL  , 
	 DEN_LAY_CODE  VARCHAR(25) NOT NULL  , 
	 DEN_SIZE  INTEGER, 
	 DEN_TOTNUM  INTEGER, 
	 DEN_SAMPLNUM  INTEGER, 
	 DEN_YH  INTEGER, 
	 DEN_SE_YH  INTEGER, 
	 DEN_CREATED_BY  VARCHAR(30), 
	 DEN_CHANGED_BY  VARCHAR(30), 
	 DEN_DATE_CREATED  DATE, 
	 DEN_DATE_CHANGED  DATE, 
	 DEN_CELL_ID  INTEGER, 
	 DEN_TYPE_CODE  VARCHAR(25), 
	 DEN_FIRE_FLAG  INTEGER, 
	 CONSTRAINT  SPR_MHT_DIARDECLN_PK  PRIMARY KEY ( DEN_RES_YEAR ,  DEN_RSC_CODE ,  DEN_GED_CODE ,  DEN_PRZ_CODE ,  DEN_PRB_CODE ,  DEN_CLA_CODE ,  DEN_LAY_CODE)
      
  )   ;
 

   

  CREATE TABLE    SPR_VIOKAL_ANAG  
   (	 STRNOMOS  VARCHAR(25), 
	 TYPOS  VARCHAR(25), 
	 NOMOS  VARCHAR(25), 
	 LAY_CODE  INTEGER, 
	 SYDEL  INTEGER, 
	 NMIN  INTEGER, 
	 STR_CODE  VARCHAR(25)
  )   ;
 

create table tmp_SAMPLES
(
  SAM_RES_YEAR      numeric(4) not null,
  SAM_RSC_CODE      VARCHAR(25) not null,
  SAM_CODE          VARCHAR(25) not null,

  SAM_LAST_NAME     varchar(50),
  SAM_FIRST_NAME    varchar(30),
  SAM_FATHERNAME    varchar(25),
  SAM_MOTHERNAME    varchar(25),
  SAM_BIRTH_YR      numeric(4),
  SAM_IDCARD        VARCHAR(25),
  SAM_AFM           VARCHAR(25),
  SAM_ADDRESS       varchar(50),
  SAM_ZIPCODE       VARCHAR(25),
  SAM_TEL           varchar(15),

  SAM_LEGALPERS     varchar(80),
  SAM_NMAX          numeric(7),
  SAM_NMIN          numeric(7),
  SAM_LAND          numeric(9,1),
  SAM_UTILLAND      numeric(9,1),
  SAM_SM_CODE       VARCHAR(25),

  SAM_CREATED_BY    varchar(30),
  SAM_CHANGED_BY    varchar(30),
  SAM_DATE_CREATED  DATETIME,
  SAM_DATE_CHANGED  DATETIME,
  SAM_GEOCODE       VARCHAR(25) not null,
  SAM_LAY_CODE      VARCHAR(25),
  SM_CHA_CODE       VARCHAR(25),

  SAM_MEMBER        numeric(1),

  SAM_QUED          numeric(7),
  SAM_AA_QUED       numeric(7),

  SAM_SECTION       VARCHAR(25),
  SAM_FINTYPE       numeric(6),
  SAM_PROPERTY      VARCHAR(25),
  SAM_LEGAL         VARCHAR(25),
  SAM_TOTSUM 	     numeric(1),
  SAM_PERSONAL      numeric(1),
  SAM_RELATION      VARCHAR(25),

  SAM_DEPT          VARCHAR(25),
  SAM_FLG           VARCHAR(25),

  SAM_YPA           VARCHAR(25),
  SAM_YPACODE       VARCHAR(25),

  SAM_BLOCK         numeric(5),
  SAM_NOMOS         VARCHAR(25),
  SAM_DIMOS         VARCHAR(25),
  SAM_TSGM          numeric,
  SAM_FEME          numeric(3),
  SAM_SURFCODE      numeric(9),

  SAM_TOTAL         numeric,
  SAM_LAYEURO_CODE  VARCHAR(25),

  SAM_SYDEL         numeric,
  SAM_STREET_NUMBER VARCHAR(25)
);


alter table TMP_SAMPLES
  add constraint TMP_SAMPLES_PK primary key (SAM_RES_YEAR, SAM_RSC_CODE, SAM_CODE);

-- Create table
create table tmp_DIAR_SAMPLES
(
  SAM_RES_YEAR          INTEGER not null,
  SAM_RSC_CODE          VARCHAR(25) not null,
  SAM_CODE              VARCHAR(25) not null,
  SAM_CREATED_BY        VARCHAR(30),
  SAM_CHANGED_BY        VARCHAR(30),
  SAM_DATE_CREATED      DATE,
  SAM_DATE_CHANGED      DATE,
  SAM_NUMBER_NEW        INTEGER,
  SAM_CURRENT_STATE     VARCHAR(25),
  SAM_AGR_OCCUPATION    VARCHAR(25),
  SAM_NEW_GEOCODE       VARCHAR(25),
  SAM_TRAINING          VARCHAR(25),
  SAM_TRAINING_LEVEL    VARCHAR(25),
  SAM_CONSUMPTION       VARCHAR(25),
  SAM_SALE              VARCHAR(25),
  SAM_OTHER_POSITION    VARCHAR(25),
  SAM_OTHER_GEOCODE     VARCHAR(25),
  SAM_OTHER_AREA        INTEGER,
  SAM_OTHER_STAVLOI     INTEGER,
  SAM_CURRENT_AREA      INTEGER,
  SAM_CURRENT_STAVLOI   INTEGER,
  SAM_FACE              VARCHAR(25),
  SAM_DURATION          VARCHAR(25),
  SAM_DATE              DATE,
  SAM_COOPERATION       VARCHAR(25),
  SAM_NOTES             VARCHAR(4000),
  SAM_GEOCODE           VARCHAR(25),
  SAM_NEW_LAST_NAME     VARCHAR(50),
  SAM_NEW_FIRST_NAME    VARCHAR(30),
  SAM_NEW_FATHERNAME    VARCHAR(25),
  SAM_NEW_BIRTH_YR      INTEGER,
  SAM_NEW_IDCARD        VARCHAR(25),
  SAM_NEW_LEGALPERS     VARCHAR(80),
  SAM_NEW_AFM           VARCHAR(25),
  SAM_NEW_STREET        VARCHAR(50),
  SAM_NEW_STREET_NUMBER VARCHAR(25),
  SAM_NEW_ZIPCODE       VARCHAR(25),
  SAM_NEW_TEL           VARCHAR(15),
  SAM_BIO_ZWA_FLG       INTEGER,
constraint TMP_DIAR_SAMPLES_PK primary key (SAM_RES_YEAR, SAM_RSC_CODE, SAM_CODE),
constraint TMP_DIAR_SAMPLES_FK foreign key (SAM_RES_YEAR, SAM_RSC_CODE, SAM_CODE)
  references TMP_SAMPLES (SAM_RES_YEAR, SAM_RSC_CODE, SAM_CODE)
);
tablespace PRODSTAT
  pctfree 10
  pctused 40
  initrans 1
  maxtrans 255
  storage
  (
    initial 512K
    next 20M
    minextents 1
    maxextents 4000
    pctincrease 0
 );
-- Add comments to the table 
comment on table SPR_DIAR_SAMPLES
  is 'Δείγμα εκμεταλλεύσεων διάρθρωσης';
-- Add comments to the columns 
comment on column SPR_DIAR_SAMPLES.SAM_RES_YEAR
  is 'Έτος Έρευνας ';
comment on column SPR_DIAR_SAMPLES.SAM_RSC_CODE
  is 'Κωδικός Έρευνας';
comment on column SPR_DIAR_SAMPLES.SAM_CODE
  is 'Κωδικός εκμετάλλευσης';
comment on column SPR_DIAR_SAMPLES.SAM_NUMBER_NEW
  is 'Αριθμός νέων εκμεταλλεύσεων που προέκυψαν από διάσπαση (2.1.β4.α)';
comment on column SPR_DIAR_SAMPLES.SAM_CURRENT_STATE
  is 'Σημερινή Κατάσταση του κατόχου (2.1.γ)';
comment on column SPR_DIAR_SAMPLES.SAM_AGR_OCCUPATION
  is 'Ο συνταξιοδοτημένος κάτοχος ασχολείταια ακόμα με τη γεωργία? (2.1.δ)';
comment on column SPR_DIAR_SAMPLES.SAM_NEW_GEOCODE
  is 'Γεωγραφικός κωδικός της νέας εκμετάλλευσης (2.3)';
comment on column SPR_DIAR_SAMPLES.SAM_TRAINING
  is 'Ο κάτοχος έχει εκπαιδευτεί σε γεωργικά θέματα? (3.3.1)';
comment on column SPR_DIAR_SAMPLES.SAM_TRAINING_LEVEL
  is 'Επίπεδο γεωργικής εκπαίδευσης (3.3.2)';
comment on column SPR_DIAR_SAMPLES.SAM_CONSUMPTION
  is 'Κατανάλωση της παραγωγής (4.1)';
comment on column SPR_DIAR_SAMPLES.SAM_SALE
  is 'Πώληση της παραγωγής (4.2)';
comment on column SPR_DIAR_SAMPLES.SAM_OTHER_POSITION
  is 'Θέση της εκμετάλλευσης (7.1)';
comment on column SPR_DIAR_SAMPLES.SAM_OTHER_GEOCODE
  is 'Γεωγραφικός κωδικός του δήμου της εκμετάλλευσης (7.2)';
comment on column SPR_DIAR_SAMPLES.SAM_OTHER_AREA
  is 'Χρησιμοποιούμενη έκταση γεωργικής γης που βρίσκεται σε άλλο δήμο (7.2.α)';
comment on column SPR_DIAR_SAMPLES.SAM_OTHER_STAVLOI
  is 'Χρησιμοποιούμενη έκταση κηνοτροφικών εγκαταστάσεων που βρίσκεται σε άλλο δήμο (7.2.α)';
comment on column SPR_DIAR_SAMPLES.SAM_CURRENT_AREA
  is 'Χρησιμοποιούμενη έκταση γεωργικής γης που βρίσκεται στο δήμο κατοικίας (7.2.β)';
comment on column SPR_DIAR_SAMPLES.SAM_CURRENT_STAVLOI
  is 'Χρησιμοποιούμενη έκταση κηνοτροφικών εγκαταστάσεων που βρίσκεται στο δήμο κατοικίας (7.2.β)';
comment on column SPR_DIAR_SAMPLES.SAM_FACE
  is 'Πρόσωπο που έδωσε τις πληροφρίες (34)';
comment on column SPR_DIAR_SAMPLES.SAM_DURATION
  is 'Διάρκεια συνέντευξης (34)';
comment on column SPR_DIAR_SAMPLES.SAM_DATE
  is 'Ημερομηνία Συνέντευξης (34)';
comment on column SPR_DIAR_SAMPLES.SAM_COOPERATION
  is 'Βαθμός συνεργασίας (34)';
comment on column SPR_DIAR_SAMPLES.SAM_NOTES
  is 'Παρατηρήσεις (34)';
comment on column SPR_DIAR_SAMPLES.SAM_GEOCODE
  is 'Γεωγραφικός κωδικός ερωτηματολογίου';
comment on column SPR_DIAR_SAMPLES.SAM_NEW_LAST_NAME
  is 'Επώνυμο νέου Κατόχου (Κεφ 2.2)';
comment on column SPR_DIAR_SAMPLES.SAM_NEW_FIRST_NAME
  is 'Όνομα νέου Κατόχου (Κεφ 2.2)';
comment on column SPR_DIAR_SAMPLES.SAM_NEW_FATHERNAME
  is 'Πατρώνυμο νέου Κατόχου (Κεφ 2.2)';
comment on column SPR_DIAR_SAMPLES.SAM_NEW_BIRTH_YR
  is 'Έτος Γέννησης νέου Κατόχου (Κεφ 2.2)';
comment on column SPR_DIAR_SAMPLES.SAM_NEW_IDCARD
  is 'Αριθμός Αστυνομικής Ταυτότητας νέου Κατόχου (Κεφ 2.2)';
comment on column SPR_DIAR_SAMPLES.SAM_NEW_LEGALPERS
  is 'Επωνυμία νέου Κατόχου (Κεφ 2.2)';
comment on column SPR_DIAR_SAMPLES.SAM_NEW_AFM
  is 'Α.Φ.Μ. νέου Κατόχου (Κεφ 2.2)';
comment on column SPR_DIAR_SAMPLES.SAM_NEW_STREET
  is 'Οδός της νέας εκμετάλλευσης (2.3)';
comment on column SPR_DIAR_SAMPLES.SAM_NEW_STREET_NUMBER
  is 'Αριθμός Οδού της νέας εκμετάλλευσης (2.3)';
comment on column SPR_DIAR_SAMPLES.SAM_NEW_ZIPCODE
  is 'Ταχυδρομικός Κωδικός της νέας εκμετάλλευσης (2.3)';
comment on column SPR_DIAR_SAMPLES.SAM_NEW_TEL
  is 'Τηλέφωνο της νέας εκμετάλλευσης (2.3)';
comment on column SPR_DIAR_SAMPLES.SAM_BIO_ZWA_FLG
  is 'Βοηθητικό πεδίο για το αν η εκμετάλλευση εφαρμόζει βιολογική εκτροφή σε όλα (flag=1), σε μερικά (2) ή σε κανένα (default = null) από τα ζώα της (Κεφ 14.2)';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TMP_DIAR_SAMPLES
  add constraint TMP_DIAR_SAMPLES_PK primary key (SAM_RES_YEAR, SAM_RSC_CODE, SAM_CODE)
  using index 
  tablespace PRODSTAT
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 512K
    next 512K
    minextents 1
    maxextents unlimited
    pctincrease 0
 );
alter table TMP_DIAR_SAMPLES
  add constraint TMP_DIAR_SAMPLES_FK foreign key (SAM_RES_YEAR, SAM_RSC_CODE, SAM_CODE)
  references TMP_SAMPLES (SAM_RES_YEAR, SAM_RSC_CODE, SAM_CODE)
  disable; 
 

create table tmp_HOUSEWORK
(
  HWO_RSC_CODE    VARCHAR(25) not null,
  HWO_RES_YEAR    NUMERIC(4) not null,
  HWO_SAM_CODE    VARCHAR(25) not null,
  HWO_AA          NUMERIC(2) not null,
  HWO_HOLDER      VARCHAR(50),
  HWO_CODE        VARCHAR(25),
  HWO_MELOS       NUMERIC(1),
  HWO_FLAG        NUMERIC(1),
  HWO_SEX         NUMERIC(1),
  HWO_PROF        NUMERIC(1),
  HWO_BORNYEAR    NUMERIC(4),
  HWO_DAYWAGE     NUMERIC(4),
  HWO_OTHER       NUMERIC(1),
  HWO_SECOND      NUMERIC(1),
  HWO_THESH       NUMERIC(1),
  HWO_SECWAGE     NUMERIC(4),
  CREATED_BY      VARCHAR(30),
  CHANGED_BY      VARCHAR(30),
  DATETIME_CREATED    DATETIME,
  DATETIME_CHANGED    DATETIME,
  HWO_SAM_GEOCODE VARCHAR(25),
constraint TMP_HWO_PK primary key (HWO_SAM_CODE, HWO_RES_YEAR, HWO_RSC_CODE, HWO_AA),
constraint TMP_HOUSEWORK_FK_TMP_SAMPLES foreign key (HWO_RES_YEAR, HWO_RSC_CODE, HWO_SAM_CODE)
  references TMP_SAMPLES (SAM_RES_YEAR, SAM_RSC_CODE, SAM_CODE)
);


alter table TMP_HOUSEWORK
  add constraint TMP_HWO_PK primary key (HWO_SAM_CODE, HWO_RES_YEAR, HWO_RSC_CODE, HWO_AA);


alter table TMP_HOUSEWORK
  add constraint TMP_HOUSEWORK_FK_TMP_SAMPLES foreign key (HWO_RES_YEAR, HWO_RSC_CODE, HWO_SAM_CODE)
  references TMP_SAMPLES (SAM_RES_YEAR, SAM_RSC_CODE, SAM_CODE);
create table tmp_MACHINES
(
  MAC_RSC_CODE    VARCHAR(25) not null,
  MAC_RES_YEAR    NUMERIC(4) not null,
  MAC_SAM_CODE    VARCHAR(25) not null,
  MAC_CAT_ID      NUMERIC(7) not null,
  MAC_OWNED       NUMERIC(5),
  MAC_COOWNED     NUMERIC(5),
  MAC_OWNEDPERC   NUMERIC,
  MAC_OTHER       NUMERIC(1),
  CREATED_BY      VARCHAR(30),
  CHANGED_BY      VARCHAR(30),
  DATE_CREATED    DATETIME,
  DATE_CHANGED    DATETIME,
  MAC_SAM_GEOCODE VARCHAR(25),
constraint TMP_MACHINES_PK primary key (MAC_SAM_CODE, MAC_RES_YEAR, MAC_RSC_CODE, MAC_CAT_ID),
constraint TMP_MAC_SAM_FK foreign key (MAC_RES_YEAR, MAC_RSC_CODE, MAC_SAM_CODE)
  references TMP_SAMPLES (SAM_RES_YEAR, SAM_RSC_CODE, SAM_CODE)
);

alter table TMP_MACHINES
  add constraint TMP_MACHINES_PK primary key (MAC_SAM_CODE, MAC_RES_YEAR, MAC_RSC_CODE, MAC_CAT_ID);


alter table TMP_MACHINES
  add constraint TMP_MAC_SAM_FK foreign key (MAC_RES_YEAR, MAC_RSC_CODE, MAC_SAM_CODE)
  references TMP_SAMPLES (SAM_RES_YEAR, SAM_RSC_CODE, SAM_CODE);

 create table tmp_OTHERLAND
(
  OLA_RSC_CODE    VARCHAR(25) not null,
  OLA_RES_YEAR    NUMERIC(4) not null,
  OLA_SAM_CODE    VARCHAR(25) not null,
  OLA_CUK_CODE    NUMERIC(2) not null,
  OLA_GEOCODE     VARCHAR(25) not null,
  OLA_ACRES       NUMERIC(8,1),
  CREATED_BY      VARCHAR(30),
  CHANGED_BY      VARCHAR(30),
  DATETIME_CREATED    DATETIME,
  DATETIME_CHANGED    DATETIME,
  OLA_SAM_GEOCODE VARCHAR(25),
constraint TMP_OTHERLAND_PK primary key (OLA_SAM_CODE, OLA_RES_YEAR, OLA_RSC_CODE, OLA_CUK_CODE, OLA_GEOCODE),
constraint TMP_OTHERLAND_FK_TMP_SAMPLES foreign key (OLA_RES_YEAR, OLA_RSC_CODE, OLA_SAM_CODE)
  references TMP_SAMPLES (SAM_RES_YEAR, SAM_RSC_CODE, SAM_CODE)
);


alter table TMP_OTHERLAND
  add constraint TMP_OTHERLAND_PK primary key (OLA_SAM_CODE, OLA_RES_YEAR, OLA_RSC_CODE, OLA_CUK_CODE, OLA_GEOCODE);


alter table TMP_OTHERLAND
  add constraint TMP_OTHERLAND_FK_TMP_SAMPLES foreign key (OLA_RES_YEAR, OLA_RSC_CODE, OLA_SAM_CODE)
  references TMP_SAMPLES (SAM_RES_YEAR, SAM_RSC_CODE, SAM_CODE);

create table tmp_PARTWORKERS
(
  PAW_RSC_CODE    VARCHAR(25) not null,
  PAW_RES_YEAR    NUMERIC(4) not null,
  PAW_SAM_CODE    VARCHAR(25) not null,
  PAW_SEX         NUMERIC(1) not null,
  PAW_PARTIME     NUMERIC(1) not null,
  PAW_PERSONS     NUMERIC(4),
  PAW_WAGES       NUMERIC(6,1),
  PAW_HOURS       NUMERIC(5),
  PAW_FEE         NUMERIC(8),
  CREATED_BY      VARCHAR(30),
  CHANGED_BY      VARCHAR(30),
  DATETIME_CREATED    DATETIME,
  DATETIME_CHANGED    DATETIME,
  PAW_SAM_GEOCODE VARCHAR(25),
constraint TMP_PARTWORKERS_PK primary key (PAW_SAM_CODE, PAW_RES_YEAR, PAW_RSC_CODE, PAW_SEX, PAW_PARTIME)
);


alter table TMP_PARTWORKERS
  add constraint TMP_PARTWORKERS_PK primary key (PAW_SAM_CODE, PAW_RES_YEAR, PAW_RSC_CODE, PAW_SEX, PAW_PARTIME);

create table tmp_QUESTAPO
(
  QUD_RSC_CODE  VARCHAR(25) not null,
  QUD_RES_YEAR  NUMERIC(4) not null,
  QUD_SAM_CODE  VARCHAR(25) not null,
  QUD_CAT_ID    NUMERIC(7) not null,
  QUD_NUM       NUMERIC,
  QUD_PRODNUM   NUMERIC,
  QUD_WATERED   NUMERIC,
  QUD_SECONDARY NUMERIC,
  QUD_GEOCODE   VARCHAR(25),
  QUD_SURFACE   NUMERIC(4),
  QUD_MONTHS    NUMERIC(4),
  CREATED_BY    VARCHAR(30),
  CHANGED_BY    VARCHAR(30),
  DATETIME_CREATED  DATETIME,
  DATETIME_CHANGED  DATETIME,
  QUD_FLAG      NUMERIC(2),
constraint tmp_QUESTAPO_PK primary key (QUD_RSC_CODE, QUD_RES_YEAR, QUD_SAM_CODE, QUD_CAT_ID),
constraint QUD_DIAR_SAMP_FK foreign key (QUD_RES_YEAR, QUD_RSC_CODE, QUD_SAM_CODE)
  references TMP_SAMPLES (SAM_RES_YEAR, SAM_RSC_CODE, SAM_CODE)
);


alter table tmp_QUESTAPO
  add constraint tmp_QUESTAPO_PK primary key (QUD_RSC_CODE, QUD_RES_YEAR, QUD_SAM_CODE, QUD_CAT_ID);

alter table tmp_QUESTAPO
  add constraint QUD_DIAR_SAMP_FK foreign key (QUD_RES_YEAR, QUD_RSC_CODE, QUD_SAM_CODE)
  references TMP_SAMPLES (SAM_RES_YEAR, SAM_RSC_CODE, SAM_CODE);
 

create table tmp_REGWORKERS
(
  RGW_RSC_CODE    VARCHAR(25) not null,
  RGW_RES_YEAR    NUMERIC(4) not null,
  RGW_SAM_CODE    VARCHAR(25) not null,
  RGW_AA          NUMERIC(3) not null,
  RGW_SEX         NUMERIC(1),
  RGW_THESH       NUMERIC(1),
  RGW_BORNYEAR    NUMERIC(4),
  RGW_WAGES       NUMERIC(4),
  RGW_FEE         NUMERIC(9),
  CREATED_BY      VARCHAR(30),
  CHANGED_BY      VARCHAR(30),
  DATETIME_CREATED    DATETIME,
  DATETIME_CHANGED    DATETIME,
  RGW_SAM_GEOCODE VARCHAR(25),
constraint TMP_REGWORKERS_PK primary key (RGW_SAM_CODE, RGW_RES_YEAR, RGW_RSC_CODE, RGW_AA),
constraint TMP_REGWORKERS_FK_TMP_SAMPLES foreign key (RGW_RES_YEAR, RGW_RSC_CODE, RGW_SAM_CODE)
  references TMP_SAMPLES (SAM_RES_YEAR, SAM_RSC_CODE, SAM_CODE)
);


alter table TMP_REGWORKERS
  add constraint TMP_REGWORKERS_PK primary key (RGW_SAM_CODE, RGW_RES_YEAR, RGW_RSC_CODE, RGW_AA);


alter table TMP_REGWORKERS
  add constraint TMP_REGWORKERS_FK_TMP_SAMPLES foreign key (RGW_RES_YEAR, RGW_RSC_CODE, RGW_SAM_CODE)
  references TMP_SAMPLES (SAM_RES_YEAR, SAM_RSC_CODE, SAM_CODE);



   CREATE OR REPLACE VIEW DIAR_SAMPLES_V AS
SELECT
        S.sam_res_year,
        S.sam_rsc_code,
        S.sam_code,
        S.sam_last_name,
        S.sam_first_name,
        S.sam_fathername,
        S.sam_mothername,
        S.sam_birth_yr,
        S.sam_idcard,
        S.sam_afm,
        S.sam_address,
        S.sam_zipcode,
        S.sam_tel,
        S.sam_legalpers,
        S.sam_nmax,
        S.sam_nmin,
        S.sam_sm_code,
        S.sam_created_by,
        S.sam_changed_by,
        S.sam_date_created,
        S.sam_date_changed,
        S.sam_geocode,
        S.sam_lay_code,
        S.sm_cha_code,
        S.sam_member,
        S.sam_qued,
        S.sam_aa_qued,
        S.sam_section,
        S.sam_legal,
        S.sam_personal,
        S.sam_relation,
        S.sam_dept,
        S.sam_flg,
        S.sam_ypa,
        S.sam_ypacode,
        S.sam_block,
        S.sam_nomos,
        S.sam_dimos,
        S.sam_tsgm,
        s.sam_fintype, --τυπολογια
        S.sam_feme,
        S.sam_surfcode,--ΠΡΟΒΛΗΜΑΤΙΚΟΤΗΤΑ (--)
        S.sam_sydel,
        S.sam_street_number,
        s.sam_land, --χρησιμοποιούμενη γη, ενημερώνεται από τον diar_questdets(cat_id=258)
        s.sam_totsum, --καλλιεργούμενη γη (χρησιμοποιούμενη - αγονοι βοσκότοποι)(cat_ids=258 - 539)
        D.SAM_NUMBER_NEW,
        D.SAM_CURRENT_STATE,
        D.SAM_AGR_OCCUPATION,
        D.SAM_NEW_GEOCODE,
        D.SAM_TRAINING,
        D.SAM_TRAINING_LEVEL,
        D.SAM_CONSUMPTION,
        D.SAM_SALE,
        D.SAM_OTHER_POSITION,
        D.SAM_OTHER_GEOCODE,
        D.SAM_OTHER_AREA,
        D.SAM_OTHER_STAVLOI,
        D.SAM_CURRENT_AREA,
        D.SAM_CURRENT_STAVLOI,
        D.SAM_FACE,
        D.SAM_DURATION,
        D.SAM_DATE,
        D.SAM_COOPERATION,
        D.SAM_NOTES,
        D.SAM_NEW_LAST_NAME,
        D.SAM_NEW_FIRST_NAME,
        D.SAM_NEW_FATHERNAME,
        D.SAM_NEW_BIRTH_YR,
        D.SAM_NEW_IDCARD,
        D.SAM_NEW_LEGALPERS,
        D.SAM_NEW_AFM,
        D.SAM_NEW_STREET,
        D.SAM_NEW_STREET_NUMBER,
        D.SAM_NEW_ZIPCODE,
        D.SAM_NEW_TEL,
        D.SAM_BIO_ZWA_FLG
  From tmp_samples S, tmp_diar_samples D
  Where S.sam_rsc_code = D.sam_rsc_code
    and S.sam_res_year = D.sam_res_year
    and S.sam_code     = D.SAM_CODE;
 
