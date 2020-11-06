package scala

import exceptions._
import scala.collection.mutable

object TransactionStatus extends Enumeration {
  val SUCCESS, PENDING, FAILED = Value
}

class TransactionQueue {

  /** Backing queue of this transaction queue. */
  private val transactions: mutable.Queue[Transaction] = new mutable.Queue[Transaction]()

  /** Remove and return the first element from the queue. */
  def pop: Transaction = this.synchronized({
    transactions.dequeue()
  })

  /** Return whether the queue is empty. */
  def isEmpty: Boolean = {
    transactions.isEmpty
  }

  /** Add new element to the back of the queue. */
  def push(t: Transaction): Unit = this.synchronized({
    transactions.enqueue(t)
  })

  /** Return the first element from the queue without removing it. */
  def peek: Transaction = {
    transactions.front
  }

  /** Return an iterator to allow you to iterate over the queue. */
  def iterator: Iterator[Transaction] = {
    transactions.iterator
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

  override def run(): Unit = {

    def doTransaction(): Unit = {
      // TODO - project task 3
      // Extend this method to satisfy requirements.
      from withdraw amount
      to deposit amount
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
