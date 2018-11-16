package pers.li.classloader;

/**
 * @author:luofeng
 * @createTime : 2018/11/16 16:14
 * @Descritpion 查看各个加载器的父类加载器
 */
public class ParentClassLoader {

    /**
     * 查看父类加载器
     */
    private static void test1() {
        ClassLoader appClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println("系统类装载器:" + appClassLoader);
        ClassLoader extensionClassLoader = appClassLoader.getParent();
        System.out.println("AppClassLoader系统类装载器的父类加载器——扩展类加载器:" + extensionClassLoader);
        ClassLoader bootstrapClassLoader = extensionClassLoader.getParent();
        System.out.println("ExtClassLoader扩展类加载器的父类加载器——引导类加载器:" + bootstrapClassLoader);
    }

    public static void main(String[] args) {
        test1();
    }
}
