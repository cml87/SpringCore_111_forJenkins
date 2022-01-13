package com.example.matthew.lifecycle;

import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepository {

    public void loadStaticData(){
        System.out.println("Loading static data ..");
    }

}
