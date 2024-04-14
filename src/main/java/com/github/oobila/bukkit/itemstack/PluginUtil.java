package com.github.oobila.bukkit.itemstack;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PluginUtil {

    private static Plugin plugin = null;

    public static Plugin getCorePlugin() {
        if (plugin == null){
            plugin = Bukkit.getPluginManager().getPlugin("ABCore");
        }
        return plugin;
    }

}
