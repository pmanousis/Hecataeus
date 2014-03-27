CREATE TABLE menu_router (
	path varchar(255) NOT NULL,
	load_functions varchar(255) NOT NULL,
	to_arg_functions varchar(255) NOT NULL,
	access_callback varchar(255) NOT NULL,
	access_arguments varchar(255),
	page_callback varchar(255) NOT NULL,
	page_arguments varchar(255),
	delivery_callback varchar(255) NOT NULL,
	fit int(11) NOT NULL,
	number_parts int(11) NOT NULL,
	context int(11) NOT NULL,
	tab_parent varchar(255) NOT NULL,
	tab_root varchar(255) NOT NULL,
	title varchar(255) NOT NULL,
	title_callback varchar(255) NOT NULL,
	title_arguments varchar(255) NOT NULL,
	theme_callback varchar(255) NOT NULL,
	theme_arguments varchar(255) NOT NULL,
	type int(11) NOT NULL,
	description varchar(255) NOT NULL,
	position varchar(255) NOT NULL,
	weight int(11) NOT NULL,
	include_file varchar(255),
	PRIMARY KEY (path)
);

CREATE TABLE mybloced_ips (
	iid int(10),
	ip varchar(40) NOT NULL,
	PRIMARY KEY (iid)
);

CREATE TABLE  actions  (
   aid  varchar(255) NOT NULL,
   type  varchar(32) NOT NULL,
   callback  varchar(255) NOT NULL,
   parameters   varchar(255) NOT NULL,
   label  varchar(255) NOT NULL,
  PRIMARY KEY ( aid )
);

CREATE TABLE  authmap  (
   aid  int(10) NOT NULL,
   uid  int(11) NOT NULL,
   authname  varchar(128) NOT NULL,
   module  varchar(128) NOT NULL,
  PRIMARY KEY ( aid )
);

CREATE TABLE  batch  (
   bid  int(10)   NOT NULL,
   token  varchar(64) NOT NULL,
   timestamp  int(11) NOT NULL,
   batch  varchar(255),
  PRIMARY KEY ( bid )
) ;

CREATE TABLE  mybloc  (
   bid  int(11) NOT NULL,
   module  varchar(64) NOT NULL,
   delta  varchar(32) NOT NULL,
   theme  varchar(64) NOT NULL,
   status  tinyint(4) NOT NULL,
   weight  int(11) NOT NULL,
   region  varchar(64) NOT NULL,
   custom  tinyint(4) NOT NULL,
   visibility  tinyint(4) NOT NULL,
   pages  varchar(255)  NOT NULL,
   title  varchar(64) NOT NULL,
   cache  tinyint(4) NOT NULL,
  PRIMARY KEY ( bid )
);

CREATE TABLE  mybloc_custom  (
   bid  int(10)   NOT NULL,
   body  varchar(255),
   info  varchar(128) NOT NULL,
   format  varchar(255),
  PRIMARY KEY ( bid )
);

CREATE TABLE  mybloc_MYNODE_type  (
   module  varchar(64) NOT NULL,
   delta  varchar(32) NOT NULL,
   type  varchar(32) NOT NULL,
  PRIMARY KEY ( module, delta, type )
) ;

CREATE TABLE  mybloc_role  (
   module  varchar(64) NOT NULL,
   delta  varchar(32) NOT NULL,
   rid  int(10)   NOT NULL,
  PRIMARY KEY ( module, delta, rid )
);

CREATE TABLE  cache  (
   cid  varchar(255) NOT NULL,
   data  varchar(255),
   expire  int(11) NOT NULL,
   created  int(11) NOT NULL,
   serialized  smallint(6) NOT NULL,
  PRIMARY KEY ( cid )
) ;

CREATE TABLE  cache_mybloc  (
   cid  varchar(255) NOT NULL,
   data  varchar(255),
   expire  int(11) NOT NULL,
   created  int(11) NOT NULL,
   serialized  smallint(6) NOT NULL,
  PRIMARY KEY ( cid )
);

CREATE TABLE  cache_bootstrap  (
   cid  varchar(255) NOT NULL,
   data  varchar(255),
   expire  int(11) NOT NULL,
   created  int(11) NOT NULL,
   serialized  smallint(6) NOT NULL,
  PRIMARY KEY ( cid )
);

CREATE TABLE  cache_field  (
   cid  varchar(255) NOT NULL,
   data  varchar(255),
   expire  int(11) NOT NULL,
   created  int(11) NOT NULL,
   serialized  smallint(6) NOT NULL,
  PRIMARY KEY ( cid )
)  ;

CREATE TABLE  cache_filter  (
   cid  varchar(255) NOT NULL,
   data  varchar(255),
   expire  int(11) NOT NULL,
   created  int(11) NOT NULL,
   serialized  smallint(6) NOT NULL,
  PRIMARY KEY ( cid )
)  ;

CREATE TABLE  cache_form  (
   cid  varchar(255) NOT NULL,
   data  varchar(255),
   expire  int(11) NOT NULL,
   created  int(11) NOT NULL,
   serialized  smallint(6) NOT NULL,
  PRIMARY KEY ( cid )
)  ;

CREATE TABLE  cache_image  (
   cid  varchar(255) NOT NULL,
   data  varchar(255),
   expire  int(11) NOT NULL,
   created  int(11) NOT NULL,
   serialized  smallint(6) NOT NULL,
  PRIMARY KEY ( cid )
)  ;

CREATE TABLE  cache_menu  (
   cid  varchar(255) NOT NULL,
   data  varchar(255),
   expire  int(11) NOT NULL,
   created  int(11) NOT NULL,
   serialized  smallint(6) NOT NULL,
  PRIMARY KEY ( cid )
)  ;

CREATE TABLE  cache_page  (
   cid  varchar(255) NOT NULL,
   data  varchar(255),
   expire  int(11) NOT NULL,
   created  int(11) NOT NULL,
   serialized  smallint(6) NOT NULL,
  PRIMARY KEY ( cid )
)  ;

CREATE TABLE  cache_path  (
   cid  varchar(255) NOT NULL,
   data  varchar(255),
   expire  int(11) NOT NULL,
   created  int(11) NOT NULL,
   serialized  smallint(6) NOT NULL,
  PRIMARY KEY ( cid )
)  ;

CREATE TABLE  cache_update  (
   cid  varchar(255) NOT NULL,
   data  varchar(255),
   expire  int(11) NOT NULL,
   created  int(11) NOT NULL,
   serialized  smallint(6) NOT NULL,
  PRIMARY KEY ( cid )
)  ;

CREATE TABLE   MYCOMMENT  (
   cid  int(11) NOT NULL,
   pid  int(11) NOT NULL,
   nid  int(11) NOT NULL,
   uid  int(11) NOT NULL,
   subject  varchar(64) NOT NULL,
   hostname  varchar(128) NOT NULL,
   created  int(11) NOT NULL,
   changed  int(11) NOT NULL,
   status  tinyint(3)   NOT NULL,
   thread  varchar(255) NOT NULL,
   name  varchar(60)   NULL,
   mail  varchar(64)   NULL,
   homepage  varchar(255)   NULL,
   language  varchar(12) NOT NULL,
  PRIMARY KEY ( cid )
)  ;

CREATE TABLE  date_format_locale  (
   format  varchar(100) NOT NULL,
   type  varchar(64) NOT NULL,
   language  varchar(12) NOT NULL,
  PRIMARY KEY ( type, language )
)  ;

CREATE TABLE  date_format_type  (
   type  varchar(64) NOT NULL,
   title  varchar(255) NOT NULL,
   locked  tinyint(4) NOT NULL,
  PRIMARY KEY ( type )
)  ;

CREATE TABLE  date_formats  (
   dfid  int(10)   NOT NULL,
   format  varchar(100),
   type  varchar(64) NOT NULL,
   locked  tinyint(4) NOT NULL,
  PRIMARY KEY ( dfid )
) ;

CREATE TABLE  field_config  (
   id  int(11) NOT NULL,
   field_name  varchar(32) NOT NULL,
   type  varchar(128) NOT NULL,
   module  varchar(128) NOT NULL,
   active  tinyint(4) NOT NULL,
   storage_type  varchar(128) NOT NULL,
   storage_module  varchar(128) NOT NULL,
   storage_active  tinyint(4) NOT NULL,
   locked  tinyint(4) NOT NULL,
   data  varchar(255) NOT NULL,
   cardinality  tinyint(4) NOT NULL,
   translatable  tinyint(4) NOT NULL,
   deleted  tinyint(4) NOT NULL,
  PRIMARY KEY ( id )
);

CREATE TABLE  field_config_instance  (
   id  int(11) NOT NULL,
   field_id  int(11) NOT NULL,
   field_name  varchar(32) NOT NULL,
   entity_type  varchar(32) NOT NULL,
   bundle  varchar(128) NOT NULL,
   data  varchar(255) NOT NULL,
   deleted  tinyint(4) NOT NULL,
  PRIMARY KEY ( id )
) ;

CREATE TABLE  field_data_body  (
   entity_type  varchar(128) NOT NULL,
   bundle  varchar(128) NOT NULL,
   deleted  tinyint(4) NOT NULL,
   entity_id  int(10)   NOT NULL,
   revision_id  int(10)     NULL,
   language  varchar(32) NOT NULL,
   delta  int(10)   NOT NULL,
   body_value  varchar(255),
   body_summary  varchar(255),
   body_format  varchar(255)   NULL,
  PRIMARY KEY ( entity_type, entity_id, deleted, delta, language )
)  ;

CREATE TABLE  field_data_MYCOMMENT_body  (
   entity_type  varchar(128) NOT NULL,
   bundle  varchar(128) NOT NULL,
   deleted  tinyint(4) NOT NULL,
   entity_id  int(10)   NOT NULL,
   revision_id  int(10)     NULL,
   language  varchar(32) NOT NULL,
   delta  int(10)   NOT NULL,
    MYCOMMENT_body_value  varchar(255),
    MYCOMMENT_body_format  varchar(255)   NULL,
  PRIMARY KEY ( entity_type, entity_id, deleted, delta, language )
)  ;

CREATE TABLE  field_data_field_image  (
   entity_type  varchar(128) NOT NULL,
   bundle  varchar(128) NOT NULL,
   deleted  tinyint(4) NOT NULL,
   entity_id  int(10)   NOT NULL,
   revision_id  int(10)     NULL,
   language  varchar(32) NOT NULL,
   delta  int(10)   NOT NULL,
   field_image_fid  int(10)     NULL,
   field_image_alt  varchar(512)   NULL,
   field_image_title  varchar(1024)   NULL,
   field_image_width  int(10)     NULL,
   field_image_height  int(10)     NULL,
  PRIMARY KEY ( entity_type, entity_id, deleted, delta, language )
)  ;

CREATE TABLE  field_data_field_tags  (
   entity_type  varchar(128) NOT NULL,
   bundle  varchar(128) NOT NULL,
   deleted  tinyint(4) NOT NULL,
   entity_id  int(10)   NOT NULL,
   revision_id  int(10)     NULL,
   language  varchar(32) NOT NULL,
   delta  int(10)   NOT NULL,
   field_tags_tid  int(10)     NULL,
  PRIMARY KEY ( entity_type, entity_id, deleted, delta, language )
)  ;

CREATE TABLE  field_revision_body  (
   entity_type  varchar(128) NOT NULL,
   bundle  varchar(128) NOT NULL,
   deleted  tinyint(4) NOT NULL,
   entity_id  int(10)   NOT NULL,
   revision_id  int(10)   NOT NULL,
   language  varchar(32) NOT NULL,
   delta  int(10)   NOT NULL,
   body_value  varchar(255),
   body_summary  varchar(255),
   body_format  varchar(255)   NULL,
  PRIMARY KEY ( entity_type, entity_id, revision_id, deleted, delta, language )
)  ;

CREATE TABLE  field_revision_MYCOMMENT_body  (
   entity_type  varchar(128) NOT NULL,
   bundle  varchar(128) NOT NULL,
   deleted  tinyint(4) NOT NULL,
   entity_id  int(10)   NOT NULL,
   revision_id  int(10)   NOT NULL,
   language  varchar(32) NOT NULL,
   delta  int(10)   NOT NULL,
    MYCOMMENT_body_value  varchar(255),
    MYCOMMENT_body_format  varchar(255)   NULL,
  PRIMARY KEY ( entity_type, entity_id, revision_id, deleted, delta, language )
)  ;

CREATE TABLE  field_revision_field_image  (
   entity_type  varchar(128) NOT NULL,
   bundle  varchar(128) NOT NULL,
   deleted  tinyint(4) NOT NULL,
   entity_id  int(10)   NOT NULL,
   revision_id  int(10)   NOT NULL,
   language  varchar(32) NOT NULL,
   delta  int(10)   NOT NULL,
   field_image_fid  int(10)     NULL,
   field_image_alt  varchar(512)   NULL,
   field_image_title  varchar(1024)   NULL,
   field_image_width  int(10)     NULL,
   field_image_height  int(10)     NULL,
  PRIMARY KEY ( entity_type, entity_id, revision_id, deleted, delta, language )
)  ;

CREATE TABLE  field_revision_field_tags  (
   entity_type  varchar(128) NOT NULL,
   bundle  varchar(128) NOT NULL,
   deleted  tinyint(4) NOT NULL,
   entity_id  int(10)   NOT NULL,
   revision_id  int(10)   NOT NULL,
   language  varchar(32) NOT NULL,
   delta  int(10)   NOT NULL,
   field_tags_tid  int(10)     NULL,
  PRIMARY KEY ( entity_type, entity_id, revision_id, deleted, delta, language )
)  ;

CREATE TABLE  file_managed  (
   fid  int(10)   NOT NULL,
   uid  int(10)   NOT NULL,
   filename  varchar(255) NOT NULL,
   uri  varchar(255),
   filemime  varchar(255) NOT NULL,
   filesize  int(10)   NOT NULL,
   status  tinyint(4) NOT NULL,
   timestamp  int(10)   NOT NULL,
  PRIMARY KEY ( fid )
)  ;

CREATE TABLE  file_usage  (
   fid  int(10)   NOT NULL,
   module  varchar(255) NOT NULL,
   type  varchar(64) NOT NULL,
   id  int(10)   NOT NULL,
   count  int(10)   NOT NULL,
  PRIMARY KEY ( fid, type, id, module )
)  ;

CREATE TABLE  filter  (
   format  varchar(255) NOT NULL,
   module  varchar(64) NOT NULL,
   name  varchar(32) NOT NULL,
   weight  int(11) NOT NULL,
   status  int(11) NOT NULL,
   settings  varchar(255),
  PRIMARY KEY ( format, name )
)  ;

CREATE TABLE  filter_format  (
   format  varchar(255) NOT NULL,
   name  varchar(255) NOT NULL,
   cache  tinyint(4) NOT NULL,
   status  tinyint(3)   NOT NULL,
   weight  int(11) NOT NULL,
  PRIMARY KEY ( format )
)  ;

CREATE TABLE  flood  (
   fid  int(11) NOT NULL,
   event  varchar(64) NOT NULL,
   identifier  varchar(128) NOT NULL,
   timestamp  int(11) NOT NULL,
   expiration  int(11) NOT NULL,
  PRIMARY KEY ( fid )
)  ;

CREATE TABLE  history  (
   uid  int(11) NOT NULL,
   nid  int(11) NOT NULL,
   timestamp  int(11) NOT NULL,
  PRIMARY KEY ( uid, nid )
)  ;

CREATE TABLE  image_effects  (
   ieid  int(10)   NOT NULL,
   isid  int(10)   NOT NULL,
   weight  int(11) NOT NULL,
   name  varchar(255) NOT NULL,
   data  varchar(255) NOT NULL,
  PRIMARY KEY ( ieid )
)  ;

CREATE TABLE  image_styles  (
   isid  int(10)   NOT NULL,
   name  varchar(255) NOT NULL,
   label  varchar(255) NOT NULL,
  PRIMARY KEY ( isid )
)  ;

CREATE TABLE  menu_custom  (
   menu_name  varchar(32) NOT NULL,
   title  varchar(255) NOT NULL,
   description  varchar(255),
  PRIMARY KEY ( menu_name )
)  ;

CREATE TABLE  menu_links  (
   menu_name  varchar(32) NOT NULL,
   mlid  int(10)   NOT NULL,
   plid  int(10)   NOT NULL,
   link_path  varchar(255) NOT NULL,
   router_path  varchar(255) NOT NULL,
   link_title  varchar(255) NOT NULL,
   options  varchar(255),
   module  varchar(255) NOT NULL,
   hidden  smallint(6) NOT NULL,
   external  smallint(6) NOT NULL,
   has_children  smallint(6) NOT NULL,
   expanded  smallint(6) NOT NULL,
   weight  int(11) NOT NULL,
   depth  smallint(6) NOT NULL,
   customized  smallint(6) NOT NULL,
   p1  int(10)   NOT NULL,
   p2  int(10)   NOT NULL,
   p3  int(10)   NOT NULL,
   p4  int(10)   NOT NULL,
   p5  int(10)   NOT NULL,
   p6  int(10)   NOT NULL,
   p7  int(10)   NOT NULL,
   p8  int(10)   NOT NULL,
   p9  int(10)   NOT NULL,
   updated  smallint(6) NOT NULL,
  PRIMARY KEY ( mlid )
)  ;

CREATE TABLE  MYNODE  (
   nid  int(10)   NOT NULL,
   vid  int(10)     NULL,
   type  varchar(32) NOT NULL,
   language  varchar(12) NOT NULL,
   title  varchar(255) NOT NULL,
   uid  int(11) NOT NULL,
   status  int(11) NOT NULL,
   created  int(11) NOT NULL,
   changed  int(11) NOT NULL,
   MYCOMMENT_MYNODE   int(11) NOT NULL,
   promote  int(11) NOT NULL,
   sticky  int(11) NOT NULL,
   tnid  int(10)   NOT NULL,
   translate  int(11) NOT NULL,
  PRIMARY KEY ( nid )
)  ;

CREATE TABLE  MYNODE_access  (
   nid  int(10)   NOT NULL,
   gid  int(10)   NOT NULL,
   realm  varchar(255) NOT NULL,
   grant_view  tinyint(3)   NOT NULL,
   grant_update  tinyint(3)   NOT NULL,
   grant_delete  tinyint(3)   NOT NULL,
  PRIMARY KEY ( nid, gid, realm )
)  ;

CREATE TABLE  MYNODE_MYCOMMENT_statistics  (
   nid  int(10)   NOT NULL,
   cid  int(11) NOT NULL,
   last_MYCOMMENT_timestamp  int(11) NOT NULL,
   last_MYCOMMENT_name  varchar(60)   NULL,
   last_MYCOMMENT_uid  int(11) NOT NULL,
    MYCOMMENT_count  int(10)   NOT NULL,
  PRIMARY KEY ( nid )
)  ;

CREATE TABLE  MYNODE_revision  (
   nid  int(10)   NOT NULL,
   vid  int(10)   NOT NULL,
   uid  int(11) NOT NULL,
   title  varchar(255) NOT NULL,
   log  varchar(255)  NOT NULL,
   timestamp  int(11) NOT NULL,
   status  int(11) NOT NULL,
   MYCOMMENT   int(11) NOT NULL,
   promote  int(11) NOT NULL,
   sticky  int(11) NOT NULL,
  PRIMARY KEY ( vid )
)  ;

CREATE TABLE  MYNODE_type  (
   type  varchar(32) NOT NULL,
   name  varchar(255) NOT NULL,
   base  varchar(255) NOT NULL,
   module  varchar(255) NOT NULL,
   description  varchar(255)  NOT NULL,
   help  varchar(255)  NOT NULL,
   has_title  tinyint(3)   NOT NULL,
   title_label  varchar(255) NOT NULL,
   custom  tinyint(4) NOT NULL,
   modified  tinyint(4) NOT NULL,
   locked  tinyint(4) NOT NULL,
   disabled  tinyint(4) NOT NULL,
   orig_type  varchar(255) NOT NULL,
  PRIMARY KEY ( type )
)  ;

CREATE TABLE  queue  (
   item_id  int(10)   NOT NULL,
   name  varchar(255) NOT NULL,
   data  varchar(255),
   expire  int(11) NOT NULL,
   created  int(11) NOT NULL,
  PRIMARY KEY ( item_id )
)   ;

CREATE TABLE  rdf_mapping  (
   type  varchar(128) NOT NULL,
   bundle  varchar(128) NOT NULL,
   mapping  varchar(255),
  PRIMARY KEY ( type, bundle )
)  ;

CREATE TABLE  registry  (
   name  varchar(255) NOT NULL,
   type  varchar(9) NOT NULL,
   filename  varchar(255) NOT NULL,
   module  varchar(255) NOT NULL,
   weight  int(11) NOT NULL,
  PRIMARY KEY ( name, type )
)  ;

CREATE TABLE  registry_file  (
   filename  varchar(255) NOT NULL,
   hash  varchar(64) NOT NULL,
  PRIMARY KEY ( filename )
)  ;

CREATE TABLE  role  (
   rid  int(10)   NOT NULL,
   name  varchar(64) NOT NULL,
   weight  int(11) NOT NULL,
  PRIMARY KEY ( rid )
)  ;

CREATE TABLE  role_permission  (
   rid  int(10)   NOT NULL,
   permission  varchar(128) NOT NULL,
   module  varchar(255) NOT NULL,
  PRIMARY KEY ( rid, permission )
)  ;

CREATE TABLE  search_dataset  (
   sid  int(10)   NOT NULL,
   type  varchar(16) NOT NULL,
   data  varchar(255)  NOT NULL,
   reindex  int(10)   NOT NULL,
  PRIMARY KEY ( sid, type )
)  ;

CREATE TABLE  search_index  (
   word  varchar(50) NOT NULL,
   sid  int(10)   NOT NULL,
   type  varchar(16) NOT NULL,
   score  float   NULL,
  PRIMARY KEY ( word, sid, type )
)  ;

CREATE TABLE  search_MYNODE_links  (
   sid  int(10)   NOT NULL,
   type  varchar(16) NOT NULL,
   nid  int(10)   NOT NULL,
   caption  varchar(255),
  PRIMARY KEY ( sid, type, nid )
)  ;

CREATE TABLE  search_total  (
   word  varchar(50) NOT NULL,
   count  float   NULL,
  PRIMARY KEY ( word )
)  ;

CREATE TABLE  semaphore  (
   name  varchar(255) NOT NULL,
   value  varchar(255) NOT NULL,
   expire  double NOT NULL,
  PRIMARY KEY ( name )
)  ;

CREATE TABLE  sequences  (
   value  int(10)   NOT NULL,
  PRIMARY KEY ( value )
) ;

CREATE TABLE  sessions  (
   uid  int(10)   NOT NULL,
   sid  varchar(128) NOT NULL,
   ssid  varchar(128) NOT NULL,
   hostname  varchar(128) NOT NULL,
   timestamp  int(11) NOT NULL,
   cache  int(11) NOT NULL,
   session  varchar(255),
  PRIMARY KEY ( sid, ssid )
)  ;

CREATE TABLE  shortcut_set  (
   set_name  varchar(32) NOT NULL,
   title  varchar(255) NOT NULL,
  PRIMARY KEY ( set_name )
)  ;

CREATE TABLE  shortcut_set_users  (
   uid  int(10)   NOT NULL,
   set_name  varchar(32) NOT NULL,
  PRIMARY KEY ( uid )
)  ;

CREATE TABLE  system  (
   filename  varchar(255) NOT NULL,
   name  varchar(255) NOT NULL,
   type  varchar(12) NOT NULL,
   owner  varchar(255) NOT NULL,
   status  int(11) NOT NULL,
   bootstrap  int(11) NOT NULL,
   schema_version  smallint(6) NOT NULL,
   weight  int(11) NOT NULL,
   info  varchar(255),
  PRIMARY KEY ( filename )
)  ;

CREATE TABLE  taxonomy_index  (
   nid  int(10)   NOT NULL,
   tid  int(10)   NOT NULL,
   sticky  tinyint(4),
   created  int(11) NOT NULL
)  ;

CREATE TABLE  taxonomy_term_data  (
   tid  int(10)   NOT NULL,
   vid  int(10)   NOT NULL,
   name  varchar(255) NOT NULL,
   description  varchar(255),
   format  varchar(255)   NULL,
   weight  int(11) NOT NULL,
  PRIMARY KEY ( tid )
)  ;

CREATE TABLE  taxonomy_term_hierarchy  (
   tid  int(10)   NOT NULL,
   parent  int(10)   NOT NULL,
  PRIMARY KEY ( tid, parent )
)  ;

CREATE TABLE  taxonomy_vocabulary  (
   vid  int(10)   NOT NULL,
   name  varchar(255) NOT NULL,
   machine_name  varchar(255) NOT NULL,
   description  varchar(255),
   hierarchy  tinyint(3)   NOT NULL,
   module  varchar(255) NOT NULL,
   weight  int(11) NOT NULL,
  PRIMARY KEY ( vid )
);

CREATE TABLE  url_alias  (
   pid  int(10)   NOT NULL,
   source  varchar(255) NOT NULL,
   alias  varchar(255) NOT NULL,
   language  varchar(12) NOT NULL,
  PRIMARY KEY ( pid )
);

CREATE TABLE  users  (
   uid  int(10)   NOT NULL,
   name  varchar(60) NOT NULL,
   pass  varchar(128) NOT NULL,
   mail  varchar(254),
   theme  varchar(255) NOT NULL,
   signature  varchar(255) NOT NULL,
   signature_format  varchar(255)   NULL,
   created  int(11) NOT NULL,
   access  int(11) NOT NULL,
   login  int(11) NOT NULL,
   status  tinyint(4) NOT NULL,
   timezone  varchar(32)   NULL,
   language  varchar(12) NOT NULL,
   picture  int(11) NOT NULL,
   init  varchar(254),
   data  varchar(255),
  PRIMARY KEY ( uid )
);

CREATE TABLE  users_roles  (
   uid  int(10)   NOT NULL,
   rid  int(10)   NOT NULL,
  PRIMARY KEY ( uid, rid )
) ;

CREATE TABLE  variable  (
   name  varchar(128) NOT NULL,
   value  varchar(255) NOT NULL,
  PRIMARY KEY ( name )
)  ;

CREATE TABLE  watchdog  (
   wid  int(11) NOT NULL,
   uid  int(11) NOT NULL,
   type  varchar(64) NOT NULL,
   message  varchar(255)  NOT NULL,
   variables  varchar(255) NOT NULL,
   severity  tinyint(3)   NOT NULL,
   link  varchar(255),
   location  varchar(255)  NOT NULL,
   referer  varchar(255),
   hostname  varchar(128) NOT NULL,
   timestamp  int(11) NOT NULL,
  PRIMARY KEY ( wid )
);

