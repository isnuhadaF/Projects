from Customer import Customer

class Store:
    def __init__(self, store_name, store_branch_address, store_telephone, products, cashiers):
        self._store_name = store_name
        self._store_branch_address = store_branch_address
        self._store_telephone = store_telephone
        self._products = products
        self._cashiers = cashiers

    def get_store_name(self):
        return self._store_name

    def get_store_branch(self):
        return self._store_branch_address

    def get_store_telephone(self):
        return self._store_telephone

    def login(self, cashier_id, cashier_password):
        for cashier in self._cashiers:
            if cashier.get_cashier_id() == cashier_id and cashier.get_cashier_password() == cashier_password:
                return cashier
        return None

    def checkout(self, customer_name):
        return Customer(customer_name)