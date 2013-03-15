--Query over single view
CREATE VIEW cross_sales AS
SELECT i_product_name product_name, i_item_sk item_sk, d1.d_year as syear, count(*) cnt
FROM   store_sales, store_returns, date_dim d1, item
WHERE  ss_sold_date_sk = d1.d_date_sk AND
       ss_item_sk = i_item_sk AND ss_item_sk = sr_item_sk AND 
       ss_ticket_number = sr_ticket_number AND
       (i_current_price BETWEEN 12 AND 22 OR i_current_price between 26 AND 36)
GROUP BY i_product_name, i_item_sk, d1.d_year;

SELECT cs1.product_name, cs1.syear, cs1.cnt, cs2.syear, cs2.cnt
FROM cross_sales cs1,cross_sales cs2
WHERE cs1.item_sk=cs2.item_sk AND cs1.syear = 2000 AND cs2.syear = 2000 +1
ORDER BY cs1.product_name, cs2.cnt;
