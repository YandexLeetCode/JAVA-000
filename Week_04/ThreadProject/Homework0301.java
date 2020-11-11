package ThreadProject;

/**
 * Created by yandex on 2020/11/11.
 * 1. 使用全局变量,题目是只有一个线程,所以 只存在线程中write,那就不需要考虑安全问题
 *
 */
public class Homework0301 {

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
       thread.join();

        System.out.println("异步计算结果为: " + result);
        System.out.println("使用时间 " + (System.currentTimeMillis() - start) + "ms");
    }
}
