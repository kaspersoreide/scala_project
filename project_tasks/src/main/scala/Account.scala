import exceptions._

class Account(val bank: Bank, initialBalance: Double) {

    class Balance(var amount: Double) {}

    val balance = new Balance(initialBalance)

    // TODO
    // for project task 1.2: implement functions
    // for project task 1.3: change return type and update function bodies
    def withdraw(amount: Double): Either[Unit, String] = this.synchronized {
        if(amount < 0)
            return Right("A negative amount cannot be withdrawn.")
        else if(amount > getBalanceAmount)
            return Right("An amount larger than the balance cannot be withdrawn.")

        Left(this.balance.amount -= amount)
    }

    def deposit (amount: Double): Either[Unit, String] = this.synchronized {
        if(amount < 0)
            return Right("A negative amount cannot be deposited.")

        Left(this.balance.amount += amount)
    }
    
    def getBalanceAmount: Double = this.synchronized {
        this.balance.amount
    }

    def transferTo(account: Account, amount: Double) = this.synchronized {
        bank addTransactionToQueue (this, account, amount)
    }


}
