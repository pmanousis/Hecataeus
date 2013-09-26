SELECT * FROM  authmap  WHERE module='openid' AND uid=0 ;
SELECT authname FROM  authmap  WHERE authname = 0 ;
SELECT authname FROM  authmap  WHERE uid = 0 AND aid = 0 AND module = 'openid';
