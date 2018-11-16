package pers.li.classloader.customclassloader;

import com.sun.org.apache.bcel.internal.util.ClassLoader;
import sun.applet.AppletClassLoader;

/**
 * @author:luofeng
 * @createTime : 2018/11/16 16:32
 * 自定义类加载测试类
 */
public class LoaderTest {

    /**
     * 父类classloader
     * @throws Exception
     */
    private static void test2() throws Exception{
        MyClassLoader loader = new MyClassLoader();
        //  类委托AppClassLoader加载的
        //
        Class<?> c = loader.loadClass("pers.li.classloader.customclassloader.PersonImpl");
        System.out.println("Loaded by :" + c.getClassLoader());

        Person p = (Person) c.newInstance();
        p.say();
        PersonImpl man = (PersonImpl) c.newInstance();
        man.say();
    }

    /**
     * 自己的classloader加载
     * @throws Exception
     */
    private static void test3() throws Exception{
        MyClassLoader loader = new MyClassLoader();
        //  由于子类覆盖了父类方法，所以此类是委托给自定义classloader去加载的
        //  直接调用findClass方法，不去其余类加载器中寻找，直接使用自定义加载器去实现类的加载过程
        Class<?> c = loader.findClass("pers.li.classloader.customclassloader.PersonImpl");
        System.out.println("Loaded by :" + c.getClassLoader());

        Person p = (Person) c.newInstance();
        p.say();
        //注释下面两行则不报错
        PersonImpl man = (PersonImpl) c.newInstance();
        man.say();
    }

    public static void main(String[] args) throws Exception {
        test2();
        Thread.sleep(1000);
        System.err.println("=================================");
        test3();
    }
}
