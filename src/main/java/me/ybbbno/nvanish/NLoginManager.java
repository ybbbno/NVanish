package me.ybbbno.nvanish;

import com.nickuc.login.api.event.bukkit.auth.AuthenticateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NLoginManager implements Listener {
    private final NVanish plugin;
    private final VanishManager vanishM;
    private final HiderManager tabM;

    public NLoginManager(NVanish plugin, VanishManager vanishM, HiderManager tabM) {
        this.plugin = plugin;
        this.vanishM = vanishM;
        this.tabM = tabM;
    }

    @EventHandler
    public void onAuthenticate(AuthenticateEvent event) {
        if (!vanishM.is_init_set() || !tabM.is_init_set()) return;
        if (!plugin.hasNLoginApi) return;
        Player joined = event.getPlayer();

        if (vanishM.isPlayerVanished(joined)) {
//            plugin.logger.info("Authorized vanished player: " + joined.getName());
            vanishM.hidePlayer(joined);
        } else {
//            plugin.logger.info("Hide vanished players to that authorized player: " + joined.getName());
            vanishM.hideFromThatPlayer(joined);
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (tabM.isPlayerHided(joined)) {
                tabM.hidePlayerFromAll(joined);
            } else {
                tabM.hideFromThatPlayer(joined);
            }
        }, 2L);
    }
}
