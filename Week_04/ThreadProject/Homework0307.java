package ThreadProject;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * Created by yandex on 2020/11/11.
 */
public class Homework0307 {
    static Integer result = -1;
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(new Fib(countDownLatch)).start();
        countDownLatch.await();
        System.out.println("异步计算结果为: " + result);
        System.out.println("使用时间 " + (System.currentTimeMillis() - start) + "ms");
    }

    static class Fib implements Runnable {
        private CountDownLatch latch;
        public Fib(CountDownLatch latch) {
            this.latch = latch;
        }
        @Override
        public void run() {
            try {
                result = FibUtils.fib(32);
            } finally {
                latch.countDown();
            }
        }
    }
}
