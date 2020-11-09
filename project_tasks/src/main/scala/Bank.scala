package scala

class Bank(val allowedAttempts: Integer = 3) {

  private val transactionsQueue: TransactionQueue = new TransactionQueue()
  private val processedTransactions: TransactionQueue = new TransactionQueue()
  /** Adds new transaction to queue
   *  
   *  Creates a new instance of Transaction and pushes it to transactionsQueue,
   *  and creates a new thread to process transactions.
   */

  def addTransactionToQueue(from: Account, to: Account, amount: Double): Unit = this.synchronized({
    val transaction = new Transaction(transactionsQueue, processedTransactions, from, to, amount, allowedAttempts)
    transactionsQueue.push(transaction)
    Main.thread({
      processTransactions()
    })
  })

  def addAccount(initialBalance: Double): Account = {
    new Account(this, initialBalance)
  }

  def getProcessedTransactionsAsList: List[Transaction] = {
    processedTransactions.iterator.toList
  }
  
  /** Goes through the queue and processes transactions
   *
   *  All transactions are popped from the queue and run. If a transaction is pending it is pushed back.
   *  The function calls itself until the queue is empty.
   */
  private def processTransactions(): Unit = this.synchronized({
    if (transactionsQueue.isEmpty) {
      // Nothing to do, return.
      return
    }
    
    // Pop a transaction off the queue, and start a new thread executing it.
    val transaction = transactionsQueue.pop
    val transThread = new Thread(transaction)
    transThread.start()
    if (transaction.status == TransactionStatus.PENDING) {
      // Transaction must be processed.
      transactionsQueue.push(transaction)
    }
    else {
      // Transaction is processed.
      processedTransactions.push(transaction)
    } 
    // Recursive call so we get through the whole queue
    processTransactions()
  })
}
