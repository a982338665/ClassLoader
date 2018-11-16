package pers.li.classloader.customclassloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 自定义classLoader
 */
public class MyClassLoader extends ClassLoader{
    /* 
     * 覆盖了父类的findClass，实现自定义的classloader
     * 在顶级加载器之后，覆盖此类执行自己的加载逻辑
     */
    @Override
    public Class<?> findClass(String name) {
        //将字节码数据转换为二进制数据
        byte[] bt = loadClassData(name);
        // 方法负责将二进制的字节码转换为Class对象，这个方法对于自定义加载类而言非常重要
        // 抛出  ClassFormatError:    二进制的字节码的格式不符合JVM Class文件的格式
        // 抛出  NoClassDefFoundError:生成的类名和二进制字节码中的不同
        // 抛出  SecurityException:   加载的class是受保护的、采用不同签名的或类名是以java.开头的
        // 抛出  LinkageError:        加载的class在此ClassLoader中已加载
        return defineClass(name, bt, 0, bt.length);
    }

    private byte[] loadClassData(String className) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(
                className.replace(".", "/") + ".class");
        ByteArrayOutputStream byteSt = new ByteArrayOutputStream();
        int len = 0;
        try {
            while ((len = is.read()) != -1) {
                byteSt.write(len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteSt.toByteArray();
    }
}