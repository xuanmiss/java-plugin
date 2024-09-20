package com.miss.pluginmanage;

import lombok.extern.slf4j.Slf4j;
import org.pf4j.*;
import org.pf4j.processor.ExtensionStorage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SpringComponentsFinder extends AbstractExtensionFinder {

    public static final String EXTENSIONS_RESOURCE = "META/plugin-flex-components.idx";

    public SpringComponentsFinder(PluginManager pluginManager) {
        super(pluginManager);
        entries = new ConcurrentHashMap<>();
    }

    @Override
    public Map<String, Set<String>> readClasspathStorages() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Set<String>> readPluginsStorages() {
        throw new UnsupportedOperationException();
    }

    private Set<String> readPluginStorage(PluginWrapper pluginWrapper) {
        String pluginId = pluginWrapper.getPluginId();
        log.debug("从插件 {} 中读取插件存储", pluginId);
        HashSet<String> bucket = new HashSet<>();
        try {
            log.debug("读取 {}", EXTENSIONS_RESOURCE);
            ClassLoader classLoader = pluginWrapper.getPluginClassLoader();
            try (InputStream resourceStream = classLoader.getResourceAsStream(EXTENSIONS_RESOURCE)) {
                if (resourceStream == null) {
                    log.debug("没有找到 {} 扩展类定义文件", EXTENSIONS_RESOURCE);
                } else {
                    collectExtensions(resourceStream, bucket);
                }
            }
            debugExtensions(bucket);
        } catch (IOException e) {
            log.error("从 " +  EXTENSIONS_RESOURCE + " 读取 components 失败", e);
        }
        return bucket;
    }

    private void collectExtensions(InputStream inputStream, Set<String> bucket) throws IOException {
        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            ExtensionStorage.read(reader, bucket);
        }
    }

    @Override
    public void pluginStateChanged(PluginStateEvent event) {
        PluginState pluginState = event.getPluginState();
        String pluginId = event.getPlugin().getPluginId();
        if (pluginState == PluginState.UNLOADED) {
            entries.remove(pluginId);
        } else if (pluginState == PluginState.CREATED || pluginState == PluginState.RESOLVED) {
            entries.computeIfAbsent(pluginId, id -> readPluginStorage(event.getPlugin()));
        }
    }


}
