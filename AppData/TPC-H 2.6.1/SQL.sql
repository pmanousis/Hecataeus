/*create view c_orders as 
select
	c_custkey as c_custkey,
	count(o_orderkey) as c_count
from
	customer,
	orders
where
	c_custkey = o_custkey
group by
	c_custkey;

create view shipping as 
select
	n1.n_name as supp_nation,
	n2.n_name as cust_nation,
	l_shipdate as l_year,
	l_extendedprice as volume
from
	supplier,
	lineitem,
	orders,
	customer,
	nation n1,
	nation n2
where
	s_suppkey = l_suppkey
	and o_orderkey = l_orderkey
	and c_custkey = o_custkey
	and s_nationkey = n1.n_nationkey
	and c_nationkey = n2.n_nationkey
	and (
		(n1.n_name = ':1' and n2.n_name = ':2')
		or (n1.n_name = ':2' and n2.n_name = ':1')
		)
	and l_shipdate ='1995-01-01';

*/
create view all_nations as 
select
	o_orderdate as o_year,
	l_extendedprice as volume,
	n2.n_name as nation
from
	part,
	supplier,
	lineitem,
	orders,
	customer,
	nation n1,
	nation n2,
	region
where
	p_partkey = l_partkey
	and s_suppkey = l_suppkey
	and l_orderkey = o_orderkey
	and o_custkey = c_custkey
	and c_nationkey = n1.n_nationkey
	and n1.n_regionkey = r_regionkey
	and r_name = '2'
	and s_nationkey = n2.n_nationkey
	and o_orderdate = '1995-01-01'
	and p_type = '3';

/*
create view profit as 
select
	n_name as nation,
	o_orderdate as o_year,
	l_extendedprice as amount
from
	part,
	supplier,
	lineitem,
	partsupp,
	orders,
	nation
where
	s_suppkey = l_suppkey
	and ps_suppkey = l_suppkey
	and ps_partkey = l_partkey
	and p_partkey = l_partkey
	and o_orderkey = l_orderkey
	and s_nationkey = n_nationkey
	and p_name = ':1';

create view revenues as 
	select
		l_suppkey as supplier_no,
		l_extendedprice as total_revenue
	from
		lineitem
	where
		l_shipdate >= ':1'
		and l_shipdate < ':1' 
	group by
		l_suppkey;


--Q7
select
	supp_nation,
	cust_nation,
	l_year,
	sum(volume) as revenue
from
	shipping
group by
	supp_nation,
	cust_nation,
	l_year;

*/
--Q8
select
	o_year,
	sum(volume) as mkt_share
from
	all_nations
group by
	o_year;
/*
--Q9
select
	nation,
	o_year,
	sum(amount) as sum_profit
from
	profit
group by
	nation,
	o_year;


--Q15
select
	s_suppkey,
	s_name,
	s_address,
	s_phone
from
	supplier,
	revenues
where
	s_suppkey = supplier_no;

--Q13
select
	c_count,
	count(*) as custdist
from
	c_orders
group by
	c_count;
	
--Q4
select
	o_orderpriority,
	count(*) as order_count
from
	orders
where
	o_orderdate >= ':1'
	and o_orderdate < ':1'
group by
	o_orderpriority;
	
--Q17
select
	p_brand,
	p_type,
	p_size,
	count(ps_suppkey) as supplier_cnt
from
	partsupp,
	part
where
	p_partkey = ps_partkey
	and p_brand = ':1'
	and p_type = ':2'
	and p_size = ':3'
group by
	p_brand,
	p_type,
	p_size;
*/