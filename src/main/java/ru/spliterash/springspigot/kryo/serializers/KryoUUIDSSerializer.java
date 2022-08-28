package ru.spliterash.springspigot.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.UUID;

public class KryoUUIDSSerializer extends Serializer<UUID> {

    public KryoUUIDSSerializer() {
        setImmutable(true);
    }

    @Override
    public void write(Kryo kryo, Output output, UUID uuid) {
        output.writeLong(uuid.getMostSignificantBits());
        output.writeLong(uuid.getLeastSignificantBits());
    }

    @Override
    public UUID read(final Kryo kryo, final Input input, final Class<? extends UUID> uuidClass) {
        return new UUID(input.readLong(), input.readLong());
    }
}
