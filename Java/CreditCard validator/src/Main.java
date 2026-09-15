import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter a credit card number: ");
        String cardNumber = input.nextLine().trim();

        String carLength = LuhnCheck.getCardLength(cardNumber);
        String cardType = LuhnCheck.getCardType(cardNumber);
        boolean valid = LuhnCheck.isValid(cardNumber);

        System.out.println("Card type: " + cardType);
        System.out.println("Status: " + (valid ? "Valid" : "Invalid"));
        System.out.println("Length: " + carLength);

    }
}