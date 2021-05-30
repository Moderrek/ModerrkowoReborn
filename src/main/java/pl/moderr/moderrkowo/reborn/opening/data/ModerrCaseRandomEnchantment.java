package pl.moderr.moderrkowo.reborn.opening.data;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.reborn.utils.ItemStackUtils;
import pl.moderr.moderrkowo.reborn.utils.RandomUtils;

import java.util.HashMap;
import java.util.Random;

public class ModerrCaseRandomEnchantment extends ModerrCaseItem{

    public ModerrCaseRandomEnchantment(ModerrCaseItemRarity rarity) {
        super(null, rarity);
    }

    Enchantment getRandomEnchantment(){
        return Enchantment.values()[(int) (Math.random()*Enchantment.values().length)];
    }
    int getRandomLevel(Enchantment enchantment){
        if(enchantment.getMaxLevel() == 1){
            return 1;
        }else{
            return RandomUtils.getRandomInt(1, enchantment.getMaxLevel());
        }
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
