object Task2 {

  // A

  def thread(body: => Unit): Thread = {
    new Thread {
      override def run(): Unit = body
    }
  }

  // B

  private var counter: Int = 0

  def increaseCounter(): Unit = {
    Thread.sleep(1)
    // Without this sleep, the thread execution order was basically deterministic. Notice the print function has the
    // same sleep. The JVM relies heavily on the OS thread implementation, so maybe I have some setting somewhere that
    // results in this behaviour. Sleeping the same duration in all three threads does introduce randomness to the
    // execution order though.

    counter += 1
  }

  def print(): Unit = {
    Thread.sleep(1)
    println(counter)
  }

  def main(args: Array[String]): Unit = {
    println()
    for (_ <- 0 until 10) {
      counter = 0
      val t1 = thread({ increaseCounter() })
      val t2 = thread({ increaseCounter() })
      val t3 = thread({ print() })

      t1.start()
      t2.start()
      t3.start()

      t1.join()
      t2.join()
      t3.join()
      // Does not proceed until all the threads are done, so one iteration cannot affect the next.
    }

    // The output is either 0, 1 and 2; 2 is the most common output. This happens because the order of execution of
    // threads is not, as least as it currently is written, determined in advance. Different orderings result in different
    // behaviour. This is called race conditions.

    // C

    // The purpose of the `increaseCounter` method is to increment the field counter. Which means, the only way it "fails"
    // is if you call it n times and counter does not increase by n. Making it thread-safe is not inherently connected to
    // race conditions. Thread interference might cause incorrect behaviour. `counter += 1` is short for
    // `counter = counter + 1`. My knowledge of the inner workings of the JVM is not good enough to determine if the
    // incremented value of `counter` is cached for a split second before it is saved into `counter`. If this is the case,
    // Two instances of the method `increaseCounter` starting execution at the same time might both cache the same initial
    // `counter` value, increment it, and both write the same incremented value into `counter`. This would mean that
    // running the method twice resulted in `counter` only being increased by one. If this is possible, the method is not
    // thread-safe. The `counter` field is private, so can only be changed from within the `Task2` object. This means
    // adding `this.synchronized` before the body of the `incrementCounter` method will make it thread-safe. Only one
    // instance of the method can run at a time, leaving no chance for interleaving reading and assignments to and from
    // `counter`.

    // def increaseCounter(): Unit = this.synchronized({
    //   counter += 1
    // })

    // D

    // A deadlock is when two or more executions are blocked by each other in such a way that execution just stops. In
    // many cases, this blocking is related to resource access.

    lazy val x: Int = {
      val t = thread { println(s"Initializing $x.") }
      t.start()
      t.join()
      1
    }
    x
  }
}