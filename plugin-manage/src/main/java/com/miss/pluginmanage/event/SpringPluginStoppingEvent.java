package com.miss.pluginmanage.event;

import com.miss.pluginmanage.SpringPlugin;
import org.springframework.context.ApplicationEvent;

public class SpringPluginStoppingEvent extends ApplicationEvent {

    private final SpringPlugin springPlugin;

    public SpringPluginStoppingEvent(Object source, SpringPlugin springPlugin) {
        super(source);
        this.springPlugin = springPlugin;
    }

    public SpringPlugin getSpringPlugin() {
        return springPlugin;
    }
}
