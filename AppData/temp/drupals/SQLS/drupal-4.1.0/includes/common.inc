SELECT * FROM watchdog WHERE type = '$type' AND hostname = '' AND timestamp < '$rate';
SELECT * FROM variable;
SELECT data FROM cache WHERE cid = 0;
SELECT cid FROM cache WHERE cid = 0;
