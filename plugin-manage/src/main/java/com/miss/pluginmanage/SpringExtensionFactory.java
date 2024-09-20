package com.miss.pluginmanage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.ExtensionFactory;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;


@Slf4j
@RequiredArgsConstructor
public class SpringExtensionFactory implements ExtensionFactory {

    protected final PluginManager pluginManager;

    @Override
    @Nullable
    public <T> T create(Class<T> extensionClass) {
       return getPluginApplicationContextBy(extensionClass)
               .map(applicationContext -> applicationContext.getBean(extensionClass))
               .orElseGet(() -> createWithoutSpring(extensionClass));
    }

    protected <T> T createWithoutSpring(final Class<T> extensionClass) throws IllegalArgumentException{
        final Constructor<?> constructor = getPublicConstructorWithShortestParameterList(extensionClass)
                .orElseThrow(() -> new IllegalArgumentException("插件扩展类" + nameOf(extensionClass) + "必须具有至少一个公共的构造方法"));
        try {
            if (log.isTraceEnabled()) {
                log.trace("使用 Java的标准反射，通过构造方法：" + constructor +" 创建类：" + nameOf(extensionClass) + " 的实例");

            }
            return (T) constructor.newInstance(nullParameters(constructor));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            log.error(ex.getMessage(), ex);

            throw new RuntimeException("通过构造方法" + constructor + "初始化类实例失败: " + ex.getMessage(), ex);
        }
    }


    private Optional<Constructor<?>> getPublicConstructorWithShortestParameterList(
            final Class<?> extensionClass
    ) {
        return Stream.of(extensionClass.getConstructors())
                .min(Comparator.comparing(Constructor::getParameterCount));
    }

    private Object[] nullParameters(final Constructor<?> constructor) {
        return new Object[constructor.getParameterCount()];
    }

    protected <T> Optional<ApplicationContext> getPluginApplicationContextBy(final Class<T> extensionClass) {
        return Optional.ofNullable(this.pluginManager.whichPlugin(extensionClass))
                .map(PluginWrapper::getPlugin)
                .filter(SpringPlugin.class::isInstance)
                .map(plugin -> (SpringPlugin) plugin)
                .map(SpringPlugin::getApplicationContext);
    }


    private <T> String nameOf(final Class<T> clazz) {
        return clazz.getName();
    }
}
