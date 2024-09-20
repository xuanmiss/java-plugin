package com.miss.pluginmanage;


import com.github.zafarkhaja.semver.Version;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DefaultSystemVersionSupplier implements SystemVersionSupplier {
    private static final String DEFAULT_VERSION = "0.0.0.0";

    private final ObjectProvider<BuildProperties> buildProperties;

    public DefaultSystemVersionSupplier(ObjectProvider<BuildProperties> buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Override
    public Version get() {
        var properties = buildProperties.getIfUnique();
        if (properties == null) {
            return Version.parse(DEFAULT_VERSION);
        }
        var projectVersion = Objects.toString(properties.getVersion(), DEFAULT_VERSION);
        return Version.parse(projectVersion);
    }

}
