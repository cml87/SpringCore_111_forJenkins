package com.example.matthew.springproxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class PersonAdviceInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        System.out.println("<I want to say:");
        Object result = invocation.proceed();
        System.out.println("That's all>");

        return result;
    }
}
