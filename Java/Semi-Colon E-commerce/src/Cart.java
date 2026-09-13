import java.util.ArrayList;

public class Cart {
    private ArrayList<CartItem> cartItems = new ArrayList<>();
    private double discountAmount;
    public Cart() {
    }

    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }

    public void addItem(Product product, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getProductName() == product) {
                item.addProductCount(quantity);
                return;
            }
        }
        cartItems.add(new CartItem(product, quantity));
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double calculateTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getItemTotal();
        }
        return total;
    }

    public double calculateDiscount() {
        return calculateTotal() * discountAmount;
    }

    public double calculateVAT() {
        return (calculateTotal() * 0.075);
    }
}
