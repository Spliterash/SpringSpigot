package ru.spliterash.springspigot.reload;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletionStage;

import static ru.spliterash.springspigot.utils.AsyncUtils.executeSequentially;

/**
 * Сервис для удобной лёгкой перезагрузки
 */
@Component
public class ReloadService {
    private static final SpringSpigotReloadAccess ACCESS = new SpringSpigotReloadAccess();
    private final DependencyGraphHelper helper;

    public ReloadService(DependencyGraphHelper helper) {
        this.helper = helper;
    }

    public CompletionStage<Void> reload() {
        return reloadType(ReloadableBean.class, ACCESS);
    }

    public <T> CompletionStage<Void> reloadType(Class<T> type, ReloadAccess<T> access) {
        List<T> beans = helper.getSortedBeans(type);
        List<T> reversed = Lists.reverse(beans);

        return executeSequentially(reversed, access::prepareReload)
                .thenCompose(unused -> executeSequentially(beans, access::reload));
    }

    private static class SpringSpigotReloadAccess implements ReloadAccess<ReloadableBean> {

        @Override
        public @Nullable CompletionStage<@Nullable Void> prepareReload(ReloadableBean obj) {
            return obj.prepareReloadBean();
        }

        @Override
        public @Nullable CompletionStage<@Nullable Void> reload(ReloadableBean obj) {
            return obj.reloadBean();
        }
    }

}