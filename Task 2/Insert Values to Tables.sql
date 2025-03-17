-- Insert 10 sample records into Customers table
INSERT INTO Customers (first_name, last_name, DOB, email, phone_number, address) VALUES
('John', 'Doe', '1990-05-15', 'john.doe@email.com', '1234567890', 'New York, USA'),
('Jane', 'Smith', '1985-10-22', 'jane.smith@email.com', '1234567891', 'Los Angeles, USA'),
('Michael', 'Brown', '1992-03-08', 'michael.brown@email.com', '1234567892', 'Chicago, USA'),
('Emily', 'Davis', '1995-07-12', 'emily.davis@email.com', '1234567893', 'Houston, USA'),
('Robert', 'Wilson', '1980-11-25', 'robert.wilson@email.com', '1234567894', 'Phoenix, USA'),
('Emma', 'Taylor', '1998-02-17', 'emma.taylor@email.com', '1234567895', 'San Diego, USA'),
('William', 'Anderson', '1983-06-30', 'william.anderson@email.com', '1234567896', 'Dallas, USA'),
('Olivia', 'Martinez', '1991-09-14', 'olivia.martinez@email.com', '1234567897', 'San Jose, USA'),
('James', 'Harris', '1987-04-05', 'james.harris@email.com', '1234567898', 'Austin, USA'),
('Sophia', 'Clark', '1993-12-20', 'sophia.clark@email.com', '1234567899', 'San Antonio, USA');

-- Insert 10 sample records into Accounts table
INSERT INTO Accounts (customer_id, account_type, balance) VALUES
(1, 'savings', 1500.00),
(2, 'current', 5000.00),
(3, 'savings', 2000.00),
(4, 'zero_balance', 0.00),
(5, 'current', 7000.00),
(6, 'savings', 3000.00),
(7, 'current', 2500.00),
(8, 'savings', 1000.00),
(9, 'zero_balance', 0.00),
(10, 'current', 8000.00);

-- Insert 10 sample records into Transactions table
INSERT INTO Transactions (account_id, transaction_type, amount) VALUES
(1, 'deposit', 500.00),
(2, 'withdrawal', 1000.00),
(3, 'deposit', 700.00),
(4, 'deposit', 0.00),
(5, 'withdrawal', 2000.00),
(6, 'deposit', 1500.00),
(7, 'withdrawal', 500.00),
(8, 'deposit', 900.00),
(9, 'deposit', 0.00),
(10, 'withdrawal', 3000.00);
