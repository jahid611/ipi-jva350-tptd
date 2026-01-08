package com.ipi.jva350.exception;

public class SalarieException extends Exception {

    public SalarieException(String s) {
        super(s);
    }

    public SalarieException(String s, Exception e) {
        super(s, e);
    }

}
