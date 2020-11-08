package scala

class Account(val bank: Bank, initialBalance: Double) {

  val balance: Balance = new Balance(initialBalance)

  /** Remove supplied amount from this account.
   *
   * Thread-safe. Cannot cause deadlock.
   *
   * @return Either a left with the unit if successful, or a right with an error message otherwise.
   */
  def withdraw(amount: Double): Either[Unit, String] = {

    if (amount < 0) {
      return Right("Amount to be withdrawn cannot be negative.")
    }

    if (balance < amount) {
      return Right("Amount to be withdrawn cannot be higher than account balance.")
    }

    Left(balance.changeBalance(-amount))

  }

  /** Add supplied amount to this account.
   *
   * Thread-safe. Cannot cause deadlock.
   *
   * @return Either a left with the unit if successful, or a right with an error message otherwise.
   */
  def deposit(amount: Double): Either[Unit, String] = {

    if (amount < 0) {
      return Right("Amount to be deposited cannot be negative.")
    }

    Left(balance.changeBalance(amount))

  }

  /** Returns amount in the balance of this account.
   *
   * Makes no changes to this account.
   */
  def getBalanceAmount: Double = {
    balance.amount
  }

  def transferTo(account: Account, amount: Double): Unit = {
    bank.addTransactionToQueue(this, account, amount)
  }

  class Balance(private var _amount: Double) {

    /** Returns whether this balance is less than amount.
     *
     * Makes no changes to this balance.
     */
    def <(amount: Double): Boolean = {
      this._amount < amount
    }

    /** Returns whether this balance is less than supplied balance.
     *
     * Makes no changes to this balance.
     */
    def <(balance: Balance): Boolean = {
      _amount < balance._amount
    }

    /** Add supplied amount to this balance.
     *
     * Amount can be negative. Thread-safe. Cannot cause deadlock.
     */
    def changeBalance(amount: Double): Unit = this.synchronized({
      this._amount += amount
    })

    /** Returns amount in this balance.
     *
     * Makes no changes to this balance.
     */
    def amount: Double = {
      _amount
    }
  }

}
