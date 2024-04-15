package com.github.oobila.bukkit.itemstack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
@Getter
public class ItemStackProxy implements IItemStackProxy {

    private final ItemStack itemStack;

}
