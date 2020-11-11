package ThreadProject;

import java.util.concurrent.*;

/**
 * Created by yandex on 2020/11/11.
 */
public class Homework0303 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<Integer> result = executorService.submit(new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
               return FibUtils.fib(20);
            }
        });
        executorService.shutdown();

        System.out.println("异步计算结果为: " + result.get());
        System.out.println("使用时间 " + (System.currentTimeMillis() - start) + "ms");
    }
}
