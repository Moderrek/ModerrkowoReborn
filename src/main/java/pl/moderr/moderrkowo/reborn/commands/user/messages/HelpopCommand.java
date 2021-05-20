package pl.moderr.moderrkowo.reborn.commands.user.messages;

import com.destroystokyo.paper.Title;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;
import pl.moderr.moderrkowo.reborn.utils.Logger;

public class HelpopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length > 0) {
                boolean success = Logger.logHelpMessage(p, Logger.getMessage(args, 0, true));
                if (success) {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                    p.sendMessage(ColorUtils.color("&aPomyślnie wysłano!"));
                    p.sendTitle(new Title(ColorUtils.color("&6&lModerrkowo"), ColorUtils.color("&aWysłano wiadomość.")));
                } else {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    p.sendTitle(new Title(ColorUtils.color("&6&lModerrkowo"), ColorUtils.color("&cNie wysłano wiadomości.")));
                    p.sendMessage(ColorUtils.color("&cBrak aktywnych moderatorów."));
                }
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
