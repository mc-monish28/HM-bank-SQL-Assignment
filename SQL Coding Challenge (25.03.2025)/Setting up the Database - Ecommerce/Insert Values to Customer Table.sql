INSERT INTO customers (customer_id, name, email, address, password) VALUES
(1, 'John Doe', 'johndoe@example.com', '123 Main St, City', MD5(RAND())),
(2, 'Jane Smith', 'janesmith@example.com', '456 Elm St, Town', MD5(RAND())),
(3, 'Robert Johnson', 'robert@example.com', '789 Oak St, Village', MD5(RAND())),
(4, 'Sarah Brown', 'sarah@example.com', '101 Pine St, Suburb', MD5(RAND())),
(5, 'David Lee', 'david@example.com', '234 Cedar St, District', MD5(RAND())),
(6, 'Laura Hall', 'laura@example.com', '567 Birch St, County', MD5(RAND())),
(7, 'Michael Davis', 'michael@example.com', '890 Maple St, State', MD5(RAND())),
(8, 'Emma Wilson', 'emma@example.com', '321 Redwood St, Country', MD5(RAND())),
(9, 'William Taylor', 'william@example.com', '432 Spruce St, Province', MD5(RAND())),
(10, 'Olivia Adams', 'olivia@example.com', '765 Fir St, Territory', MD5(RAND()));
