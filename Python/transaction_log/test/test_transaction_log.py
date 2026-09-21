import unittest
from datetime import datetime
from unittest import TestCase
from transaction_log import TransactionLog


class  test_transaction_log(unittest.TestCase):

    def test_that_transactions_is_displayed_upon_selection(self):
        starting_balance = 50000
        withdrawal_amount = 1000
        transactions = {}

        expected = transactions
        self.assertEqual(self, {}, TransactionLog.show_transactions(expected))

    def test_that_account_balance_increases_after_deposit(self):
        starting_balance = 1000
        deposit_amount = 1000
        transactions = {}
        expected = starting_balance + deposit_amount

        self.assertEqual(expected, TransactionLog.deposit(starting_balance, deposit_amount, transactions))

    def test_that_depositing_a_negative_balance_raises_error(self):
        starting_balance = 5000
        deposit_amount = -1000
        transactions = {}
        expected = "You cannot deposit negative amount. Deposit_amount must be positive."
        try:
            TransactionLog.deposit(starting_balance, deposit_amount, transactions)
            self.fail("Should have thrown an error")
        except ValueError:
            pass



    def test_that_depositing_a_non_integer_raises_error(self):
        starting_balance = 5000
        deposit_amount = "aaoada"
        transactions = {}

        expected = "Please enter a valid amount"
        try:
            TransactionLog.deposit(starting_balance, deposit_amount, transactions)
            self.fail("Should have thrown an error")
        except TypeError:
            pass

    def test_withdrawing_amount_greater_than_account_balance_raises_error(self):
        starting_balance = 500000
        withdrawal_amount = 5000000
        transactions = {}
        expected = "Amount requested exceeds account balance."
        try:
            TransactionLog.withdraw(withdrawal_amount, starting_balance, transactions)
            self.fail("Should have thrown an error")
        except ValueError:
            pass

    def test_withdrawing_negative_amount_raises_error(self):
        starting_balance = 500000
        withdrawal_amount = -500000
        transactions = {}
        expected = "You cannot withdraw negative amount. Withdrawal_amount must be positive."
        try:
            TransactionLog.withdraw(starting_balance, withdrawal_amount, transactions)
            self.fail("Should have thrown an error")
        except ValueError:
            pass

    def test_that_transactions_is_updated_after_deposit(self):
        starting_balance = 50000
        deposit_amount = 1000

        expected = {datetime.now(): deposit_amount}
        starting_balance, deposit_amount = TransactionLog.deposit(starting_balance, deposit_amount, expected)

    def test_that_transactions_is_updated_after_withdraw(self):
        transactions = {}
        starting_balance = 50000
        withdrawal_amount = 1000
        expected = {datetime.now(): withdrawal_amount}

        starting_balance, withdrawal_amount = TransactionLog.withdraw(withdrawal_amount, starting_balance, transactions)


