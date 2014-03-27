select countries_id, countries_name from countries ;
select zone_id, zone_name from zones; 
select code, name from languages;
select title, code from currencies;

SELECT pages_id FROM EZPAGES WHERE status_header = 1 and sidebox_sort_order <> 0 ;
SELECT pages_id FROM EZPAGES WHERE status_toc = 1 and toc_sort_order <> 0;
SELECT * FROM EZPAGES WHERE status_footer = 1 and footer_sort_order <> 0 and alt_url_external = 0;

SELECT * FROM  ORDERS;
SELECT last_modified from CONFIGURATION;
