 -- $Id: query1.tpl , v 1.4 2006/08/28 18:09:17 jms Exp $
 
 CREATE VIEW customer_total_ret as
 SELECT sr_customer_sk as ctr_customer_sk, sr_store_sk as ctr_store_sk, sum(sr_return_amt) as ctr_total_return
 FROM store_returns, date_dim
 WHERE sr_returned_date_sk = d_date_sk and d_year = 2000
 GROUP BY sr_customer_sk,  sr_store_sk;

 SELECT c_customer_id
 FROM customer_total_ret ctr1, sstore, customer
 WHERE ctr1.ctr_total_return >
       (SELECT avg(ctr_total_return)*1.2
        FROM customer_total_ret ctr2 
        WHERE ctr1.ctr_store_sk = ctr2.ctr_store_sk
       )
       and s_store_sk = ctr1.ctr_store_sk
       and s_state = 'TN'
       and ctr1.ctr_customer_sk = c_customer_sk
 ORDER BY c_customer_id;

 -- $Id: query3.tpl , v 1.3 2006/08/28 18:09:17 jms Exp $
   
 SELECT dt.d_year, item.i_brand_id brand_id, item.i_brand brand, sum(ss_ext_sales_price) ext_price
 FROM date_dim dt, store_sales, item
 WHERE dt.d_date_sk = store_sales.ss_sold_date_sk
    and store_sales.ss_item_sk = item.i_item_sk
    and item.i_manufact_id = 725
    and dt.d_moy=11
 GROUP BY dt.d_year, item.i_brand, item.i_brand_id
 ORDER BY dt.d_year, ext_price desc, brand_id;

 -- $Id: query4.tpl , v 1.5 2006/08/28 18:09:17 jms Exp $
 CREATE VIEW year_total as
 SELECT c_customer_id customer_id, c_first_name customer_first_name, c_last_name customer_last_name, c_preferred_cust_flag, c_birth_country, c_login, c_email_address, d_year dyear, sum(((ss_ext_list_price-ss_ext_wholesale_cost-ss_ext_discount_amt)+ss_ext_sales_price)/2) year_total, 's' sale_type
 FROM customer, store_sales, date_dim
 WHERE c_customer_sk = ss_customer_sk and ss_sold_date_sk = d_date_sk
 GROUP BY c_customer_id, c_first_name, c_last_name, c_preferred_cust_flag, c_birth_country, c_login, c_email_address, d_year
 union
 SELECT c_customer_id customer_id, c_first_name customer_first_name, c_last_name customer_last_name, c_preferred_cust_flag, c_birth_country, c_login, c_email_address, d_year dyear, sum((((cs_ext_list_price-cs_ext_wholesale_cost-cs_ext_discount_amt)+cs_ext_sales_price)/2) ) year_total, 'c' sale_type
 FROM customer, catalog_sales, date_dim
 WHERE c_customer_sk = cs_bill_customer_sk and cs_sold_date_sk = d_date_sk
 GROUP BY c_customer_id, c_first_name, c_last_name, c_preferred_cust_flag, c_birth_country, c_login, c_email_address, d_year;

 SELECT t_s_secyear.customer_id, t_s_secyear.customer_first_name, t_s_secyear.customer_last_name, t_s_secyear.c_preferred_cust_flag, t_s_secyear.c_birth_country, t_s_secyear.c_login
 FROM year_total t_s_firstyear, year_total t_s_secyear, year_total t_c_firstyear, year_total t_c_secyear
 WHERE t_s_secyear.customer_id = t_s_firstyear.customer_id
         and t_s_firstyear.customer_id = t_c_secyear.customer_id
         and t_s_firstyear.customer_id = t_c_firstyear.customer_id
         and t_s_firstyear.sale_type = 's'
         and t_c_firstyear.sale_type = 'c'
         and t_s_secyear.sale_type = 's'
         and t_c_secyear.sale_type = 'c'
         and t_s_firstyear.dyear = 2000
         and t_s_secyear.dyear = 2001
         and t_c_firstyear.dyear = 2000
         and t_c_secyear.dyear = 2001
         and t_s_firstyear.year_total > 0
         and t_c_firstyear.year_total > 0
--          and case when t_c_firstyear.year_total > 0 then t_c_secyear.year_total / t_c_firstyear.year_total else null end
--            > case when t_s_firstyear.year_total > 0 then t_s_secyear.year_total / t_s_firstyear.year_total else null end
 ORDER BY t_s_secyear.customer_id ,  t_s_secyear.customer_first_name ,  t_s_secyear.customer_last_name , t_s_secyear.c_preferred_cust_flag , t_s_secyear.c_birth_country , t_s_secyear.c_login;

 -- $Id: query7.tpl , v 1.3 2006/08/28 18:09:18 jms Exp $
 
 SELECT i_item_id ,  
        avg(ss_quantity) agg1 ,  avg(ss_list_price) agg2 ,  avg(ss_coupon_amt) agg3 ,  avg(ss_sales_price) agg4 
 FROM store_sales ,  customer_demographics ,  date_dim ,  item ,  promotion
 WHERE ss_sold_date_sk = d_date_sk and
       ss_item_sk = i_item_sk and
       ss_cdemo_sk = cd_demo_sk and
       ss_promo_sk = p_promo_sk and
       cd_gender = 1 and 
       cd_marital_status = 1 and
       cd_education_status = 1 and
       (p_channel_email = 'N' or p_channel_event = 'N') and
       d_year = 2000
 GROUP BY i_item_id;
 
 -- $Id: query8.tpl , v 1.4 2006/08/28 18:09:18 jms Exp $
 
 SELECT s_store_id , sum(ss_net_profit)
 FROM store_sales , sstore , date_dim
 WHERE ss_store_sk = s_store_sk
  and ( (s_zip in
         (SELECT ca_zip 
          FROM customer_address 
          WHERE ca_zip in 
          ( '40000' , '45684' , '45500' , '12345' , '23456' , '34567' ,  '45678' , '56789' , '95164' , '78946' , '12368' ,  '75648' , '48123' , '86479' , '30126' , '90876' ,  '405778' , '60089' , '90001' , '74102', '85669' ,  '86197' , '88274' , '83405' , '86475' ,  '85392' ,  '85460' ,  '80348' ,  '81792' )
         )
        )
        or s_zip in
         (SELECT ca_zip
          FROM customer_address
          WHERE ca_address_sk in (SELECT c_current_addr_sk 
                                  FROM customer 
                                  WHERE c_preferred_cust_flag='Y')
         )
      )
   and ss_sold_date_sk = d_date_sk 
   and d_qoy = 1 and d_year = 2001
 GROUP BY s_store_id;

