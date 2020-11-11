package ThreadProject;

import java.util.concurrent.*;

/**
 * Created by yandex on 2020/11/11.
 */
public class Homework0309 {
    static Integer result = -1;
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        Semaphore semaphore = new Semaphore(1);
       Fib fib =  new Fib(semaphore);
       fib.start();
       fib.join();

        System.out.println("异步计算结果为: " + result);
        System.out.println("使用时间 " + (System.currentTimeMillis() - start) + "ms");
    }
    static class Fib extends Thread {
        private Semaphore semaphore;
        public Fib(Semaphore semaphore) {
            this.semaphore = semaphore;
        }
        @Override
        public void run() {
            try {
                semaphore.acquire();
                result = FibUtils.fib(32);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }
    }
}
