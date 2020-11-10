import java.util.concurrent.Executors

class Bank(val allowedAttempts: Integer = 3) {

    private val transactionsQueue: TransactionQueue = new TransactionQueue()
    private val processedTransactions: TransactionQueue = new TransactionQueue()
    private val executorContext = Executors newFixedThreadPool(100)

    def addTransactionToQueue(from: Account, to: Account, amount: Double): Unit = {
        // TODO
        // project task 2
        // create a new transaction object and put it in the queue
        // spawn a thread that calls processTransactions

        transactionsQueue push new Transaction(transactionsQueue, processedTransactions, from, to, amount, allowedAttempts)
        executorContext submit new Runnable {
            override def run(): Unit = {
                processTransactions
            }
        }
    }

    private def processTransactions: Unit = {
        // TOO
        // project task 2
        // Function that pops a transaction from the queue
        // and spawns a thread to execute the transaction.
        // Finally do the appropriate thing, depending on whether
        // the transaction succeeded or not

        val transaction = transactionsQueue.pop
        transaction run

        if(transaction.status == TransactionStatus.PENDING){
            transactionsQueue push transaction
            executorContext submit new Runnable {
                override def run(): Unit = {
                    processTransactions
                }
            }
        }
        else {
            processedTransactions push transaction
        }
    }

    def addAccount(initialBalance: Double): Account = {
        new Account(this, initialBalance)
    }

    def getProcessedTransactionsAsList: List[Transaction] = {
        processedTransactions.iterator.toList
    }

}