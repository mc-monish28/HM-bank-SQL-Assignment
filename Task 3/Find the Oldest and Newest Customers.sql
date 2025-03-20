SELECT * FROM Customers 
ORDER BY created_at ASC 
LIMIT 1;  -- Oldest customer

SELECT * FROM Customers 
ORDER BY created_at DESC 
LIMIT 1;  -- Newest customer