package ThreadProject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yandex on 2020/11/11.
 */
public class Homework0306 {
    static Integer result = -1;
    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    result = FibUtils.fib(32);
                } finally {
                    LockSupport.park();
                }
            }
        });
        t1.start();
        Thread.sleep(10);
        LockSupport.unpark(new Thread());
        System.out.println("异步计算结果为: " + result);
        System.out.println("使用时间 " + (System.currentTimeMillis() - start) + "ms");
        System.exit(0);
    }
}
