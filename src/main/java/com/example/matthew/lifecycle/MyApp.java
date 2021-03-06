package com.example.matthew.lifecycle;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class MyApp {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.example.matthew.lifecycle");
        ((AnnotationConfigApplicationContext)applicationContext).start();

        //((AbstractApplicationContext) applicationContext).close();
        ((AnnotationConfigApplicationContext)applicationContext).registerShutdownHook();

        System.out.println("app is working ...");
    }

}
