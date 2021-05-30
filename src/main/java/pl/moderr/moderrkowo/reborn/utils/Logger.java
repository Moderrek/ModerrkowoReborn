package pl.moderr.moderrkowo.reborn.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import pl.moderr.moderrkowo.reborn.Main;

import java.util.logging.Level;

public class Logger {

    public static boolean logHelpMessage(Player sender, String message) {
        boolean success = false;
        int numberOfAdmins = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                TextComponent tc = new TextComponent();
                tc.setText(ColorUtils.color(String.format("&8[&9Pomoc&8] &7%s&8: &e%s", sender.getName(), message)));
                tc.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ahelpop " + sender.getName() + " "));
                tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ColorUtils.color(String.format("&eOdpowiedz &a%s", sender.getName())))));
                p.spigot().sendMessage(tc);
                numberOfAdmins++;
            }
        }
        if (numberOfAdmins != 0) {
            success = true;
        }
        return success;
    }

    public static void logWorldManager(String worldName, String message) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(ColorUtils.color("&8[&9WM&8] &e" + worldName + " &8>> &e" + message));
            }
        }
    }

    public static void logAdminChat(String message) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(ColorUtils.color("&8[&cAC&8] &f" + message));
            }
        }
    }

    public static void logAdminLog(String message) {
        ModerrkowoLog.LogAdmin(message);
    }

    public static void logPluginMessage(String message) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(ColorUtils.color("&9PL &6» &7" + message));
            }
        }
    }

    public static void logCaseMessage(String message) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(ColorUtils.color("&e&lCASE &6» &7" + message));
            }
        }
    }

    public static void logDatabaseMessage(String message) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(ColorUtils.color("&9DB &6» &7" + message));
            }
        }
        Main.getInstance().getLogger().log(Level.SEVERE, ColorUtils.color("&9DB &6» &7" + message));
    }

    public static void logDiscordMessage(String message) {
        String prefix = "";
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(ColorUtils.color(prefix + "&9DC &6» &8" + message));
            }
        }
    }

    public static String getMessage(String[] args, int startFromArg, boolean removeFirstSpace) {
        StringBuilder out = new StringBuilder();
        for (int i = startFromArg; i != args.length; i++) {
            out.append(" ").append(args[i]);
        }
        if (removeFirstSpace) {
            return out.substring(1);
        }
        return out.toString();
    }

}
