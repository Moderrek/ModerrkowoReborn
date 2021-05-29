package pl.moderr.moderrkowo.reborn.mysql;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;
import pl.moderr.moderrkowo.reborn.utils.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private Connection connection;
    public String host, database, username, password;
    public int port;

    public MySQLQuery query;

    public void mysqlSetup() {
        host = "mysql.titanaxe.com";
        port = 3306;
        database = "srv162810";
        username = "srv162810";
        password = "dikbluJD";

        try {

            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    return;
                }
                Class.forName("com.mysql.cj.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":"
                        + this.port + "/" + this.database, this.username, this.password));
                Logger.logDatabaseMessage(ColorUtils.color("&aPołączono"));
                query = new MySQLQuery(this);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!UserManager.isUserLoaded(p.getUniqueId())) {
                        UserManager.loadUser(p);
                        Logger.logDatabaseMessage(ColorUtils.color("&a+ &2" + p.getName() + " &azostał wczytany"));
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void disable() {
        for (User u : UserManager.loadedUsers.values()) {
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
