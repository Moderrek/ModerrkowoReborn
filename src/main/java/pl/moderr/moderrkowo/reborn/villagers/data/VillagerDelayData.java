package pl.moderr.moderrkowo.reborn.villagers.data;

import java.time.Instant;
import java.util.IdentityHashMap;
import java.util.Map;

public class VillagerDelayData {

    private Map<String, Instant> timers = new IdentityHashMap<>();

    public VillagerDelayData(IdentityHashMap<String, Instant> timers){
        this.timers = timers;
    }

    public Map<String, Instant> getTimers() {
        return timers;
    }

    public void setTimers(Map<String, Instant> timers) {
        this.timers = timers;
    }
}
