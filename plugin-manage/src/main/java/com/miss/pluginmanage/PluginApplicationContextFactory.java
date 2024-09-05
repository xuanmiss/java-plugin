package com.miss.pluginmanage;

import org.springframework.context.ApplicationContext;

public interface PluginApplicationContextFactory {

    ApplicationContext create(String pluginId);
}
