package ThreadC;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by yandex on 2020/10/31.
 */
public class ThreadCall implements Callable<String> {
    @Override
    public String call() throws Exception {
        return "Hello ThreadCallable";
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadCall threadCall = new ThreadCall();
        FutureTask futureTask = new FutureTask(threadCall);
        futureTask.run();
        System.out.println(futureTask.get());

    }
}
