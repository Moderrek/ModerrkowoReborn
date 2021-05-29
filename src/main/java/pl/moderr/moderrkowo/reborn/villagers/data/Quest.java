package pl.moderr.moderrkowo.reborn.villagers.data;

import pl.moderr.moderrkowo.reborn.utils.ColorUtils;

import java.util.ArrayList;

public class Quest {

    private final String name;
    private final String description;
    private final QuestDifficulty difficulty;
    private final ArrayList<IQuestItem> questItems;
    private final ArrayList<IQuestReward> rewardItems;

    public Quest(String name, String description, QuestDifficulty difficulty, ArrayList<IQuestItem> questItems, ArrayList<IQuestReward> rewardItems) {
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.questItems = questItems;
        this.rewardItems = rewardItems;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<IQuestItem> getQuestItems() {
        return questItems;
    }

    public ArrayList<IQuestReward> getRewardItems() {
        return rewardItems;
    }
    public String getDifficulty(){
        switch (difficulty){
            case EASY:
                return ColorUtils.color("&a&lŁATWY");
            case NORMAL:
                return ColorUtils.color("&9&lNORMALNY");
            case HARD:
                return ColorUtils.color("&e&lCIĘŻKI");
            case TRYHARD:
                return ColorUtils.color("&c&lBARDZO CIĘŻKI");
        }
        return ColorUtils.color("&8Nieznany");
    }

}
