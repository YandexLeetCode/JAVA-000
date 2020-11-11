package ThreadProject;

import java.util.concurrent.*;

/**
 * Created by yandex on 2020/11/11.
 */
public class Homework0304 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        FutureTask<Integer> task = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
               return FibUtils.fib(20);
            }
        });
        new Thread(task).start();
        System.out.println("异步计算结果为: " + task.get());
        System.out.println("使用时间 " + (System.currentTimeMillis() - start) + "ms");
    }
}
