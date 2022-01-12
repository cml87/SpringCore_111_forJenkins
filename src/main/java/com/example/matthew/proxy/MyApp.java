package com.example.matthew.proxy;

import org.springframework.aop.framework.ProxyFactoryBean;

public class MyApp {
    public static void main(String[] args) {

        System.out.println("invoking directly");
        new PersonImp().greet();

        System.out.println("\ninvoking through the interface");
        Person p = new PersonImp();
        p.greet();
        System.out.println("");
        System.out.println("invoking through handmade proxy");
        Person p1 = new HandmadeProxy(new PersonImp());
        p1.greet();

        System.out.println("\ninvoking through java.lang.reflect dynamic proxy");
        Person p2 = (Person) TimestampLoggingProxy.getProxyFor(new PersonImp());
        p2.greet();
        p2.greetInFrench();

        System.out.println("\ninvoking through Spring CGLIB dynamic proxy");
        // PPerson is a class with no interface
        PPerson p3 = new PPerson();
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(p3);
        PPerson bean = (PPerson) proxyFactoryBean.getObject();
        bean.greet();

        System.out.println("\ninvoking through Spring JDK dynamic proxy");
        Person p4 = new PersonImp();
        ProxyFactoryBean proxyFactoryBean2 = new ProxyFactoryBean();
        proxyFactoryBean2.setTarget(p4);
        Person bean2 = (Person) proxyFactoryBean2.getObject();
        bean2.greet();



    }

}
