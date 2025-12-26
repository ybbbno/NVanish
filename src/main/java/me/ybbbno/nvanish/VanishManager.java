package me.ybbbno.nvanish;

import me.deadybbb.ybmj.BasicManagerHandler;
import me.deadybbb.ybmj.PluginProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VanishManager extends BasicManagerHandler implements Listener {
    private final Set<UUID> vanishedPlayers = new HashSet<>();
    private final VanishConfigManager config;

    public VanishManager(PluginProvider plugin) {
        super(plugin);
        this.config = new VanishConfigManager(plugin);
    }

    @Override
    protected void onInit() {
        vanishedPlayers.clear();
        vanishedPlayers.addAll(config.getPlayers());
    }

    @Override
    protected void onDeinit() {
        config.setPlayers(vanishedPlayers);
    }

    public void hideVanishedPlayers() {
        for (UUID uuid : vanishedPlayers) {
            Player p = (Player) Bukkit.getEntity(uuid);
            if (p != null) hidePlayerFromAll(p);
        }
    }

    public void showVanishedPlayers() {
        for (UUID uuid : vanishedPlayers) {
            Player p = (Player) Bukkit.getEntity(uuid);
            if (p != null) showPlayerToAll(p);
        }
    }

    public void toggle(Player p) {
        if (!is_init_set()) return;

        if (!isPlayerVanished(p)) {
            hidePlayerFromAll(p);
            plugin.logger.info(p.getName() + " vanished.");
        } else {
            showPlayerToAll(p);
            plugin.logger.info(p.getName() + " unvanished.");
        }
    }

    public boolean isPlayerVanished(Player p) {
        return vanishedPlayers.contains(p.getUniqueId());
    }

    public void hideFromThatPlayer(Player p) {
        for (UUID id : vanishedPlayers) {
            Player vp = Bukkit.getPlayer(id);
            if (vp != null && !p.hasPermission("vanish.see") && vp != p)
                p.hidePlayer(plugin, vp);
        }
    }

    public void showToThatPlayer(Player p) {
        for (UUID id : vanishedPlayers) {
            Player vp = Bukkit.getPlayer(id);
            if (vp != null && vp != p)
                p.listPlayer(vp);
        }
    }


    public void hidePlayerFromAll(Player p) {
        if (!isPlayerVanished(p))
            vanishedPlayers.add(p.getUniqueId());

        for (Player other : Bukkit.getOnlinePlayers()) {
            if (!other.hasPermission("vanish.see") && other != p)
                other.hidePlayer(plugin, p);
        }
    }

    public void showPlayerToAll(Player p) {
        if (isPlayerVanished(p))
            vanishedPlayers.remove(p.getUniqueId());

        for (Player other : Bukkit.getOnlinePlayers())
            if (other != p) other.showPlayer(plugin, p);
    }
}
