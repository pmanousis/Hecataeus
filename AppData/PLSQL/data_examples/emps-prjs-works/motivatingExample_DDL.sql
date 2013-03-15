
CREATE TABLE EMP(	E_ID INTEGER PRIMARY KEY, 
E_NAME	VARCHAR(25) NOT NULL,
				E_TITLE	VARCHAR(10),
				E_SAL	INTEGER NOT NULL);
			
---------------------------------------------------------------------------------------------------------------
CREATE TABLE PRJS(	P_ID	INTEGER PRIMARY KEY,
				P_NAME	VARCHAR(25) NOT NULL,
				p_budget INTEGER NOT NULL);

---------------------------------------------------------------------------------------------------------------
CREATE TABLE WORKS(	E_ID	INTEGER,
				P_ID	INTEGER,
				W_RESP	VARCHAR(10),
				W_DUR	INTEGER,
				PRIMARY KEY (E_ID,P_ID),
				FOREIGN KEY (E_ID) REFERENCES EMP(E_ID),
				FOREIGN KEY (P_ID) REFERENCES PRJS(P_ID));
