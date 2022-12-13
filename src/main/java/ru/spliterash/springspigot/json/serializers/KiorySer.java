package ru.spliterash.springspigot.json.serializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import ru.spliterash.springspigot.json.JacksonModuleProvider;

import java.io.IOException;

@org.springframework.stereotype.Component
@ConditionalOnClass({Component.class, MiniMessage.class})
public class KiorySer extends SimpleModule implements JacksonModuleProvider {
    public KiorySer() {
        super("kyori_serializer");

        addDeserializer(Component.class, new KioryComponentDeserializer());
        addSerializer(Component.class, new KioryComponentSerializer());
    }

    public static class KioryComponentDeserializer extends StdDeserializer<Component> {

        protected KioryComponentDeserializer() {
            super(Component.class);
        }

        @Override
        public Component deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            String raw = p.getValueAsString();

            return MiniMessage.miniMessage().deserialize(raw);
        }
    }

    public static class KioryComponentSerializer extends StdSerializer<Component> {

        protected KioryComponentSerializer() {
            super(Component.class);
        }

        @Override
        public void serialize(Component value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            String result = MiniMessage.miniMessage().serialize(value);

            gen.writeString(result);
        }
    }
}
