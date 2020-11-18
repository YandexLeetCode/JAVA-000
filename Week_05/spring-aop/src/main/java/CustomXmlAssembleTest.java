import com.geekbang.springaop.customxml.Animals;
import com.geekbang.springaop.customxml.Cat;
import com.geekbang.springaop.customxml.Klass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by yandex on 2020/11/16.
 */
public class CustomXmlAssembleTest {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-animal.xml");

        Cat catMM = (Cat) applicationContext.getBean("mm-cat");
        System.out.println(catMM.toString());

        Cat catXM = (Cat) applicationContext.getBean("xm-cat");
        System.out.println(catXM.toString());

        Klass class1 = applicationContext.getBean(Klass.class);
        System.out.println(class1);
        System.out.println("Klass对象AOP代理后实际类型: " + class1.getClass());
        System.out.println("Klass对象AOP代理后实际类型是否是Klass子类: " + (class1 instanceof Klass));

    }
}
