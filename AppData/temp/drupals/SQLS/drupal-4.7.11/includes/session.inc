SELECT sid FROM {sessions} WHERE sid = '%s';
SELECT u.* FROM {users} u WHERE u.uid = 0;
SELECT u.*, s.* FROM {users} u INNER JOIN {sessions} s ON u.uid = s.uid WHERE s.sid = '%s';
SELECT r.rid, r.name FROM {role} r INNER JOIN {users_roles} ur ON ur.rid = r.rid WHERE ur.uid = %d;
SELECT sid FROM {sessions} WHERE sid = '%s';
