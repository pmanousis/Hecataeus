SELECT * FROM url_alias;
SELECT * FROM watchdog WHERE type = '$type' AND hostname = '". getenv("REMOTE_ADDR") ."' AND timestamp < '$rate';
SELECT * FROM variable ;
SELECT data, created FROM cache WHERE cid = 0;
