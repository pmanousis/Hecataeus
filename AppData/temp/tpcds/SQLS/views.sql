CREATE VIEW v_customer AS
SELECT *
FROM customer, customer_demographics, customer_address
WHERE customer.c_current_addr_sk = customer_address.ca_address_sk AND customer.c_current_cdemo_sk = customer_demographics.cd_demo_sk;

CREATE VIEW ssid AS
SELECT *
FROM store_sales, item, date_dim
WHERE ss_item_sk=i_item_sk AND ss_sold_date_sk=d_date_sk;

CREATE VIEW csid AS
SELECT *
FROM catalog_sales, item, date_dim
WHERE cs_item_sk=i_item_sk AND cs_sold_date_sk=d_date_sk;

CREATE VIEW wsid AS
SELECT *
FROM web_sales, item, date_dim
WHERE ws_item_sk=i_item_sk AND ws_sold_date_sk=d_date_sk;

CREATE VIEW VQ9 AS
SELECT *
FROM store_sales,reason
WHERE r_reason_sk=1;