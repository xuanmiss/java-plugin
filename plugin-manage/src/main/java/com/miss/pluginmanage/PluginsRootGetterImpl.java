package com.miss.pluginmanage;

import com.miss.pluginapi.PluginsRootGetter;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class PluginsRootGetterImpl implements PluginsRootGetter {

    private final PluginProperties pluginProperties;

    public PluginsRootGetterImpl(PluginProperties pluginProperties) {
        this.pluginProperties = pluginProperties;
    }

    @Override
    public Path get() {
        return pluginProperties.getDefaultDir();
    }
}
