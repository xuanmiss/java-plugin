package com.miss.pluginmanage;


import lombok.Data;
import org.pf4j.RuntimeMode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

@Data
@ConfigurationProperties(prefix = "miss.plugin")
public class PluginProperties {

    private Path defaultDir;

    private RuntimeMode runtimeMode = RuntimeMode.DEPLOYMENT;

    private boolean exactVersionAllowed = false;
}
