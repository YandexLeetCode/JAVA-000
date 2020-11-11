package ThreadProject;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by yandex on 2020/11/11.
 */
public class Homework0305 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        Integer result = CompletableFuture.supplyAsync(() -> {
            return FibUtils.fib(20);
        }).join();
        System.out.println("异步计算结果为: " + result);
        System.out.println("使用时间 " + (System.currentTimeMillis() - start) + "ms");
    }
}
