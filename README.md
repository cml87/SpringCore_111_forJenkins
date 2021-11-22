# Spring Core 1
In this project I will follow the 52 minutes video "Spring Core Framework Tutorial | Full Course" at https://www.youtube.com/watch?v=ZwcHeLhvuq4
It is a general instroduction to Spring Core, which seems good to me.

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
of a class we are interested on in and interface and make that class to implement 
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
**_Dependency Injection_**.

Fig. min 5:24

Spring allows for automatic Dependency Injection (or Inversion of Control, IoC).
This was, in fact, the initial purpose of the Spring framework. Spring creates the 
Java objects, or Spring beans, and injects them at runtime.

Fig. min 5:49


