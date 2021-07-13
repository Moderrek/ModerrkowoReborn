package pl.moderr.moderrkowo.core.commands.user.messages;

import com.destroystokyo.paper.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

public class HelpopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length > 0) {
                Logger.logHelpMessage(p, Logger.getMessage(args, 0, true));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                p.sendMessage(ColorUtils.color("&8[!] &aPomyślnie wysłano!"));
                p.sendMessage(ColorUtils.color("&8[&9Pomoc&8] &7" + p.getName() + "&8: &e" + Logger.getMessage(args, 0, true)));
                p.sendTitle(new Title(ColorUtils.color("&6&lModerrkowo"), ColorUtils.color("&aWysłano wiadomość.")));
                int i = (int) Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).count();
                Main.getInstance().discordManager.sendHelpop(p, Logger.getMessage(args, 0, true), i != 0);
                return true;
            } else {
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                p.sendMessage(ColorUtils.color("&cUżycie: &e/helpop <wiadomość>"));
                return false;
            }
        } else {
            sender.sendMessage("Nie jesteś graczem!");
            return false;
        }
    }
}
