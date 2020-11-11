package ThreadProject;

import java.util.concurrent.*;

/**
 * Created by yandex on 2020/11/11.
 */
public class Homework0308 {
    static Integer result = -1;
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(1, new Runnable() {
            @Override
            public void run() {
                System.out.println("异步计算结果为: " + result);
                System.out.println("使用时间 " + (System.currentTimeMillis() - start) + "ms");
            }
        });
        new Thread(new Fib(cyclicBarrier)).start();
    }
    static class Fib implements Runnable {
        private CyclicBarrier cyclicBarrier;
        public Fib(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }
        @Override
        public void run() {
            try {
                result = FibUtils.fib(32);
            } finally {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
