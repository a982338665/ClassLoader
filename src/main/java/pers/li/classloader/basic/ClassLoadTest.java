package pers.li.classloader.basic;

import java.net.URL;

/**
 * @author:luofeng
 * @createTime : 2018/11/16 17:39
 */
public class ClassLoadTest {

    public static void main(String[] args) {


        getBootstrapClassPath();




    }


    /**
     * BootStrap ClassLoader 加载的相关jar包和class文件打印
     * file:/C:/Java/jdk1.8.0_111/jre/lib/resources.jar
         file:/C:/Java/jdk1.8.0_111/jre/lib/rt.jar
         file:/C:/Java/jdk1.8.0_111/jre/lib/sunrsasign.jar
         file:/C:/Java/jdk1.8.0_111/jre/lib/jsse.jar
         file:/C:/Java/jdk1.8.0_111/jre/lib/jce.jar
         file:/C:/Java/jdk1.8.0_111/jre/lib/charsets.jar
         file:/C:/Java/jdk1.8.0_111/jre/lib/jfr.jar
         file:/C:/Java/jdk1.8.0_111/jre/classes
     */
    public static void getBootstrapClassPath(){
        URL[] urls = sun.misc.Launcher.getBootstrapClassPath().getURLs();
        for (int i = 0; i < urls.length; i++) {
            System.out.println(urls[i].toExternalForm());
        }
        //以上路径实际是通过获取系统属性sun.boot.class.path得到的
        System.out.println(System.getProperty("sun.boot.class.path"));
    }



}
