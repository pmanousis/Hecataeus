
CREATE TABLE upgrade_exceptions (
  upgrade_exception_id int(5) NOT NULL ,
  sql_file varchar(50)   ,
  reason varchar(200)   ,
  errordate datetime  , 
  sqlstatement varchar(200),
  PRIMARY KEY  (upgrade_exception_id)
) ;


CREATE TABLE address_book (
  address_book_id int(11) NOT NULL ,
  customers_id int(11) NOT NULL   ,
  entry_gender char(1) NOT NULL   ,
  entry_company varchar(64)   ,
  entry_firstname varchar(32) NOT NULL   ,
  entry_lastname varchar(32) NOT NULL   ,
  entry_street_address varchar(64) NOT NULL   ,
  entry_suburb varchar(32)   ,
  entry_postcode varchar(10) NOT NULL   ,
  entry_city varchar(32) NOT NULL   ,
  entry_state varchar(32)   ,
  entry_country_id int(11) NOT NULL   ,
  entry_zone_id int(11) NOT NULL   ,
  PRIMARY KEY  (address_book_id)
) ;

CREATE TABLE address_format (
  address_format_id int(11) NOT NULL ,
  address_format varchar(128) NOT NULL   ,
  address_summary varchar(48) NOT NULL   ,
  PRIMARY KEY  (address_format_id)
) ;

CREATE TABLE admin (
  admin_id int(11) NOT NULL ,
  admin_name varchar(32) NOT NULL   ,
  admin_email varchar(96) NOT NULL   ,
  admin_profile int(11) NOT NULL   ,
  admin_pass varchar(40) NOT NULL   ,
  prev_pass1 varchar(40) NOT NULL   ,
  prev_pass2 varchar(40) NOT NULL   ,
  prev_pass3 varchar(40) NOT NULL   ,
  pwd_last_change_date datetime NOT NULL  , 
  reset_token varchar(60) NOT NULL   ,
  last_modified datetime NOT NULL  , 
  last_login_date datetime NOT NULL  , 
  last_login_ip varchar(45) NOT NULL   ,
  failed_logins int(4)  NOT NULL   ,
  lockout_expires int(11) NOT NULL   ,
  last_failed_attempt datetime NOT NULL  , 
  last_failed_ip varchar(45) NOT NULL   ,
  PRIMARY KEY  (admin_id)
) ;

CREATE TABLE admin_activity_log (
  log_id int(15) NOT NULL ,
  access_date datetime NOT NULL  , 
  admin_id int(11) NOT NULL   ,
  page_accessed varchar(80) NOT NULL   ,
  page_parameters varchar(45),
  ip_address varchar(45) NOT NULL   ,
  flagged int NOT NULL   ,
  attention varchar(255) NOT NULL   ,
  gzpost varchar(255),
  PRIMARY KEY  (log_id)
) ;

CREATE TABLE admin_menus (
  menu_key VARCHAR(32) NOT NULL   ,
  language_key VARCHAR(255) NOT NULL   ,
  sort_order INT(11) NOT NULL  
) ;

CREATE TABLE admin_pages (
  page_key VARCHAR(32) NOT NULL   ,
  language_key VARCHAR(255) NOT NULL   ,
  main_page varchar(64) NOT NULL   ,
  page_params varchar(64) NOT NULL   ,
  menu_key varchar(32) NOT NULL   ,
  display_on_menu char(1) NOT NULL  ,
  sort_order int(11) NOT NULL
) ;

CREATE TABLE admin_profiles (
  profile_id int(11) NOT NULL ,
  profile_name varchar(64) NOT NULL   ,
  PRIMARY KEY (profile_id)
) ;

CREATE TABLE admin_pages_to_profiles (
  profile_id int(11) NOT NULL   ,
  page_key varchar(32) NOT NULL 
) ;

CREATE TABLE authorizenet (
  id int(11) NOT NULL ,
  customer_id int(11) NOT NULL   ,
  order_id int(11) NOT NULL   ,
  response_code int(1) NOT NULL   ,
  response_text varchar(255) NOT NULL   ,
  authorization_type varchar(50) NOT NULL   ,
  transaction_id int(20)   ,
  sent varchar(50) NOT NULL,
  received varchar(50) NOT NULL,
  time varchar(50) NOT NULL   ,
  session_id varchar(255) NOT NULL   ,
  PRIMARY KEY  (id)
) ;

CREATE TABLE banners (
  banners_id int(11) NOT NULL ,
  banners_title varchar(64) NOT NULL   ,
  banners_url varchar(255) NOT NULL   ,
  banners_image varchar(64) NOT NULL   ,
  banners_group varchar(15) NOT NULL   ,
  banners_html_text varchar(255),
  expires_impressions int(7)   ,
  expires_date datetime   ,
  date_scheduled datetime   ,
  date_added datetime NOT NULL  , 
  date_status_change datetime   ,
  status int(1) NOT NULL   ,
  banners_open_new_windows int(1) NOT NULL   ,
  banners_on_ssl int(1) NOT NULL   ,
  banners_sort_order int(11) NOT NULL   ,
  PRIMARY KEY  (banners_id)
) ;


CREATE TABLE banners_history (
  banners_history_id int(11) NOT NULL ,
  banners_id int(11) NOT NULL   ,
  banners_shown int(5) NOT NULL   ,
  banners_clicked int(5) NOT NULL   ,
  banners_history_date datetime NOT NULL  , 
  PRIMARY KEY  (banners_history_id)
) ;

CREATE TABLE categories (
  categories_id int(11) NOT NULL ,
  categories_image varchar(64)   ,
  parent_id int(11) NOT NULL   ,
  sort_order int(3)   ,
  date_added datetime   ,
  last_modified datetime   ,
  categories_status int(1) NOT NULL   ,
  PRIMARY KEY  (categories_id)
) ;

CREATE TABLE categories_description (
  categories_id int(11) NOT NULL   ,
  language_id int(11) NOT NULL   ,
  categories_name varchar(32) NOT NULL   ,
  categories_description varchar(32) NOT NULL,
  PRIMARY KEY  (categories_id,language_id)
) ;

CREATE TABLE configuration (
  configuration_id int(11) NOT NULL ,
  configuration_title varchar(255) NOT NULL,
  configuration_key varchar(255) NOT NULL   ,
  configuration_value varchar(255) NOT NULL,
  configuration_description varchar(255) NOT NULL,
  configuration_group_id int(11) NOT NULL   ,
  sort_order int(5)   ,
  last_modified datetime   ,
  date_added datetime NOT NULL ,
  use_function varchar(255)   ,
  set_function  varchar(255)  ,
  PRIMARY KEY  (configuration_id)
) ;

CREATE TABLE configuration_group (
  configuration_group_id int(11) NOT NULL ,
  configuration_group_title varchar(64) NOT NULL   ,
  configuration_group_description varchar(255) NOT NULL   ,
  sort_order int(5)   ,
  visible int(1)   ,
  PRIMARY KEY  (configuration_group_id)
) ;

CREATE TABLE counter (
  startdate char(8)   ,
  counter int(12) 
) ;

CREATE TABLE counter_history (
  startdate char(8)   ,
  counter int(12)   ,
  session_counter int(12)   ,
  PRIMARY KEY  (startdate)
) ;


CREATE TABLE countries (
  countries_id int(11) NOT NULL ,
  countries_name varchar(64) NOT NULL   ,
  countries_iso_code_2 char(2) NOT NULL   ,
  countries_iso_code_3 char(3) NOT NULL   ,
  address_format_id int(11) NOT NULL   ,
  PRIMARY KEY  (countries_id)
) ;

CREATE TABLE coupon_email_track (
  unique_id int(11) NOT NULL ,
  coupon_id int(11) NOT NULL   ,
  customer_id_sent int(11) NOT NULL   ,
  sent_firstname varchar(32)   ,
  sent_lastname varchar(32)   ,
  emailed_to varchar(32)   ,
  date_sent datetime NOT NULL  ,
  PRIMARY KEY  (unique_id)
) ;

CREATE TABLE coupon_gv_customer (
  customer_id int(5) NOT NULL   ,
  amount decimal(15,4) NOT NULL  ,
  PRIMARY KEY  (customer_id)
) ;

CREATE TABLE coupon_gv_queue (
  unique_id int(5) NOT NULL ,
  customer_id int(5) NOT NULL   ,
  order_id int(5) NOT NULL   ,
  amount decimal(15,4) NOT NULL  ,
  date_created datetime NOT NULL  , 
  ipaddr varchar(45) NOT NULL   ,
  release_flag char(1) NOT NULL  ,
  PRIMARY KEY  (unique_id)
) ;

CREATE TABLE coupon_redeem_track (
  unique_id int(11) NOT NULL ,
  coupon_id int(11) NOT NULL   ,
  customer_id int(11) NOT NULL   ,
  redeem_date datetime NOT NULL  , 
  redeem_ip varchar(45) NOT NULL   ,
  order_id int(11) NOT NULL   ,
  PRIMARY KEY  (unique_id)
) ;

CREATE TABLE coupon_restrict (
  restrict_id int(11) NOT NULL ,
  coupon_id int(11) NOT NULL   ,
  product_id int(11) NOT NULL   ,
  category_id int(11) NOT NULL   ,
  coupon_restrict char(1) NOT NULL  ,
  PRIMARY KEY  (restrict_id)
) ;

CREATE TABLE coupons (
  coupon_id int(11) NOT NULL ,
  coupon_type char(1) NOT NULL  , 
  coupon_code varchar(32) NOT NULL   ,
  coupon_amount decimal(15,4) NOT NULL  , 
  coupon_minimum_order decimal(15,4) NOT NULL  , 
  coupon_start_date datetime NOT NULL  ,
  coupon_expire_date datetime NOT NULL  , 
  uses_per_coupon int(5) NOT NULL   ,
  uses_per_user int(5) NOT NULL   ,
  restrict_to_products varchar(255)   ,
  restrict_to_categories varchar(255)   ,
  restrict_to_customers varchar(255),
  coupon_active char(1) NOT NULL  , 
  date_created datetime NOT NULL  , 
  date_modified datetime NOT NULL  , 
  coupon_zone_restriction int(11) NOT NULL   ,
  PRIMARY KEY (coupon_id)
) ;

CREATE TABLE coupons_description (
  coupon_id int(11) NOT NULL   ,
  language_id int(11) NOT NULL   ,
  coupon_name varchar(32) NOT NULL   ,
  coupon_description varchar(32),
  PRIMARY KEY (coupon_id,language_id)
) ;


CREATE TABLE currencies (
  currencies_id int(11) NOT NULL ,
  title varchar(32) NOT NULL   ,
  code char(3) NOT NULL   ,
  symbol_left varchar(24)   ,
  symbol_right varchar(24)   ,
  decimal_point char(1)   ,
  thousands_point char(1)   ,
  decimal_places char(1)   ,
  value float(13,8)   ,
  last_updated datetime   ,
  PRIMARY KEY  (currencies_id)
) ;


CREATE TABLE customers (
  customers_id int(11) NOT NULL ,
  customers_gender char(1) NOT NULL   ,
  customers_firstname varchar(32) NOT NULL   ,
  customers_lastname varchar(32) NOT NULL   ,
  customers_dob datetime NOT NULL  , 
  customers_email_address varchar(96) NOT NULL   ,
  customers_nick varchar(96) NOT NULL   ,
  customers_default_address_id int(11) NOT NULL   ,
  customers_telephone varchar(32) NOT NULL   ,
  customers_fax varchar(32)   ,
  customers_password varchar(40) NOT NULL   ,
  customers_newsletter char(1)   ,
  customers_group_pricing int(11) NOT NULL   ,
  customers_email_format varchar(4) NOT NULL  , 
  customers_authorization int(1) NOT NULL   ,
  customers_referral varchar(32) NOT NULL   ,
  customers_paypal_payerid VARCHAR(20) NOT NULL   ,
  customers_paypal_ec INT(1)  NOT NULL,
  PRIMARY KEY  (customers_id)
) ;


CREATE TABLE customers_basket (
  customers_basket_id int(11) NOT NULL ,
  customers_id int(11) NOT NULL   ,
  products_id varchar(8)NOT NULL,
  customers_basket_quantity float NOT NULL   ,
  final_price decimal(15,4) NOT NULL  , 
  customers_basket_date_added varchar(8)   ,
  PRIMARY KEY  (customers_basket_id)
) ;

CREATE TABLE customers_basket_attributes (
  customers_basket_attributes_id int(11) NOT NULL ,
  customers_id int(11) NOT NULL   ,
  products_id varchar(64) NOT NULL,
  products_options_id varchar(64) NOT NULL   ,
  products_options_value_id int(11) NOT NULL   ,
  products_options_value_text int NULL,
  products_options_sort_order varchar(64) NOT NULL,
  PRIMARY KEY  (customers_basket_attributes_id)
) ;


CREATE TABLE customers_info (
  customers_info_id int(11) NOT NULL   ,
  customers_info_date_of_last_logon datetime   ,
  customers_info_number_of_logons int(5)   ,
  customers_info_date_account_created datetime   ,
  customers_info_date_account_last_modified datetime   ,
  global_product_notifications int(1)   ,
  PRIMARY KEY  (customers_info_id)
) ;


CREATE TABLE db_cache (
  cache_entry_name varchar(64) NOT NULL   ,
  cache_data varchar(64),
  cache_entry_created int(15)   ,
  PRIMARY KEY  (cache_entry_name)
) ;


CREATE TABLE email_archive (
  archive_id int(11) NOT NULL ,
  email_to_name varchar(96) NOT NULL   ,
  email_to_address varchar(96) NOT NULL   ,
  email_from_name varchar(96) NOT NULL   ,
  email_from_address varchar(96) NOT NULL   ,
  email_subject varchar(255) NOT NULL   ,
  email_html varchar(255) NOT NULL,
  email_text varchar(255) NOT NULL,
  date_sent datetime NOT NULL  , 
  module varchar(64) NOT NULL   ,
  PRIMARY KEY  (archive_id)
) ;


CREATE TABLE ezpages (
  pages_id int(11) NOT NULL ,
  languages_id int(11) NOT NULL   ,
  pages_title varchar(64) NOT NULL   ,
  alt_url varchar(255) NOT NULL   ,
  alt_url_external varchar(255) NOT NULL   ,
  pages_html_text varchar(32),
  status_header int(1) NOT NULL   ,
  status_sidebox int(1) NOT NULL   ,
  status_footer int(1) NOT NULL   ,
  status_toc int(1) NOT NULL   ,
  header_sort_order int(3) NOT NULL   ,
  sidebox_sort_order int(3) NOT NULL   ,
  footer_sort_order int(3) NOT NULL   ,
  toc_sort_order int(3) NOT NULL   ,
  page_open_new_window int(1) NOT NULL   ,
  page_is_ssl int(1) NOT NULL   ,
  toc_chapter int(11) NOT NULL   ,
  PRIMARY KEY  (pages_id)
) ;


CREATE TABLE featured (
  featured_id int(11) NOT NULL ,
  products_id int(11) NOT NULL   ,
  featured_date_added datetime   ,
  featured_last_modified datetime   ,
  expires_date date NOT NULL  , 
  date_status_change datetime   ,
  status int(1) NOT NULL   ,
  featured_date_available date NOT NULL  , 
  PRIMARY KEY  (featured_id)
) ;


CREATE TABLE files_uploaded (
  files_uploaded_id int(11) NOT NULL ,
  sesskey varchar(32)   ,
  customers_id int(11)   ,
  files_uploaded_name varchar(64) NOT NULL   ,
  PRIMARY KEY  (files_uploaded_id)
);

CREATE TABLE geo_zones (
  geo_zone_id int(11) NOT NULL ,
  geo_zone_name varchar(32) NOT NULL   ,
  geo_zone_description varchar(255) NOT NULL   ,
  last_modified datetime   ,
  date_added datetime NOT NULL  , 
  PRIMARY KEY  (geo_zone_id)
) ;

CREATE TABLE get_terms_to_filter (
  get_term_name varchar(255) NOT NULL   ,
  get_term_table varchar(64) NOT NULL,
  get_term_name_field varchar(64) NOT NULL,
  PRIMARY KEY  (get_term_name)
) ;


CREATE TABLE group_pricing (
  group_id int(11) NOT NULL ,
  group_name varchar(32) NOT NULL   ,
  group_percentage decimal(5,2) NOT NULL  , 
  last_modified datetime   ,
  date_added datetime NOT NULL  , 
  PRIMARY KEY  (group_id)
) ;


CREATE TABLE languages (
  languages_id int(11) NOT NULL ,
  name varchar(32) NOT NULL   ,
  code char(2) NOT NULL   ,
  image varchar(64)   ,
  directory varchar(32)   ,
  sort_order int(3)   ,
  PRIMARY KEY  (languages_id)
) ;


CREATE TABLE layout_boxes (
  layout_id int(11) NOT NULL ,
  layout_template varchar(64) NOT NULL   ,
  layout_box_name varchar(64) NOT NULL   ,
  layout_box_status int(1) NOT NULL   ,
  layout_box_location int(1) NOT NULL   ,
  layout_box_sort_order int(11) NOT NULL   ,
  layout_box_sort_order_single int(11) NOT NULL   ,
  layout_box_status_single int(4) NOT NULL   ,
  PRIMARY KEY  (layout_id)
) ;

CREATE TABLE manufacturers (
  manufacturers_id int(11) NOT NULL ,
  manufacturers_name varchar(32) NOT NULL   ,
  manufacturers_image varchar(64)   ,
  date_added datetime   ,
  last_modified datetime   ,
  PRIMARY KEY  (manufacturers_id)
) ;


CREATE TABLE manufacturers_info (
  manufacturers_id int(11) NOT NULL   ,
  languages_id int(11) NOT NULL   ,
  manufacturers_url varchar(255) NOT NULL   ,
  url_clicked int(5) NOT NULL   ,
  date_last_click datetime   ,
  PRIMARY KEY  (manufacturers_id,languages_id)
) ;


CREATE TABLE media_clips (
  clip_id int(11) NOT NULL ,
  media_id int(11) NOT NULL   ,
  clip_type int(6) NOT NULL   ,
  clip_filename varchar(255) NOT NULL,
  date_added datetime NOT NULL  , 
  last_modified datetime NOT NULL  ,
  PRIMARY KEY  (clip_id)
) ;


CREATE TABLE media_manager (
  media_id int(11) NOT NULL ,
  media_name varchar(255) NOT NULL   ,
  last_modified datetime NOT NULL  , 
  date_added datetime NOT NULL  , 
  PRIMARY KEY  (media_id)
) ;


CREATE TABLE media_to_products (
  media_id int(11) NOT NULL   ,
  product_id int(11) NOT NULL
) ;

CREATE TABLE media_types (
  type_id int(11) NOT NULL ,
  type_name varchar(64) NOT NULL   ,
  type_ext varchar(8) NOT NULL   ,
  PRIMARY KEY  (type_id)
) ;


CREATE TABLE meta_tags_categories_description (
  categories_id int(11) NOT NULL,
  language_id int(11) NOT NULL   ,
  metatags_title varchar(255) NOT NULL   ,
  metatags_keywords varchar(255),
  metatags_description varchar(255),
  PRIMARY KEY  (categories_id,language_id)
) ;


CREATE TABLE meta_tags_products_description (
  products_id int(11) NOT NULL,
  language_id int(11) NOT NULL   ,
  metatags_title varchar(255) NOT NULL   ,
  metatags_keywords varchar(255),
  metatags_description varchar(255),
  PRIMARY KEY  (products_id,language_id)
) ;

CREATE TABLE music_genre (
  music_genre_id int(11) NOT NULL ,
  music_genre_name varchar(32) NOT NULL   ,
  date_added datetime   ,
  last_modified datetime   ,
  PRIMARY KEY  (music_genre_id)
) ;

CREATE TABLE newsletters (
  newsletters_id int(11) NOT NULL ,
  title varchar(255) NOT NULL   ,
  content varchar(255) NOT NULL,
  content_html varchar(255) NOT NULL,
  module varchar(255) NOT NULL   ,
  date_added datetime NOT NULL  , 
  date_sent datetime   ,
  status int(1)   ,
  locked int(1)   ,
  PRIMARY KEY  (newsletters_id)
) ;


CREATE TABLE orders (
  orders_id int(11) NOT NULL ,
  customers_id int(11) NOT NULL   ,
  customers_name varchar(64) NOT NULL   ,
  customers_company varchar(64)   ,
  customers_street_address varchar(64) NOT NULL   ,
  customers_suburb varchar(32)   ,
  customers_city varchar(32) NOT NULL   ,
  customers_postcode varchar(10) NOT NULL   ,
  customers_state varchar(32)   ,
  customers_country varchar(32) NOT NULL   ,
  customers_telephone varchar(32) NOT NULL   ,
  customers_email_address varchar(96) NOT NULL   ,
  customers_address_format_id int(5) NOT NULL   ,
  delivery_name varchar(64) NOT NULL   ,
  delivery_company varchar(64)   ,
  delivery_street_address varchar(64) NOT NULL   ,
  delivery_suburb varchar(32)   ,
  delivery_city varchar(32) NOT NULL   ,
  delivery_postcode varchar(10) NOT NULL   ,
  delivery_state varchar(32)   ,
  delivery_country varchar(32) NOT NULL   ,
  delivery_address_format_id int(5) NOT NULL   ,
  billing_name varchar(64) NOT NULL   ,
  billing_company varchar(64)   ,
  billing_street_address varchar(64) NOT NULL   ,
  billing_suburb varchar(32)   ,
  billing_city varchar(32) NOT NULL   ,
  billing_postcode varchar(10) NOT NULL   ,
  billing_state varchar(32)   ,
  billing_country varchar(32) NOT NULL   ,
  billing_address_format_id int(5) NOT NULL   ,
  payment_method varchar(128) NOT NULL   ,
  payment_module_code varchar(32) NOT NULL   ,
  shipping_method varchar(128) NOT NULL   ,
  shipping_module_code varchar(32) NOT NULL   ,
  coupon_code varchar(32) NOT NULL   ,
  cc_type varchar(20)   ,
  cc_owner varchar(64)   ,
  cc_number varchar(32)   ,
  cc_expires varchar(4)   ,
  cc_cvv varchar(4),
  last_modified datetime   ,
  date_purchased datetime   ,
  orders_status int(5) NOT NULL   ,
  orders_date_finished datetime   ,
  currency char(3)   ,
  currency_value decimal(14,6)   ,
  order_total decimal(14,2)   ,
  order_tax decimal(14,2)   ,
  paypal_ipn_id int(11) NOT NULL   ,
  ip_address varchar(96) NOT NULL   ,
  PRIMARY KEY  (orders_id)
) ;

CREATE TABLE orders_products (
  orders_products_id int(11) NOT NULL ,
  orders_id int(11) NOT NULL   ,
  products_id int(11) NOT NULL   ,
  products_model varchar(32)   ,
  products_name varchar(64) NOT NULL   ,
  products_price decimal(15,4) NOT NULL  , 
  final_price decimal(15,4) NOT NULL  , 
  products_tax decimal(7,4) NOT NULL  , 
  products_quantity float NOT NULL   ,
  onetime_charges decimal(15,4) NOT NULL  ,
  products_priced_by_attribute int(1) NOT NULL   ,
  product_is_free int(1) NOT NULL   ,
  products_discount_type int(1) NOT NULL   ,
  products_discount_type_from int(1) NOT NULL   ,
  products_prid varchar(64) NOT NULL,
  PRIMARY KEY  (orders_products_id)
) ;


CREATE TABLE orders_products_attributes (
  orders_products_attributes_id int(11) NOT NULL ,
  orders_id int(11) NOT NULL   ,
  orders_products_id int(11) NOT NULL   ,
  products_options varchar(32) NOT NULL   ,
  products_options_values varchar(32) NOT NULL,
  options_values_price decimal(15,4) NOT NULL  ,
  price_prefix char(1) NOT NULL   ,
  product_attribute_is_free int(1) NOT NULL   ,
  products_attributes_weight float NOT NULL   ,
  products_attributes_weight_prefix char(1) NOT NULL   ,
  attributes_discounted int(1) NOT NULL   ,
  attributes_price_base_included int(1) NOT NULL   ,
  attributes_price_onetime decimal(15,4) NOT NULL  , 
  attributes_price_factor decimal(15,4) NOT NULL  , 
  attributes_price_factor_offset decimal(15,4) NOT NULL  , 
  attributes_price_factor_onetime decimal(15,4) NOT NULL  ,
  attributes_price_factor_onetime_offset decimal(15,4) NOT NULL  , 
  attributes_qty_prices varchar(32),
  attributes_qty_prices_onetime varchar(32),
  attributes_price_words decimal(15,4) NOT NULL  , 
  attributes_price_words_free int(4) NOT NULL   ,
  attributes_price_letters decimal(15,4) NOT NULL  , 
  attributes_price_letters_free int(4) NOT NULL   ,
  products_options_id int(11) NOT NULL   ,
  products_options_values_id int(11) NOT NULL   ,
  products_prid varchar(32) NOT NULL,
  PRIMARY KEY  (orders_products_attributes_id)
) ;


CREATE TABLE orders_products_download (
  orders_products_download_id int(11) NOT NULL ,
  orders_id int(11) NOT NULL   ,
  orders_products_id int(11) NOT NULL   ,
  orders_products_filename varchar(255) NOT NULL   ,
  download_maxdays int(2) NOT NULL   ,
  download_count int(2) NOT NULL   ,
  products_prid varchar(32) NOT NULL,
  PRIMARY KEY  (orders_products_download_id)
) ;


CREATE TABLE orders_status (
  orders_status_id int(11) NOT NULL   ,
  language_id int(11) NOT NULL   ,
  orders_status_name varchar(32) NOT NULL   ,
  PRIMARY KEY  (orders_status_id,language_id)
) ;

CREATE TABLE orders_status_history (
  orders_status_history_id int(11) NOT NULL ,
  orders_id int(11) NOT NULL   ,
  orders_status_id int(5) NOT NULL   ,
  date_added datetime NOT NULL  , 
  customer_notified int(1)   ,
  comments varchar(32),
  PRIMARY KEY  (orders_status_history_id)
) ;


CREATE TABLE orders_total (
  orders_total_id int(10)  NOT NULL ,
  orders_id int(11) NOT NULL   ,
  title varchar(255) NOT NULL   ,
  text varchar(255) NOT NULL   ,
  value decimal(15,4) NOT NULL  , 
  class varchar(32) NOT NULL   ,
  sort_order int(11) NOT NULL   ,
  PRIMARY KEY  (orders_total_id)
) ;


CREATE TABLE paypal (
  paypal_ipn_id int(11)   NOT NULL ,
  order_id int(11)   NOT NULL   ,
  txn_type varchar(40) NOT NULL   ,
  module_name varchar(40) NOT NULL   ,
  module_mode varchar(40) NOT NULL   ,
  reason_code varchar(40)   ,
  payment_type varchar(40) NOT NULL   ,
  payment_status varchar(32) NOT NULL   ,
  pending_reason varchar(32)   ,
  invoice varchar(128)   ,
  mc_currency char(3) NOT NULL   ,
  first_name varchar(32) NOT NULL   ,
  last_name varchar(32) NOT NULL   ,
  payer_business_name varchar(128)   ,
  address_name varchar(64)   ,
  address_street varchar(254)   ,
  address_city varchar(120)   ,
  address_state varchar(120)   ,
  address_zip varchar(10)   ,
  address_country varchar(64)   ,
  address_status varchar(11)   ,
  payer_email varchar(128) NOT NULL   ,
  payer_id varchar(32) NOT NULL   ,
  payer_status varchar(10) NOT NULL   ,
  payment_date datetime NOT NULL  , 
  business varchar(128) NOT NULL   ,
  receiver_email varchar(128) NOT NULL   ,
  receiver_id varchar(32) NOT NULL   ,
  txn_id varchar(20) NOT NULL   ,
  parent_txn_id varchar(20)   ,
  num_cart_items int(4)   NOT NULL   ,
  mc_gross decimal(7,2) NOT NULL  , 
  mc_fee decimal(7,2) NOT NULL  , 
  payment_gross decimal(7,2)   ,
  payment_fee decimal(7,2)   ,
  settle_amount decimal(7,2)   ,
  settle_currency char(3)   ,
  exchange_rate decimal(4,2)   ,
  notify_version varchar(6) NOT NULL   ,
  verify_sign varchar(128) NOT NULL   ,
  last_modified datetime NOT NULL  , 
  date_added datetime NOT NULL  , 
  memo varchar(32),
  PRIMARY KEY (paypal_ipn_id,txn_id)
) ;

CREATE TABLE paypal_payment_status (
  payment_status_id int(11) NOT NULL ,
  payment_status_name varchar(64) NOT NULL   ,
  PRIMARY KEY (payment_status_id)
) ;


CREATE TABLE paypal_payment_status_history (
  payment_status_history_id int(11) NOT NULL ,
  paypal_ipn_id int(11) NOT NULL   ,
  txn_id varchar(64) NOT NULL   ,
  parent_txn_id varchar(64) NOT NULL   ,
  payment_status varchar(17) NOT NULL   ,
  pending_reason varchar(14)   ,
  date_added datetime NOT NULL  , 
  PRIMARY KEY (payment_status_history_id)
) ;


CREATE TABLE paypal_session (
  unique_id int(11) NOT NULL ,
  session_id varchar(32) NOT NULL,
  saved_session varchar(4) NOT NULL,
  expiry int(17) NOT NULL   ,
  PRIMARY KEY  (unique_id)
) ;


CREATE TABLE paypal_testing (
  paypal_ipn_id int(11)   NOT NULL ,
  order_id int(11)   NOT NULL   ,
  custom varchar(255) NOT NULL   ,
  txn_type varchar(40) NOT NULL   ,
  module_name varchar(40) NOT NULL   ,
  module_mode varchar(40) NOT NULL   ,
  reason_code varchar(40)   ,
  payment_type varchar(40) NOT NULL   ,
  payment_status varchar(32) NOT NULL   ,
  pending_reason varchar(32)   ,
  invoice varchar(128)   ,
  mc_currency char(3) NOT NULL   ,
  first_name varchar(32) NOT NULL   ,
  last_name varchar(32) NOT NULL   ,
  payer_business_name varchar(128)   ,
  address_name varchar(64)   ,
  address_street varchar(254)   ,
  address_city varchar(120)   ,
  address_state varchar(120)   ,
  address_zip varchar(10)   ,
  address_country varchar(64)   ,
  address_status varchar(11)   ,
  payer_email varchar(128) NOT NULL   ,
  payer_id varchar(32) NOT NULL   ,
  payer_status varchar(10) NOT NULL   ,
  payment_date datetime NOT NULL  , 
  business varchar(128) NOT NULL   ,
  receiver_email varchar(128) NOT NULL   ,
  receiver_id varchar(32) NOT NULL   ,
  txn_id varchar(20) NOT NULL   ,
  parent_txn_id varchar(20)   ,
  num_cart_items int(4)   NOT NULL   ,
  mc_gross decimal(7,2) NOT NULL  , 
  mc_fee decimal(7,2) NOT NULL  , 
  payment_gross decimal(7,2)   ,
  payment_fee decimal(7,2)   ,
  settle_amount decimal(7,2)   ,
  settle_currency char(3)   ,
  exchange_rate decimal(4,2)   ,
  notify_version decimal(2,1) NOT NULL  , 
  verify_sign varchar(128) NOT NULL   ,
  last_modified datetime NOT NULL  , 
  date_added datetime NOT NULL  , 
  memo varchar(32),
  PRIMARY KEY  (paypal_ipn_id,txn_id)
) ;

CREATE TABLE product_music_extra (
  products_id int(11) NOT NULL   ,
  artists_id int(11) NOT NULL   ,
  record_company_id int(11) NOT NULL   ,
  music_genre_id int(11) NOT NULL   ,
  PRIMARY KEY  (products_id)
) ;

CREATE TABLE product_type_layout (
  configuration_id int(11) NOT NULL ,
  configuration_title varchar(32) NOT NULL,
  configuration_key varchar(255) NOT NULL   ,
  configuration_value varchar(32) NOT NULL,
  configuration_description varchar(32) NOT NULL,
  product_type_id int(11) NOT NULL   ,
  sort_order int(5)   ,
  last_modified datetime   ,
  date_added datetime NOT NULL ,
  use_function  varchar(255)  ,
  set_function  varchar(255)  ,
  PRIMARY KEY  (configuration_id)
) ;


CREATE TABLE product_types (
  type_id int(11) NOT NULL ,
  type_name varchar(255) NOT NULL   ,
  type_handler varchar(255) NOT NULL   ,
  type_master_type int(11) NOT NULL   ,
  allow_add_to_cart char(1) NOT NULL  ,
  default_image varchar(255) NOT NULL   ,
  date_added datetime NOT NULL  , 
  last_modified datetime NOT NULL  ,
  PRIMARY KEY  (type_id)
) ;

CREATE TABLE product_types_to_category (
  product_type_id int(11) NOT NULL   ,
  category_id int(11) NOT NULL 
) ;

CREATE TABLE products (
  products_id int(11) NOT NULL ,
  products_type int(11) NOT NULL   ,
  products_quantity float NOT NULL   ,
  products_model varchar(32)   ,
  products_image varchar(64)   ,
  products_price decimal(15,4) NOT NULL  ,
  products_virtual int(1) NOT NULL   ,
  products_date_added datetime NOT NULL  , 
  products_last_modified datetime   ,
  products_date_available datetime   ,
  products_weight float NOT NULL   ,
  products_status int(1) NOT NULL   ,
  products_tax_class_id int(11) NOT NULL   ,
  manufacturers_id int(11)   ,
  products_ordered float NOT NULL   ,
  products_quantity_order_min float NOT NULL   ,
  products_quantity_order_units float NOT NULL   ,
  products_priced_by_attribute int(1) NOT NULL   ,
  product_is_free int(1) NOT NULL   ,
  product_is_call int(1) NOT NULL   ,
  products_quantity_mixed int(1) NOT NULL   ,
  product_is_always_free_shipping int(1) NOT NULL   ,
  products_qty_box_status int(1) NOT NULL   ,
  products_quantity_order_max float NOT NULL   ,
  products_sort_order int(11) NOT NULL   ,
  products_discount_type int(1) NOT NULL   ,
  products_discount_type_from int(1) NOT NULL   ,
  products_price_sorter decimal(15,4) NOT NULL  ,
  master_categories_id int(11) NOT NULL   ,
  products_mixed_discount_quantity int(1) NOT NULL   ,
  metatags_title_status int(1) NOT NULL   ,
  metatags_products_name_status int(1) NOT NULL   ,
  metatags_model_status int(1) NOT NULL   ,
  metatags_price_status int(1) NOT NULL   ,
  metatags_title_tagline_status int(1) NOT NULL   ,
  PRIMARY KEY  (products_id)
) ;

CREATE TABLE products_attributes (
  products_attributes_id int(11) NOT NULL ,
  products_id int(11) NOT NULL   ,
  options_id int(11) NOT NULL   ,
  options_values_id int(11) NOT NULL   ,
  options_values_price decimal(15,4) NOT NULL  , 
  price_prefix char(1) NOT NULL   ,
  products_options_sort_order int(11) NOT NULL   ,
  product_attribute_is_free int(1) NOT NULL   ,
  products_attributes_weight float NOT NULL   ,
  products_attributes_weight_prefix char(1) NOT NULL   ,
  attributes_display_only int(1) NOT NULL   ,
  attributes_default int(1) NOT NULL   ,
  attributes_discounted int(1) NOT NULL   ,
  attributes_image varchar(64)   ,
  attributes_price_base_included int(1) NOT NULL   ,
  attributes_price_onetime decimal(15,4) NOT NULL  , 
  attributes_price_factor decimal(15,4) NOT NULL  , 
  attributes_price_factor_offset decimal(15,4) NOT NULL  , 
  attributes_price_factor_onetime decimal(15,4) NOT NULL  , 
  attributes_price_factor_onetime_offset decimal(15,4) NOT NULL  , 
  attributes_qty_prices varchar(32),
  attributes_qty_prices_onetime varchar(32),
  attributes_price_words decimal(15,4) NOT NULL  , 
  attributes_price_words_free int(4) NOT NULL   ,
  attributes_price_letters decimal(15,4) NOT NULL  , 
  attributes_price_letters_free int(4) NOT NULL   ,
  attributes_required int(1) NOT NULL   ,
  PRIMARY KEY  (products_attributes_id)
) ;

CREATE TABLE products_attributes_download (
  products_attributes_id int(11) NOT NULL   ,
  products_attributes_filename varchar(255) NOT NULL   ,
  products_attributes_maxdays int(2)   ,
  products_attributes_maxcount int(2)   ,
  PRIMARY KEY  (products_attributes_id)
) ;

CREATE TABLE products_description (
  products_id int(11) NOT NULL ,
  language_id int(11) NOT NULL   ,
  products_name varchar(64) NOT NULL   ,
  products_description varchar(32),
  products_url varchar(255)   ,
  products_viewed int(5)   ,
  PRIMARY KEY  (products_id,language_id)
) ;

CREATE TABLE products_discount_quantity (
  discount_id int(4) NOT NULL   ,
  products_id int(11) NOT NULL   ,
  discount_qty float NOT NULL   ,
  discount_price decimal(15,4) NOT NULL
) ;

CREATE TABLE products_notifications (
  products_id int(11) NOT NULL   ,
  customers_id int(11) NOT NULL   ,
  date_added datetime NOT NULL  , 
  PRIMARY KEY  (products_id,customers_id)
) ;

CREATE TABLE products_options (
  products_options_id int(11) NOT NULL   ,
  language_id int(11) NOT NULL   ,
  products_options_name varchar(32) NOT NULL   ,
  products_options_sort_order int(11) NOT NULL   ,
  products_options_type int(5) NOT NULL   ,
  products_options_length smallint(2) NOT NULL  ,
  products_options_comment varchar(64)   ,
  products_options_size smallint(2) NOT NULL  , 
  products_options_images_per_row int(2)  , 
  products_options_images_style int(1)   ,
  products_options_rows smallint(2) NOT NULL   ,
  PRIMARY KEY  (products_options_id,language_id)
) ;

CREATE TABLE products_options_types (
  products_options_types_id int(11) NOT NULL   ,
  products_options_types_name varchar(32)   ,
  PRIMARY KEY  (products_options_types_id)
);

CREATE TABLE products_options_values (
  products_options_values_id int(11) NOT NULL   ,
  language_id int(11) NOT NULL   ,
  products_options_values_name varchar(64) NOT NULL   ,
  products_options_values_sort_order int(11) NOT NULL   ,
  PRIMARY KEY (products_options_values_id,language_id)
) ;

CREATE TABLE products_options_values_to_products_options (
  products_options_values_to_products_options_id int(11) NOT NULL ,
  products_options_id int(11) NOT NULL   ,
  products_options_values_id int(11) NOT NULL   ,
  PRIMARY KEY  (products_options_values_to_products_options_id)
) ;

CREATE TABLE products_to_categories (
  products_id int(11) NOT NULL   ,
  categories_id int(11) NOT NULL   ,
  PRIMARY KEY  (products_id,categories_id)
) ;

CREATE TABLE project_version (
  project_version_id int(3) NOT NULL ,
  project_version_key varchar(40) NOT NULL   ,
  project_version_major varchar(20) NOT NULL   ,
  project_version_minor varchar(20) NOT NULL   ,
  project_version_patch1 varchar(20) NOT NULL   ,
  project_version_patch2 varchar(20) NOT NULL   ,
  project_version_patch1_source varchar(20) NOT NULL   ,
  project_version_patch2_source varchar(20) NOT NULL   ,
  project_version_comment varchar(250) NOT NULL   ,
  project_version_date_applied datetime NOT NULL  , 
  PRIMARY KEY  (project_version_id)
);

CREATE TABLE project_version_history (
  project_version_id int(3) NOT NULL ,
  project_version_key varchar(40) NOT NULL   ,
  project_version_major varchar(20) NOT NULL   ,
  project_version_minor varchar(20) NOT NULL   ,
  project_version_patch varchar(20) NOT NULL   ,
  project_version_comment varchar(250) NOT NULL   ,
  project_version_date_applied datetime NOT NULL  , 
  PRIMARY KEY  (project_version_id)
);

CREATE TABLE query_builder (
  query_id int(11) NOT NULL ,
  query_category varchar(40) NOT NULL   ,
  query_name varchar(80) NOT NULL   ,
  query_description varchar(32) NOT NULL,
  query_string varchar(32) NOT NULL,
  query_keys_list varchar(32) NOT NULL,
  PRIMARY KEY  (query_id)
);

CREATE TABLE record_artists (
  artists_id int(11) NOT NULL ,
  artists_name varchar(32) NOT NULL   ,
  artists_image varchar(64)   ,
  date_added datetime   ,
  last_modified datetime   ,
  PRIMARY KEY  (artists_id)
) ;


CREATE TABLE record_artists_info (
  artists_id int(11) NOT NULL   ,
  languages_id int(11) NOT NULL   ,
  artists_url varchar(255) NOT NULL   ,
  url_clicked int(5) NOT NULL   ,
  date_last_click datetime   ,
  PRIMARY KEY  (artists_id,languages_id)
) ;

CREATE TABLE record_company (
  record_company_id int(11) NOT NULL ,
  record_company_name varchar(32) NOT NULL   ,
  record_company_image varchar(64)   ,
  date_added datetime   ,
  last_modified datetime   ,
  PRIMARY KEY  (record_company_id)
) ;

CREATE TABLE record_company_info (
  record_company_id int(11) NOT NULL   ,
  languages_id int(11) NOT NULL   ,
  record_company_url varchar(255) NOT NULL   ,
  url_clicked int(5) NOT NULL   ,
  date_last_click datetime   ,
  PRIMARY KEY  (record_company_id,languages_id)
) ;


CREATE TABLE reviews (
  reviews_id int(11) NOT NULL ,
  products_id int(11) NOT NULL   ,
  customers_id int(11)   ,
  customers_name varchar(64) NOT NULL   ,
  reviews_rating int(1)   ,
  date_added datetime   ,
  last_modified datetime   ,
  reviews_read int(5) NOT NULL   ,
  status int(1) NOT NULL   ,
  PRIMARY KEY  (reviews_id)
) ;

CREATE TABLE reviews_description (
  reviews_id int(11) NOT NULL   ,
  languages_id int(11) NOT NULL   ,
  reviews_text varchar(32) NOT NULL,
  PRIMARY KEY  (reviews_id,languages_id)
) ;

CREATE TABLE salemaker_sales (
  sale_id int(11) NOT NULL ,
  sale_status int(4) NOT NULL   ,
  sale_name varchar(30) NOT NULL   ,
  sale_deduction_value decimal(15,4) NOT NULL  , 
  sale_deduction_type int(4) NOT NULL   ,
  sale_pricerange_from decimal(15,4) NOT NULL  , 
  sale_pricerange_to decimal(15,4) NOT NULL  , 
  sale_specials_condition int(4) NOT NULL   ,
  sale_categories_selected varchar(32),
  sale_categories_all varchar(32),
  sale_date_start date NOT NULL  , 
  sale_date_end date NOT NULL  , 
  sale_date_added date NOT NULL  , 
  sale_date_last_modified date NOT NULL  , 
  sale_date_status_change date NOT NULL  , 
  PRIMARY KEY  (sale_id)
) ;

CREATE TABLE sessions (
  sesskey varchar(64) NOT NULL   ,
  expiry int(11)   NOT NULL   ,
  value int NOT NULL,
  PRIMARY KEY  (sesskey)
) ;


CREATE TABLE specials (
  specials_id int(11) NOT NULL ,
  products_id int(11) NOT NULL   ,
  specials_new_products_price decimal(15,4) NOT NULL  , 
  specials_date_added datetime   ,
  specials_last_modified datetime   ,
  expires_date date NOT NULL  , 
  date_status_change datetime   ,
  status int(1) NOT NULL   ,
  specials_date_available date NOT NULL  , 
  PRIMARY KEY  (specials_id)
) ;

CREATE TABLE tax_class (
  tax_class_id int(11) NOT NULL ,
  tax_class_title varchar(32) NOT NULL   ,
  tax_class_description varchar(255) NOT NULL   ,
  last_modified datetime   ,
  date_added datetime NOT NULL  ,
  PRIMARY KEY  (tax_class_id)
) ;

  tax_rates_id int(11) NOT NULL ,
  tax_zone_id int(11) NOT NULL   ,
  tax_class_id int(11) NOT NULL   ,
  tax_priority int(5)   ,
  tax_rate decimal(7,4) NOT NULL  , 
  tax_description varchar(255) NOT NULL   ,
  last_modified datetime   ,
  date_added datetime NOT NULL  , 
  PRIMARY KEY  (tax_rates_id)
) ;

CREATE TABLE template_select (
  template_id int(11) NOT NULL ,
  template_dir varchar(64) NOT NULL   ,
  template_language varchar(64) NOT NULL   ,
  PRIMARY KEY  (template_id)
) ;

CREATE TABLE whos_online (
  customer_id int(11)   ,
  full_name varchar(64) NOT NULL   ,
  session_id varchar(128) NOT NULL   ,
  ip_address varchar(45) NOT NULL   ,
  time_entry varchar(14) NOT NULL   ,
  time_last_click varchar(14) NOT NULL   ,
  last_page_url varchar(255) NOT NULL   ,
  host_address varchar(32) NOT NULL,
  user_agent varchar(255) NOT NULL 
) ;

CREATE TABLE zones (
  zone_id int(11) NOT NULL ,
  zone_country_id int(11) NOT NULL   ,
  zone_code varchar(32) NOT NULL   ,
  zone_name varchar(32) NOT NULL   ,
  PRIMARY KEY  (zone_id)
) ;

CREATE TABLE zones_to_geo_zones (
  association_id int(11) NOT NULL ,
  zone_country_id int(11) NOT NULL   ,
  zone_id int(11)   ,
  geo_zone_id int(11)   ,
  last_modified datetime   ,
  date_added datetime NOT NULL  ,
  PRIMARY KEY  (association_id)
) ;


CREATE TABLE customers_wishlist (
  products_id int(13) NOT NULL   ,
  customers_id int(13) NOT NULL   ,
  products_model varchar(13)   ,
  products_name varchar(64) NOT NULL   ,
  products_price decimal(8,2) NOT NULL  ,
  final_price decimal(8,2) NOT NULL  , 
  products_quantity int(2) NOT NULL   ,
  wishlist_name varchar(64) NOT  NULL
);



CREATE TABLE paypal_ipn (
  paypal_ipn_id int(10)   NOT NULL ,
  txn_type int(10)   NOT NULL   ,
  reason_code int(11)   ,
  payment_type int(11) NOT NULL   ,
  payment_status int(11) NOT NULL   ,
  pending_reason int(11)   ,
  invoice varchar(64)   ,
  mc_currency int(11) NOT NULL   ,
  first_name varchar(32) NOT NULL   ,
  last_name varchar(32) NOT NULL   ,
  payer_business_name varchar(32)   ,
  address_name varchar(32) NOT NULL   ,
  address_street varchar(64) NOT NULL   ,
  address_city varchar(32) NOT NULL   ,
  address_state varchar(32) NOT NULL   ,
  address_zip varchar(64) NOT NULL   ,
  address_country varchar(32) NOT NULL   ,
  address_status varchar(64) NOT NULL   ,
  address_owner varchar(64) NOT NULL   ,
  payer_email varchar(96) NOT NULL   ,
  ebay_address_id varchar(96)   ,
  payer_id varchar(32) NOT NULL   ,
  payer_status varchar(32) NOT NULL   ,
  payment_date varchar(32) NOT NULL   ,
  business varchar(32) NOT NULL   ,
  receiver_email varchar(96) NOT NULL   ,
  receiver_id varchar(32) NOT NULL   ,
  paypal_address_id varchar(64) NOT NULL   ,
  txn_id varchar(17) NOT NULL   ,
  notify_version varchar(17) NOT NULL   ,
  verify_sign varchar(64) NOT NULL   ,
  date_added datetime NOT NULL  , 
  PRIMARY KEY  (paypal_ipn_id,txn_id)
);

CREATE TABLE paypal_ipn_address_status (
  address_status_id int(11) NOT NULL ,
  language_id int(11) NOT NULL   ,
  address_status_name varchar(64) NOT NULL   ,
  PRIMARY KEY  (address_status_id,language_id)
);




CREATE TABLE paypal_ipn_mc_currency (
  mc_currency_id int(11) NOT NULL ,
  language_id int(11) NOT NULL   ,
  mc_currency_name varchar(64) NOT NULL   ,
  PRIMARY KEY  (mc_currency_id,language_id)
);




CREATE TABLE paypal_ipn_orders (
  paypal_ipn_orders_id int(11) NOT NULL ,
  paypal_ipn_id int(11) NOT NULL   ,
  num_cart_items int(4) NOT NULL   ,
  mc_gross decimal(7,2) NOT NULL  , 
  mc_fee decimal(7,2) NOT NULL  , 
  payment_gross decimal(7,2) NOT NULL  , 
  payment_fee decimal(7,2) NOT NULL  , 
  settle_amount decimal(7,2) NOT NULL  , 
  settle_currency decimal(7,2) NOT NULL  , 
  exchange_rate decimal(7,2)  , 
  PRIMARY KEY  (paypal_ipn_orders_id,paypal_ipn_id)
);

CREATE TABLE paypal_ipn_orders_memo (
  orders_memo_id int(11) NOT NULL ,
  paypal_ipn_id int(11) NOT NULL   ,
  memo varchar(32),
  PRIMARY KEY  (orders_memo_id,paypal_ipn_id)
);

CREATE TABLE paypal_ipn_payment_status (
  payment_status_id int(11) NOT NULL ,
  language_id int(11) NOT NULL   ,
  payment_status_name varchar(64) NOT NULL   ,
  PRIMARY KEY  (payment_status_id,language_id)
);




CREATE TABLE paypal_ipn_payment_type (
  payment_type_id int(11) NOT NULL ,
  language_id int(11) NOT NULL   ,
  payment_type_name varchar(64) NOT NULL   ,
  PRIMARY KEY  (payment_type_id,language_id)
);



CREATE TABLE paypal_ipn_pending_reason (
  pending_reason_id int(11) NOT NULL ,
  language_id int(11) NOT NULL   ,
  pending_reason_name varchar(64) NOT NULL   ,
  PRIMARY KEY  (pending_reason_id,language_id)
);



CREATE TABLE paypal_ipn_reason_code (
  reason_code_id int(11) NOT NULL ,
  language_id int(11) NOT NULL   ,
  reason_code_name varchar(64) NOT NULL   ,
  PRIMARY KEY  (reason_code_id,language_id)
);



CREATE TABLE paypal_ipn_txn_type (
  txn_type_id int(11) NOT NULL ,
  language_id int(11) NOT NULL  , 
  txn_type_name varchar(64) NOT NULL   ,
  PRIMARY KEY  (txn_type_id,language_id)
);


