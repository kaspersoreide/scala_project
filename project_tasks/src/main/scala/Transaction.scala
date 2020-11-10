package scala

import scala.collection.mutable

class TransactionQueue {

  // TODO
  // project task 1.1
  // Add datastructure to contain the transactions
  private val queue = new mutable.Queue[Transaction]()

  // Remove and return the first element from the queue
  def pop: Transaction = {
    this.synchronized {
      queue.dequeue
    }
  }

  // Return whether the queue is empty
  def isEmpty: Boolean = {
    this.synchronized {
      queue.isEmpty
    }
  }

  // Add new element to the back of the queue
  def push(t: Transaction): Unit = {
    this.synchronized {
      queue += t
    }
  }

  // Return the first element from the queue without removing it
  def peek: Transaction = {
    this.synchronized {
      queue.front
    }
  }

  // Return an iterator to allow you to iterate over the queue
  def iterator: Iterator[Transaction] = {
    this.synchronized {
      queue.iterator
    }
  }
}

class Transaction(val transactionsQueue: TransactionQueue,
                  val processedTransactions: TransactionQueue,
                  val from: Account,
                  val to: Account,
                  val amount: Double,
                  val allowedAttempts: Int) extends Runnable {

  var status: TransactionStatus.Value = TransactionStatus.PENDING
  var attempt = 0

  override def run: Unit = {
    /** Does the transaction
     *
     *  Withdraws amount from account "from" and deposits it into account "to"
     */
    def doTransaction(): Unit = {
      from withdraw amount match {
        // withdrawal succeeded
        case Left(_) => {
          to deposit amount match {
            // failed depositing
            case Right(string) => {
              // deposit money back into from-account
              from deposit amount
            }
            case Left(_) => {
              // Succeeded depositing,
              status = TransactionStatus.SUCCESS
            }
          }
        }
        case Right(string) => {
          // Do nothing, because withdrawal failed
        }
      }

      attempt += 1
      if (attempt == allowedAttempts && status == TransactionStatus.PENDING) {
        // too many attempts used
        status = TransactionStatus.FAILED
      }
    }

    if (status == TransactionStatus.PENDING) {
      this.synchronized {
        doTransaction()
        Thread.sleep(100)
      }
    }
  }
}

object TransactionStatus extends Enumeration {
  val SUCCESS, PENDING, FAILED = Value
}
