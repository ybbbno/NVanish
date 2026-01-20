package me.ybbbno.nvanish.vanish;

import me.deadybbb.ybmj.BasicConfigHandler;
import me.deadybbb.ybmj.PluginProvider;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class VanishConfigManager extends BasicConfigHandler {

    public VanishConfigManager(PluginProvider plugin) {
        super(plugin, "vanish.yml");
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
