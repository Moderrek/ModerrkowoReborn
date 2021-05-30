package pl.moderr.moderrkowo.reborn.mysql;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import pl.moderr.moderrkowo.reborn.Main;
import pl.moderr.moderrkowo.reborn.opening.data.UserChestStorage;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;
import pl.moderr.moderrkowo.reborn.utils.Logger;
import pl.moderr.moderrkowo.reborn.villagers.data.PlayerVillagersData;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class UserManager {

    public static Map<UUID, User> loadedUsers = new HashMap<>();

    @Contract(pure = true)
    public static boolean isUserLoaded(UUID uuid) {
        return loadedUsers.containsKey(uuid);
    }

    public static User getUser(UUID uuid) {
        if (!isUserLoaded(uuid)) {
            if (Bukkit.getPlayer(uuid) != null && Objects.requireNonNull(Bukkit.getPlayer(uuid)).isOnline()) {
                loadUser(Bukkit.getPlayer(uuid));
            }
        }
        return loadedUsers.get(uuid);
    }

    public static void loadUser(Player p) {
        UUID uuid = p.getUniqueId();
        if (isUserLoaded(uuid)) {
            return;
        }
        User u = null;
        try {
            if (!Main.getMySQL().getQuery().userExists(uuid)) {
                Main.getMySQL().getQuery().insertUser(uuid, p.getName());
                u = new User(uuid, 0, 0, new PlayerVillagersData(), new UserChestStorage(new HashMap<>()));
                p.getInventory().addItem(new ItemStack(Material.STONE_AXE));
                p.getInventory().addItem(new ItemStack(Material.OAK_LOG,2));
                p.getInventory().addItem(new ItemStack(Material.BREAD, 16));
                p.sendMessage(ColorUtils.color("&aZostałeś pomyślnie zarejestrowany!"));
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,1);
            } else {
                Main.getMySQL().getQuery().updateLastSeen(uuid);
                u = new User(uuid, Main.getMySQL().getQuery().getMoney(uuid), Main.getMySQL().getQuery().getExp(uuid), Main.getMySQL().getQuery().getQuestData(uuid), Main.getMySQL().getQuery().getUserChestStorage(uuid));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (u != null) {
            loadedUsers.put(uuid, u);
            try{
                u.UpdateScoreboard();
            }catch(Exception e){
                e.printStackTrace();
            }
            Logger.logDatabaseMessage("Wczytano gracza");
        }
    }

    public static void saveUser(User user){
        try {
            Main.getMySQL().getQuery().setMoney(user.getPlayer().getUniqueId(), user.getMoney());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            Main.getMySQL().getQuery().setExp(user.getPlayer().getUniqueId(), user.getExp());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            Main.getMySQL().getQuery().setQuestData(user.getPlayer().getUniqueId(), user.getVillagersData());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            Main.getMySQL().getQuery().setUserChestStorageData(user.getPlayer().getUniqueId(), user.getUserChestStorage());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static void unloadUser(UUID uuid) {
        if (isUserLoaded(uuid)) {
            User u = loadedUsers.get(uuid);
            try {
                Main.getMySQL().getQuery().setMoney(uuid, u.getMoney());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                Main.getMySQL().getQuery().setExp(uuid, u.getExp());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                Main.getMySQL().getQuery().setQuestData(uuid, u.getVillagersData());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                Main.getMySQL().getQuery().setUserChestStorageData(uuid, u.getUserChestStorage());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            loadedUsers.remove(uuid, u);
            Logger.logDatabaseMessage("Odczytano gracza");
            u = null;
        }
    }

}
