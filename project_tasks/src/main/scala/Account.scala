package scala

class Account(val bank: Bank, initialBalance: Double) {

  val balance = new Balance(initialBalance)

  /** Remove supplied amount from this account.
   *
   * @return Either a left with the unit if successful, or a right with an error message otherwise.
   */
  def withdraw(amount: Double): Either[Unit, String] = this.synchronized {
    if (amount > this.balance.amount || amount < 0) {
      return Right("Error: Invalid withdrawal (results in negative amount)!")
    }
    Left(this.balance.amount -= amount)
  }

  /** Add supplied amount to this account.
   *
   * @return Either a left with the unit if successful, or a right with an error message otherwise.
   */
  def deposit(amount: Double): Either[Unit, String] = this.synchronized({
    if (amount <= 0) {
      return Right("Error: amount is 0 or negative")
    }
    Left(this.balance.amount += amount)
  })

  /** Returns amount in the balance of this account.
   *
   * Makes no changes to this account.
   */
  def getBalanceAmount: Double = {
    this.balance.amount
  }

  def transferTo(account: Account, amount: Double): Unit = {
    bank.addTransactionToQueue(this, account, amount)
  }

  class Balance(var amount: Double) {}
}
