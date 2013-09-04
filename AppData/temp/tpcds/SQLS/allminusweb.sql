--q3


select dt.d_year, item.i_brand_id brand_id, item.i_brand brand, sum(ss_sales_price) sum_agg
 from  date_dim dt, store_sales, item
 where dt.d_date_sk = store_sales.ss_sold_date_sk
   and store_sales.ss_item_sk = item.i_item_sk
   and item.i_manufact_id = 100
   and dt.d_moy=11
 group by dt.d_year, item.i_brand, item.i_brand_id;
--q6


 select a.ca_state state, count(*) cnt
 from customer_address a, customer c, store_sales s, date_dim d, item i
 where a.ca_address_sk = c.c_current_addr_sk
 	and c.c_customer_sk = s.ss_customer_sk
 	and s.ss_sold_date_sk = d.d_date_sk
 	and s.ss_item_sk = i.i_item_sk
 	and d.d_month_seq = 
 	     (select distinct (d_month_seq)
 	      from date_dim
               where d_year = 2000
 	        and d_moy = 5 )
 	and i.i_current_price > 1.2 * 
             (select avg(j.i_current_price) 
 	     from item j 
 	     where j.i_category = i.i_category)
 group by a.ca_state;
--q7


select i_item_id, avg(ss_quantity) agg1, avg(ss_list_price) agg2, avg(ss_coupon_amt) agg3, avg(ss_sales_price) agg4
 from store_sales, customer_demographics, date_dim, item, promotion
 where ss_sold_date_sk = d_date_sk and
       ss_item_sk = i_item_sk and
       ss_cdemo_sk = cd_demo_sk and
       ss_promo_sk = p_promo_sk and
       cd_gender = 1 and 
       cd_marital_status = 1 and
       cd_education_status = 1 and
       (p_channel_email = 'N' or p_channel_event = 'N') and
       d_year = 2000 
 group by i_item_id;
--q13


select avg(ss_quantity) ssqavg, avg(ss_ext_sales_price)ssextspravg, avg(ss_ext_wholesale_cost) ssextwscavg, sum(ss_ext_wholesale_cost) ssextwscsum
 from store_sales, store, customer_demographics, household_demographics, customer_address, date_dim
 where s_store_sk = ss_store_sk
 and  ss_sold_date_sk = d_date_sk and d_year = 2001
 and
 ((ss_hdemo_sk=hd_demo_sk
  and cd_demo_sk = ss_cdemo_sk
  and cd_marital_status = 1
  and cd_education_status = 1
  and ss_sales_price between 100.00 and 150.00
  and hd_dep_count = 3   
     )or
     (ss_hdemo_sk=hd_demo_sk
  and cd_demo_sk = ss_cdemo_sk
  and cd_marital_status = 1
  and cd_education_status = 1
  and ss_sales_price between 50.00 and 100.00   
  and hd_dep_count = 1
     ) or 
     (ss_hdemo_sk=hd_demo_sk
  and cd_demo_sk = ss_cdemo_sk
  and cd_marital_status = 1
  and cd_education_status = 1
  and ss_sales_price between 150.00 and 200.00 
  and hd_dep_count = 1  
     )
 )
 and
 ((ss_addr_sk = ca_address_sk
  and ca_country = 'United States'
  and ca_state in (3, 6, 9)
  and ss_net_profit between 100 and 200  
     ) or
     (ss_addr_sk = ca_address_sk
  and ca_country = 'United States'
  and ca_state in (6, 3, 9)
  and ss_net_profit between 150 and 300  
     ) or
     (ss_addr_sk = ca_address_sk
  and ca_country = 'United States'
  and ca_state in (9, 6, 3)
  and ss_net_profit between 50 and 250  
     )
 );
--q19


select i_brand_id brand_id, i_brand brand, i_manufact_id, i_manufact, sum(ss_ext_sales_price) ext_price
 from date_dim, store_sales, item, customer, customer_address, store
 where d_date_sk = ss_sold_date_sk
   and ss_item_sk = i_item_sk
   and i_manager_id=1
   and d_moy=11
   and d_year=2000
   and ss_customer_sk = c_customer_sk 
   and c_current_addr_sk = ca_address_sk
   and ca_zip <> s_zip
   and ss_store_sk = s_store_sk 
 group by i_brand, i_brand_id, i_manufact_id, i_manufact;
--q22


select i_product_name, i_brand, i_class, i_category, avg(inv_quantity_on_hand) qoh
from inventory, date_dim, item, warehouse
where inv_date_sk=d_date_sk
  and inv_item_sk=i_item_sk
  and inv_warehouse_sk = w_warehouse_sk
group by (i_product_name, i_brand, i_class, i_category);
--q27


 select i_item_id, s_state, avg(ss_quantity) agg1, avg(ss_list_price) agg2, avg(ss_coupon_amt) agg3, avg(ss_sales_price) agg4
 from store_sales, customer_demographics, date_dim, store, item
 where ss_sold_date_sk = d_date_sk and
       ss_item_sk = i_item_sk and
       ss_store_sk = s_store_sk and
       ss_cdemo_sk = cd_demo_sk and
       cd_gender = 1 and
       cd_marital_status = 1 and
       cd_education_status = 1 and
       d_year = 2000 and
       s_state in ('STATE_A','STATE_B', 'STATE_C', 'STATE_D', 'STATE_E', 'STATE_F')
 group by (i_item_id, s_state);
--q34
create view dn34 as
select ss_ticket_number, ss_customer_sk, count(*) cnt
    from store_sales,date_dim,store,household_demographics
    where store_sales.ss_sold_date_sk = date_dim.d_date_sk
    and store_sales.ss_store_sk = store.s_store_sk  
    and store_sales.ss_hdemo_sk = household_demographics.hd_demo_sk
    and (date_dim.d_dom between 1 and 3 or date_dim.d_dom between 25 and 28)
    and (household_demographics.hd_buy_potential = 'BPONE' or
         household_demographics.hd_buy_potential = 'BPTWO')
    and household_demographics.hd_vehicle_count > 0
    and date_dim.d_year in (2000,2001,2002)
    and store.s_county in ('COUNTY_A','COUNTY_B','COUNTY_C','COUNTY_D','COUNTY_E','COUNTY_F','COUNTY_G','COUNTY_H')
    group by ss_ticket_number,ss_customer_sk;


 select c_last_name, c_first_name, c_salutation, c_preferred_cust_flag, ss_ticket_number, cnt
 from dn34, customer
    where ss_customer_sk = c_customer_sk
      and cnt between 15 and 20;
--q36


 select sum(ss_net_profit) ssntprofsum,sum(ss_ext_sales_price) ssextspsum, i_category, i_class
 from store_sales, date_dim d1, item, store
 where
    d1.d_year = 2000
 and d1.d_date_sk = ss_sold_date_sk
 and i_item_sk  = ss_item_sk 
 and s_store_sk  = ss_store_sk
 and s_state in ('STATE_A','STATE_B','STATE_C','STATE_D','STATE_E','STATE_F','STATE_G','STATE_H')
 group by (i_category,i_class);
--q43


 select s_store_name, s_store_id,sum(ss_sales_price) ssspsum
 from date_dim, store_sales, store
 where d_date_sk = ss_sold_date_sk and
       s_store_sk = ss_store_sk and
       s_gmt_offset = 6 and
       d_year = 2000
 group by s_store_name, s_store_id,d_day_name;
--q46
create view dn as
select ss_ticket_number, ss_customer_sk, ca_city bought_city, sum(ss_coupon_amt) amt, sum(ss_net_profit) profit
    from store_sales,date_dim,store,household_demographics,customer_address 
    where store_sales.ss_sold_date_sk = date_dim.d_date_sk
    and store_sales.ss_store_sk = store.s_store_sk  
    and store_sales.ss_hdemo_sk = household_demographics.hd_demo_sk
    and store_sales.ss_addr_sk = customer_address.ca_address_sk
    and (household_demographics.hd_dep_count = 0 or household_demographics.hd_vehicle_count= 0)
    and date_dim.d_dow in (6,0)
    and date_dim.d_year in (2000,2001,2002) 
    and store.s_city in ('CITY_A','CITY_B','CITY_C','CITY_D','CITY_E')
    group by ss_ticket_number,ss_customer_sk,ss_addr_sk,ca_city
;


 select c_last_name, c_first_name, ca_city, bought_city, ss_ticket_number, amt, profit 
 from
    dn, customer,customer_address current_addr
 where ss_customer_sk = c_customer_sk
       and customer.c_current_addr_sk = current_addr.ca_address_sk
       and current_addr.ca_city <> bought_city;
--q48


select sum (ss_quantity) ssqsum
 from store_sales, store, customer_demographics, customer_address, date_dim
 where s_store_sk = ss_store_sk
 and  ss_sold_date_sk = d_date_sk and d_year = 2000
 and  
 (
  (
   cd_demo_sk = ss_cdemo_sk
   and 
   cd_marital_status = 1
   and 
   cd_education_status = 1
   and 
   ss_sales_price between 100.00 and 150.00  
   )
 or
  (
  cd_demo_sk = ss_cdemo_sk
   and 
   cd_marital_status = 1
   and 
   cd_education_status = 1
   and 
   ss_sales_price between 50.00 and 100.00   
  )
 or 
 (
  cd_demo_sk = ss_cdemo_sk
  and 
   cd_marital_status = 1
   and 
   cd_education_status = 1
   and 
   ss_sales_price between 150.00 and 200.00  
 )
 )
 and
 (
  (
  ss_addr_sk = ca_address_sk
  and
  ca_country = 'United States'
  and
  ca_state in (3, 6, 9)
  and ss_net_profit between 0 and 2000  
  )
 or
  (ss_addr_sk = ca_address_sk
  and
  ca_country = 'United States'
  and
  ca_state in (6, 9, 3)
  and ss_net_profit between 150 and 3000 
  )
 or
  (ss_addr_sk = ca_address_sk
  and
  ca_country = 'United States'
  and
  ca_state in (9, 3, 6)
  and ss_net_profit between 50 and 25000 
  )
 );
--q52


select dt.d_year, item.i_brand_id brand_id, item.i_brand brand, sum(ss_ext_sales_price) ext_price
from date_dim dt, store_sales, item
where dt.d_date_sk = store_sales.ss_sold_date_sk
    and store_sales.ss_item_sk = item.i_item_sk
    and item.i_manager_id = 1
    and dt.d_moy=11
    and dt.d_year=2000
group by dt.d_year, item.i_brand, item.i_brand_id;
--q53
create view tmp1q53 as
select i_manufact_id, sum(ss_sales_price) sum_sales, avg(ss_sales_price) avg_quarterly_sales
 from item, store_sales, date_dim, store
 where ss_item_sk = i_item_sk and
 ss_sold_date_sk = d_date_sk and
 ss_store_sk = s_store_sk and
 d_month_seq in (1,2,3,4,5,6,7,8,9,10,11,12) and
 ((i_category in ('Books','Children','Electronics') and
 i_class in ('personal','portable','reference','self-help') and
 i_brand in ('scholaramalgamalg #14','scholaramalgamalg #7','exportiunivamalg #9','scholaramalgamalg #9'))
 or(i_category in ('Women','Music','Men') and
 i_class in ('accessories','classical','fragrances','pants') and
 i_brand in ('amalgimporto #1','edu packscholar #1','exportiimporto #1', 'importoamalg #1')))
 group by i_manufact_id, d_qoy;


select  * from tmp1q53
where avg_quarterly_sales > 0.1;
--q55


select i_brand_id brand_id, i_brand brand, sum(ss_ext_sales_price) ext_price
 from date_dim, store_sales, item
 where d_date_sk = ss_sold_date_sk
 	and ss_item_sk = i_item_sk
 	and i_manager_id=1
 	and d_moy=11
 	and d_year=2000
 group by i_brand, i_brand_id;
--q59
create view wssq59 as 
select d_week_seq, ss_store_sk, sum(ss_sales_price) as sum_sales
 from store_sales,date_dim
 where d_date_sk = ss_sold_date_sk
 group by d_week_seq,ss_store_sk,d_day_name;

create view yq59 as
select s_store_name s_store_name1,wssq59.d_week_seq d_week_seq1,s_store_id s_store_id1,sum_sales sum_sales1
  from wssq59,store,date_dim d
  where d.d_week_seq = wssq59.d_week_seq and ss_store_sk = s_store_sk and d_month_seq between 1 and 12;

create view xq59 as
select s_store_name s_store_name2,wssq59.d_week_seq d_week_seq2,s_store_id s_store_id2,sum_sales sum_sales2
  from wssq59,store,date_dim d
  where d.d_week_seq = wssq59.d_week_seq and ss_store_sk = s_store_sk and  d_month_seq between 12 and 23;


 select s_store_name1,s_store_id1,d_week_seq1, sum_sales1/sum_sales2 percentageofsums
 from yq59,xq59
 where yq59.s_store_id1=xq59.s_store_id2 and yq59.d_week_seq1=xq59.d_week_seq2-52;
--q61
create view all_sales as
select sum(ss_ext_sales_price) total
   from  store_sales, store, date_dim, customer, customer_address, item
   where ss_sold_date_sk = d_date_sk
   and   ss_store_sk = s_store_sk
   and   ss_customer_sk= c_customer_sk
   and   ca_address_sk = c_current_addr_sk
   and   ss_item_sk = i_item_sk
   and   ca_gmt_offset = 3
   and   i_category = 'Books'
   and   s_gmt_offset = 3
   and   d_year = 2000
   and   d_moy  = 11;

create view promotional_sales as
select sum(ss_ext_sales_price) promotions
   from  store_sales, store, promotion, date_dim, customer, customer_address, item
   where ss_sold_date_sk = d_date_sk
   and   ss_store_sk = s_store_sk
   and   ss_promo_sk = p_promo_sk
   and   ss_customer_sk= c_customer_sk
   and   ca_address_sk = c_current_addr_sk
   and   ss_item_sk = i_item_sk 
   and   ca_gmt_offset = 3
   and   i_category = 'Books'
   and   (p_channel_dmail = 'Y' or p_channel_email = 'Y' or p_channel_tv = 'Y')
   and   s_gmt_offset = 3
   and   d_year = 2000
   and   d_moy  = 11;


select promotions, total, promotions / total percentageofpromotions
from promotional_sales, all_sales;
-- --order by promotions, total;
--q63
create view tmp1q63 as
select i_manager_id,sum(ss_sales_price) sum_sales,avg(ss_sales_price) as avg_monthly_sales
      from item,store_sales,date_dim,store
      where ss_item_sk = i_item_sk
        and ss_sold_date_sk = d_date_sk
        and ss_store_sk = s_store_sk
        and d_month_seq in (1,2,3,4,5,6,7,8,9,10,11,12)
        and ((    i_category in ('Books','Children','Electronics')
              and i_class in ('personal','portable','refernece','self-help')
              and i_brand in ('scholaramalgamalg #14','scholaramalgamalg #7','exportiunivamalg #9','scholaramalgamalg #9'))
           or(    i_category in ('Women','Music','Men')
              and i_class in ('accessories','classical','fragrances','pants')
              and i_brand in ('amalgimporto #1','edu packscholar #1','exportiimporto #1','importoamalg #1')))
group by i_manager_id, d_moy;


select * 
from tmp1q63
where avg_monthly_sales > 0.1;
--q65
create view sa as
select  ss_store_sk, ss_item_sk, sum(ss_sales_price) as revenue
from store_sales, date_dim
where ss_sold_date_sk = d_date_sk and d_month_seq between 1 and 12
group by ss_store_sk, ss_item_sk;

create view sb as
select sa.ss_store_sk, avg(revenue) as ave
from sa
group by ss_store_sk;


select s_store_name, i_item_desc, sa.revenue, i_current_price, i_wholesale_cost, i_brand
 from store, item, sb, sa
 where sb.ss_store_sk = sa.ss_store_sk and sa.revenue <= 0.1 * sb.ave and store.s_store_sk = sa.ss_store_sk and i_item_sk = sa.ss_item_sk;
--q68
create view dn68 as
select ss_ticket_number,ss_customer_sk,ca_city bought_city,sum(ss_ext_sales_price) as extended_price ,sum(ss_ext_list_price) as list_price,sum(ss_ext_tax) as extended_tax 
from store_sales,date_dim,store,household_demographics,customer_address 
where store_sales.ss_sold_date_sk = date_dim.d_date_sk
  and store_sales.ss_store_sk = store.s_store_sk  
  and store_sales.ss_hdemo_sk = household_demographics.hd_demo_sk
  and store_sales.ss_addr_sk = customer_address.ca_address_sk
  and date_dim.d_dom between 1 and 2 
  and (household_demographics.hd_dep_count = 20 or household_demographics.hd_vehicle_count= 30)
  and date_dim.d_year in (2000,2001,2002)
  and store.s_city in ('CITY_A','CITY_B')
group by ss_ticket_number,ss_customer_sk,ss_addr_sk,ca_city;


 select c_last_name,c_first_name,ca_city,bought_city,ss_ticket_number,extended_price,extended_tax,list_price
 from dn68,customer,customer_address current_addr
 where ss_customer_sk = c_customer_sk
   and customer.c_current_addr_sk = current_addr.ca_address_sk
   and current_addr.ca_city <> bought_city;
--q73
create view dj73 as
select ss_ticket_number,ss_customer_sk,count(*) as cnt
from store_sales,date_dim,store,household_demographics
where store_sales.ss_sold_date_sk = date_dim.d_date_sk
 and store_sales.ss_store_sk = store.s_store_sk  
 and store_sales.ss_hdemo_sk = household_demographics.hd_demo_sk
 and date_dim.d_dom between 1 and 2 
 and (household_demographics.hd_buy_potential = 'BPONE' or household_demographics.hd_buy_potential = 'BPTWO')
 and household_demographics.hd_vehicle_count > 0
 and date_dim.d_year in (2000,2001,2002)
 and store.s_county in ('COUNTY_A','COUNTY_B','COUNTY_C','COUNTY_D')
group by ss_ticket_number,ss_customer_sk;


select c_last_name,c_first_name,c_salutation,c_preferred_cust_flag ,ss_ticket_number,cnt
from dj73,customer
    where dj73.ss_customer_sk = customer.c_customer_sk and dj73.cnt between 1 and 5;
--q79
create view ms79 as
select ss_ticket_number,ss_customer_sk,store.s_city,sum(ss_coupon_amt) amt,sum(ss_net_profit) profit
    from store_sales,date_dim,store,household_demographics
    where store_sales.ss_sold_date_sk = date_dim.d_date_sk
    and store_sales.ss_store_sk = store.s_store_sk  
    and store_sales.ss_hdemo_sk = household_demographics.hd_demo_sk
    and (household_demographics.hd_dep_count = 1 or household_demographics.hd_vehicle_count > 2)
    and date_dim.d_dow = 1
    and date_dim.d_year in (2000,2001,2002) 
    and store.s_number_employees between 200 and 295
    group by ss_ticket_number,ss_customer_sk,ss_addr_sk,store.s_city;


 select c_last_name,c_first_name,s_city,ss_ticket_number,amt,profit
  from ms79,customer
    where ss_customer_sk = c_customer_sk;
--q82


 select i_item_id,i_item_desc,i_current_price
 from item, inventory, date_dim, store_sales
 where i_current_price between 20 and 50
 and inv_item_sk = i_item_sk
 and d_date_sk=inv_date_sk
 and d_date between '2000-01-01' and '2000-03-01'
 and i_manufact_id in (200,350,460,510)
 and inv_quantity_on_hand between 100 and 500
 and ss_item_sk = i_item_sk
 group by i_item_id,i_item_desc,i_current_price;
--q88
create view sena as
select count(*) h8_30_to_9
 from store_sales, household_demographics , time_dim, store
 where ss_sold_time_sk = time_dim.t_time_sk   
     and ss_hdemo_sk = household_demographics.hd_demo_sk 
     and ss_store_sk = s_store_sk
     and time_dim.t_hour = 8
     and time_dim.t_minute >= 30
     and ((household_demographics.hd_dep_count = 0 and household_demographics.hd_vehicle_count<=3) or (household_demographics.hd_dep_count = 1 and household_demographics.hd_vehicle_count<=4) or
          (household_demographics.hd_dep_count = 2 and household_demographics.hd_vehicle_count<=5)) 
     and store.s_store_name = 'ese';
create view sdyo as
select count(*) h9_to_9_30 
 from store_sales, household_demographics , time_dim, store
 where ss_sold_time_sk = time_dim.t_time_sk
     and ss_hdemo_sk = household_demographics.hd_demo_sk
     and ss_store_sk = s_store_sk 
     and time_dim.t_hour = 9 
     and time_dim.t_minute < 30
     and ((household_demographics.hd_dep_count = 0 and household_demographics.hd_vehicle_count<=3) or (household_demographics.hd_dep_count = 1 and household_demographics.hd_vehicle_count<=4) or
          (household_demographics.hd_dep_count = 2 and household_demographics.hd_vehicle_count<=5))
     and store.s_store_name = 'ese';
create view stria as
select count(*) h9_30_to_10 
 from store_sales, household_demographics , time_dim, store
 where ss_sold_time_sk = time_dim.t_time_sk
     and ss_hdemo_sk = household_demographics.hd_demo_sk
     and ss_store_sk = s_store_sk
     and time_dim.t_hour = 9
     and time_dim.t_minute >= 30
     and ((household_demographics.hd_dep_count = 0 and household_demographics.hd_vehicle_count<=3) or (household_demographics.hd_dep_count = 1 and household_demographics.hd_vehicle_count<=4) or
          (household_demographics.hd_dep_count = 2 and household_demographics.hd_vehicle_count<=5))
     and store.s_store_name = 'ese';
create view stessera as
select count(*) h10_to_10_30
 from store_sales, household_demographics , time_dim, store
 where ss_sold_time_sk = time_dim.t_time_sk
     and ss_hdemo_sk = household_demographics.hd_demo_sk
     and ss_store_sk = s_store_sk
     and time_dim.t_hour = 10 
     and time_dim.t_minute < 30
     and ((household_demographics.hd_dep_count = 0 and household_demographics.hd_vehicle_count<=3) or (household_demographics.hd_dep_count = 1 and household_demographics.hd_vehicle_count<=4) or
          (household_demographics.hd_dep_count = 2 and household_demographics.hd_vehicle_count<=5))
     and store.s_store_name = 'ese';
create view spente as
select count(*) h10_30_to_11
 from store_sales, household_demographics , time_dim, store
 where ss_sold_time_sk = time_dim.t_time_sk
     and ss_hdemo_sk = household_demographics.hd_demo_sk
     and ss_store_sk = s_store_sk
     and time_dim.t_hour = 10 
     and time_dim.t_minute >= 30
     and ((household_demographics.hd_dep_count = 0 and household_demographics.hd_vehicle_count<=3) or (household_demographics.hd_dep_count = 1 and household_demographics.hd_vehicle_count<=4) or
          (household_demographics.hd_dep_count = 2 and household_demographics.hd_vehicle_count<=5))
     and store.s_store_name = 'ese';
create view sexi as
select count(*) h11_to_11_30
 from store_sales, household_demographics , time_dim, store
 where ss_sold_time_sk = time_dim.t_time_sk
     and ss_hdemo_sk = household_demographics.hd_demo_sk
     and ss_store_sk = s_store_sk 
     and time_dim.t_hour = 11
     and time_dim.t_minute < 30
     and ((household_demographics.hd_dep_count = 0 and household_demographics.hd_vehicle_count<=3) or (household_demographics.hd_dep_count = 1 and household_demographics.hd_vehicle_count<=4) or
          (household_demographics.hd_dep_count = 2 and household_demographics.hd_vehicle_count<=5))
     and store.s_store_name = 'ese';
create view sefta as
select count(*) h11_30_to_12
 from store_sales, household_demographics , time_dim, store
 where ss_sold_time_sk = time_dim.t_time_sk
     and ss_hdemo_sk = household_demographics.hd_demo_sk
     and ss_store_sk = s_store_sk
     and time_dim.t_hour = 11
     and time_dim.t_minute >= 30
     and ((household_demographics.hd_dep_count = 0 and household_demographics.hd_vehicle_count<=3) or (household_demographics.hd_dep_count = 1 and household_demographics.hd_vehicle_count<=4) or
          (household_demographics.hd_dep_count = 2 and household_demographics.hd_vehicle_count<=5))
     and store.s_store_name = 'ese';
create view soxto as
select count(*) h12_to_12_30
 from store_sales, household_demographics , time_dim, store
 where ss_sold_time_sk = time_dim.t_time_sk
     and ss_hdemo_sk = household_demographics.hd_demo_sk
     and ss_store_sk = s_store_sk
     and time_dim.t_hour = 12
     and time_dim.t_minute < 30
     and ((household_demographics.hd_dep_count = 0 and household_demographics.hd_vehicle_count<=3) or (household_demographics.hd_dep_count = 1 and household_demographics.hd_vehicle_count<=4) or
          (household_demographics.hd_dep_count = 2 and household_demographics.hd_vehicle_count<=5))
     and store.s_store_name = 'ese';


select  *
from sena, sdyo, stria, stessera, spente, sexi, sefta, soxto;
--q89
create view tmp1q89 as
select i_category, i_class, i_brand,s_store_name, s_company_name,d_moy,sum(ss_sales_price) sum_sales,avg(ss_sales_price) as avg_monthly_sales
from item, store_sales, date_dim, store
where ss_item_sk = i_item_sk and ss_sold_date_sk = d_date_sk and ss_store_sk = s_store_sk and d_year in (2000) and
      ((i_category in ('CAT_A','CAT_B','CAT_C') and i_class in ('CLASS_A','CLASS_B','CLASS_C')) or (i_category in ('CAT_D','CAT_E','CAT_F') and i_class in ('CLASS_D','CLASS_E','CLASS_F')))
group by i_category, i_class, i_brand,s_store_name, s_company_name, d_moy;


select *
from  tmp1q89
where avg_monthly_sales > 0.1;
--q96


select count(*) cntofall
from store_sales,household_demographics ,time_dim, store
where ss_sold_time_sk = time_dim.t_time_sk   
    and ss_hdemo_sk = household_demographics.hd_demo_sk 
    and ss_store_sk = s_store_sk
    and time_dim.t_hour = 16
    and time_dim.t_minute >= 30
    and household_demographics.hd_dep_count = 0
    and store.s_store_name = 'ese';
--q98


select i_item_desc,i_category,i_class,i_current_price,sum(ss_ext_sales_price) as itemrevenue, sum(ss_ext_sales_price) as revenueratio
from
	store_sales,item,date_dim
where 
	ss_item_sk = i_item_sk 
  	and i_category in ('CATEGORY1', 'CATEGORY2', 'CATEGORY3')
  	and ss_sold_date_sk = d_date_sk
	and d_date between '2000-01-01' and '2000-01-31'
group by i_item_id,i_item_desc,i_category,i_class,i_current_price;
--q15


 select ca_zip,sum(cs_sales_price) csspsum
 from catalog_sales,customer,customer_address,date_dim
 where cs_bill_customer_sk = c_customer_sk
 	and c_current_addr_sk = ca_address_sk 
 	and ( ca_zip in ('85669', '86197','88274','83405','86475','85392', '85460', '80348', '81792')
 	      or ca_state in ('CA','WA','GA')
 	      or cs_sales_price > 500)
 	and cs_sold_date_sk = d_date_sk
 	and d_qoy = 1 and d_year = 2000
 group by ca_zip;
--q16


select count(cs_order_number) as order_count,sum(cs_ext_ship_cost) as total_shipping_cost,sum(cs_net_profit) as total_net_profit
from catalog_sales cs1,date_dim,customer_address,call_center
where
    d_date between '2000-04-01' and '2000-06-01'
    and cs1.cs_ship_date_sk = d_date_sk
    and cs1.cs_ship_addr_sk = ca_address_sk
    and ca_state = 'STATE'
    and cs1.cs_call_center_sk = cc_call_center_sk
    and cc_county in ('COUNTY_A','COUNTY_B','COUNTY_C','COUNTY_D', 'COUNTY_E')
    and exists (select *
		from catalog_sales cs2
		where cs1.cs_order_number = cs2.cs_order_number
		  and cs1.cs_warehouse_sk <> cs2.cs_warehouse_sk)
    and not exists(select *
		  from catalog_returns cr1
		  where cs1.cs_order_number = cr1.cr_order_number);
--q18


select i_item_id,ca_country,ca_state,ca_county, avg(cs_quantity) agg1, avg(cs_list_price) agg2,avg(cs_coupon_amt) agg3,avg(cs_sales_price) agg4,avg(cs_net_profit) agg5,avg(c_birth_year) agg6,avg(cd1.cd_dep_count) agg7
from catalog_sales, customer_demographics cd1, customer_demographics cd2, customer, customer_address, date_dim, item
 where cs_sold_date_sk = d_date_sk and
       cs_item_sk = i_item_sk and
       cs_bill_cdemo_sk = cd1.cd_demo_sk and
       cs_bill_customer_sk = c_customer_sk and
       cd1.cd_gender = 1 and 
       cd1.cd_education_status = 1 and
       c_current_cdemo_sk = cd2.cd_demo_sk and
       c_current_addr_sk = ca_address_sk and
       c_birth_month in (1,3,5,7,9,11) and
       d_year = 2000 and
       ca_state in ('STATE.1','STATE.2','STATE.3','STATE.4','STATE.5','STATE.6','STATE.7')
 group by (i_item_id, ca_country, ca_state, ca_county);
--q20


 select i_item_desc ,i_category ,i_class ,i_current_price,sum(cs_ext_sales_price) as itemrevenue
 from	catalog_sales,item ,date_dim
 where cs_item_sk = i_item_sk 
   and i_category in (1,2,3)
   and cs_sold_date_sk = d_date_sk
 and d_date between '2000-01-01' and '2000-01-31'
 group by i_item_id,i_item_desc ,i_category,i_class,i_current_price;
--q26


 select i_item_id, avg(cs_quantity) agg1,avg(cs_list_price) agg2,avg(cs_coupon_amt) agg3,avg(cs_sales_price) agg4 
 from catalog_sales, customer_demographics, date_dim, item, promotion
 where cs_sold_date_sk = d_date_sk and
       cs_item_sk = i_item_sk and
       cs_bill_cdemo_sk = cd_demo_sk and
       cs_promo_sk = p_promo_sk and
       cd_gender = 1 and 
       cd_marital_status = 1 and
       cd_education_status = 1 and
       (p_channel_email = 'N' or p_channel_event = 'N') and
       d_year = 2000
 group by i_item_id;
--q32
create view vq32 as
select avg(cs_ext_discount_amt) as vq32result 
from catalog_sales ,date_dim
where 
  d_date between '2000-01-01' and '2000-03-01'
  and d_date_sk = cs_sold_date_sk;


select sum(cs_ext_discount_amt)  as excess_discount_amount
from catalog_sales ,item ,date_dim
where
i_manufact_id = 1
and i_item_sk = cs_item_sk 
and d_date between '2000-01-01' and '2000-03-01'
and d_date_sk = cs_sold_date_sk 
and cs_ext_discount_amt > (select vq32result from vq32);
--q37


 select i_item_id,i_item_desc,i_current_price
 from item, inventory, date_dim, catalog_sales
 where i_current_price between 20 and 50
 and inv_item_sk = i_item_sk
 and d_date_sk=inv_date_sk
 and d_date between '2000-01-01' and '2000-03-01' 
 and i_manufact_id in (667,671,675,679)
 and inv_quantity_on_hand between 100 and 500
 and cs_item_sk = i_item_sk
 group by i_item_id,i_item_desc,i_current_price;
--q40


 select w_state,i_item_id,sum(cs_sales_price - cr_refunded_cash) as sales_before, sum(cs_sales_price - cr_refunded_cash) as sales_after
 from catalog_sales left outer join catalog_returns on (cs_order_number = cr_order_number and cs_item_sk = cr_item_sk), warehouse,item,date_dim
 where
     i_current_price between 0.99 and 1.49
 and i_item_sk          = cs_item_sk
 and cs_warehouse_sk    = w_warehouse_sk 
 and cs_sold_date_sk    = d_date_sk
 and d_date between '2000-01-01' and '2000-03-01'
 group by w_state,i_item_id;
--q57
create view v1q57 as
 select i_category, i_brand,cc_name,d_year, d_moy,
        sum(cs_sales_price) sum_sales,
        avg(cs_sales_price) avg_monthly_sales
 from item, catalog_sales, date_dim, call_center
 where cs_item_sk = i_item_sk and
       cs_sold_date_sk = d_date_sk and
       cc_call_center_sk= cs_call_center_sk and
       (
         d_year = 2000 or
         ( d_year = 1999 and d_moy =12) or
         ( d_year = 2001 and d_moy =1)
       )
 group by i_category, i_brand, cc_name , d_year, d_moy;
 create view v2q57 as
 select v1q57.d_year, v1q57.avg_monthly_sales,v1q57.sum_sales, v1_lag.sum_sales psum, v1_lead.sum_sales nsum
 from v1q57, v1q57 v1_lag, v1q57 v1_lead
 where v1q57.i_category = v1_lag.i_category and
       v1q57.i_category = v1_lead.i_category and
       v1q57.i_brand = v1_lag.i_brand and
       v1q57.i_brand = v1_lead.i_brand and
       v1q57.cc_name = v1_lag.cc_name and
       v1q57.cc_name = v1_lead.cc_name;


 select *
 from v2q57
 where  d_year = 2000 and avg_monthly_sales > 0 and
        (sum_sales - avg_monthly_sales) / avg_monthly_sales > 0.1;
--q91


select cc_call_center_id Call_Center, cc_name Call_Center_Name, cc_manager Manager, sum(cr_net_loss) Returns_Loss
from call_center, catalog_returns, date_dim, customer, customer_address, customer_demographics, household_demographics
where
        cr_call_center_sk       = cc_call_center_sk
and     cr_returned_date_sk     = d_date_sk
and     cr_returning_customer_sk= c_customer_sk
and     cd_demo_sk              = c_current_cdemo_sk
and     hd_demo_sk              = c_current_hdemo_sk
and     ca_address_sk           = c_current_addr_sk
and     d_year                  = 2000
and     d_moy                   = 11
and     ( (cd_marital_status       = 'M' and cd_education_status     = 'Unknown')
        or(cd_marital_status       = 'W' and cd_education_status     = 'Advanced Degree'))

and     ca_gmt_offset           = 03
group by cc_call_center_id,cc_name,cc_manager,cd_marital_status,cd_education_status;
--q84


 select c_customer_id as customer_id, c_last_name ,c_first_name
 from customer,customer_address,customer_demographics,household_demographics,income_band,store_returns
 where ca_city	        =  'Ioannina'
   and c_current_addr_sk = ca_address_sk
   and ib_lower_bound   >=  30000
   and ib_upper_bound   <=  80000
   and ib_income_band_sk = hd_income_band_sk
   and cd_demo_sk = c_current_cdemo_sk
   and hd_demo_sk = c_current_hdemo_sk
   and sr_cdemo_sk = cd_demo_sk;
--q81
 create view customer_total_return as
 select cr_returning_customer_sk as ctr_customer_sk ,ca_state as ctr_state,  sum(cr_return_amt_inc_tax) as ctr_total_return
 from catalog_returns,date_dim,customer_address
 where cr_returned_date_sk = d_date_sk 
   and d_year =2000
   and cr_returning_addr_sk = ca_address_sk 
 group by cr_returning_customer_sk,ca_state;


 select c_customer_id,c_salutation,c_first_name,c_last_name,ca_street_number,ca_street_name,ca_street_type,ca_suite_number,ca_city,ca_county,ca_state,ca_zip,ca_country,ca_gmt_offset,ca_location_type,ctr_total_return
 from customer_total_return ctr1,customer_address,customer
 where ctr1.ctr_total_return > (select max(ctr_total_return)*1.2
 			  from customer_total_return ctr2 
                  	  where ctr1.ctr_state = ctr2.ctr_state)
       and ca_address_sk = c_current_addr_sk
       and ca_state = 'Hpeiros'
       and ctr1.ctr_customer_sk = c_customer_sk;
--q42


 select dt.d_year,item.i_category_id,item.i_category,sum(ss_ext_sales_price) ssextspsum
 from 	date_dim dt,store_sales,item
 where dt.d_date_sk = store_sales.ss_sold_date_sk
 	and store_sales.ss_item_sk = item.i_item_sk
 	and item.i_manager_id = 1  	
 	and dt.d_moy=11
 	and dt.d_year=2000
 group by 	dt.d_year,item.i_category_id,item.i_category;
--q41


 select i_product_name
 from item i1
 where i_manufact_id between 1 and 41
 and (select count(*) as item_cnt
      from item
      where (i_manufact = i1.i_manufact and
      ((i_category = 'Women' and 
      (i_color = 'COLOR1' or i_color = 'COLOR2') and 
      (i_units = 'UNIT1' or i_units = 'UNIT2') and
      (i_size = 'SIZE1' or i_size = 'SIZE2')
      ) or
      (i_category = 'Women' and
      (i_color = 'COLOR3' or i_color = 'COLOR4') and
      (i_units = 'UNIT3' or i_units = 'UNIT4') and
      (i_size = 'SIZE3' or i_size = 'SIZE4')
      ) or
      (i_category = 'Men' and
      (i_color = 'COLOR5' or i_color = 'COLOR6') and
      (i_units = 'UNIT5' or i_units = 'UNIT6') and
      (i_size = 'SIZE5' or i_size = 'SIZE6')
      ) or
      (i_category = 'Men' and
      (i_color = 'COLOR7' or i_color = 'COLOR8') and
      (i_units = 'UNIT7' or i_units = 'UNIT8') and
      (i_size = 'SIZE1' or i_size = 'SIZE2')
      ))) or
      (i_manufact = i1.i_manufact and
      ((i_category = 'Women' and 
      (i_color = 'COLOR9' or i_color = 'COLOR10') and 
      (i_units = 'UNIT9' or i_units = 'UNIT10') and
      (i_size = 'SIZE1' or i_size = 'SIZE2')
      ) or
      (i_category = 'Women' and
      (i_color = 'COLOR11' or i_color = 'COLOR12') and
      (i_units = 'UNIT11' or i_units = 'UNIT12') and
      (i_size = 'SIZE3' or i_size = 'SIZE4')
      ) or
      (i_category = 'Men' and
      (i_color = 'COLOR13' or i_color = 'COLOR14') and
      (i_units = 'UNIT13' or i_units = 'UNIT14') and
      (i_size = 'SIZE5' or i_size = 'SIZE6')
      ) or
      (i_category = 'Men' and
      (i_color = 'COLOR15' or i_color = 'COLOR16') and
      (i_units = 'UNIT15' or i_units = 'UNIT16') and
      (i_size = 'SIZE1' or i_size = 'SIZE2')
      )))) > 0;
--q28
create view b1q28 as
select avg(ss_list_price) B1_LP,count(ss_list_price) B1_CNT,count(ss_list_price) B1_CNTD
from store_sales
where ss_quantity between 0 and 5
  and (ss_list_price between 1 and 11
  or ss_coupon_amt between 18000 and 19000
  or ss_wholesale_cost between 80 and 100);
create view b2q28 as
select avg(ss_list_price) B2_LP,count(ss_list_price) B2_CNT,count(ss_list_price) B2_CNTD
from store_sales
where ss_quantity between 6 and 10
  and (ss_list_price between 1 and 11
  or ss_coupon_amt between 18000 and 19000
  or ss_wholesale_cost between 80 and 100);
create view b3q28 as
select avg(ss_list_price) B3_LP,count(ss_list_price) B3_CNT,count(ss_list_price) B3_CNTD
from store_sales
where ss_quantity between 11 and 15
  and (ss_list_price between 1 and 11
  or ss_coupon_amt between 18000 and 19000
  or ss_wholesale_cost between 80 and 100);
create view b4q28 as
select avg(ss_list_price) B4_LP,count(ss_list_price) B4_CNT,count(ss_list_price) B4_CNTD
from store_sales
where ss_quantity between 16 and 20
  and (ss_list_price between 1 and 11
  or ss_coupon_amt between 18000 and 19000
  or ss_wholesale_cost between 80 and 100);
create view b5q28 as
select avg(ss_list_price) B5_LP,count(ss_list_price) B5_CNT,count(ss_list_price) B5_CNTD
from store_sales
where ss_quantity between 21 and 25
  and (ss_list_price between 1 and 11
  or ss_coupon_amt between 18000 and 19000
  or ss_wholesale_cost between 80 and 100);
create view b6q28 as
select avg(ss_list_price) B6_LP,count(ss_list_price) B6_CNT,count(ss_list_price) B6_CNTD
from store_sales
where ss_quantity between 26 and 30
  and (ss_list_price between 1 and 11
  or ss_coupon_amt between 18000 and 19000
  or ss_wholesale_cost between 80 and 100);


select *
from b1q28,b2q28,b3q28,b4q28,b5q28,b6q28;
--q21
create view xq21before as
select w_warehouse_name,i_item_id,sum(inv_quantity_on_hand) as inv_sum
from inventory,warehouse,item,date_dim
where i_current_price between 0.99 and 1.49
  and i_item_sk          = inv_item_sk
  and inv_warehouse_sk   = w_warehouse_sk
  and inv_date_sk    = d_date_sk
  and d_date between '2000-01-01' and '2000-02-01'
  group by w_warehouse_name, i_item_id;
create view xq21after as
select w_warehouse_name,i_item_id,sum(inv_quantity_on_hand) as inv_sum
from inventory,warehouse,item,date_dim
where i_current_price between 0.99 and 1.49
  and i_item_sk          = inv_item_sk
  and inv_warehouse_sk   = w_warehouse_sk
  and inv_date_sk    = d_date_sk
  and d_date between '2000-02-01' and '2000-03-01'
  group by w_warehouse_name, i_item_id;


 select *
 from xq21before,xq21after
 where
 xq21before.w_warehouse_name=xq21after.w_warehouse_name
 and xq21before.i_item_id=xq21after.i_item_id
 and (xq21after.inv_sum / xq21before.inv_sum) between 2.0/3.0 and 3.0/2.0;
--q9
CREATE VIEW QUERY9A AS
select count(*) q9a
from store_sales,reason
where ss_quantity between 1 and 20 and r_reason_sk = 1;
CREATE VIEW QUERY9B AS
select avg(ss_ext_discount_amt) q9b
from store_sales ,reason
where ss_quantity between 1 and 20 and r_reason_sk = 1;
CREATE VIEW QUERY9C AS
select avg(ss_net_paid) q9c
from store_sales,reason
where ss_quantity between 1 and 20 and r_reason_sk = 1;
CREATE VIEW QUERY9D AS
select count(*) q9d
from store_sales,reason
where ss_quantity between 21 and 40 and r_reason_sk = 1;
CREATE VIEW QUERY9E AS
select avg(ss_ext_discount_amt) q9e
from store_sales,reason
where ss_quantity between 21 and 40 and r_reason_sk = 1;
CREATE VIEW QUERY9F AS
select avg(ss_net_paid) q9f
from store_sales,reason
where ss_quantity between 21 and 40 and r_reason_sk = 1;
CREATE VIEW QUERY9G AS
select count(*) q9g
from store_sales,reason
where ss_quantity between 41 and 60 and r_reason_sk = 1;
CREATE VIEW QUERY9H AS
select avg(ss_ext_discount_amt) q9h
from store_sales,reason
where ss_quantity between 41 and 60 and r_reason_sk = 1;
CREATE VIEW QUERY9I AS
select avg(ss_net_paid) q9i
from store_sales,reason
where ss_quantity between 41 and 60 and r_reason_sk = 1;
CREATE VIEW QUERY9J AS
select count(*) q9j
from store_sales,reason
where ss_quantity between 61 and 80 and r_reason_sk = 1;
CREATE VIEW QUERY9K AS
select avg(ss_ext_discount_amt) q9k
from store_sales,reason
where ss_quantity between 61 and 80 and r_reason_sk = 1;
CREATE VIEW QUERY9L AS
select avg(ss_net_paid) q9l
from store_sales,reason
where ss_quantity between 61 and 80 and r_reason_sk = 1;
CREATE VIEW QUERY9M AS
select count(*) q9m
from store_sales,reason
where ss_quantity between 81 and 100 and r_reason_sk = 1;
CREATE VIEW QUERY9N AS
select avg(ss_ext_discount_amt) q9n
from store_sales,reason
where ss_quantity between 81 and 100 and r_reason_sk = 1;
CREATE VIEW QUERY9O AS
select avg(ss_net_paid) q9o
from store_sales,reason
where ss_quantity between 81 and 100 and r_reason_sk = 1;
--q1
create view customer_total_returnq1 as
select sr_customer_sk as ctr_customer_sk,sr_store_sk as ctr_store_sk,sum(SR_RETURN_TAX) as ctr_total_return
from store_returns,date_dim
where sr_returned_date_sk = d_date_sk
and d_year =2000
group by sr_customer_sk,sr_store_sk;


select c_customer_id
from customer_total_returnq1 ctr1,store,customer
where ctr1.ctr_total_return > (select avg(ctr_total_return)*1.2
from customer_total_returnq1 ctr2
where ctr1.ctr_store_sk = ctr2.ctr_store_sk)
and s_store_sk = ctr1.ctr_store_sk
and s_state = 'STATE'
and ctr1.ctr_customer_sk = c_customer_sk;
--q72


select i_item_desc,w_warehouse_name,d1.d_week_seq,count(p_promo_sk) promo,count(*) total_cnt
from catalog_sales,inventory,warehouse,item,customer_demographics,household_demographics, date_dim AS d1,date_dim AS d2, date_dim AS d3, promotion, catalog_returns
where d1.d_week_seq = d2.d_week_seq
  and inv_quantity_on_hand < cs_quantity 
  and d3.d_date > d1.d_date + 5
  and hd_buy_potential = '>10000'
  and d1.d_year = 2000
  and hd_buy_potential = '>10000'
  and cd_marital_status = 1
  and d1.d_year = 2000
  and cs_item_sk = inv_item_sk
  and w_warehouse_sk=inv_warehouse_sk
  and i_item_sk = cs_item_sk
  and cs_bill_cdemo_sk = cd_demo_sk
  and cs_bill_hdemo_sk = hd_demo_sk
  and cs_sold_date_sk = d1.d_date_sk
  and inv_date_sk = d2.d_date_sk
  and cs_ship_date_sk = d3.d_date_sk
  and cs_promo_sk=p_promo_sk
  and cr_item_sk = cs_item_sk and cr_order_number = cs_order_number
group by i_item_desc,w_warehouse_name,d1.d_week_seq;
--q64
create view cs_uiq64 as
  select cs_item_sk,sum(cs_ext_list_price) as sale,sum(cr_refunded_cash+cr_reversed_charge+cr_store_credit) as refund
  from catalog_sales,catalog_returns
  where cs_item_sk = cr_item_sk
    and cs_order_number = cr_order_number
  group by cs_item_sk;

create view cross_salesq64 as
 select i_product_name product_name,i_item_sk item_sk,s_store_name store_name,s_zip store_zip,ad1.ca_street_number b_street_number,ad1.ca_street_name b_streen_name,ad1.ca_city b_city,ad1.ca_zip b_zip,ad2.ca_street_number c_street_number,ad2.ca_street_name c_street_name,ad2.ca_city c_city,ad2.ca_zip c_zip,d1.d_year as syear,d2.d_year as fsyear,d3.d_year s2year,count(*) cnt,sum(ss_wholesale_cost) s1,sum(ss_list_price) s2,sum(ss_coupon_amt) s3
  FROM   store_sales,store_returns,cs_uiq64,date_dim d1,date_dim d2,date_dim d3,store,customer,customer_demographics cd1,customer_demographics cd2,promotion,household_demographics hd1,household_demographics hd2,customer_address ad1,customer_address ad2,income_band ib1,income_band ib2,item
  WHERE  ss_store_sk = s_store_sk AND ss_sold_date_sk = d1.d_date_sk AND ss_customer_sk = c_customer_sk AND ss_cdemo_sk= cd1.cd_demo_sk AND ss_hdemo_sk = hd1.hd_demo_sk AND ss_addr_sk = ad1.ca_address_sk and ss_item_sk = i_item_sk and ss_item_sk = sr_item_sk and ss_ticket_number = sr_ticket_number and ss_item_sk = cs_uiq64.cs_item_sk and c_current_cdemo_sk = cd2.cd_demo_sk AND c_current_hdemo_sk = hd2.hd_demo_sk AND c_current_addr_sk = ad2.ca_address_sk and c_first_sales_date_sk = d2.d_date_sk and c_first_shipto_date_sk = d3.d_date_sk and ss_promo_sk = p_promo_sk and hd1.hd_income_band_sk = ib1.ib_income_band_sk and hd2.hd_income_band_sk = ib2.ib_income_band_sk and cd1.cd_marital_status <> cd2.cd_marital_status and
         i_color in ('COLOR1','COLOR2','COLOR3','COLOR4','COLOR5','COLOR6') and
         i_current_price between 10 and 20 and
         i_current_price between 11 and 25
group by i_product_name,i_item_sk,s_store_name,s_zip,ad1.ca_street_number,ad1.ca_street_name,ad1.ca_city,ad1.ca_zip,ad2.ca_street_number,ad2.ca_street_name,ad2.ca_city,ad2.ca_zip,d1.d_year,d2.d_year,d3.d_year;


select cs1.product_name,cs1.store_name,cs1.store_zip,cs1.b_street_number,cs1.b_streen_name,cs1.b_city,cs1.b_zip,cs1.c_street_number,cs1.c_street_name,cs1.c_city,cs1.c_zip,cs1.syear,cs1.cnt,cs1.s1,cs1.s2,cs1.s3,cs2.s1,cs2.s2,cs2.s3,cs2.syear,cs2.cnt
from cross_salesq64 AS cs1,cross_salesq64 AS cs2
where cs1.item_sk=cs2.item_sk and cs1.syear = 2000 and cs2.syear = 2001 and cs2.cnt <= cs1.cnt and cs1.store_name = cs2.store_name and cs1.store_zip = cs2.store_zip;
--q29


 select i_item_id,i_item_desc,s_store_id,s_store_name,sum(ss_quantity) as store_sales_quantity,sum(sr_return_quantity) as store_returns_quantity,sum(cs_quantity) as catalog_sales_quantity
 from store_sales,store_returns,catalog_sales,date_dim AS d1,date_dim AS d2,date_dim AS d3,store,item
 where
     d1.d_moy               = 4
 and d1.d_year              = 2000
 and d1.d_date_sk           = ss_sold_date_sk
 and i_item_sk              = ss_item_sk
 and s_store_sk             = ss_store_sk
 and ss_customer_sk         = sr_customer_sk
 and ss_item_sk             = sr_item_sk
 and ss_ticket_number       = sr_ticket_number
 and sr_returned_date_sk    = d2.d_date_sk
 and d2.d_moy               between 4 and  7
 and d2.d_year              = 2000
 and sr_customer_sk         = cs_bill_customer_sk
 and sr_item_sk             = cs_item_sk
 and cs_sold_date_sk        = d3.d_date_sk     
 and d3.d_year              in (2000,2001,2002)
 group by i_item_id,i_item_desc,s_store_id,s_store_name;
--q25


 select i_item_id,i_item_desc,s_store_id,s_store_name,sum(ss_net_profit) as store_sales_profit,sum(sr_net_loss) as store_returns_loss,sum(cs_net_profit) as catalog_sales_profit
 from store_sales,store_returns,catalog_sales,date_dim AS d1,date_dim AS d2,date_dim AS d3,store,item
 where
 d1.d_moy = 4
 and d1.d_year = 2000
 and d1.d_date_sk = ss_sold_date_sk
 and i_item_sk = ss_item_sk
 and s_store_sk = ss_store_sk
 and ss_customer_sk = sr_customer_sk
 and ss_item_sk = sr_item_sk
 and ss_ticket_number = sr_ticket_number
 and sr_returned_date_sk = d2.d_date_sk
 and d2.d_moy between 4 and  10
 and d2.d_year = 2000
 and sr_customer_sk = cs_bill_customer_sk
 and sr_item_sk = cs_item_sk
 and cs_sold_date_sk = d3.d_date_sk
 and d3.d_moy between 4 and  10 
 and d3.d_year = 2000
 group by i_item_id,i_item_desc,s_store_id,s_store_name;
--q17


 select i_item_id,i_item_desc,s_state,count(ss_quantity) as store_sales_quantitycount, avg(ss_quantity) as store_sales_quantityave,max(ss_quantity) as store_sales_quantitystdev,count(sr_return_quantity) as_store_returns_quantitycount,avg(sr_return_quantity) as_store_returns_quantityave,max(sr_return_quantity) as_store_returns_quantitystdev,count(cs_quantity) as catalog_sales_quantitycount,avg(cs_quantity) as catalog_sales_quantityave
 from store_sales,store_returns,catalog_sales,date_dim AS d1,date_dim AS d2,date_dim AS d3,store,item
 where d1.d_quarter_name = '2000Q1'
   and d1.d_date_sk = ss_sold_date_sk
   and i_item_sk = ss_item_sk
   and s_store_sk = ss_store_sk
   and ss_customer_sk = sr_customer_sk
   and ss_item_sk = sr_item_sk
   and ss_ticket_number = sr_ticket_number
   and sr_returned_date_sk = d2.d_date_sk
   and d2.d_quarter_name in ('2000Q1','2000Q2','2000Q3')
   and sr_customer_sk = cs_bill_customer_sk
   and sr_item_sk = cs_item_sk
   and cs_sold_date_sk = d3.d_date_sk
   and d3.d_quarter_name in ('2000Q1','2000Q2','2000Q3')
 group by i_item_id,i_item_desc,s_state;
--q99


select w_warehouse_name,sm_type,cc_name,cs_ship_date_sk,cs_sold_date_sk
from catalog_sales,warehouse,ship_mode,call_center,date_dim
where d_month_seq between 1 and 12
and cs_ship_date_sk   = d_date_sk
and cs_warehouse_sk   = w_warehouse_sk
and cs_ship_mode_sk   = sm_ship_mode_sk
and cs_call_center_sk = cc_call_center_sk
group by w_warehouse_name,sm_type,cc_name;
--q67
create view q67v as
select i_category,i_class,i_brand,i_product_name,d_year,d_qoy,d_moy,s_store_id,sum(ss_sales_price*ss_quantity) sumsales
      from store_sales,date_dim,store,item
      where  ss_sold_date_sk=d_date_sk
          and ss_item_sk=i_item_sk
          and ss_store_sk = s_store_sk
          and d_month_seq between 1 and 12
       group by  i_category, i_class, i_brand, i_product_name, d_year, d_qoy, d_moy,s_store_id;


select i_category,i_class,i_brand,i_product_name,d_year,d_qoy,d_moy,s_store_id,sumsales
from q67v
where sumsales <= 100;
-- order by i_category,i_class,i_brand,i_product_name,d_year,d_qoy,d_moy,s_store_id,sumsales,rk;
--q93
create view q93t as
select ss_item_sk,ss_ticket_number,ss_customer_sk, (ss_quantity-sr_return_quantity)*ss_sales_price act_sales
      from store_sales,store_returns,reason
      where sr_item_sk = ss_item_sk and sr_ticket_number = ss_ticket_number and sr_reason_sk = r_reason_sk and r_reason_desc = 'REASON';


select ss_customer_sk ,sum(act_sales) sumsales
from q93t
group by ss_customer_sk;
--q50


select s_store_name,s_company_id,s_street_number,s_street_name,s_street_type,s_suite_number,s_city,s_county,s_state,s_zip,sr_returned_date_sk,ss_sold_date_sk
from store_sales,store_returns,store,date_dim AS d1,date_dim AS d2
where
    d2.d_year = 2000
and d2.d_moy  = 8
and ss_ticket_number = sr_ticket_number
and ss_item_sk = sr_item_sk
and ss_sold_date_sk   = d1.d_date_sk
and sr_returned_date_sk   = d2.d_date_sk
and ss_customer_sk = sr_customer_sk
and ss_store_sk = s_store_sk
group by s_store_name,s_company_id,s_street_number,s_street_name;
