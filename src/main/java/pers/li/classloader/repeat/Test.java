package pers.li.classloader.repeat;

/**
 * @author:luofeng
 * @createTime : 2018/11/16 17:26
 */
public class Test {

    public static void main(String[] args) {
        test4();
    }


    /**
     * 对象只加载一次，返回true
     */
    private static void test4() {
        ClassLoader c1 = Test.class.getClassLoader();
        Test loadtest = new Test();
        ClassLoader c2 = loadtest.getClass().getClassLoader();
        System.out.println("c1.equals(c2):"+c1.equals(c2));
    }
}
