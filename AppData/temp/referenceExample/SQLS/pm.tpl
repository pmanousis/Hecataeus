CREATE VIEW v_course AS
SELECT semester.mid, semester.mdescr, coursestd.csid, coursestd.csname, course.cid
FROM semester, coursestd, course
WHERE semester.mid=course.mid AND coursestd.csid=course.csid;

CREATE VIEW v_tr AS
SELECT v_course.mid, v_course.mdescr, v_course.csid, v_course.csname, v_course.cid, transcript.sid, transcript.tgrade
FROM v_course, transcript
WHERE v_course.cid=transcript.cid;

SELECT v1.sid, v1.csname AS csname1, v1.tgrade AS tgrade1, v2.csname AS csname2, v2.tgrade AS tgrade2
FROM v_tr AS v1, v_tr AS v2
WHERE v1.mid=v2.mid AND v1.sid=v2.sid AND v1.csname='db_i' AND v2.csname='db_ii';

SELECT student.sid, student.sname, avg(v_tr.tgrade) AS gpa
FROM v_tr, student
WHERE v_tr.sid=student.sid AND v_tr.tgrade > 4 / 10
GROUP BY student.sid, student.sname;

CREATE VIEW v_course2 AS
SELECT semester.mid, semester.mdescr, coursestd.csid, coursestd.csname, course.cid
FROM semester, coursestd, course
WHERE semester.mid=course.mid AND coursestd.csid=course.csid;
SELECT v_course2.mid, v_course2.mdescr, v_course2.csid, v_course2.csname, v_course2.cid, transcript.sid, transcript.tgrade
FROM v_course2, transcript
WHERE v_course2.cid=transcript.cid;
