package me.ybbbno.nvanish.pm;

import me.deadybbb.ybmj.BasicConfigHandler;
import me.deadybbb.ybmj.PluginProvider;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class PMConfigHandler extends BasicConfigHandler {
    public PMConfigHandler(PluginProvider plugin) {
        super(plugin, "private_messages.yml");
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
