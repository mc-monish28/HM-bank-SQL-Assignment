SELECT customer_id, AVG(total_amount) AS avg_order_amount 
FROM orders 
GROUP BY customer_id;
