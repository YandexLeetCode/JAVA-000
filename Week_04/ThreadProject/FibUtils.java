package ThreadProject;

/**
 * Created by yandex on 2020/11/11.
 */
public class FibUtils {
    /**
     * fib 计算 有 递归 -> 数组操作 复杂度 从 2的N次方 -> O(N)
     * @param N
     * @return
     */
    public static int fib(int N) {
        if (N <= 1) {
            return N;
        }
        return fibCache(N);
    }

    private static int fibCache(int N) {
        if (N < 2) {
            return N;
        }
        int[] cache = new int[ N + 1];
        cache[0] = 0;
        cache[1] = 1;
        for (int i = 2; i <= N; i++) {
            cache[i] = cache[i - 1] + cache[i - 2];
        }
        return cache[N];
    }
}
