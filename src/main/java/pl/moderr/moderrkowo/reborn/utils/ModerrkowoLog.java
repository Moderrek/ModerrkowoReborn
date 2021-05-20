package pl.moderr.moderrkowo.reborn.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ModerrkowoLog {

    public static void LogAdmin(String text) {
        String output = ColorUtils.color(HexResolver.parseHexString("<gradient:#F7C13C:#D4E443>Log") + " &7" + text);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(output);
            }
        }
        System.out.println(output);
    }

}
