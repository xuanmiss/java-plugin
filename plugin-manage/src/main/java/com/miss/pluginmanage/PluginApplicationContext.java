package com.miss.pluginmanage;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.ResolvableType;

public class PluginApplicationContext extends AnnotationConfigApplicationContext {

    private final String pluginId;

    private final SpringPluginManager pluginManager;

    public PluginApplicationContext(String pluginId, SpringPluginManager pluginManager) {
        this.pluginId = pluginId;
        this.pluginManager = pluginManager;
    }

    public String getPluginId() {
        return pluginId;
    }

    @Override
    protected void onClose() {
        super.onClose();
    }
}
