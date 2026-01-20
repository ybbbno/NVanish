package me.ybbbno.nvanish.pm;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.deadybbb.ybmj.LegacyTextHandler;
import me.deadybbb.ybmj.YBMJ;
import me.ybbbno.nvanish.NVanish;
import me.ybbbno.nvanish.PriorityManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PMCommand implements BasicCommand {
    private final NVanish plugin;
    private final PriorityManager manager;
    private final PMTranslator translator;

    public PMCommand(NVanish plugin) {
        this.plugin = plugin;
        this.manager = plugin.getManager();
        this.translator = new PMTranslator(plugin);
        GlobalTranslator.translator().addSource(translator);
    }

    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        // You whisper to ybbbno: 123
        // ybbbno whispers to you: 123
        // Вы прошептали ybbbno: 123
        // ybbbno шепчет вам: 123

        Player s = (Player) commandSourceStack.getSender();
        Player p = Bukkit.getPlayer(args[0]);

        if (p == null || manager.isPlayerPMHidden(p)) {
            TranslatableComponent unknown = Component.translatable(PMTranslationKeys.UNKNOWN_PLAYER).color(NamedTextColor.RED);
            LegacyTextHandler.sendFormattedMessage(s, translator.translate(unknown, s.locale()));
            return;
        }

        Component ptext = Component.text("");

        if (args.length > 1) {
            String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            ptext = Component.text(message);
        }

        Component sname = Component.text(s.getName());
        Component pname = Component.text(p.getName());
        TranslatableComponent sm = Component.translatable(PMTranslationKeys.WHISPERS_TO).arguments(pname, ptext).color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC);
        TranslatableComponent pm = Component.translatable(PMTranslationKeys.WHISPERS_FROM).arguments(sname, ptext).color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC);

//        plugin.logger.info(manager.translate(sm, s.locale()).toString());
//        plugin.logger.info(manager.translate(pm, p.locale()).toString());
        LegacyTextHandler.sendFormattedMessage(s, translator.translate(sm, s.locale()));
        LegacyTextHandler.sendFormattedMessage(p, translator.translate(pm, p.locale()));
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
