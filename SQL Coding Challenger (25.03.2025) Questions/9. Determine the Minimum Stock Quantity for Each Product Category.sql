SELECT name, MIN(stockQuantity) AS min_stock 
FROM products 
GROUP BY name;
