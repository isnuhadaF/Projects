import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    @Test
    void newCart_startsEmpty() {
        Cart cart = new Cart();
        assertTrue(cart.getCartItems().isEmpty());
    }

    @Test
    void addItem_newProduct_createsOneLine() {
        Cart cart = new Cart();
        Product rice = new Product("Rice", 500.0);

        cart.addItem(rice, 2);

        assertEquals(1, cart.getCartItems().size());
        assertEquals(2, cart.getCartItems().get(0).getProductCount());
    }

    @Test
    void addItem_sameProductAddedTwice_mergesIntoOneLine() {
        Cart cart = new Cart();
        Product rice = new Product("Rice", 500.0);

        cart.addItem(rice, 2);
        cart.addItem(rice, 3);

        assertEquals(1, cart.getCartItems().size());
        assertEquals(5, cart.getCartItems().get(0).getProductCount());
    }

    @Test
    void addItem_differentProducts_createsSeparateLines() {
        Cart cart = new Cart();
        cart.addItem(new Product("Rice", 500.0), 1);
        cart.addItem(new Product("Beans", 300.0), 1);

        assertEquals(2, cart.getCartItems().size());
    }

    @Test
    void calculateTotal_sumsAllLineTotals() {
        Cart cart = new Cart();
        cart.addItem(new Product("Rice", 500.0), 2);   // 1000
        cart.addItem(new Product("Beans", 300.0), 1);  // 300

        assertEquals(1300.0, cart.calculateTotal());
    }

    @Test
    void calculateDiscount_isCurrentlyAlwaysZero() {
        // NOTE: discountAmount is hardcoded to 0 in Cart right now, so this
        // will need to change once setDiscountRate is reintroduced.
        Cart cart = new Cart();
        cart.addItem(new Product("Rice", 500.0), 2);

        assertEquals(0.0, cart.calculateDiscount());
    }

    @Test
    void calculateVAT_isSevenPointFivePercentOfTotal() {
        Cart cart = new Cart();
        cart.addItem(new Product("Rice", 500.0), 2); // total = 1000

        assertEquals(75.0, cart.calculateVAT());
    }
}