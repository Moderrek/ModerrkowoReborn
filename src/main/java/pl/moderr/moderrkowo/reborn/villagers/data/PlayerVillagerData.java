package pl.moderr.moderrkowo.reborn.villagers.data;

import java.util.HashMap;

public class PlayerVillagerData {

    private String villagerId;
    private int questIndex;
    private boolean activeQuest;
    private HashMap<String, Integer> QuestItemData;

    public PlayerVillagerData(String villagerId, int questIndex, boolean activeQuest, HashMap<String, Integer> questItemData) {
        this.villagerId = villagerId;
        this.questIndex = questIndex;
        this.activeQuest = activeQuest;
        this.QuestItemData = questItemData;
    }

    public String getVillagerId() {
        return villagerId;
    }

    public void setVillagerId(String villagerId) {
        this.villagerId = villagerId;
    }

    public boolean isActiveQuest() {
        return activeQuest;
    }

    public void setActiveQuest(boolean activeQuest) {
        this.activeQuest = activeQuest;
    }

    public int getQuestIndex() {
        return questIndex;
    }

    public void setQuestIndex(int questIndex) {
        this.questIndex = questIndex;
    }

    public HashMap<String, Integer> getQuestItemData() {
        return QuestItemData;
    }

    public void setQuestItemData(HashMap<String, Integer> questItemData) {
        QuestItemData = questItemData;
    }
}
