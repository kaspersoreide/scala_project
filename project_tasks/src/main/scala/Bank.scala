class Bank(val allowedAttempts: Integer = 3) {

    private val transactionsQueue: TransactionQueue = new TransactionQueue()
    private val processedTransactions: TransactionQueue = new TransactionQueue()

    def addTransactionToQueue(from: Account, to: Account, amount: Double): Unit = {
        val transaction = new Transaction(transactionsQueue, processedTransactions, from, to, amount, allowedAttempts)
        transactionsQueue.push(transaction)
        val thread = new Thread(new Runnable {
            def run() {    
                processTransactions
            }
        })        
        thread.start
    }

    private def processTransactions: Unit = {
        if (transactionsQueue.isEmpty) {
            //nothing to do
            return
        }

        val transaction = transactionsQueue.pop
        val transThread = new Thread(transaction)
        transThread.start
        
        if (transaction.status == TransactionStatus.PENDING){ 
            transactionsQueue.push(transaction)
        }
        else {
            processedTransactions.push(transaction)
        }
        //recursive call so we get through the whole queue
        processTransactions
    }

    def addAccount(initialBalance: Double): Account = {
        new Account(this, initialBalance)
    }

    def getProcessedTransactionsAsList: List[Transaction] = {
        processedTransactions.iterator.toList
    }

}
