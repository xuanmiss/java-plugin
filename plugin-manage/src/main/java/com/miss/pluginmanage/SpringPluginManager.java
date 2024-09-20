package com.miss.pluginmanage;

import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.springframework.context.ApplicationContext;

import java.util.List;

public interface SpringPluginManager extends PluginManager {

    ApplicationContext getRootContext();

    ApplicationContext getSharedContext();

    List<PluginWrapper> getDependencies(PluginWrapper pluginWrapper);
}
