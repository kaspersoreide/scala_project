// Task 1

// A

/** Return an array containing first 50 integers */
def range() : Array[Int] = {
  var arr = new Array[Int](50)
  for( i <- 0 to 49)
  {
    arr(i) = i+1
  }
  arr
}

val array50: Array[Int] = range()
println(array50.mkString("Array(", ", ", ")"))

// B and C

object Sum {

  // B definition
  /** Returns the sum of the integers in iterable.
   *
   * This method employs a for loop to calculate the sum.
   *
   * @param iterable Iterable to get integers to sum from.
   */
  def loop(iterable: Iterable[Int]): Int = {
    var sum = 0
    for (x <- iterable) {
      sum += x
    }
    sum
  }

  // C definitions
  /** Returns the sum of the integers in iterator.
   *
   * This method employs recursion to calculate the sum.
   *
   * @param iterator Iterator to get integers to sum from.
   */
  def recursion(iterator: Iterator[Int]): Int = {
    if (iterator.hasNext) {
      iterator.next + recursion(iterator)
    } else {
      0
    }
  }

  /** Returns the sum of the integers in iterable.
   *
   * This method employs recursion to calculate the sum.
   *
   * @param iterable Iterable to get integers to sum from.
   */
  def recursion(iterable: Iterable[Int]): Int = {
    recursion(iterable.iterator)
  }

}

// B demonstration
printf(array50.mkString("", " + ", " = %d"), Sum.loop(array50))

// C demonstration
printf(array50.mkString("", " + ", " = %d"), Sum.recursion(array50))

// D
import scala.math.BigInt

val zero = BigInt(0)
val one = BigInt(1)

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

// Print 0-th to 10-th Fibonacci numbers
for (i <- 0 until 11) {
  printf("F%d: %d\n", i, fibonacci(BigInt(i)))
}

// D theory
// Int is a signed list of bits 32 long representing whole numbers. BigInt is theoretically an arbitrary size integer,
// and can therefore hold, or represent, much larger whole numbers. Implementations seem to have a limit of about 2 to
// the power of the max value of int. Operations on ints will typically perform better.