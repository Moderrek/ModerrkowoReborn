package pl.moderr.moderrkowo.reborn.opening;

import pl.moderr.moderrkowo.reborn.utils.WeightedList;

public interface OpeningChest {
    String name();
    String description();
    WeightedList<OpeningChestReward> randomList();
}
