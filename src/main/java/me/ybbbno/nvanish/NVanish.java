package me.ybbbno.nvanish;

import me.deadybbb.ybmj.PluginProvider;
import me.ybbbno.nvanish.commands.ReloadCommand;
import me.ybbbno.nvanish.commands.TabHideCommand;
import me.ybbbno.nvanish.commands.VanishCommand;
import com.nickuc.login.api.nLoginAPI;

public final class NVanish extends PluginProvider {
    private VanishManager vanishM;
    private HiderManager tabM;
    private NLoginManager nLoginM;

    public boolean hasNLoginApi = false;

    public void onEnable() {
        saveDefaultConfig();

        vanishM = new VanishManager(this);
        getServer().getPluginManager().registerEvents(vanishM, this);
        vanishM.init();

        tabM = new HiderManager(this);
        getServer().getPluginManager().registerEvents(tabM, this);
        tabM.init();

        try {
            hasNLoginApi = nLoginAPI.getApi().isAvailable();
            nLoginM = new NLoginManager(this, vanishM, tabM);
            getServer().getPluginManager().registerEvents(nLoginM, this);
        } catch (NoClassDefFoundError err) {
            logger.warning("nLogin is not found!");
        }

        registerCommand("vanish", new VanishCommand(vanishM));
        registerCommand("tabhide", new TabHideCommand(tabM));
        registerCommand("vanishsettings", new ReloadCommand(this));
    }

    public void onDisable() {
        vanishM.deinit();
        tabM.deinit();
    }
}
