package pl.moderr.moderrkowo.reborn.enchantments;


import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.reborn.Main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.bukkit.Material.NETHERITE_PICKAXE;

public class HammerEnchantment extends Enchantment implements Listener {


    public HammerEnchantment() {
        super(new NamespacedKey(Main.getInstance(), "HammerEnchantment"));
    }

    private static String toRoman(int n) {
        String[] romanNumerals = {"M", "CM", "D", "CD", "C", "XC", "L", "X", "IX", "V", "I"};
        int[] romanNumeralNums = {1000, 900, 500, 400, 100, 90, 50, 10, 9, 5, 1};
        String finalRomanNum = "";

        for (int i = 0; i < romanNumeralNums.length; i++) {
            int currentNum = n / romanNumeralNums[i];
            if (currentNum == 0) {
                continue;
            }

            for (int j = 0; j < currentNum; j++) {
                finalRomanNum += romanNumerals[i];
            }

            n = n % romanNumeralNums[i];
        }
        return finalRomanNum;
    }

    public static List<Block> getSquare(Location location, int radius) {
        List<Block> blocks = new ArrayList<>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                blocks.add(location.getWorld().getBlockAt(x, location.getBlockY(), z));
            }
        }
        return blocks;
    }

    public static List<Block> getSquareRotation(Location loc, BlockFace face) {
        List<Block> blocks = getSquare(loc, 1);
        if (face == BlockFace.UP || face == BlockFace.DOWN) {
            return blocks;
        } else {
            List<Block> rotated = new ArrayList<>();
            blocks.forEach(b -> {
                Location center = loc.clone();
                Vector v = face.getDirection();
                if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
                    v.rotateAroundAxis(v, Math.toRadians(90));
                } else {
                    v.rotateAroundAxis(v, Math.toRadians(180));
                }
                Block newBlock = center.add(v).getBlock();
                rotated.add(newBlock);
            });
            return rotated;
        }

    }


    @Override
    public @NotNull NamespacedKey getKey() {
        return super.getKey();
    }

    @Override
    public @NotNull String getName() {
        return "Szeroki Trzon";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public @NotNull EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment other) {
        return other == Enchantment.SILK_TOUCH;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return item.getType() == NETHERITE_PICKAXE;
    }

    @NotNull
    @Override
    public Component displayName(int level) {
        return Component.text(getName());
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @NotNull
    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.VERY_RARE;
    }

    @Override
    public float getDamageIncrease(int level, @NotNull EntityCategory entityCategory) {
        return 0;
    }

    @NotNull
    @Override
    public Set<EquipmentSlot> getActiveSlots() {
        return new HashSet<EquipmentSlot>() {
            {
                add(EquipmentSlot.HAND);
            }
        };
    }
}
