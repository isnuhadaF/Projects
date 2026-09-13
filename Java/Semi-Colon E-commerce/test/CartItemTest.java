import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CartItemTest {

    @Test
    void getItemTotal_multipliesPriceByQuantity() {
        Product rice = new Product("Rice", 500.0);
        CartItem item = new CartItem(rice, 3);

        assertEquals(1500.0, item.getItemTotal());
    }

    @Test
    void addProductCount_increasesCountAndTotal() {
        Product rice = new Product("Rice", 500.0);
        CartItem item = new CartItem(rice, 2);

        item.addProductCount(3);

        assertEquals(5, item.getProductCount());
        assertEquals(2500.0, item.getItemTotal());
    }

    @Test
    void getProductName_returnsSameProductPassedIn() {
        Product rice = new Product("Rice", 500.0);
        CartItem item = new CartItem(rice, 1);

        assertEquals(rice, item.getProductName());
    }
}