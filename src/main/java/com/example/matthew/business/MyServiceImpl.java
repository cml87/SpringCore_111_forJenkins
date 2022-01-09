package com.example.matthew.business;

import com.example.matthew.data.MyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class MyServiceImpl implements MyService {

    @Value("${my.name}")
    private String name;

    private MyRepository repository;

    @Autowired
    public MyServiceImpl(MyRepository repository) {
        this.repository = repository;
     }

    @Override
    public void doBusinessLogic() {
        System.out.println("Doing business logic for " + name);
        repository.doQuery();
    }

}
