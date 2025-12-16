package me.ybbbno.nvanish.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.ybbbno.nvanish.VanishManager;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

public class VanishToggleCommand implements BasicCommand {
    private final VanishManager manager;

    public VanishToggleCommand(VanishManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        Player p = (Player) source.getExecutor();
        manager.toggleVanish(p);
    }

    @Override
    public @Nullable String permission() {
        return "vanish.use";
    }
}
