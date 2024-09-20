package com.miss.pluginmanage;

import com.miss.pluginapi.PluginContext;
import com.miss.pluginmanage.event.SpringPluginStartedEvent;
import com.miss.pluginmanage.event.SpringPluginStartingEvent;
import com.miss.pluginmanage.event.SpringPluginStoppingEvent;
import org.pf4j.Plugin;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Optional;

public class SpringPlugin extends Plugin {

    private ApplicationContext context;

    private Plugin delegate;

    private final PluginApplicationContextFactory contextFactory;

    private final PluginContext pluginContext;

    public SpringPlugin(PluginApplicationContextFactory contextFactory,
                        PluginContext pluginContext) {
        this.contextFactory = contextFactory;
        this.pluginContext = pluginContext;
    }

    @Override
    public void start() {
        log.info("准备启动插件 {}", pluginContext.getName());
        String pluginId = pluginContext.getName();
        try {
            this.context = contextFactory.create(pluginId);
            log.info("插件 {} 的Application Context {} 已经创建好了", pluginId, this.context);

            Optional<Plugin> pluginOpt = context.getBeanProvider(Plugin.class)
                    .stream()
                    .findFirst();

            log.info("插件 {} 准备发布启动事件", pluginId);
            context.publishEvent(new SpringPluginStartingEvent(this, this));
            log.info("插件 {} 已经发布了启动事件", pluginId);
            if(pluginOpt.isPresent()) {
                this.delegate = pluginOpt.get();
                log.info("插件 {} 执行插件启动 {} ", pluginId, this.delegate);
                this.delegate.start();
                log.info("插件 {} 已经启动完成 {}", pluginId, this.delegate);
            }
            log.info("插件 {} 准备发布启动完成事件", pluginId);
            context.publishEvent(new SpringPluginStartedEvent(this, this));
            log.info("插件 {} 发布完启动完成事件", pluginId);
        } catch (Throwable t) {
            log.error("插件 {} 启动失败，清理并关闭插件", pluginId);
            this.stop();
            throw t;
        }
    }

    @Override
    public void stop() {
        try {
            if (context != null) {
                log.info("插件 {} 准备发布停止事件", pluginContext.getName());
                context.publishEvent(new SpringPluginStoppingEvent(this, this));
                log.info("插件 {} 已经发布停止事件", pluginContext.getName());
            }

            if (this.delegate != null) {
                log.info("插件 {} 执行插件停止 {}", pluginContext.getName(), this.delegate);
                this.delegate.stop();
                log.info("插件 {} 已经停止完成 {}", pluginContext.getName(), this.delegate);
            }
        } finally {
            if (context instanceof ConfigurableApplicationContext configurableApplicationContext) {
                log.info("插件 {} 关闭 Application Context", pluginContext.getName());
                configurableApplicationContext.close();
                log.info("插件 {} 已经关闭 Application Context", pluginContext.getName());
            }
            log.info("重置插件 context {}", pluginContext.getName());
            context = null;
        }
    }


    @Override
    public void delete() {
        if (delegate != null) {
            delegate.delete();
        }
        this.delegate = null;
    }

    public ApplicationContext getApplicationContext() {
        return context;
    }

    public PluginContext getPluginContext() {
        return pluginContext;
    }
}
