package com.example.matthew.proxy;

public class PersonImp implements Person {

    @Override
    public void greet(){
        System.out.println("Hello there !");
    }

    @Override
    public void greetInFrench() {
        System.out.println("Salut mon ami!");
    }

}
