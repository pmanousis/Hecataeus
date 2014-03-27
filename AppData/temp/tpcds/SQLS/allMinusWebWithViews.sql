--q3
CREATE VIEW QUERY3 AS
select d_year, i_brand_id brand_id, i_brand brand, sum(ss_sales_price) sum_agg
 from  ssid
 where i_manufact_id = 100
   and d_moy=11
 group by d_year, i_brand,i_brand_id;
 --order by dt.d_year, sum_agg desc, brand_id
--q6
CREATE VIEW QUERY6 AS
 select ca_state state, count(*) cnt
 from v_customer, ssid
 where v_customer.c_customer_sk = ssid.ss_customer_sk
 	and ssid.d_month_seq =
 	     (select distinct (d_month_seq)
 	      from date_dim
               where d_year = 2000
 	        and d_moy = 5 )
 	and ssid.i_current_price > 1.2 *
             (select avg(j.i_current_price)
 	     from item j
 	     where j.i_category = ssid.i_category)
 group by ca_state;
 --having count(*) >= 10
 --order by cnt
--q7
CREATE VIEW QUERY7 AS
select i_item_id, avg(ss_quantity) agg1, avg(ss_list_price) agg2, avg(ss_coupon_amt) agg3, avg(ss_sales_price) agg4
 from customer_demographics, ssid, promotion
 where ss_cdemo_sk = cd_demo_sk and
       ss_promo_sk = p_promo_sk and
       cd_gender = 1 and
       cd_marital_status = 1 and
       cd_education_status = 1 and
       (p_channel_email = 'N' or p_channel_event = 'N') and
       d_year = 2000
 group by i_item_id;
 --order by i_item_id
--q8
-- select s_store_name, sum(ss_net_profit)
--  from store_sales, date_dim, store,
--      (select ca_zip
--      from (
--      (SELECT substr(ca_zip,1,5) ca_zip
--       FROM customer_address
--       WHERE substr(ca_zip,1,5) IN ('ZIP.1','ZIP.2','ZIP.3','ZIP.4','ZIP.5','ZIP.6'))
--      intersect
--      (select ca_zip
--       from (SELECT substr(ca_zip,1,5) ca_zip,count(*) cnt
--             FROM customer_address, customer
--             WHERE ca_address_sk = c_current_addr_sk and c_preferred_cust_flag='Y'
--             group by ca_zip
--             --having count(*) > 10
-- 	    )A1
--      )
--     )A2) V1
--  where ss_store_sk = s_store_sk
--   and ss_sold_date_sk = d_date_sk
--   and d_qoy = 1 and d_year = 2000
--   and (substr(s_zip,1,2) = substr(V1.ca_zip,1,2))
--  group by s_store_name;
--  --order by s_store_name
--q13
CREATE VIEW QUERY13 AS
select avg(ss_quantity) ssqavg, avg(ss_ext_sales_price)ssextspravg, avg(ss_ext_wholesale_cost) ssextwscavg, sum(ss_ext_wholesale_cost) ssextwscsum
 from store_sales, store, household_demographics, date_dim, v_customer
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
 from ssid, store, v_customer
 where i_manager_id=1
   and d_moy=11
   and d_year=2000
   and ss_customer_sk = c_customer_sk
   and ca_zip <> s_zip
   and ss_store_sk = s_store_sk
 group by i_brand, i_brand_id, i_manufact_id, i_manufact;
--order by ext_price desc,i_brand, i_brand_id, i_manufact_id, i_manufact;
--q22
CREATE VIEW QUERY22 AS
select i_product_name, i_brand, i_class, i_category, avg(inv_quantity_on_hand) qoh
from inventory, date_dim, item, warehouse
where inv_date_sk=d_date_sk
  and inv_item_sk=i_item_sk
  and inv_warehouse_sk = w_warehouse_sk
--  and d_month_seq between DMS and DMS + 11 ??
group by (i_product_name, i_brand, i_class, i_category);--group by rollup(i_product_name, i_brand, i_class, i_category);
--order by qoh, i_product_name, i_brand, i_class, i_category;
--q27
CREATE VIEW QUERY27 AS
 select i_item_id, s_state, avg(ss_quantity) agg1, avg(ss_list_price) agg2, avg(ss_coupon_amt) agg3, avg(ss_sales_price) agg4
 from customer_demographics, store, ssid
 where ss_store_sk = s_store_sk and
       ss_cdemo_sk = cd_demo_sk and
       cd_gender = 1 and
       cd_marital_status = 1 and
       cd_education_status = 1 and
       d_year = 2000 and
       s_state in ('STATE_A','STATE_B', 'STATE_C', 'STATE_D', 'STATE_E', 'STATE_F')
 group by (i_item_id, s_state);
--  --order by i_item_id, s_state;
--q34
--and (case when household_demographics.hd_vehicle_count > 0 then household_demographics.hd_dep_count/ household_demographics.hd_vehicle_count else null end)  > 1.2
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
-- --    order by c_last_name,c_first_name,c_salutation,c_preferred_cust_flag desc;
--q36
CREATE VIEW QUERY36 AS
 select sum(ss_net_profit) ssntprofsum,sum(ss_ext_sales_price) ssextspsum, i_category, i_class
 from ssid, store
 where
    ssid.d_year = 2000
 and s_store_sk  = ss_store_sk
 and s_state in ('STATE_A','STATE_B','STATE_C','STATE_D','STATE_E','STATE_F','STATE_G','STATE_H')
 group by (i_category,i_class);
-- order by lochierarchy desc, case when lochierarchy = 0 then i_category end, rank_within_parent;
--q43
CREATE VIEW QUERY43 AS
 select s_store_name, s_store_id,sum(ss_sales_price) ssspsum
 from date_dim, store_sales, store
 where d_date_sk = ss_sold_date_sk and
       s_store_sk = ss_store_sk and
       s_gmt_offset = 6 and
       d_year = 2000
 group by s_store_name, s_store_id,d_day_name;
--  --order by s_store_name, s_store_id,sun_sales,mon_sales,tue_sales,wed_sales,thu_sales,fri_sales,sat_sales;
--q46
--modification: creation of view:
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
 from dn, v_customer
 where ss_customer_sk = c_customer_sk
       and ca_city <> bought_city;
  --order by c_last_name, c_first_name, ca_city, bought_city, ss_ticket_number;
--q47
-- create view v1 as(
--  select i_category, i_brand,
--         s_store_name, s_company_name,
--         d_year, d_moy,
--         sum(ss_sales_price) sum_sales,
--         avg(sum(ss_sales_price)) over
--           (partition by i_category, i_brand, s_store_name, s_company_name, d_year)
--           avg_monthly_sales,
--         rank() over
--           (partition by i_category, i_brand,
--                      s_store_name, s_company_name
--            order by d_year, d_moy) rn
--  from item, store_sales, date_dim, store
--  where ss_item_sk = i_item_sk and
--        ss_sold_date_sk = d_date_sk and
--        ss_store_sk = s_store_sk and
--        (
--          d_year = 2000 or
--          ( d_year = 1999 and d_moy =12) or
--          ( d_year = 2001 and d_moy =1)
--        )
--  group by i_category, i_brand, s_store_name, s_company_name, d_year, d_moy);
--
-- create view v2 as(
--  select --??SELECTCONE
--         --??SELECTCTWO,
--         v1.avg_monthly_sales
--         ,v1.sum_sales, v1_lag.sum_sales psum, v1_lead.sum_sales nsum
--  from v1, v1 v1_lag, v1 v1_lead
--  where v1.i_category = v1_lag.i_category and
--        v1.i_category = v1_lead.i_category and
--        v1.i_brand = v1_lag.i_brand and
--        v1.i_brand = v1_lead.i_brand and
--        v1.s_store_name = v1_lag.s_store_name and
--        v1.s_store_name = v1_lead.s_store_name and
--        v1.s_company_name = v1_lag.s_company_name and
--        v1.s_company_name = v1_lead.s_company_name and
--        v1.rn = v1_lag.rn + 1 and
--        v1.rn = v1_lead.rn - 1);
--
--  select *
--  from v2
--  where  d_year = 2000 and
--         avg_monthly_sales > 0 and
--         case when avg_monthly_sales > 0 then abs(sum_sales - avg_monthly_sales) / avg_monthly_sales else null end > 0.1;
-- -- order by sum_sales - avg_monthly_sales, 3;
--q48
CREATE VIEW QUERY48 AS
select sum (ss_quantity) ssqsum
 from store_sales, store, v_customer, date_dim
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
--q51
-- create view web_v1 as (
-- select ws_item_sk item_sk, d_date, sum(sum(ws_sales_price)) over (partition by ws_item_sk order by d_date rows between unbounded preceding and current row) cume_sales
-- from web_sales,date_dim
-- where ws_sold_date_sk=d_date_sk and d_month_seq between 1 and 12 and ws_item_sk is not NULL
-- group by ws_item_sk, d_date);
--
-- create store_v1 as (
-- select ss_item_sk item_sk, d_date, sum(sum(ss_sales_price)) over (partition by ss_item_sk order by d_date rows between unbounded preceding and current row) cume_sales
-- from store_sales,date_dim
-- where ss_sold_date_sk=d_date_sk and d_month_seq between 1 and 12 and ss_item_sk is not NULL
-- group by ss_item_sk, d_date);
--
-- select *
-- from (select item_sk, d_date, web_sales, store_sales, max(web_sales) over (partition by item_sk order by d_date rows between unbounded preceding and current row) web_cumulative ,max(store_sales) over (partition by item_sk order by d_date rows between unbounded preceding and current row) store_cumulative
--      from (select case when web.item_sk is not null then web.item_sk else store.item_sk end item_sk,case when web.d_date is not null then web.d_date else store.d_date end d_date,web.cume_sales web_sales,store.cume_sales store_sales
--            from web_v1 web full outer join store_v1 store on (web.item_sk = store.item_sk and web.d_date = store.d_date)
--           )x
--      )y
-- where web_cumulative > store_cumulative
-- --order by item_sk, d_date;
--q52
CREATE VIEW QUERY52 AS
select d_year, i_brand_id brand_id, i_brand brand, sum(ss_ext_sales_price) ext_price
from ssid
where i_manager_id = 1
    and d_moy=11
    and d_year=2000
group by d_year, i_brand, i_brand_id;
 --order by dt.d_year,ext_price desc,brand_id;
--q53
create view tmp1q53 as
select i_manufact_id, sum(ss_sales_price) sum_sales, avg(ss_sales_price) avg_quarterly_sales
 from ssid, store
 where ss_store_sk = s_store_sk and
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
-- --order by avg_quarterly_sales, sum_sales, i_manufact_id;
--q55
CREATE VIEW QUERY55 AS
select i_brand_id brand_id, i_brand brand, sum(ss_ext_sales_price) ext_price
 from ssid
 where i_manager_id=1
 	and d_moy=11
 	and d_year=2000
 group by i_brand, i_brand_id;
 --order by ext_price desc, i_brand_id;
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
 --order by s_store_name1,s_store_id1,d_week_seq1;
--q61
create view all_sales as
select sum(ss_ext_sales_price) total
   from  store, v_customer, ssid
   where ss_store_sk = s_store_sk
   and   ss_customer_sk= c_customer_sk
   and   ca_gmt_offset = 3
   and   i_category = 'Books'
   and   s_gmt_offset = 3
   and   d_year = 2000
   and   d_moy  = 11;

create view promotional_sales as
select sum(ss_ext_sales_price) promotions
   from  store, promotion, v_customer, ssid
   where ss_store_sk = s_store_sk
   and   ss_promo_sk = p_promo_sk
   and   ss_customer_sk= c_customer_sk
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
      from ssid,store
      where ss_store_sk = s_store_sk
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
-- --order by i_manager_id,avg_monthly_sales,sum_sales;
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
 --order by s_store_name, i_item_desc;
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
 from dn68,v_customer
 where ss_customer_sk = c_customer_sk
   and ca_city <> bought_city;
--  --order by c_last_name,ss_ticket_number;
--q70
--  select sum(ss_net_profit) as total_sum,s_state,s_county,grouping(s_state)+grouping(s_county) as lochierarchy,rank() over (partition by grouping(s_state)+grouping(s_county),case when grouping(s_county) = 0 then s_state end order by sum(ss_net_profit) desc) as rank_within_parent
--  from store_sales,date_dim d1,store
--  where
--     d1.d_month_seq between 1 and 12
--  and d1.d_date_sk = ss_sold_date_sk
--  and s_store_sk  = ss_store_sk
--  and s_state in
--              ( select s_state
--                from  (select s_state as s_state, rank() over ( partition by s_state order by sum(ss_net_profit) desc) as ranking
--                       from   store_sales, store, date_dim
--                       where  d_month_seq between 1 and 12
--  			    and d_date_sk = ss_sold_date_sk
--  			    and s_store_sk  = ss_store_sk
--                       group by s_state
--                      ) tmp1
--                where ranking <= 5
--              )
--  group by rollup(s_state,s_county);
--  --order by lochierarchy desc, case when lochierarchy = 0 then s_state end,rank_within_parent;
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
-- --    order by cnt desc;
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
--  --order by c_last_name,c_first_name,substr(s_city,1,30), profit;
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
 --order by i_item_id;
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
--order by sum_sales - avg_monthly_sales, s_store_name
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
--order by count(*)
--q98
CREATE VIEW QUERY98 AS
select i_item_desc,i_category,i_class,i_current_price,sum(ss_ext_sales_price) as itemrevenue, sum(ss_ext_sales_price) as revenueratio
from ssid
where i_category in ('CATEGORY1', 'CATEGORY2', 'CATEGORY3') and d_date between '2000-01-01' and '2000-01-31'
group by i_item_id,i_item_desc,i_category,i_class,i_current_price;
-- --order by i_category,i_class,i_item_id,i_item_desc,revenueratio;
--q15
CREATE VIEW QUERY15 AS
 select ca_zip,sum(cs_sales_price) csspsum
 from catalog_sales,v_customer,date_dim
 where cs_bill_customer_sk = c_customer_sk
 	and ( ca_zip in ('85669', '86197','88274','83405','86475','85392', '85460', '80348', '81792')
 	      or ca_state in ('CA','WA','GA')
 	      or cs_sales_price > 500)
 	and cs_sold_date_sk = d_date_sk
 	and d_qoy = 1 and d_year = 2000
 group by ca_zip;
 --order by ca_zip;
--q16
CREATE VIEW QUERY16 AS
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
-- --order by count(distinct cs_order_number);
--q18
CREATE VIEW QUERY18 AS
select i_item_id,ca_country,ca_state,ca_county, avg(cs_quantity) agg1, avg(cs_list_price) agg2,avg(cs_coupon_amt) agg3,avg(cs_sales_price) agg4,avg(cs_net_profit) agg5,avg(c_birth_year) agg6,avg(cd1.cd_dep_count) agg7
from customer_demographics cd1, customer_demographics cd2, v_customer, csid
 where cs_bill_cdemo_sk = cd1.cd_demo_sk and
       cs_bill_customer_sk = c_customer_sk and
       cd1.cd_gender = 1 and
       cd1.cd_education_status = 1 and
       c_current_cdemo_sk = cd2.cd_demo_sk and
       c_birth_month in (1,3,5,7,9,11) and
       d_year = 2000 and
       ca_state in ('STATE.1','STATE.2','STATE.3','STATE.4','STATE.5','STATE.6','STATE.7')
 group by (i_item_id, ca_country, ca_state, ca_county);
 --order by ca_country,ca_state, ca_county,i_item_id;

--q20
CREATE VIEW QUERY20 AS
 select i_item_desc ,i_category ,i_class ,i_current_price,sum(cs_ext_sales_price) as itemrevenue
 from	csid
 where i_category in (1,2,3)
 and d_date between '2000-01-01' and '2000-01-31'
 group by i_item_id,i_item_desc ,i_category,i_class,i_current_price;
 --order by i_category,i_class,i_item_id,i_item_desc,revenueratio;
--q26
CREATE VIEW QUERY26 AS
 select i_item_id, avg(cs_quantity) agg1,avg(cs_list_price) agg2,avg(cs_coupon_amt) agg3,avg(cs_sales_price) agg4
 from customer_demographics, csid, promotion
 where cs_bill_cdemo_sk = cd_demo_sk and
       cs_promo_sk = p_promo_sk and
       cd_gender = 1 and
       cd_marital_status = 1 and
       cd_education_status = 1 and
       (p_channel_email = 'N' or p_channel_event = 'N') and
       d_year = 2000
 group by i_item_id;
-- order by i_item_id;
--q32
create view vq32 as
select avg(cs_ext_discount_amt) as vq32result
from catalog_sales ,date_dim
where d_date between '2000-01-01' and '2000-03-01'
  and d_date_sk = cs_sold_date_sk;
CREATE VIEW QUERY32 AS
select sum(cs_ext_discount_amt)  as excess_discount_amount
from csid
where
i_manufact_id = 1
and d_date between '2000-01-01' and '2000-03-01'
and cs_ext_discount_amt > (select vq32result from vq32);

--q37
CREATE VIEW QUERY37 AS
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
 --order by i_item_id;
--q40
CREATE VIEW QUERY40 AS
 select w_state,i_item_id,sum(cs_sales_price - cr_refunded_cash) as sales_before, sum(cs_sales_price - cr_refunded_cash) as sales_after
 from catalog_sales left outer join catalog_returns on (cs_order_number = cr_order_number and cs_item_sk = cr_item_sk), warehouse,item,date_dim
 where
     i_current_price between 0.99 and 1.49
 and i_item_sk          = cs_item_sk
 and cs_warehouse_sk    = w_warehouse_sk
 and cs_sold_date_sk    = d_date_sk
 and d_date between '2000-01-01' and '2000-03-01'
 group by w_state,i_item_id;
 --order by w_state,i_item_id;
--q57
create view v1q57 as
 select i_category, i_brand,cc_name,d_year, d_moy,
        sum(cs_sales_price) sum_sales,
        avg(cs_sales_price) avg_monthly_sales
 from csid, call_center
 where 
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
CREATE VIEW QUERY57 AS
 select *
 from v2q57
 where  d_year = 2000 and avg_monthly_sales > 0 and
        (sum_sales - avg_monthly_sales) / avg_monthly_sales > 0.1;
-- order by sum_sales - avg_monthly_sales, 3
--q91
CREATE VIEW QUERY91 AS
select cc_call_center_id Call_Center, cc_name Call_Center_Name, cc_manager Manager, sum(cr_net_loss) Returns_Loss
from call_center, catalog_returns, date_dim, v_customer, household_demographics
where   cr_call_center_sk       = cc_call_center_sk
and     cr_returned_date_sk     = d_date_sk
and     cr_returning_customer_sk= c_customer_sk
and     hd_demo_sk              = c_current_hdemo_sk
and     d_year                  = 2000
and     d_moy                   = 11
and     ( (cd_marital_status       = 'M' and cd_education_status     = 'Unknown')
        or(cd_marital_status       = 'W' and cd_education_status     = 'Advanced Degree'))

and     ca_gmt_offset           = 03
group by cc_call_center_id,cc_name,cc_manager,cd_marital_status,cd_education_status;
--order by sum(cr_net_loss) desc;
--and     hd_buy_potential like 'BUY_POTENTIAL%'
--q84
CREATE VIEW QUERY84 AS
 select c_customer_id as customer_id, c_last_name ,c_first_name
 from v_customer,household_demographics,income_band,store_returns
 where ca_city	        =  'Ioannina'
   and ib_lower_bound   >=  30000
   and ib_upper_bound   <=  80000
   and ib_income_band_sk = hd_income_band_sk
   and hd_demo_sk = c_current_hdemo_sk
   and sr_cdemo_sk = cd_demo_sk;
-- order by c_customer_id;
--q83
-- create view vq83dweek as
-- select d_week_seq
-- from date_dim
-- where d_date in ('2000-01-01','2000-08-01','2000-11-01');
-- 
-- create view vq83date as
-- select d_date
-- from date_dim
-- where d_week_seq in (select * from vq83dweek);
-- 
-- create view sr_items as
--  select i_item_id item_id,sum(sr_return_quantity) sr_item_qty
--  from store_returns,item,date_dim
--  where sr_item_sk = i_item_sk
--  and   d_date in (select * from vq83date)
--  and   sr_returned_date_sk   = d_date_sk
--  group by i_item_id;
-- 
-- create view cr_items as
--  select i_item_id item_id, sum(cr_return_quantity) cr_item_qty
--  from catalog_returns, item,date_dim
--  where cr_item_sk = i_item_sk
--  and   d_date in (select * from vq83date)
--  and   cr_returned_date_sk   = d_date_sk
--  group by i_item_id;
-- 
-- 
-- create view wr_items as
--  select i_item_id item_id, sum(wr_return_quantity) wr_item_qty
--  from web_returns, item, date_dim
--  where wr_item_sk = i_item_sk
--  and   d_date in (select * from vq83date)
--  and   wr_returned_date_sk   = d_date_sk
--  group by i_item_id;
-- CREATE VIEW QUERY83 AS
--  select sr_items.item_id,sr_item_qty
--        ,sr_item_qty/(sr_item_qty+cr_item_qty+wr_item_qty)/3.0 * 100 sr_dev
--        ,cr_item_qty
--        ,cr_item_qty/(sr_item_qty+cr_item_qty+wr_item_qty)/3.0 * 100 cr_dev
--        ,wr_item_qty
--        ,wr_item_qty/(sr_item_qty+cr_item_qty+wr_item_qty)/3.0 * 100 wr_dev
--        ,(sr_item_qty+cr_item_qty+wr_item_qty)/3.0 average
--  from sr_items,cr_items,wr_items
--  where sr_items.item_id=cr_items.item_id
--    and sr_items.item_id=wr_items.item_id;
 --order by sr_items.item_id,sr_item_qty
--q81
--Warning:nested with bad representation!!!!!
 create view customer_total_return as
 select cr_returning_customer_sk as ctr_customer_sk ,ca_state as ctr_state,  sum(cr_return_amt_inc_tax) as ctr_total_return
 from catalog_returns,date_dim,customer_address
 where cr_returned_date_sk = d_date_sk
   and d_year =2000
   and cr_returning_addr_sk = ca_address_sk
 group by cr_returning_customer_sk,ca_state;
CREATE VIEW QUERY81 AS
 select c_customer_id,c_salutation,c_first_name,c_last_name,ca_street_number,ca_street_name,ca_street_type,ca_suite_number,ca_city,ca_county,ca_state,ca_zip,ca_country,ca_gmt_offset,ca_location_type,ctr_total_return
 from customer_total_return ctr1,v_customer
 where ctr1.ctr_total_return > (select max(ctr_total_return)*1.2
 			  from customer_total_return ctr2
                  	  where ctr1.ctr_state = ctr2.ctr_state)
       and ca_state = 'Hpeiros'
       and ctr1.ctr_customer_sk = c_customer_sk;
--  --order by c_customer_id,c_salutation,c_first_name,c_last_name,ca_street_number,ca_street_name,ca_street_type,ca_suite_number,ca_city,ca_county,ca_state,ca_zip,ca_country,ca_gmt_offset,ca_location_type,ctr_total_return;
--q75
--Union problem
-- create view sales_detail as
-- SELECT d_year,i_brand_id,i_class_id,i_category_id,i_manufact_id,(cs_quantity - cr_return_quantity) AS sales_cnt,(cs_ext_sales_price - cr_return_amount) AS sales_amt
--  FROM catalog_sales JOIN item ON i_item_sk=cs_item_sk JOIN date_dim ON d_date_sk=cs_sold_date_sk LEFT JOIN catalog_returns ON (cs_order_number=cr_order_number  AND cs_item_sk=cr_item_sk)
--  WHERE i_category='CATEGORY'
--  UNION
--  SELECT d_year,i_brand_id,i_class_id,i_category_id,i_manufact_id,(ss_quantity - sr_return_quantity) AS sales_cnt,(ss_ext_sales_price - sr_return_amt) AS sales_amt
--  FROM store_sales JOIN item ON i_item_sk=ss_item_sk JOIN date_dim ON d_date_sk=ss_sold_date_sk LEFT JOIN store_returns ON (ss_ticket_number=sr_ticket_number AND ss_item_sk=sr_item_sk)
--  WHERE i_category='CATEGORY'
--  UNION
--  SELECT d_year,i_brand_id,i_class_id,i_category_id,i_manufact_id,(ws_quantity - wr_return_quantity) AS sales_cnt,(ws_ext_sales_price - wr_return_amt) AS sales_amt
--  FROM web_sales JOIN item ON i_item_sk=ws_item_sk JOIN date_dim ON d_date_sk=ws_sold_date_sk LEFT JOIN web_returns ON (ws_order_number=wr_order_number  AND ws_item_sk=wr_item_sk)
--  WHERE i_category='CATEGORY';
--
-- create view all_sales AS
--  SELECT d_year,i_brand_id,i_class_id,i_category_id,i_manufact_id,SUM(sales_cnt) AS sales_cnt,SUM(sales_amt) AS sales_amt
--  FROM sales_detail
--  GROUP BY d_year, i_brand_id, i_class_id, i_category_id, i_manufact_id;
--
-- SELECT prev_yr.d_year AS prev_year ,curr_yr.d_year AS year ,curr_yr.i_brand_id ,curr_yr.i_class_id ,curr_yr.i_category_id ,curr_yr.i_manufact_id ,prev_yr.sales_cnt AS prev_yr_cnt ,curr_yr.sales_cnt AS curr_yr_cnt ,curr_yr.sales_cnt-prev_yr.sales_cnt AS sales_cnt_diff ,curr_yr.sales_amt-prev_yr.sales_amt AS sales_amt_diff
--  FROM all_sales curr_yr, all_sales prev_yr
--  WHERE curr_yr.i_brand_id=prev_yr.i_brand_id
--    AND curr_yr.i_class_id=prev_yr.i_class_id
--    AND curr_yr.i_category_id=prev_yr.i_category_id
--    AND curr_yr.i_manufact_id=prev_yr.i_manufact_id
--    AND curr_yr.d_year=2001
--    AND prev_yr.d_year=2000
--    AND curr_yr.sales_cnt / prev_yr.sales_cnt < 0.9;
 --ORDER BY sales_cnt_diff;
--q44
-- create view v1q44 as
-- select ss_item_sk item_sk,avg(ss_net_profit) rank_col
--   from store_sales ss1
--   where ss_store_sk = 'STORE'
--   group by ss_item_sk
--   having avg(ss_net_profit) > 0.9*(select avg(ss_net_profit) rank_col
--                                    from store_sales
--                                    where ss_store_sk = 'STORE'
-- 				    and ss_customer_sk is null
-- 				    group by ss_store_sk);
--
-- create view v11q44 as
-- select item_sk,rank_col rnk
-- from v1q44;
--
-- create view ascending as
-- select *
-- from v11q44 where rnk < 11;
--
-- select asceding.rnk, i1.i_product_name best_performing, i2.i_product_name worst_performing
-- from asceding,
-- (select *
--  from (select item_sk,rank() over (order by rank_col desc) rnk
--        from (select ss_item_sk item_sk,avg(ss_net_profit) rank_col
--              from store_sales ss1
--              where ss_store_sk = 'STORE'
--              group by ss_item_sk
--                  having avg(ss_net_profit) > 0.9*(select avg(ss_net_profit) rank_col
--                                                   from store_sales
--                                                   where ss_store_sk = STORE
--                                                     and NULLCOLSS is null
--                                                   group by ss_store_sk))V2)V21
--      where rnk  < 11) descending,
-- item i1,
-- item i2
-- where asceding.rnk = descending.rnk
--   and i1.i_item_sk=asceding.item_sk
--   and i2.i_item_sk=descending.item_sk;
-- --order by asceding.rnk;
--q42
CREATE VIEW QUERY42 AS
 select d_year,i_category_id,i_category,sum(ss_ext_sales_price) ssextspsum
 from  ssid
 where i_manager_id = 1
 	and d_moy=11
 	and d_year=2000
 group by d_year,i_category_id,i_category;
-- order by       sum(ss_ext_sales_price) desc,dt.d_year,item.i_category_id,item.i_category;
--q41
--Nested problem!!!!!
CREATE VIEW QUERY41 AS
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
 --order by i_product_name;
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
CREATE VIEW QUERY28 AS
select *
from b1q28,b2q28,b3q28,b4q28,b5q28,b6q28;
--q30
--Warning:maybe not good!!!!!
-- create view customer_total_ret as
--  select wr_returning_customer_sk as ctr_customer_sk,ca_state as ctr_state,sum(wr_return_amt) as ctr_total_return
--  from web_returns,date_dim,customer_address
--  where wr_returned_date_sk = d_date_sk
--    and d_year =2000
--    and wr_returning_addr_sk = ca_address_sk
--  group by wr_returning_customer_sk,ca_state;
-- CREATE VIEW QUERY30 AS
--  select c_customer_id,c_salutation,c_first_name,c_last_name,c_preferred_cust_flag,c_birth_day,c_birth_month,c_birth_year,c_birth_country,c_login,c_email_address,c_last_review_date,ctr_total_return
--  from customer_total_ret ctr1,v_customer
--  where ctr1.ctr_total_return > (select avg(ctr_total_return)*1.2
--  			  from customer_total_ret ctr2
--                   	  where ctr1.ctr_state = ctr2.ctr_state)
--        and ca_state = 'STATE'
--        and ctr1.ctr_customer_sk = c_customer_sk;
 --order by c_customer_id,c_salutation,c_first_name,c_last_name,c_preferred_cust_flag,c_birth_day,c_birth_month,c_birth_year,c_birth_country,c_login,c_email_address,c_last_review_date,ctr_total_return;
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
CREATE VIEW QUERY21 AS
 select *
 from xq21before,xq21after
 where
 xq21before.w_warehouse_name=xq21after.w_warehouse_name
 and xq21before.i_item_id=xq21after.i_item_id
 and (xq21after.inv_sum / xq21before.inv_sum) between 2.0/3.0 and 3.0/2.0;
-- order by w_warehouse_name,i_item_id;
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

--q2
--Union
-- create view wscsq2 as
--  select sold_date_sk
--         ,sales_price
--   from (select ws_sold_date_sk sold_date_sk
--               ,ws_ext_sales_price sales_price
--         from web_sales) x
--         union all
--        (select cs_sold_date_sk sold_date_sk
--               ,cs_ext_sales_price sales_price
--         from catalog_sales);
--
--  create view wswscsq2 as
--  select d_week_seq,
--         sum(case when (d_day_name='Sunday') then sales_price else null end) sun_sales,
--         sum(case when (d_day_name='Monday') then sales_price else null end) mon_sales,
--         sum(case when (d_day_name='Tuesday') then sales_price else  null end) tue_sales,
--         sum(case when (d_day_name='Wednesday') then sales_price else null end) wed_sales,
--         sum(case when (d_day_name='Thursday') then sales_price else null end) thu_sales,
--         sum(case when (d_day_name='Friday') then sales_price else null end) fri_sales,
--         sum(case when (d_day_name='Saturday') then sales_price else null end) sat_sales
--  from wscs
--      ,date_dim
--  where d_date_sk = sold_date_sk
--  group by d_week_seq;
--
--  select d_week_seq1
--        ,round(sun_sales1/sun_sales2,2)
--        ,round(mon_sales1/mon_sales2,2)
--        ,round(tue_sales1/tue_sales2,2)
--        ,round(wed_sales1/wed_sales2,2)
--        ,round(thu_sales1/thu_sales2,2)
--        ,round(fri_sales1/fri_sales2,2)
--        ,round(sat_sales1/sat_sales2,2)
--  from
--  (select wswscs.d_week_seq d_week_seq1
--         ,sun_sales sun_sales1
--         ,mon_sales mon_sales1
--         ,tue_sales tue_sales1
--         ,wed_sales wed_sales1
--         ,thu_sales thu_sales1
--         ,fri_sales fri_sales1
--         ,sat_sales sat_sales1
--   from wswscs,date_dim
--   where date_dim.d_week_seq = wswscs.d_week_seq and
--         d_year = [YEAR]) y,
--  (select wswscs.d_week_seq d_week_seq2
--         ,sun_sales sun_sales2
--         ,mon_sales mon_sales2
--         ,tue_sales tue_sales2
--         ,wed_sales wed_sales2
--         ,thu_sales thu_sales2
--         ,fri_sales fri_sales2
--         ,sat_sales sat_sales2
--   from wswscs
--       ,date_dim
--   where date_dim.d_week_seq = wswscs.d_week_seq and
--         d_year = [YEAR]+1) z
--  where d_week_seq1=d_week_seq2-53
--  order by d_week_seq1;
--q1
create view customer_total_returnq1 as
select sr_customer_sk as ctr_customer_sk,sr_store_sk as ctr_store_sk,sum(SR_RETURN_TAX) as ctr_total_return
from store_returns,date_dim
where sr_returned_date_sk = d_date_sk
and d_year =2000
group by sr_customer_sk,sr_store_sk;
CREATE VIEW QUERY1 AS
select c_customer_id
from customer_total_returnq1 ctr1,store,customer
where ctr1.ctr_total_return > (select avg(ctr_total_return)*1.2
from customer_total_returnq1 ctr2
where ctr1.ctr_store_sk = ctr2.ctr_store_sk)
and s_store_sk = ctr1.ctr_store_sk
and s_state = 'STATE'
and ctr1.ctr_customer_sk = c_customer_sk;
--order by c_customer_id;
--q72
--Join problems
CREATE VIEW QUERY72 AS
select i_item_desc,w_warehouse_name,d1.d_week_seq,count(p_promo_sk) promo,count(*) total_cnt
from catalog_sales,inventory,warehouse,item,customer_demographics,household_demographics, date_dim d1,date_dim d2, date_dim d3, promotion, catalog_returns
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
-- --order by total_cnt desc, i_item_desc, w_warehouse_name, d_week_seq;
--q64
--having!!!!!
create view cs_uiq64 as
  select cs_item_sk,sum(cs_ext_list_price) as sale,sum(cr_refunded_cash+cr_reversed_charge+cr_store_credit) as refund
  from catalog_sales,catalog_returns
  where cs_item_sk = cr_item_sk
    and cs_order_number = cr_order_number
  group by cs_item_sk;
  --having sum(cs_ext_list_price)>2*sum(cr_refunded_cash+cr_reversed_charge+cr_store_credit);

create view cross_salesq64 as
 select i_product_name product_name,i_item_sk item_sk,s_store_name store_name,s_zip store_zip,ad1.ca_street_number b_street_number,ad1.ca_street_name b_streen_name,ad1.ca_city b_city,ad1.ca_zip b_zip,ad2.ca_street_number c_street_number,ad2.ca_street_name c_street_name,ad2.ca_city c_city,ad2.ca_zip c_zip,d1.d_year as syear,d2.d_year as fsyear,d3.d_year s2year,count(*) cnt,sum(ss_wholesale_cost) s1,sum(ss_list_price) s2,sum(ss_coupon_amt) s3
  FROM   store_sales,store_returns,cs_uiq64,date_dim d1,date_dim d2,date_dim d3,store,customer,customer_demographics cd1,customer_demographics cd2,promotion,household_demographics hd1,household_demographics hd2,customer_address ad1,customer_address ad2,income_band ib1,income_band ib2,item
  WHERE  ss_store_sk = s_store_sk AND ss_sold_date_sk = d1.d_date_sk AND ss_customer_sk = c_customer_sk AND ss_cdemo_sk= cd1.cd_demo_sk AND ss_hdemo_sk = hd1.hd_demo_sk AND ss_addr_sk = ad1.ca_address_sk and ss_item_sk = i_item_sk and ss_item_sk = sr_item_sk and ss_ticket_number = sr_ticket_number and ss_item_sk = cs_uiq64.cs_item_sk and c_current_cdemo_sk = cd2.cd_demo_sk AND c_current_hdemo_sk = hd2.hd_demo_sk AND c_current_addr_sk = ad2.ca_address_sk and c_first_sales_date_sk = d2.d_date_sk and c_first_shipto_date_sk = d3.d_date_sk and ss_promo_sk = p_promo_sk and hd1.hd_income_band_sk = ib1.ib_income_band_sk and hd2.hd_income_band_sk = ib2.ib_income_band_sk and cd1.cd_marital_status <> cd2.cd_marital_status and
         i_color in ('COLOR1','COLOR2','COLOR3','COLOR4','COLOR5','COLOR6') and
         i_current_price between 10 and 20 and
         i_current_price between 11 and 25
group by i_product_name,i_item_sk,s_store_name,s_zip,ad1.ca_street_number,ad1.ca_street_name,ad1.ca_city,ad1.ca_zip,ad2.ca_street_number,ad2.ca_street_name,ad2.ca_city,ad2.ca_zip,d1.d_year,d2.d_year,d3.d_year;
CREATE VIEW QUERY64 AS
select cs1.product_name,cs1.store_name,cs1.store_zip,cs1.b_street_number,cs1.b_streen_name,cs1.b_city,cs1.b_zip,cs1.c_street_number,cs1.c_street_name,cs1.c_city,cs1.c_zip,cs1.syear,cs1.cnt,cs1.s1,cs1.s2,cs1.s3,cs2.s1,cs2.s2,cs2.s3,cs2.syear,cs2.cnt
from cross_salesq64 cs1,cross_salesq64 cs2
where cs1.item_sk=cs2.item_sk and cs1.syear = 2000 and cs2.syear = 2001 and cs2.cnt <= cs1.cnt and cs1.store_name = cs2.store_name and cs1.store_zip = cs2.store_zip;
-- --order by cs1.product_name,cs1.store_name,cs2.cnt;
--q29
CREATE VIEW QUERY29 AS
 select i_item_id,i_item_desc,s_store_id,s_store_name,sum(ss_quantity) as store_sales_quantity,sum(sr_return_quantity) as store_returns_quantity,sum(cs_quantity) as catalog_sales_quantity
 from store_sales,store_returns,catalog_sales,date_dim d1,date_dim d2,date_dim d3,store,item
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
 --order by i_item_id ,i_item_desc,s_store_id,s_store_name;
--q25
CREATE VIEW QUERY25 AS
 select i_item_id,i_item_desc,s_store_id,s_store_name,sum(ss_net_profit) as store_sales_profit,sum(sr_net_loss) as store_returns_loss,sum(cs_net_profit) as catalog_sales_profit
 from store_sales,store_returns,catalog_sales,date_dim d1,date_dim d2,date_dim d3,store,item
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
-- order by i_item_id,i_item_desc,s_store_id,s_store_name;
--q17
CREATE VIEW QUERY17 AS
 select i_item_id,i_item_desc,s_state,count(ss_quantity) as store_sales_quantitycount, avg(ss_quantity) as store_sales_quantityave,max(ss_quantity) as store_sales_quantitystdev,count(sr_return_quantity) as_store_returns_quantitycount,avg(sr_return_quantity) as_store_returns_quantityave,max(sr_return_quantity) as_store_returns_quantitystdev,count(cs_quantity) as catalog_sales_quantitycount,avg(cs_quantity) as catalog_sales_quantityave
 from store_sales,store_returns,catalog_sales,date_dim d1,date_dim d2,date_dim d3,store,item
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
 --order by i_item_id,i_item_desc,s_state;
--q4
--union!!!!!
-- with year_total as (
--  select c_customer_id customer_id
--        ,c_first_name customer_first_name
--        ,c_last_name customer_last_name
--        ,c_preferred_cust_flag customer_preferred_cust_flag
--        ,c_birth_country customer_birth_country
--        ,c_login customer_login
--        ,c_email_address customer_email_address
--        ,d_year dyear
--        ,sum(((ss_ext_list_price-ss_ext_wholesale_cost-ss_ext_discount_amt)+ss_ext_sales_price)/2) year_total
--        ,'s' sale_type
--  from customer
--      ,store_sales
--      ,date_dim
--  where c_customer_sk = ss_customer_sk
--    and ss_sold_date_sk = d_date_sk
--  group by c_customer_id
--          ,c_first_name
--          ,c_last_name
--          ,c_preferred_cust_flag
--          ,c_birth_country
--          ,c_login
--          ,c_email_address
--          ,d_year
--  union all
--  select c_customer_id customer_id
--        ,c_first_name customer_first_name
--        ,c_last_name customer_last_name
--        ,c_preferred_cust_flag customer_preferred_cust_flag
--        ,c_birth_country customer_birth_country
--        ,c_login customer_login
--        ,c_email_address customer_email_address
--        ,d_year dyear
--        ,sum((((cs_ext_list_price-cs_ext_wholesale_cost-cs_ext_discount_amt)+cs_ext_sales_price)/2) ) year_total
--        ,'c' sale_type
--  from customer
--      ,catalog_sales
--      ,date_dim
--  where c_customer_sk = cs_bill_customer_sk
--    and cs_sold_date_sk = d_date_sk
--  group by c_customer_id
--          ,c_first_name
--          ,c_last_name
--          ,c_preferred_cust_flag
--          ,c_birth_country
--          ,c_login
--          ,c_email_address
--          ,d_year
-- union all
--  select c_customer_id customer_id
--        ,c_first_name customer_first_name
--        ,c_last_name customer_last_name
--        ,c_preferred_cust_flag customer_preferred_cust_flag
--        ,c_birth_country customer_birth_country
--        ,c_login customer_login
--        ,c_email_address customer_email_address
--        ,d_year dyear
--        ,sum((((ws_ext_list_price-ws_ext_wholesale_cost-ws_ext_discount_amt)+ws_ext_sales_price)/2) ) year_total
--        ,'w' sale_type
--  from customer
--      ,web_sales
--      ,date_dim
--  where c_customer_sk = ws_bill_customer_sk
--    and ws_sold_date_sk = d_date_sk
--  group by c_customer_id
--          ,c_first_name
--          ,c_last_name
--          ,c_preferred_cust_flag
--          ,c_birth_country
--          ,c_login
--          ,c_email_address
--          ,d_year
--          )
-- [_LIMITA]  select [_LIMITB] [SELECTCONE]
--  from year_total t_s_firstyear
--      ,year_total t_s_secyear
--      ,year_total t_c_firstyear
--      ,year_total t_c_secyear
--      ,year_total t_w_firstyear
--      ,year_total t_w_secyear
--  where t_s_secyear.customer_id = t_s_firstyear.customer_id
--    and t_s_firstyear.customer_id = t_c_secyear.customer_id
--    and t_s_firstyear.customer_id = t_c_firstyear.customer_id
--    and t_s_firstyear.customer_id = t_w_firstyear.customer_id
--    and t_s_firstyear.customer_id = t_w_secyear.customer_id
--    and t_s_firstyear.sale_type = 's'
--    and t_c_firstyear.sale_type = 'c'
--    and t_w_firstyear.sale_type = 'w'
--    and t_s_secyear.sale_type = 's'
--    and t_c_secyear.sale_type = 'c'
--    and t_w_secyear.sale_type = 'w'
--    and t_s_firstyear.dyear =  [YEAR]
--    and t_s_secyear.dyear = [YEAR]+1
--    and t_c_firstyear.dyear =  [YEAR]
--    and t_c_secyear.dyear =  [YEAR]+1
--    and t_w_firstyear.dyear = [YEAR]
--    and t_w_secyear.dyear = [YEAR]+1
--    and t_s_firstyear.year_total > 0
--    and t_c_firstyear.year_total > 0
--    and t_w_firstyear.year_total > 0
--    and case when t_c_firstyear.year_total > 0 then t_c_secyear.year_total / t_c_firstyear.year_total else null end
--            > case when t_s_firstyear.year_total > 0 then t_s_secyear.year_total / t_s_firstyear.year_total else null end
--    and case when t_c_firstyear.year_total > 0 then t_c_secyear.year_total / t_c_firstyear.year_total else null end
--            > case when t_w_firstyear.year_total > 0 then t_w_secyear.year_total / t_w_firstyear.year_total else null end
--  order by [SELECTCONE]
-- [_LIMITC];
--q99
CREATE VIEW QUERY99 AS
select w_warehouse_name,sm_type,cc_name,cs_ship_date_sk,cs_sold_date_sk
from catalog_sales,warehouse,ship_mode,call_center,date_dim
where d_month_seq between 1 and 12
and cs_ship_date_sk   = d_date_sk
and cs_warehouse_sk   = w_warehouse_sk
and cs_ship_mode_sk   = sm_ship_mode_sk
and cs_call_center_sk = cc_call_center_sk
group by w_warehouse_name,sm_type,cc_name;
--order by substr(w_warehouse_name,1,20),sm_type,cc_name;
--q5
--union!!!!
-- with ssr as
--  (select s_store_id,
--         sum(sales_price) as sales,
--         sum(profit) as profit,
--         sum(return_amt) as returns,
--         sum(net_loss) as profit_loss
--  from
--   ( select  ss_store_sk as store_sk,
--             ss_sold_date_sk  as date_sk,
--             ss_ext_sales_price as sales_price,
--             ss_net_profit as profit,
--             cast(0 as decimal(7,2)) as return_amt,
--             cast(0 as decimal(7,2)) as net_loss
--     from store_sales
--     union all
--     select sr_store_sk as store_sk,
--            sr_returned_date_sk as date_sk,
--            cast(0 as decimal(7,2)) as sales_price,
--            cast(0 as decimal(7,2)) as profit,
--            sr_return_amt as return_amt,
--            sr_net_loss as net_loss
--     from store_returns
--    ) salesreturns,
--      date_dim,
--      store
--  where date_sk = d_date_sk
--        and d_date between cast('[SALES_DATE]' as date)
--                   and (cast('[SALES_DATE]' as date) +  14 days)
--        and store_sk = s_store_sk
--  group by s_store_id)
--  ,
--  csr as
--  (select cp_catalog_page_id,
--         sum(sales_price) as sales,
--         sum(profit) as profit,
--         sum(return_amt) as returns,
--         sum(net_loss) as profit_loss
--  from
--   ( select  cs_catalog_page_sk as page_sk,
--             cs_sold_date_sk  as date_sk,
--             cs_ext_sales_price as sales_price,
--             cs_net_profit as profit,
--             cast(0 as decimal(7,2)) as return_amt,
--             cast(0 as decimal(7,2)) as net_loss
--     from catalog_sales
--     union all
--     select cr_catalog_page_sk as page_sk,
--            cr_returned_date_sk as date_sk,
--            cast(0 as decimal(7,2)) as sales_price,
--            cast(0 as decimal(7,2)) as profit,
--            cr_return_amount as return_amt,
--            cr_net_loss as net_loss
--     from catalog_returns
--    ) salesreturns,
--      date_dim,
--      catalog_page
--  where date_sk = d_date_sk
--        and d_date between cast('[SALES_DATE]' as date)
--                   and (cast('[SALES_DATE]' as date) +  14 days)
--        and page_sk = cp_catalog_page_sk
--  group by cp_catalog_page_id)
--  ,
--  wsr as
--  (select web_site_id,
--         sum(sales_price) as sales,
--         sum(profit) as profit,
--         sum(return_amt) as returns,
--         sum(net_loss) as profit_loss
--  from
--   ( select  ws_web_site_sk as wsr_web_site_sk,
--             ws_sold_date_sk  as date_sk,
--             ws_ext_sales_price as sales_price,
--             ws_net_profit as profit,
--             cast(0 as decimal(7,2)) as return_amt,
--             cast(0 as decimal(7,2)) as net_loss
--     from web_sales
--     union all
--     select ws_web_site_sk as wsr_web_site_sk,
--            wr_returned_date_sk as date_sk,
--            cast(0 as decimal(7,2)) as sales_price,
--            cast(0 as decimal(7,2)) as profit,
--            wr_return_amt as return_amt,
--            wr_net_loss as net_loss
--     from web_returns left outer join web_sales on
--          ( wr_item_sk = ws_item_sk
--            and wr_order_number = ws_order_number)
--    ) salesreturns,
--      date_dim,
--      web_site
--  where date_sk = d_date_sk
--        and d_date between cast('[SALES_DATE]' as date)
--                   and (cast('[SALES_DATE]' as date) +  14 days)
--        and wsr_web_site_sk = web_site_sk
--  group by web_site_id)
--  [_LIMITA] select [_LIMITB] channel
--         , id
--         , sum(sales) as sales
--         , sum(returns) as returns
--         , sum(profit) as profit
--  from
--  (select 'store channel' as channel
--         , 'store' || s_store_id as id
--         , sales
--         , returns
--         , (profit - profit_loss) as profit
--  from   ssr
--  union all
--  select 'catalog channel' as channel
--         , 'catalog_page' || cp_catalog_page_id as id
--         , sales
--         , returns
--         , (profit - profit_loss) as profit
--  from  csr
--  union all
--  select 'web channel' as channel
--         , 'web_site' || web_site_id as id
--         , sales
--         , returns
--         , (profit - profit_loss) as profit
--  from   wsr
--  ) x
--  group by rollup (channel, id)
--  order by channel
--          ,id
--  [_LIMITC];
--q66
--union
--  [_LIMITA] select [_LIMITB]
--          w_warehouse_name
--  	,w_warehouse_sq_ft
--  	,w_city
--  	,w_county
--  	,w_state
--  	,w_country
--         ,ship_carriers
--         ,year
--  	,sum(jan_sales) as jan_sales
--  	,sum(feb_sales) as feb_sales
--  	,sum(mar_sales) as mar_sales
--  	,sum(apr_sales) as apr_sales
--  	,sum(may_sales) as may_sales
--  	,sum(jun_sales) as jun_sales
--  	,sum(jul_sales) as jul_sales
--  	,sum(aug_sales) as aug_sales
--  	,sum(sep_sales) as sep_sales
--  	,sum(oct_sales) as oct_sales
--  	,sum(nov_sales) as nov_sales
--  	,sum(dec_sales) as dec_sales
--  	,sum(jan_sales/w_warehouse_sq_ft) as jan_sales_per_sq_foot
--  	,sum(feb_sales/w_warehouse_sq_ft) as feb_sales_per_sq_foot
--  	,sum(mar_sales/w_warehouse_sq_ft) as mar_sales_per_sq_foot
--  	,sum(apr_sales/w_warehouse_sq_ft) as apr_sales_per_sq_foot
--  	,sum(may_sales/w_warehouse_sq_ft) as may_sales_per_sq_foot
--  	,sum(jun_sales/w_warehouse_sq_ft) as jun_sales_per_sq_foot
--  	,sum(jul_sales/w_warehouse_sq_ft) as jul_sales_per_sq_foot
--  	,sum(aug_sales/w_warehouse_sq_ft) as aug_sales_per_sq_foot
--  	,sum(sep_sales/w_warehouse_sq_ft) as sep_sales_per_sq_foot
--  	,sum(oct_sales/w_warehouse_sq_ft) as oct_sales_per_sq_foot
--  	,sum(nov_sales/w_warehouse_sq_ft) as nov_sales_per_sq_foot
--  	,sum(dec_sales/w_warehouse_sq_ft) as dec_sales_per_sq_foot
--  	,sum(jan_net) as jan_net
--  	,sum(feb_net) as feb_net
--  	,sum(mar_net) as mar_net
--  	,sum(apr_net) as apr_net
--  	,sum(may_net) as may_net
--  	,sum(jun_net) as jun_net
--  	,sum(jul_net) as jul_net
--  	,sum(aug_net) as aug_net
--  	,sum(sep_net) as sep_net
--  	,sum(oct_net) as oct_net
--  	,sum(nov_net) as nov_net
--  	,sum(dec_net) as dec_net
--  from (
--     (select
--  	w_warehouse_name
--  	,w_warehouse_sq_ft
--  	,w_city
--  	,w_county
--  	,w_state
--  	,w_country
--  	,'[SMC.1]' || ',' || '[SMC.2]' as ship_carriers
--        ,d_year as year
--  	,sum(case when d_moy = 1
--  		then [SALESONE]* ws_quantity else 0 end) as jan_sales
--  	,sum(case when d_moy = 2
--  		then [SALESONE]* ws_quantity else 0 end) as feb_sales
--  	,sum(case when d_moy = 3
--  		then [SALESONE]* ws_quantity else 0 end) as mar_sales
--  	,sum(case when d_moy = 4
--  		then [SALESONE]* ws_quantity else 0 end) as apr_sales
--  	,sum(case when d_moy = 5
--  		then [SALESONE]* ws_quantity else 0 end) as may_sales
--  	,sum(case when d_moy = 6
--  		then [SALESONE]* ws_quantity else 0 end) as jun_sales
--  	,sum(case when d_moy = 7
--  		then [SALESONE]* ws_quantity else 0 end) as jul_sales
--  	,sum(case when d_moy = 8
--  		then [SALESONE]* ws_quantity else 0 end) as aug_sales
--  	,sum(case when d_moy = 9
--  		then [SALESONE]* ws_quantity else 0 end) as sep_sales
--  	,sum(case when d_moy = 10
--  		then [SALESONE]* ws_quantity else 0 end) as oct_sales
--  	,sum(case when d_moy = 11
--  		then [SALESONE]* ws_quantity else 0 end) as nov_sales
--  	,sum(case when d_moy = 12
--  		then [SALESONE]* ws_quantity else 0 end) as dec_sales
--  	,sum(case when d_moy = 1
--  		then [NETONE] * ws_quantity else 0 end) as jan_net
--  	,sum(case when d_moy = 2
--  		then [NETONE] * ws_quantity else 0 end) as feb_net
--  	,sum(case when d_moy = 3
--  		then [NETONE] * ws_quantity else 0 end) as mar_net
--  	,sum(case when d_moy = 4
--  		then [NETONE] * ws_quantity else 0 end) as apr_net
--  	,sum(case when d_moy = 5
--  		then [NETONE] * ws_quantity else 0 end) as may_net
--  	,sum(case when d_moy = 6
--  		then [NETONE] * ws_quantity else 0 end) as jun_net
--  	,sum(case when d_moy = 7
--  		then [NETONE] * ws_quantity else 0 end) as jul_net
--  	,sum(case when d_moy = 8
--  		then [NETONE] * ws_quantity else 0 end) as aug_net
--  	,sum(case when d_moy = 9
--  		then [NETONE] * ws_quantity else 0 end) as sep_net
--  	,sum(case when d_moy = 10
--  		then [NETONE] * ws_quantity else 0 end) as oct_net
--  	,sum(case when d_moy = 11
--  		then [NETONE] * ws_quantity else 0 end) as nov_net
--  	,sum(case when d_moy = 12
--  		then [NETONE] * ws_quantity else 0 end) as dec_net
--      from
--           web_sales
--          ,warehouse
--          ,date_dim
--          ,time_dim
--  	  ,ship_mode
--      where
--             ws_warehouse_sk =  w_warehouse_sk
--         and ws_sold_date_sk = d_date_sk
--         and ws_sold_time_sk = t_time_sk
--  	and ws_ship_mode_sk = sm_ship_mode_sk
--         and d_year = [YEAR]
--  	and t_time between [TIMEONE] and [TIMEONE]+28800
--  	and sm_carrier in ('[SMC.1]','[SMC.2]')
--      group by
--         w_warehouse_name
--  	,w_warehouse_sq_ft
--  	,w_city
--  	,w_county
--  	,w_state
--  	,w_country
--        ,d_year
--  	)
--  union all
--     (select
--  	w_warehouse_name
--  	,w_warehouse_sq_ft
--  	,w_city
--  	,w_county
--  	,w_state
--  	,w_country
--  	,'[SMC.1]' || ',' || '[SMC.2]' as ship_carriers
--        ,d_year as year
--  	,sum(case when d_moy = 1
--  		then [SALESTWO]* cs_quantity else 0 end) as jan_sales
--  	,sum(case when d_moy = 2
--  		then [SALESTWO]* cs_quantity else 0 end) as feb_sales
--  	,sum(case when d_moy = 3
--  		then [SALESTWO]* cs_quantity else 0 end) as mar_sales
--  	,sum(case when d_moy = 4
--  		then [SALESTWO]* cs_quantity else 0 end) as apr_sales
--  	,sum(case when d_moy = 5
--  		then [SALESTWO]* cs_quantity else 0 end) as may_sales
--  	,sum(case when d_moy = 6
--  		then [SALESTWO]* cs_quantity else 0 end) as jun_sales
--  	,sum(case when d_moy = 7
--  		then [SALESTWO]* cs_quantity else 0 end) as jul_sales
--  	,sum(case when d_moy = 8
--  		then [SALESTWO]* cs_quantity else 0 end) as aug_sales
--  	,sum(case when d_moy = 9
--  		then [SALESTWO]* cs_quantity else 0 end) as sep_sales
--  	,sum(case when d_moy = 10
--  		then [SALESTWO]* cs_quantity else 0 end) as oct_sales
--  	,sum(case when d_moy = 11
--  		then [SALESTWO]* cs_quantity else 0 end) as nov_sales
--  	,sum(case when d_moy = 12
--  		then [SALESTWO]* cs_quantity else 0 end) as dec_sales
--  	,sum(case when d_moy = 1
--  		then [NETTWO] * cs_quantity else 0 end) as jan_net
--  	,sum(case when d_moy = 2
--  		then [NETTWO] * cs_quantity else 0 end) as feb_net
--  	,sum(case when d_moy = 3
--  		then [NETTWO] * cs_quantity else 0 end) as mar_net
--  	,sum(case when d_moy = 4
--  		then [NETTWO] * cs_quantity else 0 end) as apr_net
--  	,sum(case when d_moy = 5
--  		then [NETTWO] * cs_quantity else 0 end) as may_net
--  	,sum(case when d_moy = 6
--  		then [NETTWO] * cs_quantity else 0 end) as jun_net
--  	,sum(case when d_moy = 7
--  		then [NETTWO] * cs_quantity else 0 end) as jul_net
--  	,sum(case when d_moy = 8
--  		then [NETTWO] * cs_quantity else 0 end) as aug_net
--  	,sum(case when d_moy = 9
--  		then [NETTWO] * cs_quantity else 0 end) as sep_net
--  	,sum(case when d_moy = 10
--  		then [NETTWo] * cs_quantity else 0 end) as oct_net
--  	,sum(case when d_moy = 11
--  		then [NETTWO] * cs_quantity else 0 end) as nov_net
--  	,sum(case when d_moy = 12
--  		then [NETTWO] * cs_quantity else 0 end) as dec_net
--      from
--           catalog_sales
--          ,warehouse
--          ,date_dim
--          ,time_dim
--  	 ,ship_mode
--      where
--             cs_warehouse_sk =  w_warehouse_sk
--         and cs_sold_date_sk = d_date_sk
--         and cs_sold_time_sk = t_time_sk
--  	and cs_ship_mode_sk = sm_ship_mode_sk
--         and d_year = [YEAR]
--  	and t_time between [TIMEONE] AND [TIMEONE]+28800
--  	and sm_carrier in ('[SMC.1]','[SMC.2]')
--      group by
--         w_warehouse_name
--  	,w_warehouse_sq_ft
--  	,w_city
--  	,w_county
--  	,w_state
--  	,w_country
--        ,d_year
--      )
--  ) x
--  group by
--         w_warehouse_name
--  	,w_warehouse_sq_ft
--  	,w_city
--  	,w_county
--  	,w_state
--  	,w_country
--  	,ship_carriers
--        ,year
--  order by w_warehouse_name
--  [_LIMITC];
--
--q11
--union
-- with year_total as (
--  select c_customer_id customer_id
--        ,c_first_name customer_first_name
--        ,c_last_name customer_last_name
--        ,c_preferred_cust_flag
--        ,c_birth_country customer_birth_country
--        ,c_login customer_login
--        ,c_email_address customer_email_address
--        ,d_year dyear
--        ,sum(ss_ext_list_price-ss_ext_discount_amt) year_total
--        ,'s' sale_type
--  from customer
--      ,store_sales
--      ,date_dim
--  where c_customer_sk = ss_customer_sk
--    and ss_sold_date_sk = d_date_sk
--  group by c_customer_id
--          ,c_first_name
--          ,c_last_name
--          ,d_year
--          ,c_preferred_cust_flag
--          ,c_birth_country
--          ,c_login
--          ,c_email_address
--          ,d_year
--  union all
--  select c_customer_id customer_id
--        ,c_first_name customer_first_name
--        ,c_last_name customer_last_name
--        ,c_preferred_cust_flag
--        ,c_birth_country customer_birth_country
--        ,c_login customer_login
--        ,c_email_address customer_email_address
--        ,d_year dyear
--        ,sum(ws_ext_list_price-ws_ext_discount_amt) year_total
--        ,'w' sale_type
--  from customer
--      ,web_sales
--      ,date_dim
--  where c_customer_sk = ws_bill_customer_sk
--    and ws_sold_date_sk = d_date_sk
--  group by c_customer_id
--          ,c_first_name
--          ,c_last_name
--          ,c_preferred_cust_flag
--          ,c_birth_country
--          ,c_login
--          ,c_email_address
--          ,d_year
--          )
--  [_LIMITA] select [_LIMITB] [SELECTCONE]
--  from year_total t_s_firstyear
--      ,year_total t_s_secyear
--      ,year_total t_w_firstyear
--      ,year_total t_w_secyear
--  where t_s_secyear.customer_id = t_s_firstyear.customer_id
--          and t_s_firstyear.customer_id = t_w_secyear.customer_id
--          and t_s_firstyear.customer_id = t_w_firstyear.customer_id
--          and t_s_firstyear.sale_type = 's'
--          and t_w_firstyear.sale_type = 'w'
--          and t_s_secyear.sale_type = 's'
--          and t_w_secyear.sale_type = 'w'
--          and t_s_firstyear.dyear = [YEAR]
--          and t_s_secyear.dyear = [YEAR]+1
--          and t_w_firstyear.dyear = [YEAR]
--          and t_w_secyear.dyear = [YEAR]+1
--          and t_s_firstyear.year_total > 0
--          and t_w_firstyear.year_total > 0
--          and case when t_w_firstyear.year_total > 0 then t_w_secyear.year_total / t_w_firstyear.year_total else null end
--              > case when t_s_firstyear.year_total > 0 then t_s_secyear.year_total / t_s_firstyear.year_total else null end
--  order by [SELECTCONE]
-- [_LIMITC];
--q31
-- create view ssq31 as
--  select ca_county,d_qoy, d_year,sum(ss_ext_sales_price) as store_sales
--  from store_sales,date_dim,customer_address
--  where ss_sold_date_sk = d_date_sk
--   and ss_addr_sk=ca_address_sk
--  group by ca_county,d_qoy, d_year;
-- 
-- create view wsq31 as
--  select ca_county,d_qoy, d_year,sum(ws_ext_sales_price) as web_sales
--  from web_sales,date_dim,customer_address
--  where ws_sold_date_sk = d_date_sk
--   and ws_bill_addr_sk=ca_address_sk
--  group by ca_county,d_qoy, d_year;
-- CREATE VIEW QUERY31 AS
--  select ss1.ca_county,ss1.d_year,ws2.web_sales/ws1.web_sales web_q1_q2_increase,ss2.store_sales/ss1.store_sales store_q1_q2_increase,ws3.web_sales/ws2.web_sales web_q2_q3_increase,ss3.store_sales/ss2.store_sales store_q2_q3_increase
--  from ssq31 ss1,ssq31 ss2,ssq31 ss3,wsq31 ws1,wsq31 ws2,wsq31 ws3
--  where
--     ss1.d_qoy = 1
--     and ss1.d_year = 2000
--     and ss1.ca_county = ss2.ca_county
--     and ss2.d_qoy = 2
--     and ss2.d_year = 2000
--  and ss2.ca_county = ss3.ca_county
--     and ss3.d_qoy = 3
--     and ss3.d_year = 2000
--     and ss1.ca_county = ws1.ca_county
--     and ws1.d_qoy = 1
--     and ws1.d_year = 2000
--     and ws1.ca_county = ws2.ca_county
--     and ws2.d_qoy = 2
--     and ws2.d_year = 2000
--     and ws1.ca_county = ws3.ca_county
--     and ws3.d_qoy = 3
--     and ws3.d_year =2000
--     and ws2.web_sales/ws1.web_sales > ss2.store_sales/ss1.store_sales and ws3.web_sales/ws2.web_sales > ss3.store_sales/ss2.store_sales;
-- order by [AGG];
--q74
--union!!!!
--  create view year_total as (
--  select c_customer_id customer_id
--        ,c_first_name customer_first_name
--        ,c_last_name customer_last_name
--        ,d_year as year
--        ,[AGGONE](ss_net_paid) year_total
--        ,'s' sale_type
--  from customer
--      ,store_sales
--      ,date_dim
--  where c_customer_sk = ss_customer_sk
--    and ss_sold_date_sk = d_date_sk
--    and d_year in ([YEAR],[YEAR]+1)
--  group by c_customer_id
--          ,c_first_name
--          ,c_last_name
--          ,d_year
--  union all
--  select c_customer_id customer_id
--        ,c_first_name customer_first_name
--        ,c_last_name customer_last_name
--        ,d_year as year
--        ,[AGGONE](ws_net_paid) year_total
--        ,'w' sale_type
--  from customer
--      ,web_sales
--      ,date_dim
--  where c_customer_sk = ws_bill_customer_sk
--    and ws_sold_date_sk = d_date_sk
--    and d_year in ([YEAR],[YEAR]+1)
--  group by c_customer_id
--          ,c_first_name
--          ,c_last_name
--          ,d_year
--          )
--  [_LIMITA] select [_LIMITB]
--         t_s_secyear.customer_id, t_s_secyear.customer_first_name, t_s_secyear.customer_last_name
--  from year_total t_s_firstyear
--      ,year_total t_s_secyear
--      ,year_total t_w_firstyear
--      ,year_total t_w_secyear
--  where t_s_secyear.customer_id = t_s_firstyear.customer_id
--          and t_s_firstyear.customer_id = t_w_secyear.customer_id
--          and t_s_firstyear.customer_id = t_w_firstyear.customer_id
--          and t_s_firstyear.sale_type = 's'
--          and t_w_firstyear.sale_type = 'w'
--          and t_s_secyear.sale_type = 's'
--          and t_w_secyear.sale_type = 'w'
--          and t_s_firstyear.year = [YEAR]
--          and t_s_secyear.year = [YEAR]+1
--          and t_w_firstyear.year = [YEAR]
--          and t_w_secyear.year = [YEAR]+1
--          and t_s_firstyear.year_total > 0
--          and t_w_firstyear.year_total > 0
--          and case when t_w_firstyear.year_total > 0 then t_w_secyear.year_total / t_w_firstyear.year_total else null end
--            > case when t_s_firstyear.year_total > 0 then t_s_secyear.year_total / t_s_firstyear.year_total else null end
--  order by [ORDERC.1],[ORDERC.2],[ORDERC.3]
-- [_LIMITC];
--
--q97
-- create view ssci as
-- select ss_customer_sk as customer_sk,ss_item_sk as item_sk
-- from store_sales,date_dim
-- where ss_sold_date_sk = d_date_sk and d_month_seq between 1 and 12
-- group by ss_customer_sk,ss_item_sk;
-- 
-- create view csci as
-- select cs_bill_customer_sk as customer_sk,cs_item_sk as item_sk
-- from catalog_sales,date_dim
-- where cs_sold_date_sk = d_date_sk and d_month_seq between 1 and 12
-- group by cs_bill_customer_sk,cs_item_sk;
-- CREATE VIEW QUERY97 AS
-- select ssci.customer_sk,csci.customer_sk
-- from ssci,csci where ssci.customer_sk=csci.customer_sk and ssci.item_sk = csci.item_sk;
--q95
-- create view ws_wh as
-- select ws1.ws_order_number,ws1.ws_warehouse_sk wh1,ws2.ws_warehouse_sk wh2
--  from web_sales ws1,web_sales ws2
--  where ws1.ws_order_number = ws2.ws_order_number
--    and ws1.ws_warehouse_sk <> ws2.ws_warehouse_sk;
-- CREATE VIEW QUERY95 AS
-- select count(ws_order_number) as order_count,sum(ws_ext_ship_cost) as total_shipping_cost,sum(ws_net_profit) as total_net_profit
-- from web_sales ws1,date_dim,customer_address,web_site
-- where d_date between '2000-01-01' and '2000-03-01'
-- and ws1.ws_ship_date_sk = d_date_sk
-- and ws1.ws_ship_addr_sk = ca_address_sk
-- and ca_state = 'STATE'
-- and ws1.ws_web_site_sk = web_site_sk
-- and web_company_name = 'pri'
-- and ws1.ws_order_number in (select ws_order_number from ws_wh)
-- and ws1.ws_order_number in (select wr_order_number
--                             from web_returns,ws_wh
--                             where wr_order_number = ws_wh.ws_order_number);
--order by count(distinct ws_order_number);
--q94
-- CREATE VIEW QUERY94 AS
-- select count(ws_order_number) as order_count,sum(ws_ext_ship_cost) as total_shipping_cost,sum(ws_net_profit) as total_net_profit
-- from web_sales ws1,date_dim,customer_address,web_site
-- where d_date between '2000-01-01' and '2000-03-01'
-- and ws1.ws_ship_date_sk = d_date_sk
-- and ws1.ws_ship_addr_sk = ca_address_sk
-- and ca_state = 'STATE'
-- and ws1.ws_web_site_sk = web_site_sk
-- and web_company_name = 'pri'
-- and exists (select *
--             from web_sales ws2
--             where ws1.ws_order_number = ws2.ws_order_number
--               and ws1.ws_warehouse_sk <> ws2.ws_warehouse_sk)
-- and not exists(select *
--                from web_returns wr1
--                where ws1.ws_order_number = wr1.wr_order_number);
--order by count(distinct ws_order_number)
--q92
-- create view q92view as
-- SELECT avg(ws_ext_discount_amt) as average
-- FROM web_sales, date_dim
-- WHERE d_date_sk = ws_sold_date_sk and d_date between '2000-01-01' and '2000-04-01';
-- CREATE VIEW QUERY92 AS
-- select sum(ws_ext_discount_amt) as Excess_Discount_Amount
-- from web_sales,item,date_dim
-- where i_manufact_id = 12
--   and i_item_sk = ws_item_sk
--   and d_date between '2000-01-01' and '2000-04-01'
--   and d_date_sk = ws_sold_date_sk
--   and ws_item_sk = i_item_sk
--   and ws_ext_discount_amt > (select average from q92view);
-- --order by sum(ws_ext_discount_amt);
--q90
-- create view atq90 as
-- select count(*) as amc
-- from web_sales, household_demographics , time_dim, web_page
-- where ws_sold_time_sk = time_dim.t_time_sk
--   and ws_ship_hdemo_sk = household_demographics.hd_demo_sk
--   and ws_web_page_sk = web_page.wp_web_page_sk
--   and time_dim.t_hour between 8 and 9
--   and household_demographics.hd_dep_count = 10000
--   and web_page.wp_char_count between 5000 and 5200;
-- 
-- create view ptq90 as
-- select count(*) as pmc
-- from web_sales, household_demographics , time_dim, web_page
-- where ws_sold_time_sk = time_dim.t_time_sk
--   and ws_ship_hdemo_sk = household_demographics.hd_demo_sk
--   and ws_web_page_sk = web_page.wp_web_page_sk
--   and time_dim.t_hour between 20 and 21
--   and household_demographics.hd_dep_count = 10000
--   and web_page.wp_char_count between 5000 and 5200;
-- CREATE VIEW QUERY90 AS
--  select amc, pmc
--  from atq90,ptq90;
 --order by am_pm_ratio;
--q86
-- CREATE VIEW QUERY86 AS
--  select sum(ws_net_paid) as total_sum,i_category,i_class
--  from wsid
--  where d_month_seq between 1 and 12
--  group by (i_category,i_class);
--q85
-- CREATE VIEW QUERY85 AS
--  select r_reason_desc,avg(ws_quantity) wsqavg,avg(wr_refunded_cash) wrrcavg,avg(wr_fee) wrfavg
--  from web_sales, web_returns, web_page, customer_demographics cd1,customer_demographics cd2, customer_address, date_dim, reason
--  where ws_web_page_sk = wp_web_page_sk
--    and ws_item_sk = wr_item_sk
--    and ws_order_number = wr_order_number
--    and ws_sold_date_sk = d_date_sk and d_year = 2000
--    and cd1.cd_demo_sk = wr_refunded_cdemo_sk
--    and cd2.cd_demo_sk = wr_returning_cdemo_sk
--    and ca_address_sk = wr_refunded_addr_sk
--    and r_reason_sk = wr_reason_sk
--    and
--    (
--     (
--      cd1.cd_marital_status = 'MS1'
--      and
--      cd1.cd_marital_status = cd2.cd_marital_status
--      and
--      cd1.cd_education_status = 'ES1'
--      and
--      cd1.cd_education_status = cd2.cd_education_status
--      and
--      ws_sales_price between 100.00 and 150.00
--     )
--    or
--     (
--      cd1.cd_marital_status = 'MS2'
--      and
--      cd1.cd_marital_status = cd2.cd_marital_status
--      and
--      cd1.cd_education_status = 'ES2'
--      and
--      cd1.cd_education_status = cd2.cd_education_status
--      and
--      ws_sales_price between 50.00 and 100.00
--     )
--    or
--     (
--      cd1.cd_marital_status = 'MS3'
--      and
--      cd1.cd_marital_status = cd2.cd_marital_status
--      and
--      cd1.cd_education_status = 'ES3'
--      and
--      cd1.cd_education_status = cd2.cd_education_status
--      and
--      ws_sales_price between 150.00 and 200.00
--     )
--    )
--    and
--    (
--     (
--      ca_country = 'United States'
--      and
--      ca_state in ('STATE1', 'STATE2', 'STATE3')
--      and ws_net_profit between 100 and 200
--     )
--     or
--     (
--      ca_country = 'United States'
--      and
--      ca_state in ('STATE4', 'STATE5', 'STATE6')
--      and ws_net_profit between 150 and 300
--     )
--     or
--     (
--      ca_country = 'United States'
--      and
--      ca_state in ('STATE7', 'STATE8', 'STATE9')
--      and ws_net_profit between 50 and 250
--     )
--    )
-- group by r_reason_desc;
--order by substr(r_reason_desc,1,20),avg(ws_quantity),avg(wr_refunded_cash),avg(wr_fee);
--q76
--union
-- select channel, col_name, d_year, d_qoy, i_category, COUNT(*) sales_cnt, SUM(ext_sales_price) sales_amt FROM (
--         SELECT 'store' as channel, ss_customer_sk col_name, d_year, d_qoy, i_category, ss_ext_sales_price ext_sales_price
--          FROM store_sales, item, date_dim
--          WHERE ss_customer_sk IS NULL
--            AND ss_sold_date_sk=d_date_sk
--            AND ss_item_sk=i_item_sk
--         UNION ALL
--         SELECT 'web' as channel, ws_bill_customer_sk col_name, d_year, d_qoy, i_category, ws_ext_sales_price ext_sales_price
--          FROM web_sales, item, date_dim
--          WHERE ws_bill_customer_sk IS NULL
--            AND ws_sold_date_sk=d_date_sk
--            AND ws_item_sk=i_item_sk
--         UNION ALL
--         SELECT 'catalog' as channel, cs_bill_customer_sk col_name, d_year, d_qoy, i_category, cs_ext_sales_price ext_sales_price
--          FROM catalog_sales, item, date_dim
--          WHERE cs_bill_customer_sk IS NULL
--            AND cs_sold_date_sk=d_date_sk
--            AND cs_item_sk=i_item_sk) foo
-- GROUP BY channel, col_name, d_year, d_qoy, i_category;
-- --ORDER BY channel, col_name, d_year, d_qoy, i_category;
--q62
-- CREATE VIEW QUERY62 AS
-- select w_warehouse_name,sm_type,web_name,ws_ship_date_sk,ws_sold_date_sk
-- from web_sales,warehouse,ship_mode,web_site,date_dim
-- where d_month_seq between 1 and 12
-- and ws_ship_date_sk   = d_date_sk
-- and ws_warehouse_sk   = w_warehouse_sk
-- and ws_ship_mode_sk   = sm_ship_mode_sk
-- and ws_web_site_sk    = web_site_sk
-- group by w_warehouse_name,sm_type,web_name;
--order by substr(w_warehouse_name,1,20),sm_type,web_name;
--q45
--I don't know!!!!!
-- create view q45view as
-- select i_item_id
-- from item
-- where i_item_sk in (2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
-- CREATE VIEW QUERY45 AS
--  select ca_zip, ca_city, sum(ws_sales_price) wsspsum
--  from v_customer, wsid
--  where ws_bill_customer_sk = c_customer_sk
--  	and ( ca_zip in ('85669', '86197','88274','83405','86475', '85392', '85460', '80348', '81792') or  i_item_id in (select i_item_id from q45view) )
--  	and d_qoy = 1 and d_year = 2000
--  group by ca_zip, ca_city;
-- -- order by ca_zip, [GBOBC];
-- --q12
-- CREATE VIEW QUERY12 AS
-- select i_item_desc,i_category,i_class,i_current_price,sum(ws_ext_sales_price) as itemrevenue,sum(ws_ext_sales_price) wsespsum
-- from wsid
-- where i_category in ('CATEGORY1', 'CATEGORY2', 'CATEGORY3') and d_date between '2000-01-01' and '2000-01-31'
-- group by i_item_id,i_item_desc,i_category,i_class,i_current_price;
--order by i_category,i_class,i_item_id,i_item_desc,revenueratio;
--q87
-- except
-- select count(*)
-- from ((select c_last_name, c_first_name, d_date
--        from store_sales, date_dim, customer
--        where store_sales.ss_sold_date_sk = date_dim.d_date_sk
--          and store_sales.ss_customer_sk = customer.c_customer_sk
--          and d_month_seq between 1 and 12)
--        except
--       (select c_last_name, c_first_name, d_date
--        from catalog_sales, date_dim, customer
--        where catalog_sales.cs_sold_date_sk = date_dim.d_date_sk
--          and catalog_sales.cs_bill_customer_sk = customer.c_customer_sk
--          and d_month_seq between 1 and 12)
--        except
--       (select c_last_name, c_first_name, d_date
--        from web_sales, date_dim, customer
--        where web_sales.ws_sold_date_sk = date_dim.d_date_sk
--          and web_sales.ws_bill_customer_sk = customer.c_customer_sk
--          and d_month_seq between 1 and 12)
-- ) cool_cust;
--q80
--union
-- with ssr as
--  (select  s_store_id as store_id,
--           sum(ss_ext_sales_price) as sales,
--           sum(coalesce(sr_return_amt, 0)) as returns,
--           sum(ss_net_profit - coalesce(sr_net_loss, 0)) as profit
--   from store_sales left outer join store_returns on
--          (ss_item_sk = sr_item_sk and ss_ticket_number = sr_ticket_number),
--      date_dim,
--      store,
--      item,
--      promotion
--  where ss_sold_date_sk = d_date_sk
--        and d_date between cast('[SALES_DATE]' as date)
--                   and (cast('[SALES_DATE]' as date) +  30 days)
--        and ss_store_sk = s_store_sk
--        and ss_item_sk = i_item_sk
--        and i_current_price > 50
--        and ss_promo_sk = p_promo_sk
--        and p_channel_tv = 'N'
--  group by s_store_id)
--  ,
--  csr as
--  (select  cp_catalog_page_id as catalog_page_id,
--           sum(cs_ext_sales_price) as sales,
--           sum(coalesce(cr_return_amount, 0)) as returns,
--           sum(cs_net_profit - coalesce(cr_net_loss, 0)) as profit
--   from catalog_sales left outer join catalog_returns on
--          (cs_item_sk = cr_item_sk and cs_order_number = cr_order_number),
--      date_dim,
--      catalog_page,
--      item,
--      promotion
--  where cs_sold_date_sk = d_date_sk
--        and d_date between cast('[SALES_DATE]' as date)
--                   and (cast('[SALES_DATE]' as date) +  30 days)
--         and cs_catalog_page_sk = cp_catalog_page_sk
--        and cs_item_sk = i_item_sk
--        and i_current_price > 50
--        and cs_promo_sk = p_promo_sk
--        and p_channel_tv = 'N'
-- group by cp_catalog_page_id)
--  ,
--  wsr as
--  (select  web_site_id,
--           sum(ws_ext_sales_price) as sales,
--           sum(coalesce(wr_return_amt, 0)) as returns,
--           sum(ws_net_profit - coalesce(wr_net_loss, 0)) as profit
--   from web_sales left outer join web_returns on
--          (ws_item_sk = wr_item_sk and ws_order_number = wr_order_number),
--      date_dim,
--      web_site,
--      item,
--      promotion
--  where ws_sold_date_sk = d_date_sk
--        and d_date between cast('[SALES_DATE]' as date)
--                   and (cast('[SALES_DATE]' as date) +  30 days)
--         and ws_web_site_sk = web_site_sk
--        and ws_item_sk = i_item_sk
--        and i_current_price > 50
--        and ws_promo_sk = p_promo_sk
--        and p_channel_tv = 'N'
-- group by web_site_id)
--  [_LIMITA] select [_LIMITB] channel
--         , id
--         , sum(sales) as sales
--         , sum(returns) as returns
--         , sum(profit) as profit
--  from
--  (select 'store channel' as channel
--         , 'store' || store_id as id
--         , sales
--         , returns
--         , profit
--  from   ssr
--  union all
--  select 'catalog channel' as channel
--         , 'catalog_page' || catalog_page_id as id
--         , sales
--         , returns
--         , profit
--  from  csr
--  union all
--  select 'web channel' as channel
--         , 'web_site' || web_site_id as id
--         , sales
--         , returns
--         , profit
--  from   wsr
--  ) x
--  group by rollup (channel, id)
--  order by channel
--          ,id
--  [_LIMITC];
-- --q78
-- create view wsq78 as
--   select d_year AS ws_sold_year, ws_item_sk,ws_bill_customer_sk ws_customer_sk,sum(ws_quantity) ws_qty,sum(ws_wholesale_cost) ws_wc,sum(ws_sales_price) ws_sp
--   from date_dim,web_sales,web_returns
--   where wr_order_number=ws_order_number and ws_item_sk=wr_item_sk and ws_sold_date_sk = d_date_sk and wr_order_number is null
--   group by d_year, ws_item_sk, ws_bill_customer_sk;
-- 
-- create view csq78 as
--   select d_year AS cs_sold_year, cs_item_sk,cs_bill_customer_sk cs_customer_sk,sum(cs_quantity) cs_qty,sum(cs_wholesale_cost) cs_wc,sum(cs_sales_price) cs_sp
--   from date_dim,catalog_sales,catalog_returns
--   where cr_order_number=cs_order_number and cs_item_sk=cr_item_sk and cs_sold_date_sk = d_date_sk and cr_order_number is null
--   group by d_year, cs_item_sk, cs_bill_customer_sk;
-- 
-- create view ssq78 as
--   select d_year AS ss_sold_year, ss_item_sk,ss_customer_sk,sum(ss_quantity) ss_qty,sum(ss_wholesale_cost) ss_wc,sum(ss_sales_price) ss_sp
--   from date_dim,store_sales,store_returns
--   where sr_ticket_number=ss_ticket_number and ss_item_sk=sr_item_sk and ss_sold_date_sk = d_date_sk and sr_ticket_number is null
--   group by d_year, ss_item_sk, ss_customer_sk;
-- 
-- CREATE VIEW QUERY78 AS
-- select ss_sold_year, ss_qty,ws_qty,cs_qty, ss_qty store_qty, ss_wc store_wholesale_cost, ss_sp store_sales_price,cs_qty,ws_wc,cs_wc,ws_sp,cs_sp
-- from csq78,ssq78,wsq78
-- where (ws_sold_year=ss_sold_year and ws_item_sk=ss_item_sk and ws_customer_sk=ss_customer_sk) and (cs_sold_year=ss_sold_year and cs_item_sk=cs_item_sk and cs_customer_sk=ss_customer_sk) and ws_qty>0 and cs_qty>0 and ss_sold_year=2000;
--q77
--union
--  create view ssq77 as
--  select s_store_sk,sum(ss_ext_sales_price) as sales,sum(ss_net_profit) as profit
--  from store_sales,date_dim,store
--  where ss_sold_date_sk = d_date_sk and d_date between '2000-08-01' and '2000-08-31' and ss_store_sk = s_store_sk
--  group by s_store_sk;
--
--  create view srq77 as
--  select s_store_sk,sum(sr_return_amt) as returns,sum(sr_net_loss) as profit_loss
--  from store_returns,date_dim,store
--  where sr_returned_date_sk = d_date_sk and d_date between '2000-08-01' and '2000-08-31' and sr_store_sk = s_store_sk
--  group by s_store_sk;
--
--  create view csq77 as
--  select cs_call_center_sk,sum(cs_ext_sales_price) as sales,sum(cs_net_profit) as profit
--  from catalog_sales,date_dim
--  where cs_sold_date_sk = d_date_sk and d_date between '2000-08-01' and '2000-08-31'
--  group by cs_call_center_sk;
--
--  create view crq77 as
--  select sum(cr_return_amount) as returns,sum(cr_net_loss) as profit_loss
--  from catalog_returns,date_dim
--  where cr_returned_date_sk = d_date_sk and d_date between '2000-08-01' and '2000-08-31';
--
--  create view wsq77 as
--  select wp_web_page_sk,sum(ws_ext_sales_price) as sales,sum(ws_net_profit) as profit
--  from web_sales,date_dim,web_page
--  where ws_sold_date_sk = d_date_sk and d_date between '2000-08-01' and '2000-08-31' and ws_web_page_sk = wp_web_page_sk
--  group by wp_web_page_sk;
--
--  create view wrq77 as
--  select wp_web_page_sk,sum(wr_return_amt) as returns,sum(wr_net_loss) as profit_loss
--  from web_returns,date_dim,web_page
--  where wr_returned_date_sk = d_date_sk and d_date between '2000-08-01' and '2000-08-31' and wr_web_page_sk = wp_web_page_sk
--  group by wp_web_page_sk;
--
--  select channel, id, sum(sales) as sales, sum(returns) as returns, sum(profit) as profit
--  from
--  (select 'store channel' as channel
--         , ss.s_store_sk as id
--         , sales
--         , coalesce(returns, 0) as returns
--         , (profit - coalesce(profit_loss,0)) as profit
--  from   ss left join sr
--         on  ss.s_store_sk = sr.s_store_sk
--  union all
--  select 'catalog channel' as channel
--         , cs_call_center_sk as id
--         , sales
--         , returns
--         , (profit - profit_loss) as profit
--  from  cs
--        , cr
--  union all
--  select 'web channel' as channel
--         , ws.wp_web_page_sk as id
--         , sales
--         , coalesce(returns, 0) returns
--         , (profit - coalesce(profit_loss,0)) as profit
--  from   ws left join wr
--         on  ws.wp_web_page_sk = wr.wp_web_page_sk
--  ) x
--  group by rollup (channel, id);
-- -- order by channel,id;
--q71
-- union
--  select i_brand_id brand_id, i_brand brand,t_hour,t_minute, sum(ext_price) ext_price
--  from item, (select ws_ext_sales_price as ext_price,
--                         ws_sold_date_sk as sold_date_sk,
--                         ws_item_sk as sold_item_sk,
--                         ws_sold_time_sk as time_sk
--                  from web_sales,date_dim
--                  where d_date_sk = ws_sold_date_sk
--                    and d_moy=[MONTH]
--                    and d_year=[YEAR]
--                  union all
--                  select cs_ext_sales_price as ext_price,
--                         cs_sold_date_sk as sold_date_sk,
--                         cs_item_sk as sold_item_sk,
--                         cs_sold_time_sk as time_sk
--                  from catalog_sales,date_dim
--                  where d_date_sk = cs_sold_date_sk
--                    and d_moy=[MONTH]
--                    and d_year=[YEAR]
--                  union all
--                  select ss_ext_sales_price as ext_price,
--                         ss_sold_date_sk as sold_date_sk,
--                         ss_item_sk as sold_item_sk,
--                         ss_sold_time_sk as time_sk
--                  from store_sales,date_dim
--                  where d_date_sk = ss_sold_date_sk
--                    and d_moy=[MONTH]
--                    and d_year=[YEAR]
--                  ) as tmp,time_dim
--  where
--    sold_item_sk = i_item_sk
--    and i_manager_id=1
--    and time_sk = t_time_sk
--    and (t_meal_time = 'breakfast' or t_meal_time = 'dinner')
--  group by i_brand, i_brand_id,t_hour,t_minute
-- --  order by ext_price desc, i_brand_id;
--q60
--union
-- create view itemq60 as
-- select i_item_id
-- from item
-- where i_category in ('CATEGORY');
--
-- create view ssq60 as
--  select i_item_id,sum(ss_ext_sales_price) total_sales
--  from store_sales,date_dim,customer_address,item
--  where i_item_id in (select * from itemq60)
--     and     ss_item_sk              = i_item_sk
--     and     ss_sold_date_sk         = d_date_sk
--     and     d_year                  = 2000
--     and     d_moy                   = 8
--     and     ss_addr_sk              = ca_address_sk
--     and     ca_gmt_offset           = 02
--     group by i_item_id;
--
-- create view csq60 as
--   select i_item_id,sum(cs_ext_sales_price) total_sales
--   from catalog_sales,date_dim,customer_address,item
--   where i_item_id in (select * from itemq60)
--     and     cs_item_sk              = i_item_sk
--     and     cs_sold_date_sk         = d_date_sk
--     and     d_year                  =2000
--     and     d_moy                   = 8
--     and     cs_bill_addr_sk         = ca_address_sk
--     and     ca_gmt_offset           = 02
--     group by i_item_id;
--
-- create view wsq60 as
--   select i_item_id,sum(ws_ext_sales_price) total_sales
--   from web_sales,date_dim,customer_address,item
--   where i_item_id in (select * from itemq60)
--     and     ws_item_sk              = i_item_sk
--     and     ws_sold_date_sk         = d_date_sk
--     and     d_year                  = 2000
--     and     d_moy                   = 8
--     and     ws_bill_addr_sk         = ca_address_sk
--     and     ca_gmt_offset           = 02
--     group by i_item_id;
--
--
--  select i_item_id,sum(total_sales) total_sales
--  from  (select * from ssq60
--         union all
--         select * from csq60
--         union all
--         select * from wsq60) tmp1
--  group by i_item_id;
--  --order by i_item_id,total_sales;
--q56
--union
--  with ss as (
--  select i_item_id,sum(ss_ext_sales_price) total_sales
--  from
--  	store_sales,
--  	date_dim,
--          customer_address,
--          item
--  where i_item_id in (select
--      i_item_id
-- from item
-- where i_color in ('[COLOR.1]','[COLOR.2]','[COLOR.3]'))
--  and     ss_item_sk              = i_item_sk
--  and     ss_sold_date_sk         = d_date_sk
--  and     d_year                  = [YEAR]
--  and     d_moy                   = [MONTH]
--  and     ss_addr_sk              = ca_address_sk
--  and     ca_gmt_offset           = [GMT]
--  group by i_item_id),
--  cs as (
--  select i_item_id,sum(cs_ext_sales_price) total_sales
--  from
--  	catalog_sales,
--  	date_dim,
--          customer_address,
--          item
--  where
--          i_item_id               in (select
--   i_item_id
-- from item
-- where i_color in ('[COLOR.1]','[COLOR.2]','[COLOR.3]'))
--  and     cs_item_sk              = i_item_sk
--  and     cs_sold_date_sk         = d_date_sk
--  and     d_year                  = [YEAR]
--  and     d_moy                   = [MONTH]
--  and     cs_bill_addr_sk         = ca_address_sk
--  and     ca_gmt_offset           = [GMT]
--  group by i_item_id),
--  ws as (
--  select i_item_id,sum(ws_ext_sales_price) total_sales
--  from
--  	web_sales,
--  	date_dim,
--          customer_address,
--          item
--  where
--          i_item_id               in (select
--   i_item_id
-- from item
-- where i_color in ('[COLOR.1]','[COLOR.2]','[COLOR.3]'))
--  and     ws_item_sk              = i_item_sk
--  and     ws_sold_date_sk         = d_date_sk
--  and     d_year                  = [YEAR]
--  and     d_moy                   = [MONTH]
--  and     ws_bill_addr_sk         = ca_address_sk
--  and     ca_gmt_offset           = [GMT]
--  group by i_item_id)
--  [_LIMITA] select [_LIMITB] i_item_id ,sum(total_sales) total_sales
--  from  (select * from ss
--         union all
--         select * from cs
--         union all
--         select * from ws) tmp1
--  group by i_item_id
--  order by total_sales
--  [_LIMITC];
--q54
--union
-- with my_customers as (
--  select distinct c_customer_sk
--         , c_current_addr_sk
--  from
--         ( select cs_sold_date_sk sold_date_sk,
--                  cs_bill_customer_sk customer_sk,
--                  cs_item_sk item_sk
--           from   catalog_sales
--           union all
--           select ws_sold_date_sk sold_date_sk,
--                  ws_bill_customer_sk customer_sk,
--                  ws_item_sk item_sk
--           from   web_sales
--          ) cs_or_ws_sales,
--          item,
--          date_dim,
--          customer
--  where   sold_date_sk = d_date_sk
--          and item_sk = i_item_sk
--          and i_category = '[CATEGORY]'
--          and i_class = '[CLASS]'
--          and c_customer_sk = cs_or_ws_sales.customer_sk
--          and d_moy = [MONTH]
--          and d_year = [YEAR]
--  )
--  , my_revenue as (
--  select c_customer_sk,
--         sum(ss_ext_sales_price) as revenue
--  from   my_customers,
--         store_sales,
--         customer_address,
--         store,
--         date_dim
--  where  c_current_addr_sk = ca_address_sk
--         and ca_county = s_county
--         and ca_state = s_state
--         and ss_sold_date_sk = d_date_sk
--         and c_customer_sk = ss_customer_sk
--         and d_month_seq between (select distinct d_month_seq+1
--                                  from   date_dim where d_year = [YEAR] and d_moy = [MONTH])
--                            and  (select distinct d_month_seq+3
--                                  from   date_dim where d_year = [YEAR] and d_moy = [MONTH])
--  group by c_customer_sk
--  )
--  , segments as
--  (select cast((revenue/50) as int) as segment
--   from   my_revenue
--  )
--  [_LIMITA] select [_LIMITB] segment, count(*) as num_customers, segment*50 as segment_base
--  from segments
--  group by segment
--  order by segment, num_customers
--  [_LIMITC];
--q49
--union
-- [_LIMITA] select [_LIMITB]
--  'web' as channel
--  ,web.item
--  ,web.return_ratio
--  ,web.return_rank
--  ,web.currency_rank
--  from (
--  	select
--  	 item
--  	,return_ratio
--  	,currency_ratio
--  	,rank() over (order by return_ratio) as return_rank
--  	,rank() over (order by currency_ratio) as currency_rank
--  	from
--  	(	select ws.ws_item_sk as item
--  		,(cast(sum(coalesce(wr.wr_return_quantity,0)) as dec(15,4))/
--  		cast(sum(coalesce(ws.ws_quantity,0)) as dec(15,4) )) as return_ratio
--  		,(cast(sum(coalesce(wr.wr_return_amt,0)) as dec(15,4))/
--  		cast(sum(coalesce(ws.ws_net_paid,0)) as dec(15,4) )) as currency_ratio
--  		from
--  		 web_sales ws left outer join web_returns wr
--  			on (ws.ws_order_number = wr.wr_order_number and
--  			ws.ws_item_sk = wr.wr_item_sk)
--                  ,date_dim
--  		where
--  			wr.wr_return_amt > 10000
--  			and ws.ws_net_profit > 1
--                          and ws.ws_net_paid > 0
--                          and ws.ws_quantity > 0
--                          and ws_sold_date_sk = d_date_sk
--                          and d_year = [YEAR]
--                          and d_moy = [MONTH]
--  		group by ws.ws_item_sk
--  	) in_web
--  ) web
--  where
--  (
--  web.return_rank <= 10
--  or
--  web.currency_rank <= 10
--  )
--  union
--  select
--  'catalog' as channel
--  ,catalog.item
--  ,catalog.return_ratio
--  ,catalog.return_rank
--  ,catalog.currency_rank
--  from (
--  	select
--  	 item
--  	,return_ratio
--  	,currency_ratio
--  	,rank() over (order by return_ratio) as return_rank
--  	,rank() over (order by currency_ratio) as currency_rank
--  	from
--  	(	select
--  		cs.cs_item_sk as item
--  		,(cast(sum(coalesce(cr.cr_return_quantity,0)) as dec(15,4))/
--  		cast(sum(coalesce(cs.cs_quantity,0)) as dec(15,4) )) as return_ratio
--  		,(cast(sum(coalesce(cr.cr_return_amount,0)) as dec(15,4))/
--  		cast(sum(coalesce(cs.cs_net_paid,0)) as dec(15,4) )) as currency_ratio
--  		from
--  		catalog_sales cs left outer join catalog_returns cr
--  			on (cs.cs_order_number = cr.cr_order_number and
--  			cs.cs_item_sk = cr.cr_item_sk)
--                 ,date_dim
--  		where
--  			cr.cr_return_amount > 10000
--  			and cs.cs_net_profit > 1
--                          and cs.cs_net_paid > 0
--                          and cs.cs_quantity > 0
--                          and cs_sold_date_sk = d_date_sk
--                          and d_year = [YEAR]
--                          and d_moy = [MONTH]
--                  group by cs.cs_item_sk
--  	) in_cat
--  ) catalog
--  where
--  (
--  catalog.return_rank <= 10
--  or
--  catalog.currency_rank <=10
--  )
--  union
--  select
--  'store' as channel
--  ,store.item
--  ,store.return_ratio
--  ,store.return_rank
--  ,store.currency_rank
--  from (
--  	select
--  	 item
--  	,return_ratio
--  	,currency_ratio
--  	,rank() over (order by return_ratio) as return_rank
--  	,rank() over (order by currency_ratio) as currency_rank
--  	from
--  	(	select sts.ss_item_sk as item
--  		,(cast(sum(coalesce(sr.sr_return_quantity,0)) as dec(15,4))/cast(sum(coalesce(sts.ss_quantity,0)) as dec(15,4) )) as return_ratio
--  		,(cast(sum(coalesce(sr.sr_return_amt,0)) as dec(15,4))/cast(sum(coalesce(sts.ss_net_paid,0)) as dec(15,4) )) as currency_ratio
--  		from
--  		store_sales sts left outer join store_returns sr
--  			on (sts.ss_ticket_number = sr.sr_ticket_number and sts.ss_item_sk = sr.sr_item_sk)
--                 ,date_dim
--  		where
--  			sr.sr_return_amt > 10000
--  			and sts.ss_net_profit > 1
--                          and sts.ss_net_paid > 0
--                          and sts.ss_quantity > 0
--                          and ss_sold_date_sk = d_date_sk
--                          and d_year = [YEAR]
--                          and d_moy = [MONTH]
--  		group by sts.ss_item_sk
--  	) in_store
--  ) store
--  where  (
--  store.return_rank <= 10
--  or
--  store.currency_rank <= 10
--  )
--  order by 1,4,5
--  [_LIMITC];
--q33
--union
-- create view itemq33 as
-- select i_manufact_id
-- from item
-- where i_category in ('CATEGORY');
--
--  create view ssq33 as
--  select i_manufact_id,sum(ss_ext_sales_price) total_sales
--  from store_sales,date_dim,customer_address,item
--  where i_manufact_id in (select * from itemq33)
--  and     ss_item_sk              = i_item_sk
--  and     ss_sold_date_sk         = d_date_sk
--  and     d_year                  = 2000
--  and     d_moy                   = 2
--  and     ss_addr_sk              = ca_address_sk
--  and     ca_gmt_offset           = 2
--  group by i_manufact_id;
--
--  create view csq33 as
--  select i_manufact_id,sum(cs_ext_sales_price) total_sales
--  from catalog_sales,date_dim,customer_address,item
--  where i_manufact_id in (select * from itemq33)
--  and     cs_item_sk              = i_item_sk
--  and     cs_sold_date_sk         = d_date_sk
--  and     d_year                  = 2000
--  and     d_moy                   = 2
--  and     cs_bill_addr_sk         = ca_address_sk
--  and     ca_gmt_offset           = 2
--  group by i_manufact_id;
--
--  create view wsq33 as
--  select i_manufact_id,sum(ws_ext_sales_price) total_sales
--  from web_sales,date_dim,customer_address,item
--  where i_manufact_id in (select * from itemq33)
--  and     ws_item_sk              = i_item_sk
--  and     ws_sold_date_sk         = d_date_sk
--  and     d_year                  = 2000
--  and     d_moy                   = 2
--  and     ws_bill_addr_sk         = ca_address_sk
--  and     ca_gmt_offset           = 2
--  group by i_manufact_id;
--
--  select i_manufact_id ,sum(total_sales) total_sales
--  from  (select * from ssq33
--         union all
--         select * from csq33
--         union all
--         select * from wsq33) tmp1
--  group by i_manufact_id;
--  --order by total_sales;
--q23
--union
-- with frequent_ss_items as
--  (select substr(i_item_desc,1,30) itemdesc,i_item_sk item_sk,d_date solddate,count(*) cnt
--   from store_sales
--       ,date_dim
--       ,item
--   where ss_sold_date_sk = d_date_sk
--     and ss_item_sk = i_item_sk
--     and d_year in ([YEAR],[YEAR]+1,[YEAR]+2,[YEAR]+3)
--   group by substr(i_item_desc,1,30),i_item_sk,d_date
--   having count(*) >4),
--  max_store_sales as
--  (select max(csales) tpcds_cmax
--   from (select c_customer_sk,sum(ss_quantity*ss_sales_price) csales
--         from store_sales
--             ,customer
--             ,date_dim
--         where ss_customer_sk = c_customer_sk
--          and ss_sold_date_sk = d_date_sk
--          and d_year in ([YEAR],[YEAR]+1,[YEAR]+2,[YEAR]+3)
--         group by c_customer_sk) x),
--  best_ss_customer as
--  (select c_customer_sk,sum(ss_quantity*ss_sales_price) ssales
--   from store_sales
--       ,customer
--   where ss_customer_sk = c_customer_sk
--   group by c_customer_sk
--   having sum(ss_quantity*ss_sales_price) > ([TOPPERCENT]/100.0) * (select
--   *
-- from
--  max_store_sales))
--  [_LIMITA] select [_LIMITB] sum(sales)
--  from ((select cs_quantity*cs_list_price sales
--        from catalog_sales
--            ,date_dim
--        where d_year = [YEAR]
--          and d_moy = [MONTH]
--          and cs_sold_date_sk = d_date_sk
--          and cs_item_sk in (select item_sk from frequent_ss_items)
--          and cs_bill_customer_sk in (select c_customer_sk from best_ss_customer))
--       union all
--       (select ws_quantity*ws_list_price sales
--        from web_sales
--            ,date_dim
--        where d_year = [YEAR]
--          and d_moy = [MONTH]
--          and ws_sold_date_sk = d_date_sk
--          and ws_item_sk in (select item_sk from frequent_ss_items)
--          and ws_bill_customer_sk in (select c_customer_sk from best_ss_customer))) y
--  [_LIMITC];
--
--  with frequent_ss_items as
--  (select substr(i_item_desc,1,30) itemdesc,i_item_sk item_sk,d_date solddate,count(*) cnt
--   from store_sales
--       ,date_dim
--       ,item
--   where ss_sold_date_sk = d_date_sk
--     and ss_item_sk = i_item_sk
--     and d_year in ([YEAR],[YEAR] + 1,[YEAR] + 2,[YEAR] + 3)
--   group by substr(i_item_desc,1,30),i_item_sk,d_date
--   having count(*) >4),
--  max_store_sales as
--  (select max(csales) tpcds_cmax
--   from (select c_customer_sk,sum(ss_quantity*ss_sales_price) csales
--         from store_sales
--             ,customer
--             ,date_dim
--         where ss_customer_sk = c_customer_sk
--          and ss_sold_date_sk = d_date_sk
--          and d_year in ([YEAR],[YEAR]+1,[YEAR]+2,[YEAR]+3)
--         group by c_customer_sk) x),
--  best_ss_customer as
--  (select c_customer_sk,sum(ss_quantity*ss_sales_price) ssales
--   from store_sales
--       ,customer
--   where ss_customer_sk = c_customer_sk
--   group by c_customer_sk
--   having sum(ss_quantity*ss_sales_price) > ([TOPPERCENT]/100.0) * (select
--   *
--  from max_store_sales))
--  [_LIMITA] select [_LIMITB] c_last_name,c_first_name,sales
--  from ((select c_last_name,c_first_name,sum(cs_quantity*cs_list_price) sales
--         from catalog_sales
--             ,customer
--             ,date_dim
--         where d_year = [YEAR]
--          and d_moy = [MONTH]
--          and cs_sold_date_sk = d_date_sk
--          and cs_item_sk in (select item_sk from frequent_ss_items)
--          and cs_bill_customer_sk in (select c_customer_sk from best_ss_customer)
--          and cs_bill_customer_sk = c_customer_sk
--        group by c_last_name,c_first_name)
--       union all
--       (select c_last_name,c_first_name,sum(ws_quantity*ws_list_price) sales
--        from web_sales
--            ,customer
--            ,date_dim
--        where d_year = [YEAR]
--          and d_moy = [MONTH]
--          and ws_sold_date_sk = d_date_sk
--          and ws_item_sk in (select item_sk from frequent_ss_items)
--          and ws_bill_customer_sk in (select c_customer_sk from best_ss_customer)
--          and ws_bill_customer_sk = c_customer_sk
--        group by c_last_name,c_first_name)) y
--      order by c_last_name,c_first_name,sales
--   [_LIMITC];
--q14
--union
-- with  cross_items as
--  (select i_item_sk ss_item_sk
--  from item,
--  (select iss.i_brand_id brand_id
--      ,iss.i_class_id class_id
--      ,iss.i_category_id category_id
--  from store_sales
--      ,item iss
--      ,date_dim d1
--  where ss_item_sk = iss.i_item_sk
--    and ss_sold_date_sk = d1.d_date_sk
--    and d1.d_year between 1999 AND 1999 + 2
--  intersect
--  select ics.i_brand_id
--      ,ics.i_class_id
--      ,ics.i_category_id
--  from catalog_sales
--      ,item ics
--      ,date_dim d2
--  where cs_item_sk = ics.i_item_sk
--    and cs_sold_date_sk = d2.d_date_sk
--    and d2.d_year between 1999 AND 1999 + 2
--  intersect
--  select iws.i_brand_id
--      ,iws.i_class_id
--      ,iws.i_category_id
--  from web_sales
--      ,item iws
--      ,date_dim d3
--  where ws_item_sk = iws.i_item_sk
--    and ws_sold_date_sk = d3.d_date_sk
--    and d3.d_year between 1999 AND 1999 + 2) x
--  where i_brand_id = brand_id
--       and i_class_id = class_id
--       and i_category_id = category_id
-- ),
--  avg_sales as
--  (select avg(quantity*list_price) average_sales
--   from (select ss_quantity quantity
--              ,ss_list_price list_price
--        from store_sales
--            ,date_dim
--        where ss_sold_date_sk = d_date_sk
--          and d_year between 1999 and 2001
--        union all
--        select cs_quantity quantity
--              ,cs_list_price list_price
--        from catalog_sales
--            ,date_dim
--        where cs_sold_date_sk = d_date_sk
--          and d_year between [YEAR] and [YEAR] + 2
--        union all
--        select ws_quantity quantity
--              ,ws_list_price list_price
--        from web_sales
--            ,date_dim
--        where ws_sold_date_sk = d_date_sk
--          and d_year between [YEAR] and [YEAR] + 2) x)
--  [_LIMITA] select [_LIMITB] channel, i_brand_id,i_class_id,i_category_id,sum(sales), sum(number_sales)
--  from(
--        select 'store' channel, i_brand_id,i_class_id
--              ,i_category_id,sum(ss_quantity*ss_list_price) sales
--              , count(*) number_sales
--        from store_sales
--            ,item
--            ,date_dim
--        where ss_item_sk in (select ss_item_sk from cross_items)
--          and ss_item_sk = i_item_sk
--          and ss_sold_date_sk = d_date_sk
--          and d_year = [YEAR]+2
--          and d_moy = 11
--        group by i_brand_id,i_class_id,i_category_id
--        having sum(ss_quantity*ss_list_price) > (select average_sales from avg_sales)
--        union all
--        select 'catalog' channel, i_brand_id,i_class_id,i_category_id, sum(cs_quantity*cs_list_price) sales, count(*) number_sales
--        from catalog_sales
--            ,item
--            ,date_dim
--        where cs_item_sk in (select ss_item_sk from cross_items)
--          and cs_item_sk = i_item_sk
--          and cs_sold_date_sk = d_date_sk
--          and d_year = [YEAR]+2
--          and d_moy = 11
--        group by i_brand_id,i_class_id,i_category_id
--        having sum(cs_quantity*cs_list_price) > (select average_sales from avg_sales)
--        union all
--        select 'web' channel, i_brand_id,i_class_id,i_category_id, sum(ws_quantity*ws_list_price) sales , count(*) number_sales
--        from web_sales
--            ,item
--            ,date_dim
--        where ws_item_sk in (select ss_item_sk from cross_items)
--          and ws_item_sk = i_item_sk
--          and ws_sold_date_sk = d_date_sk
--          and d_year = [YEAR]+2
--          and d_moy = 11
--        group by i_brand_id,i_class_id,i_category_id
--        having sum(ws_quantity*ws_list_price) > (select average_sales from avg_sales)
--  ) y
--  group by rollup (channel, i_brand_id,i_class_id,i_category_id)
--  order by channel,i_brand_id,i_class_id,i_category_id
--  [_LIMITC];
--
--  with  cross_items as
--  (select i_item_sk ss_item_sk
--  from item,
--  (select iss.i_brand_id brand_id
--      ,iss.i_class_id class_id
--      ,iss.i_category_id category_id
--  from store_sales
--      ,item iss
--      ,date_dim d1
--  where ss_item_sk = iss.i_item_sk
--    and ss_sold_date_sk = d1.d_date_sk
--    and d1.d_year between 1999 AND 1999 + 2
--  intersect
--  select ics.i_brand_id
--      ,ics.i_class_id
--      ,ics.i_category_id
--  from catalog_sales
--      ,item ics
--      ,date_dim d2
--  where cs_item_sk = ics.i_item_sk
--    and cs_sold_date_sk = d2.d_date_sk
--    and d2.d_year between 1999 AND 1999 + 2
--  intersect
--  select iws.i_brand_id
--      ,iws.i_class_id
--      ,iws.i_category_id
--  from web_sales
--      ,item iws
--      ,date_dim d3
--  where ws_item_sk = iws.i_item_sk
--    and ws_sold_date_sk = d3.d_date_sk
--    and d3.d_year between 1999 AND 1999 + 2) x
--  where i_brand_id = brand_id
--       and i_class_id = class_id
--       and i_category_id = category_id
-- ),
--  avg_sales as
-- (select avg(quantity*list_price) average_sales
--   from (select ss_quantity quantity
--              ,ss_list_price list_price
--        from store_sales
--            ,date_dim
--        where ss_sold_date_sk = d_date_sk
--          and d_year between [YEAR] and [YEAR] + 2
--        union all
--        select cs_quantity quantity
--              ,cs_list_price list_price
--        from catalog_sales
--            ,date_dim
--        where cs_sold_date_sk = d_date_sk
--          and d_year between [YEAR] and [YEAR] + 2
--        union all
--        select ws_quantity quantity
--              ,ws_list_price list_price
--        from web_sales
--            ,date_dim
--        where ws_sold_date_sk = d_date_sk
--          and d_year between [YEAR] and [YEAR] + 2) x)
--  [_LIMITA] select [_LIMITB] * from
--  (select 'store' channel, i_brand_id,i_class_id,i_category_id
--         ,sum(ss_quantity*ss_list_price) sales, count(*) number_sales
--  from store_sales
--      ,item
--      ,date_dim
--  where ss_item_sk in (select ss_item_sk from cross_items)
--    and ss_item_sk = i_item_sk
--    and ss_sold_date_sk = d_date_sk
--    and d_week_seq = (select d_week_seq
--                      from date_dim
--                      where d_year = [YEAR] + 1
--                        and d_moy = 12
--                        and d_dom = [DAY])
--  group by i_brand_id,i_class_id,i_category_id
--  having sum(ss_quantity*ss_list_price) > (select average_sales from avg_sales)) this_year,
--  (select 'store' channel, i_brand_id,i_class_id
--         ,i_category_id, sum(ss_quantity*ss_list_price) sales, count(*) number_sales
--  from store_sales
--      ,item
--      ,date_dim
--  where ss_item_sk in (select ss_item_sk from cross_items)
--    and ss_item_sk = i_item_sk
--    and ss_sold_date_sk = d_date_sk
--    and d_week_seq = (select d_week_seq
--                      from date_dim
--                      where d_year = [YEAR]
--                        and d_moy = 12
--                        and d_dom = [DAY])
--  group by i_brand_id,i_class_id,i_category_id
--  having sum(ss_quantity*ss_list_price) > (select average_sales from avg_sales)) last_year
--  where this_year.i_brand_id= last_year.i_brand_id
--    and this_year.i_class_id = last_year.i_class_id
--    and this_year.i_category_id = last_year.i_category_id
--  order by this_year.channel, this_year.i_brand_id, this_year.i_class_id, this_year.i_category_id
--  [_LIMITC];
--q67
create view q67v as
select i_category,i_class,i_brand,i_product_name,d_year,d_qoy,d_moy,s_store_id,sum(ss_sales_price*ss_quantity) sumsales
      from store,ssid
      where  ss_store_sk = s_store_sk
          and d_month_seq between 1 and 12
       group by  i_category, i_class, i_brand, i_product_name, d_year, d_qoy, d_moy,s_store_id;
CREATE VIEW QUERY67 AS
select i_category,i_class,i_brand,i_product_name,d_year,d_qoy,d_moy,s_store_id,sumsales
from q67v
where sumsales <= 100;
-- order by i_category,i_class,i_brand,i_product_name,d_year,d_qoy,d_moy,s_store_id,sumsales,rk;
--q93
create view q93t as
select ss_item_sk,ss_ticket_number,ss_customer_sk, (ss_quantity-sr_return_quantity)*ss_sales_price act_sales
      from store_sales,store_returns,reason
      where sr_item_sk = ss_item_sk and sr_ticket_number = ss_ticket_number and sr_reason_sk = r_reason_sk and r_reason_desc = 'REASON';
CREATE VIEW QUERY93 AS
select ss_customer_sk ,sum(act_sales) sumsales
from q93t
group by ss_customer_sk;
--order by sumsales, ss_customer_sk;
--q50
CREATE VIEW QUERY50 AS
select s_store_name,s_company_id,s_street_number,s_street_name,s_street_type,s_suite_number,s_city,s_county,s_state,s_zip,sr_returned_date_sk,ss_sold_date_sk
from store_sales,store_returns,store,date_dim d1,date_dim d2
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
-- --q35
--  select ca_state,cd_gender,cd_marital_status,count(*) cnt1,avg(cd_dep_count), max(cd_dep_count), min(cd_dep_count),cd_dep_employed_count,count(*) cnt2,avg(cd_dep_employed_count), max(cd_dep_employed_count), min(cd_dep_employed_count), cd_dep_college_count,count(*) cnt3, avg(cd_dep_college_count), max(cd_dep_college_count), min(cd_dep_college_count)
--  from customer c,customer_address ca,customer_demographics
--  where c.c_current_addr_sk = ca.ca_address_sk and cd_demo_sk = c.c_current_cdemo_sk and
--   exists (select *
--           from store_sales,date_dim
--           where c.c_customer_sk = ss_customer_sk and
--                 ss_sold_date_sk = d_date_sk and
--                 d_year = 2000 and
--                 d_qoy < 4) and
--    (exists (select *
--             from web_sales,date_dim
--             where c.c_customer_sk = ws_bill_customer_sk and
--                   ws_sold_date_sk = d_date_sk and
--                   d_year = 2000 and
--                   d_qoy < 4) or
--     exists (select *
--             from catalog_sales,date_dim
--             where c.c_customer_sk = cs_ship_customer_sk and
--                   cs_sold_date_sk = d_date_sk and
--                   d_year = 2000 and
--                   d_qoy < 4))
--  group by ca_state,cd_gender,cd_marital_status,cd_dep_count,cd_dep_employed_count,cd_dep_college_count;
--  --order by ca_state,cd_gender,cd_marital_status,cd_dep_count,cd_dep_employed_count,cd_dep_college_count;
--q24
--having
-- create view ssalesq24 as
-- select c_last_name,c_first_name,s_store_name,ca_state,s_state,i_color,i_current_price,i_manager_id,i_units,i_size,sum(ss_net_paid) netpaid
-- from store_sales,store_returns,store,item,customer,customer_address
-- where ss_ticket_number = sr_ticket_number
--   and ss_item_sk = sr_item_sk
--   and ss_customer_sk = c_customer_sk
--   and ss_item_sk = i_item_sk
--   and ss_store_sk = s_store_sk
--   and c_birth_country = ca_country
--   and s_zip = ca_zip
-- and s_market_id=1
-- group by c_last_name,c_first_name,s_store_name,ca_state,s_state,i_color,i_current_price,i_manager_id,i_units,i_size;
-- CREATE VIEW QUERY24A AS
-- select c_last_name,c_first_name,s_store_name,sum(netpaid) paid
-- from ssalesq24
-- where i_color = 'COLOR1'
-- group by c_last_name,c_first_name,s_store_name;
-- --having sum(netpaid) > (select 0.05*avg(netpaid)from ssales)
-- CREATE VIEW QUERY24B AS
-- select c_last_name,c_first_name,s_store_name,sum(netpaid) paid
-- from ssalesq24
-- where i_color = 'COLOR2'
-- group by c_last_name,c_first_name,s_store_name;
-- --having sum(netpaid) > (select 0.05*avg(netpaid) from ssales);
-- --q39
-- create view foo as
-- select w_warehouse_name,w_warehouse_sk,i_item_sk,d_moy,inv_quantity_on_hand stdev,avg(inv_quantity_on_hand) mean
-- from inventory,item,warehouse,date_dim
-- where inv_item_sk = i_item_sk and inv_warehouse_sk = w_warehouse_sk and inv_date_sk = d_date_sk and d_year =2000
-- group by w_warehouse_name,w_warehouse_sk,i_item_sk,d_moy;
-- 
-- create view invq39 as
-- select w_warehouse_name,w_warehouse_sk,i_item_sk,d_moy,stdev,mean, stdev/mean cov
-- from foo
-- where stdev/mean > 1;
-- 
-- CREATE VIEW QUERY39A AS
-- select inv1.w_warehouse_sk inv1whsk,inv1.i_item_sk inv1isk,inv1.d_moy inv1dmoy,inv1.mean inv1mn, inv1.cov inv1cv,inv2.w_warehouse_sk inv2whsk,inv2.i_item_sk inv2isk,inv2.d_moy inv2dmoy,inv2.mean inv2mn, inv2.cov inv2cv
-- from invq39 inv1,invq39 inv2
-- where inv1.i_item_sk = inv2.i_item_sk and inv1.w_warehouse_sk =  inv2.w_warehouse_sk and inv1.d_moy=2 and inv2.d_moy=3;
-- --order by inv1.w_warehouse_sk,inv1.i_item_sk,inv1.d_moy,inv1.mean,inv1.cov,inv2.d_moy,inv2.mean, inv2.cov;
-- --Why not view the previous and filter its output here?!!!!
-- CREATE VIEW QUERY39B AS
-- select inv1.w_warehouse_sk inv1whsk,inv1.i_item_sk inv1isk,inv1.d_moy inv1dmoy,inv1.mean inv1mn, inv1.cov inv1cv,inv2.w_warehouse_sk inv2whsk,inv2.i_item_sk inv2isk,inv2.d_moy inv2dmoy,inv2.mean inv2mn, inv2.cov inv2cv
-- from invq39 inv1,invq39 inv2
-- where inv1.i_item_sk = inv2.i_item_sk and inv1.w_warehouse_sk =  inv2.w_warehouse_sk and inv1.d_moy=2 and inv2.d_moy=3 and inv1.cov > 1.5;
-- --order by inv1.w_warehouse_sk,inv1.i_item_sk,inv1.d_moy,inv1.mean,inv1.cov,inv2.d_moy,inv2.mean, inv2.cov;
-- --q58
-- create view ddatekatq58 as
-- select d_week_seq
-- from date_dim
-- where d_date = '2000-01-01';
-- 
-- create view ddateq58 as
-- select d_date
-- from date_dim
-- where d_week_seq = (select * from ddatekatq58);
-- 
--  create view ss_itemsq58 as
--  select i_item_id item_id,sum(ss_ext_sales_price) ss_item_rev
--  from ssid
--  where d_date in (select * from ddateq58)
--  group by i_item_id;
-- 
--  create view cs_itemsq58 as
--  select i_item_id item_id,sum(cs_ext_sales_price) cs_item_rev
--  from csid
--  where d_date in (select * from ddateq58)
--  group by i_item_id;
-- 
--  create view ws_itemsq58 as
--  select i_item_id item_id,sum(ws_ext_sales_price) ws_item_rev
--  from wsid
--  where d_date in (select * from ddateq58)
--  group by i_item_id;
-- 
-- CREATE VIEW QUERY58 AS
--  select ss_itemsq58.item_id,ss_item_rev,cs_item_rev,ws_item_rev,ss_item_rev
--  from ss_itemsq58,cs_itemsq58,ws_itemsq58
--  where ss_itemsq58.item_id=cs_itemsq58.item_id
--    and ss_itemsq58.item_id=ws_itemsq58.item_id
--    and ss_item_rev between 0.9 * cs_item_rev and 1.1 * cs_item_rev
--    and ss_item_rev between 0.9 * ws_item_rev and 1.1 * ws_item_rev
--    and cs_item_rev between 0.9 * ss_item_rev and 1.1 * ss_item_rev
--    and cs_item_rev between 0.9 * ws_item_rev and 1.1 * ws_item_rev
--    and ws_item_rev between 0.9 * ss_item_rev and 1.1 * ss_item_rev
--    and ws_item_rev between 0.9 * cs_item_rev and 1.1 * cs_item_rev;
--  --order by item_id,ss_item_rev;
-- --q69
-- --wtf and exists?
-- CREATE VIEW QUERY69 AS
--  select cd_gender,cd_marital_status,cd_education_status,count(*) cnt1,cd_purchase_estimate,count(*) cnt2,cd_credit_rating,count(*) cnt3
--  from v_customer
--  where
--   ca_state in ('STATE1','STATE2','STATE3') and cd_demo_sk = c_current_cdemo_sk and
--   exists (select *
--           from store_sales,date_dim
--           where c_customer_sk = ss_customer_sk and ss_sold_date_sk = d_date_sk and d_year = 2000 and d_moy between 1 and 3) and
--    (not exists (select *
-- 		from web_sales,date_dim
-- 		where c_customer_sk = ws_bill_customer_sk and ws_sold_date_sk = d_date_sk and d_year = 2000 and d_moy between 1 and 3) and
--     not exists (select *
-- 		from catalog_sales,date_dim
-- 		where c_customer_sk = cs_ship_customer_sk and cs_sold_date_sk = d_date_sk and d_year = 2000 and d_moy between 1 and 3))
--  group by cd_gender,cd_marital_status,cd_education_status,cd_purchase_estimate,cd_credit_rating;
--  --order by cd_gender,cd_marital_status,cd_education_status,cd_purchase_estimate,cd_credit_rating;
-- --q10
-- CREATE VIEW QUERY10 AS
--  select cd_gender,cd_marital_status,cd_education_status,count(*) cnt1,cd_purchase_estimate,count(*) cnt2,cd_credit_rating,count(*) cnt3,cd_dep_count,count(*) cnt4,cd_dep_employed_count,count(*) cnt5,cd_dep_college_count,count(*) cnt6
--  from v_customer
--  where
--   ca_county in ('COUNTY1','COUNTY2','COUNTY3','COUNTY4','COUNTY5') and cd_demo_sk = c_current_cdemo_sk and
--   exists (select *
--           from store_sales,date_dim
--           where c_customer_sk = ss_customer_sk and ss_sold_date_sk = d_date_sk and d_year = 2000 and d_moy between 1 and 4) and
--    (exists (select *
--             from web_sales,date_dim
--             where c_customer_sk = ws_bill_customer_sk and ws_sold_date_sk = d_date_sk and d_year = 2000 and d_moy between 1 and 4) or
--     exists (select *
--             from catalog_sales,date_dim
--             where c_customer_sk = cs_ship_customer_sk and cs_sold_date_sk = d_date_sk and d_year = 2000 and d_moy between 1 and 4))
--  group by cd_gender,cd_marital_status,cd_education_status,cd_purchase_estimate,cd_credit_rating,cd_dep_count,cd_dep_employed_count,cd_dep_college_count;
--  --order by cd_gender,cd_marital_status,cd_education_status,cd_purchase_estimate,cd_credit_rating,cd_dep_count,cd_dep_employed_count,cd_dep_college_count;
-- -- --q38
-- -- select count(*) from (
-- --     select c_last_name, c_first_name, d_date
-- --     from store_sales, date_dim, customer
-- --     where store_sales.ss_sold_date_sk = date_dim.d_date_sk
-- --       and store_sales.ss_customer_sk = customer.c_customer_sk
-- --       and d_month_seq between 1 and 12
-- --   intersect
-- --     select c_last_name, c_first_name, d_date
-- --     from catalog_sales, date_dim, customer
-- --     where catalog_sales.cs_sold_date_sk = date_dim.d_date_sk
-- --       and catalog_sales.cs_bill_customer_sk = customer.c_customer_sk
-- --       and d_month_seq between 1 and 12
-- --   intersect
-- --     select c_last_name, c_first_name, d_date
-- --     from web_sales, date_dim, customer
-- --     where web_sales.ws_sold_date_sk = date_dim.d_date_sk
-- --       and web_sales.ws_bill_customer_sk = customer.c_customer_sk
-- --       and d_month_seq between 1 and 12
-- -- ) hot_cust;