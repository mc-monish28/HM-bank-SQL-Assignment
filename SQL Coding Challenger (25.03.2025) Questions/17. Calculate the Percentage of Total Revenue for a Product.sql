SELECT product_id, 
       (SUM(item_amount) / (SELECT SUM(item_amount) FROM order_items) * 100) AS revenue_percentage
FROM order_items 
GROUP BY product_id;
