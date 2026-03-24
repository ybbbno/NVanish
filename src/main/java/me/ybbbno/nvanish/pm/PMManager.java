package me.ybbbno.nvanish.pm;

import me.deadybbb.ybmj.BasicManagerHandler;
import me.deadybbb.ybmj.PluginProvider;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PMManager extends BasicManagerHandler {
    private final Set<UUID> hiddenPlayers = new HashSet<>();
    private final PMConfigHandler config;

    public PMManager(PluginProvider plugin) {
        super(plugin);
        this.config = new PMConfigHandler(plugin);
    }

    @Override
    protected void onInit() {
        hiddenPlayers.clear();
        hiddenPlayers.addAll(config.getPlayers());
    }

    @Override
    protected void onDeinit() {
        config.setPlayers(hiddenPlayers);
    }

    public void toggle(Player p) {
        if (!isInit()) return;

        if (!isPlayerHidden(p)) {
            hidePlayer(p);
            plugin.logger.info(p.getName() + " hide from msg command.");
        } else {
            showPlayer(p);
            plugin.logger.info(p.getName() + " unhide to msg command.");
        }
    }

    public boolean isPlayerHidden(Player p) {
        if (!isInit()) return false;
        return hiddenPlayers.contains(p.getUniqueId());
    }

    public boolean hidePlayer(Player p) {
        if (!isInit()) return false;
        return hiddenPlayers.add(p.getUniqueId());
    }

    public boolean showPlayer(Player p) {
        if (!isInit()) return false;
        return hiddenPlayers.remove(p.getUniqueId());
    }
}
