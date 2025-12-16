package me.ybbbno.nvanish;

import com.nickuc.login.api.event.bukkit.auth.AuthenticateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.UUID;

public class NLoginListener {
    @EventHandler
    public void onAuthenticate(AuthenticateEvent event) {
        if (!is_init) return;
        if (!plugin.hasNLoginApi) return;
        Player joined = event.getPlayer();

        if (isPlayerVanished(joined)) {
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (!pl.hasPermission("vanish.see"))
                    plugin.logger.info(pl.getName() + " " + joined.getName());
                pl.hidePlayer(plugin, pl);
            }
        } else {
            for (UUID id : vanished) {
                Player vp = Bukkit.getPlayer(id);
                if (vp != null && !joined.hasPermission("vanish.see"))
                    joined.hidePlayer(plugin, vp);
            }
        }
    }
}
