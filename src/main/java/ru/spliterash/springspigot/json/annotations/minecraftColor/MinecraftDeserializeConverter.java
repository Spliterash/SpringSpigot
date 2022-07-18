package ru.spliterash.springspigot.json.annotations.minecraftColor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class MinecraftDeserializeConverter extends StdDeserializer<String> {

    protected MinecraftDeserializeConverter() {
        super(String.class);
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getValueAsString().replace("&", "§");
    }
}
