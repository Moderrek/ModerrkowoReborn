package pl.moderr.moderrkowo.reborn.commands.admin;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.reborn.Main;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;

import java.io.IOException;

public class SetSpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Main.getInstance().dataConfig.set("spawn", p.getLocation());
            try {
                Main.getInstance().dataConfig.save(Main.getInstance().dataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            p.sendTitle(" ", ColorUtils.color("&aPomyślnie ustawiono spawna"));
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }
        return false;
    }
}
