package com.github.oobila.bukkit.itemstack;

import com.github.oobila.bukkit.chat.Message;
import com.github.oobila.bukkit.effects.Attribute;
import com.github.oobila.bukkit.effects.Effect;
import com.github.oobila.bukkit.itemstack.effects.ItemBehaviour;
import com.github.oobila.bukkit.itemstack.effects.ItemSlot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ExtendedItemStack<T extends ExtendedItemStack<T>> {

    T setDisplayName(String displayName);

    String getDisplayName();

    T setCount(int count);

    int getCount();

    T setDamage(int damage);

    int getDamage();

    T addMeta(String name, String value);

    T addMeta(String name, UUID value);

    T addMeta(String name, int value);

    T addMeta(String name, double value);

    T addMeta(String name, LocalDateTime value);

    String getMetaString(String name);

    UUID getMetaUUID(String name);

    int getMetaInt(String name);

    double getMetaDouble(String name);

    LocalDateTime getMetaDate(String name);

    void removeMeta(String name);

    void makeStackable();

    T makeUnstackable();

    T addItemEffect(Effect<?> effect);

    @SuppressWarnings("java:S1452")
    Set<Effect<?>> getItemEffects();

    T addAttribute(Attribute attribute);

    Set<Attribute> getAttributes();

    T setBehaviour(ItemBehaviour itemBehaviour, ItemSlot itemSlot);

    ItemBehaviour getBehaviour();

    ItemSlot getBehaviourSlot();

    T setLore(List<String> lore);

    T addLore(String lore);

    T addLore(Message lore);

    List<String> getLore();

}
