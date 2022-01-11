# Spring Core 1
In this project I will follow the 52 minutes video
<span style="color:aquamarine">Spring Core Framework Tutorial | Full Course</span>.
at https://www.youtube.com/watch?v=ZwcHeLhvuq4
It is a general introduction to Spring Core, which seems good to me.

Enriched with content from the course:
- <span style="color:aquamarine">Spring Framework: Spring Fundamentals</span>, by Bryan Hansen **pluralsight**
- <span style="color:aquamarine">Mastering Spring Framework Fundamentals</span>, by Matthew Speak. O'Reilly. **pluralsight**

## Spring online resources
Spring Framework documentation:
1. https://docs.spring.io/spring-framework/docs/current/reference/html/ or https://docs.spring.io/spring-framework/docs for the documentation of all the versions.  
2. Java doc api for each version, for example, for 4.0.0.M2 https://docs.spring.io/spring-framework/docs/4.0.0.M2/
3. Spring guides https://spring.io/guides. Guides by topic areas.

The documentation for Spring 5 onwards has been nicely split into different technologies stack. See for example, we can read the "core" part of Spring only at https://docs.spring.io/spring-framework/docs/5.3.7/reference/html/.
 
##  Layers in enterprise Java applications
- Business logic: service classes, service layer
- Data access logic: repository classes, persistence layer
- Handling HTTP request: controller classes, presentation layer

Normally, controllers will talk to service classes, and service classes will talk to repository classes. That's the convention, the layering enterprise apps go for. Beans can be shared though. For example, a same repository class can be used by many service classes.

## Spring Core general
Spring was developed to make enterprise Java development (already existing tasks) easier, as an improvement over EJBs. It holds to many best practices and well known design patterns. It allows us to actually write better Java code, because it allows for loosely coupled classes and more testable code. It makes our application configurable, instead of using hard coded settings.

### Dependency injection and IoC (Inversion of Control)
<u>Dependency injection</u> is when we set ("inject") the fields ("dependencies") of a class from outside its body, through constructors or setters that some client code will call, for example. 

<u>Inversion of Control (IoC)</u> is when the execution flow of an application is not under the "control" the code it runs, but under the control of a framework the application uses. When we use a framework like Spring, we have no control over when the framework methods will be called (for example those that initialize and destroy the beans). We can only control what they do. In fact this is a <u>distinguishing treat between a framework and a library</u>. A library does not deprive the application of the execution flow control; it's just a set of function, or methods, we call and that return to the calling point in the code once finished. 

Among the many thing a framework that does IoC can do, there is objects ("beans") creation, and dependency injection, or wiring. The framework will provide many possibilities for how these two things can be done. See https://stackoverflow.com/questions/6550700/inversion-of-control-vs-dependency-injection.

Spring started out just as an IoC framework that did dependency injection. It was conceived to reduce or replace some of the complex configurations in early Java EE applications. **_Later_**, Spring started to evolve around building enterprise applications without EJBs. They initially were just figuring out how to work better with EJBs, but then discovered that EJBs were actually not needed for a lot of situations.

Spring can essentially be used with or without EJBs, but nowadays, it is primarily used without them. No needs of EJBs means no need of an application server, such as Wildfly. So Spring allows developing enterprise applications without an application server. Spring only needs a web server, and by default uses Tomcat (easy to use and lightweight). Before Spring, it was not easy to have enterprise features in an application deployed in Tomcat.

<u>Spring is completely POJO based and interface driven</u>. Springs uses AOP and Proxies to apply things as transactions to our code to get those 'cross-cutting concerns' ? out of our code, producing smaller and lightweight applications.

Spring is built around best practices. It uses well known Design Patterns such as Singleton, Factory, Abstract Factory. Template method is used a lot.

**What problems Spring solves?**
1. Increases Testability
2. Increases Maintainability
3. Helps with Scalability
4. Helps with decoupling and caching
5. Helps in reducing code complexity
6. Helps in focusing in the business logic by removing boilerplate configuration code.

Spring allows reducing this
```java
public Car getById(String id) {
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        String sql = "select * from CAR where ID = ?";
        con = DriverManager.getConnection("localhost:3306/cars");
        stmt = con.prepareStatement(sql);
        stmt.setString(1, id);
        rs = stmt.executeQuery();
        if (rs.next()) {
            Car car = new Car();
            car.setMake(rs.getString(1));
            return car;
        } else {
            return null;
        }
    } catch (SQLException e) { e.printStackTrace();}
    finally {
        try {
            if(rs != null && !rs.isClosed()) {
                rs.close();
            }
        } catch (Exception e) {}
    }
    return null;
}
```
to this
```java
// Spring JDBC template method
public Car findCar(String id) {
    return getEntityManager().find(Car.class, id);
}
```
This is Spring JDBC template code. This is business focus !

Spring allow three types of configuration:
1. Annotation based configuration
2. Xml based configuration
3. Java based configuration

In Spring, the **Application Context** is the configured Spring IoC container with all our dependencies wired up in it. It is _mainly_ a hashmap of objects.

Spring can also be used as a registry ?

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

In general, tight coupling will make hard maintain and test the code. For example, 
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

Loose coupling ease testability of our code as well.

Java applications should always prefer abstraction, with interfaces for example, over concretion, with concrete classes, the reason being we can later change the implementations without having to change every single reference where it is used. Spring uses this approach heavily.

## Spring

Spring allows for automatic Dependency Injection (or **_Inversion of Control_**, IoC). This was, in fact, the initial purpose of the Spring framework. Spring creates the Java objects, or Spring beans, and injects them at runtime.

Fig. min 5:49

## pom.xml setting and the bom 
A nice way to download Spring dependencies is at https://search.maven.org/. Go there an in the search dialog box type "org.springframework:". The different **modules** of the Spring framework will appear. The module that brings Spring into our project is "spring-context". This is the top level dependency of the Spring Framework.

A startup pom for a spring project can be:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>conference</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <name>Spring-core-1</name>
    <description>course Spring Framework Fundamentals</description>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <!--        <java.version>1.8</java.version>-->
    </properties>
        
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-framework-bom</artifactId>
                <version>5.2.19.RELEASE</version>  <!--all other dependencies of the project will have this version -->
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
        </dependency>
        <!--        <dependency>-->
        <!--            <groupId>javax.annotation</groupId>-->
        <!--            <artifactId>javax.annotation-api</artifactId>-->
        <!--        </dependency>-->
    </dependencies>


    <build>
        <plugins>
            <!--this must always be specified, otherwise Intellij will use its Java 5 compiler-->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
<!--                <configuration>-->
<!--                    <source>${java.version}</source>-->
<!--                    <target>${java.version}</target>-->
<!--                </configuration>-->
            </plugin>

        </plugins>
    </build>
</project>
```


The Spring framework is distributed through jars. Springs wants yuo to use Maven or Gradle, to download its jars, instead of doing it manually. This is because normally Spring jars will depend on other jars and Maven will take care of downloading those other jars as well. In other words, Spring dependencies have dependencies themselves, and Maven is a dependency management tool. These other dependencies are called transitive dependencies. For example, when in the pom we ask for dependency:
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
ie. Maven has downloaded other 5 needed transitive dependencies. Dependency 'a' needed by dependency 'b' appears below it and indented.

There are some best practices with regard to the pom setting of a spring project. One is to explicitly specify the version of the `maven-compiler-plugin`, as we made above. Another one is to use a bom, or "built of materials". 

The bom is a dependency that defines the version of the dependencies of a project. It unifies the version of all the dependencies in a prokect and ensure no conflicting dependencies, or transit dependencies, are used. One we include it, we no longer need to specify a version for our dependencies. Spring Boot and Spring Cloud use this approach. It is another Spring best practice. Notice that the Maven plugin versions still need to be specified.

Notice also that the bom will only specify versions the dependencies from the 'groupId' org.springframework. Other dependencies will still need version specification.

The pom file above uses some `properties`. There are some special named properties that Maven understands, such as `project.build.sourceEncoding`, `maven.compiler.source` and `maven.compiler.target`. The two Maven compiler properties specify the Java source code and JRE version (I think), for the Maven compiler plugin, respectively. If we use them, we don't need to do such `<configuration>` in the `maven-compiler-pluging`. 

## Spring beans

Spring creates objects and inject them into our application at runtime. This functionality is provided by the Sprint **IoC Container**, which create objects, inject the needed dependencies into them, and manage their lifecycle. To create beans and set its dependencies, Spring will <u>always</u> use (call) the constructors the class has, either explicitly written by the programmer or, included by the compiler (remember, if there is no constructor at all, the compiler will add a no-args constructor to the class. But if there is one parametrized constructor, the compiler will add none). Similarly, when doing setter injection (see below) Spring will always use (call) the setters the class has. 

In Spring, the **Application Context** is the configured Spring IoC container with all our dependencies wired up in it. It is _mainly_ a hashmap of objects.

There are three ways to define and configure our bean in Spring:
1. through xml configuration file
2. through Java configuration class
3. through annotations

Each of these in turns supports dependency injection through the following mechanisms:
1. constructor injection 
2. setter injection
3. field injection

_In general, we can have (1,1), (1,2), (2,1), (2,2) and (3,1), (3,2) and (3,3)._

In the constructor injection we inject the dependencies to the class through its constructor, whereas in setter injection we do it through available setter methods. We should use constructor injection instead of setter injection, when we want to set dependencies without which we don't want the bean to be ever created. We can use both concurrently.
 
Field injection is used only when we configure our beans through annotations. 

Other than the setters, setter injection needs a default constructor (no-args) in the class where we want to inject the dependencies. The Spring IoC will call this constructor to instantiate an object of this class, and then will call the setter to set, or inject, the dependency.


fig 13.14

To use the IoC Container Spring provide us with two <u>interfaces</u>, `BeanFactory` 
and `ApplicationContext`. `ApplicationContext` actually extends `BeanFactory` and is the recommended way to go. We'll normally  obtain our beans from one of three objects implementing the `ApplicationContext` interface, and there are three of them. In other words, there are three application contexts types with which the Spring IoC container can be normally instantiated in Spring projects:

1. `ClassPathXmlApplicationContext` : Used to configure the beans through a xml file in the class path (src/main/resources).
2. `FileSystemXmlApplicationContext` : Used to configure the beans through a xml file in the file system (dir containing the src/ dir).
3. `AnnotationConfigApplicationContext`: Used to configure the beans through properly annotated classes. See below. 


There are many interfaces that interface `ApplicationContext` extends. See the hierarchy with ^H in IntelliJ! The Spring Framework splits different functionalities (methods) through these interfaces. Study each one to know all what can be done in a Spring application with the Spring context.

### xml configuration

As mentioned, many classes implement the `ApplicationContext` interface allowing us to access the IoC container through them. One is `ClassPathXmlApplicationContext`. One constructor of this class receives the path to the xml file containing information about our objects and how we want to wire them. This file is normally at `src/main/resources/`, and named `applicationContext.xml`, but it can be named anything, such as `beans.xml` for example. An example of its content is:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="emailClient" class="com.example.programmingtechie.EmailClient">
        <constructor-arg ref="advancedSpellChecker"/>
<!--        <property name="spellChecker" ref="basicSpellChecker"/>-->
    </bean>

    <bean id="basicSpellChecker" class="com.example.programmingtechie.BasicSpellChecker">
    </bean>

    <bean id="advancedSpellChecker" class="com.example.programmingtechie.AdvancedSpellChecker">
    </bean>
    <!-- more bean definitions go here -->

</beans>
```

A standard name for this file is `applicationContext.xml`. By default, Spring will look for a file with this name, without any extra configuration. Even though this modality to configure Spring beans is not popular anymore (now people prefer Java configuration and annotations) it does allow for a better separation of concerns.  

The xml schema definition, or namespaces, we normally include at the top of the `applicationContext.xml` file, allows for contextualized help content and autocomplete, while we type-in and define beans in this file. I still don't understand this though.

#### xml: constructor injection

The xml file above shows an example xml beans configuration, and constructor dependency injection. Here we have a _Spring bean_ for each class of our code. Spring beans are nothing more than JavaBeans managed by the Spring IoC Container. Each bean in this file will replace the `new` keyword when we create an object. The Spring IoC container will read the configuration data from this xml file and will create the objects automatically.

When we set the dependency of a class in its constructor, as in the `EmailClient` class above, we are doing **constructor injection**. We do this in our beans definition through the tag `<constructor-arg>`. This is how we do the wiring, or dependency 
injection, without touching our code!

Constructor injection is index based, which means we have to specify the index of the constructor arguments in the xml file. Setter injection is name based instead. Eg:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean name="speakerRepository"
          class = "com.example.programmingtechie.conference.repository.HibernateSpeakerRepositoryImpl"/>

    <bean name="speakerService" class="com.example.programmingtechie.conference.service.SpeakerServiceImpl">
        <!--setter injection-->
        <!--since the property name here is 'speakerRepository', the setter that will be called
         automatically to set it will be setSpeakerRepository(). This is a convention! -->
        <!--<property name="speakerRepository" ref="speakerRepository"/>-->

        <!--constructor injection-->
        <constructor-arg index="0" ref="speakerRepository"/>
        <!--<constructor-arg index="1" ref="anotherReferenceDependency"/>-->

    </bean>

</beans>
```

In the example below, our constructor has only one argument, so we don't need to specify which bean we want to inject when calling the `EmailClient` constructor in its bean definition in the xml file. When there is more than one constructor argument (we need to inject more than one dependency) we use the index parameter in the `constructor-arg` element, as shown above. 

Beans definition files are automatically searched for in the `resources/` directory. Thus, we can pass directly `beans.xml` to `ClassPathXmlApplicationContext` constructor. 
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
Notice that nothing stops us from getting the bean `basicSpellChecker` independently, in our `main()` method using the same approach as shown above. We could do it instead of getting the bean `emailClient` that has `basicSpellChecker` as a dependency.

Notice that `ClassPathXmlApplicationContext` has several overloaded constructors, suitable for different needs. For example, we could pass it several bean definition xml files, defining the bean used by the different layers of our enterprise application. Press ^P to see all the overloaded version of the constructor (or method).

Class `ClassPathXmlApplicationContext` would look the passed xml bean definition file from where the application is lunch (from where we call the launcher `java` of our JDK).

It seems that when we do constructor injection we don't need to include a default 
constructor in the parent class (`EmailClient` in the example above).

The **Application Context** is Spring "container" of configured and managed beans. In other words, it is the set of Spring beans autowired.

It seems that when we initialize an application context, an instance of each bean is created, even if we have not yet asked for any bean to the container. I discovered this using bean lifecycle hooks (interface `InitializingBean`, see below).

The `getBean()` methods are in interface `BeanFactory`, which is in the hierarchy `ApplicationContext` <- `ListableBeanFactory` <- `BeanFactory`. Do ^F12 on it to see its methods, and see all the overloaded versions of method `getBean()`. 

#### xml: setter injection
Setter injection with a xml configuration is performed as:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="emailClient" class="com.example.programmingtechie.EmailClient">
<!--       <constructor-arg ref="basicSpellChecker"/>-->
           <property name="spellChecker" ref="basicSpellChecker"/>
        </bean>

        <bean id="basicSpellChecker" class="com.example.programmingtechie.BasicSpellChecker">
        </bean>

        <bean id="advancedSpellChecker" class="com.example.programmingtechie.AdvancedSpellChecker">
        </bean>
        <!-- more bean definitions go here -->

</beans>
```

Notice that the dependency `spellChecker` of bean `emailClient` is of reference type. That's why we use `ref` to specify which bean in the Spring IoC container, it should be wired to. When a dependency is of primitive type, we use `value`.

#### xml: autowiring
Yes, we can do autowiring with xml configuration as well, without annotations.

Before we saw that when defining the beans in a xml file, we also define explicitly how we want to inject the dependencies in each of them. We used elements `<constructor-arg>` for the case of constructor injection, and `<property>` for the case of setter injection.

However, we can instruct Spring to do a constructor or setter injection for us to wire-up the dependencies a given bean needs, provided that the bean class has the needed setters and constructor to perform the injection of its dependency. This is how we would do it:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean name="speakerService" 
          class="com.example.programmingtechie.conference.service.SpeakerServiceImpl" autowire="constructor">
    </bean>

    <bean name="speakerRepository"
          class = "com.example.programmingtechie.conference.repository.HibernateSpeakerRepositoryImpl"/>
    
</beans>
```
Here Spring will call the constructor of the `SpeakerServiceImpl` class to set the dependency `speakerRepository` this class has, passing as argument the bean `speakerRepository`, which is the only bean assignment compatible available.

Autowire using the setter would need instead the autowire parameter as `autowire="byType"` or `autowire="byName"`. 

In both these cases, it will be called the setter named ("camelcased") after the dependency type (not its name!) we want to inject.

The difference is in how we tell Spring to pick up the bean to be injected. 

In `autowire="byType"` Spring will look for any setter having as argument an assignment compatible bean, so he can call it with an available bean to be injected as dependency. If there are several available assignment compatible beans though (many classes implementing the same interface), we need to further specify to Spring which one we want to inject. For this, see the discussion below for autowiring by type, by name and with using `@Primary` or `@Qualifier`, I made below, when talking about autowiring with annotation.

In `autowire="byName"` Spring will look for a setter exactly named after the declared type (not the name) of the dependency to be injected in the bean, and camelcased. For example, if the dependency is `private SpeakerRepository speakerRepositoryBlue;`, the setter must be called `setSpeakerRepository()`. 

The bean to be injected must always be declared somewhere though, for example in the same xml beans config file. For the case of the xml config file show above, the `SpeakerServieImpl` class may be implemented as:
```java
public class SpeakerServiceImpl implements SpeakerService {

    private SpeakerRepository speakerRepository; 
    
    public SpeakerServiceImpl() {
        System.out.println("calling SpeakerServiceImpl no-arg constructor ...");
    }

    public SpeakerServiceImpl(SpeakerRepository speakerRepository) {
        System.out.println("calling SpeakerServiceImpl argument constructor ...");
        this.speakerRepository = speakerRepository;
    }

    public void setSpeakerRepository(SpeakerRepository speakerRepository) {
        System.out.println("calling setter in SpeakerServiceImpl ... ");
        this.speakerRepository = speakerRepository;
    }

    @Override
    public List<Speaker> findAll(){
        return speakerRepository.findAll();
    }
}
```

Wiring errors will cause <u>runtime</u> exceptions!

#### xml: component scanning
Component scanning through xml requires these additional name spaces and schema definition in the bean definition xml file:
```xml
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd"
```
An example of a full applicationContext.xml file using component scanning could be:
```xml
<?xml version="1.0" encoding="utf-8" ?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                            http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

        <context:component-scan base-package="com.example.matthew"/>

</beans>
```
Notice that still the classes we want Spring to discover as beans will need to be annotated with `@Component`. Similarly, the dependencies we want Spring to inject will need to be annotated with `@Autowired`. These two annotations are explained below.

#### xml: field injection
afdafadsflkj lfjlkj lkfj;laksj
kjl;kjlkj lkjlkjl lkjlkjljljaldsjflkj akfja;lj

### Java configuration

Instead of xml configuration we can make the Spring IoC container to read beans configuration from a Java configuration class. Most newly developed applications in Spring use Java configuration because it is easier to understand. We can create the configuration class in the root package of our project. 

Almost everything in Spring can now be configured using Java configuration.

The configuration class can be annotated with `@Configuration` which give other functionalities that I still don't understand. But this annotation will mark configuration classes that we can pass to `AnnotationConfigApplicationContext` to initialize the container with the beans in all of them.

#### Java: constructor injection

The example below will create our beans exactly in the same way as the previously seen `beans.xml` file. As can be seen, now the beans will be created through methods annotated with `@Bean`, which will be called automatically by Spring when the application context is started. These will be beans "factory methods". The `@Bean` annotation can be used only at a method level.

```java
//@Configuration ??
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
Now we need to tell the Spring IoC container to use the new Java configuration class instead of the xml file. For this we use another class implementing the `ApplicationContext` interface, `AnnotationConfigApplicationContext` and pass its constructor the configuration class:

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

The `@Bean` annotated factory methods can have any name. Spring will look at their return type to decide which one to invoke when we ask for a bean of a given type with method `ApplicationContext.getBean()`.  

Class `AnnotationConfigApplicationContext` has three overloaded constructors. In one of them, we can pass the base package where our beans are. Provided the bean classes are properly annotated with `@Component` and `@Autowired`, Spring will then initialize our beans and wire up them automatically. This is a way of performing something called _component scanning_ and _autowiring_, which will be discussed below:
```java
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.example.programmingtechie");
```

Defining and using a configuration class as we did above with `AppConfig`, give us a more explicit way to define and wire our bean, as well as for doing more complex configurations if needed, I think, since we are doing all this _programmatically_. This is why it's useful. This class acts as a factory of beans.

#### Java: setter injection

To perform a setter injection our `EmailClient` class needs a setter method for the `SpellChecker` dependency: 
```java
class EmailClient {
    
    private SpellChecker spellChecker;
    
    public EmailClient(){}
    
    public void setSpellChecker(SpellChecker spellChecker) {
        this.spellChecker = spellChecker;
    }
    
    public SpellChecker getSpellChecker() {
        return spellChecker;
    }

    void sendEmail (String emailMessage){
        spellChecker.checkSpelling(emailMessage);
    }
}
```
The bean configuration class now needs this setter to inject the dependency:
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
        //return new EmailClient(createAdvancedSpellChecker());

        //setter injection
        EmailClient emailClient = new EmailClient();
        emailClient.setSpellChecker(createAdvancedSpellChecker());
        return emailClient;
    }
}
```

It seems that the way in which we want to inject the dependencies in a given class determines how we design it, and how we'll configure our beans after, in a xml configuration file or a Java configuration class. For example, if we want to do constructor injection, we will need a constructor receiving the dependencies as arguments. If we want to do instead setter injection, we'll need a no-args (default) constructor and the appropriate setters.

After, we set either a xml configuration file or a Java configuration class accordingly.

Finally, depending on whether we chose xml or Java configuration, we then use `ClassPathXmlApplicationContext` or 
`AnnotationConfigApplicationContext`  classes to instantiate the application context. 

#### Java: field injection
fadflkad jf
a;lkjflajdflkjads;flkjlkfjlajf lkjf;lajflajf lakdsjf lkfjl






### Annotations and autowiring
When using either a xml configuration file, or a Java configuration class, we are manually specifying which are the beans we want to create and how we want to inject the dependencies a given class needs (constructor or setter injection). In other words we are _manually_ defining and wiring our beans, <u>all in a same file (xml or Java)</u>.

However, Spring can do the beans definition and wiring for us: Spring can do _**autowiring**_. The difference now is that these two operations will be done in separate files. Spring will use a Java, or xml, file to define where we want to look for beans. Spring will use then the very beans class files to define which classes will be beans and to set the wiring (dependency injection) type.  

Autowiring is specially useful in big projects with many beans and dependencies among them. It is an example of _"convention over configuration"_.

### Autowiring: where are the beans?
The process of beans discovery, when configuring our beans through annotations, is called _Component Scanning_. Classes we want to be discovered in the component scan need to be annotated with `@Component` though.

If we want to use a Java class to define where we want Spring to look for beans (do the components scanning), it's enough to define a Java configuration class with empty body, and annotate it with `@ComponentScan`. We then pass to this annotation the base package(s) where the classes we want to make beans are:
```java
@ComponentScan("com.example")
    public class AppConfig {
}
```
To pass several base packages to scan for beans we list them as `@ComponentScan({"com.example",com.plumbe})`.

If instead we want to do component scanning through xml, we should set the applicationContext.xml as:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://www.springframework.org/schema/beans
https://www.springframework.org/schema/beans/spring-beans.xsd">

    <context:component-scan base-package="com.example"></context:component-scan>
            
</beans>
```


### Autowiring: which are the bean classes and how to do the wiring 
The beans to be autowired can be defined either through `@Component` annotated classes, as we just saw, but can also be defined through `@Bean` annotated methods in a Java configuration class. For example, the bean `speakerRepository` is defined in the Java configuration file below, and is injected through setter in the class `SpeakerServiceImpl`:
```java
@Configuration
public class AppConfig {

    // this return a service configured with a given repository
    // Here we do setter injection. The class where we want to inject the dependency this way must have the
    // needed setter to inject the dependency from outside. The same holds for constructor injection
    @Bean(name = "speakerService")
    //@Scope(value = "singleton")
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public SpeakerService getSpeakerService(){
        // setter injection
        // SpeakerServiceImpl speakerServiceImpl = new SpeakerServiceImpl();
        //speakerServiceImpl.setSpeakerRepository(getSpeakerRepository());

        // constructor injection
        //SpeakerServiceImpl speakerServiceImpl = new SpeakerServiceImpl(getSpeakerRepository());
        SpeakerServiceImpl speakerServiceImpl = new SpeakerServiceImpl();

        return speakerServiceImpl;
    }

    @Bean(name = "speakerRepository")
    public SpeakerRepository getSpeakerRepository(){
        return new HibernateSpeakerRepositoryImpl();
    }
}
```
```java
public class SpeakerServiceImpl implements SpeakerService {

    // we'll inject this from outside
    private SpeakerRepository speakerRepository;// = new HibernateSpeakerRepositoryImpl();

    public SpeakerServiceImpl() {
        System.out.println("SpeakerServiceImpl no args constructor");
    }

    public SpeakerServiceImpl(SpeakerRepository speakerRepository) {
        System.out.println("SpeakerServiceImpl repository constructor");
        this.speakerRepository = speakerRepository;
    }

    // Spring will look for a bean assignment compatible with 'SpeakerRepository' and will wire it
    // with the dependency speaker repository in this class.
    // That other bean can be defined inside a Java configuration class or annotated with @Component
    @Autowired
    public void setSpeakerRepository(SpeakerRepository speakerRepository) {
        System.out.println("SpeakerServiceImpl setter");
        this.speakerRepository = speakerRepository;
    }

    @Override
    public List<Speaker> findAll(){
        return speakerRepository.findAll();
    }
}
```
```java
public class HibernateSpeakerRepositoryImpl implements SpeakerRepository {
    @Override
    public List<Speaker> findAll(){
        List<Speaker> speakers = new ArrayList<>();
        Speaker speaker = new Speaker();
        speaker.setFirstName("Brian");
        speaker.setLastName("Hansen");
        speakers.add(speaker);
        return speakers;
    }
}
```

After we have told Spring where to look for bean classes and which will be the bean classes, to tell him how we want to wire our beans we use the `@Autowired` annotation. This annotation goes at <u>a constructor, a setter method, or a field</u> level, depending on the type of dependency injection we want to do. 

### Autowiring disambiguation
When wiring the beans (or injecting the dependencies) through annotations in the body of the bean class, Spring needs to know which of the possibly many assignment compatible available beans, we want to wire up in a given dependency. Remember, we will be using interfaces in most cases as dependencies in our classes, and our beans will be classes implementing those interface. 

Before this piece of information was given explicitly while wiring the beans in the xml file or Java class. Now, when using annotations to wire beans, we'll have four options, <u>all of which can be used with constructor, setter or field injection</u> !: 
1. autowire by type
2. autowire by name
3. autowire with annotation `@Primary`
4. autowire with annotation `@Qualifier`

When using `@Autowired` to wire, or inject, dependencies, through setter injection, the beans needed to be injected as dependencies can be defined either annotating their classes with `@Component`, or with `@Bean` annotated methods inside a Java configuration class. This would be a hybrid approach and affects code readability in my opinion.

However, if we want to inject through constructor, we should annotate all our beans with `@Component` and leave the configuration class empty and only annotated with `@ComponentScan` (this is what I discovered experimenting, the hybrid approach didn't work for me in this case). This is the most convenient way: going with autowiring and using the stereotype annotations to define our beans.

#### Autowiring: constructor injection
When we want to do autowire by constructor we annotate the constructor with `@Autowired`. Then the types of wiring we can do (type, name etc.) follows the same rules as for setter injection, which we discuss below. Spring will call the constructor passing in for the parameters the assignment compatible beans it finds in the IoC container. Candidate beans for the parameters need to be annotated with `@Component` (or some stereotype annotation), and there should be a Java configuration class with the `@ComponentScan` to do the component scan (or do it through xml).

If we do constructor injection, i.e. we use the constructor of a bean to set its dependencies, and these dependencies are of <u>primitive types</u>, we need to pass values for them somehow to the constructor. In this case, other than the `@Autowired` annotation in the constructor, we use `@Value` to pass the values to the constructor parameter.
```java
@Component("user")
public class User {

    @Value("#{'John Doe'}")
    private String name;
    @Value("#{30}")
    private String country;
    private String language;

    public User() {
    }

    @Autowired
    public User(@Value("#{systemProperties['user.country']}") String country,
                @Value("#{systemProperties['user.language']}") String language) {
        this.country = country;
        this.language = language;
    }

    // getters and setters
    
}
```
In this example we are using a SpEL expression in the `@Value` annotation, but it could be a simple literal, like `@Value("Peru")`, or a property `@Value("${user.country}")`. 

When we need to pass primitive values to a setter annotated with `@Autowired`, the same approach is used. If we don't annotate the setter with `@Autowired`, and only use `@Value` annotation, the setter will still be called to set the dependency, but in the BeanPostProcessor class. This is when the methods annotated with `@Value` are called, it seems. 

#### Autowiring: setter injection
Annotating a setter with `@Autowired` makes Spring to automatically call it to inject a dependency the setter's class needs. This call happens after the default constructor of this class is called to get its bean, either explicitly in a `@Bean` annotated method of a Java configuration class, or implicitly if that class is annotated with `@Component` and included in the components scan. This is how setter injection works: the no-args constructor of the bean is called first, and then the `@Autowired` annotated setter is called to inject the dependency.

In **_autowire by type_** we specify a precise class (not interface) as parameter to the setter, when we want to do setter injection. Our `EmailClient` class would be in this case:

```java
@Component("emailClient")
class EmailClient {
    private SpellChecker spellChecker; //this is an interface

    public EmailClient(){}

    EmailClient(SpellChecker spellChecker){
        this.spellChecker = spellChecker;
    }

    public SpellChecker getSpellChecker() {
        return spellChecker;
    }

    @Autowired
    //autowire by type
    public void setSpellChecker(/*SpellChecker*/ BasicSpellChecker spellChecker) {
        this.spellChecker = spellChecker;
    }

    void sendEmail (String emailMessage){
        spellChecker.checkSpelling(emailMessage);
    }
}
```

Another example of autowire by type happens when our beans implement a common interface, and we have configured them with an empty, but `@ComponentScan` annotated configuration class, using also stereotype annotations to mark the bean classes in the scanned packages. In this case, if we call `ctx.getBean()` passing just the interface implemented, Spring will not know which bean to pick up and will give us an error. For example:
```java
import com.example.programmingtechie.SpellChecker;

public class EmailApplication {
    public static void main(String[] args) {

        //ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

        // this will give an error as there are two concrete classes implementing interface SpellChecker:
        // BasicSpellChecker and AdvancedSpellChecker
        //SpellChecker spellChecker = applicationContext.getBean(SpellChecker.class); 
                
        // this is autowire by type: we are asking for a specific type implementing interface SpellChecker
        SpellChecker spellChecker1 = applicationContext.getBean(BasicSpellChecker.class);
        SpellChecker spellChecker2 = applicationContext.getBean(AdvancedSpellChecker.class);
        
    }
}
```
Below we'll see how to use the `@Primary` annotation which allow us to specify which assignment compatible bean we want to pick up whenever there is ambiguity because of interfaces. This will be useful also in the cases where, instead of asking for a bean to the Spring container, we need to inject a bean into an interface type dependency (field) of another bean.

**_Autowire by name_** is more subtle, and I don't like it. 
With this approach, we can still use the interface reference type in the setter, as Spring will use the name of the parameter to decide which of the assignment compatible beans to wire up. Suppose we want to wire a `BasicSpellChecker` with this approach, the class `EmailClient` will then be:
```java
@Component
class EmailClient {
    private SpellChecker spellChecker;

    public EmailClient(){}

    EmailClient(SpellChecker spellChecker){
        this.spellChecker = spellChecker;
    }

    public SpellChecker getSpellChecker() {
        return spellChecker;
    }

    @Autowired
    //autowire by name
    public void setSpellChecker(SpellChecker basicSpellChecker) { 
        this.spellChecker = basicSpellChecker;
    }

    void sendEmail (String emailMessage){
        spellChecker.checkSpelling(emailMessage);
    }
}
```
**_Autowire with the @Primary_** annotation works by selecting the bean annotated as such, whenever Spring needs to inject one of many assignment compatible beans in one (interface) dependency. For example, if we want to always inject an `AdvancedSpellChecker` bean in the `SpellChecker` dependency of the `EmailClient` class, it would be enough to annotate class `AdvancedSpellChecker` with `@Primary`, and leave the setter in `EmailClient` in its polymorphic (interface) form:

```java
    //...
    @Autowired
    //autowire with @Primary, either in the Basic or AdvancedSpellChecker class
    public void setSpellChecker(SpellChecker spellChecker) {
            this.spellChecker = spellChecker;
     }
    //...
```
**_Autowire with @Qualifier_** works similar to autowire by type. In this case we use the interface reference type in the setter, but we specify which assignment compatible bean we want to wire up annotating the setter parameter with `@Qualifier` and passing the 
name of the bean. The name of the bean will be that of the class but starting with lower case (the default if we don't explicitly specify a name for that bean).

```java
    //...

import org.springframework.beans.factory.annotation.Qualifier;

    @Autowired
    //autowire with @Qualifier
    public void setSpellChecker(@Qualifier("advancedSpellChecker") SpellChecker spellChecker){
        this.spellChecker=spellChecker;
    }
    //...
```
**_Autowire with @Qualifier_** overwrites autowire with @Primary.

__**((When to use one or another way of wiring beans with annotations ??))**__

I think I will always use autowire by type. Otherwise, I will use the `@Primary` or `@Qualifier` annotations. 

#### annotations: field injection
Autowire by type and by name works similarly when injecting the dependencies directly through the fields of the class. We annotate the field with `@Autowired`, and we can use `@Qualifier` or `@Primary` as before. This would be _field injection_. Spring would insert the dependency of the class through the fields using reflections:

```java
@Component
class EmailClient {

    @Autowired
    @Qualifier("advancedSpellChecker")
    private SpellChecker spellChecker;

    public SpellChecker getSpellChecker() {
        return spellChecker;
    }

    void sendEmail (String emailMessage){
        spellChecker.checkSpelling(emailMessage);
    }
}
```
Field injection is the easiest to use, as it requires less code. However, it is not recommended. See https://www.vojtechruzicka.com/field-dependency-injection-considered-harmful/.

Spring recommends setter injection.

## Stereotype annotations
Classes we want Spring to discover as beans can be marked with any of the so called _stereotype annotations_:
- `@Component`: Used for general purpose beans.
- `@Repository`: Used to denote classes used as repository objects.
- `@Service`: Used for business logic beans. It doesn't mean a web service or a microservice. 
- `@Controller`: used in Spring MVC

`@Component` is the most generic way to mark a Spring bean. The stereotype annotations are more specific and denote a given role in an enterprise application. 

As for `@Component`, we can pass as argument the name we want for the bean that will be created with these annotations, eg `@Service("speakerService")`.

Stereotype annotations are useful because we can use filters to look for classes annotated with any of them. ?


## Bean scope
The bean scope determines how many times the bean can be initialized in the IoC container. 
There are 6 beans scopes:
Scopes valid in any configuration:
- **Singleton**: Default by omission. The container will create only  
  one bean of each type, at the time of the container start-up, and then will reuse the bean through the _application context_.
- **Prototype**: a new bean is created each time it is requested to the container.

Web scopes:
- Request: (Web applications). A unique bean will be created for each incoming HTTP _request_.
- Session: (Web applications, Spring MVC). A unique bean will be created for each user HTTP _session_.
- GlobalSession: (Web applications, Spring MVC) Will returns a single bean per application deployment or server reboot.

To define a bean's scope we use the `@Scope` annotation, eg. `@Scope("singleton")` on the bean class annotated with `@Component`. If we use Java configuration, we use this annotation in the  `@Bean` annotated method. This is the method that returns the bean, eg:

```java
@Configuration
public class AppConfig {

    @Bean(name = "speakerService")
    //@Scope(value = "singleton")
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public SpeakerService getSpeakerService(){
        // setter injection
        // SpeakerServiceImpl speakerServiceImpl = new SpeakerServiceImpl();
        //speakerServiceImpl.setSpeakerRepository(getSpeakerRepository());

        // constructor injection
        SpeakerServiceImpl speakerServiceImpl = new SpeakerServiceImpl(getSpeakerRepository());
        return speakerServiceImpl;
    }

    @Bean(name = "speakerRepository")
    public SpeakerRepository getSpeakerRepository(){
        return new HibernateSpeakerRepositoryImpl();
    }
}
```

In xml configuration we specify the scope of a bean with the `scope` property of the `<bean>` element. For example:
```xml
<bean id="myService" class="com.example.programmingtechie.MyService" scope="prototype" />
```
 
### Singleton scope
If a bean has Singleton scope, there will be only one instance per Spring container or application context, not exactly one instance per JVM. We may have more than one Spring container in our JVM. 

We may check singletons easily looking at the memory address representation of our beans, when debugging our application in IntelliJ. Debug with Shift+F9. F8 for forward, F9 to finish. We'll see something like `AdvancedSpellChecker@1628` for all singleton beans, ie. they will point to the same object in memory. If instead the beans has prototype scope, we'll observe different memory addresses being used. 

## Bean lifecycle
The bean lifecycle is a set of steps Spring performs for creating, using, 
and destroying a bean at the time of application shutdown. The lifecycle has three phases:
1. Initialization phase: Initialize the bean, collect all the required properties and 
   instantiate the bean.
2. Bean usage
3. Destruction phase: Spring destroy the beans (frees up its memory I guess).

In the pluralsight course "Spring Framework: Spring Fundamentals", the bean lifecycle is presented as ??:
1. Instantiation
2. Populate properties: properties will be read from property files or injected from other resources.
3. Bean name setting: Spring sets the bean name and makes other resources aware of it.
4. Bean factory setting: The bean factory ?? is made aware of the new bean just created.
5. Pre-initialization: The bean's post processor methods are called.
6. Initialize bean: Initialize the bean utilizing the properties just set ???
7. initMehod call
8. Post initialization: Other bean post processor methods are called?

### The init method of a bean
The init method will be called automatically after the constructor of the bean is called, and the dependencies have been injected. In other words, if we inject the dependency through setter, the no-args constructor will be called first, then the setter to inject the dependency will be called, and then the init method. To be able to add an init method we need the dependency:
```xml
        <dependency>
            <groupId>javax.annotation</groupId>
            <artifactId>javax.annotation-api</artifactId>
            <version>1.3.2</version>
        </dependency>
```
An init method is declared in the bean class body annotated with `@PostConstruct`:
```java
    @PostConstruct
    private void initialize(){
        System.out.println("SpeakerServiceImpl initialize() post-constructor method call ...");
    }
```
In the initialize method we may put some configuration code or logging, but we shouldn't put any connection setting to a db, like getting, opening or closing a connection. Those things should be handled by Spring, not programmatically by us. I think that, in general, we shouldn't put things that should/can be handled by Spring.

### Factory bean
The Factory bean is a Spring pattern that builds up in the Factory Method design pattern. It allows to set the obtention of our beans through a factory method. Moreover, it's very useful when we need to adapt legacy classes to behave as Spring beans (so we can easily inject them as needed) but we cannot modify them. Below follows an example.

Suppose we have an old legacy class called `Calendar` that we want to inject in our code as a Spring bean. The pattern to follow is composed of three steps:
1. Define the `CalendarFactory` Spring factory class, typed with the old legacy `Calendar` class. We'll obtain a `Calendar` object through method `getObject()` of this factory after properly setting it. For the setting of the factory, for example, we can "add days" to it with method `addDays()`, as in the example below.
2. Define the method in the Java beans configuration class returning the calendar factory bean.
3. Define the method in the Java beans configuration class returning the `Calendar` object as a bean.
```java
public class CalendarFactory implements FactoryBean<Calendar> {

    private Calendar calendar = Calendar.getInstance();

    @Override
    public Calendar getObject() throws Exception {
        return calendar;
    }

    @Override
    public Class<?> getObjectType() {
        return Calendar.class;
    }

    public void addDays(int n){
        calendar.add(Calendar.DAY_OF_YEAR,n);
    }

}
```
```java
@Configuration
@ComponentScan({"com.example.programmingtechie.conference"})
public class AppConfig {

    // Here we hard code the factory as we want it, though
    @Bean("calendarFactory")
    public CalendarFactory calendarFactory(){
        CalendarFactory calendarFactory = new CalendarFactory();
        calendarFactory.addDays(2);
        return calendarFactory;
    }

    // We'll get this been from the Spring container, but Spring will create it through
    // the bean factory 'calendarFactory' defined here
    @Bean("calendar")
    public Calendar getCalendar() throws Exception {
        return calendarFactory().getObject();
    }

}
```
Attention should be paid to the fact that a bean that implements Spring's FactoryBean interface "cannot be used as a normal bean" (taken from the documentation of the FactoryBean interface).

It also allows working with static methods inside a class??


____________________________________________
There are mainly three ways to interact with the bean lifecycle. We may need this, for example, if we want to read some files and do some business logic, at the start of the application.
Similarly, we may need it if we want to clean some additional resources in our environment, 
at the time of application shutdown. To achieve this we can use three types of bean lifecycle hooks:
1. Interfaces `InitializingBean` and `DisposableBean`, the bean implements them 
2. Annotations `@PostConstruct` and `@Predestroy` (JSR-250)
3. Methods `init()` and `destroy()` of the `@Bean` annotation

We'll examine the Spring lifecycle for the _singleton_ and _prototype_ scope beans.

The recommended way to interact with beans lifecycle is through the JSR-250 annotation 
`@PostConstruct` and `@Predestroy`.

### Interfaces `InitializingBean` and `DisposableBean`

If we make a bean implement this two interfaces, we'll need to implement methods 
`afterPropertiesSet()` and `destroy()` which will be called by the container? after? 
the bean is initialized, and when it is destroyed (application shutdown), 
respectively. For example:
```java
@Component
class AdvancedSpellChecker implements InitializingBean, DisposableBean, SpellChecker{
    @Override
    public void checkSpelling(String emailMessage){
        if (emailMessage!=null){
            System.out.println("Advanced spelling check ...");
            System.out.println("Spell check complete!!");
        } else {
            throw new RuntimeException("An exception occurred while checking the spelling.");
        }
    }

    //from the DisposableBean interface
    @Override
    public void destroy() throws Exception {
        System.out.println("Destroyed properties");
    }

    //from the InitializingBean interface
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Setting properties after bean is initialized");
    }
}
```
The events of beans initialization will be automatically listened by our application 
context. An application context of type `AnnotationConfigApplicationContext` will listen 
to them out of the box ?. However, to be able to listen for the events occurring at 
the time of shutting down the application, we need to explicitly register a shutdown 
hook with our application context instance. We do this calling 
`registerShutdownHook()` on our application context instance:
```java
public class EmailApplication {
    public static void main(String[] args) {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

        EmailClient emailClient = applicationContext.getBean("emailClient",EmailClient.class);

        emailClient.sendEmail("Hey, this is my first email message");

        ((AnnotationConfigApplicationContext)applicationContext).registerShutdownHook();

    }
}
```
Here we need down-casting since `applicationContext` is of type interface, and we need to 
invoke with the object it is pointing to.

Once the application context is listening for the shutdown events as well, as soon as it 
detects the 'destroy event' of the `AdvanvesSpellChecker` bean, it will call method 
`destroy()` on it ? 

If the bean annotated with `InitializingBean` is of scope _prototype_, whenever we 
ask the container for it, a new bean will be created and returned. Since we have a hook 
for the bean initialization events, method `afterPropertiesSet()` of this interface 
will be called each time we ask for such bean to the container.
However, with prototype scope beans, Spring does not manage its destruction. Therefore, 
even if one of such beans implements interface `DisposableBean`, method `destroy()` on 
them will not be called at application shutdown.

### Annotations `@PostConstruct` and `@PreDestroy`
... not explained properly
### Methods `init()` and `destroy()` of the `@Bean` annotation
... not explained properly

## Properties and Spring Expression Language, SpEL ?
The Spring Expression Language can be used to:
- Manipulate an object already created? 
- Evaluate and inject values at run time and change the behaviour of our code accordingly.
- Evaluate and manipulate configuration

For example, we can inject a value to a primitive dependency of a class like
```java
    @Value("#{ T(java.lang.Math).random()*100 }")
    private double seedNum;
```

Maintainable applications use externalized configuration. In Spring, one way of achieving this is injecting properties from a properties files using SpEL.

The properties file is normally placed at `src/main/resources/`, and named `application.properties`. This file (actually all what is inside `src/main/resources`)  will be copied into the directory `<project_name>/target/classes`, which will be included in the classpath passed to the Java launcher (`java ... -classpath ...:...:... `) when we launch the application. This is why we specify it with the annotation `@PropertySource(value = "classpath:application.properties")`, ie. we give its location relative to a path that is already in the classpath of the application, namely `<project_name>/target/classes`. If our properties file was inside `src/main/resources/dir1/`, we would pass to `PropertySource` the string `"classpath:dir1/application.properties"`. 

Using annotation `@PropertiesSource` in a class, we instruct Spring to read the specified properties file and load its content into the application context. Then, using the `@Value` annotation we can inject the required property into the class field:
```java
@Component
@PropertySource(value = "classpath:application-dev.properties")
class AdvancedSpellChecker implements InitializingBean, DisposableBean, SpellChecker{

    @Value("${app.database.uri}")
    private String databaseUri;

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
We can also specify a default value for the property in case it is not found (the key is not found, not that is has an empty string value) as:
```java
    @Value("${app.database.uri:localhost/8080}")
    private String databaseUri;
```

If we are doing component scan with a `@ComponentScan` annotated configuration class, we can annotate this class with `@PropertySource("classpath:application.properties")`, so the properties in this file are available to be injected in any `@Value` annotated field of the beans discovered in the scan. Similarly, we specify a properties sources with xml configurations as:

```xml
<?xml version="1.0" encoding="utf-8" ?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                            http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="com.example.matthew"/>
    <context:property-placeholder location="classpath:application.properties"/>

    <bean id="service" class="com.example.matthew">
        <property name="repository" ref="repository"/>
        <property name="name" ref="${my.name}"/>
    </bean>

    <bean id="repository" class="com.example.matthew.data.MyRepositoryImpl"/>

</beans>
```

The xml above also shows how we can inject properties into fields of a bean through xml configuration, instead of using the `@Value` annotation. This approach will need a setter for the field though.

The `@Value` annotation in Spring can read properties from properties files, environment variables and system property (or VM options), and there is a useful overriding mechanisms in place among these sources of properties, as discussed below. 

## System properties and environment variables

_System properties_ is what we pass to a Java (`java ...`) program with the `-D` flag. By Bohemian from stackoverflow (https://stackoverflow.com/questions/7054972/java-system-properties-and-environment-variables/7054981#7054981): System properties are set on the Java command line using the `-Dpropertyname=value` syntax. They can also be added at runtime using `System.setProperty(String key, String value)` or via the various `System.getProperties().load()` methods. To get a specific system property you can use `System.getProperty(String key)` or `System.getProperty(String key, String def)`. 

In IntelliJ, we set system properties as "VM options"  under Run/Edit Configurations ... . To see the dialog box for inserting VM options go to Modify Options/Add VM option. If in the VM options we specify something like `-Dmy.name=${LOCAL_NAME}`, this will trigger a bash expansion for the environment variable `LOCAL_NAME`, in the terminal session (connection to the linux kernel) where we run our Java program.

_Environment variables_ are the usual environment variables in Linux. They are valid for the current terminal session.  By Bohemian from stackoverflow (https://stackoverflow.com/questions/7054972/java-system-properties-and-environment-variables/7054981#7054981): Environment variables are set in the OS, e.g. in Linux `export HOME=/Users/myusername`, and, unlike properties, may not be set at runtime. To get a specific environment variable you can use `System.getenv(String name)`.

In IntelliJ, environment variables are added in the same way as VM options, just using the dialog box for "environment variables". When we run our program by clicking in IntelliJ IDE's play button, IntelliJ will open a new terminal session, will set the environment variables we specified in under Run/Edit configuration, and then will run our program invoking `java ...` on that terminal. When the program ends, that terminal is closed.  

## Properties overriding

There are three ways of passing properties to a Spring application. Ordered from lower to higher precedence (overriding, see below):
1. properties file
2. environment variables
3. system property (or VM options)

Properties in a properties file will be part of the application artifact, for example a .jar. If we only had this way for sourcing properties to an application, we'd need to re-build and re-deploy the application whenever we change a value for a property. Fortunately, properties in properties files can be overridden by environment variables and system properties (VM options), provided they have the same name. Once a property is changed through one of these mechanisms, it is enough to stop and re-launch the application.

Environment variables are passed as in the properties file. VM options are passed the same, just prepending them with `-D`. 

Environment variables will _override_ properties file. System properties (VM options) will override environment variables and properties files.


## Throw exception if a property is not found
If we do component scan and properties discovering with xml as in:
```xml
<?xml version="1.0" encoding="utf-8" ?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                            http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

        <context:component-scan base-package="com.example.matthew"/>
        <context:property-placeholder location="classpath:application.properties"/>

</beans>
```
Spring will launch an exception for any property (it's key) that it doesn't found and has no default value, in a `@Value` annotation. Spring will launch `IllegalArgumentException` during the context initialization from the xml file `  ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");`.

However, if we do the component scan and properties discovery through a  Java configuration class, as in
```java
@Configuration
@ComponentScan(basePackages = "com.example.matthew")
@PropertySource("classpath:application-dev.properties")
public class AppConfig {
}
```
and initialize our context with `ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);`, Spring will just let the property unevaluated, and will inject something like "${my.name}" into the field. Strange. I discovered this. I need to learn how to throw and exception in this case as well.

## Environment and profiles
In Spring, `Environment` is an interface representing the environment in which the current application is running. It models two key aspects of the application environment: _profiles_ and _properties_. 

A _profile_ is a named, logical group of bean definitions to be registered (instantiated) with the container **only** if the given profile is _active_. Beans may be assigned to a profile whether defined in XML or via annotations. The active profile determines which beans are instantiated in the container, and which properties files are taken into account for property resolving.  

The role of the `Environment` object with relation to profiles is in determining which profiles (if any) are currently active, and which profiles (if any) should be active by default. There will be two system properties determining the active and the default profiles, `spring.profiles.active` and `spring.profiles.default`, holding comma separated list of profiles names, eg `"test, prod"`. The profiles listed in `spring.profiles.default` will always be active, and it will contain at lest one always, named "default" (Spring puts it). We can get the active profile from the environment invoking `Environment.getActiveProfiles()`. If we specify no active profile, the active profile will be "default", since the active profiles in `spring.profiles.default` are always active.

The role of the `Environment` object with relation to properties is to provide the user with a convenient service interface for configuring property sources and resolving properties from them. ?

In practice, Spring profiles are mapped into application running, or execution, environments like "local", "test" and "production". Usually, we'll have different configurations for different environments, for example, db connections beans and REST endpoints properties.      

The property in Spring applications (`Environment` object of the context, or Spring container) holding the active profiles being used is `spring.profiles.active` (notice the 's' in profiles). As the name of the property suggests, we can have more than one active profile. This property can be set programmatically, through VM options, through environment variables or in file `application.properties`. To do it programmatically, we do the same a VM option does, to set a System property, with a string having a comma separated list of profile names:
```java
public class App {

    public static void main(String[] args) {

        System.setProperty("spring.profiles.active","local, dev");

        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        MyService service = ctx.getBean(MyService.class);

        service.doBusinessLogic();

    }

}
```

We can get property `spring.profiles.active` from the `Environment` object, which is part of the Spring container, invoking `getActivePrifiles()`, as shown below.

The only concrete class implementing interface `Environment` is `StandardProfile` (at least in normal Spring Framework). It is a collection of properties, and each application context has one. The properties are: 
- logger
- activeProfiles: corresponds to property `spring.profiles.active`. Returned by `Environment.getActiveProfiles()`.
- defaultProfiles: profiles that will be active by default. Spring will always put here one called "default", which, in turn, will be the profile assumed by omission of the `@Profile` annotation in our beans. 
- propertySource
- propertyResolver
As we can see there are things related to profiles and things related to properties ?

Interface `ApplicationContext` extends interface `EnvironmentCapable` which has method `xxx` returning the environment object of the context object used to invoke it ?

There are ways to programmatically obtain the `Environment` object, or bean, of a given IoC container. This gives programmatic access to the active and default profiles our application is using, as well as the properties, through getter methods. However, we shouldn't be using any programmatic access in business logic, as this affects testability. We should use instead properties injection through `@Value` and `@Profile` annotated beans, to affect which beans are instantiated in different environments.

One way of programmatically retrieve the `Environment` bean is through the `EnvironmentAware` _callback_ interface having method `setEnvironment(Environment env)`. If a bean class implements it, this method will be called passing in the environment object of the context, which can be used to set an `Environment` type dependency of the bean:
```java
@Service
@Primary
public class MyServiceImpl implements MyService, EnvironmentAware {

    @Value("${my.name}")
    private String name;

    private Environment environment;

    private MyRepository repository;

    @Autowired
    public MyServiceImpl(MyRepository repository) {
        this.repository = repository;
     }

    @Override
    public void doBusinessLogic() {
        System.out.println("Doing business logic for " + name);
        System.out.println("Active profile is: "+ Arrays.toString(environment.getActiveProfiles()));
        repository.doQuery();
    }

    @Override 
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
```
If we have no active profile, `Arrays.toString(environment.getActiveProfiles())` will return `[]`.
Another way is to just add `@Autowired` to a field `private Environment environment;` and make the environment object be injected there by Spring.

We may use profiles, for example, to ensure that some code doesn't get into production.

### Profile specific bean and properties configuration ? 
The Java class configuration, or xml file, we use to configure our beans (our IoC container), can be environment specific (profile specific). We can tell Spring to instantiate a given configuration bean, or all the beans discovered in its component scan, annotating it with `@Profile("profile_name")`. Only if "profile_name" matches one of the active profiles (one in `spring.profiles.active` or `spring.profiles.default`) that bean (or those beans, component scan) will be instantiated, or registered in the container. I think that when we use `@Profile` in a config class with a component scan, it will apply to all beans discovered in the scan. Remember that we initialize the container passing the configuration class we want to use with `ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class)`. 

If a bean, or those component scanned by a config class, has no `@Profile` annotation, `@Profile("default")` will be assumed by default, and will always be instantiated whatever the active profiles is, since the "default" profile is always active. The profile "default" is one profile always inserted by Spring in the list of default profiles `spring.profiles.default`.

Similarly, properties files can be as-per active profile too. In _Spring Boot_, the properties file corresponding to some active profile (profile present in `spring.profiles.active` or `spring.profiles.default`) will be taken into account for property resolving and overwriting (see below). With nothing appended, file `application.properties` will be assumed to "belong" to the default profile, and since this profile is always active, it will always be taken into account. If we set as active profile one called "test", its associated properties file will be "application-test.properties", ie. the convention is `application-<active_profile_name>.properties`. 

In _Spring Boot_, there will be an overriding mechanisms in place for properties. Properties in `application.properties` will always be available for injection, but will be overwritten by properties equally named in the active profiles. Moreover, values of properties in right-most active profiles (list or properties in `spring.profiles.active`) will overwrite those of profiles to the left in the list, I think. I still need to know whether VM options, or environment variables, would overwrite these or not. 

In Spring Boot we can then put all the properties files as-per profile, inside our jar, and when we launch our application pass it which profile we want to use with --spring.profiles.active=profile_name. This way, we'll tell which beans we want to instantiate and which property files we want to consider for properties resolving.  

In normal Spring Framework, we'll explicitly pass to the config class which properties file we want to use through annotations, for example, `@PropertySource("classpath:application-local.properties")`. 

### Setting up different configurations (beans and properties) for different environments ?
We can actually pass different configuration classes to `AnnotationConfigApplicationContext()` when we instantiate the context in a Spring application. See its overloaded constructors. Each of these classes may in turn be annotated with different profile names, such that its component scanned beans will be registered in the container if its profile name matches one active profile. This is the mechanisms that allows instantiating certain beans, and considering specific properties files, passing the active profiles from outside (VM option or environment variable, see above), without having to modify the source code. This is how we would do it:
```java
package com.example.matthew;

import com.example.matthew.business.MyService;
import com.example.matthew.config.DevConfig;
import com.example.matthew.config.ProdConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    public static void main(String[] args) {

        System.setProperty("spring.profiles.active","prod");

        ApplicationContext ctx = new AnnotationConfigApplicationContext(DevConfig.class, ProdConfig.class);

        MyService service = ctx.getBean(MyService.class);

        service.doBusinessLogic();
    }
}
```
```java
package com.example.matthew.config;
import org.springframework.context.annotation.*;

@Profile("dev")
@Configuration
@ComponentScan(basePackages = "com.example.matthew")
@PropertySource("classpath:application-dev.properties")
public class DevConfig {
}
```
```java
package com.example.matthew.config;
import org.springframework.context.annotation.*;

@Profile("prod")
@Configuration
@ComponentScan(basePackages = "com.example.matthew")
@PropertySource("classpath:application-prod.properties")
public class ProdConfig {
}
```
In this example, we've set the profile manually , but we can pass it as VM option (eg. `-Dspring.profiles.active=prod`) or set is an environment variable in the terminal from where we run the java program.

If we pass to `AnnotationConfigApplicationContext` a list of configuration classes having properties files with properties with the same key, the properties files of the right-most config class passed, will take precedence, and will override the values of the properties specified for the other config classes to the left. 

## The proxy design pattern

The proxy design pattern is a pattern that allow us to inject behaviour into existing code almost without modifying it. More specifically, it allows us to modify the obtained/effective behaviour of an object without modifying its class definition. The target objects needs to have its "behaviour" defined by an interface it implements, though. 

Consider a `PersonImpl` class implementing interface `Person`:
```java
public interface Person {
    void greet();
}
```
```java
public class PersonImp implements Person {
    @Override
    public void greet(){  System.out.println("Hello there!");  }
}
```
In the main() we will in invoke this as
```java
        // invoking through the interface
        Person p = new PersonImp();
        p.greet();
```
We can define a proxy class for _all_ classes implementing the `Person` interface as:
```java
public class Proxy implements Person{

    private Person delegate;

    public Proxy(Person delegate) {
        this.delegate = delegate;
    }

    @Override
    public void greet() {
        delegate.greet(); // prints "Hello there!"
    }
}
```
This would allow us to do in the main():
```java
        //invoking through a proxy
        Person p1 = new Proxy(new PersonImp());
        p1.greet();
```
<u>We then insert the new functionalities in the proxy class's constructor or proxy method</u> (the method of the interface). For example:
```java
public class Proxy implements Person{

    private Person delegate;

    public Proxy(Person delegate) {
        System.out.println("Actually, ");
        this.delegate = delegate;
    }

    @Override
    public void greet() {
        System.out.println("I just want to say ...");                
        delegate.greet(); // prints: "Hello there!" 
    }
}
```

As can be seen, a proxy class wraps an interface, and thus all classes implementing that interface.





## Spring AOP proxies ?
Proxies is used to inject behaviour into existing code without modifying it.

