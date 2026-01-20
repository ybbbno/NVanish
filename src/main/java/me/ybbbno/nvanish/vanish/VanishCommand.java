package me.ybbbno.nvanish.vanish;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.deadybbb.ybmj.LegacyTextHandler;
import me.ybbbno.nvanish.NVanish;
import me.ybbbno.nvanish.PriorityManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

import java.util.Collection;

public class VanishCommand implements BasicCommand {
    private final PriorityManager manager;

    public VanishCommand(NVanish plugin) {
        this.manager = plugin.getManager();
    }

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

        if (!manager.isPlayerVanished(p)) {
            LegacyTextHandler.sendFormattedMessage(s, "<green>Игрок "+p.getName()+" в ванише!");
        } else {
            LegacyTextHandler.sendFormattedMessage(s, "<red>Игрок "+p.getName()+" не в ванише!");
        }

        manager.toggleVanish(p);
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
    public @Nullable String permission() {
        return "vanish.use";
    }
}
