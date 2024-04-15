package com.github.oobila.bukkit.itemstack;

import com.github.oobila.bukkit.effects.Attribute;
import com.github.oobila.bukkit.effects.Effect;
import com.github.oobila.bukkit.itemstack.effects.ItemBehaviour;
import com.github.oobila.bukkit.itemstack.effects.ItemSlot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ExtendedItemStack {

    void setDisplayName(String displayName);

    void addMeta(String name, String value);

    void addMeta(String name, UUID value);

    void addMeta(String name, int value);

    void addMeta(String name, double value);

    void addMeta(String name, LocalDateTime value);

    String getMetaString(String name);

    UUID getMetaUUID(String name);

    int getMetaInt(String name);

    double getMetaDouble(String name);

    LocalDateTime getMetaDate(String name);

    void removeMeta(String name);

    void makeStackable();

    void makeUnstackable();

    <T> void addItemEffect(Effect<T> effect);

    Set<Effect<?>> getItemEffects();

    void addAttribute(Attribute attribute);

    Set<Attribute> getAttributes();

    void setBehaviour(ItemBehaviour itemBehaviour, ItemSlot itemSlot);

    ItemBehaviour getBehaviour();

    ItemSlot getBehaviourSlot();

    void setLore(List<String> lore);

    List<String> getLore();

}
