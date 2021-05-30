package pl.moderr.moderrkowo.reborn.economy;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class RynekItem {

    public UUID owner;
    public ItemStack item;
    public int cost;

    public RynekItem(UUID owner, ItemStack item, int cost){
        this.owner = owner;
        this.item = item;
        this.cost = cost;
    }

}
