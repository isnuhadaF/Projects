public class LuhnCheck {

        public static String getCardType(String cardNumber) {
            int length = cardNumber.length();

            if (length < 13 || length > 16) {
                return "Invalid Length";
            }

            if (cardNumber.startsWith("4")) {
                return "Visa";
            } else if (cardNumber.startsWith("5")) {
                return "MasterCard";
            } else if (cardNumber.startsWith("37")) {
                return "American Express";
            } else if (cardNumber.startsWith("6")) {
                return "Discover";
            } else return "Unknown";

        }

        public static String getCardLength(String cardNumber) {
            if (cardNumber.length() == 16) {
                return "16 digits";
            } else if (cardNumber.length() == 15) {
                return "15 digits";
            } else return null;

        }

        public static boolean isValid(String cardNumber) {
            int length = cardNumber.length();
            if (length < 13 || length > 16) {
                return false;
            }

            int sumOfDoubled = 0;
            int sumOfOdd = 0;

            for (int i = length - 1; i >= 0; i--) {
                int digit = cardNumber.charAt(i) - '0';
                int positionFromRight = length - i;

                if (positionFromRight % 2 == 0) {
                    int doubled = digit * 2;
                    if (doubled > 9) {
                        doubled = doubled - 9;
                    }
                    sumOfDoubled += doubled;
                } else  sumOfOdd += digit;

            }

            int total = sumOfDoubled + sumOfOdd;
            return total % 10 == 0;
        }
}

