# Spring Core 1
In this project I will follow the 52 minutes video "Spring Core Framework Tutorial | Full Course" at https://www.youtube.com/watch?v=ZwcHeLhvuq4
It is a general introduction to Spring Core, which seems good to me.

## Spring Core general
Spring was developed to make enterprise Java development easier, as an 
improvement over EJBs. It holds to many best practices and well known design 
patterns. It allows us to actually write better Java code, because it allows 
for loosely coupled classes and more testable code.

## Tight coupling 
Classes can have fields of reference type, for example classes. Usually we'll 
be interested in some methods of this field class, so we'll need to make point 
this reference to a proper instance. If we do this inside the constructor of 
the class, we'll have **tight coupling**. For example:

```java
class ClassA {
    private ClassB classb;

    ClassA() {
        classb = new ClassB();
    }

    //methods using some method of classb
}
```

Tight coupling should be avoided, because if in the future we want to use 
another implementation of the same method of ClassB, we will need to change 
the code. Moreover, as we only are interested in a method of ClassB, we actually 
don't need to instantiate an object of it every time we want to call such method.

In general tight coupling will make hard maintain and test the code. For example, 
if we want to test the classA above, we'll not be able to inject from outside 
a dummy classB for unit testing.

## Loose coupling
The first step towards loose coupling is the use of interfaces. Put the methods 
of a class we are interested on in an interface and make that class to implement 
this interface. Then, use the interface reference, whenever we need to use a 
method of this class. For example:

```java
public interface SpellChecker {
    void checkSpelling(String emailMessage);
}
```
```java
class BasicSpellChecker implements SpellChecker {
    @Override
    public void checkSpelling(String emailMessage){
        if (emailMessage!=null){
            System.out.println("Basic spelling check ...");
            System.out.println("Spell check complete!!");
        } else {
            throw new RuntimeException("An exception occurred while checking the spelling.");
        }
    }
}
```
```java
class AdvancedSpellChecker implements SpellChecker {
    @Override
    public void checkSpelling(String emailMessage){
        if (emailMessage!=null){
            System.out.println("Advanced spelling check ...");
            System.out.println("Spell check complete!!");
        } else {
            throw new RuntimeException("An exception occurred while checking the spelling.");
        }
    }
}
```
```java
class EmailClient {
    // a dependency thay will be "injected" from outside
    private SpellChecker spellChecker;

    EmailClient(SpellChecker spellChecker){
        this.spellChecker = spellChecker;
    }
    void sendEmail (String emailMessage){
        spellChecker.checkSpelling(emailMessage);
    }
}
```

```java
public class EmailApplication {
   
    public static void main(String[] args) {
      EmailClient emailClient = new EmailClient(new BasicSpellChecker());
      //or
      //EmailClient emailClient1 = new EmailClient(new AdvancedSpellChecker());
      emailClient.sendEmail("Hey, this is my first email message");
      emailClient.sendEmail("Hey, this is my second email message");
    }
    
}
```

In this example we provide the dependency of the EmailClient class from outside.
We _inject_ it from outside when we create the EmailClient instance. This is 
**_Dependency Injection_** performed manually.  

Fig. min 5:24

## Spring

Spring allows for automatic Dependency Injection (or **_Inversion of Control_**, IoC).
This was, in fact, the initial purpose of the Spring framework. Spring creates the 
Java objects, or Spring beans, and injects them at runtime.

Fig. min 5:49

The Spring framework is distributed through jars. Springs wants yuo to use Maven 
to download its jars, instead of doing it manually. This is because normally 
Spring jars will depend on other jars and Maven will take care of downloading 
those other jars as well. In other words, Spring dependencies have dependencies 
themselves, and Maven is a dependency management tool. These other dependencies 
are called transitive dependencies. For example, when in 
the pom we ask for dependency 

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.2.0.RELEASE</version>
        </dependency>
    </dependencies>
```
and we run `mvn:dependency:tree` we get
```text
[INFO] com.example:learn-spring-framework:jar:0.0.1-SNAPSHOT
[INFO] \- org.springframework:spring-context:jar:5.2.0.RELEASE:compile
[INFO]    +- org.springframework:spring-aop:jar:5.2.0.RELEASE:compile
[INFO]    +- org.springframework:spring-beans:jar:5.2.0.RELEASE:compile
[INFO]    +- org.springframework:spring-core:jar:5.2.0.RELEASE:compile
[INFO]    |  \- org.springframework:spring-jcl:jar:5.2.0.RELEASE:compile
[INFO]    \- org.springframework:spring-expression:jar:5.2.0.RELEASE:compile
```
ie. Maven has downloaded other 5 needed transitive dependencies. Dependency 'a' 
needed by dependency 'b' appears below it and indented.

min 12.02

Spring creates objects and inject them into our application at runtime. This 
functionality is provided by the Sprint **IoC Container**, which:
- create objects
- manage lifecycle of objects
- inject dependencies (objects) into our code

One way to specify which objects we want Spring to create and inject is 
through an xml file. Other is through annotations.

fig 13.14

To use the IoC Container Spring provide us with two <u>interfaces</u>, `BeanFactory` 
and `ApplicationContext`. `ApplicationContext` actually extends `BeanFactory` and is 
the recommended way to go. 

### xml configuration

Many classes implement `ApplicationContext`. One is `ClassPathXmlApplicationContext`; we 
use this class to access the IoC container. One constructor of this class receives 
the path to the xml file containing information about our objects and how we want to wire 
them. This file is normally `src/main/resources/bean.xml`, and an example of 
its content is:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="emailClient" class="com.example.EmailClient">
        <constructor-arg ref="advancedSpellChecker"/>
<!--        <property name="spellChecker" ref="basicSpellChecker"/>-->
    </bean>

    <bean id="basicSpellChecker" class="com.example.BasicSpellChecker">
    </bean>

    <bean id="advancedSpellChecker" class="com.example.AdvancedSpellChecker">
    </bean>
    <!-- more bean definitions go here -->

</beans>
```

Here we have _Spring beans_ for each class of our code. Spring beans are nothing more than JavaBeans managed by the 
Spring IoC Container. Each bean in this file will replace the new keyword when we 
create an object. The Spring IoC container will read the configuration data from this xml file and will create
the objects automatically.

When we set the dependency of a class in its constructor, as in the `EmailClient` 
class above, we are doing **constructor injection**. We do this in our beans definition 
through the tag `<constructor-arg>`. This is how we do the wiring, or dependency 
injection, without touching our code!

Beans definition files are automatically searched for in the `resources/` directory. 
Thus, we can pass directly `beans.xml` to `ClassPathXmlApplicationContext` constructor. 
We then get and use our beans as:
```java
public class EmailApplication {
    public static void main(String[] args) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");

        EmailClient emailClient = applicationContext.getBean("emailClient",EmailClient.class);
        
        emailClient.sendEmail("Hey, this is my first email message");

    }
}
```
It seems that when we do constructor injection we don't need to include a default 
constructor in the parent class (`EmailClient` in the example above).

### Java configuration

Instead of xml configuration we can make the Spring IoC container to read bean configuration 
from a Java configuration class. Most newly developed applications in Spring use Java configuration 
because it is easier to understand.

We create the configuration class in the root package of our project. The example below will create our beans 
exactly in the same way as the previously seen `beans.xml` file.

```java
public class AppConfig {

    @Bean(name = "basicSpellChecker")
    public BasicSpellChecker createBasicSpellChecker(){
        return new BasicSpellChecker();
    }

    @Bean(name = "advancedSpellChecker")
    public AdvancedSpellChecker createAdvancedSpellChecker(){
        return new AdvancedSpellChecker();
    }

    @Bean(name = "emailClient")
    public EmailClient createEmailClient(){
        //constructor injection
        return new EmailClient(createBasicSpellChecker());
    }

}
```
Now we need to tell the Spring IoC container to use the new Java configuration class instead of the 
xml file. For this we use another class implementing the `ApplicationContext` interface, 
`AnnotationConfigApplicationContext` and pass its constructor the configuration class:

```java
public class EmailApplication {
    public static void main(String[] args) {

        //ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

        EmailClient emailClient = applicationContext.getBean("emailClient",EmailClient.class);

        emailClient.sendEmail("Hey, this is my first email message");
    }
}

```


