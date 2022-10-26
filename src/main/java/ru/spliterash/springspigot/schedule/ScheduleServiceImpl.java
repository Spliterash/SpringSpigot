package ru.spliterash.springspigot.schedule;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.springframework.stereotype.Component;
import ru.spliterash.springspigot.schedule.func.CancelRunnable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

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

    @Override
    public <T> T runInSyncWithResult(Supplier<T> supplier) {
        return runInSyncFuture(supplier).join();
    }

    @Override
    public <T> CompletableFuture<T> runInSyncFuture(Supplier<T> supplier) {
        if (Bukkit.isPrimaryThread())
            return CompletableFuture.completedFuture(supplier.get());
        else {
            CompletableFuture<T> completable = new CompletableFuture<>();

            runSyncTask(() -> {
                try {
                    T result = supplier.get();
                    completable.complete(result);
                } catch (Exception exception) {
                    completable.completeExceptionally(exception);
                }
            });

            return completable;
        }
    }

    @Override
    public void runInSync(Runnable runnable) {
        if (Bukkit.isPrimaryThread())
            runnable.run();
        else
            runSyncTask(runnable);
    }

    @Override
    public void runInSyncBlocking(Runnable runnable) {
        runInSyncWithResult(() -> {
            runnable.run();
            return null;
        });
    }
}
