import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void getName_returnsNameGivenAtConstruction() {
        Product product = new Product("Rice", 500.0);
        assertEquals("Rice", product.getName());
    }

    @Test
    void getPrice_returnsPriceGivenAtConstruction() {
        Product product = new Product("Rice", 500.0);
        assertEquals(500.0, product.getPrice());
    }
}