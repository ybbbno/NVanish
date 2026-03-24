package me.ybbbno.nvanish;

import com.nickuc.login.api.event.bukkit.auth.AuthenticateEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NLoginManager implements Listener {
    private final PriorityManager manager;
    private final boolean hasNLoginApi;

    public NLoginManager(PriorityManager manager, boolean hasNLoginAPI) {
        this.manager = manager;
        this.hasNLoginApi = hasNLoginAPI;
    }

    @EventHandler
    public void onAuthenticate(AuthenticateEvent event) {
        if (!hasNLoginApi) return;
        Player joined = event.getPlayer();

        manager.reloadState(joined);
    }
}
