package com.example.matthew.proxy;

public class MyApp {
    public static void main(String[] args) {

        // invoking directly
        new PersonImp().greet();

        // invoking through the interface
        Person p = new PersonImp();
        p.greet();


    }

}
