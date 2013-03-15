--SPJ view
CREATE VIEW spj AS
	SELECT ws1.ws_order_number, ws1.ws_warehouse_sk wh1, ws2.ws_warehouse_sk wh2
	FROM web_sales ws1 , web_sales ws2
	WHERE ws1.ws_order_number = ws2.ws_order_number
		AND ws1.ws_warehouse_sk <> ws2.ws_warehouse_sk;
