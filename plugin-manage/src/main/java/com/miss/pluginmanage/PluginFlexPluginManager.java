package com.miss.pluginmanage;



import com.miss.pluginapi.PluginsRootGetter;
import org.apache.logging.log4j.util.Lazy;
import org.pf4j.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import javax.swing.*;
import java.util.List;

public class PluginFlexPluginManager extends DefaultPluginManager implements SpringPluginManager, InitializingBean {


    private final ApplicationContext rootContext;

    private Lazy<ApplicationContext> sharedContext;

    private final PluginProperties pluginProperties;

    private final PluginsRootGetter pluginsRootGetter;

    private final SystemVersionSupplier systemVersionSupplier;

    public PluginFlexPluginManager(ApplicationContext rootContext,
                                   PluginProperties pluginProperties,
                                   PluginsRootGetter pluginsRootGetter,
                                   SystemVersionSupplier systemVersionSupplier) {
        this.rootContext = rootContext;
        this.pluginProperties = pluginProperties;
        this.pluginsRootGetter = pluginsRootGetter;
        this.systemVersionSupplier = systemVersionSupplier;
    }

    @Override
    protected void initialize() {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.runtimeMode = pluginProperties.getRuntimeMode();
        this.sharedContext = Lazy.lazy(() -> SharedApplicationContextFactory.create(rootContext));
        setExactVersionAllowed(pluginProperties.isExactVersionAllowed());
        setSystemVersion(systemVersionSupplier.get().toStableVersion().toString());
        super.initialize();
    }

    @Override
    protected ExtensionFactory createExtensionFactory() {
        return new SpringExtensionFactory(this);
    }

    @Override
    protected ExtensionFinder createExtensionFinder() {
        SpringComponentsFinder finder = new SpringComponentsFinder(this);
        addPluginStateListener(finder);
        return finder;
    }

    @Override
    protected PluginFactory createPluginFactory() {
        DefaultPluginApplicationContextFactory contextFactory = new DefaultPluginApplicationContextFactory(this);
    }

    @Override
    public ApplicationContext getRootContext() {

        return rootContext;
    }

    @Override
    public ApplicationContext getSharedContext() {
        return sharedContext.get();
    }

    @Override
    public List<PluginWrapper> getDependencies(PluginWrapper pluginWrapper) {
        return List.of();
    }


}
