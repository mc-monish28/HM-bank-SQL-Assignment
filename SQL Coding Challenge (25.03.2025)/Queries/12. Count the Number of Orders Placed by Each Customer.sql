SELECT customer_id, COUNT(order_id) AS order_count 
FROM orders 
GROUP BY customer_id;
