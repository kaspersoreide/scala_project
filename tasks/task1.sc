// Task 1

// A

def range(start: Int, end: Int): Array[Int] = {
  val array: Array[Int] = new Array[Int](end)
  for (i: Int <- start until end) {
    array(i) = i
  }
  array
}

val array50: Array[Int] = range(0, 50 + 1)

// B and C

object sum {

  // B definition
  def loop(iterable: Iterable[Int]): Int = {
    var sum = 0
    for (x <- iterable) {
      sum += x
    }
    sum
  }

  // C definitions
  def recursion(iterator: Iterator[Int]): Int = {
    if (iterator.hasNext) {
      iterator.next + recursion(iterator)
    } else {
      0
    }
  }

  def recursion(iterable: Iterable[Int]): Int = {
    recursion(iterable.iterator)
  }

}

// B demonstration
printf(array50.mkString("", " + ", " = %d"), sum.loop(array50))

// C demonstration
printf(array50.mkString("", " + ", " = %d"), sum.recursion(array50))

// D

import scala.math.BigInt

val zero = BigInt(0)
val one = BigInt(1)
def Fibonacci(n: BigInt): BigInt = {
  n match {
    case `zero` => zero
    case `one` => one
    case _ => Fibonacci(n - 2) + Fibonacci(n - 1)
  }
}

for (x <- 0 until 11) {
  println(Fibonacci(BigInt(x)))
}

// D theory
// int is a signed list of bits 32 long representing whole numbers.
// BigInt is theoretically an arbitrary size integer, but the
// implementations seem to have a limit of about 2 to the power of the
// max value of int. Operations on ints will typically perform better