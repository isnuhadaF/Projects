# main.py
from Product import Product
from Cashier import Cashier
from Store import Store


def main():
    cashiers = [Cashier("Abraham", 101, "yourpassword")]
    store = Store("Semicolon Stores",
                  "Main Branch, 312 Herbert Macaulay Way, Sabo Yaba, Lagos.",
                  "03293828343", [], cashiers)

    # --- Login ---
    cashier_id = int(input("Cashier ID: "))
    password = input("Password: ")

    cashier = store.login(cashier_id, password)
    if cashier is None:
        print("Login failed.")
        return

    # --- Customer name ---
    customer_name = input("What is the customer's Name? ")
    customer = store.checkout(customer_name)

    # --- Item entry loop ---
    add_more = "yes"
    while add_more.lower() == "yes":
        item_name = input("What did the user buy? ")
        quantity = int(input("How many pieces? "))
        price = float(input("How much per unit? "))

        product = Product(item_name, price)
        cashier.add_item_to_cart(customer, product, quantity)

        add_more = input("Add more Items? ")

    # --- Discount ---
    discount_input = float(input("How much discount will he get? "))
    customer.get_customer_cart().set_discount_rate(discount_input / 100.0)

    # --- Payment + receipt ---
    amount_given = float(input("How much did the customer give to you? "))
    cashier.print_receipt(store, customer, amount_given)


if __name__ == "__main__":
    main()