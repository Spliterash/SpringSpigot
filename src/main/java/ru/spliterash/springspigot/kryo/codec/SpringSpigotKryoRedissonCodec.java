package ru.spliterash.springspigot.kryo.codec;

import com.esotericsoftware.kryo.Kryo;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.redisson.codec.Kryo5Codec;
import ru.spliterash.springspigot.kryo.utils.KryoUtils;

import java.util.function.Consumer;

public class SpringSpigotKryoRedissonCodec extends Kryo5Codec {

    private final Consumer<Kryo> additionalAction;

    public SpringSpigotKryoRedissonCodec(ClassLoader loader, Consumer<Kryo> additionalAction) {
        super(loader);
        this.additionalAction = additionalAction;
    }

    @Override
    protected Kryo createKryo(ClassLoader classLoader) {
        Kryo kryo = super.createKryo(classLoader);

        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());

        KryoUtils.register(kryo);

        if (additionalAction != null)
            additionalAction.accept(kryo);

        return kryo;
    }
}
