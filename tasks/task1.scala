object Task1 {
    def a() : Array[Int] = {
        val result = new Array[Int](50)
        for(i <- 0 until 50) {
            result(i) = i + 1
        }
        result
    }
}