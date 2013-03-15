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
