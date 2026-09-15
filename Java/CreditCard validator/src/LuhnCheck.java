public class LuhnCheck {
    private int card;

    public int cardNumber(String cardNUmber) {
        this.card = Integer.parseInt(cardNUmber);

        return this.card;
    }

    public boolean cardIsEven(String cardNumber) {
        if (cardNumber.length() % 2 == 0) {
            return true;
        } else return false;
    }

    public int doubleSecondDigitsFromRight(String cardNumber) {
        if (cardIsEven(cardNumber)) {
            for (int index = cardNumber.length() - 2; index >= 0; index--) {
                if (index % 2 == 0) {
                    Character.getNumericValue(cardNumber.charAt(index));
                }
            }
        }
    }

}
