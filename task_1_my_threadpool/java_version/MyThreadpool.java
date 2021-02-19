import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MyThreadpool {
    private final MyThread[] mWorkers;
    private final BlockingQueue<Runnable> mQueue = new LinkedBlockingQueue<>();

    public MyThreadpool(int threadsNumber) {
        if (threadsNumber > 0) {
            mWorkers = new MyThread[threadsNumber];
        } else {
            System.out.println("Invalid value for threadsNumber argument: " + threadsNumber);
            mWorkers = null;
            return;
        }

        for (int i = 0; i < threadsNumber; i++) {
            mWorkers[i] = new MyThread();
            mWorkers[i].start();
        }
    }

    public void execute(Runnable task) {
        synchronized (mQueue){
            mQueue.add(task);
            mQueue.notify();
        }
    }

    public void terminate(){
        for (MyThread worker:mWorkers) {
            worker.interrupt();
        }
    }

    private class MyThread extends Thread {
        public void run(){
            try{
                while (true){
                    Runnable task;

                    synchronized (mQueue){
                        if (mQueue.isEmpty()) {
                            mQueue.wait();
                        }
                        task = mQueue.poll();
                    }
                    if (task != null)
                        task.run();
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}