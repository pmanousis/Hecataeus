--q3
CREATE VIEW QUERY3 AS
select dt.d_year, item.i_brand_id brand_id, item.i_brand brand, sum(ss_sales_price) sum_agg
 from  date_dim dt, store_sales, item
 where dt.d_date_sk = store_sales.ss_sold_date_sk
   and store_sales.ss_item_sk = item.i_item_sk
   and item.i_manufact_id = 100
   and dt.d_moy=11
 group by dt.d_year, item.i_brand, item.i_brand_id;
--q6
CREATE VIEW QUERY6 AS
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
CREATE VIEW QUERY7 AS
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
CREATE VIEW QUERY13 AS
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
CREATE VIEW QUERY19 AS
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
CREATE VIEW QUERY22 AS
select i_product_name, i_brand, i_class, i_category, avg(inv_quantity_on_hand) qoh
from inventory, date_dim, item, warehouse
where inv_date_sk=d_date_sk
  and inv_item_sk=i_item_sk
  and inv_warehouse_sk = w_warehouse_sk
group by (i_product_name, i_brand, i_class, i_category);
--q27
CREATE VIEW QUERY27 AS
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
CREATE VIEW QUERY34 AS
 select c_last_name, c_first_name, c_salutation, c_preferred_cust_flag, ss_ticket_number, cnt
 from dn34, customer
    where ss_customer_sk = c_customer_sk
      and cnt between 15 and 20;
--q36
CREATE VIEW QUERY36 AS
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
CREATE VIEW QUERY43 AS
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
CREATE VIEW QUERY46 AS
 select c_last_name, c_first_name, ca_city, bought_city, ss_ticket_number, amt, profit 
 from
    dn, customer,customer_address current_addr
 where ss_customer_sk = c_customer_sk
       and customer.c_current_addr_sk = current_addr.ca_address_sk
       and current_addr.ca_city <> bought_city;
--q48
CREATE VIEW QUERY48 AS
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
CREATE VIEW QUERY52 AS
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
CREATE VIEW QUERY53 AS
select  * from tmp1q53
where avg_quarterly_sales > 0.1;
--q55
CREATE VIEW QUERY55 AS
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
CREATE VIEW QUERY59 AS
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
CREATE VIEW QUERY61 AS
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
CREATE VIEW QUERY63 AS
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
CREATE VIEW QUERY65 AS
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
CREATE VIEW QUERY68 AS
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
CREATE VIEW QUERY73 AS
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
CREATE VIEW QUERY79 AS
 select c_last_name,c_first_name,s_city,ss_ticket_number,amt,profit
  from ms79,customer
    where ss_customer_sk = c_customer_sk;
--q82
CREATE VIEW QUERY82 AS
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
CREATE VIEW QUERY88 AS
select  *
from sena, sdyo, stria, stessera, spente, sexi, sefta, soxto;
--q89
create view tmp1q89 as
select i_category, i_class, i_brand,s_store_name, s_company_name,d_moy,sum(ss_sales_price) sum_sales,avg(ss_sales_price) as avg_monthly_sales
from item, store_sales, date_dim, store
where ss_item_sk = i_item_sk and ss_sold_date_sk = d_date_sk and ss_store_sk = s_store_sk and d_year in (2000) and
      ((i_category in ('CAT_A','CAT_B','CAT_C') and i_class in ('CLASS_A','CLASS_B','CLASS_C')) or (i_category in ('CAT_D','CAT_E','CAT_F') and i_class in ('CLASS_D','CLASS_E','CLASS_F')))
group by i_category, i_class, i_brand,s_store_name, s_company_name, d_moy;
CREATE VIEW QUERY89 AS
select *
from  tmp1q89
where avg_monthly_sales > 0.1;
--q96
CREATE VIEW QUERY96 AS
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
CREATE VIEW QUERY98 AS
select i_item_desc,i_category,i_class,i_current_price,sum(ss_ext_sales_price) as itemrevenue, sum(ss_ext_sales_price) as revenueratio
from
	store_sales,item,date_dim
where 
	ss_item_sk = i_item_sk 
  	and i_category in ('CATEGORY1', 'CATEGORY2', 'CATEGORY3')
  	and ss_sold_date_sk = d_date_sk
	and d_date between '2000-01-01' and '2000-01-31'
group by i_item_id,i_item_desc,i_category,i_class,i_current_price;
