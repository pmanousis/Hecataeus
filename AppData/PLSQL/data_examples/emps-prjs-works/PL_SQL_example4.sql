CREATE OR REPLACE PACKAGE BODY emp_mgmt AS 
tot_emps integer;
param varchar(200);
das varchar(200);
again varchar(200);
again2 varchar(200);
last varchar(200);
tot_depts integer;

last:='EMP';

again:='FROM ';

das:='SELECT * '||again||'PRJS;'|| 'SELECT * FROM ';

param:='EMP WHERE E_ID=2300;'||das || 'WORKS;';

EXECUTE IMMEDIATE 'SELECT * FROM EMP;'|| 'SELECT * FROM
 PRJS;SELECT * FROM WORKS;';

EXECUTE IMMEDIATE 'DELETE FROM ' ||param||'SELECT * FROM EMP;';

PROCEDURE insertTuple(a INTEGER)
AS
BEGIN
	INSERT INTO EMP VALUES(12,'value2','value3',17);

	INSERT INTO EMP(E_ID) VALUES(12);

	INSERT INTO EMP(E_ID) SELECT P_ID FROM PRJS;

--	INSERT INTO EMP ((SELECT * FROM EMP));

END insertTuple;


again2:='FROM '||last||';';
/*FGFDGDFGF*/again:=again||'EMP';


PROCEDURE deleteTuple(a INTEGER)
AS
BEGIN
	DELETE FROM EMP 
	WHERE E_SAL=34;

	DELETE FROM WORKS;

	CURSOR my_cur IS
		SELECT * FROM EMP
		FOR UPDATE;

END deleteTuple;


DBMS_SQL.EXECUTE('SELECT * '|| again2 ||'SELECT E_ID FROM EMP;'|| 'SELECT P_NAME FROM PRJS;');
/*hfghfgh*/DBMS_SQL.EXECUTE('SELECT * '|| again||';' ||'SELECT E_ID FROM EMP;'|| 'SELECT P_NAME FROM PRJS;');



FUNCTION updateTuple(a INTEGER) 
RETURN BOOLEAN IS
BEGIN

	ID	NUMBER(7);
  	SAL	NUMBER(7);

	SELECT E_ID,E_SAL
	INTO NMAX, NMIN
	FROM EMP
	WHERE E_SAL>1200;

	UPDATE EMP SET E_SAL=1500, E_ID=:a 
	WHERE E_SAL not in (select W_DUR from works);

	UPDATE EMP SET E_SAL=1500;

	RETURN TRUE;

END updateTuple;

END emp_mgmt; 
