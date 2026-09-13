import java.util.ArrayList;

public class Product {
    private final String productName;
    private final double productPrice;

    public Product(String productName, double productPrice) {
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public String getName() {
        return productName;
    }

    public double getPrice() {
        return productPrice;
    }
}
