select * from MEDIA_MANAGER;
select type_id, type_name, type_ext from MEDIA_TYPES;
.select * from MEDIA_CLIPS  where media_id = 0;
select * from MEDIA_TO_PRODUCTS;
select product_id from MEDIA_TO_PRODUCTS;
.select clip_id from MEDIA_CLIPS where media_id = 0;


select media_id, product_id from MEDIA_TO_PRODUCTS;
select media_id, media_name from MEDIA_MANAGER;
select media_id, clip_id, clip_filename, clip_type from MEDIA_CLIPS;
select type_ext, type_name from MEDIA_TYPES;


