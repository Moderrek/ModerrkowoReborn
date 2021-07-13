package pl.moderr.moderrkowo.core.lootchests;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.core.listeners.RandomRange;

public interface ShulkerDropItemMaterial extends ShulkerDropItem {
    RandomRange getRandomCount();

    Material getMaterial();

    @Override
    default ItemStack getDrop() {
        return new ItemStack(getMaterial(), getRandomCount().getRandom());
    }
}
