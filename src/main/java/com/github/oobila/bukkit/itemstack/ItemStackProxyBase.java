package com.github.oobila.bukkit.itemstack;

import com.github.oobila.bukkit.effects.Attribute;
import com.github.oobila.bukkit.effects.AttributeManager;
import com.github.oobila.bukkit.effects.Effect;
import com.github.oobila.bukkit.itemstack.effects.ItemBehaviour;
import com.github.oobila.bukkit.itemstack.effects.ItemSlot;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static com.github.oobila.bukkit.common.ABCommon.key;
import static com.github.oobila.bukkit.common.ABCommon.log;
import static com.github.oobila.bukkit.itemstack.ItemStackUtil.apply;
import static com.github.oobila.bukkit.itemstack.ItemStackUtil.get;

interface ItemStackProxyBase extends ExtendedItemStack {

    String UNSTACKABLE_KEY = "unstackable";
    String ITEM_EFFECTS_TAG = "itemEffects";
    String ITEM_ATTRIBUTES_TAG = "itemAttributes";
    String ITEM_BEHAVIOUR_TAG = "itemBehaviour";
    String ITEM_BEHAVIOUR_SLOT_TAG = "itemBehaviourSlot";
    String CUSTOM_LORE_SECTION_DELIM = "//";
    String CUSTOM_LORE_SECTION_DELIM_REPLACEMENT = "/ /";

    ItemStack getItemStack();

    @Override
    default void setDisplayName(String displayName) {
        apply(getItemStack(), meta -> meta.setDisplayName(displayName));
    }

    @Override
    default void addMeta(String name, String value) {
        apply(getItemStack(), meta -> PersistentMetaUtil.add(meta, key(name), value));
    }

    @Override
    default void addMeta(String name, UUID value) {
        apply(getItemStack(), meta -> PersistentMetaUtil.add(meta, key(name), value));
    }

    @Override
    default void addMeta(String name, int value) {
        apply(getItemStack(), meta -> PersistentMetaUtil.add(meta, key(name), value));
    }

    @Override
    default void addMeta(String name, double value) {
        apply(getItemStack(), meta -> PersistentMetaUtil.add(meta, key(name), value));
    }

    @Override
    default void addMeta(String name, LocalDateTime value) {
        apply(getItemStack(), meta -> PersistentMetaUtil.add(meta, key(name), value));
    }

    @Override
    default String getMetaString(String name) {
        return get(getItemStack(), meta -> PersistentMetaUtil.getString(meta, key(name)));
    }

    @Override
    default UUID getMetaUUID(String name) {
        return get(getItemStack(), meta -> PersistentMetaUtil.getUUID(meta, key(name)));
    }

    @Override
    default int getMetaInt(String name) {
        return get(getItemStack(), meta -> PersistentMetaUtil.getInt(meta, key(name)));
    }

    @Override
    default double getMetaDouble(String name) {
        return get(getItemStack(), meta -> PersistentMetaUtil.getDouble(meta, key(name)));
    }

    @Override
    default LocalDateTime getMetaDate(String name) {
        return get(getItemStack(), meta -> PersistentMetaUtil.getLocalDateTime(meta, key(name)));
    }

    @Override
    default void removeMeta(String name){
        apply(getItemStack(), meta -> PersistentMetaUtil.remove(meta, key(name)));
    }

    @Override
    default void makeStackable() {
        apply(getItemStack(), meta -> PersistentMetaUtil.add(meta, key(UNSTACKABLE_KEY), UUID.randomUUID()));
    }

    @Override
    default void makeUnstackable() {
        apply(getItemStack(), meta -> PersistentMetaUtil.remove(meta, key(UNSTACKABLE_KEY)));
    }

    @Override
    default <T> void addItemEffect(Effect<T> effect) {
        if (AttributeManager.effectOf(effect.getName()) == null) {
            log(Level.WARNING, "Effect \"{0}\" has not been registered!", effect.getName());
            return;
        }
        Set<Effect<?>> effects = new HashSet<>(getItemEffects());
        effects.add(effect);
        String effectString = effects.stream()
                .map(Effect::getName)
                .collect(Collectors.joining("|"));
        removeMeta(ITEM_EFFECTS_TAG);
        addMeta(ITEM_EFFECTS_TAG, effectString);
        LoreUpdate.updateLore(this);
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
    default void addAttribute(Attribute attribute) {
        if (AttributeManager.attributeOf(attribute.getName()) == null) {
            log(Level.WARNING, "Attribute \"{0}\" has not been registered!", attribute.getName());
            return;
        }
        Set<Attribute> attributes = new HashSet<>(getAttributes());
        attributes.add(attribute);
        String attributeString = attributes.stream()
                .map(Attribute::getName)
                .collect(Collectors.joining("|"));
        removeMeta(ITEM_ATTRIBUTES_TAG);
        addMeta(ITEM_ATTRIBUTES_TAG, attributeString);
        LoreUpdate.updateLore(this);
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
    default void setBehaviour(ItemBehaviour itemBehaviour, ItemSlot itemSlot) {
        removeMeta(ITEM_BEHAVIOUR_TAG);
        addMeta(ITEM_BEHAVIOUR_TAG, itemBehaviour.toString());
        removeMeta(ITEM_BEHAVIOUR_SLOT_TAG);
        addMeta(ITEM_BEHAVIOUR_SLOT_TAG, itemSlot.toString());
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
    default void setLore(List<String> lore) {
        removeMeta("lore");
        if (!lore.isEmpty()) {
            addMeta("lore", lore.stream()
                    .map(s -> s.replace(CUSTOM_LORE_SECTION_DELIM, CUSTOM_LORE_SECTION_DELIM_REPLACEMENT))
                    .collect(Collectors.joining(CUSTOM_LORE_SECTION_DELIM))
            );
        }
        LoreUpdate.updateLore(this);
    }

    @Override
    default List<String> getLore() {
        String loreString = getMetaString("lore");
        if (loreString == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(loreString.split(CUSTOM_LORE_SECTION_DELIM)).toList();
    }

    class LoreUpdate {
        private static void updateLore(ItemStackProxyBase proxy) {
            List<String> currentLore = proxy.getLore();
            if (currentLore == null) {
                currentLore = new ArrayList<>();
            }
            final List<String> lore = currentLore;

            proxy.getItemEffects().stream()
                    .filter(effect -> effect.getDisplayName() != null && !effect.getDisplayName().isEmpty())
                    .forEach(effect -> lore.add(effect.getDisplayName()));
            proxy.getAttributes().stream()
                    .filter(attribute -> attribute.getDisplayName() != null && !attribute.getDisplayName().isEmpty())
                    .forEach(attribute -> lore.add(attribute.getDisplayName()));

            apply(proxy.getItemStack(), meta -> meta.setLore(lore));
        }
    }

}
