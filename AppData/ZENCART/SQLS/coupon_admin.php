SELECT * from COUPONS where coupon_code = 0;
SELECT * from COUPONS where coupon_id=0;
SELECT * from COUPONS_DESCRIPTION  where coupon_id=0";
SELECT * from COUPON_RESTRICT  where coupon_id=0;
select * from COUPON_REDEEM_TRACK where coupon_id = 0;
select coupon_id, coupon_code, coupon_amount, coupon_type, coupon_start_date,coupon_expire_date,uses_per_user,uses_per_coupon,restrict_to_products, restrict_to_categories, date_created,date_modified, coupon_active, coupon_zone_restriction from COUPONS where coupon_type <>0;

