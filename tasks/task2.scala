// Task 2

// A
object Task2{
  /** Return a new not started thread with the supplied body.
   *
   * @param body Code block body created thread.
   */
  def thread(body: => Unit): Thread = {
    new Thread {
      override def run(): Unit = body
    }
  }
  
  // B
  
  object Counter {
  
    private var counter: Int = 0
  
    /** Increase this counter by one. */
    def increaseCounter(): Unit = this.synchronized({
      counter += 1
    })
  
    /** Print this counter to console. */
    def print(): Unit = {
      println(counter)
    }
  
    /** Reset this counter to 0. */
    def reset(): Unit = {
      counter = 0
    }
  }
  
  
  
  // The function is non-deterministic. The execution order of the threads in this function is not determined in advance
  // causing a non-deterministic outcome. This means that the function can give different results for the same input.
  //
  // In this case we see a specific case called a race-condition. This happens when threads compete for the same resource.
  // Thread t1, t2, and t3 all use the same Counter object with no exectuion order, printing different results each time
  // depending on when the print-thread exeuted.
  
  // C
  
  // The purpose of the `increaseCounter` method is to increment the counter by 1. Which means, the only way it "fails"
  // is if you call it n times and counter does not increase by n. Making it thread-safe is not inherently connected to
  // race conditions. Thread interference might cause incorrect behaviour.
  //
  // The `counter` field is private, so can only be changed from within the `Task2` object. This means
  // adding `this.synchronized` before the body of the `incrementCounter` method will make it thread-safe. Only one
  // instance of the method can run at a time, leaving no chance for interleaving reading and assignments to and from
  // `counter`.
  
  
  // D
  
  // A deadlock is when two or more executions are blocked by each other in such a way that execution halts. In
  // many cases, this blocking is related to resource access. One way to avoid a deadlock is to introduce a ordering
  // of resources that all code executions lock resources in. There must be a cyclic dependence of blocking for deadlock.
  // For example, if the ordering a > b > c is introduced,
  //     Thread 1 lock a lock b
  //     Thread 2 lock b lock a
  // becomes
  //     Thread 1 lock a lock b
  //     Thread 2 lock a lock b
  // .
  // This change does not ask that the method reorder the order in which values are changed, just the order in which
  // they are locked or claimed. This way, no cycles of dependence can ever occur.
  def main(args: Array[String]): Unit = {
    for (_ <- 0 until 10) {
      Counter.reset()
      val t1 = thread({ Counter.increaseCounter() })
      val t2 = thread({ Counter.increaseCounter() })
      val t3 = thread({ Counter.print() })
    
      t1.start()
      t2.start()
      t3.start()
    
      t1.join()
      t2.join()
      t3.join()
      // Does not proceed until all the threads are done, so one iteration cannot affect the next.
    }

    /* D code */
    lazy val x: Int = {
    val t = thread { println(s"Initializing $x.") }
    t.start()
    t.join()
    1
    }
    x
    println("Got past deadlock!! Finally!! 😭😭😭😭") // In practice unreachable
  }
}