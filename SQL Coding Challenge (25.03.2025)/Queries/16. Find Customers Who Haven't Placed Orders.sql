SELECT * FROM customers 
WHERE customer_id NOT IN (SELECT DISTINCT customer_id FROM orders);
