SELECT u.*, s.*, r.name AS role FROM {users} u INNER JOIN {sessions} s ON u.uid = s.uid LEFT JOIN {role} r ON u.rid = r.rid WHERE s.sid = '%s' AND u.status < 3;
SELECT u.*, r.name AS role FROM {users} u INNER JOIN {role} r ON u.rid = r.rid WHERE u.uid = 0;
