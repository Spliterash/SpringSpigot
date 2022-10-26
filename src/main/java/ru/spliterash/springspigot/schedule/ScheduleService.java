package ru.spliterash.springspigot.schedule;

import org.bukkit.scheduler.BukkitTask;
import ru.spliterash.springspigot.schedule.func.CancelRunnable;

import java.util.concurrent.CompletableFuture;
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

    <T> T runInSyncWithResult(Supplier<T> supplier);

    <T> CompletableFuture<T> runInSyncFuture(Supplier<T> supplier);

    /**
     * Выполнить задачу в синхроне
     * <p>
     * Если текущий поток уже синхрон, то выполнит тут же
     * <p>
     * Если вызвать в асинке, то запустит задачу на синхрон
     */
    void runInSync(Runnable runnable);

    /**
     * То же самое что и предыдущее, только в случае с асинхронном будет ждать выполнения в синке и только потом отпустит
     */
    void runInSyncBlocking(Runnable runnable);

}
