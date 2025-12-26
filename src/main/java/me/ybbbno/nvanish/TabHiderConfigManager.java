package me.ybbbno.nvanish;

import me.deadybbb.ybmj.BasicConfigHandler;
import me.deadybbb.ybmj.PluginProvider;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class TabHiderConfigManager extends BasicConfigHandler {
    private PluginProvider plugin;

    public TabHiderConfigManager(PluginProvider plugin) {
        super(plugin, "tabhider.yml");
    }

    public Set<UUID> getPlayers() {
        reloadConfig();
        return config.getStringList("players").stream().map(UUID::fromString).collect(Collectors.toSet());
    }

    public void setPlayers(Set<UUID> players) {
        config.set("players", players.stream().map(UUID::toString).toList());
        saveConfig();
    }
}
