SELECT sid FROM sessions WHERE sid = 0;
SELECT u.* FROM users u WHERE u.uid = 0;
SELECT u.*, s.* FROM users u INNER JOIN sessions s ON u.uid = s.uid WHERE s.sid = 0;
SELECT r.rid, r.name FROM role r INNER JOIN users_roles ur ON ur.rid = r.rid WHERE ur.uid = 0;
SELECT sid FROM sessions WHERE sid = 0;
