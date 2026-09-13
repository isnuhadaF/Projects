from CartItem import CartItem

class Cart:
    def __init__(self):
        self._cart_items = []
        self._discount_rate = 0  # settable per transaction, same as the reopened Java field

    def get_cart_items(self):
        return self._cart_items

    def set_discount_rate(self, discount_rate):
        self._discount_rate = discount_rate

    def add_item(self, product, quantity):
        for item in self._cart_items:
            if item.get_product_name() is product:  # identity check, matches Java's ==
                item.add_product_count(quantity)
                return
        self._cart_items.append(CartItem(product, quantity))

    def calculate_total(self):
        total = 0
        for item in self._cart_items:
            total += item.get_item_total()
        return total

    def calculate_discount(self):
        return self.calculate_total() * self._discount_rate

    def calculate_vat(self):
        return self.calculate_total() * 0.075