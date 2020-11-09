// Task 2

// A

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
  def increaseCounter(): Unit = {
    counter += 1
  }

  /** Print this counter to console. */
  def print(): Unit = {
    println(counter)
  }

  /** Reset this counter to 0. */
  def reset(): Unit = {
    counter = 0
  }
}

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

// The output is either 0, 1 or 2. 2 is the most common output. We're talking one 1 or 0 out hundreds levels of
// rare. If there is problems reproducing anything but 2s with this code, one can try hitting the computer with a
// higher computational load simultaneously. This happens because the order of execution of threads is not, as least
// as it currently is written, determined in advance. Different orderings result in different behaviour. This is
// called race conditions. It is often problematic because the designer did not account for the possibility of such
// events unfolding. An example of this kind of error might be that a message is attempted sent without the content
// being initialized, which will either crash or lead to a nonsensical message.

// C

// The purpose of the `increaseCounter` method is to increment the field counter. Which means, the only way it "fails"
// is if you call it n times and counter does not increase by n. Making it thread-safe is not inherently connected to
// race conditions. Thread interference might cause incorrect behaviour. `counter += 1` is short for
// `counter = counter + 1`. The incremented value of `counter` is cached for a split second before it is saved into
// `counter`. If this is the case, two instances of the method `increaseCounter` starting execution at the same time
// might both cache the same initial `counter` value, increment it, and both write the same incremented value into
// `counter`. This would mean that running the method twice resulted in `counter` only being increased by one. If this
// is possible, the method is not thread-safe. The `counter` field is private, so can only be changed from within the
// `Task2` object. This means adding `this.synchronized` before the body of the `incrementCounter` method will make it
// thread-safe. Only one instance of the method can run at a time, leaving no chance for interleaving reading and
// assignments to and from `counter`.

// def increaseCounter(): Unit = this.synchronized({
//   counter += 1
// })

// D

// A deadlock is when two or more executions are blocked by each other in such a way that execution just stops. In
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

lazy val x: Int = {
  val t = thread { println(s"Initializing $x.") }
  t.start()
  t.join()
  1
}
x
println("Got past deadlock!! Finally!! 😭😭😭😭") // In practice unreachable