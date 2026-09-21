from datetime import datetime

class TransactionLog:

    def deposit(starting_balance, deposit_amount, transactions: dict):
        if not isinstance(deposit_amount, (int, float)):
            raise TypeError("Please enter a valid amount")
        if float(deposit_amount).is_integer():
            if deposit_amount > 0:
                transactions[datetime.now()] = deposit_amount
                return starting_balance + deposit_amount, transactions
            else:
                raise ValueError("You cannot deposit negative amount. Deposit_amount must be positive.")
        else:
            raise TypeError("Please enter a valid amount")

    def withdraw(withdrawal_amount, account_balance, transactions: dict):
        if not isinstance(withdrawal_amount, (int, float)):
            raise TypeError("Please enter a valid amount")
        if float(withdrawal_amount).is_integer():
            if withdrawal_amount < account_balance:
                transactions[datetime.now()] = withdrawal_amount
                return account_balance - withdrawal_amount, transactions
            else:
                raise ValueError("Amount requested exceeds account balance.")
        else:
            raise TypeError("Please enter a valid amount")

    def show_transactions(transactions: dict):
        return transactions

    def main():
        transactions = {}
        account_balance = 1000
        in_main_menu = True
        while in_main_menu:
            main_menu = """
                =========================================
                    Welcome to the transaction log
                =========================================
                            Select an Option:
                (1) Deposit
                (2) Withdraw
                (3) Show Transactions
                (4) Exit
            """
            print(main_menu)
            try:
                main_menu_choice = int(input("Enter your choice: "))
            except ValueError:
                print("Please enter a valid number")
                continue

            match main_menu_choice:
                case 1:
                    print("Enter the amount you want to deposit:")
                    deposit_amount = int(input())
                    account_balance, transactions = TransactionLog.deposit(account_balance, deposit_amount, transactions)
                case 2:
                    print("Enter the amount you want to withdraw:")
                    withdrawal_amount = int(input())
                    account_balance, transactions = TransactionLog.withdraw(withdrawal_amount, account_balance, transactions)
                case 3:
                    print(TransactionLog.show_transactions(transactions))
                case 4:
                    in_main_menu = False
                case _:
                    print("Please select a valid option (1-4)")

if __name__ == "__main__":
    TransactionLog.main()