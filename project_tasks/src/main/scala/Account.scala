import exceptions._

class Account(val bank: Bank, initialBalance: Double) {

    class Balance(var amount: Double) {}

    val balance = new Balance(initialBalance)

    // TODO
    // for project task 1.2: implement functions
    // for project task 1.3: change return type and update function bodies
    def withdraw(amount: Double): Either[Unit, String] = {
        this.synchronized {
            if (amount > this.balance.amount || amount < 0) {
                return Right("Error: Invalid withdrawal (results in negative amount)!")
            }
            this.balance.amount -= amount
            Left()
        }
    }

    def deposit (amount: Double): Either[Unit, String] = {
        this.synchronized {
            if (amount <= 0) {
                return Right("Error: amount is 0 or negative")
            }
            this.balance.amount += amount   
            Left() 
        }   
    }
    def getBalanceAmount: Double = {
        this.balance.amount
    }

    def transferTo(account: Account, amount: Double) = {
        bank.addTransactionToQueue (this, account, amount)
    }
}
