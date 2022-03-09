![Ioc Java](https://khalilstemmler.com/img/blog/di-container/dependency-injection-inversion-explained.png)

___

# **Table of content**
* **[What is IoC](#What-is-IoC)**
* **[Loose Coupling vs Tight Coupling](#Tight-Coupling-vs-Loose-Coupling)**
* **[Dependency Injetion](#Dependency-Injetion)**
    * [Static](#Static)
    * [Dynamic](#Dynamic)
    * [Spring](#Using-Spring-Framework-(XML))

___

# **Inversion of Control (_IoC_)**

![Ioc Java](https://www.tutorialsteacher.com/Content/images/ioc/ioc-step1.png)

**Inversion of control** is a software design principle that asserts a program can benefit in terms of pluggability, testability, usability and loose coupling if the management of an application's flow is transferred to a different part of the application

**Example**
* Spring Ioc
* EJB

# **Tight Coupling vs Loose Coupling**
**Tight Coupling** means that classes and objects depend one another
**Example**

![Tight Coupling](https://github.com/hsnKET/hassan_KETLAS-JEE/blob/main/TP_1/Images/Tight%20coupling.png)

IDao interface

```java
public interface IDao {
 double getData();
}
```

A simple implementation of IDao
```java
public class DaoImpl implements IDao{
 @Override
 public double getData() {
 return 0;
 }
}
```

IMetier interface
```java
public interface IMetier {
 double calcule() throws Exception;
}
```

Implementation of IMetier
```java
import ma.enset.tp1.dao.DaoImpl;
/**
* Implementation of IMetier using Tight Coupling (Couplage Fort)
*/
public class IMetierImplFort implements IMetier{
 private final DaoImpl dao;
 public IMetierImplFort(DaoImpl dao) {
 this.dao = dao;
 }
 @Override
 public double calcule() {
 double data = dao.getData();
 return Math.random()*data;
 }
```

**Loose Coupling** helps reduce the interdependencies between components of a system with the aim of reducing the risk that changes in one component will require changes in any other component
**Example**

![Tight Coupling](https://github.com/hsnKET/hassan_KETLAS-JEE/blob/main/TP_1/Images/loose%20%20coupling.png)

Like the previous code we just change the implementation of IMetier interface
```java
import ma.enset.tp1.dao.DaoImpl;
import ma.enset.tp1.dao.IDao;
import ma.enset.tp1.metier.IMetierImplFaible;
public class MainDIStatic {
 public static void main(String[] args) {
 // DI Static
 IDao iDao = new DaoImpl();
 IMetierImplFaible iMetierImpl = new IMetierImplFaible(iDao);
 System.out.println(iDao.getData());
 }
}
```

# **Dependency Injetion**
**Dependency injection** is a technique in which an object receives other objects that it depends on, called dependencies.
In this section we will see how to implement di using 2 methods dynamic and static .
### **Static**

```java
import ma.enset.tp1.dao.DaoImpl;
import ma.enset.tp1.dao.IDao;
import ma.enset.tp1.metier.IMetierImplFaible;
public class MainDIStatic {
 public static void main(String[] args) {
    // DI Static
    IDao iDao = new DaoImpl();
    IMetierImplFaible iMetierImpl = new IMetierImplFaible(iDao);
    System.out.println(iDao.getData());
 }
}
```
The class **IMetierImplFaible** is a simple implementation of the IMetier interface, but loosely coupled. In this example, **IMetierImplFaible** does not depend on any other class, but on the interface, so the next time if we want to add more functions to our code, we can simply do so without changing the class

### **Dynamic**
#### Using constructor
```java
import ma.enset.tp1.dao.DaoImpl;
import ma.enset.tp1.dao.IDao;
import ma.enset.tp1.metier.IMetierImplFaible;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Scanner;
public class MainDIDynamic {
public static void main(String[] args) throws Exception{
    // DI Dynamic constructor
    Scanner scanner = new Scanner(new File("config.txt"));
    String daoClassName = scanner.next();
    Class cDao = Class.forName(daoClassName);
    IDao iDao = (IDao) cDao.newInstance();
    IMetierImplFaible iMetierImpl = new IMetierImplFaible(iDao);
    System.out.println(iDao.getData());
 }
}
```

#### Using Setters

```java
import ma.enset.tp1.dao.DaoImpl;
import ma.enset.tp1.dao.IDao;
import ma.enset.tp1.metier.IMetierImplFaible;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Scanner;
public class MainDIDynamic {
    public static void main(String[] args) throws Exception{
        // DI Dynamic setter
        Scanner scanner = new Scanner(new File("config.txt"));
        String daoClassName = scanner.next();
        Class cDao = Class.forName(daoClassName);
        IDao iDao = (IDao) cDao.newInstance();
        IMetierImplFaible iMetierImpl = new IMetierImplFaible();
        Method setDao = iMetierImpl.getClass().getMethod("setDao",IDao.class);
        setDao.invoke(iMetierImpl,iDao);
        System.out.println(iDao.getData());
 }
```

#### Using Spring Framework __(XML)__
**Configuration file**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xsi:schemaLocation="http://www.springframework.org/schema/beans
http://www.springframework.org/schema/beans/spring-beans.xsd">
 <bean class="ma.enset.tp1.dao.DaoImpl" name="dao" id="dao"/>
 <bean class="ma.enset.tp1.metier.IMetierImplFaible" name="metierImp">
 <constructor-arg >
 <ref bean="dao"/>
 </constructor-arg>
 </bean>
</beans>
```
**Injection**
```java
import ma.enset.tp1.metier.IMetierImplFaible;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class MainDISpring {
     public static void main(String[] args) {
        ApplicationContext app = new
        ClassPathXmlApplicationContext("spring_config.xml");
        IMetierImplFaible iMetierImpl = (IMetierImplFaible)
        app.getBean("metierImp");
        System.out.println(iMetierImpl.calcule());
     }
}
```

#### Using Spring Framework __(Annotation)__

First we need to add **@Component** to our implementation of our interface **IDao**
```java
@Component
public class DaoImpl implements IDao{
    @Override
    public double getData() {
        return 10;
     }
}
```

also we need to add **@Autowired** to constructor or setter method if we want to inject using setters
```java
@Autowired
public IMetierImplFaible(IDao dao) {
    this.dao = dao;
}
```

**Injection**
```java
import ma.enset.tp1.metier.IMetierImplFaible;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
public class MainDISpringAnnotation {
     public static void main(String[] args) {
        AnnotationConfigApplicationContext app = new
        AnnotationConfigApplicationContext("ma.enset.tp1.dao","ma.enset.tp1.metier");
        IMetierImplFaible iMetierImpl = (IMetierImplFaible)
        app.getBean(IMetierImplFaible.class);
        System.out.println(iMetierImpl.calcule());
     }
}
```

