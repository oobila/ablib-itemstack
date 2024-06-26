package com.github.oobila.bukkit.itemstack;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Utility class to help with common functions for Minecraft ItemStacks
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemStackUtil {

    /**
     * Force give an item to a player, if their inventory is full it will drop the item on the ground infront of them.
     * @param player
     * @param itemStack
     */
    public static void givePlayer(Player player, ItemStack itemStack){
        if(player.getInventory().firstEmpty() < 0){
            player.getWorld().dropItem(player.getLocation(), itemStack);
        } else {
            player.getInventory().addItem(itemStack);
        }
    }

    /**
     * Force give items to a player, if their inventory is full it will drop the items on the ground infront of them.
     * @param player
     * @param itemStacks
     */
    public static void givePlayer(Player player, ItemStack[] itemStacks){
        for(ItemStack itemStack : itemStacks){
            givePlayer(player, itemStack);
        }
    }
}
