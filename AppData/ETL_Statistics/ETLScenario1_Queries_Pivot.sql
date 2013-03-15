
--Enter2Space Activity for SPR_GEO1
CREATE VIEW ETL1_ACT1 AS
SELECT 
		GAME       ,           
		NOMOS      ,            
		GAMNE      ,            
		FLAGUP     ,            
		UNAME      ,            
		GGCODE     ,            
		GSTRCODE   ,            
		GEROCODE   ,            
		GTYPECODE  ,            
		GTAXICODE  ,            
		GAAEROT    ,            
		Replace(G1EPON,'ENTER','SPACE') as G1EPON,        
		Replace(G1ONOM,'ENTER','SPACE') as G1ONOM ,           
		Replace(G1PATRON,'ENTER','SPACE') as G1PATRON ,           
		G1ETOS     ,            
		Replace(G1POLICE,'ENTER','SPACE') as G1POLICE,            
		Replace(G1COMPANY,'ENTER','SPACE') as G1COMPANY,          
		G1AFM      ,            
		Replace(G1ODOS,'ENTER','SPACE') as G1ODOS,           
		G1ARITH    ,            
		G1TCODE    ,            
		G1TEL      ,           
		G21        ,            
		G22EPON    ,           
		G22ONOM    ,           
		G22PATRON  ,           
		G22ETOS    ,            
		G22POLICE  ,            
		G22GCODE   ,            
		G22COMPANY ,          
		G22ODOS    ,           
		G22ARITH   ,            
		G22TCODE   ,            
		G22TEL     ,           
		G31        ,            
		G32        ,            
		G32A       ,            
		G32B       ,            
		G41        ,            
		G501       ,          
		G502       ,          
		G503       ,          
		G504       ,          
		G505       ,          
		G506       ,          
		G507       ,          
		G508       ,          
		G509       ,          
		G510       ,          
		G611       ,          
		G612       ,          
		G613       ,          
		G614       ,          
		G615       ,          
		G71GCODE   ,            
		G72        ,          
		G72S       ,            
		G73        ,            
		GAMEUSER              
FROM SPR_GEO1;

--POLICY : ON ATTRIBUTE ADDITION, DELETION , MODIFICATION, RENAME TO SPR_GEO1 == PROPAGATE



--Semicolon2Comma Activity for SPR_GEO1

CREATE VIEW ETL1_ACT2 AS
SELECT 
		GAME       ,           
		NOMOS      ,            
		GAMNE      ,            
		FLAGUP     ,            
		UNAME      ,            
		GGCODE     ,            
		GSTRCODE   ,            
		GEROCODE   ,            
		GTYPECODE  ,            
		GTAXICODE  ,            
		GAAEROT    ,            
		Replace(G1EPON,'SEMICOLLON','COMMA') as G1EPON,        
		Replace(G1ONOM,'SEMICOLLON','COMMA') as G1ONOM ,           
		Replace(G1PATRON,'SEMICOLLON','COMMA') as G1PATRON ,           
		G1ETOS     ,            
		Replace(G1POLICE,'SEMICOLLON','COMMA') as G1POLICE,            
		Replace(G1COMPANY,'SEMICOLLON','COMMA') as G1COMPANY,          
		G1AFM      ,            
		Replace(G1ODOS,'SEMICOLLON','COMMA') as G1ODOS,           
		G1ARITH    ,            
		G1TCODE    ,            
		G1TEL      ,           
		G21        ,            
		G22EPON    ,           
		G22ONOM    ,           
		G22PATRON  ,           
		G22ETOS    ,            
		G22POLICE  ,            
		G22GCODE   ,            
		G22COMPANY ,          
		G22ODOS    ,           
		G22ARITH   ,            
		G22TCODE   ,            
		G22TEL     ,           
		G31        ,            
		G32        ,            
		G32A       ,            
		G32B       ,            
		G41        ,            
		G501       ,          
		G502       ,          
		G503       ,          
		G504       ,          
		G505       ,          
		G506       ,          
		G507       ,          
		G508       ,          
		G509       ,          
		G510       ,          
		G611       ,          
		G612       ,          
		G613       ,          
		G614       ,          
		G615       ,          
		G71GCODE   ,            
		G72        ,          
		G72S       ,            
		G73        ,            
		GAMEUSER              
FROM ETL1_ACT1;

--POLICY : ON ATTRIBUTE ADDITION, DELETION , MODIFICATION, RENAME TO SPR_GEO1 == ETL1_ACT1

----------------------------------------------------------------------------------------------------------------

--Load to Oracle for SPR_GEO1
CREATE VIEW ETL1_ACT3 AS
Select * from ETL1_ACT2;


--POLICY : ON ATTRIBUTE ADDITION, DELETION , MODIFICATION, RENAME TO ETL1_ACT2 == PROPAGATE
----------------------------------------------------------
--Load FROM ACCESS to Oracle for SPR_GEO4
CREATE VIEW ETL1_ACT4 AS
Select * from SPR_GEO4;

--POLICY : ON ATTRIBUTE ADDITION, DELETION , MODIFICATION, RENAME TO SPR_GEO4 == PROPAGATE
----------------------------------------------------------

--FILL ATTRIBUTE NOMOS  FROM PARAMETER P_NOMOS
create view ETL1_ACT5 as
SELECT ETL1_ACT4.*, 'P_NOMOS' as NOMOS 
FROM ETL1_ACT4;
--POLICY :ON ATTRIBUTE ADDITION, DELETION , MODIFICATION, RENAME TO ETL1_ACT4 == PROPAGATE

----------------------------------------------------------------------------------------------------------------
--delete temp TMP_samples
create view ETL1_ACT6 as
SELECT * FROM TMP_samples;

--POLICY : ON ATTRIBUTE ADDITION, DELETION , MODIFICATION, RENAME TO TMP_samples == PROPAGATE

----------------------------------------------------------------------------------------------------------------
--delete temp spr_diar_samples
create view ETL1_ACT7 as
SELECT * FROM TMP_diar_samples;
--POLICY : ON ATTRIBUTE ADDITION, DELETION , MODIFICATION, RENAME TO TMP_diar_samples == PROPAGATE

----------------------------------------------------------------------------------------------------------------
--delete temp TMP_DIAR_QUESTDETS
create view ETL1_ACT8 as
SELECT * FROM TMP_DIAR_QUESTDETS 
	WHERE QUD_CAT_ID in (select cat_id from diar_categories
                     where table_name = 'geo1');


--POLICY : ON ATTRIBUTE ADDITION, DELETION , MODIFICATION, RENAME TO TMP_DIAR_QUESTDETS == PROPAGATE
--POLICY : ON ATTRIBUTE ADDITION, DELETION , MODIFICATION TO diar_categories == BLOCK

----------------------------------------------------------------------------------------------------------------
-- Join Activity BETWEEN ETL1_ACT3 AND ETL1_ACT6
create view ETL1_ACT9 as
	SELECT S.* , T.gcoop,T.gface, T.gduration
			FROM ETL1_ACT3 S , ETL1_ACT5 T
			where S.gameuser=T.gameuser
			and   S.nomos= T.NOMOS;

--POLICY : ON ATTRIBUTE ADDITION, DELETION , MODIFICATION TO ETL1_ACT3 == PROPAGATE (FETCH EVERY NEW ATTRIBUTE)
--POLICY : ON ATTRIBUTE ADDITION, DELETION , MODIFICATION TO ETL1_ACT5 == BLOCK (FETCH ONLY ABOVE ATTRIBUTES)
--         (addition of gnotes and gdate not captured)--check it out

----------------------------------------------------------------------------------------------------------------
--Add Fields NMAX, NMIN, SYDEL FOR RECORDSET 1 default = 1
create view ETL1_ACT10 as
	SELECT S.* , '1' AS NMIN, '1' AS NMAX, '1' AS SYDEL
			FROM ETL1_ACT9 S;
			
--POLICY : ON ATTRIBUTE ADDITION, DELETION , MODIFICATION TO ETL1_ACT9 == PROPAGATE (FETCH EVERY NEW ATTRIBUTE)
----------------------------------------------------------------------------------------------------------------			

--UPDATE Fields NMAX, NMIN, SYDEL FOR RECORDSET 2
create view ETL1_ACT11 as
	SELECT S.* 
			FROM ETL1_ACT10 S , SPR_VIOKAL_ANAG T
			WHERE T.NOMOS=S.nomos --IT IS OUTER JOIN
      AND   T.STRNOMOS =S.GSTRCODE--IT IS OUTER JOIN
      --folowing fields are updated
			and	S.NMIN = T.NMIN
			AND S.NMAX = T.NMAX
			AND S.SYDEL= T.SYDEL;


--POLICY : ON ATTRIBUTE ADDITION, DELETION , MODIFICATION TO ETL1_ACT10 == PROPAGATE (FETCH EVERY NEW ATTRIBUTE)
--POLICY : ON ATTRIBUTE ADDITION, DELETION , MODIFICATION TO SPR_VIOKAL_ANAG == BLOCK (FETCH ONLY ABOVE ATTRIBUTES)

----------------------------------------------------------------------------------------------------------------   
--UPDATE Fields NMAX, NMIN, SYDEL FOR RECORDSET 3
create view ETL1_ACT12 as
	SELECT S.* 
			FROM ETL1_ACT11 S , SPR_MHT_DIARDECLN T
			WHERE T.den_prz_code=S.nomos 
      AND   T.DEN_LAY_CODE =S.gtaxicode
    	AND		T.den_prb_code = S.gtypecode
    	--FOLLOWING FIELDS ARE UPDATED
    	AND 	T.DEN_SAMPLNUM = S.NMIN
    	AND 	T.DEN_TOTNUM = S.NMAX
    	AND 	T.DEN_SYDEL = S.SYDEL;

--POLICY : ON ATTRIBUTE ADDITION, DELETION , MODIFICATION TO ETL1_ACT11 == PROPAGATE (FETCH EVERY NEW ATTRIBUTE)
--POLICY : ON ATTRIBUTE ADDITION, DELETION , MODIFICATION TO SPR_MHT_DIARDECLN == BLOCK (FETCH ONLY ABOVE ATTRIBUTES)

--EVENT: ADD ATTRIBUTES den_type_code,den_res_year,den_res_code TO SPR_MHT_DIARDECLN (SEE ACCESSEVOLUTIONCHANGES_FULL.mdb)
--TRASNFORMED QUERY --NOT AUTOMATICALLY!!!!
----------------------------------------------------------------------------------------------------------------   

--Project n Fields -->insert into TMP_samples FROM ETL1_ACT11
SELECT 
S.SAM_RES_YEAR, 
S.SAM_RSC_CODE,
S.SAM_CODE,
S.SAM_LAST_NAME,
S.SAM_FIRST_NAME,
S.SAM_FATHERNAME,
S.SAM_BIRTH_YR,
S.SAM_IDCARD,
S.SAM_LEGALPERS,
S.SAM_AFM,
S.SAM_ADDRESS,
S.SAM_STREET_NUMBER,
S.SAM_ZIPCODE,
S.SAM_TEL,
S.SAM_NMAX,
S.SAM_NMIN,
S.SAM_GEOCODE,
S.SAM_NOMOS,
S.SAM_LAY_CODE,
S.SM_CHA_CODE,
S.SAM_MEMBER ,
S.SAM_QUED ,
S.SAM_AA_QUED ,
S.SAM_SECTION ,
S.SAM_LEGAL ,
S.SAM_PERSONAL ,
S.SAM_RELATION ,
S.SAM_SALE ,
S.SAM_DEPT,
SAM_UTILLAND,
SAM_ASSOCIATION,
SAM_OGA,
SAM_GEO_THESH,
S.SAM_FLG,
S.SAM_CREATED_BY,
S.SAM_SYDEL       
FROM ETL1_ACT6 S,    ETL1_ACT12 R                                      
WHERE
    S.SAM_RES_YEAR= 							 '2005'     
AND S.SAM_RSC_CODE=                '01'       
AND S.SAM_CODE=                    R.game	   	
AND S.SAM_LAST_NAME=               R.g1epon  	
AND S.SAM_FIRST_NAME=              R.g1onom  	
AND S.SAM_FATHERNAME=              R.g1patron	
AND S.SAM_BIRTH_YR=                R.g1etos  	
AND S.SAM_IDCARD=                  R.g1police	
AND S.SAM_LEGALPERS=               R.g1company
AND S.SAM_AFM=                     R.g1afm   	
AND S.SAM_ADDRESS=                 R.g1odos  	
AND S.SAM_STREET_NUMBER=           R.g1arith 	
AND S.SAM_ZIPCODE=                 R.g1tcode 	
AND S.SAM_TEL=                     R.g1tel   	
AND S.SAM_NMAX=                    R.NMAX    	
AND S.SAM_NMIN=                    R.NMIN    	 	
AND S.SAM_GEOCODE=              	 R.ggcode  	
AND S.SAM_NOMOS=                	 R.nomos   	
AND S.SAM_LAY_CODE=             	 R.gtaxicode
AND S.SM_CHA_CODE=                 R.g21     	
AND S.SAM_MEMBER =                 R.g32a    	
AND S.SAM_QUED =                   R.gaaerot 	
AND S.SAM_AA_QUED =                R.gerocode	
AND S.SAM_SECTION =                R.gstrcode	
AND S.SAM_LEGAL =                  R.g31     	
AND S.SAM_PERSONAL =               R.g32     	
AND S.SAM_RELATION =               R.g32b 
AND	S.SAM_SALE   	=								 R.g41
AND S.SAM_DEPT=                    R.gtypecode
AND S.SAM_UTILLAND= 							 R.g72                       
AND	S.SAM_ASSOCIATION=						 R.g73
AND	S.SAM_OGA =										 R.g72s
AND	S.SAM_GEO_THESH=							 R.g71gcode
AND S.SAM_FLG=                     R.gamne		
AND S.SAM_CREATED_BY=              R.gameuser	
AND S.SAM_SYDEL =                  R.SYDEL ;   	

--POLICY : ON ATTRIBUTE ADDITION, DELETION , MODIFICATION TO ETL1_ACT6 == PROPAGATE (FETCH EVERY NEW ATTRIBUTE)
--POLICY : ON ATTRIBUTE ADDITION = BLOCK, DELETION , MODIFICATION TO ETL1_ACT12 == PROPAGATE (FETCH ONLY ABOVE ATTRIBUTES)
--POLICY : ON CONDITION MODIFICATION TO Q2 == PROPAGATE (FETCH ONLY ABOVE ATTRIBUTES)
---------------------------------------------------------------------------------------------------------------   


--Project m Fields -->insert into TMP_DIAR_samples FROM ETL1_ACT11

SELECT S.sam_res_year, 
      S.sam_rsc_code, 
      S.sam_code, 
      S.sam_created_by,
      S.sam_new_geocode,
      S.sam_face, 
      S.sam_duration,
      S.sam_cooperation,
      S.sam_geocode,
      S.sam_new_last_name,
      S.SAM_NEW_FIRST_NAME    ,
      S.SAM_NEW_FATHERNAME    ,
      S.SAM_NEW_BIRTH_YR      ,
      S.SAM_NEW_IDCARD        ,
      S.SAM_NEW_LEGALPERS     ,
      S.SAM_NEW_STREET        ,
      S.SAM_NEW_STREET_NUMBER ,
      S.SAM_NEW_ZIPCODE       ,
      S.SAM_NEW_TEL
FROM ETL1_ACT7 S,    ETL1_ACT12 R                                      
WHERE 
		S.sam_res_year=			'2005'                
AND S.sam_rsc_code=           '01'             
AND S.sam_code=               R.game                
AND S.sam_created_by=         R.gameuser                   
AND S.sam_new_geocode=        R.g22gcode                   
AND S.sam_face=               R.gface       
AND S.sam_duration=           R.gduration        
AND S.sam_cooperation=        R.gcoop       
AND S.sam_geocode=            R.ggcode               
AND S.sam_new_last_name=      R.g22epon       
AND S.SAM_NEW_FIRST_NAME    = R.g22onom       
AND S.SAM_NEW_FATHERNAME    = R.g22patron     
AND S.SAM_NEW_BIRTH_YR      = R.g22etos       
AND S.SAM_NEW_IDCARD        = R.g22police     
AND S.SAM_NEW_LEGALPERS     = R.g22company           
AND S.SAM_NEW_STREET        = R.g22odos       
AND S.SAM_NEW_STREET_NUMBER = R.g22arith      
AND S.SAM_NEW_ZIPCODE       = R.g22tcode      
AND S.SAM_NEW_TEL						= R.g22tel;
                               
--POLICY : ON ATTRIBUTE ADDITION, DELETION , MODIFICATION TO ETL1_ACT7 == PROPAGATE (FETCH EVERY NEW ATTRIBUTE)
--POLICY : ON ATTRIBUTE ADDITION = BLOCK, DELETION , MODIFICATION TO ETL1_ACT12 == PROPAGATE (FETCH ONLY ABOVE ATTRIBUTES)
--POLICY : ON CONDITION MODIFICATION TO Q3 == PROPAGATE (FETCH ONLY ABOVE ATTRIBUTES)
----------------------------------------------------------------------------------------------------------------   
--CURSOR CAT IS this cursor is used for pivoting, contains the CATEGORY IDS of attributes in SPR_GEO2
  CREATE VIEW ETL1_ACT13 as
  SELECT CAT_ID 
    FROM diar_categories
    where table_name='geo1';
    
--POLICY : ON ATTRIBUTE ADDITION, DELETION , MODIFICATION TO diar_categories == block (FETCH ONLY THIS ATTRIBUTE)
----------------------------------------------------------------------------------------------------------------   

--Project m Fields -->insert into TMP_QUESTDETS (PIVOT) (FOR BETTER UNDERSTANDING SEE ETLscenario2)	
	
		SELECT 
	Q.QUD_RSC_CODE  ,
	Q.QUD_RES_YEAR  ,
	Q.QUD_SAM_CODE  ,
	Q.QUD_CAT_ID    ,
	Q.QUD_NUM       ,
	Q.CREATED_BY	,
	Q.QUD_GEOCODE
FROM ETL1_ACT8 Q, ETL1_ACT9 R ,ETL1_ACT13 C
WHERE
	Q.QUD_RSC_CODE 	='01'
	AND Q.QUD_RES_YEAR 	='2005'
	AND Q.QUD_SAM_CODE 	=R.game
	AND Q.QUD_CAT_ID 	=C.CAT_ID
 	AND Q.QUD_NUM 	=DECODE(C.CAT_ID, 'PARAMETER' )
	AND Q.CREATED_BY 	=R.gameuser
	AND Q.QUD_GEOCODE 	=R.ggcode  ;
	
--POLICY : ON ATTRIBUTE ADDITION, DELETION , MODIFICATION TO ETL1_ACT8 == PROPAGATE (FETCH EVERY NEW ATTRIBUTE)
--POLICY : ON ATTRIBUTE ADDITION ==BLOCK, DELETION , MODIFICATION TO ETL1_ACT9 == PROPAGATE (FETCH ONLY ABOVE ATTRIBUTES)
--POLICY : ON ATTRIBUTE ADDITION, DELETION , MODIFICATION TO ETL1_ACT13 == block (FETCH ONLY ABOVE ATTRIBUTES)
--POLICY : ON CONDITION MODIFICATION TO Q4 == PROPAGATE (FETCH ONLY ABOVE ATTRIBUTES)