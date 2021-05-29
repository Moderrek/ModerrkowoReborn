package pl.moderr.moderrkowo.reborn.mysql;

import org.jetbrains.annotations.Contract;
import pl.moderr.moderrkowo.reborn.utils.Logger;
import pl.moderr.moderrkowo.reborn.villagers.data.PlayerVillagersData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.UUID;

public class MySQLQuery {

    public MySQL mySQL;

    @Contract(pure = true)
    public MySQLQuery(MySQL mySQL) {
        this.mySQL = mySQL;
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
        String SQL = "INSERT INTO `players`(`UUID`,`NAME`, `RANK`, `EXP`, `MONEY`, `QUEST`, `LASTSEEN`, `FIRSTJOIN`) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = mySQL.getConnection().prepareStatement(SQL);
        preparedStatement.setString(1, uuid.toString());
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, "None");
        preparedStatement.setInt(4, 0);
        preparedStatement.setInt(5, 0);
        preparedStatement.setString(6, new PlayerVillagersData().toString());
        preparedStatement.setDate(7, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
        preparedStatement.setDate(8, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
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
}
