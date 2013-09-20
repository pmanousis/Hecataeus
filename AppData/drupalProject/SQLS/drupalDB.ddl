-- MySQL dump 10.13  Distrib 5.5.31, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: testdrupal
-- ------------------------------------------------------
-- Server version	5.5.31-0+wheezy1-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


--
-- Table structure for table menu_router
--

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
)

--
-- Table structure for table blocked_ips
--

CREATE TABLE blocked_ips (
	iid int(10),
	ip varchar(40) NOT NULL ,
	PRIMARY KEY (iid)
)


--
-- Table structure for table  actions 
--

-- DROP TABLE IF EXISTS  actions ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  actions  (
   aid  varchar(255) NOT NULL ,
   type  varchar(32) NOT NULL ,
   callback  varchar(255) NOT NULL,
   parameters   varchar(255) NOT NULL,
   label  varchar(255) NOT NULL,
  PRIMARY KEY ( aid )
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  authmap 
--

-- DROP TABLE IF EXISTS  authmap ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  authmap  (
   aid  int(10) NOT NULL ,
   uid  int(11) NOT NULL ,
   authname  varchar(128) NOT NULL ,
   module  varchar(128) NOT NULL ,
  PRIMARY KEY ( aid )/*,
  UNIQUE KEY  authname  ( authname )*/
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  batch 
--

-- DROP TABLE IF EXISTS  batch ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  batch  (
   bid  int(10)   NOT NULL     ,
   token  varchar(64) NOT NULL     ,
   timestamp  int(11) NOT NULL     ,
   batch  varchar(255)     ,
  PRIMARY KEY ( bid )/*,
  KEY  token  ( token )*/
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  block 
--

-- DROP TABLE IF EXISTS  block ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  block  (
   bid  int(11) NOT NULL ,
   module  varchar(64) NOT NULL ,
   delta  varchar(32) NOT NULL ,
   theme  varchar(64) NOT NULL ,
   status  tinyint(4) NOT NULL ,
   weight  int(11) NOT NULL ,
   region  varchar(64) NOT NULL ,
   custom  tinyint(4) NOT NULL ,
   visibility  tinyint(4) NOT NULL ,
   pages  varchar(255)  NOT NULL     ,
   title  varchar(64) NOT NULL ,
   cache  tinyint(4) NOT NULL ,
  PRIMARY KEY ( bid )/*,
  UNIQUE KEY  tmd  ( theme , module , delta ),
  KEY  list  ( theme , status , region , weight , module )*/
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  block_custom 
--

-- DROP TABLE IF EXISTS  block_custom ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  block_custom  (
   bid  int(10)   NOT NULL ,
   body  longvarchar(255)      ,
   info  varchar(128) NOT NULL ,
   format  varchar(255) ,
  PRIMARY KEY ( bid )/*,
  UNIQUE KEY  info  ( info )*/
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  block_node_type 
--

-- DROP TABLE IF EXISTS  block_node_type ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  block_node_type  (
   module  varchar(64) NOT NULL ,
   delta  varchar(32) NOT NULL ,
   type  varchar(32) NOT NULL ,
  PRIMARY KEY ( module , delta , type )/*,
  KEY  type  ( type )*/
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  block_role 
--

-- DROP TABLE IF EXISTS  block_role ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  block_role  (
   module  varchar(64) NOT NULL ,
   delta  varchar(32) NOT NULL ,
   rid  int(10)   NOT NULL ,
  PRIMARY KEY ( module , delta , rid )/*,
  KEY  rid  ( rid )*/
);
/*!40101 SET character_set_client = @saved_cs_client */

--
-- Table structure for table  blocked_ips 
--



--
-- Table structure for table  cache 
--

-- DROP TABLE IF EXISTS  cache ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  cache  (
   cid  varchar(255) NOT NULL ,
   data  varchar(255)     ,
   expire  int(11) NOT NULL ,
   created  int(11) NOT NULL ,
   serialized  smallint(6) NOT NULL ,
  PRIMARY KEY ( cid )/*,
  KEY  expire  ( expire )*/
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  cache_block 
--

-- DROP TABLE IF EXISTS  cache_block ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  cache_block  (
   cid  varchar(255) NOT NULL ,
   data  varchar(255)     ,
   expire  int(11) NOT NULL ,
   created  int(11) NOT NULL ,
   serialized  smallint(6) NOT NULL ,
  PRIMARY KEY ( cid )/*,
  KEY  expire  ( expire )*/
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  cache_bootstrap 
--

-- DROP TABLE IF EXISTS  cache_bootstrap ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  cache_bootstrap  (
   cid  varchar(255) NOT NULL ,
   data  varchar(255)     ,
   expire  int(11) NOT NULL ,
   created  int(11) NOT NULL ,
   serialized  smallint(6) NOT NULL ,
  PRIMARY KEY ( cid )/*,
  KEY  expire  ( expire )*/
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  cache_field 
--

-- DROP TABLE IF EXISTS  cache_field ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  cache_field  (
   cid  varchar(255) NOT NULL          ,
   data  varchar(255)     ,
   expire  int(11) NOT NULL          ,
   created  int(11) NOT NULL          ,
   serialized  smallint(6) NOT NULL          ,
  PRIMARY KEY ( cid )/*,
  KEY  expire  ( expire )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  cache_filter 
--

-- DROP TABLE IF EXISTS  cache_filter ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  cache_filter  (
   cid  varchar(255) NOT NULL          ,
   data  varchar(255)     ,
   expire  int(11) NOT NULL          ,
   created  int(11) NOT NULL          ,
   serialized  smallint(6) NOT NULL          ,
  PRIMARY KEY ( cid )/*,
  KEY  expire  ( expire )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  cache_form 
--

-- DROP TABLE IF EXISTS  cache_form ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  cache_form  (
   cid  varchar(255) NOT NULL          ,
   data  varchar(255)     ,
   expire  int(11) NOT NULL          ,
   created  int(11) NOT NULL          ,
   serialized  smallint(6) NOT NULL          ,
  PRIMARY KEY ( cid )/*,
  KEY  expire  ( expire )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  cache_image 
--

-- DROP TABLE IF EXISTS  cache_image ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  cache_image  (
   cid  varchar(255) NOT NULL          ,
   data  varchar(255)     ,
   expire  int(11) NOT NULL          ,
   created  int(11) NOT NULL          ,
   serialized  smallint(6) NOT NULL          ,
  PRIMARY KEY ( cid )/*,
  KEY  expire  ( expire )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  cache_menu 
--

-- DROP TABLE IF EXISTS  cache_menu ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  cache_menu  (
   cid  varchar(255) NOT NULL          ,
   data  varchar(255)     ,
   expire  int(11) NOT NULL          ,
   created  int(11) NOT NULL          ,
   serialized  smallint(6) NOT NULL          ,
  PRIMARY KEY ( cid )/*,
  KEY  expire  ( expire )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  cache_page 
--

-- DROP TABLE IF EXISTS  cache_page ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  cache_page  (
   cid  varchar(255) NOT NULL          ,
   data  varchar(255)     ,
   expire  int(11) NOT NULL          ,
   created  int(11) NOT NULL          ,
   serialized  smallint(6) NOT NULL          ,
  PRIMARY KEY ( cid )/*,
  KEY  expire  ( expire )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  cache_path 
--

-- DROP TABLE IF EXISTS  cache_path ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  cache_path  (
   cid  varchar(255) NOT NULL          ,
   data  varchar(255)     ,
   expire  int(11) NOT NULL          ,
   created  int(11) NOT NULL          ,
   serialized  smallint(6) NOT NULL          ,
  PRIMARY KEY ( cid )/*,
  KEY  expire  ( expire )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  cache_update 
--

-- DROP TABLE IF EXISTS  cache_update ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  cache_update  (
   cid  varchar(255) NOT NULL          ,
   data  varchar(255)     ,
   expire  int(11) NOT NULL          ,
   created  int(11) NOT NULL          ,
   serialized  smallint(6) NOT NULL          ,
  PRIMARY KEY ( cid )/*,
  KEY  expire  ( expire )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table    
--

-- DROP TABLE IF EXISTS    ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE   comment  (
   cid  int(11) NOT NULL       ,
   pid  int(11) NOT NULL          ,
   nid  int(11) NOT NULL          ,
   uid  int(11) NOT NULL          ,
   subject  varchar(64) NOT NULL          ,
   hostname  varchar(128) NOT NULL          ,
   created  int(11) NOT NULL          ,
   changed  int(11) NOT NULL          ,
   status  tinyint(3)   NOT NULL          ,
   thread  varchar(255) NOT NULL     ,
   name  varchar(60)   NULL     ,
   mail  varchar(64)   NULL     ,
   homepage  varchar(255)   NULL     ,
   language  varchar(12) NOT NULL          ,
  PRIMARY KEY ( cid )/*,
  KEY  comment _status_pid  ( pid , status ),
  KEY comment  _num_new  ( nid , status , created , cid , thread ),
  KEY  comment _uid  ( uid ),
  KEY comment  _nid_language  ( nid , language ),
  KEY comment  _created  ( created )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  date_format_locale 
--

-- DROP TABLE IF EXISTS  date_format_locale ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  date_format_locale  (
   format  varchar(100) NOT NULL     ,
   type  varchar(64) NOT NULL     ,
   language  varchar(12) NOT NULL     ,
  PRIMARY KEY ( type , language )
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  date_format_type 
--

-- DROP TABLE IF EXISTS  date_format_type ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  date_format_type  (
   type  varchar(64) NOT NULL     ,
   title  varchar(255) NOT NULL     ,
   locked  tinyint(4) NOT NULL          ,
  PRIMARY KEY ( type )/*,
  KEY  title  ( title )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  date_formats 
--

-- DROP TABLE IF EXISTS  date_formats ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  date_formats  (
   dfid  int(10)   NOT NULL       ,
   format  varchar(100)  ,
   type  varchar(64) NOT NULL     ,
   locked  tinyint(4) NOT NULL          ,
  PRIMARY KEY ( dfid )/*,
  UNIQUE KEY  formats  ( format , type )*/
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  field_config 
--

-- DROP TABLE IF EXISTS  field_config ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  field_config  (
   id  int(11) NOT NULL       ,
   field_name  varchar(32) NOT NULL     ,
   type  varchar(128) NOT NULL     ,
   module  varchar(128) NOT NULL          ,
   active  tinyint(4) NOT NULL          ,
   storage_type  varchar(128) NOT NULL     ,
   storage_module  varchar(128) NOT NULL          ,
   storage_active  tinyint(4) NOT NULL          ,
   locked  tinyint(4) NOT NULL          ,
   data  varchar(255) NOT NULL     ,
   cardinality  tinyint(4) NOT NULL     ,
   translatable  tinyint(4) NOT NULL     ,
   deleted  tinyint(4) NOT NULL     ,
  PRIMARY KEY ( id )/*,
  KEY  field_name  ( field_name ),
  KEY  active  ( active ),
  KEY  storage_active  ( storage_active ),
  KEY  deleted  ( deleted ),
  KEY  module  ( module ),
  KEY  storage_module  ( storage_module ),
  KEY  type  ( type ),
  KEY  storage_type  ( storage_type )*/
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  field_config_instance 
--

-- DROP TABLE IF EXISTS  field_config_instance ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  field_config_instance  (
   id  int(11) NOT NULL       ,
   field_id  int(11) NOT NULL     ,
   field_name  varchar(32) NOT NULL     ,
   entity_type  varchar(32) NOT NULL     ,
   bundle  varchar(128) NOT NULL     ,
   data  varchar(255) NOT NULL,
   deleted  tinyint(4) NOT NULL     ,
  PRIMARY KEY ( id )/*,
  KEY  field_name_bundle  ( field_name , entity_type , bundle ),
  KEY  deleted  ( deleted )*/
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  field_data_body 
--

-- DROP TABLE IF EXISTS  field_data_body ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  field_data_body  (
   entity_type  varchar(128) NOT NULL          ,
   bundle  varchar(128) NOT NULL          ,
   deleted  tinyint(4) NOT NULL          ,
   entity_id  int(10)   NOT NULL     ,
   revision_id  int(10)     NULL     ,
   language  varchar(32) NOT NULL          ,
   delta  int(10)   NOT NULL     ,
   body_value  longvarchar(255) ,
   body_summary  longvarchar(255) ,
   body_format  varchar(255)   NULL,
  PRIMARY KEY ( entity_type , entity_id , deleted , delta , language )/*,
  KEY  entity_type  ( entity_type ),
  KEY  bundle  ( bundle ),
  KEY  deleted  ( deleted ),
  KEY  entity_id  ( entity_id ),
  KEY  revision_id  ( revision_id ),
  KEY  language  ( language ),
  KEY  body_format  ( body_format )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  field_data_ _body 
--

-- DROP TABLE IF EXISTS  field_data_ _body ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  field_data_comment_body  (
   entity_type  varchar(128) NOT NULL          ,
   bundle  varchar(128) NOT NULL          ,
   deleted  tinyint(4) NOT NULL          ,
   entity_id  int(10)   NOT NULL     ,
   revision_id  int(10)     NULL     ,
   language  varchar(32) NOT NULL          ,
   delta  int(10)   NOT NULL     ,
    comment_body_value  longvarchar(255) ,
    comment_body_format  varchar(255)   NULL,
  PRIMARY KEY ( entity_type , entity_id , deleted , delta , language )/*,
  KEY  entity_type  ( entity_type ),
  KEY  bundle  ( bundle ),
  KEY  deleted  ( deleted ),
  KEY  entity_id  ( entity_id ),
  KEY  revision_id  ( revision_id ),
  KEY  language  ( language ),
  KEY   _body_format  (  _body_format )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  field_data_field_image 
--

-- DROP TABLE IF EXISTS  field_data_field_image ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  field_data_field_image  (
   entity_type  varchar(128) NOT NULL          ,
   bundle  varchar(128) NOT NULL          ,
   deleted  tinyint(4) NOT NULL          ,
   entity_id  int(10)   NOT NULL     ,
   revision_id  int(10)     NULL     ,
   language  varchar(32) NOT NULL          ,
   delta  int(10)   NOT NULL     ,
   field_image_fid  int(10)     NULL     ,
   field_image_alt  varchar(512)   NULL     ,
   field_image_title  varchar(1024)   NULL     ,
   field_image_width  int(10)     NULL     ,
   field_image_height  int(10)     NULL     ,
  PRIMARY KEY ( entity_type , entity_id , deleted , delta , language )/*,
  KEY  entity_type  ( entity_type ),
  KEY  bundle  ( bundle ),
  KEY  deleted  ( deleted ),
  KEY  entity_id  ( entity_id ),
  KEY  revision_id  ( revision_id ),
  KEY  language  ( language ),
  KEY  field_image_fid  ( field_image_fid )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  field_data_field_tags 
--

-- DROP TABLE IF EXISTS  field_data_field_tags ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  field_data_field_tags  (
   entity_type  varchar(128) NOT NULL          ,
   bundle  varchar(128) NOT NULL          ,
   deleted  tinyint(4) NOT NULL          ,
   entity_id  int(10)   NOT NULL     ,
   revision_id  int(10)     NULL     ,
   language  varchar(32) NOT NULL          ,
   delta  int(10)   NOT NULL     ,
   field_tags_tid  int(10)     NULL,
  PRIMARY KEY ( entity_type , entity_id , deleted , delta , language )/*,
  KEY  entity_type  ( entity_type ),
  KEY  bundle  ( bundle ),
  KEY  deleted  ( deleted ),
  KEY  entity_id  ( entity_id ),
  KEY  revision_id  ( revision_id ),
  KEY  language  ( language ),
  KEY  field_tags_tid  ( field_tags_tid )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  field_revision_body 
--

-- DROP TABLE IF EXISTS  field_revision_body ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  field_revision_body  (
   entity_type  varchar(128) NOT NULL          ,
   bundle  varchar(128) NOT NULL          ,
   deleted  tinyint(4) NOT NULL          ,
   entity_id  int(10)   NOT NULL     ,
   revision_id  int(10)   NOT NULL     ,
   language  varchar(32) NOT NULL          ,
   delta  int(10)   NOT NULL     ,
   body_value  longvarchar(255) ,
   body_summary  longvarchar(255) ,
   body_format  varchar(255)   NULL,
  PRIMARY KEY ( entity_type , entity_id , revision_id , deleted , delta , language )/*,
  KEY  entity_type  ( entity_type ),
  KEY  bundle  ( bundle ),
  KEY  deleted  ( deleted ),
  KEY  entity_id  ( entity_id ),
  KEY  revision_id  ( revision_id ),
  KEY  language  ( language ),
  KEY  body_format  ( body_format )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  field_revision_ _body 
--

-- DROP TABLE IF EXISTS  field_revision_ _body ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  field_revision_comment_body  (
   entity_type  varchar(128) NOT NULL          ,
   bundle  varchar(128) NOT NULL          ,
   deleted  tinyint(4) NOT NULL          ,
   entity_id  int(10)   NOT NULL     ,
   revision_id  int(10)   NOT NULL     ,
   language  varchar(32) NOT NULL          ,
   delta  int(10)   NOT NULL     ,
    comment_body_value  longvarchar(255) ,
    comment_body_format  varchar(255)   NULL,
  PRIMARY KEY ( entity_type , entity_id , revision_id , deleted , delta , language )/*,
  KEY  entity_type  ( entity_type ),
  KEY  bundle  ( bundle ),
  KEY  deleted  ( deleted ),
  KEY  entity_id  ( entity_id ),
  KEY  revision_id  ( revision_id ),
  KEY  language  ( language ),
  KEY   _body_format  (  _body_format )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  field_revision_field_image 
--

-- DROP TABLE IF EXISTS  field_revision_field_image ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  field_revision_field_image  (
   entity_type  varchar(128) NOT NULL          ,
   bundle  varchar(128) NOT NULL          ,
   deleted  tinyint(4) NOT NULL          ,
   entity_id  int(10)   NOT NULL     ,
   revision_id  int(10)   NOT NULL     ,
   language  varchar(32) NOT NULL          ,
   delta  int(10)   NOT NULL     ,
   field_image_fid  int(10)     NULL     ,
   field_image_alt  varchar(512)   NULL     ,
   field_image_title  varchar(1024)   NULL     ,
   field_image_width  int(10)     NULL     ,
   field_image_height  int(10)     NULL     ,
  PRIMARY KEY ( entity_type , entity_id , revision_id , deleted , delta , language )/*,
  KEY  entity_type  ( entity_type ),
  KEY  bundle  ( bundle ),
  KEY  deleted  ( deleted ),
  KEY  entity_id  ( entity_id ),
  KEY  revision_id  ( revision_id ),
  KEY  language  ( language ),
  KEY  field_image_fid  ( field_image_fid )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  field_revision_field_tags 
--

-- DROP TABLE IF EXISTS  field_revision_field_tags ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  field_revision_field_tags  (
   entity_type  varchar(128) NOT NULL          ,
   bundle  varchar(128) NOT NULL          ,
   deleted  tinyint(4) NOT NULL          ,
   entity_id  int(10)   NOT NULL     ,
   revision_id  int(10)   NOT NULL     ,
   language  varchar(32) NOT NULL          ,
   delta  int(10)   NOT NULL     ,
   field_tags_tid  int(10)     NULL,
  PRIMARY KEY ( entity_type , entity_id , revision_id , deleted , delta , language )/*,
  KEY  entity_type  ( entity_type ),
  KEY  bundle  ( bundle ),
  KEY  deleted  ( deleted ),
  KEY  entity_id  ( entity_id ),
  KEY  revision_id  ( revision_id ),
  KEY  language  ( language ),
  KEY  field_tags_tid  ( field_tags_tid )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  file_managed 
--

-- DROP TABLE IF EXISTS  file_managed ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  file_managed  (
   fid  int(10)   NOT NULL       ,
   uid  int(10)   NOT NULL          ,
   filename  varchar(255) NOT NULL          ,
   uri  varchar(255)  ,
   filemime  varchar(255) NOT NULL          ,
   filesize  int(10)   NOT NULL          ,
   status  tinyint(4) NOT NULL          ,
   timestamp  int(10)   NOT NULL          ,
  PRIMARY KEY ( fid )/*,
  UNIQUE KEY  uri  ( uri ),
  KEY  uid  ( uid ),
  KEY  status  ( status ),
  KEY  timestamp  ( timestamp )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  file_usage 
--

-- DROP TABLE IF EXISTS  file_usage ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  file_usage  (
   fid  int(10)   NOT NULL     ,
   module  varchar(255) NOT NULL          ,
   type  varchar(64) NOT NULL          ,
   id  int(10)   NOT NULL          ,
   count  int(10)   NOT NULL          ,
  PRIMARY KEY ( fid , type , id , module )/*,
  KEY  type_id  ( type , id ),
  KEY  fid_count  ( fid , count ),
  KEY  fid_module  ( fid , module )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  filter 
--

-- DROP TABLE IF EXISTS  filter ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  filter  (
   format  varchar(255) NOT NULL     ,
   module  varchar(64) NOT NULL          ,
   name  varchar(32) NOT NULL          ,
   weight  int(11) NOT NULL          ,
   status  int(11) NOT NULL          ,
   settings  varchar(255)     ,
  PRIMARY KEY ( format , name )/*,
  KEY  list  ( weight , module , name )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  filter_format 
--

-- DROP TABLE IF EXISTS  filter_format ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  filter_format  (
   format  varchar(255) NOT NULL     ,
   name  varchar(255) NOT NULL          ,
   cache  tinyint(4) NOT NULL          ,
   status  tinyint(3)   NOT NULL          ,
   weight  int(11) NOT NULL          ,
  PRIMARY KEY ( format )/*,
  UNIQUE KEY  name  ( name ),
  KEY  status_weight  ( status , weight )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  flood 
--

-- DROP TABLE IF EXISTS  flood ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  flood  (
   fid  int(11) NOT NULL       ,
   event  varchar(64) NOT NULL          ,
   identifier  varchar(128) NOT NULL          ,
   timestamp  int(11) NOT NULL          ,
   expiration  int(11) NOT NULL          ,
  PRIMARY KEY ( fid )/*,
  KEY  allow  ( event , identifier , timestamp ),
  KEY  purge  ( expiration )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  history 
--

-- DROP TABLE IF EXISTS  history ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  history  (
   uid  int(11) NOT NULL          ,
   nid  int(11) NOT NULL          ,
   timestamp  int(11) NOT NULL          ,
  PRIMARY KEY ( uid , nid )/*,
  KEY  nid  ( nid )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  image_effects 
--

-- DROP TABLE IF EXISTS  image_effects ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  image_effects  (
   ieid  int(10)   NOT NULL       ,
   isid  int(10)   NOT NULL          ,
   weight  int(11) NOT NULL          ,
   name  varchar(255) NOT NULL     ,
   data  varchar(255) NOT NULL     ,
  PRIMARY KEY ( ieid )/*,
  KEY  isid  ( isid ),
  KEY  weight  ( weight )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  image_styles 
--

-- DROP TABLE IF EXISTS  image_styles ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  image_styles  (
   isid  int(10)   NOT NULL       ,
   name  varchar(255) NOT NULL     ,
   label  varchar(255) NOT NULL          ,
  PRIMARY KEY ( isid )/*,
  UNIQUE KEY  name  ( name )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  menu_custom 
--

-- DROP TABLE IF EXISTS  menu_custom ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  menu_custom  (
   menu_name  varchar(32) NOT NULL          ,
   title  varchar(255) NOT NULL          ,
   description  varchar(255)      ,
  PRIMARY KEY ( menu_name )
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  menu_links 
--

-- DROP TABLE IF EXISTS  menu_links ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  menu_links  (
   menu_name  varchar(32) NOT NULL          ,
   mlid  int(10)   NOT NULL       ,
   plid  int(10)   NOT NULL          ,
   link_path  varchar(255) NOT NULL          ,
   router_path  varchar(255) NOT NULL          ,
   link_title  varchar(255) NOT NULL          ,
   options  varchar(255)     ,
   module  varchar(255) NOT NULL          ,
   hidden  smallint(6) NOT NULL          ,
   external  smallint(6) NOT NULL          ,
   has_children  smallint(6) NOT NULL          ,
   expanded  smallint(6) NOT NULL          ,
   weight  int(11) NOT NULL          ,
   depth  smallint(6) NOT NULL          ,
   customized  smallint(6) NOT NULL          ,
   p1  int(10)   NOT NULL          ,
   p2  int(10)   NOT NULL          ,
   p3  int(10)   NOT NULL          ,
   p4  int(10)   NOT NULL          ,
   p5  int(10)   NOT NULL          ,
   p6  int(10)   NOT NULL          ,
   p7  int(10)   NOT NULL          ,
   p8  int(10)   NOT NULL          ,
   p9  int(10)   NOT NULL          ,
   updated  smallint(6) NOT NULL          ,
  PRIMARY KEY ( mlid )/*,
  KEY  path_menu  ( link_path (128), menu_name ),
  KEY  menu_plid_expand_child  ( menu_name , plid , expanded , has_children ),
  KEY  menu_parents  ( menu_name , p1 , p2 , p3 , p4 , p5 , p6 , p7 , p8 , p9 ),
  KEY  router_path  ( router_path (128))*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  menu_router 
--

-- DROP TABLE IF EXISTS  menu_router ;


--
-- Table structure for table  node 
--

-- DROP TABLE IF EXISTS  node ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  node  (
   nid  int(10)   NOT NULL       ,
   vid  int(10)     NULL     ,
   type  varchar(32) NOT NULL          ,
   language  varchar(12) NOT NULL          ,
   title  varchar(255) NOT NULL          ,
   uid  int(11) NOT NULL          ,
   status  int(11) NOT NULL          ,
   created  int(11) NOT NULL          ,
   changed  int(11) NOT NULL          ,
   comment   int(11) NOT NULL          ,
   promote  int(11) NOT NULL          ,
   sticky  int(11) NOT NULL          ,
   tnid  int(10)   NOT NULL          ,
   translate  int(11) NOT NULL          ,
  PRIMARY KEY ( nid )/*,
  UNIQUE KEY  vid  ( vid ),
  KEY  node_changed  ( changed ),
  KEY  node_created  ( created ),
  KEY  node_frontpage  ( promote , status , sticky , created ),
  KEY  node_status_type  ( status , type , nid ),
  KEY  node_title_type  ( title , type (4)),
  KEY  node_type  ( type (4)),
  KEY  uid  ( uid ),
  KEY  tnid  ( tnid ),
  KEY  translate  ( translate )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  node_access 
--

-- DROP TABLE IF EXISTS  node_access ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  node_access  (
   nid  int(10)   NOT NULL          ,
   gid  int(10)   NOT NULL          ,
   realm  varchar(255) NOT NULL          ,
   grant_view  tinyint(3)   NOT NULL          ,
   grant_update  tinyint(3)   NOT NULL          ,
   grant_delete  tinyint(3)   NOT NULL          ,
  PRIMARY KEY ( nid , gid , realm )
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  node_ _statistics 
--

-- DROP TABLE IF EXISTS  node_ _statistics ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  node_comment_statistics  (
   nid  int(10)   NOT NULL          ,
   cid  int(11) NOT NULL          ,
   last_comment_timestamp  int(11) NOT NULL          ,
   last_comment_name  varchar(60)   NULL     ,
   last_comment_uid  int(11) NOT NULL          ,
    comment_count  int(10)   NOT NULL          ,
  PRIMARY KEY ( nid )/*,
  KEY  node_ _timestamp  ( last_ _timestamp ),
  KEY   _count  (  _count ),
  KEY  last_ _uid  ( last_ _uid )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  node_revision 
--

-- DROP TABLE IF EXISTS  node_revision ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  node_revision  (
   nid  int(10)   NOT NULL          ,
   vid  int(10)   NOT NULL       ,
   uid  int(11) NOT NULL          ,
   title  varchar(255) NOT NULL          ,
   log  longvarchar(255)  NOT NULL     ,
   timestamp  int(11) NOT NULL          ,
   status  int(11) NOT NULL          ,
   comment   int(11) NOT NULL          ,
   promote  int(11) NOT NULL          ,
   sticky  int(11) NOT NULL          ,
  PRIMARY KEY ( vid )/*,
  KEY  nid  ( nid ),
  KEY  uid  ( uid )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  node_type 
--

-- DROP TABLE IF EXISTS  node_type ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  node_type  (
   type  varchar(32) NOT NULL     ,
   name  varchar(255) NOT NULL          ,
   base  varchar(255) NOT NULL     ,
   module  varchar(255) NOT NULL     ,
   description  varchar(255)  NOT NULL     ,
   help  varchar(255)  NOT NULL     ,
   has_title  tinyint(3)   NOT NULL     ,
   title_label  varchar(255) NOT NULL          ,
   custom  tinyint(4) NOT NULL          ,
   modified  tinyint(4) NOT NULL          ,
   locked  tinyint(4) NOT NULL          ,
   disabled  tinyint(4) NOT NULL          ,
   orig_type  varchar(255) NOT NULL          ,
  PRIMARY KEY ( type )
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  queue 
--

-- DROP TABLE IF EXISTS  queue ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  queue  (
   item_id  int(10)   NOT NULL       ,
   name  varchar(255) NOT NULL          ,
   data  varchar(255)     ,
   expire  int(11) NOT NULL          ,
   created  int(11) NOT NULL          ,
  PRIMARY KEY ( item_id )/*,
  KEY  name_created  ( name , created ),
  KEY  expire  ( expire )*/
)   ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  rdf_mapping 
--

-- DROP TABLE IF EXISTS  rdf_mapping ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  rdf_mapping  (
   type  varchar(128) NOT NULL     ,
   bundle  varchar(128) NOT NULL     ,
   mapping  varchar(255)     ,
  PRIMARY KEY ( type , bundle )
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  registry 
--

-- DROP TABLE IF EXISTS  registry ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  registry  (
   name  varchar(255) NOT NULL          ,
   type  varchar(9) NOT NULL          ,
   filename  varchar(255) NOT NULL     ,
   module  varchar(255) NOT NULL          ,
   weight  int(11) NOT NULL          ,
  PRIMARY KEY ( name , type )/*,
  KEY  hook  ( type , weight , module )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  registry_file 
--

-- DROP TABLE IF EXISTS  registry_file ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  registry_file  (
   filename  varchar(255) NOT NULL     ,
   hash  varchar(64) NOT NULL     ,
  PRIMARY KEY ( filename )
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  role 
--

-- DROP TABLE IF EXISTS  role ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  role  (
   rid  int(10)   NOT NULL       ,
   name  varchar(64) NOT NULL          ,
   weight  int(11) NOT NULL          ,
  PRIMARY KEY ( rid )/*,
  UNIQUE KEY  name  ( name ),
  KEY  name_weight  ( name , weight )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  role_permission 
--

-- DROP TABLE IF EXISTS  role_permission ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  role_permission  (
   rid  int(10)   NOT NULL     ,
   permission  varchar(128) NOT NULL          ,
   module  varchar(255) NOT NULL          ,
  PRIMARY KEY ( rid , permission )/*,
  KEY  permission  ( permission )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  search_dataset 
--

-- DROP TABLE IF EXISTS  search_dataset ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  search_dataset  (
   sid  int(10)   NOT NULL          ,
   type  varchar(16) NOT NULL     ,
   data  longvarchar(255)  NOT NULL     ,
   reindex  int(10)   NOT NULL          ,
  PRIMARY KEY ( sid , type )
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  search_index 
--

-- DROP TABLE IF EXISTS  search_index ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  search_index  (
   word  varchar(50) NOT NULL          ,
   sid  int(10)   NOT NULL          ,
   type  varchar(16) NOT NULL     ,
   score  float   NULL     ,
  PRIMARY KEY ( word , sid , type )/*,
  KEY  sid_type  ( sid , type )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  search_node_links 
--

-- DROP TABLE IF EXISTS  search_node_links ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  search_node_links  (
   sid  int(10)   NOT NULL          ,
   type  varchar(16) NOT NULL          ,
   nid  int(10)   NOT NULL          ,
   caption  longvarchar(255)      ,
  PRIMARY KEY ( sid , type , nid )/*,
  KEY  nid  ( nid )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  search_total 
--

-- DROP TABLE IF EXISTS  search_total ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  search_total  (
   word  varchar(50) NOT NULL          ,
   count  float   NULL     ,
  PRIMARY KEY ( word )
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  semaphore 
--

-- DROP TABLE IF EXISTS  semaphore ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  semaphore  (
   name  varchar(255) NOT NULL          ,
   value  varchar(255) NOT NULL          ,
   expire  double NOT NULL     ,
  PRIMARY KEY ( name )/*,
  KEY  value  ( value ),
  KEY  expire  ( expire )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  sequences 
--

-- DROP TABLE IF EXISTS  sequences ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  sequences  (
   value  int(10)   NOT NULL       ,
  PRIMARY KEY ( value )
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  sessions 
--

-- DROP TABLE IF EXISTS  sessions ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  sessions  (
   uid  int(10)   NOT NULL     ,
   sid  varchar(128) NOT NULL     ,
   ssid  varchar(128) NOT NULL          ,
   hostname  varchar(128) NOT NULL          ,
   timestamp  int(11) NOT NULL          ,
   cache  int(11) NOT NULL          ,
   session  varchar(255)     ,
  PRIMARY KEY ( sid , ssid )/*,
  KEY  timestamp  ( timestamp ),
  KEY  uid  ( uid ),
  KEY  ssid  ( ssid )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  shortcut_set 
--

-- DROP TABLE IF EXISTS  shortcut_set ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  shortcut_set  (
   set_name  varchar(32) NOT NULL          ,
   title  varchar(255) NOT NULL          ,
  PRIMARY KEY ( set_name )
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  shortcut_set_users 
--

-- DROP TABLE IF EXISTS  shortcut_set_users ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  shortcut_set_users  (
   uid  int(10)   NOT NULL          ,
   set_name  varchar(32) NOT NULL          ,
  PRIMARY KEY ( uid )/*,
  KEY  set_name  ( set_name )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  system 
--

-- DROP TABLE IF EXISTS  system ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  system  (
   filename  varchar(255) NOT NULL          ,
   name  varchar(255) NOT NULL          ,
   type  varchar(12) NOT NULL          ,
   owner  varchar(255) NOT NULL          ,
   status  int(11) NOT NULL          ,
   bootstrap  int(11) NOT NULL          ,
   schema_version  smallint(6) NOT NULL          ,
   weight  int(11) NOT NULL          ,
   info  varchar(255)     ,
  PRIMARY KEY ( filename )/*,
  KEY  system_list  ( status , bootstrap , type , weight , name ),
  KEY  type_name  ( type , name )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  taxonomy_index 
--

-- DROP TABLE IF EXISTS  taxonomy_index ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  taxonomy_index  (
   nid  int(10)   NOT NULL          ,
   tid  int(10)   NOT NULL          ,
   sticky  tinyint(4)          ,
   created  int(11) NOT NULL         /* ,
  KEY  term_node  ( tid , sticky , created ),
  KEY  nid  ( nid )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  taxonomy_term_data 
--

-- DROP TABLE IF EXISTS  taxonomy_term_data ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  taxonomy_term_data  (
   tid  int(10)   NOT NULL       ,
   vid  int(10)   NOT NULL          ,
   name  varchar(255) NOT NULL          ,
   description  longvarchar(255)      ,
   format  varchar(255)   NULL     ,
   weight  int(11) NOT NULL          ,
  PRIMARY KEY ( tid )/*,
  KEY  taxonomy_tree  ( vid , weight , name ),
  KEY  vid_name  ( vid , name ),
  KEY  name  ( name )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  taxonomy_term_hierarchy 
--

-- DROP TABLE IF EXISTS  taxonomy_term_hierarchy ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  taxonomy_term_hierarchy  (
   tid  int(10)   NOT NULL          ,
   parent  int(10)   NOT NULL          ,
  PRIMARY KEY ( tid , parent )/*,
  KEY  parent  ( parent )*/
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  taxonomy_vocabulary 
--

-- DROP TABLE IF EXISTS  taxonomy_vocabulary ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  taxonomy_vocabulary  (
   vid  int(10)   NOT NULL ,
   name  varchar(255) NOT NULL ,
   machine_name  varchar(255) NOT NULL,
   description  longvarchar(255)  ,
   hierarchy  tinyint(3)   NOT NULL ,
   module  varchar(255) NOT NULL ,
   weight  int(11) NOT NULL,
  PRIMARY KEY ( vid )/*,
  UNIQUE KEY  machine_name  ( machine_name ),
  KEY  list  ( weight , name )*/
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  url_alias 
--

-- DROP TABLE IF EXISTS  url_alias ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  url_alias  (
   pid  int(10)   NOT NULL   ,
   source  varchar(255) NOT NULL,
   alias  varchar(255) NOT NULL ,
   language  varchar(12) NOT NULL ,
  PRIMARY KEY ( pid )/*,
  KEY  alias_language_pid  ( alias , language , pid ),
  KEY  source_language_pid  ( source , language , pid )*/
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  users 
--

-- DROP TABLE IF EXISTS  users ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  users  (
   uid  int(10)   NOT NULL,
   name  varchar(60) NOT NULL,
   pass  varchar(128) NOT NULL,
   mail  varchar(254) ,
   theme  varchar(255) NOT NULL          ,
   signature  varchar(255) NOT NULL          ,
   signature_format  varchar(255)   NULL     ,
   created  int(11) NOT NULL          ,
   access  int(11) NOT NULL          ,
   login  int(11) NOT NULL          ,
   status  tinyint(4) NOT NULL          ,
   timezone  varchar(32)   NULL     ,
   language  varchar(12) NOT NULL          ,
   picture  int(11) NOT NULL          ,
   init  varchar(254)          ,
   data  varchar(255)     ,
  PRIMARY KEY ( uid )/*,
  UNIQUE KEY  name  ( name ),
  KEY  access  ( access ),
  KEY  created  ( created ),
  KEY  mail  ( mail ),
  KEY  picture  ( picture )*/
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  users_roles 
--

-- DROP TABLE IF EXISTS  users_roles ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  users_roles  (
   uid  int(10)   NOT NULL          ,
   rid  int(10)   NOT NULL          ,
  PRIMARY KEY ( uid , rid )/*,
  KEY  rid  ( rid )*/
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  variable 
--

-- DROP TABLE IF EXISTS  variable ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  variable  (
   name  varchar(128) NOT NULL          ,
   value  varchar(255) NOT NULL     ,
  PRIMARY KEY ( name )
)  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table  watchdog 
--

-- DROP TABLE IF EXISTS  watchdog ;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE  watchdog  (
   wid  int(11) NOT NULL,
   uid  int(11) NOT NULL ,
   type  varchar(64) NOT NULL ,
   message  longvarchar(255)  NOT NULL ,
   variables  varchar(255) NOT NULL,
   severity  tinyint(3)   NOT NULL,
   link  varchar(255) ,
   location  varchar(255)  NOT NULL ,
   referer  varchar(255)  ,
   hostname  varchar(128) NOT NULL,
   timestamp  int(11) NOT NULL ,
  PRIMARY KEY ( wid )/*,
  KEY  type  ( type ),
  KEY  uid  ( uid ),
  KEY  severity  ( severity )*/
);
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-09-19 16:49:06
