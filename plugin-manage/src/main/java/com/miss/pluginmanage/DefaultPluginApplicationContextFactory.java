package com.miss.pluginmanage;

import lombok.extern.slf4j.Slf4j;
import org.pf4j.PluginWrapper;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.function.RouterFunction;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class DefaultPluginApplicationContextFactory implements PluginApplicationContextFactory {

    private final SpringPluginManager pluginManager;

    public DefaultPluginApplicationContextFactory(SpringPluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @Override
    public ApplicationContext create(String pluginId) {
        log.debug("准备为插件 {} 创建 application context", pluginId);

        PluginApplicationContext context = new PluginApplicationContext(pluginId, pluginManager);
        context.setBeanNameGenerator(DefaultBeanNameGenerator.INSTANCE);
        context.registerShutdownHook();
        context.setParent(pluginManager.getSharedContext());
        PluginWrapper pluginWrapper = pluginManager.getPlugin(pluginId);
        ClassLoader classLoader = pluginWrapper.getPluginClassLoader();
        ResourceLoader resourceLoader = new DefaultResourceLoader(classLoader);
        context.setResourceLoader(resourceLoader);

        MutablePropertySources mutablePropertySources = context.getEnvironment().getPropertySources();

        resolvePropertySource(pluginId, resourceLoader)
                .forEach(mutablePropertySources::addLast);

        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        beanFactory.registerSingleton("pluginWrapper", pluginWrapper);



        return null;
    }

    private List<PropertySource<?>> resolvePropertySource(String plugin, ResourceLoader resourceLoader) {

        PluginProperties pluginProperties = pluginManager.getRootContext().getBeanProvider(PluginProperties.class)
                .getIfAvailable();
        if (pluginProperties == null) {
            return List.of();
        }
        YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();
        List<PropertySource<?>> propertySources = new ArrayList<PropertySource<?>>();
        PropertiesPropertySourceLoader  propertiesPropertySourceLoader = new PropertiesPropertySourceLoader();

        Path configPath = pluginProperties.getDefaultDir().resolve("configs");
        Stream.of(
                configPath.resolve(plugin + ".yml"),
                configPath.resolve(plugin + ".yaml")

        ).map(path -> resourceLoader.getResource(path.toUri().toString()))
                .forEach(resource -> {
                    List<PropertySource<?>> sources = loadPropertySources("plugin-defined-config", resource, yamlPropertySourceLoader);
                    propertySources.addAll(sources);
                });

        Stream.of(
                        configPath.resolve(plugin + ".properties")

                ).map(path -> resourceLoader.getResource(path.toUri().toString()))
                .forEach(resource -> {
                    List<PropertySource<?>> sources = loadPropertySources("plugin-defined-config", resource, propertiesPropertySourceLoader);
                    propertySources.addAll(sources);
                });
        return propertySources;

    }

    private List<PropertySource<?>> loadPropertySources(String propertySourceName,
                                                        Resource resource,
                                                        PropertySourceLoader propertySourceLoader
                                                        ) {
        if (!resource.exists()) {
            return List.of();
        }
        try {
            return propertySourceLoader.load(propertySourceName, resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
