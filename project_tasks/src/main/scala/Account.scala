import exceptions._

class Account(val bank: Bank, initialBalance: Double) {

    class Balance(var amount: Double) {}

    val balance = new Balance(initialBalance)

    // TODO
    // for project task 1.2: implement functions
    // for project task 1.3: change return type and update function bodies
    def withdraw(amount: Double): Either[Unit,String] = this.synchronized({
        if(amount<0)
            return Right("Cannot withraw negative amount")

        else if(amount>balance.amount)
            return Right("Cannot withraw more than your current account balance")

        Left(balance.amount -= amount)
    })

    def deposit(amount: Double): Either[Unit,String] = this.synchronized({
        if(amount<0)
            return Right("Cannot deposit negative amount")

        Left(balance.amount += amount)
    })
    def getBalanceAmount: Double       = balance.amount

    def transferTo(account: Account, amount: Double) = {
        bank addTransactionToQueue (this, account, amount)
    }


}
