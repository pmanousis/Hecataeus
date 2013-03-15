                                                              --Query over many views and tables, Having, Group by, Function

CREATE VIEW frequent_ss_items AS 
	SELECT i_item_desc, i_item_sk, d_date solddate, count(*) cnt
	FROM store_sales, date_dim, item
	WHERE ss_sold_date_sk = d_date_sk AND ss_item_sk = i_item_sk AND d_year IN (2000,2001,2002,2003)
	GROUP BY i_item_desc, i_item_sk, d_date;
	--HAVING count(*) > 4;

CREATE VIEW best_ss_customer AS
	SELECT c_customer_sk --, SUM(ss_quantity*ss_sales_price) ssales
	FROM store_sales, customer
	WHERE ss_customer_sk = c_customer_sk
	GROUP BY c_customer_sk;
	--HAVING SUM(ss_quantity*ss_sales_price) > 0.95 * (SELECT * FROM max_store_sales);


SELECT *
FROM catalog_sales, date_dim 
WHERE d_year = 2000 AND d_moy = 7 AND cs_sold_date_sk = d_date_sk 
      AND cs_item_sk IN (SELECT i_item_sk FROM frequent_ss_items)
      AND cs_bill_customer_sk IN (SELECT c_customer_sk FROM best_ss_customer);