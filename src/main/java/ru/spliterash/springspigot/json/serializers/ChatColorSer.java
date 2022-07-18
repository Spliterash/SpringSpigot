package ru.spliterash.springspigot.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.md_5.bungee.api.ChatColor;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;

public class ChatColorSer extends SimpleModule {
    public ChatColorSer() {
        super("ChatColorSerialization");

        addDeserializer(ChatColor.class, new ChatColorDeserialization());
        addSerializer(ChatColor.class, new ChatColorSerialization());
    }

    private static class ChatColorDeserialization extends StdDeserializer<ChatColor> {
        protected ChatColorDeserialization() {
            super(ChatColor.class);
        }

        @Override
        public ChatColor deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String color = p.getValueAsString();

            return ChatColor.of(color);
        }
    }

    private static class ChatColorSerialization extends StdSerializer<ChatColor> {
        protected ChatColorSerialization() {
            super(ChatColor.class);
        }

        @Override
        public void serialize(ChatColor value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            Color color = value.getColor();

            gen.writeString(String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()));
        }
    }
}
