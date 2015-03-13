-- ds410.sql:SELECT n.nid, c.timestamp, u.name AS name, u.uid AS uid FROM term_node r, node n LEFT JOIN comments c ON n.nid = c.nid LEFT JOIN users u ON c.uid = u.uid WHERE r.tid = 0 AND n.nid = r.nid AND n.type = 'forum' AND n.status = 1 AND c.status = 0 ORDER BY c.timestamp DESC; 
-- ds410.sql:SELECT n.nid, n.created AS timestamp, u.name AS name, u.uid AS uid FROM node n, term_node r LEFT JOIN users u ON n.uid = u.uid WHERE r.tid = 0 AND n.nid = r.nid AND n.type = 'forum' AND n.status = 1 ORDER BY timestamp DESC; 
-- ds410.sql:SELECT ov.nid, ov.title, ov.teaser, ov.created, ov.name, ov.uid FROM ourView ov WHERE ov.type = 'blog' AND ov.status = 1 ORDER BY ov.nid DESC; 
-- ds410.sql:SELECT ov.nid, ov.title, ov.teaser, ov.created, ov.name, ov.uid FROM ourView ov WHERE ov.type = 'blog' AND ov.uid = '$uid' AND ov.status = 1 ORDER BY ov.nid DESC; 
-- ds410.sql:SELECT n.nid, n.title, u.uid, u.name FROM node n LEFT JOIN forum f ON n.nid = f.nid LEFT JOIN users u ON n.uid = u.uid WHERE n.type = 'forum' ORDER BY n.nid DESC; 
-- ds410.sql:SELECT n.nid, n.title, u.uid, u.name, n.created, MAX(c.timestamp) AS sort FROM node n, forum f LEFT JOIN comments c ON c.nid = n.nid LEFT JOIN users u ON n.uid = u.uid WHERE n.type = 'forum' AND n.nid = f.nid AND f.shadow = 0 AND n.status = 1 GROUP BY n.nid ORDER BY sort DESC; 
-- ds410.sql:SELECT n.nid, title, users.name AS name, users.uid AS uid, n.created AS timestamp, n.created, MAX(c.timestamp) AS date_sort, COUNT(c.nid) AS num_comments, icon, n.comment AS comment_mode, shadow FROM node n, term_node r LEFT JOIN users ON n.uid = users.uid LEFT JOIN comments c ON c.nid = n.nid LEFT JOIN forum f ON n.nid = f.nid WHERE n.nid = r.nid AND r.tid = 0 AND n.status = 1 AND n.type = 'forum' GROUP BY n.nid; 
-- ds410.sql:SELECT ov.*, ov.name FROM ourView ov WHERE ov.uid = 0 ORDER BY ov.nid DESC; 
-- ds410.sql:SELECT ov.*, ov.name, ov.uid FROM ourView ov;
-- ds410.sql:SELECT ov.*, ov.name, ov.uid FROM ourView ov WHERE ov.moderate = 1;
-- ds410.sql:SELECT ov.*, ov.name, ov.uid FROM ourView ov WHERE ov.title = '%s' AND ov.status = 1 ORDER BY created DESC;
-- ds410.sql:SELECT statistics.nid, node.title, u.uid, u.name FROM statistics LEFT JOIN node ON statistics.nid = node.nid LEFT JOIN users u ON node.uid = u.uid WHERE node.status = 1; 
-- ds410.sql:SELECT ov.uid, ov.name, ov.created, ov.title, ov.nid FROM ourView ov WHERE ov.type = 'blog' AND ov.status = 1 ORDER BY ov.nid DESC; 

-- ds420.sql:SELECT COUNT(n.nid) FROM node n LEFT JOIN term_node r ON n.nid = r.nid LEFT JOIN users u ON n.uid = u.uid WHERE r.tid IN (0,1) AND n.status = '1';
-- ds420.sql:SELECT n.nid, c.timestamp, u.name AS name, u.uid AS uid FROM forum f LEFT JOIN node n ON n.nid = f.nid LEFT JOIN comments c ON n.nid = c.nid LEFT JOIN users u ON c.uid = u.uid WHERE f.tid = 0 AND n.nid = f.nid AND n.type = 'forum' AND n.status = 1 AND c.status = 0 ORDER BY c.timestamp DESC;
-- ds420.sql:SELECT n.nid, n.created AS timestamp, u.name AS name, u.uid AS uid FROM forum f LEFT JOIN node n ON n.nid = f.nid LEFT JOIN users u ON n.uid = u.uid WHERE f.tid = 0 AND n.nid = f.nid AND n.type = 'forum' AND n.status = 1 ORDER BY timestamp DESC;
-- ds420.sql:SELECT ov.nid, ov.title, ov.teaser, ov.created, ov.name, ov.uid FROM ourView ov WHERE ov.type = 'blog' AND ov.status = 1 ORDER BY ov.nid DESC;
-- ds420.sql:SELECT ov.nid, ov.title, ov.teaser, ov.created, ov.name, ov.uid FROM ourView ov WHERE ov.type = 'blog' AND ov.uid = 0 AND ov.status = 1 ORDER BY ov.nid DESC;
-- ds420.sql:SELECT n.nid, n.title, n.type, n.changed, n.uid, u.name, MAX(n.changed, c.timestamp) AS last_activity FROM node n LEFT JOIN comments c ON n.nid = c.nid LEFT JOIN users u ON n.uid = u.uid WHERE n.status = 1 GROUP BY n.nid, n.title, n.type, n.changed, n.uid, u.name ORDER BY last_activity DESC;
-- ds420.sql:SELECT n.nid, n.title, n.type, n.changed, n.uid, u.name, MAX(n.changed, c.timestamp) AS last_activity FROM node n LEFT JOIN comments c ON n.nid = c.nid LEFT JOIN users u ON n.uid = u.uid WHERE n.uid = 'check_query($id)' AND n.status = 1 GROUP BY n.nid, n.title, n.type, n.changed, n.uid, u.name ORDER BY last_activity DESC;
-- ds420.sql:SELECT (n.nid), n.title, n.type, n.created, n.changed, n.uid, n.static, n.created, u.name FROM node n LEFT JOIN term_node r ON n.nid = r.nid LEFT JOIN users u ON n.uid = u.uid WHERE r.tid IN (0,1) AND n.status = '1' ORDER BY static DESC, created DESC;
-- ds420.sql:SELECT n.nid, n.title, u.name AS name, u.uid AS uid, n.created AS timestamp, n.created, MAX(c.timestamp) AS date_sort, COUNT(c.nid) AS num_comments, f.icon, n.comment AS comment_mode, f.tid FROM node n LEFT JOIN term_node r ON n.nid = r.nid LEFT JOIN users u ON n.uid = u.uid LEFT JOIN comments c ON n.nid = c.nid LEFT JOIN forum f ON n.nid = f.nid WHERE n.nid = r.nid AND ((r.tid = 'check_query($tid)' AND f.shadow = 1) OR f.tid = 'check_query($tid)') AND n.status = 1 AND n.type = 'forum' GROUP BY n.nid, n.title, u.name, u.uid, n.created, n.comment, f.tid, f.icon; 
-- ds420.sql:SELECT n.nid, n.title, u.uid, u.name FROM node n LEFT JOIN forum f ON n.nid = f.nid LEFT JOIN users u ON n.uid = u.uid WHERE n.type = 'forum' ORDER BY n.nid DESC;
-- ds420.sql:SELECT n.nid, n.title, u.uid, u.name, n.created, MAX(c.timestamp) AS sort FROM node n LEFT JOIN forum f ON n.nid = f.nid LEFT JOIN comments c ON n.nid = c.nid LEFT JOIN users u ON n.uid = u.uid WHERE n.type = 'forum' AND n.nid = f.nid AND n.status = 1 GROUP BY n.nid, n.title, n.created, u.uid, u.name ORDER BY sort DESC;
-- ds420.sql:SELECT ov.*, ov.name FROM ourView ov WHERE ov.uid = 0 ORDER BY ov.nid DESC;
-- ds420.sql:SELECT ov.*, ov.name, ov.uid FROM ourView ov;
-- ds420.sql:SELECT ov.*, ov.name, ov.uid FROM ourView ov WHERE ov.moderate = 1;
-- ds420.sql:SELECT ov.*, ov.name, ov.uid FROM ourView ov WHERE ov.title = '%". check_query($title). "%' AND ov.status = 1 ORDER BY created DESC;
-- ds420.sql:SELECT ov.*, ov.name, ov.uid FROM ourView ov WHERE ov.title = '%s' AND ov.status = 1 ORDER BY created DESC;
-- ds420.sql:SELECT s.nid, n.title, u.uid, u.name FROM statistics s LEFT JOIN node n ON s.nid = n.nid LEFT JOIN users u ON n.uid = u.uid WHERE n.status = 1 ; 


ds431.sql:SELECT COUNT(n.nid) FROM node n INNER JOIN term_node r ON n.nid = r.nid INNER JOIN users u ON n.uid = u.uid WHERE r.tid IN (0,1) AND n.status = '1';
ds431.sql:SELECT n.nid, c.timestamp, u.name AS name, u.uid AS uid FROM forum f INNER JOIN node n ON n.nid = f.nid INNER JOIN comments c ON n.nid = c.nid INNER JOIN users u ON c.uid = u.uid WHERE f.tid = 0 AND n.nid = f.nid AND n.type = 'forum' AND n.status = 1 AND c.status = 0 ORDER BY c.timestamp DESC;
ds431.sql:SELECT n.nid, n.created AS timestamp, u.name AS name, u.uid AS uid FROM forum f INNER JOIN node n ON n.nid = f.nid INNER JOIN users u ON n.uid = u.uid WHERE f.tid = 0 AND n.nid = f.nid AND n.type = 'forum' AND n.status = 1 ORDER BY timestamp DESC;
ds431.sql:SELECT ov.nid, ov.title, ov.teaser, ov.created, ov.name, ov.uid FROM ourViewN ov WHERE ov.type = 'blog' AND ov.status = 1 ORDER BY ov.nid DESC;
ds431.sql:SELECT ov.nid, ov.title, ov.teaser, ov.created, ov.name, ov.uid FROM ourViewN ov WHERE ov.type = 'blog' AND ov.uid = 0 AND ov.status = 1 ORDER BY ov.nid DESC;
ds431.sql:SELECT n.nid, n.title, n.type, n.changed, n.uid, u.name, MAX(n.changed, c.timestamp) AS last_activity FROM node n LEFT JOIN comments c ON n.nid = c.nid INNER JOIN users u ON n.uid = u.uid WHERE n.status = 1 GROUP BY n.nid, n.title, n.type, n.changed, n.uid, u.name;
ds431.sql:SELECT n.nid, n.title, n.type, n.changed, n.uid, u.name, MAX(n.changed, c.timestamp) AS last_activity FROM node n LEFT JOIN comments c ON n.nid = c.nid INNER JOIN users u ON n.uid = u.uid WHERE n.uid = 'check_query($id)' AND n.status = 1 GROUP BY n.nid, n.title, n.type, n.changed, n.uid, u.name;
ds431.sql:SELECT (n.nid), n.title, n.type, n.created, n.changed, n.uid, n.static, n.created, u.name FROM node n INNER JOIN term_node r ON n.nid = r.nid INNER JOIN users u ON n.uid = u.uid WHERE r.tid IN (0,1) AND n.status = '1' ORDER BY static DESC, created DESC;
ds431.sql:SELECT n.nid, n.title, u.name AS name, u.uid AS uid, n.created AS timestamp, n.created, MAX(c.timestamp) AS date_sort, COUNT(c.nid) AS num_comments, n.comment AS comment_mode, f.tid FROM node n INNER JOIN term_node r ON n.nid = r.nid INNER JOIN users u ON n.uid = u.uid LEFT JOIN comments c ON n.nid = c.nid INNER JOIN forum f ON n.nid = f.nid WHERE n.nid = r.nid AND ((r.tid = 0 AND f.shadow = 1) OR f.tid = 0) AND n.status = 1 AND n.type = 'forum' GROUP BY n.nid, n.title, u.name, u.uid, n.created, n.comment, f.tid;
ds431.sql:SELECT ov.*, ov.name FROM ourViewN ov WHERE ov.type = 'blog' AND ov.uid = 0 ORDER BY ov.nid DESC;
ds431.sql:SELECT ov.*, ov.name, ov.uid FROM ourViewN ov;
ds431.sql:SELECT ov.*, ov.name, ov.uid FROM ourViewN ov WHERE ov.moderate = 1;
ds431.sql:SELECT ov.*, ov.name, ov.uid FROM ourViewN ov WHERE ov.title = '%". check_query($title) ."%' AND ov.status = 1 ORDER BY ov.created DESC;
ds431.sql:SELECT ov.*, ov.name, ov.uid FROM ourViewN ov WHERE ov.title = '%s' AND ov.status = 1 ORDER BY ov.created DESC;
ds431.sql:SELECT s.nid, n.title, u.uid, u.name FROM node_counter s INNER JOIN node n ON s.nid = n.nid INNER JOIN users u ON n.uid = u.uid WHERE n.status = 1; 


ds443.sql:SELECT COUNT(n.nid) FROM node n INNER JOIN term_node r ON n.nid = r.nid INNER JOIN users u ON n.uid = u.uid WHERE r.tid IN (0,1) AND n.status = '1';
ds443.sql:SELECT n.nid, c.timestamp, u.name AS name, u.uid AS uid FROM forum f INNER JOIN node n ON n.nid = f.nid INNER JOIN comments c ON n.nid = c.nid INNER JOIN users u ON c.uid = u.uid WHERE f.tid = 0 AND n.nid = f.nid AND n.type = 'forum' AND n.status = 1 AND c.status = 0 ORDER BY c.timestamp DESC;
ds443.sql:SELECT n.nid, n.created AS timestamp, u.name AS name, u.uid AS uid FROM forum f INNER JOIN node n ON n.nid = f.nid INNER JOIN users u ON n.uid = u.uid WHERE f.tid = 0 AND n.nid = f.nid AND n.type = 'forum' AND n.status = 1 ORDER BY timestamp DESC;
ds443.sql:SELECT n.nid, n.title, n.body, n.created, u.name FROM node n, users u WHERE n.uid=u.uid AND n.type = 'blog' AND n.uid = 0 ORDER BY n.created DESC;
ds443.sql:SELECT ov.nid, ov.title, ov.teaser, ov.created, ov.name, ov.uid FROM ourViewN ov WHERE ov.type = 'blog' AND ov.status = 1 ORDER BY ov.nid DESC;
ds443.sql:SELECT ov.nid, ov.title, ov.teaser, ov.created, ov.name, ov.uid FROM ourViewN ov WHERE ov.type = 'blog' AND ov.uid = 0 AND ov.status = 1 ORDER BY ov.nid DESC;
ds443.sql:SELECT n.nid, n.title, n.type, n.changed, n.uid, u.name, MAX(n.changed, c.timestamp) AS last_activity FROM node n LEFT JOIN comments c ON n.nid = c.nid INNER JOIN users u ON n.uid = u.uid WHERE n.status = 1 GROUP BY n.nid, n.title, n.type, n.changed, n.uid, u.name;
ds443.sql:SELECT n.nid, n.title, n.type, n.changed, n.uid, u.name, MAX(n.changed, c.timestamp) AS last_activity FROM node n LEFT JOIN comments c ON n.nid = c.nid INNER JOIN users u ON n.uid = u.uid WHERE n.uid = 0 AND n.status = 1 GROUP BY n.nid, n.title, n.type, n.changed, n.uid, u.name;
ds443.sql:SELECT n.nid, n.title, n.type, n.created, n.changed, n.uid, n.static, n.created, u.name FROM node n INNER JOIN term_node r ON n.nid = r.nid INNER JOIN users u ON n.uid = u.uid WHERE r.tid IN (0,1) AND n.status = '1' ORDER BY static DESC, created DESC;
ds443.sql:SELECT n.nid, n.title, u.name AS name, u.uid AS uid, n.created AS timestamp, n.created, MAX(c.timestamp) AS date_sort, COUNT(c.nid) AS num_comments, n.comment AS comment_mode, f.tid FROM node n INNER JOIN term_node r ON n.nid = r.nid INNER JOIN users u ON n.uid = u.uid LEFT JOIN comments c ON n.nid = c.nid INNER JOIN forum f ON n.nid = f.nid WHERE n.nid = r.nid AND ((r.tid = 0 AND f.shadow = 1) OR f.tid = 0) AND n.status = 1 AND n.type = 'forum' GROUP BY n.nid, n.title, u.name, u.uid, n.created, n.comment, f.tid;
ds443.sql:SELECT ov.*, ov.name, ov.uid FROM ourViewN ov;
ds443.sql:SELECT ov.*, ov.name, ov.uid FROM ourViewN ov WHERE ov.moderate = 1;
ds443.sql:SELECT ov.*, ov.name, ov.uid FROM ourViewN ov WHERE ov.title = '%%%s%%' AND ov.status = 1 ORDER BY ov.created DESC;
ds443.sql:SELECT ov.*, ov.name, ov.uid FROM ourViewN ov WHERE ov.title = '%s' AND ov.status = 1 ORDER BY ov.created DESC;
ds443.sql:SELECT s.nid, n.title, u.uid, u.name FROM node_counter s INNER JOIN node n ON s.nid = n.nid INNER JOIN users u ON n.uid = u.uid AND n.status = 1 


ds458.sql:SELECT ov.*, ov.name, ov.uid FROM ourViewN ov;
ds458.sql:SELECT ov.*, ov.name, ov.uid FROM ourViewN ov WHERE ov.moderate = 1;
ds458.sql:SELECT s.nid, n.title, u.uid, u.name FROM node_counter s INNER JOIN node n ON s.nid = n.nid INNER JOIN users u ON n.uid = u.uid AND n.status = 1 ; 
ds458.sql:SELECT ov.uid FROM ourView ov WHERE ov.created < ov.created;


ds4611.sql:SELECT a.aid, a.timestamp, a.url, a.uid, u.name FROM accesslog a LEFT JOIN users u ON a.uid = u.uid WHERE a.path = 'node/%d%%';
ds4611.sql:SELECT ov.nid, ov.title, ov.teaser, ov.created, ov.name, ov.uid FROM ourViewN ov WHERE ov.type = 'blog' AND ov.status = 1 ORDER BY ov.created DESC;
ds4611.sql:SELECT ov.nid, ov.title, ov.teaser, ov.created, ov.name, ov.uid FROM ourViewN ov WHERE ov.type = 'blog' AND ov.uid = 0 AND ov.status = 1 ORDER BY ov.created DESC;
ds4611.sql:SELECT n.nid, n.title, n.type, n.changed, n.uid, u.name, l.last_comment_timestamp AS last_post, l.comment_count FROM node n INNER JOIN node_comment_statistics l ON n.nid = l.nid INNER JOIN users u ON n.uid = u.uid LEFT JOIN comments c ON n.nid = c.nid AND (c.status = 0 OR c.status IS NULL) WHERE n.status = 1 AND (n.uid = 0 OR c.uid = 0) ORDER BY last_post DESC;
ds4611.sql:SELECT ov.nid, ov.title, ov.type, ov.changed, ov.uid, ov.name, l.last_comment_timestamp AS last_post, l.comment_count FROM ourViewN ov INNER JOIN node_comment_statistics l ON ov.nid = l.nid WHERE ov.status = 1 ORDER BY last_post DESC;
ds4611.sql:SELECT n.nid, n.title, u.uid, u.name FROM node_counter s INNER JOIN node n ON s.nid = n.nid INNER JOIN users u ON n.uid = u.uid WHERE n.status = 1; 
ds4611.sql:SELECT ov.uid FROM ourView ov WHERE ov.created < ov.created;


ds4711.sql:SELECT a.aid, a.timestamp, a.url, a.uid, u.name FROM accesslog a LEFT JOIN users u ON a.uid = u.uid WHERE a.path = 'node/0%%';
ds4711.sql:SELECT n.nid, n.title, n.created, u.name FROM node n, users u WHERE n.uid = u.uid AND n.type = '%s' AND n.uid = 0 ORDER BY n.created DESC;
ds4711.sql:SELECT n.nid, n.title, n.type, n.changed, n.uid, u.name, n.changed, l.last_comment_timestamp AS last_updated, l.comment_count FROM node n INNER JOIN node_comment_statistics l ON n.nid = l.nid INNER JOIN users u ON n.uid = u.uid LEFT JOIN comments c ON n.nid = c.nid AND (c.status = 0 OR c.status IS NULL) WHERE n.status = 1 AND (n.uid = 0 OR c.uid = 0) ORDER BY last_updated DESC;
ds4711.sql:SELECT n.nid, n.title, n.type, n.changed, n.uid, u.name, n.changed, l.last_comment_timestamp AS last_updated, l.comment_count FROM ourView ov INNER JOIN node_comment_statistics l ON n.nid = l.nid WHERE n.status = 1 ORDER BY last_updated DESC;
ds4711.sql:SELECT n.nid, n.title, r.body, r.format, n.comment, n.created, u.name FROM node n, node_revisions r, users u WHERE n.uid = u.uid AND n.vid = r.vid AND n.type = '%s' AND n.uid = 0 ORDER BY n.created DESC;
ds4711.sql:SELECT n.nid, n.title, r.teaser, n.created, u.name, u.uid FROM node n INNER JOIN node_revisions r ON n.vid = r.vid INNER JOIN users u ON n.uid = u.uid WHERE n.type = 'blog' AND n.status = 1 ORDER BY n.created DESC;
ds4711.sql:SELECT n.nid, n.title, r.teaser, n.created, u.name, u.uid FROM node n INNER JOIN node_revisions r ON n.vid = r.vid INNER JOIN users u ON n.uid = u.uid WHERE n.type = 'blog' AND u.uid = 0 AND n.status = 1 ORDER BY n.created DESC;
ds4711.sql:SELECT n.nid, n.title, u.uid, u.name FROM node n INNER JOIN node_counter s ON n.nid = s.nid INNER JOIN users u ON n.uid = u.uid WHERE n.status = 1; 
ds4711.sql:SELECT n.nid, n.vid, n.type, n.status, n.created, n.changed, n.comment, n.promote, n.moderate, n.sticky, r.timestamp AS revision_timestamp, r.title, r.body, r.teaser, r.log, r.format, u.uid, u.name, u.picture, u.data FROM node n INNER JOIN users u ON u.uid = n.uid INNER JOIN node_revisions r ON r.vid = n.vid ;
ds4711.sql:SELECT n.nid, r.vid, n.type, n.status, n.created, n.changed, n.comment, n.promote, n.moderate, n.sticky, r.timestamp AS revision_timestamp, r.title, r.body, r.teaser, r.log, r.format, u.uid, u.name, u.picture, u.data FROM node n INNER JOIN users u ON u.uid = n.uid INNER JOIN node_revisions r ON r.nid = n.nid AND r.vid = 0 ;
ds4711.sql:SELECT r.vid, r.title, r.log, r.uid, n.vid AS current_vid, r.timestamp, u.name FROM node_revisions r LEFT JOIN node n ON n.vid = r.vid INNER JOIN users u ON u.uid = r.uid WHERE r.nid = 0 ORDER BY r.timestamp DESC;
