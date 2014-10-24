SELECT u.*, s.* FROM  users  u ,  sessions  s WHERE u.uid = s.uid AND s.ssid = 0;
SELECT u.*, s.* FROM  users  u ,  sessions  s WHERE u.uid = s.uid AND s.sid = 0 AND s.uid = 0;
SELECT u.*, s.* FROM  users  u ,  sessions  s WHERE u.uid = s.uid AND s.sid = 0;
SELECT r.rid, r.name FROM  role  r , users_roles  ur WHERE ur.rid = r.rid AND ur.uid = 0;
