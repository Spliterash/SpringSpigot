package ru.spliterash.springspigot.utils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class AsyncUtils {


    public static <T> CompletionStage<Void> executeSequentially(List<T> list, Function<T, CompletionStage<Void>> converter) {
        AtomicInteger number = new AtomicInteger(0);

        return executeSequentially(list, converter, number);
    }

    private static <T> CompletionStage<Void> executeSequentially(List<T> list, Function<T, CompletionStage<Void>> converter, AtomicInteger number) {
        int i = number.getAndIncrement();

        if (i >= list.size())
            return CompletableFuture.completedFuture(null);

        T something = list.get(i);
        try {
            CompletionStage<Void> go = converter.apply(something);
            if (go != null)
                return go.handle((unused, throwable) -> {
                            if (throwable != null)
                                throwable.printStackTrace();

                            return executeSequentially(list, converter, number);
                        })
                        .thenCompose(Function.identity());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return executeSequentially(list, converter, number);
    }
}
