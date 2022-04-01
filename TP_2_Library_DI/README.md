
[![GitHub:](https://img.shields.io/badge/license-MIT-brightgreen)](https://github.com/hsnKET/hassan_KETLAS-JEE/tree/main/TP_2_Library_DI)

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/othneildrew/Best-README-Template">
    <img src="./images/logo.png" alt="Logo" width="160" height="80">
  </a>

<h3 align="center">KETDI</h3>

  <p align="center">
    A simple dependency injection library
    <br />
    <br />
    <br />
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#about">About</a></li>
    <li><a href="#getting-started">Getting Started</a></li>
    <li><a href="#usage">Usage</a>
        <ul>
            <li><a href="#annotation" >Annotation</a></li>
            <li><a href="#xml" >XML</a></li>
        </ul>
    </li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE LIBRARY -->

## About


There are many a lots of dependency injection library for java application, 
But if you want a DI library for small project you are in right place.
KETDI has a fewer features compared to other DI frameworks and some limitations.

* They are  many  annotation like  `component` `qualifier` `autowired` `CustomValue`
* In XML we can inject using constructor, setter and also direct injection using fields
* Also, we can declare bean to use them later like `Spring` 
* No `PostConstruct` or `PreDestroy` annotation



### Built With

The Library is build in Java with library `JAXB` for Object/XML Mapping 
(OXM) to load bean definition from xml file



<!-- GETTING STARTED -->

## Getting Started

To use `KETDI` in your project add this dependency to your Project.

### Maven

``` xml
<dependency>
    <groupId>io.github.hsnket</groupId>
    <artifactId>KETDI</artifactId>
    <version>1.0.0</version>
</dependency>
``` 

### Gradle

``` groovy
dependencies {
    implementation 'io.github.hsnket:KETDI:1.0.0'
}
```


<!-- USAGE EXAMPLES -->
## Usage

Let take same example as in <a href="https://github.com/hsnKET/hassan_KETLAS-JEE/tree/main/TP_1" >TP1 </a>

### Annotation


<div align="center" style="background: aliceblue">
  <a href="https://github.com/othneildrew/Best-README-Template">
    <img src="./images/loose%20%20coupling.png" alt="Class diagram" >
  </a>
</div>


IDao interface

``` java
public interface IDao {
    double getData();
}
```


IDaoImpl interface

``` java
@Component
public class IDaoImpl implements IDao {

    @Override
    public double getData() {
        return Math.random() * 100;
    }
}
```


Don't forget to add `@Component` to the implementation of interface.

IMetier interface
``` java
public interface IMetier {
    double calcule();
}
```


#### Constructor Injection

IMetierImpl interface

``` java
@Component
public class IMetierImpl implements IMetier {

    private IDao dao;

    @Autowired
    public IMetierImpl(IDao dao) {
        this.dao = dao;
    }


    @Override
    public double calcule() {
        return dao.getData() + 100;
    }
}
```

`@Autowired` to tell `KETDI` to search for implementation of this interface and inject it's.


#### Setter Injection

IMetierImpl interface

``` java
@Component
public class IMetierImpl implements IMetier {

    private IDao dao;

    @Override
    public double calcule() {
        return dao.getData() + 100;
    }
    
    @Autowired
    public void setDao(IDao dao) {
        this.dao = dao;
    }
}
```

Also, you can use custom value for primitive fields using annotation `@CustomValue(value = "",type = "")`


``` java
@Component
public class IMetierImpl implements IMetier {

    private IDao dao;
    private String mul;

    @Autowired
    public IMetierImpl(IDao dao,@CustomValue(value = "Hi") String mul) {
        this.dao = dao;
        this.mul = mul;
    }
    
    @Override
    public double calcule() {
        return dao.getData() + 100;
    }
}
``` 


#### Direct Field Injection

IMetierImpl interface


``` java
@Component
public class IMetierImpl implements IMetier {

    @Autowired
    private IDao dao;

    @Override
    public double calcule() {
        return dao.getData() + 100;
    }
}
```


If they are more than one implementation of interface you can use `@Qualifier` annotation
 to specify witch implementation to use, like in this example:


``` java
@Component
public class IMetierImpl2 implements IMetier {

    @Autowired
    @Qualifier(name = "IDaoImpl2")
    private IDao dao2;

    @Override
    public double calcule() {
        return dao2.getData() + 200;
    }
}
```

And also update the name of the component to match the name given in `@Qualifier` name


``` java
@Component(name = "IDaoImpl2")
public class IDaoImpl2 implements IDao {

    @Override
    public double getData() {
        return Math.random() * 200;
    }
}
```
Main start up class

``` java
public class SimpleImpl {

    public static void main(String[] args) {

        BeanFactory beanFactory = AnnotationBeanContext.getInstance("net.ketlas.ketdidoc");
        IMetier iMetier = beanFactory.getBean(IMetier.class);
        System.out.println(iMetier.calcule());

    }
}
```
You can pass also start up class if components in the same package with start up class
``` java
BeanFactory beanFactory = AnnotationBeanContext.getInstance(SimpleImpl.class);
```

For beans you can get them using his Class object or using one of the interfaces
implemented by class

``` java
IMetier iMetier = beanFactory.getBean(IMetierImpl.class);
``` 


### XML


In this section we will use the previous example, to inject using XML
we need first to configuration file.

here is an example of configuration file 

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<beans
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:noNamespaceSchemaLocation="xsd.xsd">
    <!-- Declare bean for future use -->
    <bean class="net.ketlas.ketdidoc.dao.IDaoImpl" name="dao" id="dao"/>

    <!-- Declare bean with constructor and properties -->
    <bean class="net.ketlas.ketdidoc.metier.IMetierImpl" name="metierImp" id="metierImp">

        <constructor-arg name="dao" type="net.ketlas.ketdidoc.dao.IDaoImpl" value="">
            <!-- Call bean declared above  -->
            <ref bean="dao"/>
        </constructor-arg>

        <!-- Another primitive constructor argument  -->
        <constructor-arg name="mul" type="java.lang.String" value="something"/>

        <!-- Direct injection using property   -->
        <property name="directInjection" type="java.lang.String" value="KETDI Injection"/>
    </bean>
</beans>
```

As you can see the elements of XML file that `KETDI` use is the same as Spring use, to define bean you need
just to add `bean` element inside `beans` root element and inside `bean` 
element you can add `constructor-arg` element to specify argument of construcutor 
and `property` element to specify field. And both required  `name`, `type`, `value` if the value
is primitive else you can refer to another bean using `ref` element.


#### Simple injection 
``` java
public class SimpleImpl {

    public static void main(String[] args) {

        BeanFactory beanFactory = XMLBeanContext.getInstance("config.xml");
        IMetier iMetier = (IMetierImpl) beanFactory.getBean("metierImp");
        System.out.println(iMetier.calcule());

    }
}
```

For `xsd` validation file you can find it <a href="">here</a>




<!-- ROADMAP -->
## Roadmap

- [ ] Annotation
    - [x] Component
    - [x] Qualifier
    - [x] Autowired
    - [x] CustomValue
    - [ ] PostConstruct
    - [ ] PreDestroy

- [ ] XML
- [x] Constructor injection 
- [x] Property of direct field injection 
- [x] bean refer
- [ ] Custom Collection value (Map,set,List)







<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request





<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.





<!-- CONTACT -->
## Contact

Hassan KETLAS - h.ketlas@enset-media.ac.ma

Project Link: [https://github.com/hsnket/KETDI](https://github.com/hsnket/KETDI)
