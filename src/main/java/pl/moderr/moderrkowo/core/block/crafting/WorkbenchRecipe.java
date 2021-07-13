package pl.moderr.moderrkowo.core.block.crafting;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class WorkbenchRecipe {


    private List<ItemStack> materialsRequiredToItem;
    private ItemStack endItem;
    private int slot;
    private int craftingTime;
    private String description;

    public WorkbenchRecipe(List<ItemStack> materialsRequiredToItem, ItemStack endItem, int slot, int craftingTime, String description) {
        this.materialsRequiredToItem = materialsRequiredToItem;
        this.endItem = endItem;
        this.slot = slot;
        this.craftingTime = craftingTime;
        this.description = description;
    }

    public List<ItemStack> getMaterialsRequiredToItem() {
        return materialsRequiredToItem;
    }

    public void setMaterialsRequiredToItem(List<ItemStack> materialsRequiredToItem) {
        this.materialsRequiredToItem = materialsRequiredToItem;
    }

    public ItemStack getEndItem() {
        return endItem;
    }

    public void setEndItem(ItemStack endItem) {
        this.endItem = endItem;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getCraftingTime() {
        return craftingTime;
    }

    public void setCraftingTime(int craftingTime) {
        this.craftingTime = craftingTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
