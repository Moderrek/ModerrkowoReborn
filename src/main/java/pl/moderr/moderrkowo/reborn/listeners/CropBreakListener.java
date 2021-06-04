package pl.moderr.moderrkowo.reborn.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.reborn.Main;
import pl.moderr.moderrkowo.reborn.mysql.User;
import pl.moderr.moderrkowo.reborn.mysql.UserManager;
import pl.moderr.moderrkowo.reborn.utils.*;
import pl.moderr.moderrkowo.reborn.villagers.data.*;

import java.time.Instant;
import java.util.*;

import static org.bukkit.Material.*;

public class CropBreakListener implements Listener {

    private final Map<Player, Instant> cropMessage = new IdentityHashMap<>();

    public static BlockFace getBlockFace(Player player) {
        List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 16);
        if (lastTwoTargetBlocks.size() != 2 || !lastTwoTargetBlocks.get(1).getType().isOccluding()) return null;
        Block targetBlock = lastTwoTargetBlocks.get(1);
        Block adjacentBlock = lastTwoTargetBlocks.get(0);
        return targetBlock.getFace(adjacentBlock);
    }

    public boolean inRegion(Location loc, UUID uuid) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(loc.getWorld()));
        if (regions == null) {
            return false;
        }
        ApplicableRegionSet set = regions.getApplicableRegions(BukkitAdapter.asBlockVector(loc));
        for (ProtectedRegion region : set.getRegions()) {
            if (region.getOwners().contains(uuid) || region.getMembers().contains(uuid)) {
                return false;
            }
        }
        return set.size() > 0;
    }

    /*@EventHandler
    public void anvil(PrepareAnvilEvent e){
        ItemStack first = e.getInventory().getFirstItem();
        ItemStack second = e.getInventory().getSecondItem();
        if(first == null || second == null){
            return;
        }
        if(first.getType().equals(Material.WOODEN_PICKAXE) || first.getType().equals(Material.STONE_PICKAXE) || first.getType().equals(Material.IRON_PICKAXE) || first.getType().equals(Material.DIAMOND_PICKAXE) || first.getType().equals(Material.NETHERITE_PICKAXE)){
            if(e.getInventory().getResult() == null) {
                if(second.getType().equals(Material.ENCHANTED_BOOK)){
                    EnchantmentStorageMeta book = (EnchantmentStorageMeta) second.getItemMeta();
                    if(book.hasStoredEnchant(Main.getInstance().hammerEnchantment)){
                        e.getInventory().setRepairCost(first.getRepairCost());
                        ItemStack result = new ItemStack(first.getType(), first.getAmount());
                        HashMap<Enchantment, Integer> enchantments = new HashMap<>();
                        for(Enchantment ench : first.getEnchantments().keySet()){
                            enchantments.put(ench, first.getEnchantments().get(ench));
                        }
                        for(Enchantment ench : book.getStoredEnchants().keySet()){
                            int level = book.getStoredEnchants().get(ench);
                            if(enchantments.containsKey(ench)) {
                                if(level == enchantments.get(ench)){
                                    if(enchantments.get(ench)+1 <= ench.getMaxLevel()){
                                        enchantments.replace(ench, enchantments.get(ench)+1);
                                    }
                                }
                            }else {
                                enchantments.put(ench, level);
                            }
                        }
                        for(Enchantment ench : enchantments.keySet()){
                            int level = enchantments.get(ench);
                            if(!ench.canEnchantItem(result)){
                                continue;
                            }
                            result = ItemStackUtils.addEnchantment(result, ench, level);
                            e.getInventory().setRepairCost(e.getInventory().getRepairCost()+(2*level));
                        }
                        e.setResult(result);
                    }
                }
            }
        }
    }*/

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (!e.getPlayer().getInventory().getItemInMainHand().getType().equals(NETHERITE_PICKAXE)) {
            //Logger.logAdminLog("Not pickaxe");
            return;
        }
        if (e.getPlayer().getInventory().getItemInMainHand().hasEnchant(Main.getInstance().hammerEnchantment)) {
            //Logger.logAdminLog("Posiada MŁOT");
            if (e.getPlayer().isSneaking()) {
                return;
            }
            for (Block b : PowerUtils.getSurroundingBlocks(Objects.requireNonNull(getBlockFace(e.getPlayer())), e.getBlock())) {
                if (b == null) {
                    continue;
                }
                //Logger.logAdminLog("Blok do zniszczenia");
                ArrayList<Material> stone = new ArrayList<Material>() {
                    {
                        add(STONE);
                        add(DIORITE);
                        add(ANDESITE);
                        add(GRANITE);
                    }
                };
                if (b.getType() == e.getBlock().getType() || (stone.contains(b.getType()) && stone.contains(e.getBlock().getType()))) {
                    //Logger.logAdminLog("Zły blok");
                    if (!inRegion(b.getLocation(), e.getPlayer().getUniqueId())) {
                        //Logger.logAdminLog("BREAK!");
                        b.breakNaturally(e.getPlayer().getInventory().getItemInMainHand(), true);
                        e.getPlayer().updateInventory();
                    } else {
                        //Logger.logAdminLog("Blok na cuboidzie");
                    }
                }
            }
        } else {
            //Logger.logAdminLog("Posiada NOT MŁOT");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void farmBreak(BlockBreakEvent event) {
        final Block block = event.getBlock();
        final Player player = event.getPlayer();

        if (block.getType().equals(Material.EMERALD_ORE) || block.getType().equals(Material.DIAMOND_ORE) || block.getType().equals(Material.ANCIENT_DEBRIS)) {
            StringBuilder s = new StringBuilder();
            for (ItemStack i : block.getDrops(player.getInventory().getItemInMainHand())) {
                s.append(ChatUtil.materialName(i.getType())).append(" x").append(i.getAmount()).append(" ");
            }
            Logger.logAdminLog("&6" + player.getName() + " &7wykopał &7" + "&8(&7" + s + "&8) &8[&7" + ChatUtil.materialName(player.getInventory().getItemInMainHand().getType()) + "&8]");
        }

        if(event.isCancelled()){
            return;
        }

        if (player.isSneaking()) {
            return;
        }
        if (isNotCrop(block)) {
            return;
        }

        final Ageable ageable = (Ageable) block.getState().getBlockData();

        if (ageable.getAge() == ageable.getMaximumAge()) {
            int ExpOrbs = 1;
            ExperienceOrb orb = null;
            World world = block.getWorld();
            world.spawn(block.getLocation(), ExperienceOrb.class).setExperience(ExpOrbs);
            for (ItemStack item : block.getDrops(player.getInventory().getItemInMainHand())) {
                world.dropItemNaturally(block.getLocation(), item);
            }
            try {
                User u = UserManager.getUser(player.getUniqueId());
                PlayerVillagerData data = null;
                for (PlayerVillagerData villagers : u.getVillagersData().getVillagersData().values()) {
                    if (villagers.isActiveQuest()) {
                        data = villagers;
                        break;
                    }
                }
                if (data == null) {
                    return;
                }
                VillagerData villager = Main.getInstance().villagerManager.villagers.get(data.getVillagerId().toLowerCase());
                Quest quest = villager.getQuests().get(data.getQuestIndex());
                for (IQuestItem item : quest.getQuestItems()) {
                    if(item instanceof IQuestItemCollect){
                        if(!Objects.equals(getCropSeeds(block), ((IQuestItemCollect) item).getMaterial())){
                            return;
                        }
                        int items = data.getQuestItemData().get(item.getQuestItemDataId());
                        int temp = items;
                        temp += 1;
                        data.getQuestItemData().replace(item.getQuestItemDataId(), items, temp);
                        player.sendMessage(ColorUtils.color(villager.getName() + " &6> &aZebrano " + ChatUtil.materialName(event.getBlock().getType())));
                        u.UpdateScoreboard();
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            if (ItemStackUtils.consumeItem(player, 1, getCropSeeds(block))) {
                autoReplant(block);
            } else {
                block.setType(Material.AIR);
            }
            spawnParticles(block.getLocation());
        } else {
            final Instant now = Instant.now();
            cropMessage.compute(player, (uuid, instant) -> {
                if (instant != null && now.isBefore(instant)) {
                    return instant;
                }
                player.sendMessage("§6[SP] §eKucnij aby zniszczyć małe nasionka.");
                return now.plusSeconds(10);
            });
        }

        event.setCancelled(true);
    }

    @EventHandler
    private void cropTrample(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) {
            return;
        }
        if (!event.hasBlock()) {
            return;
        }

        final Block farmland = event.getClickedBlock();
        if (farmland == null) return;
        final Block crop = farmland.getRelative(BlockFace.UP);
        if (isNotCrop(crop)) {
            return;
        }
        event.setCancelled(true);
    }

    private void spawnParticles(Location location) {
        location.add(.5, .5, .5);
        location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location, 10, .5, .5, .5, 0);
    }


    private void autoReplant(Block block) {
        block.setType(block.getType());
    }

    private Material getCropSeeds(Block block) {
        if (isNotCrop(block)) {
            return null;
        }
        switch (block.getType()) {
            case WHEAT:
                return Material.WHEAT_SEEDS;
            case CARROTS:
                return Material.CARROT;
            case POTATOES:
                return Material.POTATO;
            case BEETROOTS:
                return Material.BEETROOT_SEEDS;
            case MELON_STEM:
                return Material.MELON_SEEDS;
            case PUMPKIN_STEM:
                return Material.PUMPKIN_SEEDS;
            case NETHER_WART:
                return Material.NETHER_WART;
        }
        return null;
    }

    private boolean isNotCrop(Block block) {
        switch (block.getType()) {
            case WHEAT:
            case CARROTS:
            case POTATOES:
            case BEETROOTS:
            case MELON_STEM:
            case PUMPKIN_STEM:
            case NETHER_WART:
                return false;
        }
        return true;
    }

}
