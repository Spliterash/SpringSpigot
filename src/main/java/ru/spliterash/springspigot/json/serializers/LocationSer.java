package ru.spliterash.springspigot.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.springframework.stereotype.Component;

import java.io.IOException;

@SuppressWarnings("unused")
public class LocationSer extends SimpleModule {

    public LocationSer() {
        super("BukkitLocationSerialization");

        addDeserializer(Location.class, new LocationDeserialization());
        addSerializer(Location.class, new LocationSerialization());
    }

    public static class LocationDeserialization extends StdDeserializer<Location> {
        protected LocationDeserialization() {
            super(Location.class);
        }

        @Override
        public Location deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            TreeNode root = p.readValueAsTree();
            double x = ((NumericNode) root.get("x")).doubleValue();
            double y = ((NumericNode) root.get("y")).doubleValue();
            double z = ((NumericNode) root.get("z")).doubleValue();
            World world = Bukkit.getWorld(((TextNode) root.get("world")).textValue());

            TreeNode yawNode = root.get("yaw");
            TreeNode pitchNode = root.get("pitch");

            float yaw = 0, pitch = 0;
            if (yawNode != null)
                yaw = ((NumericNode) yawNode).floatValue();

            if (yawNode != null)
                pitch = ((NumericNode) pitchNode).floatValue();

            return new Location(world, x, y, z, yaw, pitch);
        }
    }

    public static class LocationSerialization extends StdSerializer<Location> {
        protected LocationSerialization() {
            super(Location.class);
        }

        @Override
        public void serialize(Location location, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();

            //noinspection ConstantConditions
            gen.writeStringField("world", location.getWorld().getName());
            gen.writeNumberField("x", location.getX());
            gen.writeNumberField("y", location.getY());
            gen.writeNumberField("z", location.getZ());

            if (location.getYaw() != 0)
                gen.writeNumberField("yaw", location.getYaw());

            if (location.getPitch() != 0)
                gen.writeNumberField("pitch", location.getPitch());

            gen.writeEndObject();

        }
    }
}
