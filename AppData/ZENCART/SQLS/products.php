select categories_id from CATEGORIES;
select products_name from PRODUCTS_DESCRIPTION;

select p.products_id, pd.products_name, pd.products_viewed, l.name from PRODUCTS  p, PRODUCTS_DESCRIPTION pd, LANGUAGES  l where p.products_id = pd.products_id and l.languages_id = pd.language_id;

