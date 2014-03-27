

CREATE TABLE  address  (
   address_id  int(11) NOT NULL  ,
   customer_id  int(11) NOT NULL,
   firstname  varchar(32) NOT NULL,
   lastname  varchar(32) NOT NULL,
   company  varchar(40) NOT NULL,   
   address_1  varchar(128) NOT NULL,
   address_2  varchar(128) NOT NULL,
   city  varchar(128) NOT NULL,
   postcode  varchar(10) NOT NULL,
   country_id  int(11) NOT NULL,
   zone_id  int(11) NOT NULL,
  PRIMARY KEY ( address_id )
);

CREATE TABLE  affiliate  (
   affiliate_id  int(11) NOT NULL  ,
   firstname  varchar(32) NOT NULL,
   lastname  varchar(32) NOT NULL,
   email  varchar(96) NOT NULL,
   telephone  varchar(32) NOT NULL,
   fax  varchar(32) NOT NULL,
   password  varchar(40) NOT NULL,
   salt  varchar(9) NOT NULL,
   company  varchar(40) NOT NULL,
   website  varchar(255) NOT NULL,
   address_1  varchar(128) NOT NULL,
   address_2  varchar(128) NOT NULL,
   city  varchar(128) NOT NULL,
   postcode  varchar(10) NOT NULL,
   country_id  int(11) NOT NULL,
   zone_id  int(11) NOT NULL,
   code  varchar(64) NOT NULL,
   commission  decimal(4,2) NOT NULL ,
   tax  varchar(64) NOT NULL,
   payment  varchar(6) NOT NULL,
   cheque  varchar(100) NOT NULL,
   paypal  varchar(64) NOT NULL,
   bank_name  varchar(64) NOT NULL,
   bank_branch_number  varchar(64) NOT NULL,
   bank_swift_code  varchar(64) NOT NULL,
   bank_account_name  varchar(64) NOT NULL,
   bank_account_number  varchar(64) NOT NULL,
   ip  varchar(40) NOT NULL,
   status  tinyint(1) NOT NULL,
   approved  tinyint(1) NOT NULL,
   date_added  datetime NOT NULL,
  PRIMARY KEY ( affiliate_id )
);

CREATE TABLE  affiliate_transaction  (
   affiliate_transaction_id  int(11) NOT NULL  ,
   affiliate_id  int(11) NOT NULL,
   order_id  int(11) NOT NULL,
   description  VARCHAR (250) NOT NULL,
   amount  decimal(15,4) NOT NULL,
   date_added  datetime NOT NULL,
  PRIMARY KEY ( affiliate_transaction_id )
);

CREATE TABLE  attribute  (
   attribute_id  int(11) NOT NULL  ,
   attribute_group_id  int(11) NOT NULL,
   sort_order  int(3) NOT NULL,
  PRIMARY KEY ( attribute_id )
);

CREATE TABLE  attribute_description  (
   attribute_id  int(11) NOT NULL,
   language_id  int(11) NOT NULL,
   name  varchar(64) NOT NULL,
  PRIMARY KEY ( attribute_id , language_id )
);

CREATE TABLE  attribute_group  (
   attribute_group_id  int(11) NOT NULL  ,
   sort_order  int(3) NOT NULL,
  PRIMARY KEY ( attribute_group_id )
);



CREATE TABLE  attribute_group_description  (
   attribute_group_id  int(11) NOT NULL,
   language_id  int(11) NOT NULL,
   name  varchar(64) NOT NULL,
  PRIMARY KEY ( attribute_group_id , language_id )
);


CREATE TABLE  banner  (
   banner_id  int(11) NOT NULL  ,
   name  varchar(64) NOT NULL,
   status  tinyint(1) NOT NULL,
  PRIMARY KEY ( banner_id )
);

CREATE TABLE  banner_image  (
   banner_image_id  int(11) NOT NULL  ,
   banner_id  int(11) NOT NULL,
   link  varchar(255) NOT NULL,
   image  varchar(255) NOT NULL,
   sort_order  int(3) NOT NULL,
  PRIMARY KEY ( banner_image_id )
);


CREATE TABLE  banner_image_description  (
   banner_image_id  int(11) NOT NULL,
   language_id  int(11) NOT NULL,
   banner_id  int(11) NOT NULL,
   title  varchar(64) NOT NULL,
  PRIMARY KEY ( banner_image_id , language_id )
);


CREATE TABLE  category  (
   category_id  int(11) NOT NULL  ,
   image  varchar(255) ,
   parent_id  int(11) NOT NULL ,
   top  tinyint(1) NOT NULL,
   column  int(3) NOT NULL,
   sort_order  int(3) NOT NULL ,
   status  tinyint(1) NOT NULL,
   date_added  datetime NOT NULL ,
   date_modified  datetime NOT NULL ,
  PRIMARY KEY ( category_id )
);

CREATE TABLE  category_description  (
   category_id  int(11) NOT NULL,
   language_id  int(11) NOT NULL,
   name  varchar(255) NOT NULL,
   description  VARCHAR (250) NOT NULL,
   meta_title  varchar(255) NOT NULL,
   meta_description  varchar(255) NOT NULL,
   meta_keyword  varchar(255) NOT NULL,
  PRIMARY KEY ( category_id , language_id )
);

CREATE TABLE  category_path  (
   category_id  int(11) NOT NULL,
   path_id  int(11) NOT NULL,
   level  int(11) NOT NULL,
  PRIMARY KEY ( category_id , path_id )
);

CREATE TABLE  category_filter  (
   category_id  int(11) NOT NULL,
   filter_id  int(11) NOT NULL,
  PRIMARY KEY ( category_id , filter_id )
);


CREATE TABLE  category_to_layout  (
   category_id  int(11) NOT NULL,
   store_id  int(11) NOT NULL,
   layout_id  int(11) NOT NULL,
  PRIMARY KEY ( category_id , store_id )
);

CREATE TABLE  category_to_store  (
   category_id  int(11) NOT NULL,
   store_id  int(11) NOT NULL,
  PRIMARY KEY ( category_id , store_id )
);

CREATE TABLE  country  (
   country_id  int(11) NOT NULL  ,
   name  varchar(128) NOT NULL,
   iso_code_2  varchar(2) NOT NULL,
   iso_code_3  varchar(3) NOT NULL,
   address_format  VARCHAR (250) NOT NULL,
   postcode_required  tinyint(1) NOT NULL,
   status  tinyint(1) NOT NULL,
  PRIMARY KEY ( country_id )
);

CREATE TABLE  coupon  (
   coupon_id  int(11) NOT NULL  ,
   name  varchar(128) NOT NULL,
   code  varchar(10) NOT NULL,
   type  char(1) NOT NULL,
   discount  decimal(15,4) NOT NULL,
   logged  tinyint(1) NOT NULL,
   shipping  tinyint(1) NOT NULL,
   total  decimal(15,4) NOT NULL,
   date_start  date NOT NULL ,
   date_end  date NOT NULL ,
   uses_total  int(11) NOT NULL,
   uses_customer  varchar(11) NOT NULL,
   status  tinyint(1) NOT NULL,
   date_added  datetime NOT NULL,
  PRIMARY KEY ( coupon_id )
);

CREATE TABLE  coupon_category  (
   coupon_id  int(11) NOT NULL,
   category_id  int(11) NOT NULL,
  PRIMARY KEY ( coupon_id , category_id )
);


CREATE TABLE  coupon_history  (
   coupon_history_id  int(11) NOT NULL  ,
   coupon_id  int(11) NOT NULL,
   order_id  int(11) NOT NULL,
   customer_id  int(11) NOT NULL,
   amount  decimal(15,4) NOT NULL,
   date_added  datetime NOT NULL,
  PRIMARY KEY ( coupon_history_id )
);


CREATE TABLE  coupon_product  (
   coupon_product_id  int(11) NOT NULL  ,
   coupon_id  int(11) NOT NULL,
   product_id  int(11) NOT NULL,
  PRIMARY KEY ( coupon_product_id )
);


CREATE TABLE  currency  (
   currency_id  int(11) NOT NULL  ,
   title  varchar(32) NOT NULL,
   code  varchar(3) NOT NULL,
   symbol_left  varchar(12) NOT NULL,
   symbol_right  varchar(12) NOT NULL,
   decimal_place  char(1) NOT NULL,
   value  float(15,8) NOT NULL,
   status  tinyint(1) NOT NULL,
   date_modified  datetime NOT NULL,
  PRIMARY KEY ( currency_id )
);

CREATE TABLE  customer  (
   customer_id  int(11) NOT NULL  ,
   store_id  int(11) NOT NULL ,
   firstname  varchar(32) NOT NULL,
   lastname  varchar(32) NOT NULL,
   email  varchar(96) NOT NULL,
   telephone  varchar(32) NOT NULL,
   fax  varchar(32) NOT NULL,
   password  varchar(40) NOT NULL,
   salt  varchar(9) NOT NULL,
   cart  VARCHAR (250),
   wishlist  VARCHAR (250),
   newsletter  tinyint(1) NOT NULL ,
   address_id  int(11) NOT NULL ,
   customer_group_id  int(11) NOT NULL,
   ip  varchar(40) NOT NULL,
   status  tinyint(1) NOT NULL,
   approved  tinyint(1) NOT NULL,
   token  varchar(255) NOT NULL,
   date_added  datetime NOT NULL ,
  PRIMARY KEY ( customer_id )
);

CREATE TABLE  customer_activity  (
   activity_id  int(11) NOT NULL  ,
   customer_id  int(11) NOT NULL,
   action  VARCHAR (250) NOT NULL,
   ip  varchar(40) NOT NULL,
   date_added  datetime NOT NULL,
  PRIMARY KEY ( activity_id )
);

CREATE TABLE  customer_field  (
   customer_id  int(11) NOT NULL,
   custom_field_id  int(11) NOT NULL,
   custom_field_value_id  int(11) NOT NULL,
   name  int(128) NOT NULL,
   value  VARCHAR (250) NOT NULL,
   sort_order  int(3) NOT NULL,
  PRIMARY KEY ( customer_id , custom_field_id , custom_field_value_id )
);

CREATE TABLE  customer_group  (
   customer_group_id  int(11) NOT NULL  ,
   approval  int(1) NOT NULL,
   sort_order  int(3) NOT NULL,
  PRIMARY KEY ( customer_group_id )
);

CREATE TABLE  customer_group_description  (
   customer_group_id  int(11) NOT NULL,
   language_id  int(11) NOT NULL,
   name  varchar(32) NOT NULL,
   description  VARCHAR (250) NOT NULL,
  PRIMARY KEY ( customer_group_id , language_id )
);

CREATE TABLE  customer_history  (
   customer_history_id  int(11) NOT NULL  ,
   customer_id  int(11) NOT NULL,
   comment  VARCHAR (250) NOT NULL,
   date_added  datetime NOT NULL,
  PRIMARY KEY ( customer_history_id )
);


CREATE TABLE  customer_ip  (
   customer_ip_id  int(11) NOT NULL  ,
   customer_id  int(11) NOT NULL,
   ip  varchar(40) NOT NULL,
   date_added  datetime NOT NULL,
  PRIMARY KEY ( customer_ip_id )
);


CREATE TABLE  customer_ban_ip  (
   customer_ban_ip_id  int(11) NOT NULL  ,
   ip  varchar(40) NOT NULL,
  PRIMARY KEY ( customer_ban_ip_id )
);


CREATE TABLE  customer_online  (
   ip  varchar(40) NOT NULL,
   customer_id  int(11) NOT NULL,
   url  VARCHAR (250) NOT NULL,
   referer  VARCHAR (250) NOT NULL,
   date_added  datetime NOT NULL,
  PRIMARY KEY ( ip )
);

CREATE TABLE  customer_reward  (
   customer_reward_id  int(11) NOT NULL  ,
   customer_id  int(11) NOT NULL ,
   order_id  int(11) NOT NULL,
   description  VARCHAR (250) NOT NULL,
   points  int(8) NOT NULL ,
   date_added  datetime NOT NULL ,
  PRIMARY KEY ( customer_reward_id )
);

CREATE TABLE  customer_transaction  (
   customer_transaction_id  int(11) NOT NULL  ,
   customer_id  int(11) NOT NULL,
   order_id  int(11) NOT NULL,
   description  VARCHAR (250) NOT NULL,
   amount  decimal(15,4) NOT NULL,
   date_added  datetime NOT NULL,
  PRIMARY KEY ( customer_transaction_id )
);



CREATE TABLE  custom_field  (
   custom_field_id  int(11) NOT NULL  ,
   type  varchar(32) NOT NULL,
   value  VARCHAR (250) NOT NULL,
   location  varchar(32) NOT NULL,
   position  varchar(15) NOT NULL,
   status  tinyint(1) NOT NULL,
   sort_order  int(3) NOT NULL,
  PRIMARY KEY ( custom_field_id )
);


CREATE TABLE  custom_field_customer_group  (
   custom_field_id  int(11) NOT NULL,
   customer_group_id  int(11) NOT NULL,
   required  tinyint(1) NOT NULL,
  PRIMARY KEY ( custom_field_id , customer_group_id )
);


CREATE TABLE  custom_field_description  (
   custom_field_id  int(11) NOT NULL,
   language_id  int(11) NOT NULL,
   name  varchar(128) NOT NULL,
  PRIMARY KEY ( custom_field_id , language_id )
);


CREATE TABLE  custom_field_to_customer_group  (
   custom_field_id  int(11) NOT NULL,
   customer_group_id  int(11) NOT NULL,
  PRIMARY KEY ( custom_field_id , customer_group_id )
);



CREATE TABLE  custom_field_value  (
   custom_field_value_id  int(11) NOT NULL  ,
   custom_field_id  int(11) NOT NULL,
   sort_order  int(3) NOT NULL,
  PRIMARY KEY ( custom_field_value_id )
);


CREATE TABLE  custom_field_value_description  (
   custom_field_value_id  int(11) NOT NULL,
   language_id  int(11) NOT NULL,
   custom_field_id  int(11) NOT NULL,
   name  varchar(128) NOT NULL,
  PRIMARY KEY ( custom_field_value_id , language_id )
);


CREATE TABLE  download  (
   download_id  int(11) NOT NULL  ,
   filename  varchar(128) NOT NULL,
   mask  varchar(128) NOT NULL,
   remaining  int(11) NOT NULL ,
   date_added  datetime NOT NULL ,
  PRIMARY KEY ( download_id )
);


CREATE TABLE  download_description  (
   download_id  int(11) NOT NULL,
   language_id  int(11) NOT NULL,
   name  varchar(64) NOT NULL,
  PRIMARY KEY ( download_id , language_id )
);


CREATE TABLE  filter_group  (
   filter_group_id  int(11) NOT NULL  ,
   sort_order  int(3) NOT NULL,
  PRIMARY KEY ( filter_group_id )
);


CREATE TABLE  filter_group_description  (
   filter_group_id  int(11) NOT NULL,
   language_id  int(11) NOT NULL,
   name  varchar(64) NOT NULL,
  PRIMARY KEY ( filter_group_id , language_id )
);


CREATE TABLE  filter  (
   filter_id  int(11) NOT NULL  ,
   filter_group_id  int(11) NOT NULL,
   sort_order  int(3) NOT NULL,
  PRIMARY KEY ( filter_id )
);


CREATE TABLE  filter_description  (
   filter_id  int(11) NOT NULL,
   language_id  int(11) NOT NULL,
   filter_group_id  int(11) NOT NULL,
   name  varchar(64) NOT NULL,
  PRIMARY KEY ( filter_id , language_id )
);


CREATE TABLE  extension  (
   extension_id  int(11) NOT NULL  ,
   type  varchar(32) NOT NULL,
   code  varchar(32) NOT NULL,
  PRIMARY KEY ( extension_id )
);


CREATE TABLE  geo_zone  (
   geo_zone_id  int(11) NOT NULL  ,
   name  varchar(32) NOT NULL,
   description  varchar(255) NOT NULL,
   date_modified  datetime NOT NULL,
   date_added  datetime NOT NULL ,
  PRIMARY KEY ( geo_zone_id )
);


CREATE TABLE  information  (
   information_id  int(11) NOT NULL  ,
   bottom  int(1) NOT NULL ,
   sort_order  int(3) NOT NULL ,
   status  tinyint(1) NOT NULL ,
  PRIMARY KEY ( information_id )
);


CREATE TABLE  information_description  (
   information_id  int(11) NOT NULL,
   language_id  int(11) NOT NULL,
   title  varchar(64) NOT NULL,
   description  VARCHAR (250) NOT NULL,
   meta_title  varchar(255) NOT NULL,
   meta_description  varchar(255) NOT NULL,
   meta_keyword  varchar(255) NOT NULL,
  PRIMARY KEY ( information_id , language_id )
);

CREATE TABLE  information_to_layout  (
   information_id  int(11) NOT NULL,
   store_id  int(11) NOT NULL,
   layout_id  int(11) NOT NULL,
  PRIMARY KEY ( information_id , store_id )
);


CREATE TABLE  information_to_store  (
   information_id  int(11) NOT NULL,
   store_id  int(11) NOT NULL,
  PRIMARY KEY ( information_id , store_id )
);

CREATE TABLE  language  (
   language_id  int(11) NOT NULL  ,
   name  varchar(32) NOT NULL,
   code  varchar(5) NOT NULL,
   locale  varchar(255) NOT NULL,
   image  varchar(64) NOT NULL,
   directory  varchar(32) NOT NULL,
   filename  varchar(64) NOT NULL,
   sort_order  int(3) NOT NULL ,
   status  tinyint(1) NOT NULL,
  PRIMARY KEY ( language_id )
);


CREATE TABLE  layout  (
   layout_id  int(11) NOT NULL  ,
   name  varchar(64) NOT NULL,
  PRIMARY KEY ( layout_id )
);


CREATE TABLE  layout_route  (
   layout_route_id  int(11) NOT NULL  ,
   layout_id  int(11) NOT NULL,
   store_id  int(11) NOT NULL,
   route  varchar(255) NOT NULL,
  PRIMARY KEY ( layout_route_id )
);


CREATE TABLE  length_class  (
   length_class_id  int(11) NOT NULL  ,
   value  decimal(15,8) NOT NULL,
  PRIMARY KEY ( length_class_id )
);


CREATE TABLE  length_class_description  (
   length_class_id  int(11) NOT NULL  ,
   language_id  int(11) NOT NULL,
   title  varchar(32) NOT NULL,
   unit  varchar(4) NOT NULL,
  PRIMARY KEY ( length_class_id , language_id )
);


CREATE TABLE  location  (
   location_id  int(11) NOT NULL  ,
   name  varchar(32) NOT NULL,
   telephone  varchar(32) NOT NULL,
   fax  varchar(32) NOT NULL,  
   address_1  varchar(128) NOT NULL,
   address_2  varchar(128) NOT NULL,
   city  varchar(128) NOT NULL,
   postcode  varchar(10) NOT NULL,
   country_id  int(11) NOT NULL ,
   zone_id  int(11) NOT NULL,  
   geocode  varchar(32) NOT NULL,
   image  varchar(255),
   open  VARCHAR (250) NOT NULL,
   comment  VARCHAR (250) NOT NULL,
  PRIMARY KEY ( location_id )
);

CREATE TABLE  manufacturer  (
   manufacturer_id  int(11) NOT NULL  ,
   name  varchar(64) NOT NULL,
   image  varchar(255) ,
   sort_order  int(3) NOT NULL,
  PRIMARY KEY ( manufacturer_id )
);


CREATE TABLE  manufacturer_to_store  (
   manufacturer_id  int(11) NOT NULL,
   store_id  int(11) NOT NULL,
  PRIMARY KEY ( manufacturer_id , store_id )
) ;


CREATE TABLE  marketing  (
   marketing_id  int(11) NOT NULL  ,
   name  varchar(32) NOT NULL,
   description  VARCHAR (250) NOT NULL,
   code  varchar(64) NOT NULL,
   clicks  int(5) NOT NULL ,
   date_added  datetime NOT NULL,
  PRIMARY KEY ( marketing_id )
);


CREATE TABLE  modification  (
   modification_id  int(11) NOT NULL  ,
   name  varchar(64) NOT NULL,
   author  varchar(64) NOT NULL,
   version  varchar(32) NOT NULL,
   code  VARCHAR (250) NOT NULL,
   sort_order  int(3) NOT NULL ,
   status  tinyint(1) NOT NULL,
   date_added  datetime NOT NULL,
   date_modified  datetime NOT NULL,
  PRIMARY KEY ( modification_id )
);


CREATE TABLE  option  (
   option_id  int(11) NOT NULL  ,
   type  varchar(32) NOT NULL,
   sort_order  int(3) NOT NULL,
  PRIMARY KEY ( option_id )
);


CREATE TABLE  option_description  (
   option_id  int(11) NOT NULL,
   language_id  int(11) NOT NULL,
   name  varchar(128) NOT NULL,
  PRIMARY KEY ( option_id , language_id )
);


CREATE TABLE  option_value  (
   option_value_id  int(11) NOT NULL  ,
   option_id  int(11) NOT NULL,
   image  varchar(255) NOT NULL,
   sort_order  int(3) NOT NULL,
  PRIMARY KEY ( option_value_id )
);


CREATE TABLE  option_value_description  (
   option_value_id  int(11) NOT NULL,
   language_id  int(11) NOT NULL,
   option_id  int(11) NOT NULL,
   name  varchar(128) NOT NULL,
  PRIMARY KEY ( option_value_id , language_id )
);


CREATE TABLE  myorder  (
   order_id  int(11) NOT NULL  ,
   invoice_no  int(11) NOT NULL ,
   invoice_prefix  varchar(26) NOT NULL,
   store_id  int(11) NOT NULL ,
   store_name  varchar(64) NOT NULL,
   store_url  varchar(255) NOT NULL,
   customer_id  int(11) NOT NULL,
   customer_group_id  int(11) NOT NULL ,
   firstname  varchar(32) NOT NULL,
   lastname  varchar(32) NOT NULL,
   email  varchar(96) NOT NULL,
   telephone  varchar(32) NOT NULL,
   fax  varchar(32) NOT NULL,
   payment_firstname  varchar(32) NOT NULL,
   payment_lastname  varchar(32) NOT NULL,
   payment_company  varchar(40) NOT NULL,  
   payment_address_1  varchar(128) NOT NULL,
   payment_address_2  varchar(128) NOT NULL,
   payment_city  varchar(128) NOT NULL,
   payment_postcode  varchar(10) NOT NULL,
   payment_country  varchar(128) NOT NULL,
   payment_country_id  int(11) NOT NULL,
   payment_zone  varchar(128) NOT NULL,
   payment_zone_id  int(11) NOT NULL,
   payment_address_format  VARCHAR (250) NOT NULL,
   payment_method  varchar(128) NOT NULL,
   payment_code  varchar(128) NOT NULL,
   shipping_firstname  varchar(32) NOT NULL,
   shipping_lastname  varchar(32) NOT NULL,
   shipping_company  varchar(40) NOT NULL,
   shipping_address_1  varchar(128) NOT NULL,
   shipping_address_2  varchar(128) NOT NULL,
   shipping_city  varchar(128) NOT NULL,
   shipping_postcode  varchar(10) NOT NULL,
   shipping_country  varchar(128) NOT NULL,
   shipping_country_id  int(11) NOT NULL,
   shipping_zone  varchar(128) NOT NULL,
   shipping_zone_id  int(11) NOT NULL,
   shipping_address_format  VARCHAR (250) NOT NULL,
   shipping_method  varchar(128) NOT NULL,
   shipping_code  varchar(128) NOT NULL,  
   comment  VARCHAR (250) NOT NULL,
   total  decimal(15,4) NOT NULL ,
   order_status_id  int(11) NOT NULL,
   affiliate_id  int(11) NOT NULL,
   commission  decimal(15,4) NOT NULL,
   marketing_id  int(11) NOT NULL,
   tracking  varchar(64) NOT NULL,
   language_id  int(11) NOT NULL,
   currency_id  int(11) NOT NULL,
   currency_code  varchar(3) NOT NULL,
   currency_value  decimal(15,8) NOT NULL ,
   ip  varchar(40) NOT NULL,
   forwarded_ip  varchar(40) NOT NULL,
   user_agent  varchar(255) NOT NULL,
   accept_language  varchar(255) NOT NULL,
   date_added  datetime NOT NULL,
   date_modified  datetime NOT NULL,
  PRIMARY KEY ( order_id )
);


CREATE TABLE  order_download  (
   order_download_id  int(11) NOT NULL  ,
   order_id  int(11) NOT NULL,
   order_product_id  int(11) NOT NULL,
   name  varchar(64) NOT NULL,
   filename  varchar(128) NOT NULL,
   mask  varchar(128) NOT NULL,
   remaining  int(3) NOT NULL ,
  PRIMARY KEY ( order_download_id )
);


CREATE TABLE  order_fraud  (
   order_id  int(11) NOT NULL,
   customer_id  int(11) NOT NULL,
   country_match  varchar(3) NOT NULL,
   country_code  varchar(2) NOT NULL,
   high_risk_country  varchar(3) NOT NULL,
   distance  int(11) NOT NULL,
   ip_region  varchar(255) NOT NULL,
   ip_city  varchar(255) NOT NULL,
   ip_latitude  decimal(10,6) NOT NULL,
   ip_longitude  decimal(10,6) NOT NULL,
   ip_isp  varchar(255) NOT NULL,
   ip_org  varchar(255) NOT NULL,
   ip_asnum  int(11) NOT NULL,
   ip_user_type  varchar(255) NOT NULL,
   ip_country_confidence  varchar(3) NOT NULL,
   ip_region_confidence  varchar(3) NOT NULL,
   ip_city_confidence  varchar(3) NOT NULL,
   ip_postal_confidence  varchar(3) NOT NULL,
   ip_postal_code  varchar(10) NOT NULL,
   ip_accuracy_radius  int(11) NOT NULL,
   ip_net_speed_cell  varchar(255) NOT NULL,
   ip_metro_code  int(3) NOT NULL,
   ip_area_code  int(3) NOT NULL,
   ip_time_zone  varchar(255) NOT NULL,
   ip_region_name  varchar(255) NOT NULL,
   ip_domain  varchar(255) NOT NULL,
   ip_country_name  varchar(255) NOT NULL,
   ip_continent_code  varchar(2) NOT NULL,
   ip_corporate_proxy  varchar(3) NOT NULL,
   anonymous_proxy  varchar(3) NOT NULL,
   proxy_score  int(3) NOT NULL,
   is_trans_proxy  varchar(3) NOT NULL,
   free_mail  varchar(3) NOT NULL,
   carder_email  varchar(3) NOT NULL,
   high_risk_username  varchar(3) NOT NULL,
   high_risk_password  varchar(3) NOT NULL,
   bin_match  varchar(10) NOT NULL,
   bin_country  varchar(2) NOT NULL,
   bin_name_match  varchar(3) NOT NULL,
   bin_name  varchar(255) NOT NULL,
   bin_phone_match  varchar(3) NOT NULL,
   bin_phone  varchar(32) NOT NULL,
   customer_phone_in_billing_location  varchar(8) NOT NULL,
   ship_forward  varchar(3) NOT NULL,
   city_postal_match  varchar(3) NOT NULL,
   ship_city_postal_match  varchar(3) NOT NULL,
   score  decimal(10,5) NOT NULL,
   explanation  VARCHAR (250) NOT NULL,
   risk_score  decimal(10,5) NOT NULL,
   queries_remaining  int(11) NOT NULL,
   maxmind_id  varchar(8) NOT NULL,
   error  VARCHAR (250) NOT NULL,
   date_added  datetime NOT NULL,
  PRIMARY KEY ( order_id )
);


CREATE TABLE  order_field  (
   order_id  int(11) NOT NULL,
   custom_field_id  int(11) NOT NULL,
   custom_field_value_id  int(11) NOT NULL,
   name  int(128) NOT NULL,
   value  VARCHAR (250) NOT NULL,
   sort_order  int(3) NOT NULL,
  PRIMARY KEY ( order_id , custom_field_id , custom_field_value_id )
);

CREATE TABLE  order_history  (
   order_history_id  int(11) NOT NULL  ,
   order_id  int(11) NOT NULL,
   order_status_id  int(5) NOT NULL,
   notify  tinyint(1) NOT NULL ,
   comment  VARCHAR (250) NOT NULL,
   date_added  datetime NOT NULL ,
  PRIMARY KEY ( order_history_id )
);


CREATE TABLE  order_option  (
   order_option_id  int(11) NOT NULL  ,
   order_id  int(11) NOT NULL,
   order_product_id  int(11) NOT NULL,
   product_option_id  int(11) NOT NULL,
   product_option_value_id  int(11) NOT NULL ,
   name  varchar(255) NOT NULL,
   value  VARCHAR (250) NOT NULL,
   type  varchar(32) NOT NULL,
  PRIMARY KEY ( order_option_id )
);


CREATE TABLE  order_product  (
   order_product_id  int(11) NOT NULL  ,
   order_id  int(11) NOT NULL,
   product_id  int(11) NOT NULL,
   name  varchar(255) NOT NULL,
   model  varchar(64) NOT NULL,
   quantity  int(4) NOT NULL,
   price  decimal(15,4) NOT NULL ,
   total  decimal(15,4) NOT NULL ,
   tax  decimal(15,4) NOT NULL,
   reward  int(8) NOT NULL,
  PRIMARY KEY ( order_product_id )
);


CREATE TABLE  order_status  (
   order_status_id  int(11) NOT NULL  ,
   language_id  int(11) NOT NULL,
   name  varchar(32) NOT NULL,
  PRIMARY KEY ( order_status_id , language_id )
);


CREATE TABLE  order_total  (
   order_total_id  int(10) NOT NULL  ,
   order_id  int(11) NOT NULL,
   code  varchar(32) NOT NULL,
   title  varchar(255) NOT NULL,
   text  varchar(255) NOT NULL,
   value  decimal(15,4) NOT NULL,
   sort_order  int(3) NOT NULL,
  PRIMARY KEY ( order_total_id )
);


CREATE TABLE  order_voucher  (
   order_voucher_id  int(11) NOT NULL  ,
   order_id  int(11) NOT NULL,
   voucher_id  int(11) NOT NULL,
   description  varchar(255) NOT NULL,
   code  varchar(10) NOT NULL,
   from_name  varchar(64) NOT NULL,
   from_email  varchar(96) NOT NULL,
   to_name  varchar(64) NOT NULL,
   to_email  varchar(96) NOT NULL,
   voucher_theme_id  int(11) NOT NULL,
   message  VARCHAR (250) NOT NULL,
   amount  decimal(15,4) NOT NULL,
  PRIMARY KEY ( order_voucher_id )
);

CREATE TABLE  product  (
   product_id  int(11) NOT NULL  ,
   model  varchar(64) NOT NULL,
   sku  varchar(64) NOT NULL,
   upc  varchar(12) NOT NULL,
   ean  varchar(14) NOT NULL,
   jan  varchar(13) NOT NULL,
   isbn  varchar(13) NOT NULL,
   mpn  varchar(64) NOT NULL,
   location  varchar(128) NOT NULL,
   quantity  int(4) NOT NULL ,
   stock_status_id  int(11) NOT NULL,
   image  varchar(255) ,
   manufacturer_id  int(11) NOT NULL,
   shipping  tinyint(1) NOT NULL ,
   price  decimal(15,4) NOT NULL ,
   points  int(8) NOT NULL ,
   tax_class_id  int(11) NOT NULL,
   date_available  date NOT NULL,
   weight  decimal(15,8) NOT NULL ,
   weight_class_id  int(11) NOT NULL ,
   length  decimal(15,8) NOT NULL ,
   width  decimal(15,8) NOT NULL ,
   height  decimal(15,8) NOT NULL ,
   length_class_id  int(11) NOT NULL,
   subtract  tinyint(1) NOT NULL ,
   minimum  int(11) NOT NULL ,
   sort_order  int(11) NOT NULL ,
   status  tinyint(1) NOT NULL ,
   viewed  int(5) NOT NULL ,
   date_added  datetime NOT NULL,
   date_modified  datetime NOT NULL ,
  PRIMARY KEY ( product_id )
);

CREATE TABLE  product_attribute  (
   product_id  int(11) NOT NULL,
   attribute_id  int(11) NOT NULL,
   language_id  int(11) NOT NULL,
   text  VARCHAR (250) NOT NULL,
  PRIMARY KEY ( product_id , attribute_id , language_id )
);


CREATE TABLE  product_description  (
   product_id  int(11) NOT NULL,
   language_id  int(11) NOT NULL,
   name  varchar(255) NOT NULL,
   description  VARCHAR (250) NOT NULL,
   tag  VARCHAR (250) NOT NULL,
   meta_title  varchar(255) NOT NULL,
   meta_description  varchar(255) NOT NULL,
   meta_keyword  varchar(255) NOT NULL,
  PRIMARY KEY ( product_id , language_id )
);


CREATE TABLE  product_discount  (
   product_discount_id  int(11) NOT NULL  ,
   product_id  int(11) NOT NULL,
   customer_group_id  int(11) NOT NULL,
   quantity  int(4) NOT NULL ,
   priority  int(5) NOT NULL ,
   price  decimal(15,4) NOT NULL ,
   date_start  date NOT NULL ,
   date_end  date NOT NULL ,
  PRIMARY KEY ( product_discount_id )
);

CREATE TABLE  product_filter  (
   product_id  int(11) NOT NULL,
   filter_id  int(11) NOT NULL,
  PRIMARY KEY ( product_id , filter_id )
);


CREATE TABLE  product_image  (
   product_image_id  int(11) NOT NULL  ,
   product_id  int(11) NOT NULL,
   image  varchar(255),
   sort_order  int(3) NOT NULL ,
  PRIMARY KEY ( product_image_id )
);


CREATE TABLE  product_option  (
   product_option_id  int(11) NOT NULL  ,
   product_id  int(11) NOT NULL,
   option_id  int(11) NOT NULL,
   value  VARCHAR (250) NOT NULL,
   required  tinyint(1) NOT NULL,
  PRIMARY KEY ( product_option_id )
);

CREATE TABLE  product_option_value  (
   product_option_value_id  int(11) NOT NULL  ,
   product_option_id  int(11) NOT NULL,
   product_id  int(11) NOT NULL,
   option_id  int(11) NOT NULL,
   option_value_id  int(11) NOT NULL,
   quantity  int(3) NOT NULL,
   subtract  tinyint(1) NOT NULL,
   price  decimal(15,4) NOT NULL,
   price_prefix  varchar(1) NOT NULL,
   points  int(8) NOT NULL,
   points_prefix  varchar(1) NOT NULL,
   weight  decimal(15,8) NOT NULL,
   weight_prefix  varchar(1) NOT NULL,
  PRIMARY KEY ( product_option_value_id )
);




CREATE TABLE  product_related  (
   product_id  int(11) NOT NULL,
   related_id  int(11) NOT NULL,
  PRIMARY KEY ( product_id , related_id )
);

CREATE TABLE  product_reward  (
   product_reward_id  int(11) NOT NULL  ,
   product_id  int(11) NOT NULL,
   customer_group_id  int(11) NOT NULL ,
   points  int(8) NOT NULL ,
  PRIMARY KEY ( product_reward_id )
);


CREATE TABLE  product_special  (
   product_special_id  int(11) NOT NULL  ,
   product_id  int(11) NOT NULL,
   customer_group_id  int(11) NOT NULL,
   priority  int(5) NOT NULL ,
   price  decimal(15,4) NOT NULL ,
   date_start  date NOT NULL ,
   date_end  date NOT NULL,
  PRIMARY KEY ( product_special_id )
);


CREATE TABLE  product_to_category  (
   product_id  int(11) NOT NULL,
   category_id  int(11) NOT NULL,
  PRIMARY KEY ( product_id , category_id )
);


CREATE TABLE  product_to_download  (
   product_id  int(11) NOT NULL,
   download_id  int(11) NOT NULL,
  PRIMARY KEY ( product_id , download_id )
);


CREATE TABLE  product_to_layout  (
   product_id  int(11) NOT NULL,
   store_id  int(11) NOT NULL,
   layout_id  int(11) NOT NULL,
  PRIMARY KEY ( product_id , store_id )
);


CREATE TABLE  product_to_store  (
   product_id  int(11) NOT NULL,
   store_id  int(11) NOT NULL,
  PRIMARY KEY ( product_id , store_id )
);

CREATE TABLE profile (
profile_id int(11) NOT NULL,
sort_order int(11) NOT NULL,
status tinyint(4) NOT NULL,
price decimal(10,4) NOT NULL,
frequency varchar(255) NOT NULL,
duration int(10) NOT NULL,
cycle int(10) NOT NULL,
trial_status tinyint(4) NOT NULL,
trial_price decimal(10,4) NOT NULL,
trial_frequency varchar(255) NOT NULL,
trial_duration int(10) NOT NULL,
trial_cycle int(10) NOT NULL,
PRIMARY KEY (profile_id)
);





CREATE TABLE  return  (
   return_id  int(11) NOT NULL  ,
   order_id  int(11) NOT NULL,
   product_id  int(11) NOT NULL,
   customer_id  int(11) NOT NULL,
   firstname  varchar(32) NOT NULL,
   lastname  varchar(32) NOT NULL,
   email  varchar(96) NOT NULL,
   telephone  varchar(32) NOT NULL,
   product  varchar(255) NOT NULL,
   model  varchar(64) NOT NULL,
   quantity  int(4) NOT NULL,
   opened  tinyint(1) NOT NULL,
   return_reason_id  int(11) NOT NULL,
   return_action_id  int(11) NOT NULL,
   return_status_id  int(11) NOT NULL,
   comment  VARCHAR (250),
   date_ordered  date NOT NULL,
   date_added  datetime NOT NULL,
   date_modified  datetime NOT NULL,
  PRIMARY KEY ( return_id )
);


CREATE TABLE  return_action  (
   return_action_id  int(11) NOT NULL  ,
   language_id  int(11) NOT NULL,
   name  varchar(64) NOT NULL,
  PRIMARY KEY ( return_action_id , language_id )
);


CREATE TABLE  return_history  (
   return_history_id  int(11) NOT NULL  ,
   return_id  int(11) NOT NULL,
   return_status_id  int(11) NOT NULL,
   notify  tinyint(1) NOT NULL,
   comment  VARCHAR (250) NOT NULL,
   date_added  datetime NOT NULL,
  PRIMARY KEY ( return_history_id )
);


CREATE TABLE  return_reason  (
   return_reason_id  int(11) NOT NULL  ,
   language_id  int(11) NOT NULL,
   name  varchar(128) NOT NULL,
  PRIMARY KEY ( return_reason_id , language_id )
);


CREATE TABLE  return_status  (
   return_status_id  int(11) NOT NULL  ,
   language_id  int(11) NOT NULL,
   name  varchar(32) NOT NULL,
  PRIMARY KEY ( return_status_id , language_id )
);


CREATE TABLE  review  (
   review_id  int(11) NOT NULL  ,
   product_id  int(11) NOT NULL,
   customer_id  int(11) NOT NULL,
   author  varchar(64) NOT NULL,
   text  VARCHAR (250) NOT NULL,
   rating  int(1) NOT NULL,
   status  tinyint(1) NOT NULL,
   date_added  datetime NOT NULL ,
   date_modified  datetime NOT NULL ,
  PRIMARY KEY ( review_id )
);

CREATE TABLE  setting  (
   setting_id  int(11) NOT NULL  ,
   store_id  int(11) NOT NULL,
   mygroup  varchar(32) NOT NULL,
   key  varchar(64) NOT NULL,
   value  VARCHAR (250) NOT NULL,
   serialized  tinyint(1) NOT NULL,
  PRIMARY KEY ( setting_id )
);

CREATE TABLE  stock_status  (
   stock_status_id  int(11) NOT NULL  ,
   language_id  int(11) NOT NULL,
   name  varchar(32) NOT NULL,
  PRIMARY KEY ( stock_status_id , language_id )
);


CREATE TABLE  store  (
   store_id  int(11) NOT NULL  ,
   name  varchar(64) NOT NULL,
   url  varchar(255) NOT NULL,
   ssl  varchar(255) NOT NULL,
  PRIMARY KEY ( store_id )
);

CREATE TABLE  tax_class  (
   tax_class_id  int(11) NOT NULL  ,
   title  varchar(32) NOT NULL,
   description  varchar(255) NOT NULL,
   date_added  datetime NOT NULL,
   date_modified  datetime NOT NULL,
  PRIMARY KEY ( tax_class_id )
);


CREATE TABLE  tax_rate  (
   tax_rate_id  int(11) NOT NULL  ,
   geo_zone_id  int(11) NOT NULL ,
   name  varchar(32) NOT NULL,
   rate  decimal(15,4) NOT NULL ,
   type  char(1) NOT NULL,
   date_added  datetime NOT NULL,
   date_modified  datetime NOT NULL,
  PRIMARY KEY ( tax_rate_id )
);


CREATE TABLE  tax_rate_to_customer_group  (
   tax_rate_id  int(11) NOT NULL,
   customer_group_id  int(11) NOT NULL,
  PRIMARY KEY ( tax_rate_id , customer_group_id )
);

CREATE TABLE  tax_rule  (
   tax_rule_id  int(11) NOT NULL  ,
   tax_class_id  int(11) NOT NULL,
   tax_rate_id  int(11) NOT NULL,
   based  varchar(10) NOT NULL,
   priority  int(5) NOT NULL ,
  PRIMARY KEY ( tax_rule_id )
);


CREATE TABLE  url_alias  (
   url_alias_id  int(11) NOT NULL  ,
   query  varchar(255) NOT NULL,
   keyword  varchar(255) NOT NULL,
  PRIMARY KEY ( url_alias_id )
);


CREATE TABLE  user  (
   user_id  int(11) NOT NULL  ,
   user_group_id  int(11) NOT NULL,
   username  varchar(20) NOT NULL,
   password  varchar(40) NOT NULL,
   salt  varchar(9) NOT NULL,
   firstname  varchar(32) NOT NULL,
   lastname  varchar(32) NOT NULL,
   email  varchar(96) NOT NULL,
   image  varchar(255) NOT NULL,
   code  varchar(40) NOT NULL,
   ip  varchar(40) NOT NULL,
   status  tinyint(1) NOT NULL,
   date_added  datetime NOT NULL,
  PRIMARY KEY ( user_id )
);


CREATE TABLE  user_group  (
   user_group_id  int(11) NOT NULL  ,
   name  varchar(64) NOT NULL,
   permission  VARCHAR (250) NOT NULL,
  PRIMARY KEY ( user_group_id )
);

CREATE TABLE  voucher  (
   voucher_id  int(11) NOT NULL  ,
   order_id  int(11) NOT NULL,
   code  varchar(10) NOT NULL,
   from_name  varchar(64) NOT NULL,
   from_email  varchar(96) NOT NULL,
   to_name  varchar(64) NOT NULL,
   to_email  varchar(96) NOT NULL,
   voucher_theme_id  int(11) NOT NULL,
   message  VARCHAR (250) NOT NULL,
   amount  decimal(15,4) NOT NULL,
   status  tinyint(1) NOT NULL,
   date_added  datetime NOT NULL,
  PRIMARY KEY ( voucher_id )
);


CREATE TABLE  voucher_history  (
   voucher_history_id  int(11) NOT NULL  ,
   voucher_id  int(11) NOT NULL,
   order_id  int(11) NOT NULL,
   amount  decimal(15,4) NOT NULL,
   date_added  datetime NOT NULL,
  PRIMARY KEY ( voucher_history_id )
);


CREATE TABLE  voucher_theme  (
   voucher_theme_id  int(11) NOT NULL  ,
   image  varchar(255) NOT NULL,
  PRIMARY KEY ( voucher_theme_id )
);


CREATE TABLE  voucher_theme_description  (
   voucher_theme_id  int(11) NOT NULL,
   language_id  int(11) NOT NULL,
   name  varchar(32) NOT NULL,
  PRIMARY KEY ( voucher_theme_id , language_id )
);


CREATE TABLE  weight_class  (
   weight_class_id  int(11) NOT NULL  ,
   value  decimal(15,8) NOT NULL ,
  PRIMARY KEY ( weight_class_id )
);



CREATE TABLE  weight_class_description  (
   weight_class_id  int(11) NOT NULL  ,
   language_id  int(11) NOT NULL,
   title  varchar(32) NOT NULL,
   unit  varchar(4) NOT NULL,
  PRIMARY KEY ( weight_class_id , language_id )
);



CREATE TABLE  zone  (
   zone_id  int(11) NOT NULL  ,
   country_id  int(11) NOT NULL,
   name  varchar(128) NOT NULL,
   code  varchar(32) NOT NULL,
   status  tinyint(1) NOT NULL,
  PRIMARY KEY ( zone_id )
);



CREATE TABLE  zone_to_geo_zone  (
   zone_to_geo_zone_id  int(11) NOT NULL  ,
   country_id  int(11) NOT NULL,
   zone_id  int(11) NOT NULL ,
   geo_zone_id  int(11) NOT NULL,
   date_added  datetime NOT NULL ,
   date_modified  datetime NOT NULL,
  PRIMARY KEY ( zone_to_geo_zone_id )
);

