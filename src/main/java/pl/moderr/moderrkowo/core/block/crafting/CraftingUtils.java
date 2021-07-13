package pl.moderr.moderrkowo.core.block.crafting;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CraftingUtils {
    public static boolean getBooleanOfItems(Player p, ItemStack is, int amount) {
        int found = 0;
        for(ItemStack i : p.getInventory().getContents()){
            if(i != null){
                if(i.isSimilar(is)){
                    found += i.getAmount();
                }
            }
        }
        if(amount > found){
            return false;
        }else{
            return true;
        }

    }
    public static int getCountOfItems(Player p, ItemStack is){
        int found = 0;
        for(ItemStack i : p.getInventory().getContents()){
            if(i != null){
                if(i.isSimilar(is)){
                    found += i.getAmount();
                }
            }
        }
        return found;
    }
    public static boolean consumeItems(Player p, ItemStack is, int amount){
        if(!(getCountOfItems(p, is) >= amount)){
            return false;
        }

        int toRemove = amount;

        for(int x = 0; x != amount; x++){
            for (ItemStack i : p.getInventory().getContents()) {
                if(toRemove <= 0){
                    break;
                }
                if(i != null){
                    if(i.isSimilar(is)){
                        i.setAmount(i.getAmount()-1);
                        toRemove -= 1;
                    }
                }
            }
        }

        if(toRemove <= 0){
            return true;
        }else{
            return false;
        }
    }

    /*public static boolean getBooleanOfMaterial(Player player, ItemStack itemToScan, int count){
        int found = 0;
        for(ItemStack item : player.getInventory().getContents()){
            if(item != null){
                boolean b = true;
                if(item.getType() == itemToScan.getType()){
                    for(Enchantment e : item.getEnchantments().keySet()){
                        if(!itemToScan.getEnchantments().containsKey(e)){
                            b = false;
                        }
                    }
                    if(itemToScan.getItemMeta().hasDisplayName()){
                        if(!item.getItemMeta().hasDisplayName()){
                            b = false;
                        }else{
                            if(!itemToScan.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())){
                                b = false;
                            }
                        }
                    }
                    found = found + item.getAmount();
                }
            }
        }
        if(count > found){
            return false;
        }else{
            return true;
        }
    }*/
}
