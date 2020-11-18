package InvocationProxy;

import java.lang.reflect.Proxy;

/**
 * Created by yandex on 2020/11/16.
 */
public class DynamicProxyTest {
    public static void main(String[] args) {
        DynamicProxy dynamicProxy = new DynamicProxy(new Ventor());

        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        // 获取 代理类 sell
        Sell sell = (Sell) Proxy.newProxyInstance(Sell.class.getClassLoader(), new Class[] {Sell.class},dynamicProxy);

        // 通过代理类调用 代理方法
        sell.increase();
        sell.decrease();
    }
}
