SELECT name FROM  system  WHERE type = 'module' AND schema_version <> 0 ORDER BY weight ASC, name ASC;
SELECT name, type, status FROM  system  WHERE status = 1 AND type IN ('module','theme');
SELECT MAX(bid) FROM  batch ;
SELECT COUNT(*) FROM  users  WHERE data = 0;
SELECT MAX(uid) FROM  users ;
