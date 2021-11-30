# Spring Core 1
In this project I will follow the 52 minutes video
<span style="color:aquamarine">Spring Core Framework Tutorial | Full Course</span>.
at https://www.youtube.com/watch?v=ZwcHeLhvuq4
It is a general introduction to Spring Core, which seems good to me.

Enriched with content from course <span style="color:aquamarine">Spring Framework: Spring Fundamentals</span>, by Bryan Hansen **pluralsight**

## Spring Core general
Spring was developed to make enterprise Java development (already existing tasks) easier, as an improvement over EJBs. It holds to many best practices and well known design patterns. It allows us to actually write better Java code, because it allows for loosely coupled classes and more testable code. It makes our application configurable, instead of using hard coded settings.

Spring started out just and IoC (**Inversion of Control**) container, a technique also called dependency injection. It was conceived to reduce or replace some of the complex configuration in early Java EE applications. **_Later_**, Spring started to build around building enterprise applications without EJBs. They initially were just figuring out how to work better with EJBs, but then discovered that EJBs were actually not needed for a lot of situations.

Dependency injection is removing hard coded wiring in our app. and using a framework to inject dependencies and resources where they are needed.

Spring can essentially be used with or without EJBs, but nowadays it is primarily used without them. No needs of EJBs means no need of an application server, such as Wildfly. So Spring allows developing enterprise applications without an application server. Spring only need a web server, and by defaults uses Tomcat (easy to use and lightweight). Before Spring it was not easy to have enterprise features in an application deployed in Tomcat.

<u>Spring is completely POJO based and interface driven</u>. Springs uses AOP and Proxies to apply things as transactions to our code to get those 'cross cutting concerns' ? out of our code, producing smaller and lightweight applications.

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

Spring allows for automatic Dependency Injection (or **_Inversion of Control_**, IoC).
This was, in fact, the initial purpose of the Spring framework. Spring creates the 
Java objects, or Spring beans, and injects them at runtime.

Fig. min 5:49

The Spring framework is distributed through jars. Springs wants yuo to use Maven or Gradle, to download its jars, instead of doing it manually. This is because normally 
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

Spring creates objects and inject them into our application at runtime. This functionality is provided by the Sprint **IoC Container**, which create objects, inject the needed dependencies into them, and manage their lifecycle.

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
#### xml: constructor injection

The xml file above shows an example xml beans configuration, and constructor dependency injection. Here we have a _Spring bean_ for each class of our code. Spring beans are nothing more than JavaBeans managed by the Spring IoC Container. Each bean in this file will replace the `new` keyword when we create an object. The Spring IoC container will read the configuration data from this xml file and will create the objects automatically.

When we set the dependency of a class in its constructor, as in the `EmailClient` class above, we are doing **constructor injection**. We do this in our beans definition through the tag `<constructor-arg>`. This is how we do the wiring, or dependency 
injection, without touching our code!

Note... In the example, our constructor has only one argument, so we don't need to specify which bean we want to inject when calling the `EmailClient` constructor in its bean definition in the xml file. When there is more than one constructor argument (we need to inject more than one dependency) ... 

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

The **Application Context** is Spring "container" of configured and managed beans. In other words, it is the set of Spring beans autowired.

It seems that when we initialize an application context, an instance of each bean is created, even if we have not yet asked for any bean to the container. I discovered this using bean lifecycle hooks (interface `InitializingBean`, see below).

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
#### xml: field injection
afdafadsflkj lfjlkj lkfj;laksj
kjl;kjlkj lkjlkjl lkjlkjljljaldsjflkj akfja;lj

### Java configuration

Instead of xml configuration we can make the Spring IoC container to read beans configuration from a Java configuration class. Most newly developed applications in Spring use Java configuration 
because it is easier to understand. We can create the configuration class in the root package of our project. 

Almost everything in Spring can now be configured using Java configuration.

The configuration class can be annotated with `@Configuration` which give other functionalities that I still don't understand.

#### Java: constructor injection

The example below will create our beans exactly in the same way as the previously seen `beans.xml` file.

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

However, Spring can do the beans definition and wiring for us: Spring can do _**autowiring**_ The difference now is that these two operations will be done in separate files. Spring will use a Java or xml file to define where we want to look for beans, and will use the bean classes files themselves to set the wiring (dependency injection) type.  

Autowiring is specially useful in big projects with many beans and dependencies among them.

When using a Java class to define where we want Spring to look for beans, it's enough to define a Java configuration class with empty body, and annotate it with `@ComponentScan`. We then pass to this annotation the base package where the classes we want to make beans are:
```java
@ComponentScan("com.example")
    public class AppConfig {
}
```
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

 After, to tell Spring how we want to wire our beans we use the `@Autowired` annotation in a constructor, a setter method, or a field, depending on the type of dependency injection we want to do.

When wiring the beans (or injecting the dependencies) through annotations in the body of the bean class, Spring needs to know which of the possibly many assignment compatible available beans, we want to wire up in a given dependency. Remember, we will be using interfaces in most cases as dependencies in our classes, and our beans will be classes implementing those interface. 

Before this piece of information was given explicitly while wiring the beans in the xml file or Java class. Now, when using annotations to wire beans, we'll have four options, <u>all of which can be used with constructor, setter or field injection</u> !: 
1. autowire by type
2. autowire by name
3. autowire with annotation `@Primary`
4. autowire with annotation `@Qualifier`

#### annotations: constructor injection
When we want to do autowire by constructor we annotate the constructor with `@Autowired`. Then the types of wiring we can do (type, name etc.) follow the same rules as for setter injection, which we discuss below.   

#### annotations: setter injection
In **_autowire by type_** we specify a precise class (not interface) as parameter to the setter, when we want to do setter injection. Our `EmailClient` class would be:

```java
@Component
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

## Bean scope
The bean scope determines how many times the bean can be initialized in the IoC container. 
There are 6 beans scopes:
- **Singleton**: Default by omission. The container will create only  
  one bean of each type, at the time of the container start-up, and then will reuse the bean through the _application context_.
- **Prototype**: a new bean is created each time it is requested.
- Request: (Web applicatins). A unique bean will be created for each incoming HTTP _request_.
- Session: (Web applicatins). A unique bean will be created for each incoming HTTP _session_.
- Application: (Web applications). One bean per servlet environment ?
- Websocket: (Web applications). One bean per websocket environment ?

Usage: @Scope("singleton") annotating the bean class.

## Bean lifecycle
The bean lifecycle is a set of steps Spring performs for creating, using, 
and destroying a bean at the time of application shutdown. The lifecycle has three phases:
1. Initialization phase: Initialize the bean, collect all the required properties and 
   instantiate the bean.
2. Bean usage
3. Destruction phase: Spring destroy the beans (frees up its memory I guess).

There are mainly three ways to interact with the bean lifecycle. We may need this, for 
example, if we want to read some files and do some business logic, at the start of the application.
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




 