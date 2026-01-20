package me.ybbbno.nvanish.voicechat;

import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.VolumeCategory;
import me.deadybbb.ybmj.PluginProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class VoicechatManager implements VoicechatPlugin, Listener {
    private VoicechatServerApi api;
    private final PluginProvider plugin;

    public VoicechatManager(PluginProvider plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getPluginId() {
        return "NVanish";
    }

    @Override
    public void initialize(VoicechatApi api) {
        this.api = (VoicechatServerApi) api;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (VolumeCategory volumeCategory : api.getVolumeCategories()) {
            plugin.logger.info(volumeCategory.getName());
        }
    }
}
