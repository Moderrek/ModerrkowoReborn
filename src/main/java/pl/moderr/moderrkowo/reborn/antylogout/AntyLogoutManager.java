package pl.moderr.moderrkowo.reborn.antylogout;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.moderr.moderrkowo.reborn.Main;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;
import pl.moderr.moderrkowo.reborn.utils.RandomUtils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class AntyLogoutManager implements Listener {

    private final HashMap<UUID, AntyLogoutItem> antyLogout = new HashMap<>();
    private final int seconds = 10;

    public AntyLogoutManager() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            for (UUID uuid : antyLogout.keySet()) {
                AntyLogoutItem nowy = antyLogout.get(uuid);
                if (nowy.seconds <= 0) {
                    try {
                        if (Bukkit.getPlayer(uuid) != null) {
                            Objects.requireNonNull(Bukkit.getPlayer(uuid)).sendMessage(ColorUtils.color("&aWyszedłeś z walki"));
                            nowy.bossBar.removePlayer(Objects.requireNonNull(Bukkit.getPlayer(uuid)));
                        }
                        antyLogout.remove(uuid);
                    } catch (Exception ignored) {

                    }
                    return;
                }
                nowy.bossBar.setProgress((double) nowy.seconds / seconds);
                nowy.seconds--;
                antyLogout.replace(uuid, antyLogout.get(uuid), nowy);
            }
        }, 0, 20);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (antyLogout.containsKey(e.getPlayer().getUniqueId())) {
            e.getPlayer().setHealth(0);
            Bukkit.broadcastMessage(ColorUtils.color("&c" + e.getPlayer().getName() + " wylogował się podczas walki"));
            antyLogout.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void damage(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        /*if (e.getEntity().getWorld().getName().equals("world")) {
            Date dt = new Date();
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(dt);
            if ((cal.get(Calendar.HOUR_OF_DAY) > 8 && cal.get(Calendar.HOUR_OF_DAY) < 18)) {
                if (e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                    if (e.getDamager() instanceof Arrow) {
                        Arrow arrow = (Arrow) e.getDamager();
                        if (arrow.getShooter() instanceof Player) {
                            ((Player) arrow.getShooter()).sendMessage(ColorUtils.color("&cTeraz trwa godzina pokoju do 18!"));
                            e.setCancelled(true);
                        }
                    }
                }
                if (e.getDamager() instanceof Player) {
                    e.getDamager().sendMessage(ColorUtils.color("&cTeraz trwa godzina pokoju do 18!"));
                    e.setCancelled(true);
                }
                return;
            }
        }*/
        Entity whoWasHit = e.getEntity();
        Random rd = new Random();

        ArmorStand as = whoWasHit.getWorld().spawn(whoWasHit.getLocation().add(rd.nextDouble(), 0, rd.nextDouble()), ArmorStand.class);
        as.setInvulnerable(true);
        as.setGravity(false);
        as.setVisible(false);
        as.setCustomNameVisible(true);
        Double d = e.getFinalDamage() / 2;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(1);
        as.setCustomName(ColorUtils.color("&c-" + df.format(d) + "❤"));
        //as.setCanMove(false);
        as.setSmall(true);
        as.setRemoveWhenFarAway(true);
        as.setAI(false);
        as.setCanPickupItems(false);
        as.setCollidable(false);
        as.setSilent(true);
        //sas.setVelocity(as.getVelocity().setY(Objects.requireNonNull(as.getLocation().getWorld()).getHighestBlockYAt(as.getLocation())));
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), as::remove, 20L * RandomUtils.getRandomInt(1, 2));
        if (e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
            if (e.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) e.getDamager();
                if (arrow.getShooter() instanceof Player) {
                    setAntyLogout(((Player) arrow.getShooter()));
                    setAntyLogout((Player) e.getEntity());
                }
            }
        }
        if (e.getDamager() instanceof Player) {
            setAntyLogout((Player) e.getEntity());
            setAntyLogout((Player) e.getDamager());
        }
    }

    public void setAntyLogout(Player p) {
        AntyLogoutItem item = new AntyLogoutItem(seconds, Bukkit.createBossBar(ColorUtils.color("&aANTY LOGOUT"), BarColor.RED, BarStyle.SOLID));
        if (!antyLogout.containsKey(p.getUniqueId())) {
            antyLogout.put(p.getUniqueId(), item);
            item.bossBar.addPlayer(p);
            p.sendMessage(ColorUtils.color("&cWkroczyłeś do walki"));
        } else {
            AntyLogoutItem itemA = antyLogout.get(p.getUniqueId());
            itemA.seconds = seconds;
        }
    }

    public boolean inFight(UUID uuid) {
        return antyLogout.containsKey(uuid);
    }

}
