SELECT mlid FROM  menu_links  WHERE link_path = 0;
SELECT mlid FROM  menu_links  WHERE link_path = 0 AND module = 0;
SELECT 1 FROM  menu_links  WHERE menu_name = 0;
SELECT 1 FROM  menu_custom  WHERE menu_name = 0;
SELECT mlid FROM  menu_links  ml WHERE ml.link_path = 0;
SELECT 1 FROM  menu_custom  WHERE menu_name = 0;
SELECT COUNT(*) FROM  menu_links  WHERE menu_name = 0;
SELECT * FROM  menu_custom  ORDER BY title;
