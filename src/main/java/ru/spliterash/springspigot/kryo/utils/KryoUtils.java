package ru.spliterash.springspigot.kryo.utils;

import com.esotericsoftware.kryo.Kryo;
import ru.spliterash.springspigot.kryo.serializers.KryoUUIDSerializer;

import java.util.UUID;

public class KryoUtils {

    public static void register(Kryo kryo) {
        kryo.addDefaultSerializer(UUID.class, new KryoUUIDSerializer());
    }
}
