package pl.moderr.moderrkowo.reborn.opening;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.reborn.Main;
import pl.moderr.moderrkowo.reborn.opening.data.ModerrCaseItem;
import pl.moderr.moderrkowo.reborn.opening.data.ModerrCaseItemRarity;
import pl.moderr.moderrkowo.reborn.utils.ItemStackUtils;
import pl.moderr.moderrkowo.reborn.utils.RandomUtils;

import java.util.HashMap;

public class ModerrCaseRandomEnchantmentPerfect extends ModerrCaseItem{

    public ModerrCaseRandomEnchantmentPerfect(ModerrCaseItemRarity rarity, int weight) {
        super(null, rarity, weight);
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
        if(enchantment.getKey() == Main.getInstance().hammerEnchantment.getKey()){
            enchantment = Enchantment.DIG_SPEED;
        }
        Enchantment finalEnchantment = enchantment;
        HashMap<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>(){
            {
                put(finalEnchantment, getRandomLevel(finalEnchantment));
            }
        };
        return ItemStackUtils.generateEnchantmentBook(enchantments);
    }
}