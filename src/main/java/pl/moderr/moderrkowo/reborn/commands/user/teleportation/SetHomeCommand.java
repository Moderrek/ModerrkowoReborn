package pl.moderr.moderrkowo.reborn.commands.user.teleportation;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.reborn.Main;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;
import pl.moderr.moderrkowo.reborn.utils.Logger;

import java.io.IOException;

public class SetHomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if(Main.getInstance().instanceAntyLogout.inFight(p.getUniqueId())){
                p.sendMessage(ColorUtils.color("&cNie możesz uciec podczas walki"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
                Logger.logAdminLog(p.getName() + " chciał uciec podczas walki [sethome]");
                return false;
            }
            Main.getInstance().dataConfig.set("homes." + p.getUniqueId(), p.getLocation());
            try {
                Main.getInstance().dataConfig.save(Main.getInstance().dataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            p.sendMessage(ColorUtils.color("&8[!] &aUstawiono nowe miejsce domu"));
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
            //Logger.logAdminLog(p.getName() + " ustawił dom");
        }
        return false;
    }
}