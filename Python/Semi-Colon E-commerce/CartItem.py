class CartItem:
    def __init__(self, product, product_count):
        self._product = product
        self._product_count = product_count

    def get_product_name(self):
        return self._product

    def get_product_count(self):
        return self._product_count

    def add_product_count(self, product_quantity):
        self._product_count += product_quantity

    def get_item_total(self):
        return self._product.get_price() * self._product_count

    def get_item_discount(self, discount_rate):
        return self._product.get_price() * discount_rate