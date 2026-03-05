package com.robostore;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Teacher {
    private Printer printer;

    public Teacher(@Qualifier("canonPrinter") Printer printer) {
        this.printer = printer;
    }

    public void teach() {
        printer.print("I'm a teacher");
    }

}
