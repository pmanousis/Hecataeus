CREATE TABLE history (
  uid integer NOT NULL ,
  nid integer NOT NULL ,
  timestamp integer NOT NULL ,
  PRIMARY KEY  (uid,nid)
);
CREATE TABLE permission (
  rid integer NOT NULL ,
  perm varchar(1024) ,
  tid integer NOT NULL
);
CREATE TABLE bundle (
  bid INTEGER,
  title varchar(255) NOT NULL ,
  attrs varchar(255) NOT NULL ,
  PRIMARY KEY  (bid),
  UNIQUE (title)
);
CREATE TABLE role (
  rid INTEGER,
  name varchar(32) NOT NULL ,
  PRIMARY KEY  (rid),
  UNIQUE (name)
);
CREATE TABLE moderation_filters (
  fid INTEGER,
  filter varchar(255) NOT NULL ,
  minimum smallint NOT NULL ,
  PRIMARY KEY  (fid)
);
CREATE TABLE comments (
  cid INTEGER,
  pid integer NOT NULL ,
  nid integer NOT NULL ,
  uid integer NOT NULL ,
  subject varchar(64) NOT NULL ,
  comment varchar(1024) NOT NULL ,
  hostname varchar(128) NOT NULL ,
  timestamp integer NOT NULL ,
  link varchar(16) NOT NULL ,
  score integer NOT NULL ,
  status smallint  NOT NULL ,
  users varchar(1024) ,
  PRIMARY KEY  (cid)
);
CREATE TABLE feed (
  fid INTEGER,
  title varchar(255) NOT NULL ,
  url varchar(255) NOT NULL ,
  refresh integer NOT NULL ,
  timestamp integer NOT NULL ,
  attrs varchar(255) NOT NULL ,
  link varchar(255) NOT NULL ,
  description varchar(1024) NOT NULL ,
  PRIMARY KEY  (fid),
  UNIQUE (title),
  UNIQUE (url)
);
CREATE TABLE item (
  iid INTEGER,
  fid integer NOT NULL ,
  title varchar(255) NOT NULL ,
  link varchar(255) NOT NULL ,
  author varchar(255) NOT NULL ,
  description varchar(1024) NOT NULL ,
  timestamp integer NOT NULL ,
  attrs varchar(255) NOT NULL ,
  PRIMARY KEY  (iid)
);
CREATE TABLE moderation_roles (
  rid integer NOT NULL ,
  mid integer NOT NULL ,
  value smallint NOT NULL
);
CREATE TABLE moderation_votes (
  mid INTEGER,
  vote varchar(255) ,
  weight smallint NOT NULL ,
  PRIMARY KEY  (mid)
);
CREATE TABLE node (
  nid INTEGER,
  type varchar(16) NOT NULL ,
  title varchar(128) NOT NULL ,
  score integer NOT NULL ,
  votes integer NOT NULL ,
  uid integer NOT NULL ,
  status integer NOT NULL ,
  created integer NOT NULL ,
  comment integer NOT NULL ,
  promote integer NOT NULL ,
  moderate integer NOT NULL ,
  users varchar(1024) NOT NULL ,
  attrs varchar(255) NOT NULL ,
  teaser varchar(1024) NOT NULL ,
  body varchar(1024) NOT NULL ,
  changed integer NOT NULL ,
  revisions varchar(1024) NOT NULL ,
  static integer NOT NULL ,
  PRIMARY KEY  (nid)
);
CREATE TABLE users (
  uid INTEGER,
  name varchar(60) NOT NULL ,
  pass varchar(32) NOT NULL ,
  mail varchar(64) ,
  homepage varchar(128) NOT NULL ,
  mode smallint NOT NULL ,
  sort smallint ,
  threshold smallint ,
  theme varchar(255) NOT NULL ,
  signature varchar(255) NOT NULL ,
  timestamp integer NOT NULL ,
  hostname varchar(128) NOT NULL ,
  status smallint NOT NULL ,
  timezone varchar(8) ,
  rating decimal(8,2) ,
  language char(2) NOT NULL ,
  sid varchar(32) NOT NULL ,
  init varchar(64) ,
  session varchar(1024) ,
  data varchar(1024) ,
  rid integer NOT NULL ,
  PRIMARY KEY  (uid),
  UNIQUE (name)
);
