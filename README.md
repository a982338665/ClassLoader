
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
    
**6.为什么使用双亲委派模型去加载类:父加载器加载完之后，子加载器不会再去加载该类**

    理解string不可变的原因:
        1.String不可变体现在堆内存中的值不变，而非指向内存的引用不变
            String a="123"; a指针指向123    123仍在内存中存在，只是指针指向了存放345的内存
            a="345";        a指针指向345
        2.字符串常量池:若字符串可变，则每当去修改字符串时，将会影响到其他对象 严格来说，这种常量池的思想,是一种优化手段.
        3.安全问题，若字符串可变则会有很大的安全隐患
    假设没有使用双亲委派模型去加载类：那么我们就可以自定义String类，使用自定义的classLoader去加载自己写的String类，
    这样由于String此时非不可变类，在之后的使用中就会出行一系列安全问题，而双亲委派模型，则可以避免这种情况，因为String在
    启动的时候就会被引导使用bootstrapClassLoader去加载，这样自定义的String将永远没有机会被自定的类加载器去加载一个自己写的
    String，除非，可以去修改JDK中classLoader搜索类的默认算法
    
**7.JVM搜索类判断两个class是否相同的条件是:**

    1.类名是否相同
    2.是否由同一个类加载器实例加载的
    
**8.JVM主要包括的四个部分：**

    1.类加载器:jvm启动时或类加载时，将.class文件加载到内存中
    2.执行引擎:负责执行class文件中的字节码指令
    3.内存区:jvm运行时操作所分配的内存区
        --1.方法区：存储结构信息的地方-->常量池，静态变量，构造函数等  --java线程共享
        --2.java堆(heap)：存储java实例或对象的地方，GC的主要区域             --java线程共享
        --3.java栈(stack):总和线程关联在一起
            -1.每创建一个线程，jvm就创建一个对应的java栈(一个java栈会包含多个栈帧)
            -2.每运行一个方法就创建一个栈帧用于存储局部变量表、操作栈、方法返回值等
            -3.每一个方法从调用直至执行完成的过程，就对应一个栈帧在java栈中入栈到出栈的过程。所以java栈是线程私有的。
        --4.程序计数器:用于保存当前线程执行的内存地址。由于JVM程序是多线程执行的（线程轮流切换），
            所以为了保证线程切换回来后，还能恢复到原先状态，就需要一个独立的计数器，记录之前中断的地方，
            可见程序计数器也是线程私有的。
        --5.本地方法栈(Native Method Stack)：和java栈的作用差不多，只不过是为JVM使用到的native方法服务的。
    4.本地方法接口：主要是调用C或C++实现的本地方法及返回结果。
 
**9.java内存分配分类：**
 
    静态内存:编译时就能够确定的内存就是静态内存，即内存是固定的，系统一次性分配，比如int类型变量；
    动态内存:在程序执行时才知道要分配的存储空间大小，比如java对象的内存空间。
    根据上面我们知道，java栈、程序计数器、本地方法栈都是线程私有的，线程生就生，线程灭就灭，栈中的栈帧随着方法的结束也会撤销，
    内存自然就跟着回收了。所以这几个区域的内存分配与回收是确定的，我们不需要管的。
    但是java堆和方法区则不一样，我们只有在程序运行期间才知道会创建哪些对象，所以这部分内存的分配和回收都是动态的。
    一般我们所说的垃圾回收也是针对的这一部分。
    
    Stack的内存管理是顺序分配的，而且定长，不存在内存回收问题；
    而Heap 则是为java对象的实例随机分配内存，不定长度，所以存在内存分配和回收的问题；

**10.垃圾回收算法之分代收集算法介绍：**

    1.为什么要进行分代？
        因为java对象的生命周期各有不同：例如session，socket，线程的生命周期要长于String的生命周期，由于string不可变，一般在
        使用一次后便可回收。
        试想，若忽略对象生命周期，则每次进行垃圾回收都需要去对整个堆空间进行回收，会耗费很长时间，去扫描生命周期较长的对象
        也是徒劳，所以引入分治思想，把对象通过生命周期长短进行划分，放在不同代上，然后针对每个代设置不同的垃圾回收机制 
        就能大大提高垃圾回收效率
    2.分代:
        -1.年轻代:经过多次GC后仍存在的被转至年老代                --复制算法
        -2.年老代:年老代内存被占满时会触发full GC回收整个堆内存   --标记整理算法
        -3.持久代:主要存放静态文件,java类,方法等信息，与java垃圾回收关系不大
        

           





