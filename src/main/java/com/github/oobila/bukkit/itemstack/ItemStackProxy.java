package com.github.oobila.bukkit.itemstack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static com.github.oobila.bukkit.chat.Message.message;

@RequiredArgsConstructor
@Getter
public class ItemStackProxy implements IItemStackProxy<ItemStackProxy> {

    private final ItemStack itemStack;

    @Override
    public ItemStackProxy getReturnObject() {
        return this;
    }

    public static ItemStackProxy item(Material material) {
        return new ItemStackProxy(new ItemStack(material));
    }

    public static ItemStackProxy item(Material material, String name) {
        return new ItemStackProxy(new ItemStack(material)).setDisplayName(name);
    }

    public static ItemStackProxy skull(String texture) {
        return new ItemStackProxy(new NonPlayerSkull(texture));
    }

    public static ItemStackProxy skull(String texture, String name) {
        return new ItemStackProxy(new NonPlayerSkull(texture)).setDisplayName(name);
    }

    public static ItemStackProxy skull(Player player) {
        return new ItemStackProxy(new PlayerSkull(player));
    }

    public static ItemStackProxy skull(Player player, String name) {
        return new ItemStackProxy(new PlayerSkull(player)).setDisplayName(name);
    }

    public ItemStackProxy lore(String message, String... args) {
        return this.addLore(message(message, args));
    }
}
