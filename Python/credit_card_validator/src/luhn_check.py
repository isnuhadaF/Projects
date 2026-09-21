def get_card_type(card_number):
    """Return the card brand based on length and starting digits."""
    length = len(card_number)

    if length < 13 or length > 16:
        return "Invalid Length"

    if card_number.startswith("4"):
        return "Visa"
    elif card_number.startswith("5"):
        return "MasterCard"
    elif card_number.startswith("37"):
        return "American Express"
    elif card_number.startswith("6"):
        return "Discover"
    else:
        return "Unknown"


def is_valid(card_number):
    """Run the Mod 10 / Luhn check on the digit string."""
    length = len(card_number)
    if length < 13 or length > 16:
        return False

    sum_of_doubled = 0  # Step 1 + 2
    sum_of_odd = 0       # Step 3

    for i in range(length - 1, -1, -1):
        digit = int(card_number[i])
        position_from_right = length - i

        if position_from_right % 2 == 0:
            doubled = digit * 2
            if doubled > 9:
                doubled -= 9  # same as summing its two digits
            sum_of_doubled += doubled
        else:
            sum_of_odd += digit

    total = sum_of_doubled + sum_of_odd  # Step 4
    return total % 10 == 0                # Step 5


def main():
    card_number = input("Enter a credit card number: ").strip()

    print(f"Card type: {get_card_type(card_number)}")
    print(f"Status: {'Valid' if is_valid(card_number) else 'Invalid'}")


if __name__ == "__main__":
    main()