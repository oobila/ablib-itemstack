package com.github.oobila.bukkit.itemstack;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CustomItemStack extends ItemStack implements IItemStackProxy {

    public CustomItemStack(Material material){
        super(material);
    }

    public CustomItemStack(ItemStack itemStack){
        super(itemStack.getType());
        setItemMeta(itemStack.getItemMeta());
        setAmount(itemStack.getAmount());
        setData(itemStack.getData());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public ItemStack getItemStack() {
        return this;
    }

    public static ItemStackBuilder builder(Material material){
        return new ItemStackBuilder(material);
    }

    public static ItemStackBuilder builder(ItemStack itemStack){
        return new ItemStackBuilder(itemStack);
    }
}
