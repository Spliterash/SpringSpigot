package ru.spliterash.springspigot.init;

import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.context.ConfigurableApplicationContext;
import ru.spliterash.springspigot.port.PluginCallback;
import ru.spliterash.springspigot.reload.DependencyGraphHelper;
import ru.spliterash.springspigot.reload.ReloadableBean;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public abstract class SpringSpigotPlugin extends JavaPlugin implements PluginCallback {
    private ConfigurableApplicationContext context;

    @Override
    public void onEnable() {
        this.context = SpringSpigotBootstrapper.initialize(this, getAppClass());
    }

    @Override
    public void onDisable() {
        if (context != null) {
            context.close();
            context = null;
        }
    }

    public void reload() {
        if (context != null)
            context.close();
        context = SpringSpigotBootstrapper.initialize(this, getAppClass());
    }

    public <T> T getService(Class<T> serviceClass) {
        if (allowExternalAccess(serviceClass))
            return context.getBean(serviceClass);
        else
            return null;
    }

    protected boolean allowExternalAccess(Class<?> needClass) {
        return true; // по дефлоту разрешаем всё
    }

    protected abstract Class<?> getAppClass();
}
