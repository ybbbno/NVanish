package me.ybbbno.nvanish;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.deadybbb.ybmj.LegacyTextHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class NVanishCommand implements BasicCommand {
    private final NVanish plugin;

    public NVanishCommand(NVanish plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        CommandSender s = source.getSender();

        if (args[0].equals("reload")) {
            plugin.reloadConfig();
            LegacyTextHandler.sendFormattedMessage(s, "<green>NVanish config reloaded.");
            return;
        } else if (args[0].equals("status")) {
            Player p = null;
            if (args.length > 1) p = Bukkit.getPlayer(args[1]);
            if (p == null) p = (Player) s;

            boolean vanish = plugin.getManager().isPlayerVanished(p);
            boolean tab = plugin.getManager().isPlayerTabHidden(p);
            LegacyTextHandler.sendFormattedMessage(s, "Статус игрока "+p.getName()+": \n"+"Ваниш: "+vanish+"\n"+"Таб: "+tab);
            return;
        }

        LegacyTextHandler.sendFormattedMessage(s, "<red>Usage: /nvanish reload/status");
    }

    @Override
    public Collection<String> suggest(final CommandSourceStack commandSourceStack, final String[] args) {
        if (args.length != 0 && args[0].equals("status")) {
            if (args.length == 1 || args[1].isEmpty()) {
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
            }
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase()
                            .startsWith(args[args.length - 1]
                                    .toLowerCase())).toList();
        }
        return List.of("reload", "status");
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
