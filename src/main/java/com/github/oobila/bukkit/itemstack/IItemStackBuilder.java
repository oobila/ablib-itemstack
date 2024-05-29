package com.github.oobila.bukkit.itemstack;

import com.github.oobila.bukkit.chat.Message;
import com.github.oobila.bukkit.effects.Attribute;
import com.github.oobila.bukkit.effects.AttributeManager;
import com.github.oobila.bukkit.effects.Effect;
import com.github.oobila.bukkit.itemstack.effects.ItemBehaviour;
import com.github.oobila.bukkit.itemstack.effects.ItemSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static com.github.oobila.bukkit.common.ABCommon.key;
import static com.github.oobila.bukkit.common.ABCommon.log;

public interface IItemStackBuilder<T extends ExtendedItemStack<T>> extends ExtendedItemStack<T>, ItemStackProvider {

    String UNSTACKABLE_KEY = "unstackable";
    String ITEM_EFFECTS_TAG = "item-effects";
    String ITEM_ATTRIBUTES_TAG = "item-attributes";
    String ITEM_BEHAVIOUR_TAG = "item-behaviour";
    String ITEM_BEHAVIOUR_SLOT_TAG = "item-behaviour-slot";
    String CUSTOM_LORE_SECTION_DELIM = "//";
    String CUSTOM_LORE_SECTION_DELIM_REPLACEMENT = "/ /";

    T getReturnObject();

    @Override
    default T setDisplayName(String displayName) {
        apply(meta -> meta.setDisplayName(displayName));
        return getReturnObject();
    }

    @Override
    default T setCount(int count) {
        getItemStack().setAmount(count);
        return getReturnObject();
    }

    @Override
    default T setDamage(int damage) {
        apply(meta -> {
            if (meta instanceof Damageable damageable) {
                damageable.setDamage(damage);
            }
        });
        return getReturnObject();
    }

    @Override
    default T addMeta(String name, String value) {
        apply(meta -> PersistentMetaUtil.add(meta, key(name), value));
        return getReturnObject();
    }

    @Override
    default T addMeta(String name, UUID value) {
        apply(meta -> PersistentMetaUtil.add(meta, key(name), value));
        return getReturnObject();
    }

    @Override
    default T addMeta(String name, int value) {
        apply(meta -> PersistentMetaUtil.add(meta, key(name), value));
        return getReturnObject();
    }

    @Override
    default T addMeta(String name, double value) {
        apply(meta -> PersistentMetaUtil.add(meta, key(name), value));
        return getReturnObject();
    }

    @Override
    default T addMeta(String name, LocalDateTime value) {
        apply(meta -> PersistentMetaUtil.add(meta, key(name), value));
        return getReturnObject();
    }

    @Override
    default T makeUnstackable() {
        apply(meta -> PersistentMetaUtil.remove(meta, key(UNSTACKABLE_KEY)));
        return getReturnObject();
    }

    @Override
    default T addItemEffect(Effect<?> effect) {
        if (AttributeManager.effectOf(effect.getName()) == null) {
            log(Level.WARNING, "Effect \"{0}\" has not been registered!", effect.getName());
            return getReturnObject();
        }
        Set<Effect<?>> effects = new HashSet<>(getItemEffects());
        effects.add(effect);
        String effectString = effects.stream()
                .map(Effect::getName)
                .collect(Collectors.joining("|"));
        removeMeta(ITEM_EFFECTS_TAG);
        addMeta(ITEM_EFFECTS_TAG, effectString);
        updateLore();
        return getReturnObject();
    }

    @Override
    default T addAttribute(Attribute attribute) {
        if (AttributeManager.attributeOf(attribute.getName()) == null) {
            log(Level.WARNING, "Attribute \"{0}\" has not been registered!", attribute.getName());
            return getReturnObject();
        }
        Set<Attribute> attributes = new HashSet<>(getAttributes());
        attributes.add(attribute);
        String attributeString = attributes.stream()
                .map(Attribute::getName)
                .collect(Collectors.joining("|"));
        removeMeta(ITEM_ATTRIBUTES_TAG);
        addMeta(ITEM_ATTRIBUTES_TAG, attributeString);
        updateLore();
        return getReturnObject();
    }

    @Override
    default T setBehaviour(ItemBehaviour itemBehaviour, ItemSlot itemSlot) {
        removeMeta(ITEM_BEHAVIOUR_TAG);
        addMeta(ITEM_BEHAVIOUR_TAG, itemBehaviour.toString());
        removeMeta(ITEM_BEHAVIOUR_SLOT_TAG);
        addMeta(ITEM_BEHAVIOUR_SLOT_TAG, itemSlot.toString());
        return getReturnObject();
    }

    @Override
    default T setLore(List<String> lore) {
        removeMeta("lore");
        if (!lore.isEmpty()) {
            addMeta("lore", lore.stream()
                    .map(s -> s.replace(CUSTOM_LORE_SECTION_DELIM, CUSTOM_LORE_SECTION_DELIM_REPLACEMENT))
                    .collect(Collectors.joining(CUSTOM_LORE_SECTION_DELIM))
            );
        }
        updateLore();
        return getReturnObject();
    }

    @Override
    default T addLore(String lore) {
        List<String> itemLore = new ArrayList<>(getLore());
        itemLore.add(lore);
        setLore(itemLore);
        return getReturnObject();
    }

    @Override
    default T addLore(Message lore) {
        return addLore(lore.toString());
    }

    default ItemStack build() {
        return getItemStack();
    }

    default void updateLore() {
        List<String> currentLore = getLore();
        if (currentLore == null) {
            currentLore = new ArrayList<>();
        }
        final List<String> lore = currentLore;

        getItemEffects().stream()
                .filter(effect -> effect.getDisplayName() != null && !effect.getDisplayName().isEmpty())
                .forEach(effect -> lore.add(effect.getDisplayName()));
        getAttributes().stream()
                .filter(attribute -> attribute.getDisplayName() != null && !attribute.getDisplayName().isEmpty())
                .forEach(attribute -> lore.add(attribute.getDisplayName()));

        apply(meta -> meta.setLore(lore));
    }

    default void apply(ItemMetaConsumer consumer) {
        ItemMeta meta = getItemStack().getItemMeta();
        consumer.consume(meta);
        getItemStack().setItemMeta(meta);
    }

    interface ItemMetaConsumer {
        void consume(ItemMeta meta);
    }
}
