package pl.moderr.moderrkowo.reborn.opening;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.reborn.Main;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;
import pl.moderr.moderrkowo.reborn.utils.ItemStackUtils;
import pl.moderr.moderrkowo.reborn.utils.WeightedList;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class OpeningManager implements Listener {

    private String invname = ColorUtils.color("&eOtwórz &6&lSKRZYNIE");

    public OpeningManager(){
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    public Inventory getInv(){
        //22
        Inventory inv = Bukkit.createInventory(null,54,invname);
        for (int i = 0; i != 53; i++) {
            inv.setItem(i, ItemStackUtils.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
        }
        for (int i = 53 - 8; i != 53; i++) {
            inv.setItem(i, ItemStackUtils.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, " "));
        }
        inv.setItem(53, ItemStackUtils.createGuiItem(Material.BARRIER, 1, ColorUtils.color("&cWyjdź")));
        inv.setItem(22, ItemStackUtils.createGuiItem(Material.CHEST, 1, ColorUtils.color("&7Skrzynia " + "&6&lTEST")));
        return inv;
    }

    HashMap<UUID, OpeningData> openingCase = new HashMap<>();

    public void OpenCase(Player p, OpeningChest chest){
        Inventory inv = Bukkit.createInventory(null, 27, ColorUtils.color("&7Skrzynia " + chest.name()));
        for (int i = 0; i != 26; i++) {
            inv.setItem(i, ItemStackUtils.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, " "));
        }
        inv.setItem(4, ItemStackUtils.createGuiItem(Material.LIME_STAINED_GLASS_PANE, 1, " "));
        inv.setItem(22, ItemStackUtils.createGuiItem(Material.LIME_STAINED_GLASS_PANE, 1, " "));
        Bukkit.broadcastMessage(ColorUtils.color("  &e&l" + p.getName() + " &7otwiera skrzynie &f" + chest.name()));
        ArrayList<OpeningChestReward> randomizedReward = new ArrayList<>();
        for(int i = 0; i != 33; i++){
            randomizedReward.add(chest.randomList().get(new Random()));
        }
        // showing anim
        int maxOffset = 21;
        int taskId;
        AtomicInteger offset = new AtomicInteger();
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            offset.getAndIncrement();
            anim(p, inv, randomizedReward, offset.get());
            if(offset.get() == maxOffset){
                openingCase.get(p.getUniqueId()).setReward(randomizedReward.get(3+ offset.get()));
                OpenCaseReward(p.getUniqueId());
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
            }else{
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP,2,1);
            }
        },0,5L);
        openingCase.put(p.getUniqueId(), new OpeningData(randomizedReward.get(3), inv, taskId));
        p.openInventory(inv);
    }

    public void OpenCaseReward(UUID playerId){
        OpeningData data = openingCase.get(playerId);
        openingCase.remove(playerId);
        Bukkit.getScheduler().cancelTask(data.getTaskId());
        Bukkit.getPlayer(playerId).getInventory().addItem(data.getReward().item());
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> Objects.requireNonNull(Bukkit.getPlayer(playerId)).closeInventory(), 30);

    }

    public void anim(Player p, Inventory inv, ArrayList<OpeningChestReward> rewards, int offset){
        for(int i = 10; i != 16; i++){
            inv.setItem(i, rewards.get((i-10)+offset).item());
        }
    }

    @EventHandler
    public void closeInventory(InventoryCloseEvent e){
        if(e.getView().getTitle().equals(ColorUtils.color("&7Skrzynia &6&lTEST"))){
            OpenCaseReward(e.getPlayer().getUniqueId());
        }
    }

    public final OpeningChest _TEST = new OpeningChest() {
        @Override
        public String name() {
            return ColorUtils.color("&6&lTEST");
        }

        @Override
        public String description() {
            return "OPIS";
        }

        @Override
        public WeightedList<OpeningChestReward> randomList() {
            return new WeightedList<OpeningChestReward>(){
                {
                    put(() -> new ItemStack(Material.DIRT, 1), 40);
                    put(() -> new ItemStack(Material.COBBLESTONE,1), 30);
                    put(() -> new ItemStack(Material.DIAMOND, 1), 10);
                    put(() -> new ItemStack(Material.HOPPER, 1), 20);
                }
            };
        }
    };

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        if (e.getClickedInventory() == null) {
            return;
        }
        if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            if (e.getView().getTitle().equals(invname) || e.getView().getTitle().equals(ColorUtils.color("&7Skrzynia &6&lTEST"))) {
                e.setCancelled(true);
                ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
                return;
            }
            return;
        }
        if(e.getView().getTitle().equals(ColorUtils.color("&7Skrzynia &6&lTEST"))){
            e.setCancelled(true);
            ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
        }
        if (e.getView().getTitle().equals(invname)) {
            e.setCancelled(true);
            if(e.getSlot() == 22){
                OpenCase((Player) e.getWhoClicked(), _TEST);
                return;
            }
            if(e.getSlot() == 53){
                e.getWhoClicked().closeInventory();
                return;
            }
            ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
        }
    }


    @EventHandler
    public void interact(PlayerInteractEvent e){
        if(e.getClickedBlock() == null){
            return;
        }
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
           return;
        }
        if(e.getPlayer().isSneaking()){
            return;
        }
        Location loc1 = new Location(Bukkit.getWorld("world"), 219, 66, -44);
        Location loc2 = e.getClickedBlock().getLocation();
        if(loc1.getBlockX() == loc2.getBlockX() &&
        loc1.getBlockY() == loc2.getBlockY() &&
        loc1.getBlockZ() == loc2.getBlockZ()){
            if(!e.getPlayer().isOp()){
                e.getPlayer().sendMessage(ColorUtils.color("&cMożliwość otwierania skrzyń będzie niedługo!"));
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
                return;
            }
            Player p = e.getPlayer();
            p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN,1,0.6f);
            p.openInventory(getInv());
            /*OpeningChest(e.getPlayer(), new OpeningChest() {
                @Override
                public String name() {
                    return ColorUtils.color("&6&lTEST");
                }
            });*/
        }
    }

}
