---view web_sales join item join date_dim
CREATE VIEW websales_item_datedim AS 
select i_item_id
			,i_item_desc
			,i_item_sk 
      ,i_category 
      ,i_class 
      ,i_current_price
      ,i_manufact_id
      ,ws_ext_sales_price
      ,ws_sales_price
      ,ws_quantity
      ,ws_list_price
      ,ws_coupon_amt
      ,ws_sales_price
      ,ws_net_paid
      ,ws_ext_discount_amt
      ,ws_bill_customer_sk --for joining with customer
      ,ws_bill_cdemo_sk --for joining with customer demographics
      ,ws_promo_sk --for joining with promotion
      ,ws_item_sk --for query 97 inline views
      ,ws_warehouse_sk --for joining with warehouse
      ,ws_ship_customer_sk --for joining with customer
      ,d_date
      ,d_qoy
      ,d_year
      ,d_moy
from	web_sales
    	,item 
    	,date_dim
where ws_item_sk = i_item_sk 
  	and ws_sold_date_sk = d_date_sk;
  	
/*optimal annotation:
			web_sales : ADD->BLOCK DELETE ->PROP , RENAME->PROP 
    	item      : ADD->PROP, DELETE->PROP, RENAME->PROP
    	date_dim  : ADD->PROP DELETE ->PROP , RENAME->PROP
    	
    	PROP =8 , BLOCK=1
*/

---view customer join customer_address
CREATE VIEW customer_cust_address AS 
select c_customer_sk
			,ca_zip
			,ca_gmt_offset
			
from	customer
    	,customer_address
where c_current_addr_sk = ca_address_sk;
  	
/*optimal annotation:
			customer : ADD->PROP DELETE ->PROP , RENAME->PROP 
    	customer_address      : ADD->PROP, DELETE->PROP, RENAME->PROP
    	
    	PROP =6 , BLOCK=0
*/


--QUERY 12 (changed in this context) //q1
---------------------------------------------------------------------
--Define YEAR=random(1998,2002,uniform);
--Define SDATE=date([YEAR]+"-01-01",[YEAR]+"-07-01",sales);
--Define CATEGORY=list(dist(categories,1,1),3);

select
		 i_item_desc 
      ,i_category 
      ,i_class 
      ,i_current_price
      ,sum(ws_ext_sales_price) as itemrevenue 
      --i transform the following unsupported by the parser clause
      --to as much as similar supported clause that retains the same calls to fields
      /*,sum(ws_ext_sales_price)*100/sum(sum(ws_ext_sales_price)) over
         (partition by i_class) as revenueratio*/
      ,sum(ws_ext_sales_price) as revenueratio1
      ,sum(ws_ext_sales_price) as revenueratio2
      ,i_class as partitionBy
         
from	
	websales_item_datedim
where i_category in ('[CATEGORY.1]', '[CATEGORY.2]', '[CATEGORY.3]')
	and d_date between '[SDATE]' and '[SDATE]+30'
group by 
				i_item_id
        ,i_item_desc 
        ,i_category
        ,i_class
        ,i_current_price;
/*optimal annotation:
			websales_item_datedim : ADD->PROP DELETE ->PROP , RENAME->PROP 
    	
    	PROP =3 , BLOCK=0
*/

--QUERY 45(changed in this context) //q2,q3
---------------------------------------------------------------------

-- define YEAR=random(1998,2002,uniform);
-- define QOY=random(1,2,uniform);

 select ca_zip, sum(ws_sales_price)
 from websales_item_datedim , customer_cust_address 
 where ws_bill_customer_sk = c_customer_sk
 	and ( substring(ca_zip,1,5) in ('85669', '86197','88274','83405','86475', '85392', '85460', '80348', '81792')
 	      or 
 	      i_item_id in (select i2.i_item_id
                             from item i2
                             where i2.i_item_sk in (2, 3, 5, 7, 11, 13, 17, 19, 23, 29)
                             )
 	   )
 
 	and d_qoy = '[QOY]' and d_year = '[YEAR]'
 group by ca_zip;
/*optimal annotation:
Q2
			websales_item_datedim: ADD->BLOCK DELETE ->BLOCK , RENAME->PROP 
			customer_cust_address : ADD->BLOCK DELETE ->BLOCK , RENAME->PROP 
			    	
    	PROP =2 , BLOCK=4
Q3
			item      : ADD->BLOCK, DELETE->BLOCK, RENAME->PROP
    	PROP =1 , BLOCK=2
*/

--QUERY 62(changed in this context) //q4
---------------------------------------------------------------------
--define YEAR = random(1998,2002,uniform);
CREATE VIEW Days30 AS
select
   substring(w_warehouse_name,1,20) warehouse_name
  ,sm_type
  ,web_name
  ,count(ws_ship_date_sk) AS "shipdays"
from
   web_sales
  ,warehouse
  ,ship_mode
  ,web_site
  ,date_dim
where
    d_date = '[YEAR]'
and ws_ship_date_sk   = d_date_sk
and ws_warehouse_sk   = w_warehouse_sk
and ws_ship_mode_sk   = sm_ship_mode_sk
and ws_web_site_sk    = web_site_sk
and ws_ship_date_sk - ws_sold_date_sk <= 30
group by
  substring(w_warehouse_name,1,20)
  ,sm_type
  ,web_name
;
/*optimal annotation:
			web_sales : ADD->BLOCK DELETE ->BLOCK , RENAME->PROP 
    	warehouse : ADD->PROP DELETE ->PROP , RENAME->PROP
			ship_mode  : ADD->PROP DELETE ->PROP , RENAME->PROP
    	web_site  : ADD->PROP DELETE ->PROP , RENAME->PROP
    	date_dim  : ADD->BLOCK DELETE ->PROP , RENAME->PROP
    	
    	PROP =12 , BLOCK=3
*/
CREATE VIEW Days30_60 AS
select
   substring(w_warehouse_name,1,20) warehouse_name
  ,sm_type
  ,web_name
  ,count(ws_ship_date_sk) AS "shipdays" 
from
   web_sales
  ,warehouse
  ,ship_mode
  ,web_site
  ,date_dim
where  ws_ship_date_sk - ws_sold_date_sk > 30 
and   ws_ship_date_sk - ws_sold_date_sk <= 60
and	d_date = '[YEAR]'
and ws_ship_date_sk   = d_date_sk
and ws_warehouse_sk   = w_warehouse_sk
and ws_ship_mode_sk   = sm_ship_mode_sk
and ws_web_site_sk    = web_site_sk
group by
  substring(w_warehouse_name,1,20)
  ,sm_type
  ,web_name
;
/*optimal annotation:
			web_sales : ADD->BLOCK DELETE ->BLOCK , RENAME->PROP 
    	warehouse : ADD->PROP DELETE ->PROP , RENAME->PROP
			ship_mode  : ADD->PROP DELETE ->PROP , RENAME->PROP
    	web_site  : ADD->PROP DELETE ->PROP , RENAME->PROP
    	date_dim  : ADD->BLOCK DELETE ->PROP , RENAME->PROP
    	
    	PROP =12 , BLOCK=3
*/
CREATE VIEW Days60_90 AS
select
   substring(w_warehouse_name,1,20) warehouse_name
  ,sm_type
  ,web_name
  ,count(ws_ship_date_sk) AS "shipdays" 
from
   web_sales
  ,warehouse
  ,ship_mode
  ,web_site
  ,date_dim
where  ws_ship_date_sk - ws_sold_date_sk > 60 
and ws_ship_date_sk - ws_sold_date_sk <= 90
and	d_date = '[YEAR]'
and ws_ship_date_sk   = d_date_sk
and ws_warehouse_sk   = w_warehouse_sk
and ws_ship_mode_sk   = sm_ship_mode_sk
and ws_web_site_sk    = web_site_sk
group by
  substring(w_warehouse_name,1,20)
  ,sm_type
  ,web_name
;
/*optimal annotation:
			web_sales : ADD->BLOCK DELETE ->BLOCK , RENAME->PROP 
    	warehouse : ADD->PROP DELETE ->PROP , RENAME->PROP
			ship_mode  : ADD->PROP DELETE ->PROP , RENAME->PROP
    	web_site  : ADD->PROP DELETE ->PROP , RENAME->PROP
    	date_dim  : ADD->BLOCK DELETE ->PROP , RENAME->PROP
    	
    	PROP =12 , BLOCK=3
*/

CREATE VIEW Days90_120 AS
select
   substring(w_warehouse_name,1,20) warehouse_name
  ,sm_type
  ,web_name
  ,count(ws_ship_date_sk) AS "shipdays" 
from
   web_sales
  ,warehouse
  ,ship_mode
  ,web_site
  ,date_dim
where  ws_ship_date_sk - ws_sold_date_sk > 90
and ws_ship_date_sk - ws_sold_date_sk <= 120
and	d_date = '[YEAR]'
and ws_ship_date_sk   = d_date_sk
and ws_warehouse_sk   = w_warehouse_sk
and ws_ship_mode_sk   = sm_ship_mode_sk
and ws_web_site_sk    = web_site_sk
group by
  substring(w_warehouse_name,1,20)
  ,sm_type
  ,web_name
;
/*optimal annotation:
			web_sales : ADD->BLOCK DELETE ->BLOCK , RENAME->PROP 
    	warehouse : ADD->PROP DELETE ->PROP , RENAME->PROP
			ship_mode  : ADD->PROP DELETE ->PROP , RENAME->PROP
    	web_site  : ADD->PROP DELETE ->PROP , RENAME->PROP
    	date_dim  : ADD->BLOCK DELETE ->PROP , RENAME->PROP
    	
    	PROP =12 , BLOCK=3
*/

CREATE VIEW Days120 AS
select
   substring(w_warehouse_name,1,20) warehouse_name
  ,sm_type
  ,web_name
  ,count(ws_ship_date_sk) AS "shipdays" 
from
   web_sales
  ,warehouse
  ,ship_mode
  ,web_site
  ,date_dim
where  ws_ship_date_sk - ws_sold_date_sk > 120
and	d_date = '[YEAR]'
and ws_ship_date_sk   = d_date_sk
and ws_warehouse_sk   = w_warehouse_sk
and ws_ship_mode_sk   = sm_ship_mode_sk
and ws_web_site_sk    = web_site_sk
group by
  substring(w_warehouse_name,1,20)
  ,sm_type
  ,web_name;
/*optimal annotation:
			web_sales : ADD->BLOCK DELETE ->BLOCK , RENAME->PROP 
    	warehouse : ADD->PROP DELETE ->PROP , RENAME->PROP
			ship_mode  : ADD->PROP DELETE ->PROP , RENAME->PROP
    	web_site  : ADD->PROP DELETE ->PROP , RENAME->PROP
    	date_dim  : ADD->BLOCK DELETE ->PROP , RENAME->PROP
    	
    	PROP =12 , BLOCK=3
*/
  
//q4
select
   d1.warehouse_name
  ,d1.sm_type
  ,d1.web_name
  ,d1.shipdays AS "<30 days" 
  ,d2.shipdays AS "31-60 days" 
  ,d3.shipdays  AS "61-90 days" 
  ,d4.shipdays AS "91-120 days" 
  ,d5.shipdays AS ">120 days" 
from
   Days30 d1
   ,Days30_60 d2
   ,Days60_90 d3
  ,Days90_120 d4
  ,Days120 d5
where
    d1.warehouse_name = d2.warehouse_name
and d2.warehouse_name = d3.warehouse_name
and d3.warehouse_name = d4.warehouse_name
and d4.warehouse_name = d5.warehouse_name
and d1.sm_type = d2.sm_type
and d2.sm_type = d3.sm_type
and d3.sm_type = d4.sm_type
and d4.sm_type = d5.sm_type
and d1.web_name = d2.web_name
and d2.web_name = d3.web_name
and d3.web_name = d4.web_name
and d4.web_name = d5.web_name
;

/*optimal annotation:
			Days30 : ADD->PROP DELETE ->PROP , RENAME->PROP 
    	Days30_60 : ADD->PROP DELETE ->PROP , RENAME->PROP 
    	Days60_90 : ADD->PROP DELETE ->PROP , RENAME->PROP 
    	Days90_120 : ADD->PROP DELETE ->PROP , RENAME->PROP 
    	Days120 : ADD->PROP DELETE ->PROP , RENAME->PROP 
    	
    	PROP =15 , BLOCK=0
    	
    	SUBTOTAL = PROP=75, BLOCK=15
*/
--QUERY 76(changed in this context) //q5
---------------------------------------------------------------------

 /*define GEN= dist(gender, 1, 1);
 define MS= dist(marital_status, 1, 1);
 define ES= dist(education, 1, 1);
 define YEAR = random(1998,2002,uniform);
 */
 
 select i_item_id, 
        avg(ws_quantity) agg1,
        avg(ws_list_price) agg2,
        avg(ws_coupon_amt) agg3,
        avg(ws_sales_price) agg4 
 from websales_item_datedim, customer_demographics, promotion
 where ws_bill_cdemo_sk = cd_demo_sk and
       ws_promo_sk = p_promo_sk and
       cd_gender = '[GEN]' and 
       cd_marital_status = '[MS]' and
       cd_education_status = '[ES]' and
       (p_channel_email = 'N' or p_channel_event = 'N') 
 group by i_item_id;
 
 /*optimal annotation:
			websales_item_datedim : ADD->BLOCK DELETE ->PROP , RENAME->PROP 
			customer_demographics : ADD->BLOCK DELETE ->PROP , RENAME->PROP
			promotion  : ADD->BLOCK, DELETE ->PROP , RENAME->PROP
    	
    	PROP =6 , BLOCK=3
*/


--QUERY 86(changed in this context) //q6
---------------------------------------------------------------------

 --define YEAR=random(1998,2002,uniform);
select
    sum(ws_net_paid) as total_sum
   ,i_category
   ,i_class
   
  /*,grouping(i_category)+grouping(i_class) as lochierarchy
  
  ,rank() over (
 	partition by grouping(i_category)+grouping(i_class),
 	case when grouping(i_class) = 0 then i_category end 
 	order by sum(ws_net_paid) desc) as rank_within_parent*/
 	
 from
    websales_item_datedim
 where
     d_year = '[YEAR]'
 group by i_category,i_class ;
/*optimal annotation:
			websales_item_datedim : ADD->PROP DELETE ->PROP , RENAME->PROP 
    	
    	PROP =3 , BLOCK=0
*/


--QUERY 90(changed in this context) //q7
---------------------------------------------------------------------

 --define DEPCNT=random(0,9,uniform);
 --define HOUR_AM = random(6,12,uniform);
 --define HOUR_PM = random(13,21,uniform);

CREATE VIEW at AS
select count(web_sales.*) as amc
       from web_sales, household_demographics , time_dim, web_page
       where ws_sold_time_sk = t_time_sk
         and ws_ship_hdemo_sk = hd_demo_sk
         and ws_web_page_sk = wp_web_page_sk
         and t_hour between '[HOUR_AM]' and '[HOUR_AM]+1'
         and hd_dep_count = '[DEPCNT]'
         and wp_char_count between '5000' and '5200' ;

/*optimal annotation:
			web_sales : ADD->BLOCK DELETE ->PROP , RENAME->PROP 
    	household_demographics : ADD->BLOCK DELETE ->PROP , RENAME->PROP
			web_page  : ADD->BLOCK, DELETE ->PROP , RENAME->PROP
    	time_dim  : ADD->BLOCK DELETE ->PROP , RENAME->PROP
    	
    	PROP =8 , BLOCK=4
*/         
         
CREATE VIEW pt AS 
select count(web_sales.*) pmc
       from web_sales, household_demographics , time_dim, web_page
       where ws_sold_time_sk = time_dim.t_time_sk
         and ws_ship_hdemo_sk = household_demographics.hd_demo_sk
         and ws_web_page_sk = web_page.wp_web_page_sk
         and time_dim.t_hour between '[HOUR_PM]' and '[HOUR_PM]+1'
         and household_demographics.hd_dep_count = '[DEPCNT]'
         and web_page.wp_char_count between '5000' and '5200';         
 
 /*optimal annotation:
			web_sales : ADD->BLOCK DELETE ->PROP , RENAME->PROP 
    	household_demographics : ADD->BLOCK DELETE ->PROP , RENAME->PROP
			web_page  : ADD->BLOCK, DELETE ->PROP , RENAME->PROP
    	time_dim  : ADD->BLOCK DELETE ->PROP , RENAME->PROP
    	
    	PROP =8 , BLOCK=4
*/
 --query 90 //q7
 select amc/pmc am_pm_ratio
 from at,pt;

/*optimal annotation:
			AT : ADD->PROP DELETE ->BLOCK , RENAME->PROP 
    	PT : ADD->PROP DELETE ->BLOCK , RENAME->PROP
			    	
    	PROP =4 , BLOCK=2
*/

 

--QUERY 92 //q8, q9
---------------------------------------------------------------------


--Define IMID  = random(1,1000,uniform);
--Define YEAR  = random(1998,2002,uniform);
--Define WSDATE1 = date([YEAR]+"-01-01",[YEAR]+"-04-01",sales);
--Define WSDATE2 = cast('[WSDATE]' as date) + 90 days;

--query 92

select
   sum(ws_ext_discount_amt) as "Excess Discount Amount" 
from 
    websales_item_datedim
where 
i_manufact_id = '[IMID]'
and d_date between '[WSDATE1]' and '[WSDATE2]'
and  ws_ext_discount_amt  > ( 
         SELECT avg(ws_ext_discount_amt) as Avg_discount_amt
         FROM  web_sales ws
           		,date_dim dd
         WHERE ws.ws_item_sk = i_item_sk
          and dd.d_date between '[WSDATE1]' and '[WSDATE2]'
          and dd.d_date_sk = ws.ws_sold_date_sk 
      ) 
; 
/*optimal annotation:
Q8
			websales_item_datedim : ADD->BLOCK DELETE ->BLOCK , RENAME->PROP 
			
    	PROP =1 , BLOCK=2
Q9
			web_sales : ADD->BLOCK DELETE ->BLOCK , RENAME->PROP 
    	date_dim  : ADD->BLOCK DELETE ->PROP , RENAME->PROP
    	PROP =3 , BLOCK=3
*/


--QUERY 97(changed in this context) //q10
---------------------------------------------------------------------
--define inline views separately for query 97
/*
	Define YEAR = random(1998,2002, uniform);
	Define MONTH = random(11,12,uniform);
	Define CATEGORY = text({"Books",1},{"Home",1},{"Electronics",1},{"Jewelry",1},{"Sports",1});
	Define GMT = text({"-6",1},{"-7",1});
*/

CREATE VIEW promotional_sale AS
select count(ws_item_sk) promotions
	   from  websales_item_datedim
	        ,warehouse
	        ,promotion
	        ,customer_cust_address
	  where ws_warehouse_sk = w_warehouse_sk
	   and   ws_promo_sk = p_promo_sk
	   and   ws_ship_customer_sk= c_customer_sk
	   and   ca_gmt_offset = '[GMT]'
	   and   i_category = '[CATEGORY]'
	   and   (p_channel_dmail = 'Y' or p_channel_email = 'Y' or p_channel_tv = 'Y')
	   and   d_year = '[YEAR]'
	   and   d_moy  = '[MONTH]';

/*optimal annotation:
			websales_item_datedim : ADD->BLOCK DELETE ->BLOCK , RENAME->PROP
			warehouse : ADD->BLOCK DELETE ->PROP , RENAME->PROP
			promotion  : ADD->BLOCK, DELETE ->PROP , RENAME->PROP
    	customer_cust_address : ADD->BLOCK DELETE ->PROP , RENAME->PROP
			
    	PROP =7 , BLOCK=5
*/	   
CREATE VIEW all_sales AS
select count(ws_item_sk) total
	   from  websales_item_datedim
	        ,warehouse
	        ,customer_cust_address
	  where ws_warehouse_sk = w_warehouse_sk
	   and   ws_ship_customer_sk= c_customer_sk
	   and   ca_gmt_offset = '[GMT]'
	   and   i_category = '[CATEGORY]'
	   and   d_year = '[YEAR]'
	   and   d_moy  = '[MONTH]';
	   
/*optimal annotation:
			websales_item_datedim : ADD->BLOCK DELETE ->BLOCK , RENAME->PROP
			warehouse : ADD->BLOCK DELETE ->PROP , RENAME->PROP
			customer_cust_address : ADD->BLOCK DELETE ->PROP , RENAME->PROP
    	
    	PROP =5 , BLOCK=4
*/		   

--q97
	select promotions, total, ((promotions /total) *100) percent
	from promotional_sale, all_sales;


/*optimal annotation:
			promotional_sale : ADD->PROP DELETE ->BLOCK , RENAME->PROP 
    	all_sales : ADD->PROP DELETE ->BLOCK , RENAME->PROP
		
    	PROP =4 , BLOCK=2
*/

 
/*
OPTIMAL ANNOTATION TOTAL: PROP=130(66.66%), BLOCK=50(33.33%)
*/
 	