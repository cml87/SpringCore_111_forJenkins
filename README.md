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


## Spring Core general
Spring was developed to make enterprise Java development (already existing tasks) easier, as an improvement over EJBs. It holds to many best practices and well known design patterns. It allows us to actually write better Java code, because it allows for loosely coupled classes and more testable code. It makes our application configurable, instead of using hard coded settings.

### Dependency injection and IoC (Inversion of Control)
<u>Dependency injection</u> is when we set ("inject") the fields ("dependencies") of a class from outside its body, through constructors or setters that some client code will call, for example. 

<u>Inversion of Control (IoC)</u> is when the execution flow of an application is not under the "control" the code it runs, but under the control of a framework the application uses. When we use a framework like Spring, we have no control over when the framework methods will be called (for example those that initialize and destroy the beans). We can only control what they do. In fact this is a <u>distinguishing treat between a framework and a library</u>. A library does not deprive the application of the execution flow control; it's just a set of function, or methods, we call and that return to the calling point in the code once finished. 

Among the many thing a framework that does IoC can do, there is objects ("beans") creation, and dependency injection, or wieing. The framework will provide many possibilities for how these two things can be done. See https://stackoverflow.com/questions/6550700/inversion-of-control-vs-dependency-injection.

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

Spring creates objects and inject them into our application at runtime. This functionality is provided by the Sprint **IoC Container**, which create objects, inject the needed dependencies into them, and manage their lifecycle.

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

In the constructor injection we inject the dependencies to the class through its constructor, whereas in setter injection we do it through available setter methods.

Field injection is used only when we configure our beans through annotations. 

Other than the setters, setter injection needs a default constructor (no-args) in the class where we want to inject the dependencies. The Spring IoC will call this constructor to instantiate an object of this class, and then will call the setter to set, or inject, the dependency.


fig 13.14

To use the IoC Container Spring provide us with two <u>interfaces</u>, `BeanFactory` 
and `ApplicationContext`. `ApplicationContext` actually extends `BeanFactory` and is the recommended way to go. We'll normally  obtain our beans from an object implementing the `ApplicationContext` interface. The kind of object we'll exactly use will depend on how we decide to configure our beans. For example, if we configure our beans through a xml file, we'll use class `ClassPathXmlApplicationContext`, as shown below.

The other classes implementing interface `ApplicationContext` and normally used in Spring projects are `FileSystemXmlApplicationContext` and `AnnotationConfigApplicationContext`. See hierarchy with ^H in IntelliJ !

There are many interfaces that interface `ApplicationContext` extends. Spring has split different functionalities (methods) in these interfaces. Study each one to know all what can be done in a Spring application with the Spring context.

### xml configuration

Many classes implement `ApplicationContext`. One is `ClassPathXmlApplicationContext`; we use this class to access the IoC container. One constructor of this class receives the path to the xml file containing information about our objects and how we want to wire them. This file is normally `src/main/resources/bean.xml`, and an example of its content is:

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

A standard name for this file is `applicationContext.xml`. By default, Spring will look for a file with this name, without any extra configuration. Even though this modality to configure Spring beans is not popular anymore (now people prefer Java configuration and annotations) it actually allows for a better separation of concerns.  

The xml schema definition, or namespaces, we normally include at the top of the `applicationContext.xml` file, allows for contextualized help content and autocomplete, while we type-in and define beans in this file.

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
          class = "com.example.conference.repository.HibernateSpeakerRepositoryImpl"/>

    <bean name="speakerService" class="com.example.conference.service.SpeakerServiceImpl">
        <!--=setter injection-->
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

    <bean id="emailClient" class="com.example.EmailClient">
<!--       <constructor-arg ref="basicSpellChecker"/>-->
           <property name="spellChecker" ref="basicSpellChecker"/>
        </bean>

        <bean id="basicSpellChecker" class="com.example.BasicSpellChecker">
        </bean>

        <bean id="advancedSpellChecker" class="com.example.AdvancedSpellChecker">
        </bean>
        <!-- more bean definitions go here -->

</beans>
```

#### xml: autowiring
Yes, we can do autowiring with xml configuration as well, with no annotations.

Before we saw that when defining the beans in a xml file, we also define explicitly how we want to inject the dependencies in each of them. We used elements `<constructor-arg>` for the case of constructor injection, and `<property>` for the case of setter injection.

However, we can instruct Spring to do a constructor or setter injection for us to wire-up the dependencies a given bean needs, as far as the bean class has the needed setters and constructor to perform the injection of its dependency. This is how we would do it:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean name="speakerRepository"
          class = "com.example.conference.repository.HibernateSpeakerRepositoryImpl"/>

    <bean name="speakerService" class="com.example.conference.service.SpeakerServiceImpl" autowire="constructor">
    </bean>

</beans>
```
Here Spring will call the constructor of the `SpeakerServiceImpl` class to set the dependency `speakerRepository` this class has, passing as argument the bean `speakerRepository`, which is the only bean assignment compatible available.

Autowire using the setter would need instead the autowire parameter as `autowire="byType"` or `autowire="byName"`. 

In both these cases the setter named ("camelcased") after the dependency type (not its name!) we want to inject will be called.

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

#### xml: field injection
afdafadsflkj lfjlkj lkfj;laksj
kjl;kjlkj lkjlkjl lkjlkjljljaldsjflkj akfja;lj

### Java configuration

Instead of xml configuration we can make the Spring IoC container to read beans configuration from a Java configuration class. Most newly developed applications in Spring use Java configuration because it is easier to understand. We can create the configuration class in the root package of our project. 

Almost everything in Spring can now be configured using Java configuration.

The configuration class can be annotated with `@Configuration` which give other functionalities that I still don't understand. But this annotation will mark configuration classes that we can pass to 
`AnnotationConfigApplicationContext` to initialize the container with the beans in all of them.

#### Java: constructor injection

The example below will create our beans exactly in the same way as the previously seen `beans.xml` file. As can be seen, now the beans will be created through methods annotated with `@Bean`, which will be called automatically by Spring when the application context is started. The `@Bean` annotation can be used only at a method level.

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






### Annotations (autowiring)
When using either a xml configuration file, or a Java configuration class, we are manually specifying which are the beans we want to create and how we want to inject the dependencies a given class needs (constructor or setter injection). In other words we are _manually_ defining and wiring our beans, <u>all in a same file (xml or Java)</u>.

However, Spring can do the beans definition and wiring for us: Spring can do _**autowiring**_. The difference now is that these two operations will be done in separate files. Spring will use a Java, or xml, file to define where we want to look for beans. Spring will use then the very beans class files to define which classes will be beans and to set the wiring (dependency injection) type.  

Autowiring is specially useful in big projects with many beans and dependencies among them. It is an example of _"convention over configuration"_.

### Autowiring: where are the beans?
If we want to use a Java class to define where we want Spring to look for beans, it's enough to define a Java configuration class with empty body, and annotate it with `@ComponentScan`. We then pass to this annotation the base package(s) where the classes we want to make beans are:
```java
@ComponentScan("com.example")
    public class AppConfig {
}
```
To pass several base packages to scan for beans we list them as `@ComponentScan({"com.example",com.plumbe})`.
If instead we want to do the same through a xml file, we should set it as:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://www.springframework.org/schema/beans
https://www.springframework.org/schema/beans/spring-beans.xsd">

    <context:component-scan base-package="com.example"></context:component-scan>
            
</beans>
```
The process of beans discovery, when configuring our beans through annotations, is called _Component Scanning_. Classes we want to be discovered in the component scan need to be annotated with `@Component` though.

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
When we want to do autowire by constructor we annotate the constructor with `@Autowired`. Then the types of wiring we can do (type, name etc.) follow the same rules as for setter injection, which we discuss below.   
All the beans needed to successfully do the dependency injection need to be defined with `@Component` (or some stereotype annotation), and there should be Java configuration class with the `@ComponentScan`.

If we do constructor injection, i.e. we use the constructor of a bean to set its dependencies, and these dependencies are of <u>primitive types</u>, we need to pass values for them somehow to the constructor. In this case, other than the `@Autowire` annotation in the constructor, we use `@Value` to pass the values to the constructor parameter.
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
Annotating a setter with `@Autowired` makes Spring to automatically call it to inject a dependency the setter's class needs. This call happens after the default constructor of this class is called to get its bean, either explicitly in a `@Bean` annotated method of a Java configuration class, or implicitly if that class is annotated with `@Component` and included in the components scan. This how setter injection works: the no-args constructor of the bean is called first, and then the `@Autowired` annotated setter is called to inject the dependency.

In **_autowire by type_** we specify a precise class (not interface) as parameter to the setter, when we want to do setter injection. Our `EmailClient` class would be:

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
**_Autowire with the @Primary_** annotation works by selecting the bean annotated as such, whenever Spring needs to inject one of many assignment compatible beans in one (interface) dependency. For example, if we want to always inject an `AdvancedSpellChecker` 
bean in the `SpellChecker` dependency of the `EmailClient` class, it would be enough to annotate class `AdvancedSpellChecker` with `@Primary`, and leave the setter in `EmailClient` in its polymorphic (interface) form:

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
Bean classes can be marked through any of the so called _stereotype annotations_ are:
- `@Component`: Used for general purpose beans.
- `@Repository`: Used to denote classes used as repository objects.
- `@Service`: Used for business logic beans. It doesn't mean a web service or a microservice. 
- `@Controller`: used in Spring MVC

We can use filters to look for specific types of annotations. We can pass as argument the name we want for the bean that will be created with these annotations, eg `@Service("speakerService")`.


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
<bean id="myService" class="com.example.MyService" scope="prototype" />
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
@ComponentScan({"com.example.conference"})
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

____________________________________________

## Spring Expression Language, SpEL ?
The Spring Expression Language can be used to:
- Manipulate an object already created? 
- Evaluate and inject values at run time and change the behaviour of our code accordingly.
- Evaluate and manipulate configuration

For example, we can inject a value to a primitive dependency of a class like
```java
    @Value("#{ T(java.lang.Math).random()*100 }")
    private double seedNum;
```

Maintainable applications use externalized configuration. In Spring, one way of achieving this is injecting properties from a properties files using SpEL. The properties file is usually placed in the resources directory.

Using annotation `@PropertiesSource` in a class, we instruct Spring to read the specified properties file and load its content into the application context. Then, using the `@Value` annotation we can inject the required property into the class field:
```java
@Component
@PropertySource(value = "classpath:/application.properties")
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

## Spring AOP proxies ?
Proxies is used to inject behaviour into existing code without modifying it.

## Beans profiles
An environment is a collection of properties and profiles. Interface `ApplicationContext` extends interface `EnvironmentCapable` which has method a method returning an `Environment`. 

A profile is an environment specific configuration.

Beans profiles were introduced in Spring to help code adapt to different environments. This feature allows setting up code that gets run only in specific environments, so we can swap out configuration at run time.

The `@Profile("profile_name")` annotation in a bean classes, specifies in which "profile" such bean will be available. The profile name can be anything. When we run the application, we then need to pass the profile to the JVM (VM option), for example, `-Dspring.profiles.active=dev`, for a profile called dev.

We may use profiles, for example, to ensure that some code doesn't get into production.