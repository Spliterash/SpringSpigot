package ru.spliterash.springspigot.schedule;

import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.springframework.stereotype.Component;
import ru.spliterash.springspigot.schedule.func.CancelRunnable;

@Component
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final JavaPlugin plugin;


    @Override
    public BukkitTask runSyncTask(Runnable runnable, long delay) {
        return plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay);
    }

    @Override
    public BukkitTask runAsyncTask(Runnable runnable, long delay) {
        return plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
    }

    @Override
    public BukkitTask runTaskTimer(Runnable runnable, long delay, long period) {
        return plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, delay, period);
    }

    @Override
    public BukkitTask runTaskTimerAsync(Runnable runnable, long delay, long period) {
        return plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period);
    }

    @Override
    public BukkitTask runSyncTask(Runnable runnable) {
        return plugin.getServer().getScheduler().runTask(plugin, runnable);
    }

    @Override
    public BukkitTask runAsyncTask(Runnable runnable) {
        return plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    @Override
    public BukkitTask runTaskTimer(CancelRunnable runnable, long delay, long period) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (!runnable.run())
                    cancel();
            }
        }.runTaskTimer(plugin, delay, period);
    }

    @Override
    public BukkitTask runTaskTimerAsync(CancelRunnable runnable, long delay, long period) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (!runnable.run())
                    cancel();
            }
        }.runTaskTimerAsynchronously(plugin, delay, period);
    }

}
