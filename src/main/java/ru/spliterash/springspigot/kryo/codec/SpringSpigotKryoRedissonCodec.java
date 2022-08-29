package ru.spliterash.springspigot.kryo.codec;

import com.esotericsoftware.kryo.Kryo;
import org.redisson.codec.Kryo5Codec;
import ru.spliterash.springspigot.kryo.utils.KryoUtils;

public class SpringSpigotKryoRedissonCodec extends Kryo5Codec {

    public SpringSpigotKryoRedissonCodec(ClassLoader loader) {
        super(loader);
    }

    @Override
    protected Kryo createKryo(ClassLoader classLoader) {
        Kryo kryo = super.createKryo(classLoader);

        KryoUtils.register(kryo);

        return kryo;
    }
}
