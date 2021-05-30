package pl.moderr.moderrkowo.reborn.events;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import pl.moderr.moderrkowo.reborn.Main;
import pl.moderr.moderrkowo.reborn.events.drop.DropEvent;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;

import java.util.ArrayList;

public class EventManager {

    private BossBar eventBossBar = null;

    public ArrayList<ModerrEvent> events = new ArrayList<ModerrEvent>(){
        {
            add(new DropEvent());
        }
    };

    public EventManager(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            if(Bukkit.getOnlinePlayers().size() >= 5){
                Bukkit.broadcastMessage(ColorUtils.color("  "));
                Bukkit.broadcastMessage(ColorUtils.color("  &eRopoczęto wydarzenie &c" + "brak"));
                Bukkit.broadcastMessage("  ");
            }
        }, 0,144000L);
    }
}
