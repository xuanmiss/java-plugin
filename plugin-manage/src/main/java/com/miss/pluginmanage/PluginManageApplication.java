package com.miss.pluginmanage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableAutoConfiguration
@SpringBootApplication
public class PluginManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(PluginManageApplication.class, args);
    }

}
