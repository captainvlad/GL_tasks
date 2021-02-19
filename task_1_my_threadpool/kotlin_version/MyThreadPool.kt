import java.util.concurrent.LinkedBlockingQueue

class MyThreadPool(threadsNumber: Int) {
    val mQueue: LinkedBlockingQueue<Runnable> = LinkedBlockingQueue<Runnable>()
    private val mLock = Object()
    var mWorkers: Array<MyThread>

    init{
        mWorkers = emptyArray()

        for (x in 0 until threadsNumber) {
            mWorkers += MyThread()
            mWorkers[x].start()
        }
    }

    fun execute(task: Runnable) {
        synchronized(mLock){
            mQueue.add(task)
            mLock.notify()
        }
    }

    inner class MyThread:Thread() {
        override fun run(){
            try {
                while (!interrupted()) {
                    var task: Runnable

                    synchronized(mLock) {
                        if (mQueue.isEmpty()) {
                            mLock.wait()
                        }
                        task = mQueue.poll()
                    }
                    task.run()
                }
            } catch (e: InterruptedException) {
                return
            }
        }
    }

    fun terminate() {
        mWorkers.forEach {it.interrupt()}
    }
}