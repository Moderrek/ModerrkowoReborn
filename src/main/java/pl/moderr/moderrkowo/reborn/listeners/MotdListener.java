package pl.moderr.moderrkowo.reborn.listeners;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.moderr.moderrkowo.reborn.Main;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;
import pl.moderr.moderrkowo.reborn.utils.HexResolver;

public class MotdListener implements Listener {

    final int dlugoscMotd = 59;

    @EventHandler
    public void ping(PaperServerListPingEvent e) {
        e.setMaxPlayers(e.getNumPlayers() + 1);
        e.setVersion("Moderrkowo 1.16.5");
        String line1 = ColorUtils.color(HexResolver.parseHexString("<gradient:#FD4F1D:#FCE045>Moderrkowo") + " &8[1.16]");
        String line2 = ColorUtils.color(HexResolver.parseHexString(Main.getInstance().getConfig().getString("motd-secound")));
        String line3 = "Moderrkowo [1.16]";
        String line4 = Main.getInstance().getConfig().getString("motd-secound2");
        e.setMotd(centerText(line3).replace(line3, line1) + "\n" + centerText(line4).replace(line4, line2));
    }

    String centerText(String text) {
        StringBuilder builder = new StringBuilder(text);
        char space = ' ';
        int distance = (dlugoscMotd - text.length()) / 2;
        for (int i = 0; i < distance; ++i) {
            builder.insert(0, space);
            builder.append(space);
        }
        return builder.toString();
    }

}
