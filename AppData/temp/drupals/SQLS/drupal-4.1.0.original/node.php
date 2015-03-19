SELECT nid FROM node WHERE title = '%s' AND status = 1;
SELECT n.*, u.name, u.uid FROM node n LEFT JOIN users u ON n.uid = u.uid WHERE n.title = '%s' AND n.status = 1 ORDER BY created DESC;
