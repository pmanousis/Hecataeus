SELECT status FROM  system  WHERE type = 0 AND name = 0;
SELECT * FROM  system  WHERE type = 'theme' OR (type = 'module' AND status = 1) ORDER BY weight ASC, name ASC;
SELECT name, filename FROM  system  WHERE status = 1 AND bootstrap = 1 AND type = 'module' ORDER BY weight ASC, name ASC;
