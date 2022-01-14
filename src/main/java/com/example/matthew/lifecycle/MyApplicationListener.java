package com.example.matthew.lifecycle;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

@Component
public class MyApplicationListener implements ApplicationListener<ApplicationEvent> {

    // the event will contain the application context object, so we can do things with it programmatically
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println(" -> Application event: "+ event.toString());
        //ApplicationContext applicationContext = event.getApplicationContext();
    }
}
