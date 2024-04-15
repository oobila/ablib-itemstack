package com.github.oobila.bukkit.itemstack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
@Getter
public class ItemStackBuilder implements IItemStackProxy<ItemStackBuilder> {

    private final ItemStack itemStack;

    public ItemStackBuilder(Material material) {
        this.itemStack = new ItemStack(material);
    }

    @Override
    public ItemStackBuilder getReturnObject() {
        return this;
    }

}
