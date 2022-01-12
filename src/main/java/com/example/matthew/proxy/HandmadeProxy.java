package com.example.matthew.proxy;

public class HandmadeProxy implements Person{

    private Person delegate;

    public HandmadeProxy(Person delegate) {
        System.out.println("Actually, ");
        this.delegate = delegate;
    }

    @Override
    public void greet() {
        System.out.println("I just want to say ...");
        delegate.greet();
    }

    @Override
    public void greetInFrench() {

    }
}
