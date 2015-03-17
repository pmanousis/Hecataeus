SELECT nid FROM node WHERE title = '%s' AND status = 1;
SELECT v.* FROM ourView v WHERE v.title = '%s' AND v.status = 1 ORDER BY created DESC;
