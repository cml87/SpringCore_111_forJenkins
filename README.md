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
method of this class.