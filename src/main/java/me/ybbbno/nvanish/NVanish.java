package me.ybbbno.nvanish;

import io.papermc.paper.command.brigadier.BasicCommand;
import me.deadybbb.ybmj.PluginProvider;
import me.ybbbno.nvanish.pm.PMCommand;
import me.ybbbno.nvanish.tabhider.TabHideCommand;
import me.ybbbno.nvanish.vanish.VanishCommand;
import com.nickuc.login.api.nLoginAPI;
import de.maxhenkel.voicechat.api.BukkitVoicechatService;
import me.ybbbno.nvanish.voicechat.VoicechatManager;

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

        boolean hasVoicechatAPI = false;
        try {
            BukkitVoicechatService service = getServer().getServicesManager().load(BukkitVoicechatService.class);
            if (service != null) {
                VoicechatManager m = new VoicechatManager(this);
                service.registerPlugin(m);
                getServer().getPluginManager().registerEvents(m, this);
            }
        } catch (NoClassDefFoundError err) {
            logger.warning("Simple Voice Chat is not found!");
        }

        priorityM = new PriorityManager(this, hasNLoginAPI);
        priorityM.init();
        getServer().getPluginManager().registerEvents(priorityM, this);

        registerCommand("vanish", new VanishCommand(this));
        registerCommand("tabhide", new TabHideCommand(this));
        registerCommand("nvanish", new NVanishCommand(this));

        BasicCommand msg = new PMCommand(this);

        registerCommand("msg", msg);
        registerCommand("w", msg);
        registerCommand("tell", msg);
    }

    public void onDisable() {
        priorityM.deinit();
    }

    public PriorityManager getManager() {
        return priorityM;
    }
}
