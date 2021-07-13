package pl.moderr.moderrkowo.core.commands.user.information;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

public class DiscordCommand implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(ColorUtils.color("&9&lDiscord &7https://discord.gg/mgPAGYNu2s"));
        return false;
    }
}
