import java.util.ArrayList;

public class Customer {
    private final String customerName;
    private final Cart cart = new Cart();

    public Customer(String customerName) {
        this.customerName = customerName;
    }

    public Cart getCustomerCart() {
        return cart;
    }

    public String getCustomerName() {
        return customerName;
    }
}
