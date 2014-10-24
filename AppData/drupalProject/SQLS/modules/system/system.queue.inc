SELECT data, item_id FROM  queue  q WHERE expire = 0 AND name = 0 ORDER BY created ASC;
SELECT COUNT(item_id) FROM  queue  WHERE name = 0;
