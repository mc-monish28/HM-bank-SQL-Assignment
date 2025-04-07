# E-Commerce Application

A Java-based E-Commerce application that implements core functionalities like customer management, product management, cart management, and order processing.

## Features

- Customer Management
  - Register new customers
  - Update customer information
  - View customer details

- Product Management
  - Add new products
  - Delete products
  - View product details

- Cart Management
  - Add products to cart
  - Remove products from cart
  - View cart contents

- Order Management
  - Place orders
  - View order history
  - Track order status

## Technical Stack

- Java 11
- MySQL Database
- Maven for dependency management
- JUnit for testing

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── ecommerce/
│   │           ├── entity/       # Entity classes
│   │           ├── dao/          # Data Access Objects
│   │           ├── exception/    # Custom exceptions
│   │           ├── util/         # Utility classes
│   │           └── main/         # Main application class
│   └── resources/
│       ├── db.properties         # Database configuration
│       └── schema.sql            # Database schema
└── test/
    └── java/
        └── com/
            └── ecommerce/
                └── test/         # Unit tests
```

## Setup Instructions

1. Clone the repository
2. Create a MySQL database named `ecommerce_db`
3. Update the `db.properties` file with your database credentials
4. Run the `schema.sql` script to create the required tables
5. Build the project using Maven:
   ```bash
   mvn clean install
   ```
6. Run the application:
   ```bash
   java -cp target/ecommerce-app-1.0-SNAPSHOT.jar com.ecommerce.main.EcomApp
   ```

## Database Schema

The application uses the following tables:

1. `customers` - Stores customer information
2. `products` - Stores product information
3. `cart` - Stores cart items
4. `orders` - Stores order information
5. `order_items` - Stores order details

## Testing

Run the unit tests using:
```bash
mvn test
```

## Exception Handling

The application includes custom exceptions:
- `CustomerNotFoundException`
- `ProductNotFoundException`
- `OrderNotFoundException`

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License. 