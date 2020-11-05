import scala.concurrent.Future

// Task A
def initialize_tread(func:()=>Unit):Thread = {
  val f = new Thread {
    override def run() = func()
  }
  f
}


// Task B
private var counter: Int = 0

def increaseCounter(): Unit = {
  counter += 1
  Thread.currentThread().join()
}
def printCounter():Unit = {
  print(counter)
}

val t1 = initialize_tread(()=>increaseCounter)
val t2 = initialize_tread(()=>increaseCounter)
val t3 = initialize_tread(()=>printCounter)

t1 start()
t2 start()
t3 start()


// Non-deterministic outcome

// Deadlock is when the program is in a state where two threads are waiting for eachother to finish executing
