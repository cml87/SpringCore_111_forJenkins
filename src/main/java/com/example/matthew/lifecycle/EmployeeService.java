package com.example.matthew.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService implements InitializingBean, DisposableBean {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        employeeRepository.loadStaticData();
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("Destroying service bean ...");
    }
}
