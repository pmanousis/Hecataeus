CREATE TABLE access (
  aid integer,
  mask varchar(255) NOT NULL ,
  type varchar(255) NOT NULL ,
  status smallint NOT NULL ,
  PRIMARY KEY (aid),
  UNIQUE (mask)
);
CREATE TABLE accesslog (
  nid integer ,
  url varchar(255) ,
  hostname varchar(128) ,
  uid integer ,
  timestamp integer NOT NULL
);
CREATE INDEX accesslog_timestamp_idx ON accesslog (timestamp);
CREATE TABLE authmap (
  aid integer,
  uid integer NOT NULL ,
  authname varchar(128) NOT NULL ,
  module varchar(128) NOT NULL ,
  PRIMARY KEY (aid),
  UNIQUE (authname)
);
CREATE TABLE blocks (
  module varchar(64) NOT NULL ,
  delta varchar(32) NOT NULL ,
  status smallint NOT NULL ,
  weight smallint NOT NULL ,
  region smallint NOT NULL ,
  path varchar(255) NOT NULL ,
  custom smallint NOT NULL
);
CREATE TABLE book (
  nid integer NOT NULL ,
  parent integer NOT NULL ,
  weight smallint NOT NULL ,
  format smallint ,
  log varchar(1024) ,
  PRIMARY KEY (nid)
);
CREATE INDEX book_nid_idx ON book(nid);
CREATE INDEX book_parent ON book(parent);
CREATE TABLE boxes (
  bid integer,
  title varchar(64) NOT NULL ,
  body varchar(1024) ,
  info varchar(128) NOT NULL ,
  type smallint NOT NULL ,
  PRIMARY KEY  (bid),
  UNIQUE (info),
  UNIQUE (title)
);
CREATE TABLE bundle (
  bid integer,
  title varchar(255) NOT NULL ,
  attributes varchar(255) NOT NULL ,
  PRIMARY KEY  (bid),
  UNIQUE (title)
);
CREATE TABLE cache (
  cid varchar(255) NOT NULL ,
  data varchar(1024) ,
  expire integer NOT NULL ,
  created integer NOT NULL ,
  PRIMARY KEY  (cid)
);
CREATE TABLE comments (
  cid integer,
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
  thread varchar(255) ,
  users varchar(1024) ,
  PRIMARY KEY  (cid)
);
CREATE INDEX comments_nid_idx ON comments(nid);
CREATE TABLE directory (
  link varchar(255) NOT NULL ,
  name varchar(128) NOT NULL ,
  mail varchar(128) NOT NULL ,
  slogan varchar(1024) NOT NULL ,
  mission varchar(1024) NOT NULL ,
  timestamp integer NOT NULL ,
  PRIMARY KEY  (link)
);
CREATE TABLE feed (
  fid integer,
  title varchar(255) NOT NULL ,
  url varchar(255) NOT NULL ,
  refresh integer NOT NULL ,
  timestamp integer NOT NULL ,
  attributes varchar(255) NOT NULL ,
  link varchar(255) NOT NULL ,
  description varchar(1024) NOT NULL ,
  PRIMARY KEY  (fid),
  UNIQUE (title),
  UNIQUE (url)
);
CREATE TABLE forum (
  nid integer NOT NULL ,
  tid integer NOT NULL ,
  icon varchar(255) NOT NULL ,
  shadow integer NOT NULL ,
  PRIMARY KEY  (nid)
);
CREATE INDEX forum_tid_idx ON forum(tid);
CREATE TABLE history (
  uid integer NOT NULL ,
  nid integer NOT NULL ,
  timestamp integer NOT NULL ,
  PRIMARY KEY  (uid,nid)
);
CREATE TABLE item (
  iid integer,
  fid integer NOT NULL ,
  title varchar(255) NOT NULL ,
  link varchar(255) NOT NULL ,
  author varchar(255) NOT NULL ,
  description varchar(1024) NOT NULL ,
  timestamp integer NOT NULL ,
  attributes varchar(255) NOT NULL ,
  PRIMARY KEY  (iid)
);
CREATE TABLE locales (
  lid integer,
  location varchar(128) NOT NULL ,
  string varchar(1024) NOT NULL ,
  da varchar(1024) NOT NULL ,
  fi varchar(1024) NOT NULL ,
  fr varchar(1024) NOT NULL ,
  en varchar(1024) NOT NULL ,
  es varchar(1024) NOT NULL ,
  nl varchar(1024) NOT NULL ,
  no varchar(1024) NOT NULL ,
  sw varchar(1024) NOT NULL ,
  PRIMARY KEY  (lid)
);
CREATE TABLE moderation_filters (
  fid integer,
  filter varchar(255) NOT NULL ,
  minimum smallint NOT NULL ,
  PRIMARY KEY  (fid)
);
CREATE TABLE moderation_roles (
  rid integer NOT NULL ,
  mid integer NOT NULL ,
  value smallint NOT NULL
);
CREATE INDEX moderation_roles_rid_idx ON moderation_roles(rid);
CREATE INDEX moderation_roles_mid_idx ON moderation_roles(mid);
CREATE TABLE moderation_votes (
  mid integer,
  vote varchar(255) ,
  weight smallint NOT NULL ,
  PRIMARY KEY  (mid)
);
CREATE INDEX node_type_idx ON node(type);
CREATE INDEX node_title_idx ON node(title,type);
CREATE INDEX node_status_idx ON node(status);
CREATE INDEX node_uid_idx ON node(uid);
CREATE INDEX node_moderate_idx ON node (moderate);
CREATE INDEX node_promote_status_idx ON node (promote, status);
CREATE TABLE node_counter (
  nid integer NOT NULL ,
  totalcount integer NOT NULL ,
  daycount integer NOT NULL ,
  timestamp integer NOT NULL ,
  PRIMARY KEY  (nid)
);
CREATE INDEX node_counter_totalcount_idx ON node_counter(totalcount);
CREATE INDEX node_counter_daycount_idx ON node_counter(daycount);
CREATE INDEX node_counter_timestamp_idx ON node_counter(timestamp);
CREATE TABLE page (
  nid integer NOT NULL ,
  link varchar(128) NOT NULL ,
  format smallint NOT NULL ,
  description varchar(128) NOT NULL ,
  PRIMARY KEY  (nid)
);
CREATE INDEX page_nid_idx ON page(nid);
CREATE TABLE url_alias (
  pid integer,
  dst varchar(128) NOT NULL ,
  src varchar(128) NOT NULL ,
  PRIMARY KEY  (pid)
);
CREATE INDEX url_alias_src_idx ON url_alias(src);
CREATE INDEX url_alias_dst_idx ON url_alias(dst);
CREATE TABLE permission (
  rid integer NOT NULL ,
  perm varchar(1024) ,
  tid integer NOT NULL
);
CREATE INDEX permission_rid_idx ON permission(rid);
CREATE TABLE poll (
  nid integer NOT NULL ,
  runtime integer NOT NULL ,
  voters varchar(1024) NOT NULL ,
  active integer NOT NULL ,
  PRIMARY KEY  (nid)
);
CREATE TABLE poll_choices (
  chid integer,
  nid integer NOT NULL ,
  chtext varchar(128) NOT NULL ,
  chvotes integer NOT NULL ,
  chorder integer NOT NULL ,
  PRIMARY KEY  (chid)
);
CREATE INDEX poll_choices_nid_idx ON poll_choices(nid);
CREATE TABLE role (
  rid integer,
  name varchar(32) NOT NULL ,
  PRIMARY KEY  (rid),
  UNIQUE (name)
);
CREATE TABLE search_index (
  word varchar(50) NOT NULL ,
  lno integer NOT NULL ,
  type varchar(16) ,
  count integer 
);
CREATE INDEX search_index_lno_idx ON search_index(lno);
CREATE INDEX search_index_word_idx ON search_index(word);
CREATE TABLE sessions (
  uid integer NOT NULL,
  sid varchar(32) NOT NULL ,
  hostname varchar(128) NOT NULL ,
  timestamp integer NOT NULL ,
  session varchar(1024),
  PRIMARY KEY (sid)
);
CREATE TABLE sequences (
  name varchar(255) NOT NULL,
  id integer NOT NULL,
  PRIMARY KEY (name)
);
CREATE TABLE site (
  sid integer,
  name varchar(128) NOT NULL ,
  link varchar(255) NOT NULL ,
  size varchar(1024) NOT NULL ,
  changed integer NOT NULL ,
  checked integer NOT NULL ,
  feed varchar(255) NOT NULL ,
  refresh integer NOT NULL ,
  threshold integer NOT NULL ,
  PRIMARY KEY  (sid),
  UNIQUE (name),
  UNIQUE (link)
);
CREATE TABLE system (
  filename varchar(255) NOT NULL ,
  name varchar(255) NOT NULL ,
  type varchar(255) NOT NULL ,
  description varchar(255) NOT NULL ,
  status integer NOT NULL ,
  PRIMARY KEY  (filename)
);
CREATE TABLE term_data (
  tid integer,
  vid integer NOT NULL ,
  name varchar(255) NOT NULL ,
  description varchar(1024) ,
  weight smallint NOT NULL ,
  PRIMARY KEY  (tid)
);
CREATE INDEX term_data_vid_idx ON term_data(vid);
CREATE TABLE term_hierarchy (
  tid integer NOT NULL ,
  parent integer NOT NULL
);
CREATE INDEX term_hierarchy_tid_idx ON term_hierarchy(tid);
CREATE INDEX term_hierarchy_parent_idx ON term_hierarchy(parent);
CREATE TABLE term_node (
  nid integer NOT NULL ,
  tid integer NOT NULL
);
CREATE INDEX term_node_nid_idx ON term_node(nid);
CREATE INDEX term_node_tid_idx ON term_node(tid);
CREATE TABLE term_relation (
  tid1 integer NOT NULL ,
  tid2 integer NOT NULL
);
CREATE INDEX term_relation_tid1_idx ON term_relation(tid1);
CREATE INDEX term_relation_tid2_idx ON term_relation(tid2);
CREATE TABLE term_synonym (
  tid integer NOT NULL ,
  name varchar(255) NOT NULL 
);
CREATE INDEX term_synonym_tid_idx ON term_synonym(tid);
CREATE INDEX term_synonym_name_idx ON term_synonym(name);
CREATE TABLE variable (
  name varchar(48) NOT NULL ,
  value varchar(1024) NOT NULL ,
  PRIMARY KEY  (name)
);
CREATE TABLE vocabulary (
  vid integer,
  name varchar(255) NOT NULL ,
  description varchar(1024) ,
  relations smallint NOT NULL ,
  hierarchy smallint NOT NULL ,
  multiple smallint NOT NULL ,
  required smallint NOT NULL ,
  nodes varchar(1024) ,
  weight smallint NOT NULL ,
  PRIMARY KEY  (vid)
);
CREATE TABLE watchdog (
  wid integer,
  uid integer NOT NULL ,
  type varchar(16) NOT NULL ,
  message varchar(1024) NOT NULL ,
  link varchar(255) NOT NULL ,
  location varchar(128) NOT NULL ,
  hostname varchar(128) NOT NULL ,
  timestamp integer NOT NULL ,
  PRIMARY KEY  (wid)
);
CREATE TABLE users (
  uid integer NOT NULL ,
  name varchar(60) NOT NULL ,
  pass varchar(32) NOT NULL ,
  mail varchar(64) ,
  mode smallint NOT NULL ,
  sort smallint ,
  threshold smallint ,
  theme varchar(255) NOT NULL ,
  signature varchar(255) NOT NULL ,
  timestamp integer NOT NULL ,
  status smallint NOT NULL ,
  timezone varchar(8) ,
  language char(2) NOT NULL ,
  init varchar(64) ,
  data varchar(1024) ,
  rid integer NOT NULL ,
  PRIMARY KEY  (uid),
  UNIQUE (name)
);
CREATE TABLE node (
  nid integer,
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
  attributes varchar(255) NOT NULL ,
  teaser varchar(1024) NOT NULL ,
  body varchar(1024) NOT NULL ,
  changed integer NOT NULL ,
  revisions varchar(1024) NOT NULL ,
  static integer NOT NULL ,
  PRIMARY KEY  (nid)
);
CREATE INDEX users_timestamp_idx ON users(timestamp);
CREATE SEQUENCE users_uid_seq INCREMENT 1 START 1;
CREATE VIEW ourView AS
SELECT USERS.uid, name, pass, mail, mode, sort, threshold, theme, signature, timestamp, USERS.status, timezone, language, init, data, rid, nid, type, title, score, votes, created, comment, promote, moderate, NODE.users, attributes, teaser, body, changed, revisions, static
FROM USERS LEFT JOIN NODE ON USERS.uid = NODE.uid;
CREATE VIEW ourViewN AS
SELECT USERS.uid, name, pass, mail, mode, sort, threshold, theme, signature, timestamp, USERS.status, timezone, language, init, data, rid, nid, type, title, score, votes, created, comment, promote, moderate, NODE.users, attributes, teaser, body, changed, revisions, static
FROM USERS INNER JOIN NODE ON USERS.uid = NODE.uid;