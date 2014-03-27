select z.zone_id, c.countries_id, c.countries_name, z.zone_name, z.zone_code, z.zone_country_id from ZONES  z, COUNTRIES  c where z.zone_country_id = c.countries_id;

