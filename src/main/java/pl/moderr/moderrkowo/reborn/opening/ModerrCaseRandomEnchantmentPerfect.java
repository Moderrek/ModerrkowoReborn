package pl.moderr.moderrkowo.reborn.opening;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.reborn.opening.data.ModerrCaseItem;
import pl.moderr.moderrkowo.reborn.opening.data.ModerrCaseItemRarity;
import pl.moderr.moderrkowo.reborn.utils.ItemStackUtils;
import pl.moderr.moderrkowo.reborn.utils.RandomUtils;

import java.util.HashMap;

public class ModerrCaseRandomEnchantmentPerfect extends ModerrCaseItem{

    public ModerrCaseRandomEnchantmentPerfect(ModerrCaseItemRarity rarity) {
        super(null, rarity);
    }

    Enchantment getRandomEnchantment(){
        return Enchantment.values()[(int) (Math.random()*Enchantment.values().length)];
    }
    int getRandomLevel(Enchantment enchantment){
        return enchantment.getMaxLevel();
    }

    @Override
    public ItemStack item() {
        Enchantment enchantment = getRandomEnchantment();
        HashMap<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>(){
            {
                put(enchantment, getRandomLevel(enchantment));
            }
        };
        return ItemStackUtils.generateEnchantmentBook(enchantments);
    }
}