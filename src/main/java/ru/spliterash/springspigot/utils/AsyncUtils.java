package ru.spliterash.springspigot.utils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class AsyncUtils {


    public static <T> CompletionStage<Void> executeSequentially(List<T> list, Function<T, CompletionStage<Void>> converter) {
        return executeSequentially(list, converter, 0);
    }

    private static <T> CompletionStage<Void> executeSequentially(List<T> list, Function<T, CompletionStage<Void>> converter, int i) {
        if (i >= list.size())
            return CompletableFuture.completedFuture(null);

        T something = list.get(i);
        try {
            CompletionStage<Void> go = converter.apply(something);
            if (go != null)
                return go.handle((unused, throwable) -> {
                            if (throwable != null)
                                throwable.printStackTrace();

                            return executeSequentially(list, converter, i + 1);
                        })
                        .thenCompose(Function.identity());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return executeSequentially(list, converter, i + 1);
    }
}
