package pl.moderr.moderrkowo.core.commands.user.teleportation;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

import java.util.Objects;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if(Main.getInstance().instanceAntyLogout.inFight(p.getUniqueId())){
                p.sendMessage(ColorUtils.color("&cNie możesz uciec podczas walki"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
                Logger.logAdminLog(p.getName() + " chciał uciec podczas walki [spawn]");
                return false;
            }
            //p.teleport(Main.getInstance().dataConfig.getLocation("spawn"));
            p.teleport(Objects.requireNonNull(Bukkit.getWorld("spawn")).getSpawnLocation());
            p.sendTitle(ColorUtils.color("&6Wioska"), ColorUtils.color("&eModerrkowo"));
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            //Logger.logAdminLog(p.getName() + " przeteleportował się na spawna");
        }
        return false;
    }
}
