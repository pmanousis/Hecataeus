SELECT aid FROM  actions  WHERE parameters <> '0';
SELECT menu_name, mlid FROM  menu_links  WHERE link_path = 'admin/config' AND module = 'system';
SELECT * FROM  actions  WHERE aid = 0;
