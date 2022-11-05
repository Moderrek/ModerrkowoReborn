package pl.moderr.moderrkowo.core.mysql;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;

public class MySQL {

    private Connection connection;
    public String host, database, username, password;
    public int port;

    public MySQLQuery query;

    public final String homesTable = "homes";
    public final String usersTable = "users";
    public final String rynekTable = "marketplace";
    public final String pvpShieldTable = "pvpshield";
    public final String notificationTable = "notifications";
    public final String rewardTable = "rewards";
    public final String virtualPln = "virtualpln";

    public void enable() {
        host = "host :)";
        port = 3306;
        database = "database";
        username = "user";
        password = "pass";
        try {
            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    return;
                }
                String url = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?useSSL=false";
                Class.forName("com.mysql.cj.jdbc.Driver");
                setConnection(DriverManager.getConnection(url, this.username, this.password));
                Logger.logDatabaseMessage(ColorUtils.color("&aPołączono"));
                query = new MySQLQuery(this);

                query.Query("create table if not exists `" + rynekTable + "` (OWNER text not null, ITEM text not null, COST double not null, `EXPIRE` text not null);");
                query.Query("create table if not exists `" + usersTable + "` (UUID text not null, NAME text not null, MONEY double not null, SEASON_ONE_COINS int not null, `RANK` text not null, STUFF_RANK text not null, `LEVELS` text not null, QUEST_DATA text not null, LAST_SEEN date not null, REGISTERED date not null, SIDEBAR boolean not null, PLAYING_TIME int not null, `VERSION` text not null);");
                query.Query("create table if not exists `" + homesTable + "` (UUID text not null, WORLD text not null, X double not null, Y double not null, Z double not null, YAW FLOAT not null, PITCH FLOAT not null);");
                query.Query("create table if not exists `" + pvpShieldTable + "` (`UUID` text not null, `WHEN_END` datetime not null);");
                query.Query("create table if not exists `" + notificationTable + "` (`ID` text not null,`OWNER` text not null,`TYPE` text not null,`DATA` text not null);");
                query.Query("create table if not exists `" + rewardTable + "` (`UUID` text not null, `ITEM` text not null);");
                query.Query("create table if not exists `" + virtualPln + "` (`NICK` text not null, `AMOUNT` double not null);");
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!UserManager.isUserLoaded(p.getUniqueId())) {
                        UserManager.loadUser(p);
                        Logger.logDatabaseMessage(ColorUtils.color("&a+ &2" + p.getName() + " &azostał wczytany"));
                    }
                }
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                    Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
                        int count = UserManager.loadedUsers.values().size();
                        Logger.logAdminLog("&eAutomatyczne zapisywanie użytkowników [" + count + "]");
                        int i = 1;
                        for (User u : UserManager.loadedUsers.values()) {
                            try {
                                query.updateUser(u);
                                u.getPlayer().sendActionBar(ColorUtils.color("&a✔ Zapisano dane."));
                                Logger.logAdminLog("&a✔. Zapisano &2" + u.getName() + " &f[" + i + "/" + count + "]");
                                i++;
                            } catch (SQLException exception) {
                                Logger.logAdminLog("&c✘. Nie udało się zapisać " + u.getName());
                                i++;
                                u.getPlayer().sendActionBar(ColorUtils.color("&c✘ Nie udało się zapisać danych."));
                                u.getPlayer().sendMessage(ColorUtils.color("&eAutomatyczny zapis\n&cWystąpił błąd podczas zapisywania danych zgłoś się do administracji"));
                                u.getPlayer().playSound(u.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_HURT, 2, 1);
                                exception.printStackTrace();
                            }


                        }
                    }, 0, 20 * 60 * 15);
                }, 20 * 60);
            }
        } catch (SQLException | ClassNotFoundException e) {
            Logger.logDatabaseMessage(ColorUtils.color("&cWystąpił problem SQL podczas łączenia"));
            e.printStackTrace();
        }
    }
    public void disable() {
        for (Iterator<User> i = UserManager.loadedUsers.values().iterator(); i.hasNext(); ) {
            User u = i.next();
            UserManager.unloadUser(u.getPlayer().getUniqueId());
            Logger.logDatabaseMessage(ColorUtils.color("&c- &4" + u.getName() + " &czostał odczytany"));
        }
    }

    public MySQLQuery getQuery() {
        return query;
    }

    public Connection getConnection() {
        return connection;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

}
