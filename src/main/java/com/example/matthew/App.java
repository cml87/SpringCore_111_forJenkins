package com.example.matthew;

import com.example.matthew.business.MyService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    public static void main(String[] args) {
        //ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContextMatthew.xml");
        //ApplicationContext ctx = new AnnotationConfigApplicationContext("com.example.matthew");
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);


        MyService service = ctx.getBean(MyService.class);

        service.doBusinessLogic();
    }

}