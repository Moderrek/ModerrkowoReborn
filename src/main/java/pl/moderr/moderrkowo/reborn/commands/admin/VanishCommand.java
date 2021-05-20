package pl.moderr.moderrkowo.reborn.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.reborn.Main;
import pl.moderr.moderrkowo.reborn.listeners.JoinQuitListener;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;
import pl.moderr.moderrkowo.reborn.utils.ModerrkowoLog;

import java.util.ArrayList;
import java.util.UUID;

public class VanishCommand implements CommandExecutor {


    public final static BossBar bossBar = Bukkit.createBossBar(ColorUtils.color("&9>>> JESTEŚ UKRYTY <<<"), BarColor.BLUE, BarStyle.SOLID);

    public final static ArrayList<UUID> hidden = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (hidden.contains(p.getUniqueId())) {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (players.isOp()) {
                        continue;
                    }
                    players.showPlayer(Main.getInstance(), p);
                }
                hidden.remove(p.getUniqueId());
                p.sendMessage(ColorUtils.color("&cZostałeś odkryty"));
                Bukkit.broadcastMessage(JoinQuitListener.getJoinMessage(p));
                ModerrkowoLog.LogAdmin(p.getName() + " został odkryty");
                bossBar.removePlayer(p);
            } else {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (players.isOp()) {
                        continue;
                    }
                    players.hidePlayer(Main.getInstance(), p);
                }
                hidden.add(p.getUniqueId());
                p.sendMessage(ColorUtils.color("&aZostałeś ukryty"));
                Bukkit.broadcastMessage(JoinQuitListener.getQuitMessage(p));
                ModerrkowoLog.LogAdmin(p.getName() + " został ukryty");
                bossBar.addPlayer(p);
            }
        }
        return false;
    }
}
