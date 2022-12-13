package ru.spliterash.springspigot.json;

import com.fasterxml.jackson.databind.Module;
import org.jetbrains.annotations.Nullable;

/**
 * Класс, предоставляющий какой-либо модуль для objectmapper'а
 */
public interface JacksonModuleProvider {
    @Nullable
    default Module getModule() {
        if (this instanceof Module)
            return (Module) this;
        else {
            return null;
        }
    }
}
