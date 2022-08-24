# Spring Spigot

Скачать последнюю версию плагина
можно [отсюда](https://jenkins.spliterash.ru/job/public/job/minecraft/job/spring-spigot/)

Этот плагин позволяет использовать spring для написания майнкрафт плагинов.

Основное отличие от всех остальных spring spigot либ то, что это отдельный плагин, его не надо шейдить или ещё что либо
делать, просто положил в plugins, а затем используешь в своих плагинах всю мощь спринга.

Чтобы иметь возможность использовать заготовленные классы из этой либы, нужно просто подключить её как compileOnly(ну
или provided если вы любитель мавена) зависимость

```groovy
repositories {
    maven {
        url = uri("https://nexus.spliterash.ru/repository/group/")
    }
}

dependencies {
    compileOnly("ru.spliterash:spring-spigot:1.0.1")
}
```

Дальше, нужно создать класс Spring приложения
```java
@SpringBootApplication(scanBasePackages = "ru.yourpackage.plugin")
public class YourAmazingPluginApplication {
}
```
Затем, надо поменять главный класс вашего плагина, на следующее
```java
public class YourAmazingPlugin extends SpringSpigotPlugin {
    @Override
    protected Class<?> getAppClass() {
        return YourAmazingPluginApplication.class;
    }
}
```

После этого, можете использовать всю мощь spring'а. Для каждого плагина создаётся свой spring context, поэтому можно спокойно использовать его в множестве плагинов

```java
@Component
public class SomeService {
    // CODE
}
```

Так же имеются удобства для майнкрафта

### SpigotListener

Автоматически регистрирует класс в Bukkit.getPluginManager().registerEvents()

```java
@SpigotListener
@RequiredArgsConstructor // Lombok
public class PlayerListener implements Listener {
    private final SomeService someService;

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        someService.playerMove(e.getPlayer());
    }

}
```

### config.yml

Всё, что лежит в config.yml читается спрингом так, как будто это application.yml, поэтому вы можете получать значения из
конфига через `@Value`

```java
@Component
public class SomeService {
    public SomeService(@Value("${config.value}") String value) {
        // CODE
    }
}

@SpigotListener
@ConditionalOnExpression("${pvp-handle.enable:false}")
// значение pvp.enable в конфиге true, если такого нет, то по дефолту false
public class PvpListener implements Listener {

}
```

### Command

Регистрирует обработчик команд в spigot

```java
@SpigotCommand(command = "tp")
public class TpExecutor implements CommandExecutor {
    // Реализация
}
```

### Conditional

`@ConditionalOnMinecraftVersion` Создаст бин только в том случае, если версия майнкрафта попадает в диапазон

```java
@Component
@ConditionalOnMinecraftVersion(min = "1.13", max = "1.16.5")
public class PacketAdapter_13_16 implements PacketAdapter {
    // Реализация отправки пакетов с версии 1.13 по 1.16.5
}
```

### Базы данных

Есть возможность использовать MongoTemplate и Redis спринга, для этого надо в файле application.yml, который лежит в
ресурсах плагина указать

По умолчанию стоит false

Это так же можно прописывать в config.yml если хочется динамичности

```yaml
spring-spigot:
  mongo: true # Включает mongo
  redis: true # Включает redis
```
### Перезагрузка
Для того чтобы реализовать перезагрузку в своём плагине, достаточно сделать примерно следующее
```java
@SpigotCommand(command = "plugin_name")
@RequiredArgsConstructor
public class PluginCommand implements CommandExecutor {
    private final PluginCallback pluginCallback;
    // Или
    private final YourAmazingPlugin amazingPlugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0)
            return true;
        
        if (args[0].equals("reload"))
            pluginCallback.reload();
        return true;
    }
}
```

Это завершит приложение spring, а затем заново создаст его, те все компоненты будут созданы заново

Можно повешать внутри них `@PreDestroy`, чтобы завершить игровые события при перезагрузке, или выключении сервера