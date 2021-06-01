package pl.moderr.moderrkowo.reborn.villagers.data;

import org.bukkit.entity.Player;
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
    private boolean boostedSell;
    private boolean custom;

    @Contract(pure = true)
    public VillagerShopItem(ItemStack item, int requiredQuestLevel, int requiredPlayerLevel, String description, int cost) {
        this.item = item;
        this.requiredQuestLevel = requiredQuestLevel;
        this.requiredPlayerLevel = requiredPlayerLevel;
        this.description = description;
        this.cost = cost;
    }

    @Contract(pure = true)
    public VillagerShopItem(ItemStack item, int requiredQuestLevel, int requiredPlayerLevel, String description, int cost, int sellCost, boolean boostedSell) {
        this.item = item;
        this.requiredQuestLevel = requiredQuestLevel;
        this.requiredPlayerLevel = requiredPlayerLevel;
        this.description = description;
        this.cost = cost;
        this.sellCost = sellCost;
        this.canSell = true;
        this.boostedSell = boostedSell;
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
    public int getFinalCost(){
        int temp = getSellCost();
        temp = (int) (temp * 1.5d);
        if(temp > cost){
            if(cost == 0){
                return temp;
            }else{
                temp = cost;
            }
        }
        return temp;
    }
    public int getRequiredPlayerLevel() {
        return requiredPlayerLevel;
    }
    public boolean isBoostedSell() {
        return boostedSell;
    }
    public void setBoostedSell(boolean boostedSell) {
        this.boostedSell = boostedSell;
    }


    public void onBuy(Player p){
        if (p.getInventory().firstEmpty() != -1) {
            p.getInventory().addItem(getItem().clone());
        } else {
            p.getWorld().dropItem(p.getLocation(), getItem().clone());
        }
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }
}
