package com.example.matthew.lifecycle;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

@Component
public class MyApplicationListener implements ApplicationListener<ContextStartedEvent> {

    // the event will contain the application context object, so we can do things with it programmatically
    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        System.out.println("Application context is created!");
        //ApplicationContext applicationContext = event.getApplicationContext();
    }
}
