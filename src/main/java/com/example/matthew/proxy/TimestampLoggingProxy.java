package com.example.matthew.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;

// This is going to be a dynamic proxy
public class TimestampLoggingProxy implements InvocationHandler {

    // this will be the delegate
    private Object delegate;

    // notice the private constructor
    private TimestampLoggingProxy(Object delegate) {
        this.delegate = delegate;
    }

    public static Object getProxyFor(Object o){
         return Proxy.newProxyInstance(
                 o.getClass().getClassLoader(),
                 o.getClass().getInterfaces(),
                 new TimestampLoggingProxy(o)
         );
    }


   // This method will be automatically invoked when a method of the target class which is being proxied through is invoked.
   // It will receive in the parameters the proxy object `proxy`, the method which is being invoked `method` and the arguments
    // `args` passed to that method being invoked. We can do whatever we want with this information in the `invoke()` method.
    // We could even decide to not call the method of the target class at all.=

    // java.lang.reflect has not been refactored to use generics, so we still need to use Object and casting, as in the old times
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Thread.sleep(1000);
        System.out.println(new Date()); // this is the added functionality
        return method.invoke(delegate, args); // this is a call to the original method

    }
}
