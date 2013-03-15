-- QUERY OVER TABLE, ORDER BY, GROUP BY, BETWEEN
SELECT i_item_id, i_item_desc, i_current_price
FROM item
WHERE i_current_price BETWEEN 13 AND 13+30
 GROUP BY i_item_id,i_item_desc,i_current_price
 ORDER BY i_item_id;