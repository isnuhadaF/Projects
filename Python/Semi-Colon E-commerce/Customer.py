from Cart import Cart

class Customer:
    def __init__(self, customer_name):
        self._customer_name = customer_name
        self._cart = Cart()

    def get_customer_cart(self):
        return self._cart

    def get_customer_name(self):
        return self._customer_name