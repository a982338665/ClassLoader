
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
    4.ClassLoader使用的是双亲委托模型来搜索类的，每个ClassLoader实例都有一个父类加载器的引用（不是继承的关系，是一个包含的关系）
      虚拟机内置的类加载器（Bootstrap ClassLoader）本身没有父类加载器，但可以用作其它ClassLoader实例的的父类加载器
 
**2.java类加载器分类:**
   
    BootStrapClassLoader    启动类加载器
        --1.最顶层的类加载器，内嵌在JVM，启动时初始化该类加载器
        --2.加载java核心类库:JRE/lib/rt.jar等中所有的class文件    -->java规范定义的所有接口及实现
    ExtensionClassLoader    扩展类加载器
        --1.加载Java的一些扩展类库，如读取JRE/lib/ext/*.jar中的包等(有些版本没有ext目录)
    AppClassLoader          系统类加载器
        --1.加载应用程序CLASSPATH下指定的所有jar包或目录的类文件，一般情况下这个就是程序中默认的类加载器
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
    3.详细流程:
        --1.一个ClassLoader实例需要加载某个类
        --2.它会试图亲自搜索某个类之前，先把这个任务委托给它的父类加载器
        --3.这个过程是由上至下依次检查的，首先由最顶层的类加载器Bootstrap ClassLoader试图加载，
            如果没加载到，则把任务转交给Extension ClassLoader试图加载，
            如果也没加载到，则转交给App ClassLoader 进行加载，
        --4.如果它也没有加载得到的话，则返回给委托的发起者
        --5.由它到指定的文件系统或网络等URL中加载该类。如果它们都没有加载到这个类时，
            则通过方法findClass抛出ClassNotFoundException异常
        --6.如果加载到类:将这个找到的类生成一个类的定义，并将它加载到内存当中，最后返回这个类在内存中的Class实例对象
        
**4.ClassLoader的loadClass和 Class.forName("com.mysql.jdbc.Driver"):**

    Class.forName("com.mysql.jdbc.Driver")：显式加载类
        JVM查找并加载指定的类，也就是说JVM会执行该类的静态代码段
        查看com.mysql.jdbc.Driver源码可以发现里面有个静态代码块，
        在加载后，类里面的静态代码块就执行（主要目的是注册驱动，把自己注册进去），所以主要目的就是为了触发这个静态方法。

        
**5.重复加载与回收:**    
    
    一个class可以被不同的class loader重复加载，但同一个class只能被同一个class loader加载一次 -->pers.li.classloader.repeat.Test
    当某个classloader加载的所有类实例化的所有对象都被回收了，则该classloader会被回收。
    
**5.ClassLoader引入:-->动态加载.class文件到内存当中**    

    1.应用程序组成:.class文件
    2.程序启动不会一次性加载所有.class文件
    3.ClassLoader则是动态加载某个class文件到内存当中
    