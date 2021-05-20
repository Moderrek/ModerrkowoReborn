package pl.moderr.moderrkowo.reborn.commands.admin;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;
import pl.moderr.moderrkowo.reborn.utils.Logger;
import pl.moderr.moderrkowo.reborn.utils.ModerrkowoLog;

public class NazwaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length > 0) {
                String itemName = Logger.getMessage(args, 0, true);
                if (p.getInventory().getItemInMainHand().getType() != Material.AIR) {
                    ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
                    meta.setDisplayName(ColorUtils.color(itemName));
                    p.getInventory().getItemInMainHand().setItemMeta(meta);
                    p.sendMessage(ColorUtils.color("&aPomyślnie zmieniono nazwę przedmiotu"));
                    ModerrkowoLog.LogAdmin(ColorUtils.color("&6" + p.getName() + " &7zmienił nazwę przedmiotu &8(&6" + ColorUtils.color(itemName) + "&8)"));
                } else {
                    p.sendMessage(ColorUtils.color("&cAby ustawić nazwę przedmiotu musisz go trzymać w ręku!"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return false;
                }
            } else {
                p.sendMessage(ColorUtils.color("&cPodaj jaką nazwę przedmiotu ustawić!"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
        }
        return false;
    }
}
