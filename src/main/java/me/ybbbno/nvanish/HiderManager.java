package me.ybbbno.playerTabHider;

import me.deadybbb.ybmj.PluginProvider;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class HiderManager implements Listener {
    private boolean is_init;

    private final Set<UUID> hided = new HashSet<>();

    private final PluginProvider plugin;
    private final HiderConfigManager config;

    public HiderManager(PluginProvider plugin) {
        this.plugin = plugin;
        this.config = new HiderConfigManager(plugin);
        is_init = false;
    }

    public boolean is_init_set() {
        return is_init;
    }

    public void init() {
        if (is_init) return;
        hided.addAll(config.getPlayers());
        for (UUID uuid : hided) {
            Player p = (Player) Bukkit.getEntity(uuid);
            if (p != null) hidePlayerFromAll(p);
        }
        is_init = true;
    }

    public void deinit() {
        if (!is_init) return;
        for (UUID uuid : hided) {
            Player p = (Player) Bukkit.getEntity(uuid);
            if (p != null) showPlayerToAll(p);
        }
        config.setPlayers(hided);
        hided.clear();
        is_init = false;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(!is_init) return;
        Player joined = event.getPlayer();

        if (isPlayerHided(joined)) {
            hidePlayerFromAll(joined);
            event.joinMessage(null);
        } else {
            hideFromThatPlayer(joined);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (!is_init) return;
        Player quited = event.getPlayer();

        if (isPlayerHided(quited)) {
            event.quitMessage(null);
        }
    }

    public void toggle(Player p) {
        if (!is_init) return;

        if (!isPlayerHided(p)) {
            hided.add(p.getUniqueId());
            hidePlayerFromAll(p);
            plugin.logger.info(p.getName() + " has hided from tab list.");
        } else {
            hided.remove(p.getUniqueId());
            showPlayerToAll(p);
            plugin.logger.info(p.getName() + " has added to tab list.");
        }
    }

    public boolean isPlayerHided(Player p) {
        return hided.contains(p.getUniqueId());
    }

    public void hideFromThatPlayer(Player p) {
        for (UUID id : hided) {
            Player vp = Bukkit.getPlayer(id);
            if (vp != null && !p.hasPermission("tab_hider.see"))
                p.unlistPlayer(vp);
        }
    }

    public void showToThatPlayer(Player p) {
        for (UUID id : hided) {
            Player vp = Bukkit.getPlayer(id);
            if (vp != null && !p.hasPermission("tab_hider.see"))
                p.listPlayer(vp);
        }
    }

    public void hidePlayerFromAll(Player p) {
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (!other.hasPermission("tab_hider.see"))
                other.unlistPlayer(p);
        }
    }

    public void showPlayerToAll(Player p) {
        for (Player other : Bukkit.getOnlinePlayers())
            other.listPlayer(p);
    }
}
