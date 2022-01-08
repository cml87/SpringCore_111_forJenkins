package com.example.matthew;

import org.springframework.stereotype.Component;

@Component
public class MyRepository {

    public void doQuery() {
        System.out.println("Doing DB query!");
    }

}
