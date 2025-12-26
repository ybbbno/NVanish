package me.ybbbno.nvanish.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.deadybbb.ybmj.LegacyTextHandler;
import me.ybbbno.nvanish.NVanish;
import me.ybbbno.nvanish.PriorityManager;
import me.ybbbno.nvanish.TabHiderManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Collection;

public class TabHideCommand implements BasicCommand {
    private final PriorityManager manager;

    public TabHideCommand(NVanish plugin) { this.manager = plugin.getManager(); }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        Player s = (Player) source.getSender();
        Player p = s;

        if (args.length > 0) {
            p = Bukkit.getPlayer(args[0]);
        }

        if (p == null) {
            LegacyTextHandler.sendFormattedMessage(s, "<red>Игрок не найден!");
            return;
        }

        if (!manager.isPlayerTabHidden(p)) {
            LegacyTextHandler.sendFormattedMessage(s, "<green>Игрок "+p.getName()+" убран из таба!");
        } else {
            LegacyTextHandler.sendFormattedMessage(s, "<red>Игрок "+p.getName()+" добавлен в таб!");
        }

        manager.toggleTabHider(p);
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
