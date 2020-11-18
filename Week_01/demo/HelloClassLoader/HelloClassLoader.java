package demo.HelloClassLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HelloClassLoader extends ClassLoader{
    public static void main(String[] args) {
        try {
            HelloClassLoader helloClassLoader = new HelloClassLoader();
            Class clazz = helloClassLoader.findClass("/home/promise/Work/code/JAVA-000/Week_01/demo/HelloClassLoader/Hello");
            Object object = clazz.newInstance();
            Method hello = clazz.getMethod("hello");
            hello.invoke(object, null);
           // Method hello = clazz.getDeclaredMethod("hello",null);
           // hello.invoke(object, null);

         //   new HelloClassLoader().findClass("/home/promise/Work/code/JAVA-000/Week_01/demo/HelloClassLoader/Hello").newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class cls = null;
        String classFilename = name + ".xlass";
        //String classFilename = name + ".class";
        File classFile = new File(classFilename);
        if (classFile.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(classFile);
                int len = 0, totalLen = 0 ;
                byte[] buf = new byte[1024];
                while((len = fileInputStream.read(buf))!=-1) {
                    totalLen += len;
                    System.out.print("len:"+len);
                }
                System.out.print('\n');
                fileInputStream.close();
                for (int i = 0; i < totalLen; i++) {
                    /* class 文件 不需要 255 - */
                    buf[i] = (byte) (0xFF - buf[i]);
                    System.out.print(Integer.toHexString(buf[i])+"");
                    //System.out.print(Integer.toHexString(buf[i])+"");
                }
                System.out.print('\n');
                // 类似 javac 加载 class 文件
                cls = defineClass("Hello", buf, 0, totalLen);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (cls == null) {
            throw new ClassNotFoundException(name);
        }
        return cls;
    }
}
