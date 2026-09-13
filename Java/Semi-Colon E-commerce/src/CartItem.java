public class CartItem {
    private final Product product;
    private int productCount;

    public CartItem(Product product, int productCount) {
        this.product = product;
        this.productCount = productCount;
    }

    public Product getProductName() {
        return product;
    }

    public int getProductCount() {
        return productCount;
    }

    public void addProductCount(int productQuantity) {
        productCount += productQuantity;
    }

    public double getItemTotal() {
        return product.getPrice() * productCount;
    }

    public double getItemDiscount(double discountRate) {
        return product.getPrice() * discountRate;
    }
}
