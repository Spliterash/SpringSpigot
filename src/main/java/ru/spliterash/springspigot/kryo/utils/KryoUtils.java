package ru.spliterash.springspigot.kryo.utils;

import com.esotericsoftware.kryo.Kryo;
import ru.spliterash.springspigot.kryo.serializers.KryoUUIDSSerializer;

import java.util.UUID;

public class KryoUtils {

    public static void register(Kryo kryo) {
        kryo.register(UUID.class, new KryoUUIDSSerializer());
    }
}
