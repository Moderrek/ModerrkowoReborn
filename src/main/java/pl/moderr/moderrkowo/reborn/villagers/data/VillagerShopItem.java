package pl.moderr.moderrkowo.reborn.villagers.data;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;

public class VillagerShopItem {

    private final int requiredQuestLevel;
    private final int requiredPlayerLevel;
    private final ItemStack item;
    private final String description;
    private final int cost;
    private boolean canSell;
    private int sellCost;

    @Contract(pure = true)
    public VillagerShopItem(ItemStack item, int requiredQuestLevel, int requiredPlayerLevel, String description, int cost) {
        this.item = item;
        this.requiredQuestLevel = requiredQuestLevel;
        this.requiredPlayerLevel = requiredPlayerLevel;
        this.description = description;
        this.cost = cost;
    }

    @Contract(pure = true)
    public VillagerShopItem(ItemStack item, int requiredQuestLevel, int requiredPlayerLevel, String description, int cost, int sellCost) {
        this.item = item;
        this.requiredQuestLevel = requiredQuestLevel;
        this.requiredPlayerLevel = requiredPlayerLevel;
        this.description = description;
        this.cost = cost;
        this.sellCost = sellCost;
        this.canSell = true;
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public String getDescription() {
        return description;
    }

    public int getRequiredQuestLevel() {
        return requiredQuestLevel;
    }

    public int getCost() {
        return cost;
    }

    public boolean canSell() {
        return canSell;
    }

    public int getSellCost() {
        return sellCost;
    }

    public int getRequiredPlayerLevel() {
        return requiredPlayerLevel;
    }
}
