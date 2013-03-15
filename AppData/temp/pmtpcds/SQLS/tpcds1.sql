-- $Id: query15.tpl , v 1.4 2006/08/28 18:09:17 jms Exp $

 SELECT ca_zip ,  sum(cs_sales_price)
 FROM catalog_sales, customer, customer_address, date_dim
 WHERE cs_bill_customer_sk = c_customer_sk
    and c_current_addr_sk = ca_address_sk 
    and ( ca_zip in ('85669' ,  '86197' , '88274' , '83405' , '86475' ,  '85392' ,  '85460' ,  '80348' ,  '81792')
          or 
          ca_state in ('CA' , 'WA' , 'GA')
          or
          cs_sales_price > 500
        )
    and cs_sold_date_sk = d_date_sk
    and d_qoy = 11 and d_year = 1999
 GROUP BY ca_zip;

-- $Id: query19.tpl , v 1.5 2006/08/28 18:09:17 jms Exp $

 SELECT i_brand_id brand_id ,  i_brand brand ,  i_manufact_id ,  i_manufact ,     sum(ss_ext_sales_price) ext_price
 FROM date_dim ,  store_sales ,  item , customer , customer_address , sstore
 WHERE d_date_sk = ss_sold_date_sk and ss_item_sk = i_item_sk and i_manager_id=8 and d_moy=11 and d_year=1998 and ss_customer_sk = c_customer_sk 
   and c_current_addr_sk = ca_address_sk and ca_zip <> s_zip  and ss_store_sk = s_store_sk 
 GROUP BY i_brand ,  i_brand_id , i_manufact_id ,  i_manufact
 ORDER BY ext_price desc ,  i_brand ,  i_brand_id , i_manufact_id ,  i_manufact;

-- $Id: query25.tpl , v 1.4 2006/08/28 18:09:17 jms Exp $

 SELECT i_item_id , i_item_desc , s_store_id , s_store_name , sum(ss_net_profit) as store_sales_profit , sum(sr_net_loss) as store_returns_loss , sum(cs_net_profit) as catalog_sales_profit
 FROM  store_sales , store_returns , catalog_sales , date_dim d1 , date_dim d2 , date_dim d3 , sstore , item
 WHERE
 d1.d_moy = 4
 and d1.d_year = 2001
 and d1.d_date_sk = ss_sold_date_sk
 and i_item_sk = ss_item_sk
 and s_store_sk = ss_store_sk
 and ss_customer_sk = sr_customer_sk
 and ss_item_sk = sr_item_sk
 and ss_ticket_number = sr_ticket_number
 and sr_returned_date_sk = d2.d_date_sk
 and d2.d_moy between 4 and 10
 and d2.d_year = 2001
 and sr_customer_sk = cs_bill_customer_sk
 and sr_item_sk = cs_item_sk
 and cs_sold_date_sk = d3.d_date_sk
 and d3.d_moy between 4 and 10
 and d3.d_year = 2001
 GROUP BY i_item_id, i_item_desc, s_store_id, s_store_name
 ORDER BY i_item_id, i_item_desc, s_store_id, s_store_name;

-- $Id: query26.tpl , v 1.3 2006/08/28 18:09:17 jms Exp $

 SELECT i_item_id, avg(cs_quantity) agg1, avg(cs_list_price) agg2, avg(cs_coupon_amt) agg3, avg(cs_sales_price) agg4
 FROM catalog_sales, customer_demographics, date_dim, item, promotion
 WHERE cs_sold_date_sk = d_date_sk and cs_item_sk = i_item_sk and cs_bill_cdemo_sk = cd_demo_sk and cs_promo_sk = p_promo_sk and cd_gender = 'M'
       and  cd_marital_status = 'S' and cd_education_status = 'College' and (p_channel_email = 'N' or p_channel_event = 'N') and d_year = 2000
 GROUP BY i_item_id;

-- $Id: query28.tpl , v 1.6 2006/08/28 18:09:17 jms Exp $

 SELECT distinct(c_last_name)
 FROM customer c1
 WHERE c_salutation = 'Mr.'
   and (SELECT count(*) as customer_cnt
        FROM customer
        WHERE (c_last_name = c1.c_last_name and
        ((c_preferred_cust_flag = 'Y' and 
        (c_birth_month = 6 or c_birth_month = 4) and
        (c_birth_year = 1931 or c_birth_year= 1935)
        ) or
        (c_preferred_cust_flag = 'N' and
        (c_birth_month = 8 or c_birth_month = 3) and
        (c_birth_year = 1926 or c_birth_year= 1979)
        )))) > 0
 ORDER BY c_last_name;

-- $Id: query29.tpl , v 1.6 2006/08/28 18:09:17 jms Exp $

 SELECT i_item_id, i_item_desc, s_store_id, s_store_name, sum(ss_quantity) as store_sales_quantity, sum(sr_return_quantity) as store_returns_quantity, sum(cs_quantity) as catalog_sales_quantity
 FROM   store_sales , store_returns , catalog_sales , date_dim d1 , date_dim d2 , date_dim d3 , sstore , item
 WHERE
     d1.d_moy               = 4
 and d1.d_year              = 1999
 and d1.d_date_sk           = ss_sold_date_sk
 and i_item_sk              = ss_item_sk
 and s_store_sk             = ss_store_sk
 and ss_customer_sk         = sr_customer_sk
 and ss_item_sk             = sr_item_sk
 and ss_ticket_number       = sr_ticket_number
 and sr_returned_date_sk    = d2.d_date_sk
 and d2.d_moy               between 4 and  7
 and d2.d_year              = 1999
 and sr_customer_sk         = cs_bill_customer_sk
 and sr_item_sk             = cs_item_sk
 and cs_sold_date_sk        = d3.d_date_sk     
 and d3.d_year              in (1999 , 2000 , 2001)
 GROUP BY i_item_id , i_item_desc , s_store_id , s_store_name
 ORDER BY i_item_id  , i_item_desc , s_store_id , s_store_name;

-- $Id: query30.tpl , v 1.5 2006/08/28 18:09:17 jms Exp $

 CREATE VIEW customer_total_retrn as
 SELECT wr_returning_customer_sk as ctr_customer_sk, ca_state as ctr_state, sum(wr_return_amt) as ctr_total_return
 FROM web_returns , date_dim , customer_address
 WHERE wr_returned_date_sk = d_date_sk 
   and d_year =2002
   and wr_returning_addr_sk = ca_address_sk 
 GROUP BY wr_returning_customer_sk ,  ca_state;

 SELECT c_customer_id , c_salutation , c_first_name , c_last_name , c_preferred_cust_flag , c_birth_day , c_birth_month , c_birth_year , c_birth_country , c_login , c_email_address , c_last_review_date , ctr_total_return
 FROM customer_total_retrn ctr1 , customer_address , customer
 WHERE ctr1.ctr_total_return > (SELECT avg(ctr_total_return)*1.2
			        FROM customer_total_retrn ctr2 
                                WHERE ctr1.ctr_state = ctr2.ctr_state
			       )
       and ca_address_sk = c_current_addr_sk
       and ca_state = 'GA'
       and ctr1.ctr_customer_sk = c_customer_sk
 ORDER BY c_customer_id , c_salutation , c_first_name , c_last_name , c_preferred_cust_flag , c_birth_day , c_birth_month , c_birth_year , c_birth_country , c_login , c_email_address , c_last_review_date , ctr_total_return;

-- $Id: query34.tpl , v 1.8 2006/08/28 18:09:17 jms Exp $

CREATE VIEW view34 AS
SELECT ss_ticket_number , ss_customer_sk , count(*) cnt
FROM store_sales , date_dim , sstore , household_demographics
WHERE store_sales.ss_sold_date_sk = date_dim.d_date_sk
and store_sales.ss_store_sk = sstore.s_store_sk  
and store_sales.ss_hdemo_sk = household_demographics.hd_demo_sk
and (date_dim.d_dom between 1 and 3 or date_dim.d_dom between 25 and 28)
and (household_demographics.hd_buy_potential = 'unknown' or household_demographics.hd_buy_potential = '10000')
and household_demographics.hd_vehicle_count > 0
--           and (case when household_demographics.hd_vehicle_count > 0  then household_demographics.hd_dep_count/ household_demographics.hd_vehicle_count else null end)  > 1.2
and date_dim.d_year in (1999 , 2000 , 2001)
and sstore.s_county in ('Williamson County' , 'Williamson County' , 'Williamson County' , 'Williamson County' ,  'Williamson County' , 'Williamson County' , 'Williamson County' , 'Williamson County')
GROUP BY ss_ticket_number , ss_customer_sk;

 SELECT c_last_name , c_first_name , c_salutation , c_preferred_cust_flag  , ss_ticket_number , cnt
 FROM   view34 dn , customer
 WHERE ss_customer_sk = c_customer_sk and cnt between 15 and 20
 ORDER BY c_last_name , c_first_name , c_salutation , c_preferred_cust_flag desc;

-- $Id: query35.tpl , v 1.4 2006/08/28 18:09:17 jms Exp $
--   
--  SELECT ca_state, cd_gender, cd_marital_status, count(*), min(cd_dep_count), max(cd_dep_count), avg(cd_dep_count), cd_dep_employed_count, count(*), min(cd_dep_employed_count), max(cd_dep_employed_count), avg(cd_dep_employed_count), cd_dep_college_count, count(*), min(cd_dep_college_count), max(cd_dep_college_count), avg(cd_dep_college_count)
--  FROM   customer c , customer_address ca , customer_demographics
--  WHERE
--   c.c_current_addr_sk = ca.ca_address_sk and
--   cd_demo_sk = c.c_current_cdemo_sk and 
--   exists (SELECT *
--           FROM store_sales , date_dim
--           WHERE c.c_customer_sk = ss_customer_sk and
--                 ss_sold_date_sk = d_date_sk and
--                 d_year = 2002 and
--                 d_qoy < 4) and
--    (exists (SELECT *
--             FROM web_sales , date_dim
--             WHERE c.c_customer_sk = ws_bill_customer_sk and
--                   ws_sold_date_sk = d_date_sk and
--                   d_year = 2002 and
--                   d_qoy < 4) or 
--     exists (SELECT * 
--             FROM catalog_sales , date_dim
--             WHERE c.c_customer_sk = cs_ship_customer_sk and
--                   cs_sold_date_sk = d_date_sk and
--                   d_year = 2002 and
--                   d_qoy < 4))
--  GROUP BY ca_state ,  cd_gender ,  cd_marital_status ,  cd_dep_count ,  cd_dep_employed_count ,  cd_dep_college_count
--  ORDER BY ca_state ,  cd_gender ,  cd_marital_status ,  cd_dep_count ,  cd_dep_employed_count ,  cd_dep_college_count ;
 
-- $Id: query37.tpl , v 1.6 2006/08/28 18:09:17 jms Exp $

 SELECT i_item_id , i_item_desc , i_current_price
 FROM item ,  inventory ,  date_dim ,  catalog_sales
 WHERE i_current_price between 28 and 58
 and inv_item_sk = i_item_sk
 and d_date_sk=inv_date_sk
 and d_date between '2000-02-01' and '2000-04-02'
 and i_manufact_id in (516 , 909 , 76 , 320)
 and inv_quantity_on_hand between 100 and 500
 and cs_item_sk = i_item_sk
 GROUP BY i_item_id , i_item_desc , i_current_price
 ORDER BY i_item_id;

-- $Id: query38 $

CREATE VIEW view38 AS
SELECT distinct c_last_name ,  c_first_name ,  d_date
FROM store_sales ,  date_dim ,  customer
WHERE store_sales.ss_sold_date_sk = date_dim.d_date_sk
and store_sales.ss_customer_sk = customer.c_customer_sk
and d_year = 2001
intersect
SELECT distinct c_last_name ,  c_first_name ,  d_date
FROM catalog_sales ,  date_dim ,  customer
WHERE catalog_sales.cs_sold_date_sk = date_dim.d_date_sk and catalog_sales.cs_bill_customer_sk = customer.c_customer_sk
and d_year = 2001
intersect
SELECT distinct c_last_name ,  c_first_name ,  d_date
FROM web_sales ,  date_dim ,  customer
WHERE web_sales.ws_sold_date_sk = date_dim.d_date_sk
and web_sales.ws_bill_customer_sk = customer.c_customer_sk
and d_year = 2001;

SELECT count(*)
FROM view38 hot_cust;

-- $Id: query42.tpl , v 1.3 2006/08/28 18:09:17 jms Exp $

 SELECT dt.d_year, item.i_category_id, item.i_category, avg(ss_ext_sales_price)
 FROM    date_dim dt, store_sales, item
 WHERE dt.d_date_sk = store_sales.ss_sold_date_sk
    and store_sales.ss_item_sk = item.i_item_sk
    and item.i_manager_id = 1     
    and dt.d_moy=11
    and dt.d_year=2000
 GROUP BY    dt.d_year, item.i_category_id, item.i_category
 ORDER BY    dt.d_year, item.i_category_id, item.i_category;

-- $Id: query44.tpl , v 1.3 2006/08/28 18:09:17 jms Exp $

 SELECT c_customer_id as customer_id , c_last_name || ' ,  ' || c_first_name as customername
 FROM customer , customer_address , customer_demographics , household_demographics , income_band
 WHERE ca_city           =  'Edgewood'
   and c_current_addr_sk = ca_address_sk
   and ib_lower_bound   >=  38128
   and ib_upper_bound   <=  88128
   and ib_income_band_sk = hd_income_band_sk
   and cd_demo_sk = c_current_cdemo_sk
   and hd_demo_sk = c_current_hdemo_sk
 ORDER BY c_customer_id;

-- $Id: query52.tpl , v 1.3 2006/08/28 18:09:18 jms Exp $

 SELECT  dt.d_year, item.i_brand_id brand_id, item.i_brand brand, sum(ss_ext_sales_price) ext_price
 FROM date_dim dt , store_sales , item
 WHERE dt.d_date_sk = store_sales.ss_sold_date_sk and store_sales.ss_item_sk = item.i_item_sk and item.i_manager_id = 1 and dt.d_moy=11 and dt.d_year=2000
 GROUP BY dt.d_year, item.i_brand, item.i_brand_id
 ORDER BY dt.d_year, ext_price desc, brand_id;

-- $Id: query55.tpl , v 1.6 2006/08/28 18:09:18 jms Exp $

 SELECT i_brand_id brand_id, i_brand brand, sum(ss_ext_sales_price) ext_price
 FROM date_dim, store_sales, item
 WHERE d_date_sk = ss_sold_date_sk
    and ss_item_sk = i_item_sk
    and i_manager_id=28
    and d_moy=11
    and d_year=1999
 GROUP BY i_brand,  i_brand_id
 ORDER BY ext_price desc,  i_brand_id;
