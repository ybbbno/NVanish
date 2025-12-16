package me.ybbbno.playerTabHider;

import me.deadybbb.ybmj.BasicConfigHandler;
import me.deadybbb.ybmj.PluginProvider;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class HiderConfigManager extends BasicConfigHandler {
    private PluginProvider plugin;

    public HiderConfigManager(PluginProvider plugin) {
        super(plugin, "tabhider.yml");

        this.plugin = plugin;
    }

    public Set<UUID> getPlayers() {
        reloadConfig();
        return config.getStringList("players").stream().map(UUID::fromString).collect(Collectors.toSet());
    }

    public void setPlayers(Set<UUID> players) {
        config.set("players", players.stream().toList());
        saveConfig();
    }
}
