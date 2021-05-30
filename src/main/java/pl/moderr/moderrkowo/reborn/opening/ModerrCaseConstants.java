package pl.moderr.moderrkowo.reborn.opening;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.reborn.opening.data.*;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;
import pl.moderr.moderrkowo.reborn.utils.Logger;
import pl.moderr.moderrkowo.reborn.utils.WeightedList;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;

public class ModerrCaseConstants {

    public static final Map<ModerrCaseEnum, ModerrCase> cases = new IdentityHashMap<>();

    static {
        addCase(ModerrCaseEnum.ZWYKLA, new ModerrCase() {
            @Override
            public String name() {
                return ColorUtils.color("&a&lZWYKŁA");
            }

            @Override
            public String guiName() {
                return ColorUtils.color("&7Skrzynia " + name());
            }

            @Override
            public String description() {
                return "OPIS";
            }

            @Override
            public WeightedList<ModerrCaseItem> randomList() {
                return new WeightedList<ModerrCaseItem>() {
                    {
                        put(new ModerrCaseItem(new ItemStack(Material.ACACIA_SAPLING,8), ModerrCaseItemRarity.POSPOLITE), 60);
                        put(new ModerrCaseItem(new ItemStack(Material.OAK_WOOD,64), ModerrCaseItemRarity.POSPOLITE), 60);
                        put(new ModerrCaseItem(new ItemStack(Material.COOKED_BEEF,64), ModerrCaseItemRarity.POSPOLITE), 60);
                        put(new ModerrCaseItem(new ItemStack(Material.IRON_INGOT,32), ModerrCaseItemRarity.POSPOLITE), 55);
                        put(new ModerrCaseItem(new ItemStack(Material.SUGAR_CANE,64), ModerrCaseItemRarity.POSPOLITE), 55);
                        put(new ModerrCaseItem(new ItemStack(Material.EMERALD,20), ModerrCaseItemRarity.POSPOLITE), 55);

                        put(new ModerrCaseItem(new ItemStack(Material.ENDER_PEARL,8), ModerrCaseItemRarity.RZADKIE), 25);
                        put(new ModerrCaseItem(new ItemStack(Material.ENCHANTING_TABLE,1), ModerrCaseItemRarity.RZADKIE), 25);
                        put(new ModerrCaseItem(new ItemStack(Material.BLAZE_ROD,2), ModerrCaseItemRarity.RZADKIE), 30);

                        put(new ModerrCaseItem(new ItemStack(Material.HOPPER,1), ModerrCaseItemRarity.RZADKIE), 15);
                        put(new ModerrCaseRandomEnchantment(ModerrCaseItemRarity.RZADKIE), 25);
                        put(new ModerrCaseItem(new ItemStack(Material.SLIME_BALL,16), ModerrCaseItemRarity.RZADKIE), 16);
                        put(new ModerrCaseRandomDisc(ModerrCaseItemRarity.RZADKIE), 15);

                        put(new ModerrCaseRandomTool(ModerrCaseItemRarity.LEGENDARNE),10);
                        put(new ModerrCaseItem(new ItemStack(Material.OBSERVER,1), ModerrCaseItemRarity.LEGENDARNE), 10);
                        put(new ModerrCaseItem(new ItemStack(Material.GOLDEN_APPLE,2), ModerrCaseItemRarity.LEGENDARNE), 3);
                        put(new ModerrCaseItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE,1), ModerrCaseItemRarity.LEGENDARNE), 2);
                        put(new ModerrCaseItem(new ItemStack(Material.TOTEM_OF_UNDYING,1), ModerrCaseItemRarity.LEGENDARNE), 2);
                        put(new ModerrCaseItem(new ItemStack(Material.NETHERITE_SCRAP,1), ModerrCaseItemRarity.MITYCZNE), 1);
                        put(new ModerrCaseItem(new ItemStack(Material.TRIDENT,1), ModerrCaseItemRarity.MITYCZNE), 1);
                    }
                };
            }
        });
        addCase(ModerrCaseEnum.DNIADZIECKA, new ModerrCase() {
            @Override
            public String name() {
                return ColorUtils.color("&d&lDNIA DZIECKA");
            }

            @Override
            public String guiName() {
                return ColorUtils.color("&7Skrzynia " + name());
            }

            @Override
            public String description() {
                return null;
            }

            @Override
            public WeightedList<ModerrCaseItem> randomList() {
                return new WeightedList<ModerrCaseItem>(){
                    {
                        put(new ModerrCaseItem(new ItemStack(Material.DIRT,16), ModerrCaseItemRarity.POSPOLITE),100);
                    }
                };
            }
        });
    }

    public static ArrayList<ModerrCaseEnum> getChestTypes(){
        return new ArrayList<>(cases.keySet());
    }
    public static ArrayList<ModerrCase> getChests(){
        return new ArrayList<>(cases.values());
    }
    public static ArrayList<String> getGuiNames(){
        ArrayList<String> list = new ArrayList<>();
        for(ModerrCase chest : getChests()){
            list.add(chest.guiName());
        }
        return list;
    }
    public static ModerrCase getCase(ModerrCaseEnum type){
        return cases.get(type);
    }
    public static void addCase(ModerrCaseEnum type, ModerrCase moderrCase){
        if (!cases.containsKey(type)) {
            cases.put(type, moderrCase);
            Logger.logCaseMessage("Zarejestrowano nową skrzynkę " + type.toString());
        } else {
            Logger.logCaseMessage("Skrzynka " + type.toString() + " już jest zarejestrowana!");
        }
    }

    public static ArrayList<String> getChestStringTypes() {
        ArrayList<String> list = new ArrayList<>();
        for(ModerrCaseEnum chest : getChestTypes()){
            list.add(chest.toString());
        }
        return list;
    }
}
