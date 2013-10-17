SELECT orders_products_id, orders_products_filename, products_prid from ORDERS_PRODUCTS_DOWNLOAD  WHERE orders_products_download_id=0;
select txn_id, parent_txn_id from PAYPAL  where order_id = 0;
SELECT opd.*, op.products_id from ORDERS_PRODUCTS_DOWNLOAD  opd, ORDERS_PRODUCTS op;

