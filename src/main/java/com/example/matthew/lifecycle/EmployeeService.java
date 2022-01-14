package com.example.matthew.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostConstruct
    public void myPostConstruct() throws Exception {
        employeeRepository.loadStaticData();
    }

    @PreDestroy
    public void myPreDestroy() throws Exception {
        System.out.println("Destroying service bean ...");
    }
}
