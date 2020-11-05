object Task2 {
    /* a) */
    def make_thread(function: () => Unit) = {
        val thread = new Thread(new Runnable {
            def run(){
                function()
            }
        })
        thread
    }
    /* b) */
    private var counter: Int = 0
    def increaseCounter(): Unit = {
        counter += 1
    }

    def print_counter() {
        println(counter)
    }

    /* c) */
    import java.util.concurrent.atomic.AtomicInteger
    private var atomic_counter = new AtomicInteger(0)
    def increaseCounterAtomic(): Unit = {
        atomic_counter.incrementAndGet()
    }

    def print_counter_atomic() {
        println(atomic_counter.get())
    }

    def main(args: Array[String]) {
        /* Test a) */
        def helloworld() {
            println("hello world!")
        }
        val t = make_thread(helloworld)
        t.start

        /* Test b) */
        val t1 = make_thread(increaseCounter)
        val t2 = make_thread(increaseCounter)
        val t3 = make_thread(print_counter)
        t1.start
        t2.start
        t3.start
        /*
         * We can see that sometimes 1 is printed and sometimes 2. This
         */

        /* Test c) */
        val t4 = make_thread(increaseCounterAtomic)
        val t5 = make_thread(increaseCounterAtomic)
        val t6 = make_thread(print_counter_atomic)
        t4.start
        t5.start
        t6.start
    }
}

