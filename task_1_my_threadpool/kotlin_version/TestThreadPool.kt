import java.util.concurrent.TimeUnit

class TestThreadPool{
    fun threadsTasksEqual(size: Int) {
        val t = MyThreadPool(size)

        for (i in 0 until size){
            t.execute(MyTask(i.toLong()))
        }
        TimeUnit.SECONDS.sleep(20)
        t.terminate()
    }

    fun threadsTasksNotEqual(threadsNumber: Int, tasksNumber: Int) {
        val t = MyThreadPool(threadsNumber)

        for (i in 0 until tasksNumber) {
            t.execute( MyTask(i.toLong() ))
        }
        TimeUnit.SECONDS.sleep(20)
        t.terminate()
    }
}

class MyTask(val number: Long):Runnable{
    override fun run() {
        println("Task number $number started executing")
        TimeUnit.SECONDS.sleep(number)
        println("Task number $number finished executing")
    }
}

fun main(){
    val test = TestThreadPool()
//    test.threadsTasksEqual(6)
//    test.threadsTasksNotEqual(2, 4)
//    test.threadsTasksNotEqual(4, 2)

}