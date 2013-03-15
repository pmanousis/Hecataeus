-- SQL to create the initial tables for the MediaWiki database.
-- This is read and executed by the install script; you should
-- not have to run it by itself unless doing a manual install.

-- This is a shared schema file used for both MySQL and SQLite installs.

--
-- General notes:
--
-- If possible, create tables as InnoDB to benefit from the
-- superior resiliency against crashes and ability to read
-- during writes (and write during reads!)
--
-- Only the 'searchindex' table requires MyISAM due to the
-- requirement for fulltext index support, which is missing
-- from InnoDB.
--
--
-- The MySQL table backend for MediaWiki currently uses
-- 14-character BINARY or VARBINARY fields to store timestamps.
-- The format is YYYYMMDDHHMMSS, which is derived from the
-- text format of MySQL's TIMESTAMP fields.
--
-- Historically TIMESTAMP fields were used, but abandoned
-- in early 2002 after a lot of trouble with the fields
-- auto-updating.
--
-- The Postgres backend uses DATETIME fields for timestamps,
-- and we will migrate the MySQL definitions at some point as
-- well.
--
--
-- The /*_*/ comments in this and other files are
-- replaced with the defined table prefix by the installer
-- and updater scripts. If you are installing or running
-- updates manually, you will need to manually insert the
-- table prefix if any when running these scripts.
--


--
-- The user table contains basic account information,
-- authentication keys, etc.
--
-- Some multi-wiki sites may share a single central user table
-- between separate wikis using the $wgSharedDB setting.
--
-- Note that when a external authentication plugin is used,
-- user table entries still need to be created to store
-- preferences and to key tracking information in the other
-- tables.
--
CREATE TABLE /*_*/user (
  user_id int NOT NULL PRIMARY KEY,
  
  -- Usernames must be unique, must not be in the form of
  -- an IP address. _Shouldn't_ allow slashes or case
  -- conflicts. Spaces are allowed, and are _not_ converted
  -- to underscores like titles. See the User::newFromName() for
  -- the specific tests that usernames have to pass.
  user_name varchar(255) NOT NULL,
  
  -- Optional 'real name' to be displayed in credit listings
  user_real_name varchar(255)  NOT NULL,
  
  -- Password hashes, normally hashed like so:
  -- MD5(CONCAT(user_id,'-',MD5(plaintext_password))), see
  -- wfEncryptPassword() in GlobalFunctions.php
  user_password varchar NOT NULL,
  
  -- When using 'mail me a new password', a random
  -- password is generated and the hash stored here.
  -- The previous password is left in place until
  -- someone actually logs in with the new password,
  -- at which point the hash is moved to user_password
  -- and the old password is invalidated.
  user_newpassword varchar NOT NULL,
  
  -- Timestamp of the last time when a new password was
  -- sent, for throttling purposes
  user_newpass_time varchar,

  -- Note: email should be restricted, not public info.
  -- Same with passwords.
  user_email varchar NOT NULL,
  
  -- Newline-separated list of name=value defining the user
  -- preferences
  -- Now obsolete in favour of user_properties table;
  -- old values will be migrated from here transparently.
  user_options varchar NOT NULL,
  
  -- This is a timestamp which is updated when a user
  -- logs in, logs out, changes preferences, or performs
  -- some other action requiring HTML cache invalidation
  -- to ensure that the UI is updated.
  user_touched varchar NOT NULL ,
  
  -- A pseudorandomly generated value that is stored in
  -- a cookie when the "remember password" feature is
  -- used (previously, a hash of the password was used, but
  -- this was vulnerable to cookie-stealing attacks)
  user_token varchar NOT NULL ,
  
  -- Initially NULL; when a user's e-mail address has been
  -- validated by returning with a mailed token, this is
  -- set to the current timestamp.
  user_email_authenticated varchar,
  
  -- Randomly generated token created when the e-mail address
  -- is set and a confirmation test mail sent.
  user_email_token varchar,
  
  -- Expiration date for the user_email_token
  user_email_token_expires varchar,
  
  -- Timestamp of account registration.
  -- Accounts predating this schema addition may contain NULL.
  user_registration varchar,
  
  -- Count of edits and edit-like actions.
  --
  -- *NOT* intended to be an accurate copy of COUNT(*) WHERE rev_user=user_id
  -- May contain NULL for old accounts if batch-update scripts haven't been
  -- run, as well as listing deleted edits and other myriad ways it could be
  -- out of sync.
  --
  -- Meant primarily for heuristic checks to give an impression of whether
  -- the account has been used much.
  --
  user_editcount int,
  CONSTRAINT uc_user_user_name UNIQUE (user_name)
)  ;

--CREATE UNIQUE INDEX /*i*/user_name ON /*_*/user (user_name);
--CREATE INDEX /*i*/user_email_token ON /*_*/user (user_email_token);

--
-- User permissions have been broken out to a separate table;
-- this allows sites with a shared user table to have different
-- permissions assigned to a user in each project.
--
-- This table replaces the old user_rights field which used a
-- comma-separated blob.
--
CREATE TABLE /*_*/user_groups (
  -- Key to user_id
  ug_user int NOT NULL ,
  
  -- Group names are short symbolic string keys.
  -- The set of group names is open-ended, though in practice
  -- only some predefined ones are likely to be used.
  --
  -- At runtime $wgGroupPermissions will associate group keys
  -- with particular permissions. A user will have the combined
  -- permissions of any group they're explicitly in, plus
  -- the implicit '*' and 'user' groups.
  ug_group varchar NOT NULL,
  CONSTRAINT uc_user_groups UNIQUE (ug_user,ug_group)
  
);


--CREATE UNIQUE INDEX /*i*/ug_user_group ON /*_*/user_groups (ug_user,ug_group);
--CREATE INDEX /*i*/ug_group ON /*_*/user_groups (ug_group);

--
-- Stores notifications of user talk page changes, for the display
-- of the "you have new messages" box
--
CREATE TABLE /*_*/user_newtalk (
  -- Key to user.user_id
  user_id int NOT NULL,
  -- If the user is an anonymous user their IP address is stored here
  -- since the user_id of 0 is ambiguous
  user_ip varchar(40) NOT NULL,
  -- The highest timestamp of revisions of the talk page viewed
  -- by this user
  user_last_timestamp varchar(14) NOT NULL
) /*$wgDBTableOptions*/;

-- Indexes renamed for SQLite in 1.14
--CREATE INDEX /*i*/un_user_id ON /*_*/user_newtalk (user_id);
--CREATE INDEX /*i*/un_user_ip ON /*_*/user_newtalk (user_ip);

--
-- User preferences and perhaps other fun stuff. :)
-- Replaces the old user.user_options blob, with a couple nice properties:
--
-- 1) We only store non-default settings, so changes to the defauls
--    are now reflected for everybody, not just new accounts.
-- 2) We can more easily do bulk lookups, statistics, or modifications of
--    saved options since it's a sane table structure.
--
CREATE TABLE /*_*/user_properties (
  -- Foreign key to user.user_id
  up_user int NOT NULL,
  
  -- Name of the option being saved. This is indexed for bulk lookup.
  up_property varchar(32) NOT NULL,
  
  -- Property value as a string.
  up_value varchar,
  
   CONSTRAINT uc_user_properties_user_property UNIQUE (up_user,up_property)
)  ;


--CREATE UNIQUE INDEX /*i*/user_properties_user_property ON /*_*/user_properties (up_user,up_property);
--CREATE INDEX /*i*/user_properties_property ON /*_*/user_properties (up_property);

--
-- Core of the wiki: each page has an entry here which identifies
-- it by title and contains some essential metadata.
--
CREATE TABLE /*_*/page (
  -- Unique identifier number. The page_id will be preserved across
  -- edits and rename operations, but not deletions and recreations.
  page_id int  NOT NULL PRIMARY KEY ,
  
  -- A page name is broken into a namespace and a title.
  -- The namespace keys are UI-language-independent constants,
  -- defined in includes/Defines.php
  page_namespace int NOT NULL,
  
  -- The rest of the title, as text.
  -- Spaces are transformed into underscores in title storage.
  page_title varchar(255)  NOT NULL,
  
  -- Comma-separated set of permission keys indicating who
  -- can move or edit the page.
  page_restrictions varchar NOT NULL,
  
  -- Number of times this page has been viewed.
  page_counter int   NOT NULL  ,
  
  -- 1 indicates the article is a redirect.
  page_is_redirect int   NOT NULL  ,
  
  -- 1 indicates this is a new entry, with only one edit.
  -- Not all pages with one edit are new pages.
  page_is_new int   NOT NULL  ,
  
  -- Random value between 0 and 1, used for Special:Randompage
  page_random int   NOT NULL,
  
  -- This timestamp is updated whenever the page changes in
  -- a way requiring it to be re-rendered, invalidating caches.
  -- Aside from editing this includes permission changes,
  -- creation or deletion of linked pages, and alteration
  -- of contained templates.
  page_touched varchar(14) NOT NULL  ,

  -- Handy key to revision.rev_id of the current revision.
  -- This may be 0 during page creation, but that shouldn't
  -- happen outside of a transaction... hopefully.
  page_latest int   NOT NULL,
  
  -- Uncompressed length in bytes of the page's current source text.
  page_len int   NOT NULL,
  
  
   CONSTRAINT uc_name_title UNIQUE (page_namespace,page_title)
) /*$wgDBTableOptions*/;
 
--CREATE UNIQUE INDEX /*i*/name_title ON /*_*/page (page_namespace,page_title);
--CREATE INDEX /*i*/page_random ON /*_*/page (page_random);
--CREATE INDEX /*i*/page_len ON /*_*/page (page_len);



--
-- Every edit of a page creates also a revision row.
-- This stores metadata about the revision, and a reference
-- to the text storage backend.
--
CREATE TABLE /*_*/revision (
  rev_id int   NOT NULL PRIMARY KEY  ,
  
  -- Key to page_id. This should _never_ be invalid.
  rev_page int   NOT NULL,
  
  -- Key to text.old_id, where the actual bulk text is stored.
  -- It's possible for multiple revisions to use the same text,
  -- for instance revisions where only metadata is altered
  -- or a rollback to a previous version.
  rev_text_id int   NOT NULL,
  
  -- Text comment summarizing the change.
  -- This text is shown in the history and other changes lists,
  -- rendered in a subset of wiki markup by Linker::formatComment()
  rev_comment varchar NOT NULL,
  
  -- Key to user.user_id of the user who made this edit.
  -- Stores 0 for anonymous edits and for some mass imports.
  rev_user int   NOT NULL  ,
  
  -- Text username or IP address of the editor.
  rev_user_text varchar(255)   NOT NULL  ,
  
  -- Timestamp
  rev_timestamp varchar(14) NOT NULL  ,
  
  -- Records whether the user marked the 'minor edit' checkbox.
  -- Many automated edits are marked as minor.
  rev_minor_edit int   NOT NULL  ,
  
  -- Not yet used; reserved for future changes to the deletion system.
  rev_deleted int   NOT NULL  ,
  
  -- Length of this revision in bytes
  rev_len int  ,

  -- Key to revision.rev_id
  -- This field is used to add support for a tree structure (The Adjacency List Model)
  rev_parent_id int  ,
  CONSTRAINT uc_rev_page_id UNIQUE (rev_page, rev_id)

)  ;

--CREATE UNIQUE INDEX /*i*/rev_page_id ON /*_*/revision (rev_page, rev_id);
--CREATE INDEX /*i*/rev_timestamp ON /*_*/revision (rev_timestamp);
--CREATE INDEX /*i*/page_timestamp ON /*_*/revision (rev_page,rev_timestamp);
--CREATE INDEX /*i*/user_timestamp ON /*_*/revision (rev_user,rev_timestamp);
--CREATE INDEX /*i*/usertext_timestamp ON /*_*/revision (rev_user_text,rev_timestamp);
 

--
-- Holds text of individual page revisions.
--
-- Field names are a holdover from the 'old' revisions table in
-- MediaWiki 1.4 and earlier: an upgrade will transform that
-- table into the 'text' table to minimize unnecessary churning
-- and downtime. If upgrading, the other fields will be left unused.
--
CREATE TABLE /*_*/text (
  -- Unique text storage key number.
  -- Note that the 'oldid' parameter used in URLs does *not*
  -- refer to this number anymore, but to rev_id.
  --
  -- revision.rev_text_id is a key to this column
  old_id int  NOT NULL PRIMARY KEY ,
  
  -- Depending on the contents of the old_flags field, the text
  -- may be convenient plain text, or it may be funkily encoded.
  old_text varchar NOT NULL,
  
  -- Comma-separated list of flags:
  -- gzip: text is compressed with PHP's gzdeflate() function.
  -- utf8: text was stored as UTF-8.
  --       If $wgLegacyEncoding option is on, rows *without* this flag
  --       will be converted to UTF-8 transparently at load time.
  -- object: text field contained a serialized PHP object.
  --         The object either contains multiple versions compressed
  --         together to achieve a better compression ratio, or it refers
  --         to another row where the text can be found.
  old_flags varchar NOT NULL
) /*$wgDBTableOptions*/;
-- In case tables are created as MyISAM, use row hints for MySQL <5.0 to avoid 4GB limit


--
-- Holding area for deleted articles, which may be viewed
-- or restored by admins through the Special:Undelete interface.
-- The fields generally correspond to the page, revision, and text
-- fields, with several caveats.
--
CREATE TABLE /*_*/archive (
  ar_namespace int NOT NULL ,
  ar_title varchar(255)  NOT NULL ,
  
  -- Newly deleted pages will not store text in this table,
  -- but will reference the separately existing text rows.
  -- This field is retained for backwards compatibility,
  -- so old archived pages will remain accessible after
  -- upgrading from 1.4 to 1.5.
  -- Text may be gzipped or otherwise funky.
  ar_text varchar NOT NULL,
  
  -- Basic revision stuff...
  ar_comment varchar NOT NULL,
  ar_user int  NOT NULL ,
  ar_user_text varchar(255)  NOT NULL,
  ar_timestamp varchar(14) NOT NULL ,
  ar_minor_edit int NOT NULL ,
  
  -- See ar_text note.
  ar_flags varchar NOT NULL,
  
  -- When revisions are deleted, their unique rev_id is stored
  -- here so it can be retained after undeletion. This is necessary
  -- to retain permalinks to given revisions after accidental delete
  -- cycles or messy operations like history merges.
  -- 
  -- Old entries from 1.4 will be NULL here, and a new rev_id will
  -- be created on undeletion for those revisions.
  ar_rev_id int ,
  
  -- For newly deleted revisions, this is the text.old_id key to the
  -- actual stored text. To avoid breaking the block-compression scheme
  -- and otherwise making storage changes harder, the actual text is
  -- *not* deleted from the text table, merely hidden by removal of the
  -- page and revision entries.
  --
  -- Old entries deleted under 1.2-1.4 will have NULL here, and their
  -- ar_text and ar_flags fields will be used to create a new text
  -- row upon undeletion.
  ar_text_id int ,

  -- rev_deleted for archives
  ar_deleted int  NOT NULL ,

  -- Length of this revision in bytes
  ar_len int ,

  -- Reference to page_id. Useful for sysadmin fixing of large pages 
  -- merged together in the archives, or for cleanly restoring a page
  -- at its original ID number if possible.
  --
  -- Will be NULL for pages deleted prior to 1.11.
  ar_page_id int ,
  
  -- Original previous revision
  ar_parent_id int  
) /*$wgDBTableOptions*/;

--CREATE INDEX /*i*/name_title_timestamp ON /*_*/archive (ar_namespace,ar_title,ar_timestamp);
--CREATE INDEX /*i*/ar_usertext_timestamp ON /*_*/archive (ar_user_text,ar_timestamp);


--
-- Track page-to-page hyperlinks within the wiki.
--
CREATE TABLE /*_*/pagelinks (
  -- Key to the page_id of the page containing the link.
  pl_from int  NOT NULL ,
  
  -- Key to page_namespace/page_title of the target page.
  -- The target page may or may not exist, and due to renames
  -- and deletions may refer to different page records as time
  -- goes by.
  pl_namespace int NOT NULL ,
  pl_title varchar(255)  NOT NULL ,
  
  CONSTRAINT uc_pl_from UNIQUE (pl_from,pl_namespace,pl_title),
  
  CONSTRAINT uc_pl_namespace UNIQUE (pl_namespace,pl_title,pl_from)
  
) /*$wgDBTableOptions*/;

--CREATE UNIQUE INDEX /*i*/pl_from ON /*_*/pagelinks (pl_from,pl_namespace,pl_title);
--CREATE UNIQUE INDEX /*i*/pl_namespace ON /*_*/pagelinks (pl_namespace,pl_title,pl_from);


--
-- Track template inclusions.
--
CREATE TABLE /*_*/templatelinks (
  -- Key to the page_id of the page containing the link.
  tl_from int  NOT NULL ,
  
  -- Key to page_namespace/page_title of the target page.
  -- The target page may or may not exist, and due to renames
  -- and deletions may refer to different page records as time
  -- goes by.
  tl_namespace int NOT NULL ,
  tl_title varchar(255)  NOT NULL ,
  
  CONSTRAINT uc_tl_from UNIQUE (tl_from,tl_namespace,tl_title),
  
  CONSTRAINT uc_tl_namespace UNIQUE (tl_namespace,tl_title,tl_from)
  
) /*$wgDBTableOptions*/;

--CREATE UNIQUE INDEX /*i*/tl_from ON /*_*/templatelinks (tl_from,tl_namespace,tl_title);
--CREATE UNIQUE INDEX /*i*/tl_namespace ON /*_*/templatelinks (tl_namespace,tl_title,tl_from);


--
-- Track links to images *used inline*
-- We don't distinguish live from broken links here, so
-- they do not need to be changed on upload/removal.
--
CREATE TABLE /*_*/imagelinks (
  -- Key to page_id of the page containing the image / media link.
  il_from int  NOT NULL ,
  
  -- Filename of target image.
  -- This is also the page_title of the file's description page;
  -- all such pages are in namespace 6 (NS_FILE).
  il_to varchar(255)  NOT NULL ,
  
  CONSTRAINT uc_il_from UNIQUE (il_from,il_to),
  
  CONSTRAINT uc_il_namespace UNIQUE (il_to,il_from)
) /*$wgDBTableOptions*/;

--CREATE UNIQUE INDEX /*i*/il_from ON /*_*/imagelinks (il_from,il_to);
--CREATE UNIQUE INDEX /*i*/il_to ON /*_*/imagelinks (il_to,il_from);


--
-- Track category inclusions *used inline*
-- This tracks a single level of category membership
--
CREATE TABLE /*_*/categorylinks (
  -- Key to page_id of the page defined as a category member.
  cl_from int  NOT NULL ,
  
  -- Name of the category.
  -- This is also the page_title of the category's description page;
  -- all such pages are in namespace 14 (NS_CATEGORY).
  cl_to varchar(255)  NOT NULL ,
  
  -- The title of the linking page, or an optional override
  -- to determine sort order. Sorting is by  order, which
  -- isn't always ideal, but collations seem to be an exciting
  -- and dangerous new world in MySQL... The sortkey is updated
  -- if no override exists and cl_from is renamed.
  --
  -- Truncate so that the cl_sortkey key fits in 1000 bytes 
  -- (MyISAM 5 with server_character_set=utf8)
  cl_sortkey varchar(70)  NOT NULL ,
  
  -- This isn't really used at present. Provided for an optional
  -- sorting method by approximate addition time.
  cl_timestamp timestamp NOT NULL,
  
  CONSTRAINT uc_cl_from UNIQUE (cl_from,cl_to)
) /*$wgDBTableOptions*/;

--CREATE UNIQUE INDEX /*i*/cl_from ON /*_*/categorylinks (cl_from,cl_to);

-- We always sort within a given category...
--CREATE INDEX /*i*/cl_sortkey ON /*_*/categorylinks (cl_to,cl_sortkey,cl_from);

-- Not really used?
--CREATE INDEX /*i*/cl_timestamp ON /*_*/categorylinks (cl_to,cl_timestamp);


-- 
-- Track all existing categories.  Something is a category if 1) it has an en-
-- try somewhere in categorylinks, or 2) it once did.  Categories might not
-- have corresponding pages, so they need to be tracked separately.
--
CREATE TABLE /*_*/category (
  -- Primary key
  cat_id int  NOT NULL PRIMARY KEY ,

  -- Name of the category, in the same form as page_title (with underscores).
  -- If there is a category page corresponding to this category, by definition,
  -- it has this name (in the Category namespace).
  cat_title varchar(255)  NOT NULL,

  -- The numbers of member pages (including categories and media), subcatego-
  -- ries, and Image: namespace members, respectively.  These are signed to
  -- make underflow more obvious.  We make the first number include the second
  -- two for better sorting: subtracting for display is easy, adding for order-
  -- ing is not.
  cat_pages int  NOT NULL ,
  cat_subcats int  NOT NULL ,
  cat_files int  NOT NULL ,

  -- Reserved for future use
  cat_hidden int  NOT NULL ,
  
  CONSTRAINT uc_cat_title UNIQUE (cat_title)
  
) /*$wgDBTableOptions*/;

--CREATE UNIQUE INDEX /*i*/cat_title ON /*_*/category (cat_title);

-- For Special:Mostlinkedcategories
--CREATE INDEX /*i*/cat_pages ON /*_*/category (cat_pages);


--
-- Track links to external URLs
--
CREATE TABLE /*_*/externallinks (
  -- page_id of the referring page
  el_from int  NOT NULL ,

  -- The URL
  el_to varchar NOT NULL,

  -- In the case of HTTP URLs, this is the URL with any username or password
  -- removed, and with the labels in the hostname reversed and converted to 
  -- lower case. An extra dot is added to allow for matching of either
  -- example.com or *.example.com in a single scan.
  -- Example: 
  --      http://user:password@sub.example.com/page.html
  --   becomes
  --      http://com.example.sub./page.html
  -- which allows for fast searching for all pages under example.com with the
  -- clause: 
  --      WHERE el_index LIKE 'http://com.example.%'
  el_index varchar NOT NULL
) /*$wgDBTableOptions*/;

--CREATE INDEX /*i*/el_from ON /*_*/externallinks (el_from, el_to(40));
--CREATE INDEX /*i*/el_to ON /*_*/externallinks (el_to(60), el_from);
--CREATE INDEX /*i*/el_index ON /*_*/externallinks (el_index(60));


--
-- Track external user accounts, if ExternalAuth is used
--
CREATE TABLE /*_*/external_user (
  -- Foreign key to user_id
  eu_local_id int  NOT NULL PRIMARY KEY,

  -- Some opaque identifier provided by the external database
  eu_external_id varchar(255)  NOT NULL,
  
  CONSTRAINT uc_eu_external_id UNIQUE (eu_external_id)
) /*$wgDBTableOptions*/;

--CREATE UNIQUE INDEX /*i*/eu_external_id ON /*_*/external_user (eu_external_id);


-- 
-- Track interlanguage links
--
CREATE TABLE /*_*/langlinks (
  -- page_id of the referring page
  ll_from int  NOT NULL ,
  
  -- Language code of the target
  ll_lang varchar(20) NOT NULL ,

  -- Title of the target, including namespace
  ll_title varchar(255)  NOT NULL ,
  
  CONSTRAINT uc_ll_from UNIQUE (ll_from, ll_lang)
) /*$wgDBTableOptions*/;

--CREATE UNIQUE INDEX /*i*/ll_from ON /*_*/langlinks (ll_from, ll_lang);
--CREATE INDEX /*i*/ll_lang ON /*_*/langlinks (ll_lang, ll_title);


--
-- Contains a single row with some aggregate info
-- on the state of the site.
--
CREATE TABLE /*_*/site_stats (
  -- The single row should contain 1 here.
  ss_row_id int  NOT NULL,
  
  -- Total number of page views, if hit counters are enabled.
  ss_total_views int  ,
  
  -- Total number of edits performed.
  ss_total_edits int  ,
  
  -- An approximate count of pages matching the following criteria:
  -- * in namespace 0
  -- * not a redirect
  -- * contains the text '[['
  -- See Article::isCountable() in includes/Article.php
  ss_good_articles int  ,
  
  -- Total pages, theoretically equal to SELECT COUNT(*) FROM page; except faster
  ss_total_pages int ,

  -- Number of users, theoretically equal to SELECT COUNT(*) FROM user;
  ss_users int ,
  
  -- Number of users that still edit
  ss_active_users int ,

  -- Deprecated, no longer updated as of 1.5
  ss_admins int ,

  -- Number of images, equivalent to SELECT COUNT(*) FROM image
  ss_images int ,
  
  CONSTRAINT uc_ss_row_id UNIQUE (ss_row_id)
) /*$wgDBTableOptions*/;

-- Pointless index to assuage developer superstitions
--CREATE UNIQUE INDEX /*i*/ss_row_id ON /*_*/site_stats (ss_row_id);


--
-- Stores an ID for every time any article is visited;
-- depending on $wgHitcounterUpdateFreq, it is
-- periodically cleared and the page_counter column
-- in the page table updated for all the articles
-- that have been visited.)
--
CREATE TABLE /*_*/hitcounter (
  hc_id int  NOT NULL
) ;


--
-- The internet is full of jerks, alas. Sometimes it's handy
-- to block a vandal or troll account.
--
CREATE TABLE /*_*/ipblocks (
  -- Primary key, introduced for privacy.
  ipb_id int NOT NULL PRIMARY KEY ,
  
  -- Blocked IP address in dotted-quad form or user name.
  ipb_address varchar NOT NULL,
  
  -- Blocked user ID or 0 for IP blocks.
  ipb_user int  NOT NULL ,
  
  -- User ID who made the block.
  ipb_by int  NOT NULL ,
  
  -- User name of blocker
  ipb_by_text varchar(255)  NOT NULL ,
  
  -- Text comment made by blocker.
  ipb_reason varchar NOT NULL,
  
  -- Creation (or refresh) date in standard YMDHMS form.
  -- IP blocks expire automatically.
  ipb_timestamp varchar(14) NOT NULL ,
  
  -- Indicates that the IP address was banned because a banned
  -- user accessed a page through it. If this is 1, ipb_address
  -- will be hidden, and the block identified by block ID number.
  ipb_auto bit NOT NULL ,

  -- If set to 1, block applies only to logged-out users
  ipb_anon_only bit NOT NULL ,

  -- Block prevents account creation from matching IP addresses
  ipb_create_account bit NOT NULL ,

  -- Block triggers autoblocks
  ipb_enable_autoblock bit NOT NULL ,
  
  -- Time at which the block will expire.
  -- May be "infinity"
  ipb_expiry varchar(14) NOT NULL ,
  
  -- Start and end of an address range, in hexadecimal
  -- Size chosen to allow IPv6
  ipb_range_start varchar NOT NULL,
  ipb_range_end varchar NOT NULL,

  -- Flag for entries hidden from users and Sysops
  ipb_deleted bit NOT NULL ,

  -- Block prevents user from accessing Special:Emailuser
  ipb_block_email bit NOT NULL ,
  
  -- Block allows user to edit their own talk page
  ipb_allow_usertalk bit NOT NULL ,
  
  CONSTRAINT uc_ipb_address UNIQUE (ipb_address, ipb_user, ipb_auto, ipb_anon_only)

) /*$wgDBTableOptions*/;
  
-- Unique index to support "user already blocked" messages
-- Any new options which prevent collisions should be included
--CREATE UNIQUE INDEX /*i*/ipb_address ON /*_*/ipblocks (ipb_address(255), ipb_user, ipb_auto, ipb_anon_only);

--CREATE INDEX /*i*/ipb_user ON /*_*/ipblocks (ipb_user);
--CREATE INDEX /*i*/ipb_range ON /*_*/ipblocks (ipb_range_start(8), ipb_range_end(8));
--CREATE INDEX /*i*/ipb_timestamp ON /*_*/ipblocks (ipb_timestamp);
--CREATE INDEX /*i*/ipb_expiry ON /*_*/ipblocks (ipb_expiry);


--
-- Uploaded images and other files.
--
CREATE TABLE /*_*/image (
  -- Filename.
  -- This is also the title of the associated description page,
  -- which will be in namespace 6 (NS_FILE).
  img_name varchar(255)  NOT NULL  PRIMARY KEY,
  
  -- File size in bytes.
  img_size int  NOT NULL ,
  
  -- For images, size in pixels.
  img_width int NOT NULL ,
  img_height int NOT NULL ,
  
  -- Extracted EXIF metadata stored as a serialized PHP array.
  img_metadata varchar NOT NULL,
  
  -- For images, bits per pixel if known.
  img_bits int NOT NULL ,
  
  -- Media type as defined by the MEDIATYPE_xxx constants
  img_media_type varchar, --ENUM("UNKNOWN", "BITMAP", "DRAWING", "AUDIO", "VIDEO", "MULTIMEDIA", "OFFICE", "TEXT", "EXECUTABLE", "ARCHIVE") ,
  
  -- major part of a MIME media type as defined by IANA
  -- see http://www.iana.org/assignments/media-types/
  img_major_mime varchar, --ENUM("unknown", "application", "audio", "image", "text", "video", "message", "model", "multipart") NOT NULL default "unknown",
  
  -- minor part of a MIME media type as defined by IANA
  -- the minor parts are not required to adher to any standard
  -- but should be consistent throughout the database
  -- see http://www.iana.org/assignments/media-types/
  img_minor_mime varchar(32) NOT NULL ,
  
  -- Description field as entered by the uploader.
  -- This is displayed in image upload history and logs.
  img_description varchar NOT NULL,
  
  -- user_id and user_name of uploader.
  img_user int  NOT NULL ,
  img_user_text varchar(255)  NOT NULL,
  
  -- Time of the upload.
  img_timestamp varchar(14) NOT NULL ,
  
  -- SHA-1 content hash in base-36
  img_sha1 varchar(32) NOT NULL 
) /*$wgDBTableOptions*/;

--CREATE INDEX /*i*/img_usertext_timestamp ON /*_*/image (img_user_text,img_timestamp);
-- Used by Special:Imagelist for sort-by-size
--CREATE INDEX /*i*/img_size ON /*_*/image (img_size);
-- Used by Special:Newimages and Special:Imagelist
--CREATE INDEX /*i*/img_timestamp ON /*_*/image (img_timestamp);
-- Used in API and duplicate search
--CREATE INDEX /*i*/img_sha1 ON /*_*/image (img_sha1);


--
-- Previous revisions of uploaded files.
-- Awkwardly, image rows have to be moved into
-- this table at re-upload time.
--
CREATE TABLE /*_*/oldimage (
  -- Base filename: key to image.img_name
  oi_name varchar(255)  NOT NULL ,
  
  -- Filename of the archived file.
  -- This is generally a timestamp and '!' prepended to the base name.
  oi_archive_name varchar(255)  NOT NULL ,
  
  -- Other fields as in image...
  oi_size int  NOT NULL ,
  oi_width int NOT NULL ,
  oi_height int NOT NULL ,
  oi_bits int NOT NULL ,
  oi_description varchar NOT NULL,
  oi_user int  NOT NULL ,
  oi_user_text varchar(255)  NOT NULL,
  oi_timestamp varchar(14) NOT NULL ,

  oi_metadata varchar NOT NULL,
  oi_media_type varchar,-- ENUM("UNKNOWN", "BITMAP", "DRAWING", "AUDIO", "VIDEO", "MULTIMEDIA", "OFFICE", "TEXT", "EXECUTABLE", "ARCHIVE") ,
  oi_major_mime varchar, --ENUM("unknown", "application", "audio", "image", "text", "video", "message", "model", "multipart") NOT NULL ,
  oi_minor_mime varchar(32) NOT NULL ,
  oi_deleted int  NOT NULL ,
  oi_sha1 varchar(32) NOT NULL 
) /*$wgDBTableOptions*/;

--CREATE INDEX /*i*/oi_usertext_timestamp ON /*_*/oldimage (oi_user_text,oi_timestamp);
--CREATE INDEX /*i*/oi_name_timestamp ON /*_*/oldimage (oi_name,oi_timestamp);
-- oi_archive_name truncated to 14 to avoid key length overflow
--CREATE INDEX /*i*/oi_name_archive_name ON /*_*/oldimage (oi_name,oi_archive_name(14));
--CREATE INDEX /*i*/oi_sha1 ON /*_*/oldimage (oi_sha1);


--
-- Record of deleted file data
--
CREATE TABLE /*_*/filearchive (
  -- Unique row id
  fa_id int NOT NULL PRIMARY KEY ,
  
  -- Original base filename; key to image.img_name, page.page_title, etc
  fa_name varchar(255)  NOT NULL ,
  
  -- Filename of archived file, if an old revision
  fa_archive_name varchar(255)  ,
  
  -- Which storage bin (directory tree or object store) the file data
  -- is stored in. Should be 'deleted' for files that have been deleted;
  -- any other bin is not yet in use.
  fa_storage_group varchar(16),
  
  -- SHA-1 of the file contents plus extension, used as a key for storage.
  -- eg 8f8a562add37052a1848ff7771a2c515db94baa9.jpg
  --
  -- If NULL, the file was missing at deletion time or has been purged
  -- from the archival storage.
  fa_storage_key varchar(64) ,
  
  -- Deletion information, if this file is deleted.
  fa_deleted_user int,
  fa_deleted_timestamp varchar(14) ,
  fa_deleted_reason varchar,
  
  -- Duped fields from image
  fa_size int  ,
  fa_width int ,
  fa_height int ,
  fa_metadata varchar,
  fa_bits int ,
  fa_media_type varchar, --ENUM("UNKNOWN", "BITMAP", "DRAWING", "AUDIO", "VIDEO", "MULTIMEDIA", "OFFICE", "TEXT", "EXECUTABLE", "ARCHIVE") ,
  fa_major_mime varchar, --ENUM("unknown", "application", "audio", "image", "text", "video", "message", "model", "multipart") ,
  fa_minor_mime varchar(32) ,
  fa_description varchar,
  fa_user int  ,
  fa_user_text varchar(255) ,
  fa_timestamp varchar(14) ,

  -- Visibility of deleted revisions, bitfield
  fa_deleted int  NOT NULL 
) /*$wgDBTableOptions*/;

-- pick out by image name
--CREATE INDEX /*i*/fa_name ON /*_*/filearchive (fa_name, fa_timestamp);
-- pick out dupe files
--CREATE INDEX /*i*/fa_storage_group ON /*_*/filearchive (fa_storage_group, fa_storage_key);
-- sort by deletion time
--CREATE INDEX /*i*/fa_deleted_timestamp ON /*_*/filearchive (fa_deleted_timestamp);
-- sort by uploader
--CREATE INDEX /*i*/fa_user_timestamp ON /*_*/filearchive (fa_user_text,fa_timestamp);

--
-- Primarily a summary table for Special:Recentchanges,
-- this table contains some additional info on edits from
-- the last few days, see Article::editUpdates()
--
CREATE TABLE /*_*/recentchanges (
  rc_id int NOT NULL PRIMARY KEY ,
  rc_timestamp varchar(14) NOT NULL ,
  rc_cur_time varchar(14) NOT NULL ,
  
  -- As in revision
  rc_user int  NOT NULL ,
  rc_user_text varchar(255)  NOT NULL,
  
  -- When pages are renamed, their RC entries do _not_ change.
  rc_namespace int NOT NULL ,
  rc_title varchar(255)  NOT NULL ,
  
  -- as in revision...
  rc_comment varchar(255)  NOT NULL ,
  rc_minor int  NOT NULL ,
  
  -- Edits by user accounts with the 'bot' rights key are
  -- marked with a 1 here, and will be hidden from the
  -- default view.
  rc_bot int  NOT NULL ,
  
  rc_new int  NOT NULL ,
  
  -- Key to page_id (was cur_id prior to 1.5).
  -- This will keep links working after moves while
  -- retaining the at-the-time name in the changes list.
  rc_cur_id int  NOT NULL ,
  
  -- rev_id of the given revision
  rc_this_oldid int  NOT NULL ,
  
  -- rev_id of the prior revision, for generating diff links.
  rc_last_oldid int  NOT NULL ,
  
  -- These may no longer be used, with the new move log.
  rc_type int  NOT NULL ,
  rc_moved_to_ns int  NOT NULL ,
  rc_moved_to_title varchar(255)  NOT NULL ,
  
  -- If the Recent Changes Patrol option is enabled,
  -- users may mark edits as having been reviewed to
  -- remove a warning flag on the RC list.
  -- A value of 1 indicates the page has been reviewed.
  rc_patrolled int  NOT NULL ,
  
  -- Recorded IP address the edit was made from, if the
  -- $wgPutIPinRC option is enabled.
  rc_ip varchar(40) NOT NULL ,
  
  -- Text length in characters before
  -- and after the edit
  rc_old_len int,
  rc_new_len int,

  -- Visibility of recent changes items, bitfield
  rc_deleted int  NOT NULL ,

  -- Value corresonding to log_id, specific log entries
  rc_logid int  NOT NULL ,
  -- Store log type info here, or null
  rc_log_type varchar(255) NULL ,
  -- Store log action or null
  rc_log_action varchar(255) NULL ,
  -- Log params
  rc_params varchar NULL
) /*$wgDBTableOptions*/;

--CREATE INDEX /*i*/rc_timestamp ON /*_*/recentchanges (rc_timestamp);
--CREATE INDEX /*i*/rc_namespace_title ON /*_*/recentchanges (rc_namespace, rc_title);
--CREATE INDEX /*i*/rc_cur_id ON /*_*/recentchanges (rc_cur_id);
--CREATE INDEX /*i*/new_name_timestamp ON /*_*/recentchanges (rc_new,rc_namespace,rc_timestamp);
--CREATE INDEX /*i*/rc_ip ON /*_*/recentchanges (rc_ip);
--CREATE INDEX /*i*/rc_ns_usertext ON /*_*/recentchanges (rc_namespace, rc_user_text);
--CREATE INDEX /*i*/rc_user_text ON /*_*/recentchanges (rc_user_text, rc_timestamp);


CREATE TABLE /*_*/watchlist (
  -- Key to user.user_id
  wl_user int  NOT NULL,
  
  -- Key to page_namespace/page_title
  -- Note that users may watch pages which do not exist yet,
  -- or existed in the past but have been deleted.
  wl_namespace int NOT NULL ,
  wl_title varchar(255)  NOT NULL ,
  
  -- Timestamp when user was last sent a notification e-mail;
  -- cleared when the user visits the page.
  wl_notificationtimestamp varchar(14),
  
  CONSTRAINT uc_wl_user UNIQUE (wl_user, wl_namespace, wl_title)
  
) /*$wgDBTableOptions*/;

--CREATE UNIQUE INDEX /*i*/wl_user ON /*_*/watchlist (wl_user, wl_namespace, wl_title);
--CREATE INDEX /*i*/namespace_title ON /*_*/watchlist (wl_namespace, wl_title);


--
-- Used by the math module to keep track
-- of previously-rendered items.
--
CREATE TABLE /*_*/math (
  --  MD5 hash of the latex fragment, used as an identifier key.
  math_inputhash varchar(16) NOT NULL,
  
  -- Not sure what this is, exactly...
  math_outputhash varchar(16) NOT NULL,
  
  -- texvc reports how well it thinks the HTML conversion worked;
  -- if it's a low level the PNG rendering may be preferred.
  math_html_conservativeness int NOT NULL,
  
  -- HTML output from texvc, if any
  math_html varchar,
  
  -- MathML output from texvc, if any
  math_mathml varchar,
  
  CONSTRAINT uc_math_inputhash UNIQUE (math_inputhash)
) /*$wgDBTableOptions*/;

--CREATE UNIQUE INDEX /*i*/math_inputhash ON /*_*/math (math_inputhash);


--
-- When using the default MySQL search backend, page titles
-- and varchar are munged to strip markup, do Unicode case folding,
-- and prepare the result for MySQL's fullvarchar index.
--
-- This table must be MyISAM; InnoDB does not support the needed
-- fullvarchar index.
--
CREATE TABLE /*_*/searchindex (
  -- Key to page_id
  si_page int  NOT NULL,
  
  -- Munged version of title
  si_title varchar(255) NOT NULL ,
  
  -- Munged version of body varchar
  si_varchar varchar NOT NULL,
  
  CONSTRAINT uc_si_page UNIQUE (si_page)
) ;

--CREATE UNIQUE INDEX /*i*/si_page ON /*_*/searchindex (si_page);
--CREATE FULLvarchar INDEX /*i*/si_title ON /*_*/searchindex (si_title);
--CREATE FULLvarchar INDEX /*i*/si_varchar ON /*_*/searchindex (si_varchar);


--
-- Recognized interwiki link prefixes
--
CREATE TABLE /*_*/interwiki (
  -- The interwiki prefix, (e.g. "Meatball", or the language prefix "de")
  iw_prefix varchar(32) NOT NULL,
  
  -- The URL of the wiki, with "$1" as a placeholder for an article name.
  -- Any spaces in the name will be transformed to underscores before
  -- insertion.
  iw_url varchar NOT NULL,
  
  -- A bit value indicating whether the wiki is in this project
  -- (used, for example, to detect redirect loops)
  iw_local bit NOT NULL,
  
  -- bit value indicating whether interwiki transclusions are allowed.
  iw_trans int NOT NULL ,
  
  CONSTRAINT uc_iw_prefix UNIQUE (iw_prefix)
) /*$wgDBTableOptions*/;

--CREATE UNIQUE INDEX /*i*/iw_prefix ON /*_*/interwiki (iw_prefix);


--
-- Used for caching expensive grouped queries
--
CREATE TABLE /*_*/querycache (
  -- A key name, generally the base name of of the special page.
  qc_type varchar(32) NOT NULL,
  
  -- Some sort of stored value. Sizes, counts...
  qc_value int  NOT NULL ,
  
  -- Target namespace+title
  qc_namespace int NOT NULL ,
  qc_title varchar(255)  NOT NULL 
) /*$wgDBTableOptions*/;

--CREATE INDEX /*i*/qc_type ON /*_*/querycache (qc_type,qc_value);


--
-- For a few generic cache operations if not using Memcached
--
CREATE TABLE /*_*/objectcache (
  keyname varchar(255) NOT NULL  PRIMARY KEY,
  value varchar,
  exptime date
) /*$wgDBTableOptions*/;
--CREATE INDEX /*i*/exptime ON /*_*/objectcache (exptime);


--
-- Cache of interwiki transclusion
--
CREATE TABLE /*_*/transcache (
  tc_url varchar(255) NOT NULL,
  tc_contents varchar,
  tc_time varchar(14) NOT NULL,
  
  CONSTRAINT uc_tc_url_idx UNIQUE (tc_url)
) /*$wgDBTableOptions*/;

--CREATE UNIQUE INDEX /*i*/tc_url_idx ON /*_*/transcache (tc_url);


CREATE TABLE /*_*/logging (
  -- Log ID, for referring to this specific log entry, probably for deletion and such.
  log_id int  NOT NULL PRIMARY KEY ,

  -- Symbolic keys for the general log type and the action type
  -- within the log. The output format will be controlled by the
  -- action field, but only the type controls categorization.
  log_type varchar(32) NOT NULL ,
  log_action varchar(32) NOT NULL ,
  
  -- Timestamp. Duh.
  log_timestamp varchar(14) NOT NULL ,
  
  -- The user who performed this action; key to user_id
  log_user int  NOT NULL ,
  
  -- Name of the user who performed this action
  log_user_varchar varchar(255)  NOT NULL ,
  
  -- Key to the page affected. Where a user is the target,
  -- this will point to the user page.
  log_namespace int NOT NULL ,
  log_title varchar(255)  NOT NULL ,
  log_page int  NULL,
  
  -- Freeform varchar. Interpreted as edit history comments.
  log_comment varchar(255) NOT NULL ,
  
  -- LF separated list of miscellaneous parameters
  log_params varchar NOT NULL,

  -- rev_deleted for logs
  log_deleted int  NOT NULL 
) /*$wgDBTableOptions*/;

--CREATE INDEX /*i*/type_time ON /*_*/logging (log_type, log_timestamp);
--CREATE INDEX /*i*/user_time ON /*_*/logging (log_user, log_timestamp);
--CREATE INDEX /*i*/page_time ON /*_*/logging (log_namespace, log_title, log_timestamp);
--CREATE INDEX /*i*/times ON /*_*/logging (log_timestamp);
--CREATE INDEX /*i*/log_user_type_time ON /*_*/logging (log_user, log_type, log_timestamp);
--CREATE INDEX /*i*/log_page_id_time ON /*_*/logging (log_page,log_timestamp);


CREATE TABLE /*_*/log_search (
  -- The type of ID (rev ID, log ID, rev timestamp, username)
  ls_field varchar(32) NOT NULL,
  -- The value of the ID
  ls_value varchar(255) NOT NULL,
  -- Key to log_id
  ls_log_id int  NOT NULL ,
  
  CONSTRAINT uc_ls_field_val UNIQUE (ls_field,ls_value,ls_log_id)
) /*$wgDBTableOptions*/;
--CREATE UNIQUE INDEX /*i*/ls_field_val ON /*_*/log_search (ls_field,ls_value,ls_log_id);
--CREATE INDEX /*i*/ls_log_id ON /*_*/log_search (ls_log_id);


CREATE TABLE /*_*/trackbacks (
  tb_id int PRIMARY KEY ,
  tb_page int ,--REFERENCES /*_*/page(page_id) ON DELETE CASCADE,
  tb_title varchar(255) NOT NULL,
  tb_url varchar NOT NULL,
  tb_ex varchar,
  tb_name varchar(255),
  
  CONSTRAINT fk_tb_page FOREIGN KEY (tb_page) REFERENCES page(page_id) --ON DELETE CASCADE
) /*$wgDBTableOptions*/;
--CREATE INDEX /*i*/tb_page ON /*_*/trackbacks (tb_page);


-- Jobs performed by parallel apache threads or a command-line daemon
CREATE TABLE /*_*/job (
  job_id int  NOT NULL PRIMARY KEY ,
  
  -- Command name
  -- Limited to 60 to prevent key length overflow
  job_cmd varchar(60) NOT NULL ,

  -- Namespace and title to act on
  -- Should be 0 and '' if the command does not operate on a title
  job_namespace int NOT NULL,
  job_title varchar(255)  NOT NULL,

  -- Any other parameters to the command
  -- Stored as a PHP serialized array, or an empty string if there are no parameters
  job_params varchar NOT NULL
) /*$wgDBTableOptions*/;

--CREATE INDEX /*i*/job_cmd ON /*_*/job (job_cmd, job_namespace, job_title, job_params(128));


-- Details of updates to cached special pages
CREATE TABLE /*_*/querycache_info (
  -- Special page name
  -- Corresponds to a qc_type value
  qci_type varchar(32) NOT NULL ,

  -- Timestamp of last update
  qci_timestamp varchar(14) NOT NULL ,--default '19700101000000'
  
  CONSTRAINT uc_qci_type UNIQUE (qci_type)
) /*$wgDBTableOptions*/;

--CREATE UNIQUE INDEX /*i*/qci_type ON /*_*/querycache_info (qci_type);


-- For each redirect, this table contains exactly one row defining its target
CREATE TABLE /*_*/redirect (
  -- Key to the page_id of the redirect page
  rd_from int  NOT NULL  PRIMARY KEY,

  -- Key to page_namespace/page_title of the target page.
  -- The target page may or may not exist, and due to renames
  -- and deletions may refer to different page records as time
  -- goes by.
  rd_namespace int NOT NULL ,
  rd_title varchar(255)  NOT NULL ,
  rd_interwiki varchar(32) ,
  rd_fragment varchar(255)  
) /*$wgDBTableOptions*/;

--CREATE INDEX /*i*/rd_ns_title ON /*_*/redirect (rd_namespace,rd_title,rd_from);


-- Used for caching expensive grouped queries that need two links (for example double-redirects)
CREATE TABLE /*_*/querycachetwo (
  -- A key name, generally the base name of of the special page.
  qcc_type varchar(32) NOT NULL,
  
  -- Some sort of stored value. Sizes, counts...
  qcc_value int  NOT NULL ,
  
  -- Target namespace+title
  qcc_namespace int NOT NULL ,
  qcc_title varchar(255)  NOT NULL ,
  
  -- Target namespace+title2
  qcc_namespacetwo int NOT NULL ,
  qcc_titletwo varchar(255)  NOT NULL 
) /*$wgDBTableOptions*/;

--CREATE INDEX /*i*/qcc_type ON /*_*/querycachetwo (qcc_type,qcc_value);
--CREATE INDEX /*i*/qcc_title ON /*_*/querycachetwo (qcc_type,qcc_namespace,qcc_title);
--CREATE INDEX /*i*/qcc_titletwo ON /*_*/querycachetwo (qcc_type,qcc_namespacetwo,qcc_titletwo);


-- Used for storing page restrictions (i.e. protection levels)
CREATE TABLE /*_*/page_restrictions (
  -- Field for an ID for this restrictions row (sort-key for Special:ProtectedPages)
  pr_id int  NOT NULL PRIMARY KEY ,
  -- Page to apply restrictions to (Foreign Key to page).
  pr_page int NOT NULL,
  -- The protection type (edit, move, etc)
  pr_type varchar(60) NOT NULL,
  -- The protection level (Sysop, autoconfirmed, etc)
  pr_level varchar(60) NOT NULL,
  -- Whether or not to cascade the protection down to pages transcluded.
  pr_cascade int NOT NULL,
  -- Field for future support of per-user restriction.
  pr_user int ,
  -- Field for time-limited protection.
  pr_expiry varchar(14) ,
  CONSTRAINT uc_pr_pagetype UNIQUE (pr_page,pr_type)
) /*$wgDBTableOptions*/;

--CREATE UNIQUE INDEX /*i*/pr_pagetype ON /*_*/page_restrictions (pr_page,pr_type);
--CREATE INDEX /*i*/pr_typelevel ON /*_*/page_restrictions (pr_type,pr_level);
--CREATE INDEX /*i*/pr_level ON /*_*/page_restrictions (pr_level);
--CREATE INDEX /*i*/pr_cascade ON /*_*/page_restrictions (pr_cascade);


-- Protected titles - nonexistent pages that have been protected
CREATE TABLE /*_*/protected_titles (
  pt_namespace int NOT NULL,
  pt_title varchar(255)  NOT NULL,
  pt_user int  NOT NULL,
  pt_reason varchar,
  pt_timestamp varchar(14) NOT NULL,
  pt_expiry varchar(14) NOT NULL ,
  pt_create_perm varchar(60) NOT NULL ,
  CONSTRAINT uc_pt_namespace_title UNIQUE (pt_namespace,pt_title)
) /*$wgDBTableOptions*/;

--CREATE UNIQUE INDEX /*i*/pt_namespace_title ON /*_*/protected_titles (pt_namespace,pt_title);
--CREATE INDEX /*i*/pt_timestamp ON /*_*/protected_titles (pt_timestamp);


-- Name/value pairs indexed by page_id
CREATE TABLE /*_*/page_props (
  pp_page int NOT NULL,
  pp_propname varchar(60) NOT NULL,
  pp_value varchar NOT NULL,
  
  CONSTRAINT uc_pp_page_propname UNIQUE (pp_page,pp_propname)
) /*$wgDBTableOptions*/;

--CREATE UNIQUE INDEX /*i*/pp_page_propname ON /*_*/page_props (pp_page,pp_propname);


-- A table to log updates, one varchar key row per update.
CREATE TABLE /*_*/updatelog (
  ul_key varchar(255) NOT NULL PRIMARY KEY
) /*$wgDBTableOptions*/;


-- A table to track tags for revisions, logs and recent changes.
CREATE TABLE /*_*/change_tag (
  -- RCID for the change
  ct_rc_id int ,
  -- LOGID for the change
  ct_log_id int ,
  -- REVID for  change
  ct_rev_id int NULL,
  -- Tag applied
  ct_tag varchar(255) NOT NULL,
  -- Parameters for the tag, presently unused
  ct_params varchar ,
  CONSTRAINT uc_change_tag_rc_tag UNIQUE (ct_rc_id,ct_tag),
  CONSTRAINT uc_change_tag_log_tag UNIQUE (ct_log_id,ct_tag),
  CONSTRAINT uc_change_tag_rev_tag UNIQUE (ct_rev_id,ct_tag)
) /*$wgDBTableOptions*/;

--CREATE UNIQUE INDEX /*i*/change_tag_rc_tag ON /*_*/change_tag (ct_rc_id,ct_tag);
--CREATE UNIQUE INDEX /*i*/change_tag_log_tag ON /*_*/change_tag (ct_log_id,ct_tag);
--CREATE UNIQUE INDEX /*i*/change_tag_rev_tag ON /*_*/change_tag (ct_rev_id,ct_tag);
-- Covering index, so we can pull all the info only out of the index.
--CREATE INDEX /*i*/change_tag_tag_id ON /*_*/change_tag (ct_tag,ct_rc_id,ct_rev_id,ct_log_id);


-- Rollup table to pull a LIST of tags simply without ugly GROUP_CONCAT
-- that only works on MySQL 4.1+
CREATE TABLE /*_*/tag_summary (
  -- RCID for the change  
  ts_rc_id int ,
  -- LOGID for the change
  ts_log_id int ,
  -- REVID for the change
  ts_rev_id int ,
  -- Comma-separated list of tags
  ts_tags varchar NOT NULL,
  CONSTRAINT tag_summary_rc_id UNIQUE (ts_rc_id),
  CONSTRAINT tag_summary_log_id UNIQUE (ts_log_id),
  CONSTRAINT tag_summary_rev_id UNIQUE (ts_rev_id)
) /*$wgDBTableOptions*/;

--CREATE UNIQUE INDEX /*i*/tag_summary_rc_id ON /*_*/tag_summary (ts_rc_id);
--CREATE UNIQUE INDEX /*i*/tag_summary_log_id ON /*_*/tag_summary (ts_log_id);
--CREATE UNIQUE INDEX /*i*/tag_summary_rev_id ON /*_*/tag_summary (ts_rev_id);


CREATE TABLE /*_*/valid_tag (
  vt_tag varchar(255) NOT NULL PRIMARY KEY
) /*$wgDBTableOptions*/;

-- Table for storing localisation data
CREATE TABLE /*_*/l10n_cache (
  -- Language code
  lc_lang varchar(32) NOT NULL,
  -- Cache key
  lc_key varchar(255) NOT NULL,
  -- Value
  lc_value varchar NOT NULL
) /*$wgDBTableOptions*/;
--CREATE INDEX /*i*/lc_lang_key ON /*_*/l10n_cache (lc_lang, lc_key);

-- vim: sw=2 sts=2 et
