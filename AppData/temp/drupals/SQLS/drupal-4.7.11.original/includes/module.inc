SELECT name, filename, throttle, bootstrap FROM system WHERE type = 'module' AND status = 1 AND bootstrap = 1 ORDER BY weight ASC, filename ASC;
SELECT name, filename, throttle, bootstrap FROM system WHERE type = 'module' AND status = 1 ORDER BY weight ASC, filename ASC;
