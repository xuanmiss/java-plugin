package com.miss.pluginmanage;

import com.miss.pluginapi.PluginContext;
import org.pf4j.Plugin;
import org.springframework.context.ApplicationContext;

public class SpringPlugin extends Plugin {

    private ApplicationContext context;

    private Plugin delegate;

    private final PluginContext pluginContext;

    public SpringPlugin(PluginContext pluginContext) {
        this.pluginContext = pluginContext;
    }
}
