set client_min_messages = 'warning';
CREATE TABLE access (
  aid integer,
  mask varchar(255) NOT NULL ,
  type varchar(255) NOT NULL ,
  status smallint NOT NULL ,
  PRIMARY KEY (aid)
);
CREATE TABLE accesslog (
  aid integer,
  sid varchar(32) NOT NULL ,
  title varchar(255) ,
  path varchar(1024) ,
  url varchar(1024) ,
  hostname varchar(128) ,
  uid integer ,
  timer integer NOT NULL ,
  timestamp integer NOT NULL ,
  PRIMARY KEY (aid)
);
CREATE INDEX accesslog_timestamp_idx ON accesslog (timestamp);
CREATE TABLE aggregator_category (
  cid integer,
  title varchar(255) NOT NULL ,
  description varchar(1024),
  block smallint NOT NULL ,
  PRIMARY KEY (cid),
  UNIQUE (title)
);
CREATE TABLE aggregator_category_feed (
  fid integer NOT NULL ,
  cid integer NOT NULL ,
  PRIMARY KEY (fid,cid)
);
CREATE TABLE aggregator_category_item (
  iid integer NOT NULL ,
  cid integer NOT NULL ,
  PRIMARY KEY (iid,cid)
);
CREATE TABLE aggregator_feed (
  fid integer,
  title varchar(255) NOT NULL ,
  url varchar(255) NOT NULL ,
  refresh integer NOT NULL ,
  checked integer NOT NULL ,
  link varchar(255) NOT NULL ,
  description varchar(1024),
  image varchar(1024),
  etag varchar(255) NOT NULL ,
  modified integer NOT NULL ,
  block smallint NOT NULL ,
  PRIMARY KEY (fid),
  UNIQUE (url),
  UNIQUE (title)
);
CREATE TABLE aggregator_item (
  iid integer,
  fid integer NOT NULL ,
  title varchar(255) NOT NULL ,
  link varchar(255) NOT NULL ,
  author varchar(255) NOT NULL ,
  description varchar(1024),
  timestamp integer ,
  PRIMARY KEY (iid)
);
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
  theme varchar(255) NOT NULL ,
  status smallint NOT NULL ,
  weight smallint NOT NULL ,
  region varchar(64)  NOT NULL,
  custom smallint NOT NULL ,
  throttle smallint NOT NULL ,
  visibility smallint NOT NULL ,
  pages varchar(1024) NOT NULL 
);
CREATE TABLE book (
  vid integer NOT NULL ,
  nid integer NOT NULL ,
  parent integer NOT NULL ,
  weight smallint NOT NULL ,
  PRIMARY KEY (vid)
);
CREATE INDEX book_nid_idx ON book(nid);
CREATE INDEX book_parent_idx ON book(parent);
CREATE TABLE boxes (
  bid integer,
  title varchar(64) NOT NULL ,
  body varchar(1024) ,
  info varchar(128) NOT NULL ,
  format smallint NOT NULL ,
  PRIMARY KEY (bid),
  UNIQUE (info)
);
CREATE TABLE cache (
  cid varchar(255) NOT NULL ,
  data varchar(1024) ,
  expire integer NOT NULL ,
  created integer NOT NULL ,
  headers varchar(1024) ,
  PRIMARY KEY (cid)
);
CREATE INDEX cache_expire_idx ON cache(expire);
CREATE TABLE comments (
  cid integer,
  pid integer NOT NULL ,
  nid integer NOT NULL ,
  uid integer NOT NULL ,
  subject varchar(64) NOT NULL ,
  comment varchar(1024) NOT NULL ,
  hostname varchar(128) NOT NULL ,
  timestamp integer NOT NULL ,
  score integer NOT NULL ,
  status smallint NOT NULL ,
  format smallint NOT NULL ,
  thread varchar(255) ,
  users varchar(1024) ,
  name varchar(60) ,
  mail varchar(64) ,
  homepage varchar(255) ,
  PRIMARY KEY (cid)
);
CREATE INDEX comments_nid_idx ON comments(nid);
CREATE TABLE contact (
  cid integer,
  category varchar(255) NOT NULL ,
  recipients varchar(1024) NOT NULL ,
  reply varchar(1024) NOT NULL ,
  weight smallint NOT NULL ,
  selected smallint NOT NULL ,
  PRIMARY KEY (cid),
  UNIQUE (category)
);
CREATE TABLE node_comment_statistics (
  nid integer NOT NULL,
  last_comment_timestamp integer NOT NULL ,
  last_comment_name varchar(60) ,
  last_comment_uid integer NOT NULL ,
  comment_count integer NOT NULL ,
  PRIMARY KEY (nid)
);
CREATE INDEX node_comment_statistics_last_comment_timestamp_idx ON node_comment_statistics(last_comment_timestamp);
CREATE TABLE client (
  cid integer,
  link varchar(255) NOT NULL ,
  name varchar(128) NOT NULL ,
  mail varchar(128) NOT NULL ,
  slogan varchar(1024) NOT NULL ,
  mission varchar(1024) NOT NULL ,
  users integer NOT NULL ,
  nodes integer NOT NULL ,
  version varchar(35) NOT NULL ,
  created integer NOT NULL ,
  changed integer NOT NULL ,
  PRIMARY KEY (cid)
);
CREATE TABLE client_system (
  cid integer NOT NULL,
  name varchar(255) NOT NULL ,
  type varchar(255) NOT NULL ,
  PRIMARY KEY (cid,name)
);
CREATE TABLE files (
  fid integer,
  nid integer NOT NULL ,
  filename varchar(255) NOT NULL ,
  filepath varchar(255) NOT NULL ,
  filemime varchar(255) NOT NULL ,
  filesize integer NOT NULL ,
  PRIMARY KEY (fid)
);
CREATE TABLE file_revisions (
  fid integer NOT NULL ,
  vid integer NOT NULL ,
  description varchar(255) NOT NULL ,
  list smallint NOT NULL ,
  PRIMARY KEY (fid, vid)
);
CREATE TABLE filter_formats (
  format integer,
  name varchar(255) NOT NULL ,
  roles varchar(255) NOT NULL ,
  cache smallint NOT NULL ,
  PRIMARY KEY (format),
  UNIQUE (name)
);
CREATE TABLE filters (
  format integer NOT NULL ,
  module varchar(64) NOT NULL ,
  delta smallint NOT NULL ,
  weight smallint  NOT NULL
);
CREATE INDEX filters_weight_idx ON filters(weight);
CREATE TABLE flood (
  event varchar(64) NOT NULL ,
  hostname varchar(128) NOT NULL ,
  timestamp integer NOT NULL 
);
CREATE TABLE forum (
  nid integer NOT NULL ,
  vid integer NOT NULL ,
  tid integer NOT NULL ,
  PRIMARY KEY (vid)
);
CREATE INDEX forum_tid_idx ON forum(tid);
CREATE INDEX forum_nid_idx ON forum(nid);
CREATE TABLE history (
  uid integer NOT NULL ,
  nid integer NOT NULL ,
  timestamp integer NOT NULL ,
  PRIMARY KEY (uid,nid)
);
CREATE TABLE locales_meta (
  locale varchar(12) NOT NULL ,
  name varchar(64) NOT NULL ,
  enabled integer NOT NULL ,
  isdefault integer NOT NULL ,
  plurals integer NOT NULL ,
  formula varchar(128) NOT NULL ,
  PRIMARY KEY (locale)
);
CREATE TABLE locales_source (
  lid integer,
  location varchar(255) NOT NULL ,
  source varchar(1024) NOT NULL,
  PRIMARY KEY (lid)
);
CREATE TABLE locales_target (
  lid integer NOT NULL ,
  translation varchar(1024)  NOT NULL,
  locale varchar(12) NOT NULL ,
  plid integer NOT NULL ,
  plural integer NOT NULL 
);
CREATE INDEX locales_target_lid_idx ON locales_target(lid);
CREATE INDEX locales_target_locale_idx ON locales_target(locale);
CREATE INDEX locales_target_plid_idx ON locales_target(plid);
CREATE INDEX locales_target_plural_idx ON locales_target(plural);
CREATE SEQUENCE menu_mid_seq START 2;
CREATE TABLE menu (
  mid integer NOT NULL ,
  pid integer NOT NULL ,
  path varchar(255) NOT NULL ,
  title varchar(255) NOT NULL ,
  description varchar(255) NOT NULL ,
  weight smallint NOT NULL ,
  type smallint NOT NULL ,
  PRIMARY KEY (mid)
);
CREATE TABLE users_roles (
  uid integer NOT NULL ,
  rid integer NOT NULL ,
  PRIMARY KEY (uid, rid)
);
CREATE TABLE variable (
  name varchar(48) NOT NULL ,
  value varchar(1024) NOT NULL ,
  PRIMARY KEY (name)
);
CREATE TABLE vocabulary (
  vid integer,
  name varchar(255) NOT NULL ,
  description varchar(1024) ,
  help varchar(255) NOT NULL ,
  relations smallint NOT NULL ,
  hierarchy smallint NOT NULL ,
  multiple smallint NOT NULL ,
  required smallint NOT NULL ,
  tags smallint NOT NULL ,
  module varchar(255) NOT NULL ,
  weight smallint NOT NULL ,
  PRIMARY KEY (vid)
);
CREATE TABLE vocabulary_node_types (
  vid integer NOT NULL ,
  type varchar(32) NOT NULL ,
  PRIMARY KEY (vid, type)
);
CREATE TABLE watchdog (
  wid integer,
  uid integer NOT NULL ,
  type varchar(16) NOT NULL ,
  message varchar(1024) NOT NULL ,
  severity smallint NOT NULL ,
  link varchar(1024) NOT NULL ,
  location varchar(1024) NOT NULL ,
  referer varchar(1024) NOT NULL ,
  hostname varchar(128) NOT NULL ,
  timestamp integer NOT NULL ,
  PRIMARY KEY (wid)
);
CREATE TABLE node_access (
  nid integer,
  gid integer NOT NULL ,
  realm varchar(255) NOT NULL ,
  grant_view smallint NOT NULL ,
  grant_update smallint NOT NULL ,
  grant_delete smallint NOT NULL ,
  PRIMARY KEY (nid,gid,realm)
);
CREATE TABLE node_revisions (
  nid integer NOT NULL ,
  vid integer NOT NULL ,
  uid integer NOT NULL ,
  title varchar(128) NOT NULL ,
  body varchar(1024) NOT NULL ,
  teaser varchar(1024) NOT NULL ,
  log varchar(1024) NOT NULL ,
  timestamp integer NOT NULL ,
  format int NOT NULL ,
  PRIMARY KEY  (vid)
);
CREATE INDEX node_revisions_nid_idx ON node_revisions(nid);
CREATE INDEX node_revisions_uid_idx ON node_revisions(uid);
CREATE SEQUENCE node_revisions_vid_seq INCREMENT 1 START 1;
CREATE TABLE profile_fields (
  fid integer,
  title varchar(255) ,
  name varchar(128) ,
  explanation varchar(1024) ,
  category varchar(255) ,
  page varchar(255) ,
  type varchar(128) ,
  weight smallint  NOT NULL,
  required smallint  NOT NULL,
  register smallint  NOT NULL,
  visibility smallint  NOT NULL,
  autocomplete smallint  NOT NULL,
  options varchar(1024),
  UNIQUE (name),
  PRIMARY KEY (fid)
);
CREATE INDEX profile_fields_category_idx ON profile_fields (category);
CREATE TABLE profile_values (
  fid integer ,
  uid integer ,
  value varchar(1024)
);
CREATE INDEX profile_values_uid ON profile_values (uid);
CREATE INDEX profile_values_fid ON profile_values (fid);
CREATE TABLE url_alias (
  pid integer,
  src varchar(128) NOT NULL ,
  dst varchar(128) NOT NULL ,
  PRIMARY KEY (pid)
);
CREATE UNIQUE INDEX url_alias_dst_idx ON url_alias(dst);
CREATE INDEX url_alias_src_idx ON url_alias(src);
CREATE TABLE permission (
  rid integer NOT NULL ,
  perm varchar(1024) ,
  tid integer NOT NULL 
);
CREATE INDEX permission_rid_idx ON permission(rid);
CREATE TABLE poll (
  nid integer NOT NULL ,
  runtime integer NOT NULL ,
  active integer NOT NULL ,
  PRIMARY KEY (nid)
);
CREATE TABLE poll_votes (
  nid int NOT NULL,
  uid int NOT NULL ,
  hostname varchar(128) NOT NULL 
);
CREATE INDEX poll_votes_nid_idx ON poll_votes (nid);
CREATE INDEX poll_votes_uid_idx ON poll_votes (uid);
CREATE INDEX poll_votes_hostname_idx ON poll_votes (hostname);
CREATE TABLE poll_choices (
  chid integer,
  nid integer NOT NULL ,
  chtext varchar(128) NOT NULL ,
  chvotes integer NOT NULL ,
  chorder integer NOT NULL ,
  PRIMARY KEY (chid)
);
CREATE INDEX poll_choices_nid_idx ON poll_choices(nid);
CREATE TABLE role (
  rid integer,
  name varchar(32) NOT NULL ,
  PRIMARY KEY (rid),
  UNIQUE (name)
);
CREATE TABLE search_dataset (
  sid integer NOT NULL ,
  type varchar(16) ,
  data varchar(1024) NOT NULL 
);
CREATE INDEX search_dataset_sid_type_idx ON search_dataset(sid, type);
CREATE TABLE search_index (
  word varchar(50) NOT NULL ,
  sid integer NOT NULL ,
  type varchar(16) ,
  fromsid integer NOT NULL ,
  fromtype varchar(16) ,
  score float 
);
CREATE INDEX search_index_sid_type_idx ON search_index(sid, type);
CREATE INDEX search_index_fromsid_fromtype_idx ON search_index(fromsid, fromtype);
CREATE INDEX search_index_word_idx ON search_index(word);
CREATE TABLE search_total (
  word varchar(50) NOT NULL ,
  count float ,
  PRIMARY KEY(word)
);
CREATE TABLE sessions (
  uid integer not null,
  sid varchar(32) NOT NULL ,
  hostname varchar(128) NOT NULL ,
  timestamp integer NOT NULL ,
  cache integer NOT NULL ,
  session varchar(1024),
  PRIMARY KEY (sid)
);
CREATE INDEX sessions_uid_idx ON sessions(uid);
CREATE INDEX sessions_timestamp_idx ON sessions(timestamp);
CREATE TABLE sequences (
  name varchar(255) NOT NULL,
  id integer NOT NULL,
  PRIMARY KEY (name)
);
CREATE TABLE node_counter (
  nid integer NOT NULL ,
  totalcount integer NOT NULL ,
  daycount integer NOT NULL ,
  timestamp integer NOT NULL ,
  PRIMARY KEY (nid)
);
CREATE INDEX node_counter_totalcount_idx ON node_counter(totalcount);
CREATE INDEX node_counter_daycount_idx ON node_counter(daycount);
CREATE INDEX node_counter_timestamp_idx ON node_counter(timestamp);
CREATE TABLE system (
  filename varchar(255) NOT NULL ,
  name varchar(255) NOT NULL ,
  type varchar(255) NOT NULL ,
  description varchar(255) NOT NULL ,
  status integer NOT NULL ,
  throttle smallint NOT NULL ,
  bootstrap integer NOT NULL ,
  schema_version smallint NOT NULL ,
  weight smallint NOT NULL ,
  PRIMARY KEY (filename)
);
CREATE INDEX system_weight_idx ON system(weight);
CREATE TABLE term_data (
  tid integer,
  vid integer NOT NULL ,
  name varchar(255) NOT NULL ,
  description varchar(1024) ,
  weight smallint NOT NULL ,
  PRIMARY KEY (tid)
);
CREATE INDEX term_data_vid_idx ON term_data(vid);
CREATE TABLE term_hierarchy (
  tid integer NOT NULL ,
  parent integer NOT NULL ,
  PRIMARY KEY (tid, parent)
);
CREATE INDEX term_hierarchy_tid_idx ON term_hierarchy(tid);
CREATE INDEX term_hierarchy_parent_idx ON term_hierarchy(parent);
CREATE TABLE term_node (
  nid integer NOT NULL ,
  tid integer NOT NULL ,
  PRIMARY KEY (tid,nid)
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
CREATE TABLE node (
  nid integer,
  vid integer NOT NULL ,
  type varchar(32) NOT NULL ,
  title varchar(128) NOT NULL ,
  uid integer NOT NULL ,
  status integer NOT NULL,
  created integer NOT NULL ,
  changed integer NOT NULL ,
  comment integer NOT NULL ,
  promote integer NOT NULL ,
  moderate integer NOT NULL ,
  sticky integer NOT NULL ,
  PRIMARY KEY (nid, vid)
);
CREATE INDEX node_nid_idx ON node(nid);
CREATE INDEX node_type_idx ON node(type);
CREATE INDEX node_title_type_idx ON node(title,type);
CREATE INDEX node_status_idx ON node(status);
CREATE INDEX node_uid_idx ON node(uid);
CREATE UNIQUE INDEX node_vid_idx ON node(vid);
CREATE INDEX node_moderate_idx ON node (moderate);
CREATE INDEX node_promote_status_idx ON node (promote, status);
CREATE INDEX node_created_idx ON node(created);
CREATE INDEX node_changed_idx ON node(changed);
CREATE INDEX node_status_type_nid_idx ON node(status,type,nid);
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
  created integer NOT NULL ,
  access integer NOT NULL ,
  login integer NOT NULL ,
  status smallint NOT NULL ,
  timezone varchar(8) ,
  language varchar(12) NOT NULL ,
  picture varchar(255) NOT NULL ,
  init varchar(64) ,
  data varchar(1024) ,
  PRIMARY KEY (uid),
  UNIQUE (name)
);
CREATE INDEX users_access_idx ON users(access);
CREATE SEQUENCE users_uid_seq INCREMENT 1 START 1;