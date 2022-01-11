package com.example.matthew.proxy;

public class MyApp {
    public static void main(String[] args) {

        // invoking directly
        new PersonImp().greet();
        System.out.println("");
        // invoking through the interface
        Person p = new PersonImp();
        p.greet();
        System.out.println("");
        //invoking through as proxy
        Person p1 = new Proxy(new PersonImp());
        p1.greet();

    }

}
