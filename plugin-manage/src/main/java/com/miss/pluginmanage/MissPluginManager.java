package com.miss.pluginmanage;



import org.apache.logging.log4j.util.Lazy;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginWrapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class MissPluginManager extends DefaultPluginManager implements SpringPluginManager, InitializingBean {


    private final ApplicationContext rootContext;

    private Lazy<ApplicationContext> sharedContext;


    @Override
    public ApplicationContext getRootContext() {

        return null;
    }

    @Override
    public ApplicationContext getSharedContext() {
        return null;
    }

    @Override
    public List<PluginWrapper> getDependencies(PluginWrapper pluginWrapper) {
        return List.of();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
