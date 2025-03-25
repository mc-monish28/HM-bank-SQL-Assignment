SELECT customer_id, SUM(total_amount) AS total_spent 
FROM orders 
GROUP BY customer_id 
HAVING total_spent > 1000;
