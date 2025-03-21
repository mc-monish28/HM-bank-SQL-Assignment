SELECT customer_id, first_name, last_name, balance
FROM Accounts
WHERE balance = (SELECT MAX(balance) FROM Accounts);
