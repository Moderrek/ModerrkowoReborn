package pl.moderr.moderrkowo.core.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import pl.moderr.moderrkowo.core.mysql.LevelCategory;
import pl.moderr.moderrkowo.core.mysql.User;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

import java.util.ArrayList;

public class KopanieListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void breakBlock(BlockBreakEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getPlayer().getInventory().getItemInMainHand().getAmount() != 0) {
            if (e.getPlayer().getInventory().getItemInMainHand().hasEnchant(Enchantment.SILK_TOUCH)) {
                if (e.getBlock().getType().equals(Material.SPAWNER)) {
                    CreatureSpawner cs = (CreatureSpawner) e.getBlock().getState();
                    ItemStack spawner = new ItemStack(e.getBlock().getType(), 1);
                    BlockStateMeta blockMeta = (BlockStateMeta) spawner.getItemMeta();
                    blockMeta.setBlockState(cs);
                    blockMeta.setDisplayName(ColorUtils.color("&eGenerator potworów &7- &6" + cs.getSpawnedType()));
                    blockMeta.setLore(new ArrayList<>() {
                        {
                            add(ColorUtils.color("&9&lRzadki"));
                            add(ColorUtils.color(" "));
                            add(ColorUtils.color("&fPoziom: &c" + 1));
                            add(ColorUtils.color("&fGeneruje: &e" + ChatUtil.materialName(cs.getSpawnedType())));
                            add(ColorUtils.color(" "));
                            add(ColorUtils.color("&fWykopany przez: &a" + e.getPlayer().getName()));
                        }
                    });
                    spawner.setItemMeta(blockMeta);
                    e.setCancelled(true);
                    e.getBlock().setType(Material.AIR);
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), spawner);
                }
                return;
            }
        }
        if (getExpValue(e.getBlock()) != 0) {
            int amount = e.getBlock().getDrops(e.getPlayer().getInventory().getItemInMainHand()).stream().mapToInt(ItemStack::getAmount).sum();
            expCollect(e.getPlayer(), e.getBlock(), amount);
        }
    }

    private void expCollect(Player player, Block block, int amount) {
        try {
            User u = UserManager.getUser(player.getUniqueId());
            u.addExp(LevelCategory.Kopanie, getExpValue(block) * amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double getExpValue(Block block) {
        switch (block.getType()) {
            case COAL_ORE:
            case REDSTONE_ORE:
                return 0.2;
            case IRON_ORE:
                return 0.4;
            case NETHER_GOLD_ORE:
                return 0.7;
            case GOLD_ORE:
            case LAPIS_ORE:
                return 0.6;
            case NETHER_QUARTZ_ORE:
                return 0.5;
            case DIAMOND_ORE:
                return 1.2;
            case EMERALD_ORE:
                return 2.2;
            default:
                return 0;
        }
    }

}
