package me.ybbbno.nvanish;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public final class NVanishAPI {
    private final NVanish plugin;

    private NVanishAPI(NVanish plugin) {
        this.plugin = plugin;
    }

    public PriorityManager getManager() {
        return plugin.getManager();
    }

    public static NVanishAPI getAPI() {
        Plugin p = Bukkit.getPluginManager().getPlugin("NVanish");
        if (p instanceof NVanish) {
            return new NVanishAPI((NVanish) p);
        }
        return null;
    }
}
