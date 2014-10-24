SELECT COUNT(*) FROM  mynode  n ,  search_dataset  d WHERE d.type = 'mynode' AND d.sid = n.nid AND n.status = 1 AND d.sid IS NULL OR d.reindex <> 0;
SELECT COUNT(*) FROM  mynode  WHERE status = 1
