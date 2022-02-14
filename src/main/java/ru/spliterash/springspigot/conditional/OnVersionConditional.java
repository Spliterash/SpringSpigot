package ru.spliterash.springspigot.conditional;

import org.bukkit.Bukkit;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

@SuppressWarnings("ConstantConditions")
public class OnVersionConditional implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String realVersion = getRawVersion();

        Map<String, Object> attrs = metadata.getAnnotationAttributes(ConditionalOnMinecraftVersion.class.getName());


        String min = attrs.get("min").toString();

        String max = attrs.get("max").toString();

        if (min.equals("") && max.equals(""))
            return false;
        if (min.equals("")) {
            int compare = compareVersions(realVersion, max);
            return compare != 1; // Версия не больше чем максимальная
        }

        if (max.equals("")) {
            int compare = compareVersions(realVersion, min);
            return compare != -1; // Версия не меньше чем минимальная
        }
        int minCompare = compareVersions(realVersion, min);
        int maxCompare = compareVersions(realVersion, max);


        return minCompare != 1 && maxCompare != -1;
    }

    public static int compareVersions(String version1, String version2) {

        String[] levels1 = version1.split("\\.");
        String[] levels2 = version2.split("\\.");

        int length = Math.max(levels1.length, levels2.length);
        for (int i = 0; i < length; i++) {
            Integer v1 = i < levels1.length ? Integer.parseInt(levels1[i]) : 0;
            Integer v2 = i < levels2.length ? Integer.parseInt(levels2[i]) : 0;
            int compare = v1.compareTo(v2);
            if (compare != 0) {
                return compare;
            }
        }

        return 0;
    }

    public static String getRawVersion() {
        String strVersion = Bukkit.getVersion();

        int start = strVersion.indexOf("(MC: ") + 5;

        strVersion = strVersion.substring(start);

        int end = strVersion.indexOf(")");

        strVersion = strVersion.substring(0, end);

        return strVersion;
    }
}
