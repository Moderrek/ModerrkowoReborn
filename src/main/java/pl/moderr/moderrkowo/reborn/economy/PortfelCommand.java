package pl.moderr.moderrkowo.reborn.economy;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.reborn.mysql.UserManager;
import pl.moderr.moderrkowo.reborn.utils.ChatUtil;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;

public class PortfelCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.sendMessage(ColorUtils.color("&8[!] &7Stan konta wynosi: &a" + ChatUtil.getMoney(UserManager.getUser(p.getUniqueId()).getMoney())));
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
        } else {
            sender.sendMessage(ColorUtils.color("&cNie jestes graczem"));
        }
        return false;
    }
}
