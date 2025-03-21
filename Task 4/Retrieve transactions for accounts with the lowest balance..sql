SELECT *
FROM Transactions
WHERE account_id IN (
    SELECT account_id
    FROM Accounts
    WHERE balance = (SELECT MIN(balance) FROM Accounts)
);
