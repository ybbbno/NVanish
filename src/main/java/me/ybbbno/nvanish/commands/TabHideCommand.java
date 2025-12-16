package me.ybbbno.nvanish.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.deadybbb.ybmj.LegacyTextHandler;
import me.ybbbno.nvanish.HiderManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Collection;

public class TabHideCommand implements BasicCommand {
    private final HiderManager manager;

    public TabHideCommand(HiderManager manager) { this.manager = manager; }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        Player s;

        if (args.length == 0) {
            s = (Player) source.getSender();
        } else {
            s = Bukkit.getPlayer(args[0]);
        }

        if (s == null) {
            LegacyTextHandler.sendFormattedMessage(source.getSender(), "<red>Игрок не был найден!");
        }

        manager.toggle(s);
    }

    @Override
    public Collection<String> suggest(final CommandSourceStack source, final String[] args) {
        if (args.length == 0) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        }
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase()
                .startsWith(args[args.length - 1]
                .toLowerCase())).toList();
    }

    @Override
    public boolean canUse(final CommandSender sender) {
        final String permission = this.permission();
        return sender.hasPermission(permission) &&
                sender instanceof Player;
    }

    @Override
    public @Nullable String permission() { return "tab_hider.use"; }
}
