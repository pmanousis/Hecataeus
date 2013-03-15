 
--Q3: 
SELECT *
FROM EMP
WHERE (E_ID, E_NAME) = 
(SELECT P_ID, P_NAME 
FROM PRJS where P_NAME= 'parameter1');


