package InvocationProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by yandex on 2020/11/16.
 */
public class DynamicProxy implements InvocationHandler {
    private Object obj;
    public DynamicProxy(Object obj) {
        this.obj = obj;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("sell before");
        Object result = method.invoke(obj, args);
        System.out.println("sell after");
        return result;
    }
}
