package ru.spliterash.springspigot.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.springframework.stereotype.Component;
import ru.spliterash.springspigot.json.JacksonModuleProvider;

import java.io.IOException;

@SuppressWarnings("unused")
@Component
public class TextComponentSer extends SimpleModule implements JacksonModuleProvider {

    public TextComponentSer() {
        super("BungeeTextComponentSerialization");

        addDeserializer(TextComponent.class, new TextComponentDeserialization());
        addSerializer(TextComponent.class, new TextComponentSerialization());
    }

    public static class TextComponentDeserialization extends StdDeserializer<TextComponent> {
        protected TextComponentDeserialization() {
            super(TextComponent.class);
        }

        @Override
        public TextComponent deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String str = p.getValueAsString();
            return new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', str)));
        }
    }

    public static class TextComponentSerialization extends StdSerializer<TextComponent> {
        protected TextComponentSerialization() {
            super(TextComponent.class);
        }

        @Override
        public void serialize(TextComponent tex, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(tex.toLegacyText().replace("§", "&"));
        }
    }
}
