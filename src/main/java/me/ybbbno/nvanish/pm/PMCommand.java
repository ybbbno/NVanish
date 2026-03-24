package me.ybbbno.nvanish.pm;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.deadybbb.ybmj.LegacyTextHandler;
import me.ybbbno.nvanish.NVanish;
import me.ybbbno.nvanish.NVanishTranslationKeys;
import me.ybbbno.nvanish.PriorityManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PMCommand implements BasicCommand {
    private final PriorityManager manager;

    public PMCommand(NVanish plugin) {
        this.manager = plugin.getManager();
    }

    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        // You whisper to ybbbno: 123
        // ybbbno whispers to you: 123
        // Вы прошептали ybbbno: 123
        // ybbbno шепчет вам: 123
        Player s = (Player) commandSourceStack.getSender();

        if (args.length == 0) {
            LegacyTextHandler.sendFormattedMessage(s, Component.translatable(NVanishTranslationKeys.COMMAND_FAILED, "Unknown command or insufficient permissions").color(NamedTextColor.RED));
            return;
        }

        Player p = Bukkit.getPlayer(args[0]);

        if (p == null || manager.isPlayerPMHidden(p)) {
            LegacyTextHandler.sendFormattedMessage(s, Component.translatable(NVanishTranslationKeys.UNKNOWN_PLAYER, "No player was found").color(NamedTextColor.RED));
            return;
        }

        Component ptext = Component.text("");

        if (args.length > 1) {
            String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            ptext = Component.text(message);
        }

        Component sname = Component.text(s.getName());
        Component pname = Component.text(p.getName());
        TranslatableComponent sm = Component.translatable(NVanishTranslationKeys.WHISPERS_TO, "You whisper to %s: %s").arguments(pname, ptext).color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC);
        TranslatableComponent pm = Component.translatable(NVanishTranslationKeys.WHISPERS_FROM, "%s whispers to you: %s").arguments(sname, ptext).color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC);

//        plugin.logger.info(manager.translate(sm, s.locale()).toString());
//        plugin.logger.info(manager.translate(pm, p.locale()).toString());
        LegacyTextHandler.sendFormattedMessage(s, sm);
        LegacyTextHandler.sendFormattedMessage(p, pm);
    }

    @Override
    public Collection<String> suggest(final CommandSourceStack source, final String[] args) {
        return switch (args.length) {
            case 0 -> Bukkit.getOnlinePlayers().stream()
                    .filter(p -> !manager.isPlayerPMHidden(p))
                    .map(Player::getName)
                    .toList();
            case 1 -> Bukkit.getOnlinePlayers().stream()
                    .filter(p -> !manager.isPlayerPMHidden(p))
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase()
                            .startsWith(args[args.length - 1]
                                    .toLowerCase()))
                    .toList();
            default -> List.of("<message>");
        };
    }
}
