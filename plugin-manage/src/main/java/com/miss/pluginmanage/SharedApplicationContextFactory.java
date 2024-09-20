package com.miss.pluginmanage;

import com.miss.pluginapi.PluginsRootGetter;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class SharedApplicationContextFactory {

    public static ApplicationContext create(ApplicationContext rootContext) {
        GenericApplicationContext sharedContext = new GenericApplicationContext();
        sharedContext.registerShutdownHook();
        ConfigurableListableBeanFactory beanFactory = sharedContext.getBeanFactory();
        rootContext.getBeanProvider(PluginsRootGetter.class)
                .ifUnique(pluginsRootGetter ->
                        beanFactory.registerSingleton("pluginRootGetter", pluginsRootGetter));

        sharedContext.refresh();
        return sharedContext;
    }
}
