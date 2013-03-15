--SPJG view
CREATE VIEW spjg AS
	SELECT ca_county, d_qoy, d_year, sum(ss_ext_sales_price) AS sumPrice
	FROM store_sales, date_dim, customer_address
	WHERE ss_sold_date_sk = d_date_sk AND ss_addr_sk=ca_address_sk
	GROUP BY ca_county,d_qoy, d_year ; 