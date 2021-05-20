package pl.moderr.moderrkowo.reborn.automessage;

import org.bukkit.Bukkit;
import pl.moderr.moderrkowo.reborn.Main;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;
import pl.moderr.moderrkowo.reborn.utils.RandomUtils;

import java.util.ArrayList;

public class ModerrkowoAutoMessage {

    public ModerrkowoAutoMessage(Main main, int secs, ArrayList<String> messages) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {
            if (messages.size() == 0) {
                return;
            }
            Bukkit.broadcastMessage(ColorUtils.color("&8[!] &e" + messages.get(RandomUtils.getRandomInt(0, messages.size() - 1))));
        }, 0, 20L * secs);
    }

}
