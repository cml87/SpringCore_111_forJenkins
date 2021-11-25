package com.example;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class MyBeansLifeCycleHooks {

    @PostConstruct
    public void myPostConstruct(){
        System.out.println("oooo inside my PostConstruct");
    }

    @PreDestroy
    public void myPreDestroy(){
        System.out.println("ooo inside my PreDestroy");
    }

}
