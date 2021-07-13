package pl.moderr.moderrkowo.core.lootchests;

import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.core.economy.WithdrawCommand;
import pl.moderr.moderrkowo.core.listeners.RandomRange;

public interface ShulkerDropItemBanknot extends ShulkerDropItem {
    RandomRange getRandomCount();

    @Override
    default ItemStack getDrop() {
        return WithdrawCommand.generateItemStatic(1, getRandomCount().getRandom());
    }
}