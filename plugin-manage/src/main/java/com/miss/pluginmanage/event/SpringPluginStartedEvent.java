package com.miss.pluginmanage.event;

import com.miss.pluginmanage.SpringPlugin;
import org.springframework.context.ApplicationEvent;

public class SpringPluginStartedEvent extends ApplicationEvent {

    private final SpringPlugin springPlugin;

    public SpringPluginStartedEvent(Object source, SpringPlugin springPlugin) {
        super(source);
        this.springPlugin = springPlugin;
    }

    public SpringPlugin getSpringPlugin() {
        return springPlugin;
    }

}
