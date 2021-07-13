package pl.moderr.moderrkowo.core.listeners;

import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

public class SpawnerPlaceListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void spawnerPlace(BlockPlaceEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getBlock().getType().equals(Material.SPAWNER)) {
            ItemStack spawner = e.getItemInHand();
            CreatureSpawner cs = (CreatureSpawner) e.getBlock().getState();
            String s = spawner.getDisplayName().replace(ColorUtils.color("&eGenerator potworów &7- &6"), "");
            cs.setSpawnedType(EntityType.valueOf(s));
            cs.update();
        }
    }

}
