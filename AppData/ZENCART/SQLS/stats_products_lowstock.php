select p.products_id, pd.products_name, p.products_quantity, p.products_type from PRODUCTS  p, PRODUCTS_DESCRIPTION  pd where p.products_id = pd.products_id and pd.language_id=0;

