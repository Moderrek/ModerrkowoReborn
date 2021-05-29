package pl.moderr.moderrkowo.reborn.villagers.data;

import org.bukkit.Material;

public interface IQuestItemBreak extends IQuestItem {
    Material getMaterial();
    int getCount();
    boolean blockSilk();
    @Override
    default String getQuestItemPrefix() {
        return "Zniszcz";
    }
}