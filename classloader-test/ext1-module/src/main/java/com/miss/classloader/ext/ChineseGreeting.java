package com.miss.classloader.ext;

import com.miss.classloader.main.IGreeting;

public class ChineseGreeting implements IGreeting {


    @Override
    public void greeting(String world) {
        System.out.println("ext1-module: 你好 " + world);
    }
}
