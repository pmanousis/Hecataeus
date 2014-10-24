SELECT 1 FROM  url_alias  WHERE language <> 0;
SELECT COUNT(alias) FROM  url_alias  WHERE pid <> 0 AND alias = 0 AND language = 0;
