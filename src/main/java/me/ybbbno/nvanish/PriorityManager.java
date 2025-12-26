package me.ybbbno.nvanish;

import com.nickuc.login.api.event.bukkit.auth.AuthenticateEvent;
import me.deadybbb.ybmj.BasicManagerHandler;
import me.deadybbb.ybmj.PluginProvider;
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

    private boolean vSet = false;
    private boolean tSet = false;

    public PriorityManager(PluginProvider plugin, boolean hasNLoginAPI) {
        super(plugin);
        this.hasNLoginApi = hasNLoginAPI;
        this.vanishM = new VanishManager(plugin);
        this.tabM = new TabHiderManager(plugin);
    }

    @Override
    protected void onInit() {
        vanishM.init();
        tabM.init();

        vSet = vanishM.is_init_set();
        tSet = tabM.is_init_set();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (vSet && vanishM.isPlayerVanished(p)) {
                vanishM.hidePlayerFromAll(p);
            } else if (tSet && tabM.isPlayerHidden(p)) {
                tabM.hidePlayerFromAll(p);
            }
        }
    }

    @Override
    protected void onDeinit() {
        vanishM.deinit();
        tabM.deinit();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (vanishM.isPlayerVanished(p)) {
                vanishM.showPlayerToAll(p);
            } else if (tabM.isPlayerHidden(p)) {
                tabM.showPlayerToAll(p);
            }
        }

        vSet = vanishM.is_init_set();
        tSet = tabM.is_init_set();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (hasNLoginApi) return;
        Player joined = event.getPlayer();

        if (vSet && vanishM.isPlayerVanished(joined)) {
            vanishM.hidePlayerFromAll(joined);
        } else if (tSet && tabM.isPlayerHidden(joined)) {
            tabM.hidePlayerFromAll(joined);
        } else {
            if (vSet) {
                vanishM.hideFromThatPlayer(joined);
            }

            if (tSet) {
                tabM.hideFromThatPlayer(joined);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player quited = event.getPlayer();

        if (vSet && vanishM.isPlayerVanished(quited)) {
            event.quitMessage(null);
        } else if (tSet && tabM.isPlayerHidden(quited)) {
            event.quitMessage(null);
        }
    }

    @EventHandler
    public void onAuthenticate(AuthenticateEvent event) {
        if (!hasNLoginApi) return;
        Player joined = event.getPlayer();

        if (vSet && vanishM.isPlayerVanished(joined)) {
            vanishM.hidePlayerFromAll(joined);
        } else if (tSet && tabM.isPlayerHidden(joined)) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                tabM.hidePlayerFromAll(joined);
            }, 2L);
        } else {
            if (vSet) {
                vanishM.hideFromThatPlayer(joined);
            }

            if (tSet) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    tabM.hideFromThatPlayer(joined);
                }, 2L);
            }
        }
    }

    public void toggleVanish(Player p) {
        if (!vSet) return;

        if (tSet && tabM.isPlayerHidden(p)) {
            tabM.toggle(p);
        }

        vanishM.toggle(p);
    }

    public void toggleTabHider(Player p) {
        if (!tSet) return;

        if (vSet && vanishM.isPlayerVanished(p)) {
            vanishM.toggle(p);
        }

        tabM.toggle(p);
    }

    public boolean isPlayerVanished(Player p) {
        return vanishM.isPlayerVanished(p);
    }

    public boolean isPlayerTabHidden(Player p) {
        return tabM.isPlayerHidden(p);
    }
}
