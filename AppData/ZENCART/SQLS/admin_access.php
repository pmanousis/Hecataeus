
 SELECT admin_id from ADMIN;
 SELECT a.*, p.profile_name FROM ADMIN ;
 select admin_id, admin_name, admin_email, admin_pass, pwd_last_change_date, reset_token, failed_logins, lockout_expires, admin_profile from ADMIN where admin_name = 0;
  select admin_name from ADMIN  where admin_id = 0;
  SELECT admin_pass, prev_pass1, prev_pass2, prev_pass3 FROM ADMIN;
  SELECT admin_id FROM ADMIN ;
SELECT * FROM ADMIN_PROFILES;
  SELECT profile_name FROM ADMIN_PROFILES  WHERE profile_id = 0;
  SELECT * FROM PRODUCT_TYPES  WHERE type_handler 0;
  SELECT page_key FROM ADMIN_PAGES_TO_PROFILES WHERE profile_id = 0;
  SELECT admin_id FROM ADMIN  WHERE admin_profile =0;
SELECT profile_id FROM ADMIN_PROFILES  WHERE profile_name =0;
  SELECT menu_key, language_key FROM ADMIN_MENUS;
  SELECT page_key FROM ADMIN_PAGES  WHERE page_key = 0;

