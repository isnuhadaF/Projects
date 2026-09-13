import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void getCustomerName_returnsNameGivenAtConstruction() {
        Customer customer = new Customer("Chukwuma");
        assertEquals("Chukwuma", customer.getCustomerName());
    }

    @Test
    void newCustomer_startsWithEmptyCart() {
        Customer customer = new Customer("Chukwuma");
        assertTrue(customer.getCustomerCart().getCartItems().isEmpty());
    }

    @Test
    void twoCustomers_haveCompletelyIndependentCarts() {
        Customer a = new Customer("A");
        Customer b = new Customer("B");

        a.getCustomerCart().addItem(new Product("Rice", 500.0), 1);

        assertEquals(1, a.getCustomerCart().getCartItems().size());
        assertEquals(0, b.getCustomerCart().getCartItems().size());
    }
}