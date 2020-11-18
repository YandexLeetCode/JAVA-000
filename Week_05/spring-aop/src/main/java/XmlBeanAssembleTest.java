import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by yandex on 2020/11/16.
 */
public class XmlBeanAssembleTest {
    public static void main(String[] args) {
        String xmlPath = "spring-config.xml";

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);

        // 构造函数输出
        System.out.println(applicationContext.getBean("user"));
        System.out.println(applicationContext.getBean("userByValue"));
    }
}
