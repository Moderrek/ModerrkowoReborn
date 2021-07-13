package pl.moderr.moderrkowo.core.block.crafting;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.ItemStackUtils;

import java.util.ArrayList;
import java.util.HashMap;

public interface Workbench {

    Material getMaterial();
    Inventory getInventory();
    String getBlockName();

    ArrayList<WorkbenchRecipe> getCraftingi();

    HashMap<Player, Integer> getCraftingTask();

    @EventHandler
    void openCraftingEvent(PlayerInteractEvent e);

    Inventory getCraftingInventory(Player p);
    void initializeItemsToCraftingInventory();
    void addCraftingsToInventory();

    @EventHandler
    void onInventoryClick(InventoryClickEvent e);



    default void openBlockInventory(PlayerInteractEvent e, boolean ignoreSneaking){
        if(e.isCancelled()){
            return;
        }
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            Block block = e.getClickedBlock();
            //414 79 -453
            if(block.getLocation().getBlockX() == 414 &&
            block.getLocation().getBlockY() == 79 &&
            block.getLocation().getBlockZ() == -453){
                if (block.getType().equals(getMaterial())) {
                    if(!ignoreSneaking){
                        if (p.isSneaking()) {
                            return;
                        }
                    }
                    openCrafting(p);
                    e.setCancelled(true);
                }
            }
        }
    }

    default void setCraftingItem(int slot, Inventory inv, Player p){
        for (WorkbenchRecipe crafting : getCraftingi()) {
            if (slot == crafting.getSlot()) {
                inv.clear();
                initializeItemsToCraftingInventory();
                inv.setItem(22, crafting.getEndItem());

                for (int i = 45; i < 54; i++) {
                    inv.setItem(i, ItemStackUtils.changeName(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "));
                    if (i == 49)
                        inv.setItem(i, ItemStackUtils.createGuiItem(Material.CRAFTING_TABLE, 1, ColorUtils.color("&cStwórz!"), ColorUtils.color("&7Jeżeli posiadasz przedmioty do wybranej receptury,"), ColorUtils.color("&7kliknij na ten przedmiot aby wytworzyć")));
                    if (i == 53)
                        inv.setItem(i, ItemStackUtils.changeLore(ItemStackUtils.changeName(new ItemStack(Material.BARRIER, 1), ColorUtils.color("&cWyjdź")), ColorUtils.color(" "), ColorUtils.color("&7Aby wyjść z okna kliknij")));
                }
                int slot1 = 27;
                for (ItemStack is : crafting.getMaterialsRequiredToItem()) {
                    boolean udane = false;
                    inv.setItem(slot1, is);
                    udane = CraftingUtils.getBooleanOfItems(p, is, is.getAmount());
                    ItemStack statusMaterial = ItemStackUtils.createGuiItem(Material.RED_STAINED_GLASS_PANE, 1,ColorUtils.color("&cNie posiadasz! ❌"), ColorUtils.color("&cBrak &m" + is.getType().toString()));
                    if (udane) {
                        statusMaterial = ItemStackUtils.changeName(new ItemStack(Material.LIME_STAINED_GLASS_PANE),ColorUtils.color("&aPosiadasz ✔"));
                    }
                    inv.setItem(slot1 + 9, statusMaterial);
                    slot1++;
                }
            }
        }
    }

    default void craftingClick(InventoryClickEvent e){
        if (!e.getView().getTitle().equals(ColorUtils.color(getBlockName()))) {
            return;
        }
        if (e.getClick().equals(ClickType.NUMBER_KEY)) {
            e.setCancelled(true);
        }
        e.setCancelled(true);
        if (e.getClickedInventory().getType() == InventoryType.PLAYER) {
            return;
        }

        Player p = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        if (e.getSlot() < 17) {
            setCraftingItem(e.getSlot(), e.getClickedInventory(), p);
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
            return;
        }
        if (e.getSlot() == 53) {
            p.closeInventory();
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
            return;
        }
        if (e.getCurrentItem().getType() == Material.CRAFTING_TABLE) {
            for (WorkbenchRecipe crafting : getCraftingi()) {
                if(crafting.getEndItem().getType() != getInventory().getItem(22).getType()){
                    continue;
                }
                Inventory inv = p.getInventory();
                int udanyInt = 0;
                HashMap<ItemStack, Integer> doUsuniecia = new HashMap<ItemStack, Integer>();
                for (ItemStack is : crafting.getMaterialsRequiredToItem()) {
                    int ileUsunac = is.getAmount();
                    Material coUsunac = is.getType();
                    if(CraftingUtils.getBooleanOfItems(p, is, ileUsunac)){
                        udanyInt++;
                        doUsuniecia.put(is, ileUsunac);
                    }

                }
                if (udanyInt == crafting.getMaterialsRequiredToItem().size()) {
                    for (ItemStack is1 : doUsuniecia.keySet()) {
                        CraftingUtils.consumeItems(p, is1, is1.getAmount());
                    }
                    p.closeInventory();
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                    p.sendTitle("", ColorUtils.color("&eTrwa tworzenie &6" + crafting.getEndItem().getItemMeta().getDisplayName() + "!"));
                    getCraftingTask().put(p, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
                        int countdown = crafting.getCraftingTime();
                        @Override
                        public void run() {
                            if(countdown > 0){
                                p.sendActionBar( ColorUtils.color("&eWytwarzanie &6" + crafting.getEndItem().getItemMeta().getDisplayName() + "&e &f(&6" + countdown + "sek&f)"));
                                countdown--;
                            }else{
                                Bukkit.getScheduler().cancelTask(getCraftingTask().get(p));
                                p.sendActionBar( ColorUtils.color(" "));
                                p.sendTitle(ColorUtils.color("&eWytworzono"), ColorUtils.color("&6" + crafting.getEndItem().getItemMeta().getDisplayName()));
                                p.getWorld().spawnParticle(Particle.VILLAGER_HAPPY,1,1,1,20);
                                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE,1,1);
                                if (p.getInventory().firstEmpty() != -1) {
                                    p.getInventory().addItem(crafting.getEndItem());
                                }else{
                                    p.getWorld().dropItem(p.getLocation(), crafting.getEndItem());
                                }
                                countdown = crafting.getCraftingTime();
                            }

                        }
                    }, 0L, 20L));
                } else {
                    p.closeInventory();
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    p.sendTitle("", ColorUtils.color("&cNie posiadasz wymaganych przedmiotów!"));
                }
            }
            return;
        }
        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
    }

    default void openCrafting(Player p) {
        p.openInventory(getCraftingInventory(p));
        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES,1,1);
        p.spawnParticle(Particle.VILLAGER_HAPPY, 1,1,1,10);
    }

}
