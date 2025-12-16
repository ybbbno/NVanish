package me.ybbbno.nvanish.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.deadybbb.ybmj.LegacyTextHandler;
import me.deadybbb.ybmj.PluginProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class ReloadCommand implements BasicCommand {
    private final PluginProvider plugin;

    public ReloadCommand(PluginProvider plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        CommandSender s = source.getSender();

        if (args[0].equals("reload")) {
            plugin.reloadConfig();
            LegacyTextHandler.sendFormattedMessage(s, "<green>Vanish config reloaded.");
            return;
        }

        LegacyTextHandler.sendFormattedMessage(s, "<red>Usage: /vanishsettings reload");
    }

    @Override
    public Collection<String> suggest(final CommandSourceStack commandSourceStack, final String[] args) {
        return List.of("reload");
    }

    @Override
    public boolean canUse(final CommandSender sender) {
        final String permission = this.permission();
        return sender.hasPermission(permission) &&
                sender instanceof Player;
    }

    @Override
    public @Nullable String permission() {
        return "vanish.reload";
    }
}
