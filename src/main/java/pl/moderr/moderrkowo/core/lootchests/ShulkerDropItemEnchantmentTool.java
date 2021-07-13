package pl.moderr.moderrkowo.core.lootchests;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.core.utils.ItemStackUtils;
import pl.moderr.moderrkowo.core.utils.WeightedList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public interface ShulkerDropItemEnchantmentTool extends ShulkerDropItem {
    default Enchantment getRandomEnchantment() {
        return Enchantment.values()[(int) (Math.random() * Enchantment.values().length)];
    }

    default ItemStack randomTool() {
        WeightedList<Material> materialWeightedList = new WeightedList<>() {
            {
                put(Material.DIAMOND_SWORD, 10);
                put(Material.DIAMOND_PICKAXE, 5);
                put(Material.DIAMOND_CHESTPLATE, 10);
                put(Material.DIAMOND_SHOVEL, 10);
                put(Material.DIAMOND_LEGGINGS, 10);
                put(Material.DIAMOND_HOE, 10);
                put(Material.DIAMOND_AXE, 10);
                put(Material.NETHERITE_AXE, 3);
                put(Material.NETHERITE_PICKAXE, 3);
                put(Material.NETHERITE_HOE, 3);
                put(Material.NETHERITE_SWORD, 3);
                put(Material.NETHERITE_CHESTPLATE, 3);
                put(Material.NETHERITE_HELMET, 3);
                put(Material.NETHERITE_LEGGINGS, 3);
                put(Material.NETHERITE_BOOTS, 3);
                put(Material.FISHING_ROD, 1);
            }
        };
        return new ItemStack(materialWeightedList.get(new Random()), 1);
    }


    default ItemStack randomEnchantment(ItemStack item) {
        // Store all possible enchantments for the item
        List<Enchantment> possible = new ArrayList<Enchantment>();

        // Loop through all enchantemnts
        for (Enchantment ench : Enchantment.values()) {
            // Check if the enchantment can be applied to the item, save it if it can
            if(ench.isCursed()){
                continue;
            }
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
    default ItemStack getDrop() {
        ItemStack tool = randomTool();
        tool = randomEnchantment(tool);
        tool = randomEnchantment(tool);
        tool = randomEnchantment(tool);
        return tool;
    }
}
