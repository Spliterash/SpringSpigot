package ru.spliterash.springspigot.init;

import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.context.ConfigurableApplicationContext;
import ru.spliterash.springspigot.port.PluginCallback;

@RequiredArgsConstructor
public abstract class SpringSpigotPlugin extends JavaPlugin implements PluginCallback {
    private ConfigurableApplicationContext context;

    @Override
    public void onEnable() {
        saveDefaultConfig();
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
