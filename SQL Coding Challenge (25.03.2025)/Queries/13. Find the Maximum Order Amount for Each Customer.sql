SELECT customer_id, MAX(total_amount) AS max_order_amount 
FROM orders 
GROUP BY customer_id;
