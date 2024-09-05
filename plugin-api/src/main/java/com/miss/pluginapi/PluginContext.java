package com.miss.pluginapi;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.pf4j.RuntimeMode;

@Getter
@Builder
@RequiredArgsConstructor
public class PluginContext {
    
    private final String name;


    private final String version;

    private final RuntimeMode runtimeMode;

}
