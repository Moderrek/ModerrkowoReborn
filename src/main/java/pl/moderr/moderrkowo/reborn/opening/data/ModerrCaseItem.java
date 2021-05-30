package pl.moderr.moderrkowo.reborn.opening.data;

import org.bukkit.inventory.ItemStack;

public class ModerrCaseItem {

    ItemStack item;
    ModerrCaseItemRarity rarity;
    public ModerrCaseItem(ItemStack item, ModerrCaseItemRarity rarity){
        this.item = item;
        this.rarity = rarity;
    }

    public ItemStack item(){
        return item.clone();
    }
    public ModerrCaseItemRarity rarity(){
        return rarity;
    }
}
