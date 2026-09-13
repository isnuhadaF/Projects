import java.util.ArrayList;

public class Cashier {
    private final String cashierName;
    private final int CashierID;
    private final String cashierPassword;

    public Cashier(String cashierName, int CashierID, String cashierPassword) {
        this.cashierName = cashierName;
        this.CashierID = CashierID;
        this.cashierPassword = cashierPassword;
    }

    public String getStoreCashier() {
        return cashierName;
    }

    public int getCashierID() {
        return CashierID;
    }

    public String getCashierPassword() {
        return cashierPassword;
    }

    public void addItemToCart(Customer customer, Product product, int productCount) {
        customer.getCustomerCart().addItem(product, productCount);
    }

    void printReceipt(Store store, Customer customer, double amountGiven) {
        Cart cart = customer.getCustomerCart();
        double subTotal = cart.calculateTotal();
        double discount = cart.calculateDiscount();
        double vat = cart.calculateVAT();
        double billTotal = subTotal - discount + vat;

        System.out.println(store.getStoreName());
        System.out.println(store.getStoreBranch());
        System.out.println("LOCATION: " + store.getStoreBranch());
        System.out.println("TEL: " + store.getStoreTelephone());
        System.out.println("Cashier: " + cashierName);
        System.out.println("Customer Name: " + customer.getCustomerName());
        System.out.println("=".repeat(55));
        System.out.printf("%-15s%5s%10s%12s%n", "ITEM", "QTY", "PRICE", "TOTAL(NGN)");
        System.out.println("-".repeat(55));

        for (CartItem item : cart.getCartItems()) {
            System.out.printf("%-15s%5d%10.2f%12.2f%n",
                    item.getProductName().getName(), item.getProductCount(),
                    item.getProductName().getPrice(), item.getItemTotal());
        }

        System.out.println("-".repeat(55));
        System.out.printf("%40s%12.2f%n", "Sub Total:", subTotal);
        System.out.printf("%40s%12.2f%n", "Discount:", discount);
        System.out.printf("VAT @ %.2f%%:%29.2f%n", 7.5, vat);
        System.out.println("=".repeat(55));
        System.out.printf("%40s%12.2f%n", "Bill Total:", billTotal);
        System.out.println("=".repeat(55));

        // TODO: handle amountGiven < billTotal (reject/re-prompt, or show amount still owed)
        double change = amountGiven - billTotal;
        System.out.printf("Change: %.2f%n", change);
    }

}
