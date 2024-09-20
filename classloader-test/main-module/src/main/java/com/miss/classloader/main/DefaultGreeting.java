package com.miss.classloader.main;

public class DefaultGreeting implements IGreeting{


    @Override
    public void greeting(String world) {
        System.out.println("default main: 你好呀~~~ ");
    }
}
