package ru.spliterash.springspigot.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.bukkit.util.Vector;
import org.springframework.stereotype.Component;
import ru.spliterash.springspigot.json.JacksonModuleProvider;

import java.io.IOException;

@SuppressWarnings("unused")
@Component
public class VectorSer extends SimpleModule implements JacksonModuleProvider {

    public VectorSer() {
        super("BukkitVectorSerialization");

        addDeserializer(Vector.class, new VectorDeserialization());
        addSerializer(Vector.class, new VectorSerialization());
    }

    public static class VectorDeserialization extends StdDeserializer<Vector> {
        protected VectorDeserialization() {
            super(Vector.class);
        }

        @Override
        public Vector deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            TreeNode root = p.readValueAsTree();

            double x = ((NumericNode) root.get("x")).doubleValue();
            double y = ((NumericNode) root.get("y")).doubleValue();
            double z = ((NumericNode) root.get("z")).doubleValue();

            return new Vector(x, y, z);
        }
    }

    public static class VectorSerialization extends StdSerializer<Vector> {
        protected VectorSerialization() {
            super(Vector.class);
        }

        @Override
        public void serialize(Vector vector, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();

            gen.writeNumberField("x", vector.getX());
            gen.writeNumberField("y", vector.getY());
            gen.writeNumberField("z", vector.getZ());


            gen.writeEndObject();

        }
    }
}
