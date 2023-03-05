package ru.spliterash.springspigot.reload;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletionStage;

public interface ReloadableBean {
    @Nullable
    default CompletionStage<@Nullable Void> prepareReloadBean() {
        return null;
    }

    @Nullable
    CompletionStage<@Nullable Void> reloadBean();
}
