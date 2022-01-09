package com.example.matthew.business;

import com.example.matthew.data.MyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnotherServiceImpl implements MyService {

    @Override
    public void doBusinessLogic() {
        System.out.println("Doing business logic slightly different!");
     }

}
