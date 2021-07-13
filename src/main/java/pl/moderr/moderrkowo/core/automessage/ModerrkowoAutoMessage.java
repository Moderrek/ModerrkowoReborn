package pl.moderr.moderrkowo.core.automessage;

import org.bukkit.Bukkit;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.RandomUtils;

import java.util.ArrayList;

public class ModerrkowoAutoMessage {

    public ModerrkowoAutoMessage(Main main, int secs, ArrayList<String> messages) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {
            if (messages.size() == 0) {
                return;
            }
            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage(ColorUtils.color("&7[&6&lM&7] &f" + messages.get(RandomUtils.getRandomInt(0, messages.size() - 1))));
            Bukkit.broadcastMessage(" ");
        }, 0, 20L * secs);
    }

}
