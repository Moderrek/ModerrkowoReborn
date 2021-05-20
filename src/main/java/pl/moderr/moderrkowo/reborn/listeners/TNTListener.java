package pl.moderr.moderrkowo.reborn.listeners;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

public class TNTListener implements Listener {

    public static List<Location> generateSphere(Location centerBlock, int radius, boolean hollow, Material Mat) {

        List<Location> circleBlocks = new ArrayList<>();

        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();

        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {

                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));

                    if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {

                        Location l = new Location(centerBlock.getWorld(), x, y, z);
                        World world = centerBlock.getWorld();
                        world.getBlockAt(l).setType(Mat);

                    }

                }
            }
        }

        return circleBlocks;
    }
    /*@EventHandler
    public void onCraft(PrepareItemCraftEvent e) {
        if (e.getInventory().getResult() == null) {
            return;
        }
        if (e.getInventory().getResult().getType().equals(Material.TNT)) {
            e.getInventory().setResult(new ItemStack(Material.AIR));
            for (HumanEntity p : e.getInventory().getViewers()) {
                p.sendMessage(ColorUtils.color("&cCrafting TNT jest wyłączony!"));
            }
        }
    }
    @EventHandler
    public void placeTnt(BlockPlaceEvent e) {
        if (e.getBlockPlaced().getType() == Material.TNT) {
            e.getBlockPlaced().setMetadata("tnt", new FixedMetadataValue(Main.getInstance(), "tnt"));
        }
    }
    @EventHandler
    public void breakTnt(BlockBreakEvent e) {
        if (!e.isCancelled()) {
            if (e.getBlock().getType() == Material.TNT) {
                if (!e.getBlock().hasMetadata("tnt")) {
                    e.setCancelled(true);
                    e.getBlock().setType(Material.AIR);
                    e.getPlayer().sendMessage(ColorUtils.color("&cNie można zbierać tnt wygenerowanego przez świat!"));
                }
            }
        }
    }*/

    public static void replaceSphere(Location centerBlock, int radius, boolean hollow, Material from, Material to, int percent) {

        List<Location> circleBlocks = new ArrayList<>();

        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();

        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {

                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));

                    if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {

                        Location l = new Location(centerBlock.getWorld(), x, y, z);
                        World world = centerBlock.getWorld();
                        if (Math.random() * 100 < percent) {
                            if (world.getBlockAt(l).getType() == from) {
                                world.getBlockAt(l).setType(to);
                            }
                        }


                    }

                }
            }
        }

    }

    public static List<Location> replaceSphereSolidBlocks(Location centerBlock, int radius, boolean hollow, Material to, int percent) {

        List<Location> circleBlocks = new ArrayList<>();

        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();

        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {

                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));

                    if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {

                        Location l = new Location(centerBlock.getWorld(), x, y, z);
                        World world = centerBlock.getWorld();
                        if (Math.random() * 100 < percent) {
                            if (world.getBlockAt(l).getType().isSolid()) {
                                world.getBlockAt(l).setType(to);
                            }
                        }


                    }

                }
            }
        }

        return circleBlocks;
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (e.getEntityType() == EntityType.PRIMED_TNT) {
            e.setCancelled(true);
            e.getEntity().getWorld().createExplosion(e.getLocation(), 7);
            replaceSphere(e.getEntity().getLocation(), 8, false, Material.AIR, Material.FIRE, 10);
            replaceSphere(e.getEntity().getLocation(), 5, false, Material.STONE, Material.COBBLESTONE, 3);
            replaceSphere(e.getEntity().getLocation(), 5, false, Material.STONE, Material.ANDESITE, 6);
            replaceSphere(e.getEntity().getLocation(), 5, false, Material.STONE, Material.GRAVEL, 5);
            e.getEntity().getWorld().spawnParticle(Particle.FLAME, e.getLocation(), 400, 1, 1, 1, 0.3f);
            e.getEntity().getWorld().spawnParticle(Particle.SMOKE_LARGE, e.getLocation(), 275, 2, 2, 2, 0.1f);
            e.getEntity().getWorld().spawnParticle(Particle.SMOKE_LARGE, e.getLocation().add(0, 3, 0), 375, 2, 2, 2, 0.02f);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.playSound(e.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, 0.2f);
                p.playSound(e.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 5, 2f);
            }

        }
        if (e.getEntityType() == EntityType.MINECART_TNT) {
            e.setCancelled(true);
            e.getEntity().getWorld().createExplosion(e.getLocation(), 5);
            replaceSphere(e.getEntity().getLocation(), 8, false, Material.AIR, Material.FIRE, 3);
            replaceSphere(e.getEntity().getLocation(), 5, false, Material.STONE, Material.COBBLESTONE, 6);
            replaceSphere(e.getEntity().getLocation(), 5, false, Material.STONE, Material.ANDESITE, 12);
            replaceSphere(e.getEntity().getLocation(), 5, false, Material.STONE, Material.GRAVEL, 10);
            e.getEntity().getWorld().spawnParticle(Particle.FLAME, e.getLocation(), 200, 1, 1, 1, 0.2f);
            e.getEntity().getWorld().spawnParticle(Particle.SMOKE_LARGE, e.getLocation(), 100, 2, 2, 2, 0.1f);
        }
    }

    public void cylinder(Location loc, Material mat, int r) {
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        World w = loc.getWorld();
        int rSquared = r * r;
        for (int x = cx - r; x <= cx + r; x++) {
            for (int z = cz - r; z <= cz + r; z++) {
                if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= rSquared) {
                    w.getBlockAt(x, cy, z).setType(mat);
                }
            }
        }
    }

}
