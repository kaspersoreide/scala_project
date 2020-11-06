package scala

import exceptions._

class Account(val bank: Bank, initialBalance: Double) {

  class Balance(private var amount: Double) {
    /** Returns whether this balance is less than amount.
     *
     * Makes no changes to balance.
     */
    def <(amount: Double): Boolean = {
      this.amount < amount
    }

    /** Returns whether this balance is less than supplied balance.
     *
     * Makes no changes to balance.
     */
    def <(balance: Balance): Boolean = {
      amount < balance.amount
    }

    /** Add supplied amount to balance.
     *
     * Amount can be negative. Thread-safe. Cannot cause deadlock.
     */
    def changeBalance(amount: Double): Unit = this.synchronized({
      this.amount = amount
    })
  }

  val balance: Balance = new Balance(initialBalance)

  /** Remove supplied amount from this account.
   *
   * Thread-safe. Cannot cause deadlock.
   * @return Either a left with unit if successful, or a right with a error message otherwise.
   */
  def withdraw(amount: Double): Either[Unit, String] = {

    if (amount < 0) {
      return Right[Unit, String]("Amount to be withdrawn cannot be negative.")
    }

    if (balance < amount) {
      return Right[Unit, String]("Amount to be withdrawn cannot be higher than account balance.")
    }

    Left[Unit, String](balance.changeBalance(-amount))
    
  }

  /** Add supplied amount to this account.
   *
   * Thread-safe. Cannot cause deadlock.
   * @return Either a left with unit if successful, or a right with a error message otherwise.
   */
  def deposit (amount: Double): Either[Unit, String] = {

    if (amount < 0) {
      return Right[Unit, String]("Amount to be deposited cannot be negative.")
    }

    Left[Unit, String](balance.changeBalance(amount))

  }

  def getBalanceAmount: Double = ???

  def transferTo(account: Account, amount: Double): Unit = {
    bank addTransactionToQueue (this, account, amount)
  }


}
