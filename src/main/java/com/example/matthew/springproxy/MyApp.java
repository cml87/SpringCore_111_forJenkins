package com.example.matthew.springproxy;

import org.springframework.aop.framework.ProxyFactory;

public class MyApp {

    public static void main(String[] args) {

        System.out.println("****** Spring CGLIB proxy *******");

        Car car = new Car();
        System.out.println("- Call to car object method without proxy: ");
        car.start();

        Car car1 = new Car();
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.addAdvice(new CarAdviceInterceptor());
        proxyFactory.setTarget(car1);

        Car proxiedCar = (Car) proxyFactory.getProxy();
        System.out.println("- Call to car object method without proxy: ");
        proxiedCar.start();

        System.out.println("proxy car object is of type: "  + proxiedCar.getClass().getName());

        System.out.println("\n****** Spring SDK Dynamic proxy *******");

        Person person = new PersonImp();
        person.greet();
        person.greetInFrench();

        System.out.println("Now we'll use tht proxy class:");
        ProxyFactory proxyFactory1 = new ProxyFactory();
        proxyFactory1.addAdvice(new PersonAdviceInterceptor());
        proxyFactory1.setInterfaces(Person.class);
        proxyFactory1.setTarget(person);

        Person person1 = (Person) proxyFactory1.getProxy();
        person1.greet();
        person1.greetInFrench();

        System.out.println("proxy person object is of type: "  + person1.getClass().getName());


    }

}
