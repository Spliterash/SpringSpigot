package ru.spliterash.springspigot.json.modules;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import ru.spliterash.springspigot.json.JacksonModuleProvider;

@Component
@ConditionalOnClass(name = "ru.spliterash.kotlinmc.CoroutinePlugin")
public class KotlinSerializer implements JacksonModuleProvider {
    @Override
    public Module getModule() {
        return new KotlinModule.Builder().build();
    }
}
