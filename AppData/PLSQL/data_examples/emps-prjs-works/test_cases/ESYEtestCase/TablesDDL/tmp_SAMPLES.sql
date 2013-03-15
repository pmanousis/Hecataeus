create table tmp_SAMPLES
(
  SAM_RES_YEAR      numeric(4) not null,
  SAM_RSC_CODE      varchar(4) not null,
  SAM_CODE          varchar(8) not null,

  SAM_LAST_NAME     varchar(50),
  SAM_FIRST_NAME    varchar(30),
  SAM_FATHERNAME    varchar(25),
  SAM_MOTHERNAME    varchar(25),
  SAM_BIRTH_YR      numeric(4),
  SAM_IDCARD        varchar(8),
  SAM_AFM           varchar(10),
  SAM_ADDRESS       varchar(50),
  SAM_ZIPCODE       varchar(5),
  SAM_TEL           varchar(15),

  SAM_LEGALPERS     varchar(80),
  SAM_NMAX          numeric(7),
  SAM_NMIN          numeric(7),
  SAM_LAND          numeric(9,1),
  SAM_UTILLAND      numeric(9,1),
  SAM_SM_CODE       varchar(10),

  SAM_CREATED_BY    varchar(30),
  SAM_CHANGED_BY    varchar(30),
  SAM_DATE_CREATED  DATETIME,
  SAM_DATE_CHANGED  DATETIME,
  SAM_GEOCODE       varchar(8) not null,
  SAM_LAY_CODE      varchar(4),
  SM_CHA_CODE       varchar(4),

  SAM_MEMBER        numeric(1),

  SAM_QUED          numeric(7),
  SAM_AA_QUED       numeric(7),

  SAM_SECTION       varchar(5),
  SAM_FINTYPE       numeric(6),
  SAM_PROPERTY      varchar(2),
  SAM_LEGAL         varchar(2),

  SAM_PERSONAL      numeric(1),
  SAM_RELATION      varchar(2),

  SAM_DEPT          varchar(4),
  SAM_FLG           varchar(1),

  SAM_YPA           varchar(4),
  SAM_YPACODE       varchar(10),

  SAM_BLOCK         numeric(5),
  SAM_NOMOS         varchar(4),
  SAM_DIMOS         varchar(8),
  SAM_TSGM          numeric(10,2),
  SAM_FEME          numeric(3),
  SAM_SURFCODE      numeric(9),

  SAM_TOTAL         numeric(9,2),
  SAM_LAYEURO_CODE  varchar(4),

  SAM_SYDEL         numeric(10,5),
  SAM_STREET_NUMBER varchar(6)
)


alter table TMP_SAMPLES
  add constraint TMP_SAMPLES_PK primary key (SAM_RES_YEAR, SAM_RSC_CODE, SAM_CODE)

 