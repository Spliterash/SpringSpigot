package ru.spliterash.springspigot.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.bukkit.Color;
import org.springframework.stereotype.Component;
import ru.spliterash.springspigot.json.JacksonModuleProvider;

import java.io.IOException;

@SuppressWarnings("unused")
@Component
public class ColorSer extends SimpleModule implements JacksonModuleProvider {

    public ColorSer() {
        super("BukkitColorSerialization");

        addDeserializer(Color.class, new ColorDeserialization());
        addSerializer(Color.class, new ColorSerialization());
    }

    public static class ColorDeserialization extends StdDeserializer<Color> {
        protected ColorDeserialization() {
            super(Color.class);
        }

        @Override
        public Color deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String hex = p.getValueAsString();

            return Color.fromRGB(
                    Integer.valueOf(hex.substring(1, 3), 16),
                    Integer.valueOf(hex.substring(3, 5), 16),
                    Integer.valueOf(hex.substring(5, 7), 16)
            );
        }
    }

    public static class ColorSerialization extends StdSerializer<Color> {
        protected ColorSerialization() {
            super(Color.class);
        }

        @Override
        public void serialize(Color c, JsonGenerator gen, SerializerProvider provider) throws IOException {
            String hex = String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());

            gen.writeString(hex);
        }
    }
}
