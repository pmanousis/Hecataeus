select p.order_id, p.paypal_ipn_id, p.txn_type, p.payment_type, p.payment_status, p.pending_reason, p.mc_currency, p.payer_status, p.mc_currency, p.date_added, p.mc_gross, p.first_name, p.last_name, p.payer_business_name, p.parent_txn_id, p.txn_id from PAYPAL  as p, ORDERS  as o  where o.orders_id = p.order_id;

select p.order_id, p.paypal_ipn_id, p.txn_type, p.payment_type, p.payment_status, p.pending_reason, p.mc_currency, p.payer_status, p.mc_currency, p.date_added, p.mc_gross, p.first_name, p.last_name, p.payer_business_name, p.parent_txn_id, p.txn_id from PAYPAL  as p,ORDERS  as o  where o.orders_id = p.order_id ;
select p.order_id, p.paypal_ipn_id, p.txn_type, p.payment_type, p.payment_status, p.pending_reason, p.mc_currency, p.payer_status, p.mc_currency, p.date_added, p.mc_gross, p.first_name, p.last_name, p.payer_business_name, p.parent_txn_id, p.txn_id from PAYPAL . " as p left join ORDERS  as o on o.orders_id = p.order_id;

