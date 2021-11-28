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

Spring creates objects and inject them into our application at runtime. This functionality is provided by the Sprint **IoC Container**, which create objects, inject the needed dependencies into them, and manage their lifecycle.

There are three ways to define our bean in Spring:
1. through xml configuration file
2. through Java configuration class
3. through annotations

Each of these in turns supports dependency injection through the following mechanisms:
1. constructor injection 
2. setter injection
3. field injection

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

It seems that when we initialize an application context, an instance of each bean is 
created, even if we have not yet asked for any bean to the container. I discovered 
this using bean lifecycle hooks (interface `InitializingBean`, see below).

#### xml: setter injection
fadljfldfjlkjflajdlfkj ladksjf;l k
jfl jalkfji

#### xml: field injection
afdafadsflkj lfjlkj lkfj;laksj
kjl;kjlkj lkjlkjl lkjlkjljljaldsjflkj akfja;lj

### Java configuration

Instead of xml configuration we can make the Spring IoC container to read beans configuration from a Java configuration class. Most newly developed applications in Spring use Java configuration 
because it is easier to understand. We can create the configuration class in the root package of our project. 

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


## Types of dependency injection
There are three types of (dependency) injection:
1. setter injection
2. constructor injection
3. field injection

In setter injection we inject the dependencies to the class through a defined setter method, 
whereas in the constructor injection we do it in the class constructor, as we have been doing 
so far.

Setter injection needs a default constructor (no-args) in the class where we want to inject the dependencies, 
other than the setters. The Spring IoC will call this constructor to instantiate an object of 
this class and then will call the setter to set, or inject, the dependency. So, to perform a 
setter injection our `EmailClient` class would be as:

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
If instead we want to use xml configuration to perform a setter injection, we would 
need to set the `beans.xml` file as
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
It seems that the way in which we want to inject the dependencies in a given class 
determines how we design it, how we set the xml file or Java configuration class to 
configure our beans, and how we get the bean from the IoC container after.

Once we have decided how to inject the dependencies in our class (constructor or setter), we need to configure 
either the beans xml file or the Java configuration class accordingly. Then, depending 
on which of the last two we chose, we use `ClassPathXmlApplicationContext` or 
`AnnotationConfigApplicationContext` to get the bean from the IoC container.

## Autowiring
When using either a xml configuration file, or a Java configuration class, 
we are manually specifying which are the beans we want to create and how we want to 
inject the dependencies a given class need (constructor or setter injection). In other 
words we are _manually_ wiring our beans. However, Spring can do all that for us, ie. 
it can create the beans and inject, or wire, them as needed; Spring can do _**autowiring**_.

Autowiring is specially useful in big projects with many beans and dependencies among them.

To perform beans autowiring, Spring first scan our project for beans, through something 
called _Component Scanning_. We tell Spring which classes we want to make Spring beans 
annotating them with `@Component` We tell Spring how we want to wire, or inject, them in one of 
many possible ways, as we'll see below.  

To use autowiring we still use a configuration class. However, we only need to annotate 
this class with `@ComponenScan` specifying the base package were we want Spring to look 
for beans, which will be classes annotated with `@Component`.

```java
@ComponentScan("com.example")
public class AppConfig {
}
```
To tell Spring how to wire our beans we use the `@Autowired` annotation in a constructor, 
a setter method, or a field, depending on the type of dependency injection we choose. 

When wiring the beans, or injecting the dependencies, Spring needs to know which of 
the possibly many assignment compatible available beans, we want to wire up in a given 
dependency. Remember, we will be using interfaces most of the time as dependencies in our  
classes, and our beans will be classes implementing that interface. To give this piece 
of information to Spring there are three ways: 
- autowire by type
- autowire by name
- autowire with annotation `@Primary`
- autowire with annotation `@Qualifier`

Suppose we choose setter injection. In this case we _autowire by type_ specifying a 
class (not interface) as parameter to the setter; our `EmailClient` class would be:

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

_Autowire by name_ is more subtle, and I don't like it. 
With this approach, we can still use the interface reference type in the setter, as 
Spring will use the name of the parameter to decide which of the assignment 
compatible beans to wire up. Suppose we want to wire a `BasicSpellChecker` with 
this approach, the class `EmailClient` will then be:
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
_Autowire with the `@Primary`_ annotation works by selecting the bean annotated as such 
whenever Spring needs to inject one of many assignment compatible beans in one
(interface) dependency. For example, if we want to always inject an `AdvancedSpellChecker` 
bean in the `SpellChecker` dependency of the `EmailClien` class, it would be enough to 
annotate class `AdvancedSpellChecker` with `@Primary` and leave the setter in `EmailClient` 
in its polymorphic form:

```java
    //...
    @Autowired
    //autowire with @Primary, either in the Basic or AdvancedSpellChecker class
    public void setSpellChecker(SpellChecker spellChecker) {
            this.spellChecker = spellChecker;
     }
    //...
```
_Autowire with @Qualifier_ works similar to autowire by type. In this case we use the 
interface reference type in the setter, but we specify which assignment compatible bean 
we want to wire up annotating the setter parameter with `@Qualifier` and passing the 
name of the bean. The name of the bean will be that of the class but starting with 
lower case.

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
Autowire with @Qualifier overwrites autowire with @Primary.

I think I will always use either autowire by type, with `@Primary` annotation of 
`@Qualifier`. 

We can use `@Autowired` and `@Qualifier` directly in the field dependency of the class. 
This would be _field injection_. Spring would insert the dependency of the class through 
the fields using reflections:

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
Field injection is the easiest to use, as it requires less code. However, it is not 
recommended. See https://www.vojtechruzicka.com/field-dependency-injection-considered-harmful/.
Spring recommends setter injection.

## Bean scope
The bean scope determines how many times the bean can be initialized in the IoC container. 
There are 6 beans scopes:
- **Singleton**: Default by omission. The container will create only  
  one bean of each type, at the time of the container start-up, and then will reuse the bean
  through the _application context_.
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




 