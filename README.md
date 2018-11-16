
# 类加载机制 https://blog.csdn.net/xyang81/article/details/7292380

**1.类加载的使用:**

    1.按需加载-->懒加载
    2.类隔离  -->避免类加载冲突
    3.资源回收
    
**2.java类加载机制:--》双亲委派模型**

    0.参考图:png\类加载机制.png
    1.父类加载该类，子类就不会再去加载一次，避免重复加载
    2.JVM根据 类名+包名+ClassLoader实例ID 来判定两个类是否相同，是否已经加载过
    3.如果想形象地看到java的类加载顺序，可以在运行java的时候加个启动参数，即java –verbose
    
    BootStrapClassLoader    
        --1.最顶层的类加载器，内嵌在JVM，启动时初始化该类加载器
        --2.加载java核心类库:JRE/lib/rt.jar中所有的class文件    -->java规范定义的所有接口及实现
    ExtensionClassLoader
        --1.加载Java的一些扩展类库，如读取JRE/lib/ext/*.jar中的包等(有些版本没有ext目录)
    AppClassLoader
        --1.加载CLASSPATH下指定的所有jar包或目录的类文件，一般情况下这个就是程序中默认的类加载器
    CustomClassLoader
        --1.它是用户自定义编写的，它用来读取指定类文件 。基于自定义的ClassLoader可用于加载非Classpath中（如从网络上下载的jar或二进制）的jar及目录、
            还可以在加载前对class文件优一些动作，如解密、编码等
    
**3.源码中类加载的过程:**
    
    1.加载入口:loadClass(String name);加载指定类的方法
        --1.先从已经加载的类中寻找此类是否被加载过
        --2.没有则从 父classLoader寻找
        --3.没有则从 BootstrapClassLoader中寻找
        --4.没有则调用 findclass抛出异常ClassNotFoundException
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                  throw new ClassNotFoundException(name);
              }
    2.由上可知，我们可以覆盖findClass方法去进行手动加载类，在BootstrapClassLoader中寻找失败后，调用此方法不直接抛出异常，而是自己去实现类加载逻辑
        
**4.ClassLoader的loadClass和 Class.forName("com.mysql.jdbc.Driver"):**

    Class.forName("com.mysql.jdbc.Driver")：显式加载类
        JVM查找并加载指定的类，也就是说JVM会执行该类的静态代码段
        查看com.mysql.jdbc.Driver源码可以发现里面有个静态代码块，
        在加载后，类里面的静态代码块就执行（主要目的是注册驱动，把自己注册进去），所以主要目的就是为了触发这个静态方法。

        
**5.重复加载与回收:**    
    
    一个class可以被不同的class loader重复加载，但同一个class只能被同一个class loader加载一次 -->pers.li.classloader.repeat.Test
    当某个classloader加载的所有类实例化的所有对象都被回收了，则该classloader会被回收。