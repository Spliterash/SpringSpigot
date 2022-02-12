package ru.spliterash.springspigot.schedule;

import org.bukkit.scheduler.BukkitTask;
import ru.spliterash.springspigot.schedule.func.CancelRunnable;

import java.util.function.Supplier;

public interface ScheduleService {

    BukkitTask runSyncTask(Runnable runnable, long delay);

    BukkitTask runAsyncTask(Runnable runnable, long delay);

    BukkitTask runTaskTimer(Runnable runnable, long delay, long period);

    BukkitTask runTaskTimerAsync(Runnable runnable, long delay, long period);

    BukkitTask runSyncTask(Runnable runnable);

    BukkitTask runAsyncTask(Runnable runnable);

    // Таймеры с отменой

    BukkitTask runTaskTimer(CancelRunnable runnable, long delay, long period);

    BukkitTask runTaskTimerAsync(CancelRunnable runnable, long delay, long period);

}
