import exceptions._
import scala.collection.mutable

object TransactionStatus extends Enumeration {
  val SUCCESS, PENDING, FAILED = Value
}

class TransactionQueue {

    // TODO
    // project task 1.1
    // Add datastructure to contain the transactions
    var queueOfTransactions = new mutable.Queue[Transaction]()

    // Remove and return the first element from the queue
    def pop: Transaction = this.synchronized {
      queueOfTransactions.dequeue
    }

    // Return whether the queue is empty
    def isEmpty: Boolean = this.synchronized {
      queueOfTransactions.isEmpty
    }

    // Add new element to the back of the queue
    def push(t: Transaction): Unit = this.synchronized {
      queueOfTransactions.enqueue(t)
    }

    // Return the first element from the queue without removing it
    def peek: Transaction = this.synchronized {
      queueOfTransactions.front
    }

    // Return an iterator to allow you to iterate over the queue
    def iterator: Iterator[Transaction] = this.synchronized {
      queueOfTransactions.iterator
    }
}

class Transaction(val transactionsQueue: TransactionQueue,
                  val processedTransactions: TransactionQueue,
                  val from: Account,
                  val to: Account,
                  val amount: Double,
                  val allowedAttemps: Int) extends Runnable {

  var status: TransactionStatus.Value = TransactionStatus.PENDING
  var attempt = 0

  override def run: Unit = {

      def doTransaction() = {
        // TODO - project task 3
        // Extend this method to satisfy requirements.
        from withdraw amount match {
          // Withdrawal succeded
          case Left(unit) => {
            to deposit amount match {
              // Depositing failed
              case Right(string) => {
                // Deposit money back to sender
                from deposit amount
              }
              case Left(unit) => {
                // Depositing is a success
                status = TransactionStatus.SUCCESS 
              }
            }
          }
          case Right(string) => {
            // Witdrawal failed, nothing done
          }
        }
        attempt += 1
        if (attempt == allowedAttemps && status == TransactionStatus.PENDING) {
          status = TransactionStatus.FAILED
        }
      }

      // TODO - project task 3
      // make the code below thread safe
      if (status == TransactionStatus.PENDING) {
          doTransaction
          Thread.sleep(50) // you might want this to make more room for
                           // new transactions to be added to the queue
      }


    }
}
