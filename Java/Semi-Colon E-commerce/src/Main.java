import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ArrayList<Cashier> cashiers = new ArrayList<>();
        cashiers.add(new Cashier("Abraham", 101, "yourpassword"));

        Store store = new Store("Semicolon Stores",
                "Main Branch, 312 Herbert Macaulay Way, Sabo Yaba, Lagos.",
                "03293828343", new ArrayList<>(), cashiers);

        // --- Login (unchanged) ---
        System.out.print("Cashier ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Cashier cashier = store.login(id, password);
        if (cashier == null) {
            System.out.println("Login failed.");
            return;
        }

        // --- Customer name ---
        System.out.print("What is the customer's Name? ");
        String customerName = scanner.nextLine();
        Customer customer = store.checkout(customerName);

        // --- Item entry loop, matching the screenshot exactly ---
        String addMore = "yes";
        while (addMore.equalsIgnoreCase("yes")) {
            System.out.print("What did the user buy? ");
            String itemName = scanner.nextLine();

            System.out.print("How many pieces? ");
            int quantity = Integer.parseInt(scanner.nextLine());

            System.out.print("How much per unit? ");
            double price = Double.parseDouble(scanner.nextLine());

            Product product = new Product(itemName, price);
            cashier.addItemToCart(customer, product, quantity);

            System.out.print("Add more Items? ");
            addMore = scanner.nextLine();
        }

        // --- Discount, entered directly as the screenshot shows ---
        System.out.print("How much discount will he get? ");
        double discountInput = Double.parseDouble(scanner.nextLine());
        customer.getCustomerCart().setDiscountAmount(discountInput / 100.0);

        // --- Payment + receipt ---
        System.out.print("How much did the customer give to you? ");
        double amountGiven = Double.parseDouble(scanner.nextLine());
        cashier.printReceipt(store, customer, amountGiven);
    }
}