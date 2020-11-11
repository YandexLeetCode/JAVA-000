package ThreadProject;

/**
 * Created by yandex on 2020/11/11.
 *
 */
public class Homework0302 {
    public static int result = -1;
    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                result = FibUtils.fib(32);
            }
        });
        thread.start();
        Thread.sleep(1000);

        System.out.println("异步计算结果为: " + result);
        System.out.println("使用时间 " + (System.currentTimeMillis() - start) + "ms");
    }
}
