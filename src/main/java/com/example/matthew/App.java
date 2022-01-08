package com.example.matthew;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(("applicationContextMatthew.xml"));
        MyService bean1 = ctx.getBean(MyService.class);
        MyService bean2 = ctx.getBean(MyService.class);
        MyService bean3 = ctx.getBean(MyService.class);

        bean1.doSomething();
    }

}