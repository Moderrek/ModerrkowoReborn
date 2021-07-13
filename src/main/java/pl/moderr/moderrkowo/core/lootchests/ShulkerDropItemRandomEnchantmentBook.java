package pl.moderr.moderrkowo.core.lootchests;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.core.utils.ItemStackUtils;
import pl.moderr.moderrkowo.core.utils.RandomUtils;
import pl.moderr.moderrkowo.core.utils.WeightedList;

import java.util.HashMap;
import java.util.Random;

public interface ShulkerDropItemRandomEnchantmentBook extends ShulkerDropItem {
    default Enchantment getRandomEnchantment() {
        return Enchantment.values()[(int) (Math.random() * Enchantment.values().length)];
    }

    default int getRandomLevel(Enchantment enchantment) {
        if (enchantment.getMaxLevel() == 1) {
            return 1;
        } else {
            return RandomUtils.getRandomInt(1, enchantment.getMaxLevel());
        }
    }

    default int getRandomEnchantsCount() {
        WeightedList<Integer> weightedList = new WeightedList<Integer>();
        weightedList.put(1, 75);
        weightedList.put(2, 10);
        weightedList.put(3, 15);
        return weightedList.get(new Random());
    }

    @Override
    default ItemStack getDrop() {
        Enchantment enchantment = getRandomEnchantment();
        HashMap<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>() {
            {
                put(enchantment, getRandomLevel(enchantment));
            }
        };
        return ItemStackUtils.generateEnchantmentBook(enchantments);
    }
}
