package pl.moderr.moderrkowo.reborn.mysql;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.Contract;
import pl.moderr.moderrkowo.reborn.Main;
import pl.moderr.moderrkowo.reborn.opening.data.UserChestStorage;
import pl.moderr.moderrkowo.reborn.utils.ChatUtil;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;
import pl.moderr.moderrkowo.reborn.utils.Logger;
import pl.moderr.moderrkowo.reborn.villagers.data.PlayerVillagersData;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class MySQLQuery {

    public MySQL mySQL;

    public ArrayList<UUID> ranking = new ArrayList<UUID>(){
        {
            add(UUID.fromString("d29c0225-2e25-44f2-aed9-7bcaecd25ba3"));
            add(UUID.fromString("adf57814-8e23-47fc-a025-33d353eb6799"));
            add(UUID.fromString("c8190bc8-91b3-43e0-a975-de4271618e95"));
            add(UUID.fromString("d0f19b18-9c8d-4a93-bcd1-4c40de5e623d"));
            add(UUID.fromString("1a1e7e59-7270-4b2f-91f4-2fa79b20ec3e"));
            add(UUID.fromString("b7be038f-8a40-4f25-8053-9040b28df99d"));
            add(UUID.fromString("c3b55fd8-9bc1-4df3-b90d-17f156f5dde6"));
            add(UUID.fromString("4479b156-1e28-487a-9c35-58959771606d"));
            add(UUID.fromString("0bdca9c7-7a60-45e6-80e0-1be7c6e338ad"));
            add(UUID.fromString("9d268995-501f-452d-ad9c-af4f7aa26846"));
        }
    };

    @Contract(pure = true)
    public MySQLQuery(MySQL mySQL) {
        this.mySQL = mySQL;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            try {
                World w = Bukkit.getWorld("world");
                String SQL = "SELECT `NAME`,`MONEY` FROM `players` ORDER BY `players`.`MONEY` DESC LIMIT 10";
                PreparedStatement statement = mySQL.getConnection().prepareStatement(SQL);
                statement.execute();
                ResultSet rs = statement.getResultSet();
                int i = 9;
                while(rs.next()){
                    try{
                        Entity e = w.getEntity(ranking.get(i));
                        e.setCustomName(ColorUtils.color("&c" + (10-i) + ". &e" + rs.getString("NAME") + " &c" + ChatUtil.getMoney(rs.getInt("MONEY"))));
                        e.teleport(new Location(w, 201.5d,63.5d+(i*0.3d),-29.0d));
                    }catch(Exception e){
                        Logger.logAdminLog("Nie udało się zaaktualizować rankingu");
                        e.printStackTrace();
                    }
                    i--;
                }
            } catch (SQLException throwables) {
                Logger.logAdminLog("Nie udało się zaaktualizować rankingu");
                throwables.printStackTrace();
            }
        }, 0, 20*60*15);
    }

    // Global User
    public boolean userExists(UUID uuid) throws SQLException {
        String SQL = "SELECT `UUID` FROM `players` WHERE `UUID`=?";
        PreparedStatement preparedStatement = mySQL.getConnection().prepareStatement(SQL);
        preparedStatement.setString(1, uuid.toString());
        ResultSet rs = preparedStatement.executeQuery();
        return rs.next();
    }

    public void insertUser(UUID uuid, String name) throws SQLException {
        String SQL = "INSERT INTO `players`(`UUID`,`NAME`, `RANK`, `EXP`, `MONEY`, `QUEST`, `CHEST_STORAGE`, `LASTSEEN`, `FIRSTJOIN`) VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = mySQL.getConnection().prepareStatement(SQL);
        preparedStatement.setString(1, uuid.toString());
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, "None");
        preparedStatement.setInt(4, 0);
        preparedStatement.setInt(5, 0);
        preparedStatement.setString(6, new PlayerVillagersData().toString());
        preparedStatement.setString(7, new UserChestStorage(new HashMap<>()).toString());
        preparedStatement.setDate(8, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
        preparedStatement.setDate(9, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
        preparedStatement.execute();
        Logger.logDatabaseMessage("Utworzono nowego użytkownika");
    }

    // Last seen date
    public void updateLastSeen(UUID uuid) throws SQLException {
        String SQL = "UPDATE `players` SET `LASTSEEN`=? WHERE `UUID`=?";
        PreparedStatement preparedStatement = mySQL.getConnection().prepareStatement(SQL);
        preparedStatement.setDate(1, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
        preparedStatement.setString(2, uuid.toString());
        preparedStatement.executeUpdate();
    }

    // MONEY
    public void setMoney(UUID uuid, int money) throws SQLException {
        String SQL = "UPDATE `players` SET `MONEY`=? WHERE `UUID`=?";
        PreparedStatement preparedStatement = mySQL.getConnection().prepareStatement(SQL);
        preparedStatement.setInt(1, money);
        preparedStatement.setString(2, uuid.toString());
        preparedStatement.executeUpdate();
    }

    public int getMoney(UUID uuid) throws SQLException {
        String SQL = "SELECT `MONEY` FROM `players` WHERE `UUID`=?";
        PreparedStatement preparedStatement = mySQL.getConnection().prepareStatement(SQL);
        preparedStatement.setString(1, uuid.toString());
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getInt("MONEY");
        } else {
            return 0;
        }
    }

    // EXP
    public void setExp(UUID uuid, int exp) throws SQLException {
        String SQL = "UPDATE `players` SET `EXP`=? WHERE `UUID`=?";
        PreparedStatement preparedStatement = mySQL.getConnection().prepareStatement(SQL);
        preparedStatement.setInt(1, exp);
        preparedStatement.setString(2, uuid.toString());
        preparedStatement.executeUpdate();
    }

    public int getExp(UUID uuid) throws SQLException {
        String SQL = "SELECT `EXP` FROM `players` WHERE `UUID`=?";
        PreparedStatement preparedStatement = mySQL.getConnection().prepareStatement(SQL);
        preparedStatement.setString(1, uuid.toString());
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getInt("EXP");
        } else {
            return 0;
        }
    }

    public void setUserChestStorageData(UUID uuid, UserChestStorage data) throws SQLException {
        String SQL = "UPDATE `players` SET `CHEST_STORAGE`=? WHERE `UUID`=?";
        PreparedStatement preparedStatement = mySQL.getConnection().prepareStatement(SQL);
        preparedStatement.setString(1, data.toString());
        preparedStatement.setString(2, uuid.toString());
        preparedStatement.executeUpdate();
    }

    // QUEST
    public void setQuestData(UUID uuid, PlayerVillagersData data) throws SQLException {
        String SQL = "UPDATE `players` SET `QUEST`=? WHERE `UUID`=?";
        PreparedStatement preparedStatement = mySQL.getConnection().prepareStatement(SQL);
        preparedStatement.setString(1, data.ToJson());
        preparedStatement.setString(2, uuid.toString());
        preparedStatement.executeUpdate();
    }

    public PlayerVillagersData getQuestData(UUID uuid) throws SQLException {
        String SQL = "SELECT `QUEST` FROM `players` WHERE `UUID`=?";
        PreparedStatement preparedStatement = mySQL.getConnection().prepareStatement(SQL);
        preparedStatement.setString(1, uuid.toString());
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return new PlayerVillagersData(rs.getString("QUEST"));
        } else {
            return null;
        }
    }

    public UserChestStorage getUserChestStorage(UUID uuid) throws SQLException {
        String SQL = "SELECT `CHEST_STORAGE` FROM `players` WHERE `UUID`=?";
        PreparedStatement preparedStatement = mySQL.getConnection().prepareStatement(SQL);
        preparedStatement.setString(1, uuid.toString());
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return new UserChestStorage(rs.getString("CHEST_STORAGE"));
        } else {
            return null;
        }
    }
}
