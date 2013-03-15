create table semester
(
	mid integer,
	mdescr varchar(50),
	primary key (mid)
);

create table coursestd
(
	csid integer,
	csname varchar(50),
	cspts float,
	primary key (csid)
);

create table course
(
	cid integer,
	csid integer,
	mid integer,
	foreign key (mid) references semester (mid),
	foreign key (csid) references coursestd (csid),
	primary key (cid)
);

create table student
(
	sid integer,
	sname varchar(50),
	primary key (sid)
);

create table transcript
(
	cid integer,
	sid integer,
	tgrade float,
	foreign key (sid) references student (sid),
	foreign key (cid) references course (cid),
	primary key (cid, sid)
);
