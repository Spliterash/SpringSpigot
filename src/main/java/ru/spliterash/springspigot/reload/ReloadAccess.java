package ru.spliterash.springspigot.reload;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public interface ReloadAccess<T> {
    @Nullable CompletionStage<@Nullable Void> prepareReload(T obj);

    @Nullable CompletionStage<@Nullable Void> reload(T obj);
}
