package me.ybbbno.nvanish;

import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.deadybbb.ybmj.LegacyTextHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VanishManager implements Listener {
    private boolean is_init;

    private final Set<UUID> vanished = new HashSet<>();

    private final NVanish plugin;
    private final FileConfiguration config;
    private final Server server;

    private ScheduledTask actionBarTask;

    public VanishManager(NVanish plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.server = plugin.getServer();
        is_init = false;
    }

    public boolean is_init_set() {
        return is_init;
    }

    public void init() {
        if (is_init) return;
        if (config.getBoolean("features.action-bar", true))
            actionBarTask = server.getGlobalRegionScheduler().runAtFixedRate(plugin,
                    (task) -> { updateActionBars(); }, 1L, 20L);
        is_init = true;
    }

    public void deinit() {
        if (!is_init) return;
        if (actionBarTask != null)
            actionBarTask.cancel();
        for (UUID uuid : vanished) {
            Player p = (Player) Bukkit.getEntity(uuid);
            if (p != null) toggleVanish(p);
        }
        vanished.clear();
        is_init = false;
    }

    public boolean isPlayerVanished(Player p) {
        return vanished.contains(p.getUniqueId());
    }

    public void toggleVanish(Player p) {
        if (!is_init) return;
        if (!vanished.contains(p.getUniqueId())) {
            vanished.add(p.getUniqueId());
            hidePlayer(p);
            LegacyTextHandler.sendFormattedMessage(p, config.getString("messages.vanish"));
            plugin.logger.info(p.getName() + " has vanished.");
        } else {
            vanished.remove(p.getUniqueId());
            showPlayer(p);
            LegacyTextHandler.sendFormattedMessage(p, config.getString("messages.unvanish"));
            p.sendActionBar("");
            plugin.logger.info(p.getName() + " has un-vanished.");
        }
    }

    public void hideFromThatPlayer(Player p) {
        for (UUID id : vanished) {
            Player vp = Bukkit.getPlayer(id);
            if (vp != null && !p.hasPermission("vanish.see"))
                p.hidePlayer(plugin, vp);
        }
    }

    public void hidePlayer(Player p) {
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (!other.hasPermission("vanish.see"))
                other.hidePlayer(plugin, p);
        }
    }

    public void showPlayer(Player p) {
        for (Player other : Bukkit.getOnlinePlayers())
            other.showPlayer(plugin, p);
    }

    private void updateActionBars() {
        if (!is_init) return;
        Component barMsg = LegacyTextHandler.parseText(config.getString("messages.action-bar"));
        for (UUID id : vanished) {
            Player p = Bukkit.getPlayer(id);
            if (p != null && p.isOnline())
                p.sendActionBar(barMsg);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!is_init) return;
        Player joined = e.getPlayer();
        if (config.getBoolean("features.silent-joinquit", true) &&
                vanished.contains(joined.getUniqueId()))
            e.joinMessage(null);

        if (isPlayerVanished(joined)) {
//            plugin.logger.info("Joined vanished player: " + joined.getName());
            hidePlayer(joined);
        } else {
//            plugin.logger.info("Hide vanished players to that joined player: " + joined.getName());
            hideFromThatPlayer(joined);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (!is_init) return;
        if (config.getBoolean("features.silent-joinquit", true) &&
                vanished.contains(e.getPlayer().getUniqueId()))
            e.quitMessage(null);
    }

    @EventHandler
    public void onChat(AsyncChatEvent e) {
        if (!is_init) return;
        if (config.getBoolean("features.disable-chat", true) &&
                vanished.contains(e.getPlayer().getUniqueId())) {
            LegacyTextHandler.sendFormattedMessage(e.getPlayer(), config.getString("messages.cannot-chat"));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!is_init) return;
        if (config.getBoolean("features.disable-damage", true) &&
                e.getDamager() instanceof Player &&
                vanished.contains(e.getDamager().getUniqueId()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onTarget(EntityTargetEvent e) {
        if (!is_init) return;
        if (config.getBoolean("features.disable-target", true) &&
                e.getTarget() instanceof Player &&
                vanished.contains((e.getTarget()).getUniqueId()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if (!is_init) return;
        if (config.getBoolean("features.disable-pickup", true) &&
                e.getEntity() instanceof Player &&
                vanished.contains(((Player)e.getEntity()).getUniqueId()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (!is_init) return;
        if (config.getBoolean("features.disable-build", true) &&
                vanished.contains(e.getPlayer().getUniqueId()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (!is_init) return;
        if (config.getBoolean("features.disable-build", true) &&
                vanished.contains(e.getPlayer().getUniqueId()))
            e.setCancelled(true);
    }
}
