package com.example.matthew.springproxy;

import org.springframework.aop.framework.ProxyFactory;

public class MyApp {

    public static void main(String[] args) {

        Car car = new Car();
        System.out.println("- Call to car object method without proxy: ");
        car.start();

        Car car1 = new Car();
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.addAdvice(new AdviceInterceptor());
        proxyFactory.setTarget(car1);

        Car proxiedCar = (Car) proxyFactory.getProxy();
        System.out.println("- Call to car object method without proxy: ");
        proxiedCar.start();

        System.out.println("proxy car object is of type: "  + proxiedCar.getClass().getName());

    }

}
