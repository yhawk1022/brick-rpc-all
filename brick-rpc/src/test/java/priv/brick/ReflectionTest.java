package priv.brick;

import java.lang.reflect.Method;

/**
 * @author : pangyan
 * @date : 2022/11/05
 * @description :
 */
public class ReflectionTest {
    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> aClass = ClassLoader.getSystemClassLoader().loadClass("priv.brick.DemoServiceImpl");
    }
}
