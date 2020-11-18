package InvocationProxy;

/**
 * Created by yandex on 2020/11/16.
 */
public class Ventor implements Sell{
    @Override
    public void increase() {
        System.out.println("increase items");
    }

    @Override
    public void decrease() {
        System.out.println("decrease items");
    }
}
