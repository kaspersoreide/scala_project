import java.util.concurrent.Executors

class Bank(val allowedAttempts: Integer = 3) {

    private val transactionsQueue: TransactionQueue = new TransactionQueue()
    private val processedTransactions: TransactionQueue = new TransactionQueue()
    private val executorContext = Executors.newFixedThreadPool(100)

    /** Adds new transaction to queue
     *  
     *  Creates a new instance of Transaction and pushes it to transactionsQueue,
     *  and creates a new thread to process transactions.
     */
    def addTransactionToQueue(from: Account, to: Account, amount: Double): Unit = {
        transactionsQueue.push(new Transaction(transactionsQueue, processedTransactions, from, to, amount, allowedAttempts))
        executorContext.submit(new Runnable {
            override def run(): Unit = {
                processTransactions
            }
        })
    }
    /** Goes through the queue and processes transactions
     *
     *  All transactions are popped from the queue and run. If a transaction is pending it is pushed back.
     *  The function calls itself until the queue is empty.
     */
    private def processTransactions: Unit = {
        val transaction = transactionsQueue.pop
        transaction.run()

        if(transaction.status == TransactionStatus.PENDING){
            transactionsQueue.push(transaction)
            executorContext.submit(new Runnable {
                override def run(): Unit = {
                    processTransactions
                }
            })
        }
        else {
            processedTransactions.push(transaction)
        }
    }

    def addAccount(initialBalance: Double): Account = {
        new Account(this, initialBalance)
    }

    def getProcessedTransactionsAsList: List[Transaction] = {
        processedTransactions.iterator.toList
    }

}