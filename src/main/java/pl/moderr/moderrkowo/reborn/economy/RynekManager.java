package pl.moderr.moderrkowo.reborn.economy;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.moderr.moderrkowo.reborn.Main;
import pl.moderr.moderrkowo.reborn.mysql.User;
import pl.moderr.moderrkowo.reborn.mysql.UserManager;
import pl.moderr.moderrkowo.reborn.utils.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class RynekManager implements Listener {

    public final String RynekGui_WithoutPage = ColorUtils.color("&6Rynek graczy &7- &eStrona ");
    public final String RynekGUI_Name = ColorUtils.color("&6Rynek graczy &7- &eStrona %s");
    public final String RynekTableName = "rynekItems";

    public final ArrayList<RynekItem> rynekItems = new ArrayList<>();


    public RynekManager() {
        load();
    }
    public void load() {
        Logger.logAdminLog("Wczytywanie przedmiotów na rynku...");
        int total = 0;
        int i = 0;
        try {
            rynekItems.clear();
            String sqlGet = String.format("SELECT * FROM `%s`", RynekTableName);
            Statement stmt = Main.getMySQL().getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sqlGet);
            if (rs == null) {
                Logger.logAdminLog("Nie wczytano rynku, ponieważ nie ma żadnych przedmiotów");
                return;
            }
            while (rs.next()) {
                total++;
                try {
                    rynekItems.add(new RynekItem(UUID.fromString(rs.getString("UUID")), JsonItemStack.fromJson(rs.getString("ITEM")), rs.getInt("COST")));
                    i++;
                } catch (Exception e) {
                    Logger.logAdminLog("Wystąpił problem z pobraniem przedmiotem na rynku");
                    e.printStackTrace();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            Logger.logAdminLog("Wystąpił problem podczas ładowania rynku");
        }
        Logger.logAdminLog("Wczytano przedmioty na rynku (" + i + "/" + total + ")");
    }
    public void save() throws SQLException {
        Logger.logAdminLog("Zapisywanie przedmiotów na rynku...");
        String sql = String.format("TRUNCATE TABLE %s", RynekTableName);
        Statement stmt = Main.getMySQL().getConnection().createStatement();
        stmt.execute(sql);
        int temp = 0;
        for (RynekItem i : rynekItems) {
            String SQL = String.format("INSERT INTO `%s` (`UUID`,`ITEM`,`COST`) VALUES (?,?,?)", RynekTableName);
            PreparedStatement preparedStatement = Main.getMySQL().getConnection().prepareStatement(SQL);
            preparedStatement.setString(1, i.owner.toString());
            preparedStatement.setString(2, JsonItemStack.toJson(i.item));
            preparedStatement.setInt(3, i.cost);
            preparedStatement.execute();
            temp++;
        }
        Logger.logAdminLog("Zapisano przedmioty na rynku (" + temp + ")");
    }
    public void buyItem(User buyer, RynekItem item) {
        // Seller earn
        Player seller = Bukkit.getPlayer(item.owner);
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pl-PL"));
        nf.setMaximumFractionDigits(2);
        if (seller != null) {
                User sellerU = UserManager.getUser(seller.getUniqueId());
                sellerU.addMoney(item.cost);
                buyer.subtractMoney(item.cost);

                if (Objects.requireNonNull(buyer.getPlayer()).getInventory().firstEmpty() != -1) {
                    buyer.getPlayer().getInventory().addItem(item.item.clone());
                } else {
                    buyer.getPlayer().getLocation().getWorld().dropItem(buyer.getPlayer().getLocation(), item.item.clone());
                }
                rynekItems.remove(item);
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (players.getOpenInventory().getTitle().contains(RynekGui_WithoutPage)) {
                        players.openInventory(getRynekInventory(Integer.parseInt(players.getOpenInventory().getTitle().replace(RynekGui_WithoutPage, "")), players));
                    }
                }
                buyer.getPlayer().sendMessage(ColorUtils.color("&aPomyślnie zakupiono przedmiot z rynku od " + sellerU.getName()));
                buyer.getPlayer().sendMessage(ColorUtils.color("&c- " + nf.format(item.cost) + " zł"));
                buyer.getPlayer().playSound(buyer.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                buyer.getPlayer().sendMessage(ColorUtils.color("&aPrzelew zakończony powodzeniem"));
                seller.sendMessage(ColorUtils.color("&9Rynek &8> &6" + buyer.getName() + " &7zakupił przedmiot od Ciebie"));
                seller.sendMessage(ColorUtils.color("&9Rynek &8> &a+ " + nf.format(item.cost) + " zł"));
                ModerrkowoLog.LogAdmin(ColorUtils.color("&6" + buyer.getName() + " &7kupił &6" + item.item.getType() + " &7od &6" + sellerU.getName() + " &7za &6" + nf.format(item.cost) + " zł"));
        } else {
            OfflinePlayer offlineSeller = Bukkit.getOfflinePlayer(item.owner);
            Objects.requireNonNull(buyer.getPlayer()).sendMessage(ColorUtils.color("&aTrwa wysyłanie przelewu..."));
            try {
                int kwota = Main.getMySQL().getQuery().getMoney(item.owner);
                kwota += item.cost;
                Main.getMySQL().getQuery().setMoney(item.owner, kwota);
                if (buyer.getPlayer().getInventory().firstEmpty() != -1) {
                    buyer.getPlayer().getInventory().addItem(item.item.clone());
                } else {
                    buyer.getPlayer().getLocation().getWorld().dropItem(buyer.getPlayer().getLocation(), item.item.clone());
                }
                rynekItems.remove(item);
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (players.getOpenInventory().getTitle().contains(RynekGui_WithoutPage)) {
                        players.openInventory(getRynekInventory(Integer.parseInt(players.getOpenInventory().getTitle().replace(RynekGui_WithoutPage, "")), players));
                    }
                }
                buyer.getPlayer().sendMessage(ColorUtils.color("&aPomyślnie zakupiono przedmiot z rynku od " + offlineSeller.getName()));
                buyer.getPlayer().sendMessage(ColorUtils.color("&c- " + nf.format(item.cost) + " zł"));
                buyer.getPlayer().playSound(buyer.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                buyer.getPlayer().sendMessage(ColorUtils.color("&aPrzelew zakończony powodzeniem"));
                ModerrkowoLog.LogAdmin(ColorUtils.color("&6" + buyer.getName() + " &7kupił &6" + item.item.getType() + " &7od &6" + Bukkit.getOfflinePlayer(item.owner).getName() + " &7za &6" + nf.format(item.cost) + " zł"));
            } catch (SQLException throwables) {
                buyer.getPlayer().sendMessage(ColorUtils.color("&cPrzelew zakończony niepowodzeniem"));
                throwables.printStackTrace();
            }
        }
        // Seller earn
    }
    public void addItem(RynekItem item) {
        rynekItems.add(item);
    }
    public RynekItem getItem(int page, int slot) {
        ArrayList<RynekItem> pageItems = getPage(page);
        if (pageItems.size() < (slot + 1)) {
            return null;
        }
        return pageItems.get(slot);
    }
    public ItemStack getItemOfItemRynek(RynekItem item, Player player) {
        ItemStack i = item.item.clone();
        ItemMeta meta = i.getItemMeta();
        assert meta != null;
        ArrayList<String> lore = (ArrayList<String>) meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pl-PL"));
        nf.setMaximumFractionDigits(2);
        DecimalFormat df = (DecimalFormat) nf;
        if (item.owner.equals(player.getUniqueId())) {
            lore.add(ColorUtils.color("&7Cena: &6" + df.format(item.cost) + " zł"));
            lore.add(ColorUtils.color("&7Wystawione przez: &c" + "Ciebie"));
            lore.add(" ");
            lore.add(ColorUtils.color("&cKliknij aby usunąć z rynku"));
        } else {
            lore.add(ColorUtils.color("&7Cena: &6" + df.format(item.cost) + " zł"));
            lore.add(ColorUtils.color("&7Wystawione przez: &6" + Bukkit.getOfflinePlayer(item.owner).getName()));
            lore.add(" ");
            lore.add(ColorUtils.color("&8Kliknij aby kupić"));
        }
        if (meta.hasDisplayName()) {
            meta.setDisplayName(meta.getDisplayName());
        } else {
            meta.setDisplayName(ColorUtils.color("&6" + materialName(i.getType())));
        }
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }
    public int getPages() {
        int viewItems = 0;
        int pages = 0;
        while (rynekItems.size() > viewItems) {
            viewItems += 45;
            pages++;
        }
        return pages;
    }
    public ArrayList<RynekItem> getPage(int page) {
        int startItem = (page - 1) * 45;
        ArrayList<RynekItem> pageList = new ArrayList<>();
        for (int i = startItem; i != startItem + 45; i++) {
            try {
                pageList.add(rynekItems.get(i));
            } catch (Exception ignored) {

            }
        }
        return pageList;
    }
    public Inventory getRynekInventory(int page, Player p) {
        int size = 54 - 1;
        ArrayList<RynekItem> pageItems = getPage(page);
        int availablePages = getPages();
        Inventory inv = Bukkit.createInventory(null, 54, String.format(RynekGUI_Name, page + ""));
        for (int i = size - 8; i != size + 1; i++) {
            inv.setItem(i, ItemStackUtils.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, " "));
        }
        if (page > 1) {
            inv.setItem(size - 8, ItemStackUtils.createGuiItem(Material.ARROW, 1, ColorUtils.color("&cPoprzednia strona")));
        }
        if (page < availablePages) {
            inv.setItem(size, ItemStackUtils.createGuiItem(Material.ARROW, 1, ColorUtils.color("&cNastępna strona")));
        }
        //inv.setItem(size - 4, ItemStackUtils.createGuiItem(Material.DIAMOND, 1, ColorUtils.color("&aTwoje oferty")));
        if (pageItems.size() > 0) {
            for (int i = 0; i != pageItems.size(); i++) {
                inv.setItem(i, getItemOfItemRynek(pageItems.get(i), p));
            }
        }
        return inv;
    }
    public String materialName(Material material) {
        String materialName = material.toString();
        materialName = materialName.replaceAll("_", " ");
        materialName = materialName.toLowerCase();
        return WordUtils.capitalizeFully(materialName);
    }
    @EventHandler
    public void onRynekClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        if (e.getClickedInventory() == null) {
            return;
        }
        if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            if (e.getView().getTitle().contains(RynekGui_WithoutPage)) {
                e.setCancelled(true);
                return;
            }
            return;
        }
        if (e.getView().getTitle().contains(RynekGui_WithoutPage)) {
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            User u;

            try {
                u = UserManager.getUser(p.getUniqueId());
            } catch (Exception userNotLoaded) {
                userNotLoaded.printStackTrace();
                p.sendMessage(ColorUtils.color("&cWystąpił błąd. Jeżeli się powtarza, wyjdź i wejdź na serwer."));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }

            //<editor-fold> Page switcher
            String title = e.getView().getTitle().replace(RynekGui_WithoutPage, "");
            int page = Integer.parseInt(title);
            if (e.getSlot() == 54 - 1) {
                if (Objects.requireNonNull(e.getClickedInventory().getItem(e.getSlot())).getType().equals(Material.ARROW)) {
                    page++;
                    p.openInventory(getRynekInventory(page, p));
                }
            }
            if (e.getSlot() == 54 - 9) {
                if (Objects.requireNonNull(e.getClickedInventory().getItem(e.getSlot())).getType().equals(Material.ARROW)) {
                    page--;
                    p.openInventory(getRynekInventory(page, p));
                }
            }
            //</editor-fold> Page switcher

            //<editor-fold> Buy items
            RynekItem item = getItem(page, e.getSlot());
            if (item == null) {
                return;
            }
            if (item.owner.equals(p.getUniqueId())) {
                if (p.getInventory().firstEmpty() != -1)
                {
                    p.getInventory().addItem(item.item.clone());
                } else {
                    p.getWorld().dropItem(p.getLocation(), item.item.clone());
                }
                rynekItems.remove(item);
                p.sendMessage(ColorUtils.color("&aPomyślnie zwrócono przedmiot z rynku."));
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (players.getOpenInventory().getTitle().contains(RynekGui_WithoutPage)) {
                        players.openInventory(getRynekInventory(Integer.parseInt(players.getOpenInventory().getTitle().replace(RynekGui_WithoutPage, "")), players));
                    }
                }
            } else {
                if (u.getMoney() >= item.cost) {
                    buyItem(u, item);
                } else {
                    p.sendMessage(ColorUtils.color("&cNie posiadasz tyle pieniędzy!"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                }
            }
            //</editor-fold> Buy items

        }
    }

}
