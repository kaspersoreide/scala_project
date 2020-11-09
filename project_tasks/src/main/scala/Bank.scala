package scala

import scala.annotation.tailrec

class Bank(val allowedAttempts: Integer = 3) {

  private val transactionsQueue: TransactionQueue = new TransactionQueue()
  private val processedTransactions: TransactionQueue = new TransactionQueue()

  def addTransactionToQueue(from: Account, to: Account, amount: Double): Unit = {
    val transaction: Transaction =
      new Transaction(transactionsQueue, processedTransactions, from, to, amount, allowedAttempts)
    transactionsQueue.push(transaction)
    Main.thread(transaction) // Questionable
    TransactionCrawler.wake()
  }

  def addAccount(initialBalance: Double): Account = {
    new Account(this, initialBalance)
  }

  def getProcessedTransactionsAsList: List[Transaction] = {
    processedTransactions.iterator.toList
  }

  /** Object that iterates through the transaction queue processing each transaction as needed.
   *
   * Right now, the object ensures that no more than one thread is processing the transaction queue, although many might
   * work on the transactions themselves; this can be changed if the bank needs increase.
   */
  private object TransactionCrawler {

    private var _isCrawling = false

    private def processTransactions(): Unit = {
      setIsCrawling(true)
      while (!transactionsQueue.isEmpty) {
        val transaction: Transaction = transactionsQueue.pop
        transaction.status match {
          case TransactionStatus.FAILED | TransactionStatus.SUCCESS =>
            processedTransactions.push(transaction)
            setIsCrawling(false)
            return
          case TransactionStatus.PENDING =>
            transactionsQueue.push(transaction)
        }
      }
    }
    // TOO
    // project task 2
    // Function that pops a transaction from the queue
    // and spawns a thread to execute the transaction.
    // Finally do the appropriate thing, depending on whether
    // the transaction succeeded or not

    def setIsCrawling(value: Boolean): Unit = this.synchronized({
      _isCrawling = value
    })

    def wake(): Unit = this.synchronized({
      if (!_isCrawling) {
        Main.thread({ processTransactions() })
      }
    })

  }

}
