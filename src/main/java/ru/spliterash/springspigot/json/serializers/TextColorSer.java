package ru.spliterash.springspigot.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import ru.spliterash.springspigot.json.JacksonModuleProvider;

import java.io.IOException;
import java.lang.reflect.Field;

@Component
@ConditionalOnClass(TextColor.class)
public class TextColorSer extends SimpleModule implements JacksonModuleProvider {
    public TextColorSer() {
        super("BukkitTextColorSerialization");

        addDeserializer(TextColor.class, new TextColorSer.TextColorDeserialization());
        addSerializer(TextColor.class, new TextColorSer.TextColorSerialization());
    }

    public static class TextColorDeserialization extends StdDeserializer<TextColor> {
        protected TextColorDeserialization() {
            super(TextColor.class);
        }

        @Override
        public TextColor deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = p.getValueAsString();

            if (value.startsWith("#"))
                return TextColor.fromHexString(value);
            else {
                try {
                    Field field = NamedTextColor.class.getField(value.toUpperCase());

                    return (NamedTextColor) field.get(null);
                } catch (Exception exception) {
                    return null;
                }
            }
        }
    }

    public static class TextColorSerialization extends StdSerializer<TextColor> {
        protected TextColorSerialization() {
            super(TextColor.class);
        }

        @Override
        public void serialize(TextColor color, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if (color instanceof NamedTextColor) {
                NamedTextColor named = (NamedTextColor) color;

                gen.writeString(named.toString());
            } else {
                String hex = color.asHexString();
                gen.writeString(hex);
            }
        }
    }
}
