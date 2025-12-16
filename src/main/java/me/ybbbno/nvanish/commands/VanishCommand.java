package me.ybbbno.nvanish.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.ybbbno.nvanish.VanishManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

public class VanishCommand implements BasicCommand {
    private final VanishManager manager;

    public VanishCommand(VanishManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        Player s = (Player) source.getSender();

        manager.toggleVanish(s);
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
