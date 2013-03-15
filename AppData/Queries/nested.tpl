-- NESTED
SELECT *
FROM catalog_sales, date_dim 
WHERE d_year = 2000 AND d_moy = 7 AND cs_sold_date_sk = d_date_sk 
      AND cs_item_sk IN (SELECT i_item_sk FROM item)
      AND cs_bill_customer_sk IN (SELECT c_customer_sk FROM customer);

