package com.example.matthew.transactional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Transactional
    public void placeOrder(){
        System.out.println("Placing order ...");
    }
}
