class Product:
    def __init__(self, product_name, product_price):
        self._product_name = product_name
        self._product_price = product_price

    def get_name(self):
        return self._product_name

    def get_price(self):
        return self._product_price