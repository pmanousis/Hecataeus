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

-- $Id: query9.tpl , v 1.3 2006/08/28 18:09:18 jms Exp $

 SELECT c_customer_id as customer_id , c_last_name || ' ,  ' || c_first_name as customername
 FROM customer , customer_address , household_demographics , income_band
 WHERE ca_city           =  'Edgewood'
   and c_current_addr_sk = ca_address_sk
   and ib_lower_bound   >=  38128
   and ib_upper_bound   <=  88128
   and ib_income_band_sk = hd_income_band_sk
   and hd_demo_sk = c_current_hdemo_sk
 ORDER BY c_customer_id ;
