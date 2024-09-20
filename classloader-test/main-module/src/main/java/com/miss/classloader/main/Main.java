package com.miss.classloader.main;

import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println("Hello world!");
        invokeCustomerClassMethod("D:\\code_space\\ideaProjects\\github\\xuanmiss\\java-plugin\\classloader-test\\ext1-module\\target\\classes");
        invokeCustomerClassMethod("D:\\code_space\\ideaProjects\\github\\xuanmiss\\java-plugin\\classloader-test\\ext2-module\\target\\classes");

    }

    private static void invokeCustomerClassMethod(String path) throws ClassNotFoundException {
        ClassLoader classLoader = new CustomerClassloader(path);
        System.out.println("customer classloader is: " + classLoader.toString());
        Class<IGreeting> aClass = (Class<IGreeting>) classLoader.loadClass("com.miss.classloader.ext.ChineseGreeting");
        try {
            Object o = aClass.getDeclaredConstructor().newInstance();
            aClass.getMethod("greeting", String.class).invoke(o, "中国欢迎您！");
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        Class<IGreeting> defaultClass = (Class<IGreeting>) classLoader.loadClass("com.miss.classloader.main.DefaultGreeting");
        try {
            Object o = defaultClass.getDeclaredConstructor().newInstance();
            defaultClass.getMethod("greeting", String.class).invoke(o, "中国欢迎您！");
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }


        try {
            Class<?> ext1Class = classLoader.loadClass("com.miss.classloader.ext.Ext1Class");
            Object o = ext1Class.getDeclaredConstructor().newInstance();
            ext1Class.getMethod("hello").invoke(o);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
           e.printStackTrace();
        }


        try {
            Class<?> ext2Class = classLoader.loadClass("com.miss.classloader.ext.Ext2Class");
            Object o = ext2Class.getDeclaredConstructor().newInstance();
            ext2Class.getMethod("hello").invoke(o);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

    }
}
