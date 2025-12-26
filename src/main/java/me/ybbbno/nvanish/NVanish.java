package me.ybbbno.nvanish;

import me.deadybbb.ybmj.PluginProvider;
import me.ybbbno.nvanish.commands.NVanishCommand;
import me.ybbbno.nvanish.commands.TabHideCommand;
import me.ybbbno.nvanish.commands.VanishCommand;
import com.nickuc.login.api.nLoginAPI;

public final class NVanish extends PluginProvider {
    private PriorityManager priorityM;

    public void onEnable() {
        saveDefaultConfig();

        boolean hasNLoginAPI = false;
        try {
            hasNLoginAPI = nLoginAPI.getApi().isAvailable();
        } catch (NoClassDefFoundError err) {
            logger.warning("nLogin is not found!");
        }

        priorityM = new PriorityManager(this, hasNLoginAPI);
        priorityM.init();
        getServer().getPluginManager().registerEvents(priorityM, this);

        registerCommand("vanish", new VanishCommand(this));
        registerCommand("tabhide", new TabHideCommand(this));
        registerCommand("nvanish", new NVanishCommand(this));
    }

    public void onDisable() {
        priorityM.deinit();
    }

    public PriorityManager getManager() {
        return priorityM;
    }
}
