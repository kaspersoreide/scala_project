import scala.math.BigInt

object Task1 {
    def a() : Array[Int] = {
        var arr = new Array[Int](50)
        for( i <- 0 to 49)
        {
            arr(i) = i+1
        }
        arr
    }

    def b(arr : Array[Int]) : Int = {
        var sum = 0
        for( i <- arr)
        {
            sum += i
        }
        sum
    }

    def c(arr : Array[Int]) : Int = {
        if(arr.nonEmpty){
            arr(0) + c(arr.drop(1))
        }
        else{
            0
        }
    }

    def fibonacci (n:BigInt):BigInt = {
        if(n<=1)
             n
        else
            fibonacci(n-1) + fibonacci(n-2)
    }

    def main(args: Array[String]): Unit = {
        // Can be used to test functions
    }
}

/*
* The difference between Int and BigInt is BigInt can handle much bigger numbers than an Int's 2^32 bytes
*/