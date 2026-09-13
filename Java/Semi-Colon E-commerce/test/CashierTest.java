import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CashierTest {

    @Test
    void gettersReturnConstructorValues() {
        Cashier cashier = new Cashier("Abraham", 101, "pass123");

        assertEquals("Abraham", cashier.getStoreCashier());
        assertEquals(101, cashier.getCashierID());
        assertEquals("pass123", cashier.getCashierPassword());
    }

    @Test
    void addItemToCart_addsItemToCustomersOwnCart() {
        Cashier cashier = new Cashier("Abraham", 101, "pass123");
        Customer customer = new Customer("Chukwuma");
        Product rice = new Product("Rice", 500.0);

        cashier.addItemToCart(customer, rice, 2);

        assertEquals(1, customer.getCustomerCart().getCartItems().size());
        assertEquals(2, customer.getCustomerCart().getCartItems().get(0).getProductCount());
    }
}