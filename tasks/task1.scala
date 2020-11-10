object Task1 {

  // A

  val array50: Array[Int] = range()
  val zero = BigInt(0)
  val one = BigInt(1)


  // B

  /** Return an array containing first 50 integers */
  def range(): Array[Int] = {
    var arr = new Array[Int](50)
    for (i <- 0 to 49) {
      arr(i) = i + 1
    }
    arr
  }

  def demonstrateA(): Unit = {
    println(array50.mkString("Array(", ", ", ")"))
  }

  // C

  /** Returns the sum of the integers in iterable.
   *
   * This method employs a for loop to calculate the sum.
   *
   * @param iterable Iterable to get integers to sum from.
   */
  def sumLoop(iterable: Iterable[Int]): Int = {
    var sum = 0
    for (x <- iterable) {
      sum += x
    }
    sum
  }

  def demonstrateB(): Unit = {
    printf(array50.mkString("", " + ", " = %d\n"), sumLoop(array50))
  }

  /** Returns the sum of the integers in iterator.
   *
   * This method employs recursion to calculate the sum.
   *
   * @param iterator Iterator to get integers to sum from.
   */
  def sumRecursion(iterator: Iterator[Int]): Int = {
    if (iterator.hasNext) {
      iterator.next + sumRecursion(iterator)
    } else {
      0
    }
  }

  // D

  import scala.math.BigInt

  /** Returns the sum of the integers in iterable.
   *
   * This method employs recursion to calculate the sum.
   *
   * @param iterable Iterable to get integers to sum from.
   */
  def sumRecursion(iterable: Iterable[Int]): Int = {
    sumRecursion(iterable.iterator)
  }

  def demonstrateC(): Unit = {
    printf(array50.mkString("", " + ", " = %d\n"), sumRecursion(array50))
  }

  /** Returns the n-th Fibonacci number.
   *
   * @param n Index of Fibonacci number to return.
   */
  def fibonacci(n: BigInt): BigInt = {
    n match {
      case `zero` => zero
      case `one` => one
      case _ => fibonacci(n - 2) + fibonacci(n - 1)
    }
  }

  def demonstrateD(): Unit = {
    // Print 0-th to 10-th Fibonacci numbers
    for (i <- 0 until 11) {
      printf("F%d: %d\n", i, fibonacci(BigInt(i)))
    }
  }


  // D theory
  // Int is a signed list of bits 32 long representing whole numbers. BigInt is theoretically an arbitrary size integer,
  // and can therefore hold, or represent, much larger whole numbers. Implementations seem to have a limit of about 2 to
  // the power of the max value of int. Operations on ints will typically perform better.

  def main(args: Array[String]): Unit = {
    demonstrateA()
    demonstrateB()
    demonstrateC()
    demonstrateD()
  }

}