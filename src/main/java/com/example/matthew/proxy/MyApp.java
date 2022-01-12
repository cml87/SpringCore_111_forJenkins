package com.example.matthew.proxy;

public class MyApp {
    public static void main(String[] args) {

        // invoking directly
        new PersonImp().greet();
        System.out.println("");
        // invoking through the interface
        Person p = new PersonImp();
        p.greet();
        System.out.println("");
        //invoking through handmade proxy
        Person p1 = new HandmadeProxy(new PersonImp());
        p1.greet();

        System.out.println("");
        //invoking through java.lang.reflect dynamic proxy
        Person p2 = (Person) TimestampLoggingProxy.getProxyFor(new PersonImp());
        p2.greet();
        p2.greetInFrench();



    }

}
