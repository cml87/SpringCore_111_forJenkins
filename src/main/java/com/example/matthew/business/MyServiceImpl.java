package com.example.matthew.business;

import com.example.matthew.data.MyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Primary
public class MyServiceImpl implements MyService, EnvironmentAware {

    @Value("${my.name}")
    private String name;

    private Environment environment;

    private MyRepository repository;

    @Autowired
    public MyServiceImpl(MyRepository repository) {
        this.repository = repository;
     }

    @Override
    public void doBusinessLogic() {
        System.out.println("Doing business logic for " + name);
        System.out.println("Active profile is: "+ Arrays.toString(environment.getActiveProfiles()));
        repository.doQuery();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
