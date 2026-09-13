class Cashier:
    def __init__(self, cashier_name, cashier_id, cashier_password):
        self._cashier_name = cashier_name
        self._cashier_id = cashier_id
        self._cashier_password = cashier_password

    def get_store_cashier(self):
        return self._cashier_name

    def get_cashier_id(self):
        return self._cashier_id

    def get_cashier_password(self):
        return self._cashier_password

    def add_item_to_cart(self, customer, product, product_count):
        customer.get_customer_cart().add_item(product, product_count)

    def print_receipt(self, store, customer, amount_given):
        cart = customer.get_customer_cart()
        sub_total = cart.calculate_total()
        discount = cart.calculate_discount()
        vat = cart.calculate_vat()
        bill_total = sub_total - discount + vat

        print(store.get_store_name())
        print(store.get_store_branch())
        print("LOCATION: " + store.get_store_branch())
        print("TEL: " + store.get_store_telephone())
        print("Cashier: " + self._cashier_name)
        print("Customer Name: " + customer.get_customer_name())
        print("=" * 55)
        print(f"{'ITEM':<15}{'QTY':>5}{'PRICE':>10}{'TOTAL(NGN)':>12}")
        print("-" * 55)

        for item in cart.get_cart_items():
            print(f"{item.get_product_name().get_name():<15}"
                  f"{item.get_product_count():>5}"
                  f"{item.get_product_name().get_price():>10.2f}"
                  f"{item.get_item_total():>12.2f}")

        print("-" * 55)
        print(f"{'Sub Total:':>40}{sub_total:>12.2f}")
        print(f"{'Discount:':>40}{discount:>12.2f}")
        print(f"VAT @ 7.50%:{vat:>29.2f}")
        print("=" * 55)
        print(f"{'Bill Total:':>40}{bill_total:>12.2f}")
        print("=" * 55)

        # TODO: handle amount_given < bill_total (reject/re-prompt, or show amount still owed)
        change = amount_given - bill_total
        print(f"Change: {change:.2f}")