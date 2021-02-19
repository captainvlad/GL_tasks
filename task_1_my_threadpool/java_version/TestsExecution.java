import java.util.concurrent.TimeUnit;

public class TestsExecution {
    public static void main(String[] args) {
        ThreadPoolTest t = new ThreadPoolTest();
//        t.TasksEqualThreadsNumber(6);
//        t.TasksNotEqualThreadsNumber(10, 2);
//        t.TasksNotEqualThreadsNumber(5, 10);
    }
}

class ThreadPoolTest {
    private Runnable taskbuilder(int taskNumber) {
        Runnable taskToRet = new Runnable() {
            @Override
            public void run() {
                System.out.println("Task number " + taskNumber + " is executing");

                try{
                    TimeUnit.SECONDS.sleep(taskNumber);
                } catch (InterruptedException e){
                    System.out.println("Exception while working: continue working");
                }
                System.out.println("Task number " + taskNumber + " finished executing");
            }
        };
        return taskToRet;
    }

    public void TasksEqualThreadsNumber(int tasksAmount){
        MyThreadpool testingItem = new MyThreadpool(tasksAmount);

        for (int i = 0; i < tasksAmount; i++) {
            testingItem.execute(taskbuilder(i));
        }

        try {
            TimeUnit.SECONDS.sleep(20);
            testingItem.terminate();
        }catch (InterruptedException e){
            System.out.println("InterruptedException, reason: " + e.getMessage());
        }
    }

    public void TasksNotEqualThreadsNumber(int tasksAmount, int threadsAmount){
        MyThreadpool testingItem = new MyThreadpool(threadsAmount);

        for (int i = 0; i < tasksAmount; i++) {
            testingItem.execute(taskbuilder(i));
        }

        try {
            TimeUnit.SECONDS.sleep(100);
            testingItem.terminate();
        }catch (InterruptedException e){
            System.out.println("InterruptedException, reason: " + e.getMessage());
        }
    }
}