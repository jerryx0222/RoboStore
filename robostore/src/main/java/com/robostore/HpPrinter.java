package com.robostore;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class HpPrinter implements Printer {
    private int count;

    @PostConstruct
    public void initialize(){
        count = 5;
    }

    @Override
    public void print(String message){
        count--;
        System.out.println("Hp Printer:" + message);
        System.out.println("剩餘次數:" + count);
    }

    

    
}
