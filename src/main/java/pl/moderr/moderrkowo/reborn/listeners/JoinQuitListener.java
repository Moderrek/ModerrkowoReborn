package pl.moderr.moderrkowo.reborn.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.moderr.moderrkowo.reborn.Main;
import pl.moderr.moderrkowo.reborn.commands.admin.VanishCommand;
import pl.moderr.moderrkowo.reborn.mysql.UserManager;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;
import pl.moderr.moderrkowo.reborn.utils.HexResolver;
import pl.moderr.moderrkowo.reborn.utils.ModerrkowoLog;

import java.util.UUID;

public class JoinQuitListener implements Listener {

    public static String getJoinMessage(Player p) {
        return ColorUtils.color("&e" + p.getName() + " &7dołączył");
    }

    public static String getQuitMessage(Player p) {
        return ColorUtils.color("&e" + p.getName() + " &7opuścił serwer");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.sendTitle(ColorUtils.color("&9Witaj,"), ColorUtils.color("&9") + p.getName());
        p.setPlayerListName(ColorUtils.color("&9" + p.getName()));
        if (p.isOp()) {
            p.setPlayerListName(ColorUtils.color("&c&lADM &r&9" + p.getName()));
        }
        p.sendMessage(ColorUtils.color("&6⚔ " + HexResolver.parseHexString("<gradient:#FD4F1D:#FCE045>Moderrkowo") + " &r&6⚔"));
        p.sendMessage(ColorUtils.color("&6» &7Witaj, &6" + p.getName() + " &7na &6MODERRKOWO!"));
        p.sendMessage(ColorUtils.color("&6» &7Discord serwera gdzie znajdują sie wszystkie informacje &c/discord"));
        p.sendMessage(ColorUtils.color("&6» &7Granie na serwerze oznacza akceptację regulaminu &c/regulamin"));
        p.sendMessage(ColorUtils.color("&6» &cModerrkowo &7to gwarancja satysfakcji zabawy i bezpieczeństwa!"));
        // Update highest value of players
        int maxPlayer = Main.getInstance().dataConfig.getInt("MaxPlayer");
        if (maxPlayer < Bukkit.getOnlinePlayers().size()) {
            Main.getInstance().dataConfig.set("MaxPlayer", Bukkit.getOnlinePlayers().size());
            Bukkit.broadcastMessage(ColorUtils.color("&8[!] &6Rekord graczy został pobity!"));
        }
        // TAB
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateTab(player);
        }
        // Vanish
        for (UUID uuid : VanishCommand.hidden) {
            Player hidden = Bukkit.getPlayer(uuid);
            if (p.isOp()) {
                assert hidden != null;
                p.sendMessage(ColorUtils.color("&7" + hidden.getName() + " jest ukryty ale ty masz permisje aby go widzieć"));
                continue;
            }
            assert hidden != null;
            p.hidePlayer(Main.getInstance(), hidden);
        }
        // Load User
        UserManager.loadUser(p);
        // Message
        e.setJoinMessage(JoinQuitListener.getJoinMessage(p));
    }

    public void updateTab(Player e) {
        int administracja = 0;
        int gracze = 0;
        for (Player admin : Bukkit.getOnlinePlayers()) {
            if (VanishCommand.hidden.contains(admin.getUniqueId())) {
                continue;
            }
            gracze++;
            if (admin.hasPermission("moderr.admin")) {
                administracja++;
            }
        }
        String header
                = " \n "
                + HexResolver.parseHexString("&6⚔ &8[ <gradient:#FD4F1D:#FCE045>Moderrkowo") + " &r&8] &6⚔&r"
                + " \n "
                + " \n&7Administracja online  &8» &6" + administracja
                + " \n&7Gracze online &8» &6" + gracze
                + " \n&7Rekord graczy &8» &6" + Main.getInstance().dataConfig.getInt("MaxPlayer")
                + " \n ";
        String footer
                = " \n&7Adres serwera: &amoderrkowo.pl"
                + " \n&7Discord: &a/discord"
                + " \n&7Strona: &cbrak"
                + " \n ";
        e.setPlayerListHeader(ColorUtils.color(header));
        e.setPlayerListFooter(ColorUtils.color(footer));

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        UserManager.unloadUser(e.getPlayer().getUniqueId());
        // Update Tab
        for (Player players : Bukkit.getOnlinePlayers()) {
            updateTab(players);
        }
        // Vanish
        if (VanishCommand.hidden.contains(e.getPlayer().getUniqueId())) {
            VanishCommand.hidden.remove(e.getPlayer().getUniqueId());
            ModerrkowoLog.LogAdmin("Gracz " + e.getPlayer().getName() + " wyszedł z serwera i został pokazany");
        }
        // Message
        e.setQuitMessage(JoinQuitListener.getQuitMessage(e.getPlayer()));
    }

}
