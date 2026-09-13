import java.util.ArrayList;

public class Store {
    private final String storeName;
    private final String storeBranchAddress;
    private final String storeTelephone;
    private final ArrayList<Product> products;
    private final ArrayList<Cashier> cashiers;

    public Store(String storeName,  String storeBranchAddress, String storeTelephone, ArrayList<Product> products, ArrayList<Cashier> cashiers) {
        this.storeName = storeName;
        this.storeBranchAddress = storeBranchAddress;
        this.storeTelephone = storeTelephone;
        this.products = products;
        this.cashiers = cashiers;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreBranch() {
        return storeBranchAddress;
    }


    public String getStoreTelephone() {
        return storeTelephone;
    }

    public Cashier login(int cashierID, String cashierPassword) {
        for (Cashier cashier : cashiers) {
            if (cashier.getCashierID() == cashierID && cashier.getCashierPassword().equals(cashierPassword)) {
                return cashier;
            }
        }
        return null;
    }

    public Customer checkout(String customerName) {
        return new Customer(customerName);
    }

}
