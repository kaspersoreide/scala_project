package scala

object Main extends App {

  def thread(body: => Unit): Thread = {
    val t = new Thread {
      override def run(): Unit = body
    }
    t.start()
    t
  }

}