package pl.moderr.moderrkowo.reborn.villagers.data;

import com.google.gson.Gson;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;

public class PlayerVillagersData {

    private HashMap<String, PlayerVillagerData> villagersData;

    @Contract(pure = true)
    public PlayerVillagersData() {
        this.villagersData = new HashMap<>();
    }

    @Contract(pure = true)
    public PlayerVillagersData(HashMap<String, PlayerVillagerData> villagersData) {
        this.villagersData = villagersData;
    }

    public PlayerVillagersData(String json) {
        Gson gson = new Gson();
        villagersData = gson.fromJson(json, PlayerVillagersData.class).getVillagersData();
    }

    public HashMap<String, PlayerVillagerData> getVillagersData() {
        return villagersData;
    }

    public void setVillagersData(HashMap<String, PlayerVillagerData> villagersData) {
        this.villagersData = villagersData;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String ToJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
