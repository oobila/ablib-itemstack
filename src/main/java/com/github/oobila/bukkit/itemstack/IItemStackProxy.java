package com.github.oobila.bukkit.itemstack;

import com.github.oobila.bukkit.effects.Attribute;
import com.github.oobila.bukkit.effects.AttributeManager;
import com.github.oobila.bukkit.effects.Effect;
import com.github.oobila.bukkit.itemstack.effects.ItemBehaviour;
import com.github.oobila.bukkit.itemstack.effects.ItemSlot;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.github.oobila.bukkit.common.ABCommon.key;

public interface IItemStackProxy extends IItemStackBuilder {

    @Override
    default String getDisplayName() {
        return get(ItemMeta::getDisplayName);
    }

    @Override
    default int getCount() {
        return getItemStack().getAmount();
    }

    @Override
    default int getDamage() {
        return get(meta -> {
            if (meta instanceof Damageable damageable) {
                return damageable.getDamage();
            } else {
                return 0;
            }
        });
    }

    @Override
    default String getMetaString(String name) {
        return get(meta -> PersistentMetaUtil.getString(meta, key(name)));
    }

    @Override
    default UUID getMetaUUID(String name) {
        return get(meta -> PersistentMetaUtil.getUUID(meta, key(name)));
    }

    @Override
    default int getMetaInt(String name) {
        return get(meta -> PersistentMetaUtil.getInt(meta, key(name)));
    }

    @Override
    default double getMetaDouble(String name) {
        return get(meta -> PersistentMetaUtil.getDouble(meta, key(name)));
    }

    @Override
    default LocalDateTime getMetaDate(String name) {
        return get(meta -> PersistentMetaUtil.getLocalDateTime(meta, key(name)));
    }

    @Override
    default void removeMeta(String name){
        apply(meta -> PersistentMetaUtil.remove(meta, key(name)));
    }

    @Override
    default void makeStackable() {
        apply(meta -> PersistentMetaUtil.add(meta, key(UNSTACKABLE_KEY), UUID.randomUUID()));
    }

    @Override
    default Set<Effect<?>> getItemEffects() {
        String effectString = getMetaString(ITEM_EFFECTS_TAG);
        if (effectString == null) {
            return Collections.emptySet();
        }
        return Arrays.stream(effectString.split("\\|"))
                .map(AttributeManager::effectOf)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    default Set<Attribute> getAttributes() {
        String attributeString = getMetaString(ITEM_ATTRIBUTES_TAG);
        if (attributeString == null) {
            return Collections.emptySet();
        }
        return Arrays.stream(attributeString.split("\\|"))
                .map(AttributeManager::attributeOf)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    default ItemBehaviour getBehaviour() {
        return ItemBehaviour.valueOf(getMetaString(ITEM_BEHAVIOUR_TAG));
    }

    @Override
    default ItemSlot getBehaviourSlot() {
        return ItemSlot.valueOf(getMetaString(ITEM_BEHAVIOUR_SLOT_TAG));
    }

    @Override
    default List<String> getLore() {
        String loreString = getMetaString("lore");
        if (loreString == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(loreString.split(CUSTOM_LORE_SECTION_DELIM)).toList();
    }

    default <T> T get(ItemMetaFunction<T> function) {
        ItemMeta meta = getItemStack().getItemMeta();
        return function.apply(meta);
    }

    interface ItemMetaFunction<T> {
        T apply(ItemMeta meta);
    }

}
