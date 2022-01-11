package com.example.matthew.proxy;

public class Proxy implements Person{

    private Person delegate;

    public Proxy(Person delegate) {
        System.out.println("Actually, ");
        this.delegate = delegate;
    }

    @Override
    public void greet() {
        System.out.println("I just want to say ...");
        delegate.greet();
    }
}
