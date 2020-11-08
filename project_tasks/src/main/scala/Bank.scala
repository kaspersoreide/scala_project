class Bank(val allowedAttempts: Integer = 3) {

    private val transactionsQueue: TransactionQueue = new TransactionQueue()
    private val processedTransactions: TransactionQueue = new TransactionQueue()

    def addTransactionToQueue(from: Account, to: Account, amount: Double): Unit = {
        val transaction:Transaction = new Transaction(
                                                transactionsQueue,
                                                processedTransactions,
                                                from,
                                                to,
                                                amount,
                                                this.allowedAttempts
        )
        println("COCK")
        transactionsQueue.push(transaction)
        val thread:Thread = new Thread {
            override def run(): Unit = processTransactions
        }
        thread.start()
    }

                                                // TODO
                                                // project task 2
                                                // create a new transaction object and put it in the queue
                                                // spawn a thread that calls processTransactions

    private def processTransactions: Unit = {
        val transaction:Transaction = transactionsQueue.pop
        val thread:Thread = new Thread{
            override def run(): Unit = transaction.run()
        }
        thread.start()

        if(transaction.status == TransactionStatus.PENDING) {
            transactionsQueue.push(transaction)
            println("peeeeeeenis")
        }
        else {
            processedTransactions.push(transaction)
        }
    }
                                                // TOO
                                                // project task 2
                                                // Function that pops a transaction from the queue
                                                // and spawns a thread to execute the transaction.
                                                // Finally do the appropriate thing, depending on whether
                                                // the transaction succeeded or not

    def addAccount(initialBalance: Double): Account = {
        new Account(this, initialBalance)
    }

    def getProcessedTransactionsAsList: List[Transaction] = {
        processedTransactions.iterator.toList
    }

}
