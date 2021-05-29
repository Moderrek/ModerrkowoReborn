package pl.moderr.moderrkowo.reborn.villagers.data;

import org.bukkit.Material;

public interface IQuestItemCollect extends IQuestItem {
    Material getMaterial();
    int getCount();
    @Override
    default String getQuestItemPrefix() {
        return "Zbierz";
    }
}
