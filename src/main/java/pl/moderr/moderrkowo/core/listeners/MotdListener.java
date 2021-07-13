package pl.moderr.moderrkowo.core.listeners;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.HexResolver;

import java.time.LocalDateTime;
import java.time.Month;

public class MotdListener implements Listener {

    public static final LocalDateTime localDate = LocalDateTime.of(2021, Month.JUNE, 25, 19, 0);
    final int lenghtMotd = 59;

    @EventHandler
    public void preLogin(AsyncPlayerPreLoginEvent e) {
        if (e.getName().equals("MODERR")) {
            return;
        }
        if (LocalDateTime.now().isBefore(localDate)) {
            e.setKickMessage(ColorUtils.color("&6&lModerrkowo\n&cPoczekaj na start sezonu II!\n&fza " + ChatUtil.getTime(localDate) + "\n\n&7Więcej informacji\n&9Discord: &fhttps://bit.ly/moderrkowo"));
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            e.setResult(PlayerPreLoginEvent.Result.KICK_OTHER);
            Main.getInstance().discordManager.sendTryJoin(e.getName());
        }
    }

    @EventHandler
    public void ping(PaperServerListPingEvent e) {
        //String line1 = ColorUtils.color(HexResolver.parseHexString("<gradient:#FD4F1D:#FCE045>Moderrkowo") + " &fSezon II");
        //
        String line1 = ColorUtils.color("&e⚔ &c&k::&6Moderrkowo.PL&c&k:: &8| &fSezon II &7[&f1.16.5&7] &e⚔");
        if (LocalDateTime.now().isAfter(localDate)) {
            String line2 = ColorUtils.color(HexResolver.parseHexString(Main.getInstance().getConfig().getString("motd-secound")));
            String line3 = "⚔ ::Moderrkowo.PL:: | Sezon II [1.16.5] ⚔";
            String line4 = Main.getInstance().getConfig().getString("motd-secound2");
            e.setMotd(centerText(line3).replace(line3, line1) + "\n" + centerText("Zadania - Działki - Ekonomia - Dodatki").replace("Zadania - Działki - Ekonomia - Dodatki", ColorUtils.color("&aZadania &9- &6Działki &9- &eEkonomia &9- &cDodatki")));
        } else {
            String line3 = "Moderrkowo Sezon II";
            e.setMotd(centerText(line3).replace(line3, line1) + "\n" + centerText("za " + ChatUtil.getTime(localDate)));
        }
    }

    String centerText(String text) {
        StringBuilder builder = new StringBuilder(text);
        char space = ' ';
        int distance = (lenghtMotd - text.length()) / 2;
        for (int i = 0; i < distance; ++i) {
            builder.insert(0, space);
            builder.append(space);
        }
        return builder.toString();
    }

}
