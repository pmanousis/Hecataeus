create or replace PACKAGE BODY spr_diar_load_2005 IS

PROCEDURE trunc_tmp is

BEGIN

  EXECUTE IMMEDIATE 'TRUNCATE TABLE tmp_samples;' ;
  EXECUTE IMMEDIATE 'TRUNCATE TABLE tmp_diar_samples;' ;
  EXECUTE IMMEDIATE 'TRUNCATE TABLE tmp_questapo;' ;
  EXECUTE IMMEDIATE 'TRUNCATE TABLE tmp_partworkers;' ;
  EXECUTE IMMEDIATE 'TRUNCATE TABLE tmp_otherland;' ;
  EXECUTE IMMEDIATE 'TRUNCATE TABLE tmp_housework;' ;
  EXECUTE IMMEDIATE 'TRUNCATE TABLE tmp_regworkers;' ;
  EXECUTE IMMEDIATE 'TRUNCATE TABLE tmp_machines;' ;
  
END ;

PROCEDURE geo1(p_nomos varchar2)  IS

/*************************************************************************************
***  LOADIND  TMP_SAMPLES,   FROM  spr_diar_2005_geo1, spr_diar_2005_geo4          ***
*** 10/05/2006                                                                     ***
*** by gpapas                                                                      ***
*************************************************************************************/


  --cursor for all sam of each user                                                                                                                                                                                                                                                                
	CURSOR C1 IS
  		SELECT a.*, b.gcoop,b.gface,b.gduration, b.gdate, b.notes
			FROM spr_diar_2005_geo1 a, spr_diar_2005_geo4 b
			where a.game=b.game 
      and   a.nomos=:p_nomos;
  
/*		CURSOR C2 IS
		SELECT * from spr_mht_diardecln
	  where r1.nomos=den_prz_code
  	      and r1.gtaxicode
  	      and decode(r1.gtypecode,'T21','T201','T22','T202+203','T60','T6+7+8+0',r1.gtypecode) = den_prb_code;

	DEN 	C2%ROWTYPE;   */


  --counter for committing every 1000 records proccessed
  I				                      NUMBER    ;
  
  --counter for inserted record in tmp_samples
  sam_rec                       number    ;
  --counter for inserted record in tmp_questapo
  qud_rec                       number    ;
  --counter for inserted record in tmp_diar_samples
  sam1_rec                      number    ;

  --holds current sam
  v_current_ame                 VARCHAR2(8);


--variables for sydelestes
  NMAX                          NUMBER(7)    ;
  NMIN                          NUMBER(7)    ;
  
  G21_CODE_NULL                 EXCEPTION;

BEGIN

        --clear tmp tables
        begin
           delete from tmp_samples ;
           delete from tmp_diar_samples;
           delete from tmp_questapo 
                  where QUD_CAT_ID in (select d.cat_id from diar_categories d
                                   where d.table_name = 'geo1'
                                   and d.year=2005);
                             
        --   delete from tmp_samples1;
           
           commit;                     
                             
        end;                     
      
 --initialize counters
	I        := 0 ;
  sam_rec  := 0 ;
  sam1_rec := 0 ;
  qud_rec  := 0 ;


--get each sam separately
FOR R1 IN C1 LOOP
 BEGIN
          --get current ame proccessed
          v_current_ame := NVL(R1.GAME,'');
        	
        	I       := I + 1;
          sam_rec := sam_rec + 1 ;	
      	  sam1_rec := sam1_rec + 1 ;
         
          IF I = 1000 THEN
              	DBMS_OUTPUT.PUT_LINE(sam_rec || ' ROWS PROCCESSED');
                COMMIT;
      		      I := 0;
          END IF;
          
          
         
          --BLOCK FOR SYDELESTES       
          --gia biologikes - zwikes ekmettalleyseis
  
          IF r1.gstrcode  = '9990' THEN
                   NMAX := 1; 
          	       NMIN := 1; 
          ELSIF r1.gstrcode IN ('9991','9992','9993','9994','9995','9996') THEN
          --gia biologikes- fytikes
            SELECT T.NMIN, 'ROUND(T.SYDEL*T.NMIN,0)'
             INTO   NMIN, NMAX
             FROM SPR_VIOKAL_ANAG T;
         ELSE
          --GIA TIS YPOLOIPES
          SELECT DEN_TOTNUM, DEN_SAMPLNUM 
      	   INTO   NMAX,NMIN  
      	   FROM SPR_MHT_DIARDECLN
      	   where r1.nomos = den_prz_code
      	      and r1.gtaxicode= DEN_LAY_CODE
      	      and r1.gtypecode = den_type_code
              and den_res_year = 2005
              and den_rsc_code=01;
         END IF;

    

          
	  
-- PRE LOAD CONTROLS  FOR MITRWO -->NOT HERE.  
--check the sam state (g21 attribute) to determine which
--PERSONS AND ADDRESS data will be selected
          
--IN VERSION ORACLE 9+, CONVERT THIS CODE TO SELECT CASE...END, CODE
/*	
--CHECK IF ADDRESS DATA IS FILLED
FUNCTION isFILLED_AddressData(
					STREET			       VARCHAR2,
					STREET_NUMBER	     VARCHAR2,
					ZIPCODE            VARCHAR2,
					TEL                VARCHAR2
					)
RETURN BOOLEAN
IS
BEGIN
	IF LTRIM(RTRIM(STREET)) IS NULL AND LTRIM(RTRIM(STREET_NUMBER)) IS NULL AND LTRIM(RTRIM(ZIPCODE)) IS NULL AND LTRIM(RTRIM(TEL)) IS NULL THEN
		RETURN FALSE;
	ELSE
		RETURN TRUE;
  END IF;
END;

---
start of controls

IF R1.G21 IN (1,3,4,13) THEN
		--persons data from kefalaio 1
      LAST_NAME      :=r1.g1epon               ;
  		FIRST_NAME     :=r1.g1onom               ;
  		FATHERNAME     :=r1.g1patron             ;
  		BIRTH_YR       :=r1.g1etos               ;
  		IDCARD         :=r1.g1police             ;
  		AFM            :=r1.g1afm                ;
  		LEGALPERS      :=r1.g1company            ;
			
			IF   LTRIM(RTRIM(r1.g22gcode)) IS NOT NULL AND isFILLED_AddressData(r1.g22odos,r1.g22arith,r1.g22tcode,r1.g22tel) THEN
				--address data from kefalaio 2.3
					STREET	:=r1.g22odos;
					STREET_NUMBER :=r1.g22arith  ;
					ZIPCODE        :=r1.g22tcode    ;
					TEL            :=r1.g22tel           ;
					GEOCODE := r1.g22gcode ;
					G22GCODE       :=r1.g22gcode ;
			ELSE
				--address data from kefalaio1 
					STREET	:=r1.g1odos;
					STREET_NUMBER :=r1.g1arith  ;
					ZIPCODE        :=r1.g1tcode    ;
					TEL            :=r1.g1tel           ;
					GEOCODE := r1.ggcode ;
					G22GCODE       :=r1.g22gcode ;
      END IF;
ELSIF R1.G21 IN (2) THEN
			--CHECK FOR PERSONS DATA
			IF LTRIM(RTRIM(r1.g22epon)) IS NULL AND LTRIM(RTRIM(r1.g22company)) IS NULL THEN
					LAST_NAME      :=r1.g1epon               ;
					FIRST_NAME     :=r1.g1onom               ;
					FATHERNAME     :=r1.g1patron             ;
					BIRTH_YR       :=r1.g1etos               ;
					IDCARD         :=r1.g1police             ;
					AFM            :=r1.g1afm                ;
					LEGALPERS      :=r1.g1company            ;
      ELSE
					LAST_NAME      :=r1.g22epon               ;
					FIRST_NAME     :=r1.g22onom               ;
					FATHERNAME     :=r1.g22patron             ;
					BIRTH_YR       :=r1.g22etos               ;
					IDCARD         :=r1.g22police             ;
					AFM            :=r1.g22afm                ;
					LEGALPERS      :=r1.g22company            ;
      END IF;    
       
			IF   LTRIM(RTRIM(r1.g22gcode)) IS NOT NULL AND isFILLED_AddressData(r1.g22odos,r1.g22arith,r1.g22tcode,r1.g22tel) THEN
				--address data from kefalaio 2.3
					STREET	:=r1.g22odos;
					STREET_NUMBER :=r1.g22arith  ;
					ZIPCODE        :=r1.g22tcode    ;
					TEL            :=r1.g22tel           ;
					GEOCODE := r1.g22gcode ;
					G22GCODE       :=r1.g22gcode ;
			ELSE
				--address data from kefalaio1 
					STREET	:=r1.g1odos;
					STREET_NUMBER :=r1.g1arith  ;
					ZIPCODE        :=r1.g1tcode    ;
					TEL            :=r1.g1tel           ;
					GEOCODE := r1.ggcode ;
					G22GCODE       :=r1.g22gcode ;
      END IF;
ELSIF R1.G21 IN (5,6,7,8) THEN
			LAST_NAME      :=r1.g22epon               ;
			FIRST_NAME     :=r1.g22onom               ;
			FATHERNAME     :=r1.g22patron             ;
			BIRTH_YR       :=r1.g22etos               ;
			IDCARD         :=r1.g22police             ;
			AFM            :=r1.g22afm                ;
			LEGALPERS      :=r1.g22company            ;
			
			IF   LTRIM(RTRIM(r1.g22gcode)) IS NOT NULL AND isFILLED_AddressData(r1.g22odos,r1.g22arith,r1.g22tcode,r1.g22tel) THEN
				--address data from kefalaio 2.3
					STREET	:=r1.g22odos;
					STREET_NUMBER :=r1.g22arith  ;
					ZIPCODE        :=r1.g22tcode    ;
					TEL            :=r1.g22tel           ;
					GEOCODE := r1.g22gcode ;
					G22GCODE       :=r1.g22gcode ;
			ELSE
				--address data from kefalaio1 
					STREET	:=r1.g1odos;
					STREET_NUMBER :=r1.g1arith  ;
					ZIPCODE        :=r1.g1tcode    ;
					TEL            :=r1.g1tel           ;
					GEOCODE := r1.ggcode ;
					G22GCODE       :=r1.g22gcode ;
       END IF;
ELSIF R1.G21 IN (9,10,11,12,14,15) THEN
					LAST_NAME      :=r1.g1epon               ;
					FIRST_NAME     :=r1.g1onom               ;
					FATHERNAME     :=r1.g1patron             ;
					BIRTH_YR       :=r1.g1etos               ;
					IDCARD         :=r1.g1police             ;
					AFM            :=r1.g1afm                ;
					LEGALPERS      :=r1.g1company            ;
					STREET	:=r1.g1odos;
					STREET_NUMBER :=r1.g1arith  ;
					ZIPCODE        :=r1.g1tcode    ;
					TEL            :=r1.g1tel           ;
					GEOCODE := r1.ggcode ;
					G22GCODE       :=r1.g22gcode ;
ELSE 
         RAISE G21_CODE_NULL;

END IF;
                         
         
---END OF CHECK 
*/

--INSERT INTO TMP_SAMPLES       
insert into tmp_samples (SAM_RES_YEAR,
                        SAM_RSC_CODE,
          				 			SAM_CODE,
                        SAM_LAST_NAME,
                        SAM_FIRST_NAME,
                        SAM_FATHERNAME,
                        SAM_BIRTH_YR,
                        SAM_IDCARD,
                        SAM_LEGALPERS,
                        SAM_AFM,
                        SAM_ADDRESS,
                        SAM_STREET_NUMBER,
                        SAM_ZIPCODE,
                        SAM_TEL,
                        SAM_NMAX,
                        SAM_NMIN,
                        SAM_SM_CODE,
                        SAM_GEOCODE,
                        SAM_NOMOS,
					              SAM_LAY_CODE,
          				      SM_CHA_CODE,
                        SAM_MEMBER ,
                        SAM_QUED ,
                        SAM_AA_QUED ,
                        SAM_SECTION ,
                        SAM_LEGAL ,
                        SAM_PERSONAL ,
                        SAM_RELATION ,
                        SAM_DEPT,
                        SAM_FLG,
                        SAM_CREATED_BY,
                        SAM_DATE_CREATED,
                        SAM_SYDEL       )                                          
            values  	('2005',
                       '01',
                       'r1.game',	 
                       'r1.g1epon',
                       'r1.g1onom',
                       'r1.g1patron',
                       'r1.g1etos',
                       'r1.g1police',
                       'r1.g1company',
                       'r1.g1afm',
                       'r1.g1odos',
                       'r1.g1arith',
                       'r1.g1tcode',
                       'r1.g1tel',
                       'NMAX',
                       'NMIN',
                       'r1.g21q',
           			       'r1.ggcode',
           			       'r1.nomos',
          				 		 'r1.gtaxicode',
                       'r1.g21', 
                       'r1.g32a',
                       'r1.gaaerot',
                       'r1.gerocode',
                       'r1.gstrcode',
                       'r1.g31',
                       'r1.g32',
                       'r1.g32b',
                       'r1.gtypecode',
                       'r1.gamne',
                       'r1.gameuser',
                       'SYSDATE',
                       'ROUND(NMAX/NMIN,5)');

--INSERT INTO TMP_DIAR_SAMPLES
INSERT INTO tmp_diar_samples (sam_res_year, 
                              sam_rsc_code, 
                              sam_code, 
                              sam_created_by,
                              sam_date_created,                               
                              sam_number_new, 
                              sam_current_state, 
                              sam_agr_occupation, 
                              sam_new_geocode, 
                              sam_training, 
                              sam_training_level, 
                              sam_consumption, 
                              sam_sale, 
                              sam_other_position, 
                              sam_other_geocode, 
                              sam_other_area, 
                              sam_other_stavloi, 
                              sam_current_area, 
                              sam_current_stavloi, 
                              sam_face, 
                              sam_duration, 
                              sam_date, 
                              sam_cooperation, 
                              sam_notes, 
                              sam_geocode,
                              sam_new_last_name,
                              SAM_NEW_FIRST_NAME    ,
                              SAM_NEW_FATHERNAME    ,
                              SAM_NEW_BIRTH_YR      ,
                              SAM_NEW_IDCARD        ,
                              SAM_NEW_LEGALPERS     ,
                              SAM_NEW_AFM           ,
                              SAM_NEW_STREET        ,
                              SAM_NEW_STREET_NUMBER ,
                              SAM_NEW_ZIPCODE       ,
                              SAM_NEW_TEL)
VALUES                       
                              ('2005',
                               '01',
                               'r1.game',
                               'r1.gameuser',
                               'SYSDATE',
                               'r1.g21b4n',
                               'r1.g21g',
                               'r1.g21d',
                          		 'r1.g22gcode',
                               'r1.g33',
                               'r1.g33a',
                               'r1.g41',
                               'r1.g42',
                               'r1.g71',
                               'r1.g72gcode',
                               'r1.g7216',
                               'r1.g7217',
                               'r1.g7218',
                               'r1.g7219',
                               'r1.gface',
                               'r1.gduration',
                               'r1.gdate',
                               'r1.gcoop',
                               'r1.notes',
                               'r1.ggcode',
                               'r1.g22epon',
                               'r1.g22onom',
                               'r1.g22patron',
                               'r1.g22etos',
                               'r1.g22police',
                               'r1.g22company',
                               'r1.g22afm',
                               'r1.g22odos',
                               'r1.g22arith',
                               'r1.g22tcode',
                               'r1.g22tel'                            
                               );
--
                      
--insert kefalaio 5 into tmp_questapo(i.e. diar_questdets)
                if  nvl(r1.g501,0) <> 0 then
                  insert into tmp_questapo (                
                                            QUD_RSC_CODE  ,
                                            QUD_RES_YEAR  ,
                                            QUD_SAM_CODE  ,
                                            QUD_CAT_ID       ,
                                            QUD_NUM       ,
                                            QUD_GEOCODE
                                                               )                       
                   								values  	(	 '01','2005',
                   													'r1.game',
                                            258,
                                            'r1.g501',
                                            'r1.ggcode')
                                  ;
                    qud_rec := qud_rec + 1;
                 end if;     
                 if nvl(r1.g502,0) <> 0 then        
                  insert into tmp_questapo (                
                                            QUD_RSC_CODE  ,
                                            QUD_RES_YEAR  ,
                                            QUD_SAM_CODE  ,
                                            QUD_CAT_ID       ,
                                            QUD_NUM       ,
                                            QUD_GEOCODE
                                                               )                       
                   								values  	(	 '01','2005',
                   													'r1.game',         								
                                            267,
                                            'r1.g502',
                                            'r1.ggcode')
                                            ;
                  qud_rec := qud_rec + 1;                          
                  end if;
                  if nvl(r1.g503,0) <> 0 then                                           
                  insert into tmp_questapo (                
                                            QUD_RSC_CODE  ,
                                            QUD_RES_YEAR  ,
                                            QUD_SAM_CODE  ,
                                            QUD_CAT_ID       ,
                                            QUD_NUM       ,
                                            QUD_GEOCODE                                            
                                                               )                       
                        					values  	(	 '01','2005',
                                            'r1.game',
                                            591,
                                            'r1.g503'  ,
                                            'r1.ggcode')
                                            ;
                    qud_rec := qud_rec + 1;                         
                   end if;
                   if nvl(r1.g504,0) <> 0 then                          
                   insert into tmp_questapo (                
                                            QUD_RSC_CODE  ,
                                            QUD_RES_YEAR  ,
                                            QUD_SAM_CODE  ,
                                            QUD_CAT_ID       ,
                                            QUD_NUM       ,
                                            QUD_GEOCODE
                                                               )                       
                   								values  	(	 '01','2005',
                   													'r1.game',         								
                                            594,
                                            'r1.g504'   ,
                                            'r1.ggcode'    )
                                            ;
                    qud_rec := qud_rec + 1;                         
                   end if;
                   if nvl(r1.g505,0) <> 0 then                         
                   insert into tmp_questapo (                
                                            QUD_RSC_CODE  ,
                                            QUD_RES_YEAR  ,
                                            QUD_SAM_CODE  ,
                                            QUD_CAT_ID       ,
                                            QUD_NUM       ,
                                            QUD_GEOCODE
                                                               )                       
                   								values  	(	 '01','2005',
                   													'r1.game',         								
                                            595,
                                            'r1.g505',
                                            'r1.ggcode'
                                            )
                                            ;
                      qud_rec := qud_rec + 1;                       
                    end if;
                    if nvl(r1.g506,0) <> 0 then                         
                    insert into tmp_questapo (                
                                            QUD_RSC_CODE  ,
                                            QUD_RES_YEAR  ,
                                            QUD_SAM_CODE  ,
                                            QUD_CAT_ID       ,
                                            QUD_NUM       ,
                                            QUD_GEOCODE
                                                               )                       
                        					values  	(	 '01','2005',
                   													'r1.game',              					
                                            596,
                                            'r1.g506'   ,
                                            'r1.ggcode')
                                            ;
                    	qud_rec := qud_rec + 1;                         
                    end if;                                                                           
                   if nvl(r1.g507,0) <> 0 then                
                   insert into tmp_questapo (                
                                            QUD_RSC_CODE  ,
                                            QUD_RES_YEAR  ,
                                            QUD_SAM_CODE  ,
                                            QUD_CAT_ID       ,
                                            QUD_NUM       ,
                                            QUD_GEOCODE
                                                               )                       
                   								values  	(	 '01','2005',
                   													'r1.game',         								
                                            597,
                                            'r1.g507',
                                            'r1.ggcode'      )
                                            ;
                      qud_rec := qud_rec + 1;                       
                    end if;
                   if nvl(r1.g508,0) <> 0 then                                    
                   insert into tmp_questapo (                
                                            QUD_RSC_CODE  ,
                                            QUD_RES_YEAR  ,
                                            QUD_SAM_CODE  ,
                                            QUD_CAT_ID       ,
                                            QUD_NUM       ,
                                            QUD_GEOCODE
                                                               )                       
                   								values  	(	 '01','2005',
                   													'r1.game',         								
                                            592,
                                            'r1.g508',
                                            'r1.ggcode')
                                            ;
                    qud_rec := qud_rec + 1;                         
                   end if;
                  if nvl(r1.g509,0) <> 0 then                           
                  insert into tmp_questapo (                
                                            QUD_RSC_CODE  ,
                                            QUD_RES_YEAR  ,
                                            QUD_SAM_CODE  ,
                                            QUD_CAT_ID       ,
                                            QUD_NUM       ,
                                            QUD_GEOCODE
                                                               )                       
                   								values  	(	 '01','2005',
                   													'r1.game',         								
                                            593,
                                            'r1.g509',
                                            'r1.ggcode' )
                                            ;
                    qud_rec := qud_rec + 1;                         
                  end if;
                  if nvl(r1.g510,0) <> 0 then                           
                  insert into tmp_questapo (                
                                            QUD_RSC_CODE  ,
                                            QUD_RES_YEAR  ,
                                            QUD_SAM_CODE  ,
                                            QUD_CAT_ID       ,
                                            QUD_NUM       ,
                                            QUD_GEOCODE
                                                               )                       
                   								values  	(	 '01','2005',
                   													'r1.game',         								
                                            268,
                                            'r1.g510',
                                            'r1.ggcode' )
                                            ;
                    qud_rec := qud_rec + 1;                         
                  end if;
                  if nvl(r1.g611,0) <> 0 then                           
                  insert into tmp_questapo (                
                                            QUD_RSC_CODE  ,
                                            QUD_RES_YEAR  ,
                                            QUD_SAM_CODE  ,
                                            QUD_CAT_ID       ,
                                            QUD_NUM       ,
                                            QUD_GEOCODE    
                                                         )                       
                   								values  	(	 '01','2005',
                   													'r1.game',         								
                                            261,
                                            'r1.g611',
                                            'r1.ggcode'   )
                                            ;
                    qud_rec := qud_rec + 1;                         
                   end if;
                   if nvl(r1.g612,0) <> 0 then                          
                   insert into tmp_questapo (                
                                            QUD_RSC_CODE  ,
                                            QUD_RES_YEAR  ,
                                            QUD_SAM_CODE  ,
                                            QUD_CAT_ID       ,
                                            QUD_NUM       ,
                                            QUD_GEOCODE
                                                               )                       
                   								values  	(	 '01','2005',
                   													'r1.game',         								
                                            262,
                                            'r1.g612',
                                            'r1.ggcode'  )
                                            ;
                    qud_rec := qud_rec + 1;                         
                  end if;
                  if nvl(r1.g613,0) <> 0 then                           
                  insert into tmp_questapo (                
                                            QUD_RSC_CODE  ,
                                            QUD_RES_YEAR  ,
                                            QUD_SAM_CODE  ,
                                            QUD_CAT_ID       ,
                                            QUD_NUM       ,
                                            QUD_GEOCODE
                                                               )                       
                   								values  	(	 '01','2005',
                   													'r1.game',          								
                                            263,
                                            'r1.g613',
                                            'r1.ggcode'  )
                                            ;
                      qud_rec := qud_rec + 1;                       
                    end if;
                    if nvl(r1.g614,0) <> 0 then                         
                    insert into tmp_questapo (                
                                            QUD_RSC_CODE  ,
                                            QUD_RES_YEAR  ,
                                            QUD_SAM_CODE  ,
                                            QUD_CAT_ID       ,
                                            QUD_NUM       ,
                                            QUD_GEOCODE
                                                               )                       
                  								 values  	(	 '01','2005',
                   													'r1.game',         								 
                                            264,
                                            'r1.g614',
                                            'r1.ggcode'   )
                                            ;
                      qud_rec := qud_rec + 1;                       
                    end if;
                    if nvl(r1.g615,0) <> 0 then                         
                    insert into tmp_questapo (                
					    QUD_RES_YEAR  ,
                                            QUD_RSC_CODE  ,
                                            QUD_SAM_CODE  ,
                                            QUD_CAT_ID       ,
                                            QUD_NUM       ,
                                            QUD_GEOCODE
                                                               )                       
                   								values  	(	 '01','2005',
                   													'r1.game',          								
                                            265,
                                            'r1.g615',
                                            'r1.ggcode'   );
                   qud_rec := qud_rec + 1;  
                   end if;  
  EXCEPTION
   WHEN NO_DATA_FOUND THEN
     rollback;
     	dbms_output.put_line( 'ERROR WHILE INSERTING RECORD: '||sam_rec );
 		  dbms_output.put_line( 'ERROR WHILE INSERTING RECORD: '||v_current_ame );
      DBMS_OUTPUT.PUT_LINE( 'THERE IS NO SYDEL FOUND');
   WHEN TOO_MANY_ROWS THEN
     rollback;
     	dbms_output.put_line( 'ERROR WHILE INSERTING RECORD: '||sam_rec );
 		  dbms_output.put_line( 'ERROR WHILE INSERTING RECORD: '||v_current_ame );
      DBMS_OUTPUT.PUT_LINE( 'MORE THAN ONE SYDEL FOUND');
   WHEN G21_CODE_NULL THEN
      rollback;
     dbms_output.put_line( 'ERROR WHILE INSERTING RECORD: '||sam_rec );
 		 dbms_output.put_line( 'ERROR WHILE INSERTING RECORD: '||v_current_ame );
     DBMS_OUTPUT.PUT_LINE('G21 CODE IS NULL');
	 when others then 
      rollback;
		  dbms_output.put_line( 'ERROR WHILE INSERTING RECORD: '||sam_rec );
 		  dbms_output.put_line( 'ERROR WHILE INSERTING RECORD: '||v_current_ame );
		  dbms_output.put_line( 'RECORDS INSERTED INTO TMP_diar_SAMPLES  : '||sam1_rec );
		  DBMS_OUTPUT.PUT_LINE(SQLERRM);
 END; 
END LOOP;

  COMMIT;
     dbms_output.put_line( 'RECORDS INSERTED INTO TMP_SAMPLES  : '||sam_rec );
     dbms_output.put_line( 'RECORDS INSERTED INTO TMP_DIAR_SAMPLES : '||sam1_rec );
     dbms_output.put_line( 'RECORDS INSERTED INTO TMP_questapo : '||qud_rec );

  EXCEPTION
	   when others then 
      rollback;
		  dbms_output.put_line( 'ERROR WHILE INSERTING RECORD: '||sam_rec );
 		  dbms_output.put_line( 'ERROR WHILE INSERTING RECORD: '||v_current_ame );
		  dbms_output.put_line( 'RECORDS INSERTED INTO TMP_diar_SAMPLES  : '||sam1_rec );
		  DBMS_OUTPUT.PUT_LINE(SQLERRM);
END;


PROCEDURE geo2(p_nomos varchar2) IS

/*************************************************************************************
***  LOADIND  TMP_questapo  FROM  spr_diar_2005_geo2                                          ***
*** 10/05/2006                                                                      ***
*** by gpapas                                                                      ***
*************************************************************************************/

  --declare cursor for all ekmetalleuseis in spr_diar_2005_geo2                                                                                      
	CURSOR C1 IS
     SELECT g2.*, t.sam_geocode
			FROM spr_diar_2005_geo2 g2, tmp_samples t
			where g2.game=t.sam_code;
--      and   g2.nomos=p_nomos ;
	
  --declare cursor for mappings between categories and geo fields (mappings)
  --Mappings are stored in DIAR_CATEGORIES 
  CURSOR CAT IS
  SELECT * 
			FROM diar_categories 
		where table_name='geo2'
    and year = 2005	    ;

  --used for committing every 1000 records proccessed
  I			  	NUMBER     ;
  --holds total number of records proccessed
  QUD_REC  	NUMBER     ;
  --holds research code (:=01)
  RSC_CODE  VARCHAR2(2);
  
  --insert command are created dynamically
  --according to mappings
  TXT       VARCHAR2(1000);
  TXT1      VARCHAR2(1000);
  TXT2      VARCHAR2(1000);
  
  --holds current sam
  v_current_ame   VARCHAR2(8);
  --hold current category_id
  v_current_cat    diar_categories.cat_id%type;
  
BEGIN

  begin
  
   --delete existing records in tmp table
   delete from tmp_questapo
   where QUD_CAT_ID in (select cat_id from diar_categories
   where table_name='geo2'
   and year = 2005	 );
  
/*   delete  from spr_diar_2005_geo2
   where 
   nvl(G8013,0)+nvl(G8014,0)+nvl(G8015,0)+nvl(G8023,0)+nvl(G8024,0)+          
   nvl(G8025,0)+nvl(G8033,0)+nvl(G8034,0)+nvl(G8035,0)+nvl(G8043,0)+          
   nvl(G8044,0)+nvl(G8045,0)+nvl(G8053,0)+nvl(G8054,0)+nvl(G8055,0)+          
   nvl(G8063,0)+nvl(G8064,0)+nvl(G8065,0)+nvl(G8073,0)+nvl(G8074,0)+          
   nvl(G8075,0)+nvl(G8083,0)+nvl(G8084,0)+nvl(G8085,0)+nvl(G8093,0)+          
   nvl(G8094,0)+nvl(G8095,0)+nvl(G8103,0)+nvl(G8104,0)+nvl(G8105,0)+          
   nvl(G8113,0)+nvl(G8114,0)+nvl(G8115,0)+nvl(G8123,0)+nvl(G8124,0)+          
   nvl(G8125,0)+nvl(G8133,0)+nvl(G8134,0)+nvl(G8135,0)+nvl(G8143,0)+          
   nvl(G8144,0)+nvl(G8145,0)+nvl(G8153,0)+nvl(G8154,0)+nvl(G8155,0)+          
   nvl(G8163,0)+nvl(G8164,0)+nvl(G8165,0)+nvl(G8173,0)+nvl(G8174,0)+          
   nvl(G8175,0)+nvl(G8183,0)+nvl(G8184,0)+nvl(G8185,0)+nvl(G8193,0)+          
   nvl(G8194,0)+nvl(G8195,0)+nvl(G8203,0)+nvl(G8204,0)+nvl(G8205,0)+          
   nvl(G8213,0)+nvl(G8214,0)+nvl(G8215,0)+nvl(G8223,0)+nvl(G8224,0)+          
   nvl(G8225,0)+nvl(G8233,0)+nvl(G8234,0)+nvl(G8235,0)+nvl(G8243,0)+          
   nvl(G8244,0)+nvl(G8245,0)+nvl(G8253,0)+nvl(G8254,0)+nvl(G8255,0)+          
   nvl(G8263,0)+nvl(G8264,0)+nvl(G8265,0)+nvl(G8273,0)+nvl(G8274,0)+          
   nvl(G8275,0)+nvl(G8283,0)+nvl(G8284,0)+nvl(G8285,0)+nvl(G8293,0)+          
   nvl(G8294,0)+nvl(G8295,0)+nvl(G8303,0)+nvl(G8304,0)+nvl(G8305,0)+          
   nvl(G8313,0)+nvl(G8314,0)+nvl(G8315,0)+nvl(G8323,0)+nvl(G8324,0)+          
   nvl(G8325,0)+nvl(G8333,0)+nvl(G8334,0)+nvl(G8335,0)+nvl(G8343,0)+          
   nvl(G8344,0)+nvl(G8345,0)+nvl(G8353,0)+nvl(G8354,0)+nvl(G8355,0)+          
   nvl(G8363,0)+nvl(G8364,0)+nvl(G8365,0)+nvl(G8373,0)+nvl(G8374,0)+          
   nvl(G8375,0)+nvl(G8383,0)+nvl(G8384,0)+nvl(G8385,0)+nvl(G8393,0)+          
   nvl(G8394,0)+nvl(G8395,0)+nvl(G8403,0)+nvl(G8404,0)+nvl(G8405,0)+          
   nvl(G8413,0)+nvl(G8414,0)+nvl(G8415,0)+nvl(G8423,0)+nvl(G8424,0)+          
   nvl(G8425,0)+nvl(G8433,0)+nvl(G8434,0)+nvl(G8435,0)+nvl(G8443,0)+          
   nvl(G8444,0)+nvl(G8445,0)+nvl(G8453,0)+nvl(G8454,0)+nvl(G8455,0)+          
   nvl(G8463,0)+nvl(G8464,0)+nvl(G8465,0)+nvl(G8473,0)+nvl(G8474,0)+          
   nvl(G8483,0)+nvl(G8484,0)+nvl(G8493,0)+nvl(G8494,0)+nvl(G8503,0)+          
   nvl(G8504,0)+nvl(G8513,0)+nvl(G8514,0)+nvl(G8523,0)+nvl(G8524,0)+          
   nvl(G8525,0)+nvl(G9533,0)+nvl(G9534,0)+
   nvl(G10543,0)+nvl(G10544,0)+nvl(G10553,0)+nvl(G10563,0)+nvl(G10564,0)  =0 ;
*/	
	 commit ;
	 
   end ;	

   --initialize variables
   I        := 0 ;
   qud_rec  := 0 ;
   RSC_CODE := '01';


   FOR r1 IN C1 LOOP
   
       --get current game
       v_current_ame:=NVL(r1.game,'');
  	 
        FOR CAT_REC IN CAT LOOP

        v_current_cat:=CAT_REC.CAT_ID;
  	    qud_rec := qud_rec + 1 ;	
  	   	I:= I + 1;
         
  	
        IF I = 1000 THEN
          DBMS_OUTPUT.PUT_LINE(qud_rec || ' ROWS PROCCESSED');
        	COMMIT;
	      	I := 0;
        END IF;
       
          	txt := 'insert into tmp_questapo ( '||               
                   'QUD_RSC_CODE  ,'||
                   'QUD_RES_YEAR  ,'||
                   'QUD_SAM_CODE  ,'||
                   'QUD_CAT_ID       ,'||
                   'QUD_NUM       ,'||
                   'QUD_WATERED   ,'||
                   'QUD_SECONDARY  ,'||
                   'QUD_GEOCODE   )'||
                  ' select '||'\'RSC_CODE\',2005,\'R1.GAME\''||',\'CAT_REC.CAT_ID\''||',\'CAT_REC.PED1\',\'
                            CAT_REC.PED2\',\'
                            CAT_REC.PED3\','
                            ||'\'R1.SAM_GEOCODE\''
                            ||' from spr_diar_2005_geo2 where game = '||'\'R1.GAME\';';
                              
             txt1 := 'insert into tmp_questapo ( '||               
                     'QUD_RSC_CODE  ,'||
                     'QUD_RES_YEAR  ,'||
                     'QUD_SAM_CODE  ,'||
                     'QUD_CAT_ID       ,'||
                     'QUD_NUM       ,'||
                     'QUD_WATERED   ,'||
                     'QUD_GEOCODE   )'||
                     ' select '||'\'RSC_CODE\''||',2005,'||'\'R1.GAME\''||',\'CAT_REC.CAT_ID\''||',\'CAT_REC.PED1\','
                              ||'\'CAT_REC.PED2\''||','
                              ||'\'R1.SAM_GEOCODE\''
                              ||' from spr_diar_2005_geo2 where game = '||'\'R1.GAME\';';
      
             txt2 := 'insert into tmp_questapo ( '||               
                     'QUD_RSC_CODE  ,'||
                     'QUD_RES_YEAR  ,'||
                     'QUD_SAM_CODE  ,'||
                     'QUD_CAT_ID       ,'||
                     'QUD_NUM       ,'||
                     'QUD_GEOCODE   )'||
                     ' select '||'\'RSC_CODE\''||',2005,'||'\'R1.GAME\''||',\'CAT_REC.CAT_ID\',
          	     \'CAT_REC.PED1\','
                     ||'\'R1.SAM_GEOCODE\''
                     ||' from spr_diar_2005_geo2 where game = \'R1.GAME\';';
         
             if     nvl(cat_rec.ped2,'0') <> '0'  and nvl(cat_rec.ped3,'0')<>  '0'  then
                  EXECUTE IMMEDIATE TXT;
             elsif  nvl(cat_rec.ped2,'0') <> '0' and  nvl(cat_rec.ped3,'0') =  '0'  then  
            		EXECUTE IMMEDIATE TXT1;
             elsif  nvl(cat_rec.ped2,'0') =  '0' and  nvl(cat_rec.ped3,'0') =  '0'  then
             	    EXECUTE IMMEDIATE TXT2;
             end if;

     END LOOP;
       
  END LOOP;

  
  -- clear null and/or zero records
  delete from tmp_questapo
     where qud_num + qud_watered = 0 ;

 qud_rec := qud_rec-(SQL%ROWCOUNT);
 
  COMMIT;

     dbms_output.put_line( 'RECORDS INSERTED INTO TMP_questapo : '||qud_rec );
     exception
	   when others then
      rollback;
	    dbms_output.put_line( 'TXT = ' ||txt);
			dbms_output.put_line( 'RECORDS INSERTED INTO TMP_questapo : '||qud_rec ); 
		  dbms_output.put_line( 'ERROR WHILE INSERTING RECORD: '||v_current_ame ||'  CAT_ID= '||v_current_cat);
		  DBMS_OUTPUT.PUT_LINE(SQLERRM);
END;

PROCEDURE geo3(p_nomos varchar2) IS

/*************************************************************************************
***  LOADIND  TMP_questapo  FROM  spr_diar_2005_geo3                               ***
*** 10/05/2006                                                                     ***
*** by gpapas                                                                      ***
*************************************************************************************/
	CURSOR C1 IS
		SELECT g3.*, T.SAM_GEOCODE
			FROM spr_diar_2005_geo3 g3, tmp_samples T
			where g3.game=t.sam_code;
--      and   g3.nomos=p_nomos ;
		
	
  CURSOR CAT IS
		SELECT * 
			FROM diar_categories
		where table_name='geo3'	
    and   year='2005' ;

  --used for committing every 1000 records proccessed
  I			  	NUMBER     ;
  --holds total number of records proccessed
  QUD_REC  	NUMBER     ;
  --holds research code (:=01)
  RSC_CODE  VARCHAR2(2);
  
  --insert command are created dynamically
  --according to mappings
  TXT       VARCHAR2(1000);
  
  --holds current sam
  v_current_ame   VARCHAR2(8);
  --hold current category_id
  v_current_cat    diar_categories.cat_id%type;
  
BEGIN
    I        := 0 ;
    qud_rec  := 0 ;
    RSC_CODE := '01';
begin
  
   delete from tmp_questapo
   where QUD_CAT_ID in (select cat_id from diar_categories
   where table_name='geo3'
   and   year='2005');
   
   /*
   delete  from spr_geo3
   where
    nvl(G11573,0)+ nvl(G11574,0)+ nvl(G11583,0)+ nvl(G11584,0)+ nvl(G11593,0)+ nvl(G11594,0)+ 
    nvl(G11603,0)+ nvl(G11604,0)+ nvl(G11613,0)+ nvl(G11614,0)+ nvl(G11623,0)+ nvl(G11624,0)+ 
    nvl(G11633,0)+ nvl(G11634,0)+ nvl(G11643,0)+ nvl(G11644,0)+ nvl(G11653,0)+ nvl(G11654,0)+ 
	  nvl(G11663,0)+ nvl(G11664,0)+ nvl(G11673,0)+ nvl(G11674,0)+ nvl(G11683,0)+ nvl(G11684,0)+ 
    nvl(G11693,0)+ nvl(G11694,0)+ nvl(G11703,0)+ nvl(G11704,0)+ nvl(G11713,0)+ nvl(G11714,0)+ 
    nvl(G11723,0)+ nvl(G11724,0)+ nvl(G11733,0)+ nvl(G11734,0)+ nvl(G11743,0)+ nvl(G11744,0)+ 
    nvl(G11753,0)+ nvl(G11754,0)+ nvl(G11763,0)+ nvl(G11764,0)+ nvl(G11773,0)+ nvl(G11774,0)+ 
    nvl(G11783,0)+ nvl(G11784,0)+ nvl(G11793,0)+ nvl(G11794,0)+ nvl(G11803,0)+ nvl(G11804,0)+ 
    nvl(G11813,0)+ nvl(G11814,0)+ nvl(G11823,0)+ nvl(G11824,0)+ nvl(G11833,0)+ nvl(G11834,0)+ 
    nvl(G11843,0)+ nvl(G11844,0)+ nvl(G11853,0)+ nvl(G11854,0)+ nvl(G11863,0)+ nvl(G11864,0)+ 
    nvl(G11873,0)+ nvl(G11874,0)+ nvl(G11883,0)+ nvl(G11884,0)+ nvl(G11893,0)+ nvl(G11894,0)+ 
    nvl(G11903,0)+ nvl(G11904,0)+ nvl(G11913,0)+ nvl(G11914,0)+ nvl(G11923,0)+ nvl(G11924,0)+ 
    nvl(G11933,0)+ nvl(G11934,0)+ nvl(G11943,0)+ nvl(G11944,0)+ nvl(G11953,0)+ nvl(G11954,0)+ 
    nvl(G11963,0)+ nvl(G11964,0) = 0 ;
    */
    commit;
 
 end;     


 FOR r1 IN C1  LOOP
     --get current game
       v_current_ame:=NVL(r1.game,'');
  	 
        FOR CAT_REC IN CAT LOOP

        v_current_cat:=CAT_REC.CAT_ID;
        qud_rec := qud_rec + 1 ;	
  	
  	    I   := I + 1;
  	
        IF I = 1000 THEN
          DBMS_OUTPUT.PUT_LINE(qud_rec || ' ROWS PROCCESSED');
          COMMIT;
	      	I := 0;
        END IF;
       
                    
        txt := 'insert into tmp_questapo ( '||               
           'QUD_RSC_CODE  ,'||
           'QUD_RES_YEAR  ,'||
           'QUD_SAM_CODE  ,'||
           'QUD_CAT_ID       ,'||
           'QUD_NUM       ,'||
           'QUD_WATERED   ,'||
           'QUD_GEOCODE   )'||
           ' select '||'\'RSC_CODE\''||',2005,'||'\'R1.GAME\''||','||'\'CAT_REC.CAT_ID\''||','||'\'CAT_REC.PED1\''||','
                    ||'\'CAT_REC.PED2\''||','
                    ||'\'R1.SAM_GEOCODE\''||
                    ||' from spr_diar_2005_geo3 where game = '||'\'R1.GAME\';'||;
   
        EXECUTE IMMEDIATE TXT;
   
        END LOOP;

        
  END LOOP;
 
  
   delete from tmp_questapo
     where qud_num+qud_watered+QUD_SECONDARY = 0 ;

     
 qud_rec := qud_rec-(SQL%ROWCOUNT);
 
  COMMIT;
  
     dbms_output.put_line( 'RECORDS INSERTED INTO TMP_questapo : '||qud_rec );
     exception
	   when others then
	    ROLLBACK;
      dbms_output.put_line( 'TXT = ' ||txt);
			dbms_output.put_line( 'RECORDS INSERTED INTO TMP_questapo : '||qud_rec ); 
		  dbms_output.put_line( 'ERROR WHILE INSERTING RECORD: '||v_current_ame ||'  CAT_ID= '||v_current_cat);
		  DBMS_OUTPUT.PUT_LINE(SQLERRM);
END;


PROCEDURE geo4(p_nomos varchar2) IS

/*************************************************************************************
***  LOADIND  TMP_questapo  FROM  spr_diar_2005_geo4                               ***
*** 10/05/2006                                                                     ***
*** by gpapas                                                                      ***
*************************************************************************************/
	CURSOR C1 IS
		SELECT g4.*, T.SAM_GEOCODE
			FROM spr_diar_2005_geo4 g4, tmp_samples T
			where g4.game=t.sam_code;
--      and   g4.nomos=p_nomos ;


  CURSOR CAT IS
		SELECT * 
			FROM diar_categories
		where table_name='geo4'
    and year='2005'	 ;

 
  --used for committing every 1000 records proccessed
  I			  	NUMBER     ;
  --holds total number of records proccessed
  QUD_REC  	NUMBER     ;
  --holds research code (:=01)
  RSC_CODE  VARCHAR2(2);
  --holds part time workers records
  PART_REC  NUMBER     ;
  --holds machines records
  MACH_REC  NUMBER     ;
    --holds questdets records
  QUD1_REC  NUMBER     ;
  
  --insert commands are created dynamically
  --according to mappings
  TXT1      VARCHAR2(1000);
  TXT2      VARCHAR2(1000);  
  txtMachines VARCHAR2(1000);  
  
  --holds current sam
  v_current_ame   VARCHAR2(8);
  --hold current category_id
  v_current_cat    diar_categories.cat_id%type;


  --exception for other animals
  OTHER_ANIMALS_EXCEPTION      EXCEPTION;
  
BEGIN

begin
  
  --delete values for categories of geo 4
   delete from tmp_questapo
   where QUD_CAT_ID in (select cat_id from diar_categories
                     where table_name='geo4'
                     and year='2005');
   
   --delete explicitly categories 606-610 (they are not in diar_categories)
   delete from tmp_questapo 
   where QUD_CAT_ID in (606,607,608,609,610); 
   
   --delete  part workers
   delete from tmp_partworkers;  
   
   --delete  machines
   delete from tmp_machines;                    
   commit;
   end;      
	
    i        := 0;
    qud_rec  := 0 ;
    qud1_rec := 0 ;
    PART_rec := 0 ;
    MACH_REC := 0 ;
    RSC_CODE := '01';

 FOR r1 IN C1  LOOP
     --get current game
       v_current_ame:=NVL(r1.game,'');
  	 
        FOR CAT_REC IN CAT LOOP

        v_current_cat:=CAT_REC.CAT_ID;
        qud_rec := qud_rec + 1 ;	
  	
    
      	I       := I + 1;
  	
        IF I = 1000 THEN
          DBMS_OUTPUT.PUT_LINE(qud_rec || ' ROWS PROCCESSED');
          COMMIT;
	      	I := 0;
        END IF;
                      
         txt1 := 'insert into tmp_questapo ( '||               
                 'QUD_RSC_CODE  ,'||
                 'QUD_RES_YEAR  ,'||
                 'QUD_SAM_CODE  ,'||
                 'QUD_CAT_ID       ,'||
                 'QUD_NUM       ,'||
                 'QUD_WATERED   ,'||
                 'QUD_GEOCODE   )'||
                 ' select \'RSC_CODE\',2005,'||'\'R1.GAME\''||','
                          ||'\'CAT_REC.CAT_ID\''||','
                          ||'\'CAT_REC.PED1\''||','
                          ||'\'CAT_REC.PED2\''||','||'\'R1.SAM_GEOCODE\''
                          ||' from spr_diar_2005_geo4 where game = '||'\'R1.GAME\';';
         txt2 := 'insert into tmp_questapo ( '||               
                 'QUD_RSC_CODE  ,'||
                 'QUD_RES_YEAR  ,'||
                 'QUD_SAM_CODE  ,'||
                 'QUD_CAT_ID       ,'||
                 'QUD_NUM       ,'||
                 'QUD_GEOCODE   )'||
                 ' select \'RSC_CODE\''||',2005,\'R1.GAME\',\'
                 CAT_REC.CAT_ID\',\'
                 ||CAT_REC.PED1\',\'
	         R1.SAM_GEOCODE\''
                 ||' from spr_diar_2005_geo4 where game = \'R1.GAME\';';
         
          txtMachines := 'insert into tmp_machines ( '||               
                 'MAC_RSC_CODE  ,'||
                 'MAC_RES_YEAR  ,'||
                 'MAC_SAM_CODE  ,'||
                 'MAC_CAT_ID    ,'||
                 'MAC_OWNED   ,'||
                 'MAC_COOWNED   ,'||
                 'MAC_OWNEDPERC ,'||
                 'MAC_OTHER     ,'||
                 'MAC_SAM_GEOCODE )'||
                 ' select '||'\'RSC_CODE\''||',2005,'||'\'R1.GAME\''||','
                          ||'\'CAT_REC.CAT_ID\''||','
                          ||'\'CAT_REC.PED1\''||','
                          ||'\'CAT_REC.PED2\''||','
                          ||'\'CAT_REC.PED3\''||','
                          ||'\'CAT_REC.PED4\''||','
                          ||'\'R1.SAM_GEOCODE\''||
                          ||' from spr_diar_2005_geo4 where game = '||'\'R1.GAME\';';
                          
         
         --CHECK FOR MACHINES 
         IF NVL(CAT_REC.CAT_ID,0) IN (2922,2923,2924,2925,29211,29212,29213,29214,29215) THEN
          	EXECUTE IMMEDIATE txtMachines;
            MACH_REC := MACH_REC +1 ;
         ELSIF  nvl(cat_rec.ped2,'0') <> '0' and  nvl(cat_rec.ped3,'0') =  '0'  then  
      				EXECUTE IMMEDIATE TXT1;
               qud1_rec := qud1_rec + 1 ;	
         ELSIF  nvl(cat_rec.ped2,'0') =  '0' and  nvl(cat_rec.ped3,'0') =  '0'  then
         	    EXECUTE IMMEDIATE TXT2;
               qud1_rec := qud1_rec + 1 ;	
         END IF;
              
        END LOOP;


--insert explicitly values for other animals (i.e., cat_ids=606..610) 

--CHECK IF SAME CODES HAVE BEEN INSERTED
IF r1.g27code= r1.g27code_2 THEN
    --two animals with the same code exist
     RAISE OTHER_ANIMALS_EXCEPTION;
END IF;   

   
  if r1.g27code = '37'  then
    	 insert into tmp_questapo (              
           QUD_RSC_CODE  ,
           QUD_RES_YEAR  ,
           QUD_SAM_CODE  ,
           QUD_CAT_ID       ,
           QUD_NUM       ,
           QUD_GEOCODE)
        values ('01',2005,'r1.game',610,'r1.g27other','R1.SAM_GEOCODE');
        qud1_rec := qud1_rec + 1 ;	
     end if ;
  
  if r1.g27code = '38'  then
    	 insert into tmp_questapo (              
           QUD_RSC_CODE  ,
           QUD_RES_YEAR  ,
           QUD_SAM_CODE  ,
           QUD_CAT_ID       ,
           QUD_NUM       ,
           QUD_GEOCODE)
        values ('01',2005,'r1.game',606,'r1.g27other', 'R1.SAM_GEOCODE');
        qud1_rec := qud1_rec + 1 ;	
     end if ;
     
     if r1.g27code = '39'  then
    	 insert into tmp_questapo (              
           QUD_RSC_CODE  ,
           QUD_RES_YEAR  ,
           QUD_SAM_CODE  ,
           QUD_CAT_ID       ,
           QUD_NUM       ,
           QUD_GEOCODE)
        values ('01',2005,'r1.game',607,'r1.g27other', 'R1.SAM_GEOCODE');
        qud1_rec := qud1_rec + 1 ;	
     end if ;
     
     if r1.g27code = '40'  then
    	 insert into tmp_questapo (              
           QUD_RSC_CODE  ,
           QUD_RES_YEAR  ,
           QUD_SAM_CODE  ,
           QUD_CAT_ID       ,
           QUD_NUM       ,
           QUD_GEOCODE)
        values ('01',2005,'r1.game',608,'r1.g27other', 'R1.SAM_GEOCODE');
        qud1_rec := qud1_rec + 1 ;	
     end if ;
     
     if r1.g27code= '41'  then
    	 insert into tmp_questapo (              
           QUD_RSC_CODE  ,
           QUD_RES_YEAR  ,
           QUD_SAM_CODE  ,
           QUD_CAT_ID       ,
           QUD_NUM       ,
           QUD_GEOCODE)
        values ('01',2005,'r1.game',609,'r1.g27other', 'R1.SAM_GEOCODE');
        qud1_rec := qud1_rec + 1 ;	
     end if ;

     
  if r1.g27code_2 = '37'  then
    	 insert into tmp_questapo (              
           QUD_RSC_CODE  ,
           QUD_RES_YEAR  ,
           QUD_SAM_CODE  ,
           QUD_CAT_ID       ,
           QUD_NUM       ,
           QUD_GEOCODE)
        values ('01',2005,'r1.game',610,'r1.g27other_2', 'R1.SAM_GEOCODE');
        qud1_rec := qud1_rec + 1 ;	
     end if ;
  
  if r1.g27code_2 = '38'  then
    	 insert into tmp_questapo (              
           QUD_RSC_CODE  ,
           QUD_RES_YEAR  ,
           QUD_SAM_CODE  ,
           QUD_CAT_ID       ,
           QUD_NUM       ,
           QUD_GEOCODE)
        values ('01',2005,'r1.game',606,'r1.g27other_2', 'R1.SAM_GEOCODE');
        qud1_rec := qud1_rec + 1 ;	
     end if ;
     
     if r1.g27code_2 = '39'  then
    	 insert into tmp_questapo (              
           QUD_RSC_CODE  ,
           QUD_RES_YEAR  ,
           QUD_SAM_CODE  ,
           QUD_CAT_ID       ,
           QUD_NUM       ,
           QUD_GEOCODE)
        values ('01',2005,'r1.game',607,'r1.g27other_2', 'R1.SAM_GEOCODE');
        qud1_rec := qud1_rec + 1 ;	
     end if ;
     
     if r1.g27code_2 = '40'  then
    	 insert into tmp_questapo (              
           QUD_RSC_CODE  ,
           QUD_RES_YEAR  ,
           QUD_SAM_CODE  ,
           QUD_CAT_ID       ,
           QUD_NUM       ,
           QUD_GEOCODE)
        values ('01',2005,'r1.game',608,'r1.g27other_2', 'R1.SAM_GEOCODE');
        qud1_rec := qud1_rec + 1 ;	
     end if ;
     
     if r1.g27code_2 = '41'  then
    	 insert into tmp_questapo (              
           QUD_RSC_CODE  ,
           QUD_RES_YEAR  ,
           QUD_SAM_CODE  ,
           QUD_CAT_ID       ,
           QUD_NUM       ,
           QUD_GEOCODE)
        values ('01',2005,'r1.game',609,'r1.g27other_2', 'R1.SAM_GEOCODE');
        qud1_rec := qud1_rec + 1 ;	
     end if ;



   
--insert part time workers     
    
   if nvl(r1.g312t,0) + nvl(r1.g313t,0) > 0  then
   insert into tmp_partworkers (PAW_RSC_CODE,                   
 																PAW_RES_YEAR ,                   
 																PAW_SAM_CODE ,                   
 																PAW_PARTIME  ,                   
 																PAW_SEX      ,                   
 																PAW_PERSONS  ,                            
 																PAW_WAGES    ,
                                PAW_SAM_GEOCODE)
 	  values  ( '01',2005,'R1.GAME',1,3,'r1.g312t','r1.g313t', 'r1.sam_geocode') ;
 	  part_REC := part_REC + 1 ;
    end if;
 	  
    if nvl(r1.g312m,0) + nvl(r1.g313m,0) > 0  then
    insert into tmp_partworkers (PAW_RSC_CODE,                   
 																PAW_RES_YEAR ,                   
 																PAW_SAM_CODE ,                   
 																PAW_PARTIME  ,                   
 																PAW_SEX      ,                   
 																PAW_PERSONS  ,                            
 																PAW_WAGES    ,
                                PAW_SAM_GEOCODE)
 	  values  ( '01',2005,'R1.GAME',1,1,'r1.g312m','r1.g313m', 'r1.sam_geocode') ;
 	  part_REC := part_REC + 1 ;
    end if;
 	  
 	  if nvl(r1.g312f,0) + nvl(r1.g313f,0) > 0  then
 	  insert into tmp_partworkers (PAW_RSC_CODE,                   
 																PAW_RES_YEAR ,                   
 																PAW_SAM_CODE ,                   
 																PAW_PARTIME  ,                   
 																PAW_SEX      ,                   
 																PAW_PERSONS  ,                            
 																PAW_WAGES    ,
                                PAW_SAM_GEOCODE)
 	  values  ( '01',2005,'R1.GAME',1,2,'r1.g312f','r1.g313f', 'r1.sam_geocode') ;
 	  part_REC := part_REC + 1 ;
 	  
 	  end if;
 	  
 	 if nvl(r1.g322t,0) + nvl(r1.g323t,0) > 0  then
   insert into tmp_partworkers (PAW_RSC_CODE,                   
 																PAW_RES_YEAR ,                   
 																PAW_SAM_CODE ,                   
 																PAW_PARTIME  ,                   
 																PAW_SEX      ,                   
 																PAW_PERSONS  ,                            
 																PAW_WAGES    ,
                                PAW_SAM_GEOCODE)
 	  values  ( '01',2005,'R1.GAME',2,3,'r1.g322t','r1.g323t', 'r1.sam_geocode') ;
 	  part_REC := part_REC + 1 ;
    end if;
 	  
    if nvl(r1.g322m,0) + nvl(r1.g323m,0) > 0  then
    insert into tmp_partworkers (PAW_RSC_CODE,                   
 																PAW_RES_YEAR ,                   
 																PAW_SAM_CODE ,                   
 																PAW_PARTIME  ,                   
 																PAW_SEX      ,                   
 																PAW_PERSONS  ,                            
 																PAW_WAGES    ,
                                PAW_SAM_GEOCODE)
 	  values  ( '01',2005,'R1.GAME',2,1,'r1.g322m','r1.g323m', 'r1.sam_geocode') ;
 	  part_REC := part_REC + 1 ;
    end if;
 	  
 	  if nvl(r1.g322f,0) + nvl(r1.g323f,0) > 0  then
 	  insert into tmp_partworkers (PAW_RSC_CODE,                   
 																PAW_RES_YEAR ,                   
 																PAW_SAM_CODE ,                   
 																PAW_PARTIME  ,                   
 																PAW_SEX      ,                   
 																PAW_PERSONS  ,                            
 																PAW_WAGES    ,
                                PAW_SAM_GEOCODE)
 	  values  ( '01',2005,'R1.GAME',2,2,'r1.g322f','r1.g323f',  'r1.sam_geocode') ;
 	  part_REC := part_REC + 1 ;
 	  end if;


-- if g33 is yes(01) 	  
    if nvl(r1.g331,0) + nvl(r1.g332,0) + nvl(r1.g333,0)> 0  THEN
	      insert into tmp_partworkers(PAW_RSC_CODE ,                   
 																PAW_RES_YEAR ,                   
 																PAW_SAM_CODE ,                   
 																PAW_PARTIME  ,                   
 																PAW_SEX      ,                   
 																PAW_PERSONS  ,                            
 																PAW_WAGES    ,
 																PAW_HOURS   ,
                                PAW_SAM_GEOCODE)
 	  values  ( '01',2005,'R1.GAME',3,1,'r1.g331','r1.g333','r1.g332', 'r1.sam_geocode') ;
 	  part_REC := part_REC + 1 ;
 	  end if;

--else
 	  if nvl(r1.g33,0)  =2  then
	     insert into tmp_partworkers (PAW_RSC_CODE ,                   
 																PAW_RES_YEAR ,                   
 																PAW_SAM_CODE ,                   
 																PAW_PARTIME  ,                   
 																PAW_SEX      ,
                                PAW_SAM_GEOCODE)                   
 	  values  ( '01',2005,'R1.GAME',3,2, 'r1.sam_geocode') ;
 	  part_REC := part_REC + 1 ;
 	  end if;

      
  END LOOP;
  
    -- clear null and/or zero records
  delete from TMP_MACHINES T
       where T.MAC_OWNED+T.MAC_COOWNED+T.MAC_OWNEDPERC+T.MAC_OTHER = 0 ;
  MACH_rec := MACH_rec - (SQL%ROWCOUNT);
  
  delete from tmp_questapo
     where qud_num = 0;
  qud1_rec := qud1_rec - (SQL%ROWCOUNT);

  COMMIT;
     dbms_output.put_line( 'RECORDS INSERTED INTO TMP_questapo    : '||qud1_rec );
     dbms_output.put_line( 'RECORDS INSERTED INTO TMP_partworkers : '||part_rec );
     dbms_output.put_line( 'RECORDS INSERTED INTO TMP_MACHINES : '||MACH_rec );
     exception
     when OTHER_ANIMALS_EXCEPTION then
     rollback;
			dbms_output.put_line( 'RECORDS INSERTED INTO TMP_questapo : '||qud1_rec ); 
		  dbms_output.put_line( 'ERROR WHILE INSERTING RECORD: '||v_current_ame);
      dbms_output.put_line( 'TWO DIFFERENT CODES FOR THE SAME ANIMAL IN RECORD: '||v_current_ame);      
	   when others then
     rollback;
	    dbms_output.put_line( 'TXT1 = ' ||txt1);
	    dbms_output.put_line( 'TXT2 = ' ||txt2);
 	    dbms_output.put_line( 'TXT2 = ' ||txtMachines);

			dbms_output.put_line( 'RECORDS INSERTED INTO TMP_questapo : '||qud1_rec ); 
		  dbms_output.put_line( 'ERROR WHILE INSERTING RECORD: '||v_current_ame ||'  CAT_ID= '||v_current_cat );
		  DBMS_OUTPUT.PUT_LINE(SQLERRM);
END;


PROCEDURE geo29(p_nomos varchar2) IS

/*************************************************************************************
***  LOADIND  TMP_HOUSEWORK  FROM  spr_diar_2005_geo29                             ***
*** 10/05/2006                                                                     ***
*** by gpapas                                                                      ***
***************************************************************************************/
	CURSOR C1 IS
			SELECT t.*, s.sam_geocode
      	FROM spr_diar_2005_geo29 t, tmp_samples s
          where t.game29 = s.sam_code;
--         and t.nomos=p_nomos;



 
  I			    	NUMBER     ;
  HOUSE_REC  	NUMBER     ;
  -- count house workers
  var_aa      number     ;
  
  --holds ame code
  var_code    varchar2(8);

  --holds current relation code
  v_current_rel  spr_diar_2005_geo29.g293%type;
  
BEGIN

  begin
    delete from tmp_housework;
    commit;
  end;  
    
    I          := 0 ;
    HOUSE_rec  := 0 ;
    var_aa     := 0 ;
    var_code   := '00000000';


FOR c1_rec IN C1 LOOP

    --GET RECORD VARIABLES
      v_current_rel  :=   c1_rec.g293;

    
    I   :=  I + 1;
  	
     IF I = 1000 THEN
        	COMMIT;
	      	I := 0;
     END IF;
     
     if c1_rec.game29 = var_code then
              var_aa := var_aa + 1 ;
     else  var_aa   :=  1;
           var_code :=  c1_rec.game29;
     end if;           
  	 
  	insert into tmp_housework ( HWO_RSC_CODE ,   
																HWO_RES_YEAR ,  
																HWO_SAM_CODE ,  
																HWO_AA       ,  
																HWO_CODE     ,  
																HWO_MELOS    ,  
																HWO_FLAG     ,  
																HWO_SEX      ,  
																HWO_PROF     ,  
																HWO_BORNYEAR ,  
																HWO_DAYWAGE  ,  
																HWO_OTHER    ,  
																HWO_SECOND   ,  
																HWO_THESH    ,
                                hwo_sam_geocode   )
   VALUES ('01',2005,'c1_rec.game29',
                     'var_aa',
                     'c1_rec.g293',
                    'c1_rec.g294',
                     'c1_rec.g295',
                     'c1_rec.g296',
                     'c1_rec.g297',
                     '1'||'c1_rec.g298',
                     'c1_rec.g299',
                     'c1_rec.g2910',
                     'c1_rec.g2911',
                     'c1_rec.g2912',
                     'c1_rec.sam_geocode' ) ;
                     
  HOUSE_rec := HOUSE_rec + 1  ;                   

  END LOOP;

 
  COMMIT;
     dbms_output.put_line( 'RECORDS INSERTED INTO TMP_HOUSEWOK    : '||house_rec );
  exception
	   when others then
     ROLLBACK;
		  dbms_output.put_line( 'ERROR WHILE INSERTING RECORD: '||var_code||' Σχέση με κάτοχο = '||v_current_rel);
		  DBMS_OUTPUT.PUT_LINE(SQLERRM);
END;


PROCEDURE geo30(p_nomos varchar2) IS

/*************************************************************************************
***  LOADIND  TMP_regworkers  FROM  spr_diar_2005_geo30                            ***
*** 10/05/2006                                                                     ***
*** by gpapas                                                                      ***
***************************************************************************************/
	CURSOR C1 IS
		SELECT t.*, s.sam_geocode
			FROM spr_diar_2005_geo30 t, tmp_samples s
			where t.g30ame=s.sam_code;
--      and t.nomos=p_nomos; 


  I			    	NUMBER   ;
  reg_REC  	  NUMBER   ;
  VAR_AA      NUMBER   ;
  VAR_G30AME  VARCHAR2(8) ;
  
BEGIN

begin
  delete from  tmp_regworkers;
end;  
    
    I          :=  0 ;
    reg_rec    :=  0 ;
    VAR_G30AME := '00000000' ;
    VAR_AA     :=  0 ;


FOR c1_rec IN C1  LOOP
    
    I   :=  I + 1;
  	
     IF I = 1000 THEN
        	COMMIT;
	      	I := 0;
     END IF;
     
     IF VAR_G30AME = C1_REC.G30AME THEN
        VAR_AA := VAR_AA + 1  ;
     ELSE  VAR_G30AME := C1_REC.G30AME;
           VAR_AA     :=  1 ;
     END IF;
  	 
  	insert into tmp_regworkers ( RGW_RSC_CODE  ,                   
                                 RGW_RES_YEAR  ,                   
                                 RGW_SAM_CODE  ,                 
                                 RGW_AA        ,                 
                                 RGW_SEX       ,                 
                                 RGW_THESH     ,                 
                                 RGW_BORNYEAR  ,                 
                                 RGW_WAGES     ,
                                 RGW_SAM_GEOCODE)
   VALUES ('01',2005,'c1_rec.g30ame'   ,
                     'var_aa'          ,
                     'c1_rec.g302'     ,
                     'c1_rec.g303'     ,
                     '1'||'c1_rec.g304',
                     'c1_rec.g305'     ,
                     'c1_rec.Sam_Geocode'
                       ) ;
             
                     
  reg_rec := reg_rec + 1  ;                   

  END LOOP;
 
 
  COMMIT;
     dbms_output.put_line( 'RECORDS INSERTED INTO TMP_REGWORKERS   : '||REG_rec );
  exception
	   when others then
     ROLLBACK;
		  dbms_output.put_line( 'ERROR WHILE INSERTING RECORD: '||VAR_G30AME||'  = '||VAR_AA);
		  DBMS_OUTPUT.PUT_LINE(SQLERRM);
END;


PROCEDURE geo73(p_nomos varchar2) IS

/*************************************************************************************
***  LOADIND  TMP_otherland  FROM  spr_diar_2005_geo73                             ***
*** 10/05/2006                                                                     ***
*** by gpapas                                                                      ***
***************************************************************************************/
	CURSOR C1 IS
		SELECT *
			FROM spr_diar_2005_geo73 t, TMP_SAMPLES S
			where T.G73AME = S.SAM_CODE
         AND t.g73gcode IS NOT NULL;
--         and t.nomos=p_nomos;
 
  I			    	NUMBER     ;
  ola_REC  	NUMBER     ;
  v_current_ame        spr_diar_2005_geo73.g73ame%type;
  v_current_code      spr_diar_2005_geo73.g73code%type;
  
BEGIN

begin
  delete from  tmp_otherland;
end;  
    
    I          :=  0 ;
    ola_rec    :=  0 ;

FOR c1_rec IN C1  LOOP


    v_current_ame:=   C1_REC.G73AME;
    v_current_code:= C1_REC.G73CODE;

    
    I   :=  I + 1;
  	
     IF I = 1000 THEN
        	COMMIT;
	      	I := 0;
     END IF;
     
     insert into tmp_otherland (OLA_RSC_CODE,           
                                OLA_RES_YEAR,           
                                OLA_SAM_CODE,           
                                OLA_CUK_CODE,           
                                OLA_GEOCODE ,           
                                OLA_ACRES   ,
                                OLA_SAM_GEOCODE)
     values ( '01',2005,'c1_rec.g73ame','c1_rec.g73code','c1_rec.g73gcode','c1_rec.g73ektasi', 'c1_rec.sam_geocode') ;
                     
  ola_rec := ola_rec + 1  ;                   

  END LOOP;
 

  COMMIT;
     dbms_output.put_line( 'RECORDS INSERTED INTO TMP_otherland   : '||ola_rec );
  exception
	   when others then
     ROLLBACK;
		  dbms_output.put_line( 'ERROR WHILE INSERTING RECORD: '||v_current_ame||'  = '||v_current_code);
		  DBMS_OUTPUT.PUT_LINE(SQLERRM);
END;

END spr_diar_load_2005;
