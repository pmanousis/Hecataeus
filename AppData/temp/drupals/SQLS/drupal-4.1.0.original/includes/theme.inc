SELECT * FROM system where type = 'theme' AND status = '1' ORDER BY name;
SELECT * FROM blocks WHERE (status = '1' OR custom = '1') AND region = '%s';
