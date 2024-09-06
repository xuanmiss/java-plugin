package com.miss.classloader.ext;

public class Ext1Class {

    public void hello() {
        System.out.println(Thread.currentThread().getContextClassLoader().toString() + "hello, ext1 class");
    }
}
