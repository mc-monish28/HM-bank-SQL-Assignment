package bean;

import java.util.regex.Pattern;

public class Customer {
    private long customerID;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;

    public Customer(long customerID, String firstName, String lastName, String email, String phone, String address) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        setEmail(email);
        setPhone(phone);
        this.address = address;
    }

    public void setEmail(String email) {
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        if (!Pattern.matches(regex, email)) {
            throw new IllegalArgumentException("Invalid email address.");
        }
        this.email = email;
    }

    public void setPhone(String phone) {
        if (!phone.matches("\\d{10}")) {
            throw new IllegalArgumentException("Invalid phone number. Must be 10 digits.");
        }
        this.phone = phone;
    }

    public String toString() {
        return "Customer ID: " + customerID + "\nName: " + firstName + " " + lastName + "\nEmail: " + email
                + "\nPhone: " + phone + "\nAddress: " + address;
    }
}
