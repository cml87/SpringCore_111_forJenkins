package com.example.matthew;

import com.example.matthew.business.MyService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;

public class App {

    public static void main(String[] args) {

       System.setProperty("spring.profiles.active","local, dev");


        //ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContextMatthew.xml");
        //ApplicationContext ctx = new AnnotationConfigApplicationContext("com.example.matthew");

        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        //env.getRequiredProperty("propertyName")


        MyService service = ctx.getBean(MyService.class);

        service.doBusinessLogic();

       // System.out.println("System property: "+ System.getenv("myname"));
    }

}