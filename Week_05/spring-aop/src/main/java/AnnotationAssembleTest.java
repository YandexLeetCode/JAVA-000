import com.geekbang.springaop.annotation.UserController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by yandex on 2020/11/16.
 */
public class AnnotationAssembleTest {
    public static void main(String[] args) {
       String xmlPath = "spring-config2.xml";

       ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);

        UserController userController = (UserController) applicationContext.getBean("userController");
        userController.save();
    }
}
