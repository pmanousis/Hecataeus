SELECT * FROM watchdog WHERE type = '$type' AND hostname = '' AND time() - timestamp < $rate;
SELECT * FROM variable;
SELECT data FROM cache WHERE cid = '%s';
SELECT cid FROM cache WHERE cid = '%s';
