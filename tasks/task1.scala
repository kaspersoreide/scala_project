/* TASK 1 */

// A)
import scala.math.BigInt

def array_for_loop = {
  var arr:Array[Int] = new Array[Int](50)
  for(i <- 1 to 50) {
    arr(i-1) = i
  }
  arr
}

def sum_for_loop (array: Array[Int]) = {
  var sum = 0
  for (e <- array) {
    sum+=e
  }
  sum
}

def recursive_sum (arr: Array[Int]):Int = {
  if(arr.nonEmpty)
    arr(0) + recursive_sum(arr.drop(1))
  else
    0
}

def fibonacci (n:BigInt):BigInt = {
  if(n<=1)
    n
  else
    fibonacci(n-1) + fibonacci(n-2)
}
val b = BigInt(10)
print(fibonacci(b))

/*
* The difference between Int and BigInt is BigInt can handle much bigger numbers than an Int's 2^32 bytes
*/