package com.example.matthew.springproxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class AdviceInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        System.out.println("Starting the car ...");
        Object result = invocation.proceed();

        System.out.println("The car has been started");

        return result;
    }
}
