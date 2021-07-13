package pl.moderr.moderrkowo.core.lootchests;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.Contract;
import pl.moderr.moderrkowo.core.utils.WeightedList;

import java.util.Random;

public class ShulkerDrop {

    private final int percentageForSlot;
    private final int maxSlots;
    private final String name;
    private final Material color;
    private WeightedList<ShulkerDropItem> drops;

    @Contract(pure = true)
    public ShulkerDrop(WeightedList<ShulkerDropItem> drops, int percentageForSlot, int maxSlots, String name, Material color) {
        this.drops = drops;
        this.percentageForSlot = percentageForSlot;
        this.maxSlots = maxSlots;
        this.name = name;
        this.color = color;
    }

    public WeightedList<ShulkerDropItem> getDrops() {
        return drops;
    }

    public void setDrops(WeightedList<ShulkerDropItem> drops) {
        this.drops = drops;
    }

    public ItemStack generateShulker() {
        ItemStack item = new ItemStack(color, 1);
        BlockStateMeta im = (BlockStateMeta) item.getItemMeta();
        ShulkerBox shulker = (ShulkerBox) im.getBlockState();
        for (int i = 0; i != shulker.getInventory().getSize(); ++i) {
            final int chance = new Random().nextInt(100);
            if (chance <= percentageForSlot) {
                shulker.getInventory().setItem(i, drops.get(new Random()).getDrop());
            }
        }
        im.setBlockState(shulker);
        item.setItemMeta(im);
        item.setDisplayName(name);
        return item;
    }
    public Inventory generateItems(String title, int size) {
        Inventory inv = Bukkit.createInventory(null,size,title);
        for (int i = 0; i != inv.getSize(); ++i) {
            final int chance = new Random().nextInt(100);
            if (chance <= percentageForSlot) {
                inv.setItem(i, drops.get(new Random()).getDrop());
            }
        }
        return inv;
    }

    public Material getColor() {
        return color;
    }

    public int getPercentageForSlot() {
        return percentageForSlot;
    }

    public int getMaxSlots() {
        return maxSlots;
    }
}