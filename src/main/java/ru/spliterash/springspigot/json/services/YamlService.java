package ru.spliterash.springspigot.json.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.spliterash.springspigot.json.serializers.ChatColorSer;
import ru.spliterash.springspigot.json.serializers.LocationSer;
import ru.spliterash.springspigot.json.serializers.TextComponentSer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@SuppressWarnings("unused")
@Getter
@Component
public class YamlService {
    private final ObjectMapper mapper;

    public YamlService() {
        mapper = new ObjectMapper(new YAMLFactory());

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mapper.registerModule(new LocationSer());
        mapper.registerModule(new TextComponentSer());
        mapper.registerModule(new ChatColorSer());
        mapper.registerModule(new JavaTimeModule());

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public <T> T load(File file, Class<T> clazz) throws IOException {
        return mapper.readValue(file, clazz);
    }

    public <T> T load(String str, Class<T> clazz) throws IOException {
        return mapper.readValue(str, clazz);
    }

    public <T> T load(InputStream stream, Class<T> clazz) throws IOException {
        return mapper.readValue(stream, clazz);
    }

    public <T> T load(File file, TypeReference<T> reference) throws IOException {
        return mapper.readValue(file, reference);
    }

    public <T> T load(String str, TypeReference<T> reference) throws IOException {
        return mapper.readValue(str, reference);
    }

    public <T> T load(InputStream stream, TypeReference<T> reference) throws IOException {
        return mapper.readValue(stream, reference);
    }

    public void save(File file, Object obj) throws IOException {
        mapper.writeValue(file, obj);
    }

    public void save(OutputStream outputStream, Object obj) throws IOException {
        mapper.writeValue(outputStream, obj);
    }

    public String objToYaml(Object obj) throws IOException {
        return mapper.writeValueAsString(obj);
    }

}
