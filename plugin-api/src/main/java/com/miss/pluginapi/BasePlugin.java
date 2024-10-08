package com.miss.pluginapi;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.Plugin;

@Getter
@Slf4j
public class BasePlugin extends Plugin {


    protected PluginContext context;

    public BasePlugin(PluginContext pluginContext) {
        this.context = pluginContext;
    }

    public BasePlugin() {}
}
