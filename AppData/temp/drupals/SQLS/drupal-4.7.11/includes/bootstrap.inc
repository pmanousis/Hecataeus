SELECT filename FROM {system} WHERE name = '%s' AND type = '%s';
SELECT * FROM {variable};
SELECT data, created, headers, expire FROM {cache} WHERE cid = '%s';
SELECT * FROM {access} WHERE status = 1 AND type = '%s' AND LOWER('%s') LIKE LOWER(mask);
SELECT * FROM {access} WHERE status = 0 AND type = '%s' AND LOWER('%s') LIKE LOWER(mask);
