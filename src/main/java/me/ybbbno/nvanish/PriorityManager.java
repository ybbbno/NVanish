package me.ybbbno.nvanish;

import me.deadybbb.ybmj.BasicManagerHandler;
import me.deadybbb.ybmj.PluginProvider;
import me.ybbbno.nvanish.pm.PMManager;
import me.ybbbno.nvanish.tabhider.TabHiderManager;
import me.ybbbno.nvanish.vanish.VanishManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PriorityManager extends BasicManagerHandler implements Listener {
    private final boolean hasNLoginApi;
    private final VanishManager vanishM;
    private final TabHiderManager tabM;
    private final PMManager pmM;

    public PriorityManager(PluginProvider plugin, boolean hasNLoginAPI) {
        super(plugin);
        this.hasNLoginApi = hasNLoginAPI;
        this.vanishM = new VanishManager(plugin);
        this.tabM = new TabHiderManager(plugin);
        this.pmM = new PMManager(plugin);
    }

    @Override
    protected void onInit() {
        vanishM.init();
        tabM.init();
        pmM.init();

        for (Player p : Bukkit.getOnlinePlayers()) {
            reloadState(p);
        }
    }

    @Override
    protected void onDeinit() {
        vanishM.deinit();
        tabM.deinit();
        pmM.deinit();

        for (Player p : Bukkit.getOnlinePlayers()) {
            reloadState(p);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (hasNLoginApi) return;
        Player joined = event.getPlayer();

        reloadState(joined);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player quited = event.getPlayer();

        if (vanishM.isPlayerVanished(quited) || tabM.isPlayerHidden(quited)) {
            event.quitMessage(null);
        }
    }

    public void toggleVanish(Player p) {
        if (!vanishM.isInit()) return;

        if (tabM.isPlayerHidden(p)) {
            tabM.toggle(p);
        }

        vanishM.toggle(p);

        reloadState(p);
    }

    public void toggleTabHider(Player p) {
        if (!tabM.isInit()) return;

        if (vanishM.isPlayerVanished(p)) {
            vanishM.toggle(p);
        }

        tabM.toggle(p);

        reloadState(p);
    }

    public boolean isPlayerVanished(Player p) {
        return vanishM.isPlayerVanished(p);
    }

    public boolean isPlayerTabHidden(Player p) {
        return tabM.isPlayerHidden(p);
    }

    public boolean isPlayerPMHidden(Player p) { return pmM.isPlayerHidden(p); }

    public void reloadState(Player p) {
        if (vanishM.isPlayerVanished(p)) {
            vanishM.hidePlayerFromAll(p);
            if (pmM.isPlayerHidden(p)) pmM.hidePlayer(p);
        } else if (tabM.isPlayerHidden(p)) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> tabM.hidePlayerFromAll(p), 2L);
            if (pmM.isPlayerHidden(p)) pmM.hidePlayer(p);
        } else {
            vanishM.hideFromThatPlayer(p);
            Bukkit.getScheduler().runTaskLater(plugin, () -> tabM.hideFromThatPlayer(p), 2L);
        }
    }
}
