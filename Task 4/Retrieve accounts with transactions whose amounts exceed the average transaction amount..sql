SELECT *
FROM Transactions
WHERE amount > (SELECT AVG(amount) FROM Transactions);
