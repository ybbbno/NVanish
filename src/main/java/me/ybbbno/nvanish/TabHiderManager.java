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

public class TabHiderManager extends BasicManagerHandler implements Listener {
    private final Set<UUID> hiddenPlayers = new HashSet<>();
    private final TabHiderConfigManager config;

    public TabHiderManager(PluginProvider plugin) {
        super(plugin);
        this.config = new TabHiderConfigManager(plugin);
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

//    @EventHandler
//    public void onCommand(PlayerCommandPreprocessEvent event) {
//        if (!is_init) return;
//
//        String[] parts = event.getMessage().split(" ");
//        plugin.logger.info(String.valueOf(parts[0].equals("/deop")));
//        if (!parts[0].equals("/deop") || !parts[0].equals("/op")) return;
//
//        Player player = Bukkit.getPlayer(parts[1]);
//        plugin.logger.info(player.getName());
//        if (player == null) return;
//
//        if (parts[0].contains("/deop")) {
//            hideFromThatPlayer(player);
//        } else {
//            showToThatPlayer(player);
//        }
//    }

    public void toggle(Player p) {
        if (!is_init_set()) return;

        if (!isPlayerHidden(p)) {
            hidePlayerFromAll(p);
            plugin.logger.info(p.getName() + " hide from tab list.");
        } else {
            showPlayerToAll(p);
            plugin.logger.info(p.getName() + " unhide to tab list.");
        }
    }

    public void hideHiddenPlayers() {
        for (UUID uuid : hiddenPlayers) {
            Player p = (Player) Bukkit.getEntity(uuid);
            if (p != null) hidePlayerFromAll(p);
        }
    }

    public void showHiddenPlayers() {
        for (UUID uuid : hiddenPlayers) {
            Player p = (Player) Bukkit.getEntity(uuid);
            if (p != null) showPlayerToAll(p);
        }
    }

    public boolean isPlayerHidden(Player p) {
        return hiddenPlayers.contains(p.getUniqueId());
    }

    public void hideFromThatPlayer(Player p) {
        for (UUID id : hiddenPlayers) {
            Player vp = Bukkit.getPlayer(id);
            if (vp != null && !p.hasPermission("tab_hider.see") && vp != p) {
                p.unlistPlayer(vp);
            }
        }
    }

    public void showToThatPlayer(Player p) {
        for (UUID id : hiddenPlayers) {
            Player vp = Bukkit.getPlayer(id);
            if (vp != null && vp != p)
                p.listPlayer(vp);
        }
    }

    public void hidePlayerFromAll(Player p) {
        if (!isPlayerHidden(p))
            hiddenPlayers.add(p.getUniqueId());

        for (Player other : Bukkit.getOnlinePlayers()) {
            if (!other.hasPermission("tab_hider.see") && other != p)
                other.unlistPlayer(p);
        }
    }

    public void showPlayerToAll(Player p) {
        if (isPlayerHidden(p))
            hiddenPlayers.remove(p.getUniqueId());

        for (Player other : Bukkit.getOnlinePlayers())
            if (other != p) other.listPlayer(p);
    }
}
