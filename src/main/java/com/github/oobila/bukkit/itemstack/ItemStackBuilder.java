package com.github.oobila.bukkit.itemstack;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ItemStackBuilder {

    private final ItemStack itemStack;
    private final ItemMeta meta;

    public ItemStackBuilder(Material material) {
        itemStack = new CustomItemStack(material);
        meta = itemStack.getItemMeta();
    }

    public ItemStackBuilder(ItemStack itemStack) {
        this.itemStack = new CustomItemStack(itemStack);
        meta = this.itemStack.getItemMeta();
    }

    public ItemStackBuilder displayName(String displayName) {
        meta.setDisplayName(displayName);
        return this;
    }

    public ItemStackBuilder itemCount(int i) {
        itemStack.setAmount(i);
        return this;
    }

    public ItemStackBuilder lore(List<String> lore) {
        ItemStackProxy proxy = new ItemStackProxy(itemStack);
        proxy.setLore(lore);
        return this;
    }

    public ItemStackBuilder secretString(NamespacedKey namespacedKey, String string) {
        PersistentMetaUtil.add(meta, namespacedKey, string);
        return this;
    }

    public ItemStackBuilder secretUUID(NamespacedKey namespacedKey, UUID uuid) {
        PersistentMetaUtil.add(meta, namespacedKey, uuid);
        return this;
    }

    public ItemStackBuilder secretLocalDateTime(NamespacedKey namespacedKey, LocalDateTime localDateTime) {
        PersistentMetaUtil.add(meta, namespacedKey, localDateTime);
        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
