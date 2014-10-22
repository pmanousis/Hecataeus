select * from COUPONS where coupon_id = 0;
select * from COUPONS_DESCRIPTION  where coupon_id = 0: and language_id = 0;
select * from COUPON_RESTRICT where coupon_id=0 and category_id <> 0;
select * from COUPON_RESTRICT  where coupon_id=0 and product_id <> 0;

