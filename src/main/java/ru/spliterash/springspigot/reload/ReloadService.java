package ru.spliterash.springspigot.reload;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletionStage;

import static ru.spliterash.springspigot.utils.AsyncUtils.executeSequentially;

/**
 * Сервис для удобной лёгкой перезагрузки
 */
@Component
public class ReloadService {
    private final DependencyGraphHelper helper;

    public ReloadService(DependencyGraphHelper helper) {
        this.helper = helper;
    }

    public CompletionStage<Void> reload() {
        List<ReloadableBean> beans = helper.getSortedBeans(ReloadableBean.class);
        List<ReloadableBean> reversed = Lists.reverse(beans);

        return executeSequentially(reversed, ReloadableBean::prepareReloadBean)
                .thenCompose(unused -> executeSequentially(beans, ReloadableBean::reloadBean));
    }

}