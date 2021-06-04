package pl.moderr.moderrkowo.reborn.opening;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
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
import org.bukkit.inventory.meta.FireworkMeta;
import pl.moderr.moderrkowo.reborn.Main;
import pl.moderr.moderrkowo.reborn.mysql.User;
import pl.moderr.moderrkowo.reborn.mysql.UserManager;
import pl.moderr.moderrkowo.reborn.opening.data.*;
import pl.moderr.moderrkowo.reborn.utils.ChatUtil;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;
import pl.moderr.moderrkowo.reborn.utils.ItemStackUtils;
import pl.moderr.moderrkowo.reborn.utils.WeightedList;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class OpeningManager implements Listener {

    private final String invname = ColorUtils.color("&eOtwórz &6&lSKRZYNIE");
    private final String invnamepercent = ColorUtils.color("&ePodgląd skrzyni");

    public OpeningManager(){
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }


    private Inventory getInvPercent(ModerrCase chest) {
        Inventory inv = Bukkit.createInventory(null,54,invnamepercent);
        for (int i = 0; i != 53; i++) {
            inv.setItem(i, ItemStackUtils.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
        }
        for (int i = 53 - 8; i != 53; i++) {
            inv.setItem(i, ItemStackUtils.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, " "));
        }
        for(int i = 0; i != chest.itemList().size(); i++){
            ModerrCaseItem item = new ArrayList<>(chest.itemList()).get(i);
            String color = "";
            switch (item.rarity()){
                case POSPOLITE:
                    color = "&f&lPospolite";
                    break;
                case RZADKIE:
                    color = "&9&lRzadkie";
                    break;
                case LEGENDARNE:
                    color = "&d&lLegendarne";
                    break;
                case MITYCZNE:
                    color = "&c&lMityczne";
                    break;
            }
            ItemStack itemstack = item.item();
            ItemStackUtils.changeLore(itemstack, " ", ColorUtils.color("&eRzadkość: " + color), ColorUtils.color("&eSzansa: " + checkPercent(chest.randomList(), item.weight)), " ");
            inv.setItem(i, itemstack);
        }
        inv.setItem(53, ItemStackUtils.createGuiItem(Material.BARRIER, 1, ColorUtils.color("&cWyjdź")));
        return inv;
    }

    public String checkPercent(WeightedList<ModerrCaseItem> weightedList, double weight){
        double suma = weightedList.values().stream().mapToInt(integer -> integer).sum();
        DecimalFormat df = new DecimalFormat("##.##%");
        return df.format(weight /suma);
    }

    public Inventory getInv(User u){
        //22
        Inventory inv = Bukkit.createInventory(null,54,invname);
        for (int i = 0; i != 53; i++) {
            inv.setItem(i, ItemStackUtils.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
        }
        for (int i = 53 - 8; i != 53; i++) {
            inv.setItem(i, ItemStackUtils.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, " "));
        }
        inv.setItem(53, ItemStackUtils.createGuiItem(Material.BARRIER, 1, ColorUtils.color("&cWyjdź")));
        ModerrCaseEnum chestType = ModerrCaseEnum.ZWYKLA;
        ModerrCase chest = ModerrCaseConstants.getCase(chestType);
        inv.setItem(22, ItemStackUtils.createGuiItem(Material.CHEST, 1,
                ColorUtils.color("   &7Skrzynia " + chest.name() + "   "),
                ColorUtils.color(" "),
                ColorUtils.color("   &7Posiadasz: &6" + u.getUserChestStorage().getAmountOfItem(new StorageItemKey(StorageItemType.Chest, ModerrCaseEnum.ZWYKLA)) + "   "),
                ColorUtils.color("   &7Klucze: &6" + u.getUserChestStorage().getAmountOfItem(new StorageItemKey(StorageItemType.Key, ModerrCaseEnum.ZWYKLA)) + "   "),
                ColorUtils.color(" "),
                ColorUtils.color("   &8Kliknij aby otworzyć" + "   "),
                ColorUtils.color("   &8PPM szczegóły"),
                ColorUtils.color(" ")
        ));
        ModerrCase chest2 = ModerrCaseConstants.getCase(ModerrCaseEnum.SKAZENIA);
        inv.setItem(31, ItemStackUtils.createGuiItem(Material.CRIMSON_NYLIUM, 1,
                ColorUtils.color("   &7Skrzynia " + chest2.name() + "   "),
                ColorUtils.color(" "),
                ColorUtils.color("   &7Posiadasz: &6" + u.getUserChestStorage().getAmountOfItem(new StorageItemKey(StorageItemType.Chest, ModerrCaseEnum.SKAZENIA)) + "   "),
                ColorUtils.color("   &7Klucze: &6" + u.getUserChestStorage().getAmountOfItem(new StorageItemKey(StorageItemType.Key, ModerrCaseEnum.SKAZENIA)) + "   "),
                ColorUtils.color(" "),
                ColorUtils.color("   &8Kliknij aby otworzyć" + "   "),
                ColorUtils.color("   &8PPM szczegóły"),
                ColorUtils.color(" ")
        ));
        return inv;
    }

    HashMap<UUID, OpeningData> openingCase = new HashMap<>();

    public void OpenCase(Player p, ModerrCase chest){
        Inventory inv = Bukkit.createInventory(null, 27, ColorUtils.color("&7Skrzynia " + chest.name()));
        for (int i = 0; i != 27; i++) {
            inv.setItem(i, ItemStackUtils.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, " "));
        }
        inv.setItem(4, ItemStackUtils.createGuiItem(Material.LIME_STAINED_GLASS_PANE, 1, " "));
        inv.setItem(22, ItemStackUtils.createGuiItem(Material.LIME_STAINED_GLASS_PANE, 1, " "));
        ArrayList<ModerrCaseItemTemp> randomizedReward = new ArrayList<>();
        for(int i = 0; i != 33; i++){
            ModerrCaseItem item = chest.randomList().get(new Random());
            randomizedReward.add(new ModerrCaseItemTemp(item.item(), item.rarity()));
        }
        // showing anim
        int maxOffset = 21;
        int taskId;
        AtomicInteger offset = new AtomicInteger();
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            offset.getAndIncrement();
            anim(p, inv, randomizedReward, offset.get());
            if(offset.get() == maxOffset){
                OpenCaseReward(p.getUniqueId());
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
            }else{
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP,2,1);
            }
        },0,5L);
        openingCase.put(p.getUniqueId(), new OpeningData(new ModerrCaseItemTemp(randomizedReward.get(3).item(), randomizedReward.get(3).rarity()), chest.name(), inv, taskId));
        openingCase.get(p.getUniqueId()).setReward(randomizedReward.get(3+maxOffset));
        p.openInventory(inv);
    }

    public void OpenCaseReward(UUID playerId){
        OpeningData data = openingCase.get(playerId);
        openingCase.remove(playerId);
        Bukkit.getScheduler().cancelTask(data.getTaskId());
        Bukkit.broadcastMessage(ColorUtils.color("  &e&l" + Bukkit.getPlayer(playerId).getName() + " &7otwiera skrzynie &f" + data.name()));
        String color = "";
        switch (data.getReward().rarity()){
            case POSPOLITE:
                color = "&f&lP &e";
                break;
            case RZADKIE:
                color = "&9&lR &e";
                break;
            case LEGENDARNE:
                color = "&d&lL &e";
                break;
            case MITYCZNE:
                color = "&c&lM &e";
                break;
        }
        Bukkit.broadcastMessage(ColorUtils.color("  &7znajduje " + color + ChatUtil.materialName(data.getReward().item().getType())));
        Bukkit.getPlayer(playerId).getInventory().addItem(data.getReward().item());
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> Objects.requireNonNull(Bukkit.getPlayer(playerId)).closeInventory(), 30);
        spawnFireworks(Bukkit.getPlayer(playerId).getLocation(), 1);

    }

    public static void spawnFireworks(Location location, int amount){
        Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();

        for(int i = 0;i<amount; i++){
            Firework fw2 = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }

    public void anim(Player p, Inventory inv, ArrayList<ModerrCaseItemTemp> rewards, int offset){
        for(int i = 10; i != 17; i++){
            String color = "";
            ModerrCaseItemTemp item = rewards.get((i-10)+offset);
            switch (item.rarity()){
                case POSPOLITE:
                    color = "&f&lP &e";
                    break;
                case RZADKIE:
                    color = "&9&lR &e";
                    break;
                case LEGENDARNE:
                    color = "&d&lL &e";
                    break;
                case MITYCZNE:
                    color = "&c&lM &e";
                    break;
            }
            inv.setItem(i, ItemStackUtils.changeName(item.item(), color + ChatUtil.materialName(item.item().getType())));
        }
    }

    @EventHandler
    public void closeInventory(InventoryCloseEvent e){
        if(ModerrCaseConstants.getGuiNames().contains(e.getView().getTitle())){
            OpenCaseReward(e.getPlayer().getUniqueId());
        }
    }


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
        if(e.getView().getTitle().equals(invnamepercent)){
            e.setCancelled(true);
            if(e.getSlot() == 53){
                e.getWhoClicked().closeInventory();
            }
            return;
        }
        if(ModerrCaseConstants.getGuiNames().contains(e.getView().getTitle())){
            e.setCancelled(true);
            ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
        }
        if (e.getView().getTitle().equals(invname)) {
            e.setCancelled(true);
            User u = UserManager.getUser(e.getWhoClicked().getUniqueId());
            if(e.getSlot() == 22){
                if(e.isRightClick()){
                    e.getWhoClicked().openInventory(getInvPercent(ModerrCaseConstants.getCase(ModerrCaseEnum.ZWYKLA)));
                    return;
                }
                if(u.getUserChestStorage().hasItem(new StorageItemKey(StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), 1) && u.getUserChestStorage().hasItem(new StorageItemKey(StorageItemType.Key, ModerrCaseEnum.ZWYKLA), 1)){
                    OpenCase((Player) e.getWhoClicked(), ModerrCaseConstants.getCase(ModerrCaseEnum.ZWYKLA));
                    u.getUserChestStorage().subtractChest(new StorageItemKey(StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), 1);
                    u.getUserChestStorage().subtractKey(new StorageItemKey(StorageItemType.Key, ModerrCaseEnum.ZWYKLA), 1);
                }else{
                    e.getWhoClicked().sendMessage(ColorUtils.color("&8[!] &cNie posiadasz potrzebnych przedmiotów"));
                    e.getWhoClicked().sendMessage(ColorUtils.color("&8[!] &eAby otworzyć skrzynke potrzebna jest skrzynia i klucz"));
                }
                return;
            }
            if(e.getSlot() == 31){
                if(e.isRightClick()){
                    e.getWhoClicked().openInventory(getInvPercent(ModerrCaseConstants.getCase(ModerrCaseEnum.SKAZENIA)));
                    return;
                }
                if(u.getUserChestStorage().hasItem(new StorageItemKey(StorageItemType.Chest, ModerrCaseEnum.SKAZENIA), 1) && u.getUserChestStorage().hasItem(new StorageItemKey(StorageItemType.Key, ModerrCaseEnum.SKAZENIA), 1)){
                    OpenCase((Player) e.getWhoClicked(), ModerrCaseConstants.getCase(ModerrCaseEnum.SKAZENIA));
                    u.getUserChestStorage().subtractChest(new StorageItemKey(StorageItemType.Chest, ModerrCaseEnum.SKAZENIA), 1);
                    u.getUserChestStorage().subtractKey(new StorageItemKey(StorageItemType.Key, ModerrCaseEnum.SKAZENIA), 1);
                }else{
                    e.getWhoClicked().sendMessage(ColorUtils.color("&8[!] &cNie posiadasz potrzebnych przedmiotów"));
                    e.getWhoClicked().sendMessage(ColorUtils.color("&8[!] &eAby otworzyć skrzynke potrzebna jest skrzynia i klucz"));
                }
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
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(e.getPlayer().isSneaking()){
                return;
            }
            Location loc1 = new Location(Bukkit.getWorld("world"), 219, 66, -44);
            Location loc2 = e.getClickedBlock().getLocation();
            if(loc1.getBlockX() == loc2.getBlockX() &&
                    loc1.getBlockY() == loc2.getBlockY() &&
                    loc1.getBlockZ() == loc2.getBlockZ()){
                /*if(!e.getPlayer().isOp()){
                    e.getPlayer().sendMessage(ColorUtils.color("&cMożliwość otwierania skrzyń będzie niedługo!"));
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
                    return;
                }*/
                Player p = e.getPlayer();
                p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN,1,0.6f);
                p.openInventory(getInv(UserManager.getUser(p.getUniqueId())));
            /*OpeningChest(e.getPlayer(), new OpeningChest() {
                @Override
                public String name() {
                    return ColorUtils.color("&6&lTEST");
                }
            });*/
            }
        }
    }

}
