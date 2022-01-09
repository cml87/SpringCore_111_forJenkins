package com.example.matthew;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    public static void main(String[] args) {
        //ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContextMatthew.xml");
        //ApplicationContext ctx = new AnnotationConfigApplicationContext("com.example.matthew");
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);


        MyService service1 = ctx.getBean(MyService.class);
        MyService service2 = ctx.getBean(MyService.class);

        service1.doBusinessLogic();
    }

}