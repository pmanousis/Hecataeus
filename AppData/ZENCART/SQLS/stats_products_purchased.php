select p.products_id, p.products_ordered, pd.products_name from PRODUCTS  p, PRODUCTS_DESCRIPTION pd where pd.products_id = p.products_id and pd.language_id = 0 and p.products_ordered > 0 group by pd.products_id;

