package pl.moderr.moderrkowo.reborn.opening.data;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.reborn.utils.ItemStackUtils;
import pl.moderr.moderrkowo.reborn.utils.WeightedList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ModerrCaseRandomTool extends ModerrCaseItem{

    public ModerrCaseRandomTool(ModerrCaseItemRarity rarity) {
        super(null, rarity);
    }

    ItemStack randomTool(){
        WeightedList<Material> materialWeightedList = new WeightedList<Material>(){
            {
                put(Material.DIAMOND_SWORD,25);
                put(Material.DIAMOND_PICKAXE,50);
                put(Material.DIAMOND_CHESTPLATE,50);
                put(Material.DIAMOND_HELMET,50);
                put(Material.DIAMOND_BOOTS,50);
                put(Material.DIAMOND_SHOVEL,20);
                put(Material.DIAMOND_LEGGINGS,50);
                put(Material.DIAMOND_HOE,20);
                put(Material.DIAMOND_AXE,50);
                put(Material.FISHING_ROD,50);
            }
        };
        return new ItemStack(materialWeightedList.get(new Random()),1);
    }


    ItemStack randomEnchantment(ItemStack item) {
        // Store all possible enchantments for the item
        List<Enchantment> possible = new ArrayList<Enchantment>();

        // Loop through all enchantemnts
        for (Enchantment ench : Enchantment.values()) {
            // Check if the enchantment can be applied to the item, save it if it can
            if (ench.canEnchantItem(item)) {
                possible.add(ench);
            }
        }

        // If we have at least one possible enchantment
        if (possible.size() >= 1) {
            // Randomize the enchantments
            Collections.shuffle(possible);
            // Get the first enchantment in the shuffled list
            Enchantment chosen = possible.get(0);
            // Apply the enchantment with a random level between 1 and the max level
            item = ItemStackUtils.addEnchantment(item, chosen, 1 + (int) (Math.random() * ((chosen.getMaxLevel() - 1) + 1)));
        }

        // Return the item even if it doesn't have any enchantments
        return item;
    }

    @Override
    public ItemStack item() {
        ItemStack tool = randomTool();
        tool = randomEnchantment(tool);
        tool = randomEnchantment(tool);
        tool = randomEnchantment(tool);
        return tool;
    }
}
