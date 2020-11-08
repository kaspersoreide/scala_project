import exceptions._
import scala.collection.mutable._

object TransactionStatus extends Enumeration {
    val SUCCESS, PENDING, FAILED = Value
}

class TransactionQueue {

    // TODO
    // project task 1.1
    // Add datastructure to contain the transactions
    private val queue = new Queue[Transaction]()
    // Remove and return the first element from the queue
    def pop: Transaction = {
        this.synchronized {
            queue.dequeue
        }
    }

    // Return whether the queue is empty
    def isEmpty: Boolean = {
        queue.isEmpty
    }

    // Add new element to the back of the queue
    def push(t: Transaction): Unit = {
        this.synchronized {
            queue += t
        }
    }

    // Return the first element from the queue without removing it
    def peek: Transaction = {
        queue.front
    }

    // Return an iterator to allow you to iterate over the queue
    def iterator: Iterator[Transaction] = {
        queue.iterator
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
            // from withdraw amount
            // to deposit amount
            val withdrawn = from.withdraw(amount)
            withdrawn match {
                // withdrawal succeeded
                case Left(unit) => {
                    val deposited = to.deposit(amount)
                    deposited match{
                        // failed depositing
                        case Right(string) => {
                            // deposit money back into from-account
                            from.deposit(amount)
                        }
                        // succeeded depositing
                        case Left(unit) => {
                            status = TransactionStatus.SUCCESS
                        }
                    }
                }
                case Right(string) => {
                    // do nothing
                }
            }
            
            attempt += 1
            if (attempt == allowedAttemps) {
                status = TransactionStatus.FAILED
            }
        }

        if (status == TransactionStatus.PENDING) {
            this.synchronized {
                doTransaction
                Thread.sleep(100)
            }
        }
    }
}
