package ru.spliterash.springspigot.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import ru.spliterash.springspigot.schedule.annotation.AsyncSchedule;
import ru.spliterash.springspigot.schedule.annotation.SyncSchedule;
import ru.spliterash.springspigot.schedule.markers.SpigotScheduler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.Executor;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ScheduleInitializer implements AsyncConfigurer {
    private final ApplicationContext context;
    private final ScheduleService scheduleService;

    private boolean initialized = false;

    @SuppressWarnings("unused")
    @EventListener
    public void onStartup(ContextRefreshedEvent event) {
        if (initialized) return;
        initialized = true;

        Collection<SpigotScheduler> values = context.getBeansOfType(SpigotScheduler.class).values();

        for (SpigotScheduler value : values) {
            //noinspection unchecked
            Class<SpigotScheduler> realClass = (Class<SpigotScheduler>) AopUtils.getTargetClass(value);

            for (Method method : realClass.getMethods()) {
                try {
                    AsyncSchedule async = method.getAnnotation(AsyncSchedule.class);
                    if (async != null) {
                        checkMethod(method);
                        scheduleService.runTaskTimerAsync(() -> execute(method, value), 0, async.period());
                        continue;
                    }

                    SyncSchedule sync = method.getAnnotation(SyncSchedule.class);
                    if (sync != null) {
                        checkMethod(method);
                        scheduleService.runTaskTimer(() -> execute(method, value), 0, sync.period());
                        continue;
                    }

                } catch (InvalidMethodSignature ex) {
                    log.warn("Method " + method.getName() + " in class " + realClass.getSimpleName() + " have invalid signature. Method need be empty params");
                }
            }
        }
    }

    @Override
    public Executor getAsyncExecutor() {
        return scheduleService::runAsyncTask;
    }

    private void checkMethod(Method method) throws InvalidMethodSignature {
        if (method.getParameterCount() > 0)
            throw new InvalidMethodSignature();
    }

    private void execute(Method method, Object realObj) {
        try {
            method.invoke(realObj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.warn("Error execute scheduled method " + method.getName() + " in class " + method.getDeclaringClass().getSimpleName());
            e.printStackTrace();
        }
    }

    private static class InvalidMethodSignature extends Exception {
    }
}
