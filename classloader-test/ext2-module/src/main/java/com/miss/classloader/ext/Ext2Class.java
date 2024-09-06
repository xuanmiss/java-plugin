package com.miss.classloader.ext;

public class Ext2Class {

    public void hello() {
        System.out.println(Thread.currentThread().getContextClassLoader().toString() + "hello, ext2 class");
    }
}
