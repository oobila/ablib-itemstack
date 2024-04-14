package com.github.oobila.bukkit.itemstack;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.github.oobila.bukkit.itemstack.PluginUtil.getCorePlugin;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PersistentMetaUtil {

    private static final String UNSTACKABLE_KEY = "unstackable";
    private static final String LIST_DELIMITER = "#|#";

    public static void remove(PersistentDataHolder data, NamespacedKey key){
        data.getPersistentDataContainer().remove(key);
    }

    public static void addString(PersistentDataHolder data, NamespacedKey key, String value){
        data.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
    }

    public static boolean containsKey(PersistentDataHolder data, NamespacedKey key){
        if(data == null) {
            return false;
        }
        return data.getPersistentDataContainer().has(key);
    }

    public static String getString(PersistentDataHolder data, NamespacedKey key){
        if(data == null) {
            return null;
        }
        return data.getPersistentDataContainer().get(key,PersistentDataType.STRING);
    }

    public static void addInt(PersistentDataHolder data, NamespacedKey key, int value){
        data.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
    }

    public static int getInt(PersistentDataHolder data, NamespacedKey key){
        if(data == null) {
            return 0;
        }
        Integer i = data.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
        return i == null ? 0 : i;
    }

    public static void addDouble(PersistentDataHolder data, NamespacedKey key, double value){
        data.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
    }

    public static double getDouble(PersistentDataHolder data, NamespacedKey key){
        if(data == null) {
            return 0;
        }
        Double d = data.getPersistentDataContainer().get(key, PersistentDataType.DOUBLE);
        return d == null ? 0 : d;
    }

    public static void addLocalDateTime(PersistentDataHolder data, NamespacedKey key, LocalDateTime localDateTime){
        data.getPersistentDataContainer().set(
                key,
                PersistentDataType.LONG,
                localDateTime.toEpochSecond(ZoneOffset.UTC)
        );
    }

    public static LocalDateTime getLocalDateTime(PersistentDataHolder data, NamespacedKey key){
        if(data == null) {
            return null;
        }
        Long epochMillis = data.getPersistentDataContainer().get(key,PersistentDataType.LONG);
        return epochMillis == null || epochMillis <= 0 ?
                null :
                LocalDateTime.ofEpochSecond(epochMillis, 0, ZoneOffset.UTC);
    }

    public static void addUUID(PersistentDataHolder data, NamespacedKey key, UUID value){
        data.getPersistentDataContainer().set(key, PersistentDataType.STRING, value.toString());
    }

    public static UUID getUUID(PersistentDataHolder data, NamespacedKey key){
        if(data == null) {
            return null;
        }
        String value = data.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        return value == null ? null : UUID.fromString(value);
    }

    public static void addList(PersistentDataHolder data, NamespacedKey key, List<String> value) {
        data.getPersistentDataContainer().set(
                key,
                PersistentDataType.STRING,
                String.join(LIST_DELIMITER, value)
        );
    }

    public static List<String> getList(PersistentDataHolder data, NamespacedKey key){
        if(data == null) {
            return Collections.emptyList();
        }
        String value = data.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        return value == null ? null : Arrays.stream(value.split(LIST_DELIMITER)).toList();
    }

    public static void makeUnique(PersistentDataHolder data){
        PersistentMetaUtil.addUUID(data, new NamespacedKey(getCorePlugin(), UNSTACKABLE_KEY), UUID.randomUUID());
    }

    public static void makeNonUnique(PersistentDataHolder data){
        PersistentMetaUtil.remove(data, new NamespacedKey(getCorePlugin(), UNSTACKABLE_KEY));
    }
}
