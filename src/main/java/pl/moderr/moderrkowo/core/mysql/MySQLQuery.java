package pl.moderr.moderrkowo.core.mysql;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.npc.data.data.PlayerNPCSData;
import pl.moderr.moderrkowo.core.ranks.Rank;
import pl.moderr.moderrkowo.core.ranks.StuffRank;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;
import pl.moderr.moderrkowo.core.utils.RandomUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.*;
import java.util.*;

public class MySQLQuery {

    public MySQL mySQL;

    public ArrayList<UUID> ranking = new ArrayList<UUID>() {
        {
            add(UUID.fromString("987a444c-6855-4c32-9464-73819e56283d"));
            add(UUID.fromString("aa76bff5-d36f-42dc-ad8c-1a50fce756d7"));
            add(UUID.fromString("9b3c334c-8d25-4963-8da3-6742b1445b0a"));
            add(UUID.fromString("3e5362e8-c9a2-4899-af01-0c09ee0d92ed"));
            add(UUID.fromString("b1d5190c-dbca-4ab5-8651-f68af02ab75c"));
            add(UUID.fromString("2254eadc-c106-4b02-bb73-8719711668a2"));
            add(UUID.fromString("235a1f35-cdd0-41bb-a1b3-f3b79108cdae"));
            add(UUID.fromString("4413ed00-d860-416a-bc44-e76cf911121e"));
            add(UUID.fromString("236aa8ff-7d77-4bae-9d01-4153e22c9992"));
            add(UUID.fromString("b7c34c8b-c8a8-41fa-8d85-39cf09eeb7c5"));
        }
    };

    @Contract(pure = true)
    public MySQLQuery(MySQL mySQL) {
        this.mySQL = mySQL;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            try {
                World w = Bukkit.getWorld("spawn");
                String SQL = "SELECT `NAME`,`MONEY` FROM `" + mySQL.usersTable + "` ORDER BY `" + mySQL.usersTable + "`.`MONEY` DESC LIMIT 10";
                PreparedStatement statement = mySQL.getConnection().prepareStatement(SQL);
                statement.execute();
                ResultSet rs = statement.getResultSet();
                int i = 9;
                try {
                    for (int j = 0; j < ranking.size(); j++) {
                        Entity e = w.getEntity(ranking.get(j));
                        e.setCustomName(ColorUtils.color("&f#" + (10 - j) + " &7Brak"));
                        e.teleport(new Location(w, 424.5d, 81.0d + (j * 0.3d), -419.0d));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                while (rs.next()) {
                    try {
                        Entity e = w.getEntity(ranking.get(i));
                        e.setCustomName(ColorUtils.color("&f#" + (10 - i) + " &7" + rs.getString("NAME") + " &6" + ChatUtil.getMoney(rs.getDouble("MONEY"))));
                    } catch (Exception e) {
                        Logger.logAdminLog("Nie udało się zaaktualizować rankingu");
                        e.printStackTrace();
                    }
                    i--;
                }
            } catch (SQLException throwables) {
                Logger.logAdminLog("Nie udało się zaaktualizować rankingu");
                throwables.printStackTrace();
            }
        }, 0, 20 * 60 * 15);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
            if (Main.getInstance().dataConfig.getInt("wplncheckid") == 0) {
                Main.getInstance().dataConfig.set("wplncheckid", 1);
                try {
                    Main.getInstance().dataConfig.save(Main.getInstance().dataFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
            JSONObject jsonObject;
            try {
                jsonObject = readJsonFromUrl(new URL("https://tipo.live/api/v2/payments/"), new LinkedHashMap<>() {
                    {

                        //?token=CdKLsY6SL8vq31jF17aN&status=completed&paid_at_min_timestamp=" + Main.getInstance().dataConfig.getString("wplncheck")
                        put("token", "CdKLsY6SL8vq31jF17aN");
                        put("min_id", Main.getInstance().dataConfig.getString("wplncheckid"));
                    }
                });
                System.out.println(jsonObject.toString());
                //System.out.println("=============================================");
                JSONArray array = jsonObject.getJSONArray("data");
                //System.out.println("=============================================");
                //GregorianCalendar gc = new GregorianCalendar();
                //gc.setTime(new java.util.Date());
                int highestId = 0;
                for (int i = 0; i < array.length(); i++) {
                    int id = array.getJSONObject(i).getInt("id");
                    if (highestId < id) {
                        highestId = id;
                    }
                }
                System.out.println(highestId);
                System.out.println(array.toString());

                if (array.length() > 0) {
                    Main.getInstance().dataConfig.set("wplncheckid", highestId + 1);
                    Main.getInstance().dataConfig.save(Main.getInstance().dataFile);
                }

                for (int i = 0; i < array.length(); i++) {
                    JSONObject wpln = array.getJSONObject(i);
                    double d = Main.getMySQL().getQuery().getWPLN(wpln.getString("name"));
                    d += (double) wpln.getInt("amount") / 100;
                    Main.getMySQL().getQuery().setWPLN(wpln.getString("name"), d);
                    if (Bukkit.getPlayer(wpln.getString("name")) != null) {
                        Player p = Bukkit.getPlayer(wpln.getString("name"));
                        p.sendMessage(ColorUtils.color("&7Otrzymano &6" + ChatUtil.getNumber((double) wpln.getInt("amount") / 100) + " wPLN!"));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1);
                        Bukkit.broadcastMessage(" ");
                        Bukkit.broadcastMessage(ColorUtils.color(" &6> &e" + p.getName() + " &7zakupił\n   &6" + (double) wpln.getInt("amount") / 100 + " wPLN!"));
                        Bukkit.broadcastMessage(" ");
                    } else {
                        Bukkit.broadcastMessage(" ");
                        Bukkit.broadcastMessage(ColorUtils.color(" &6> &e" + wpln.getString("name") + " &7zakupił\n   &6" + (double) wpln.getInt("amount") / 100 + " wPLN!"));
                        Bukkit.broadcastMessage(" ");
                    }
                }
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
            //new java.sql.Date(Calendar.getInstance().getTime().getTime());

        }, 0, 20 * 60 * 15);
    }


    private static String readAll(URL url, LinkedHashMap<String, String> params) throws IOException {
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        String urlParameters = postData.toString();
        System.out.println(url.toString() + "?" + urlParameters);
        String url2 = url.toString() + "?" + urlParameters;

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url2);

        // add request header
        request.addHeader("User-Agent", "Mozilla/5.0");

        HttpResponse response = client.execute(request);

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result.toString());
        return result.toString();
    }

    public static JSONObject readJsonFromUrl(URL url, LinkedHashMap<String, String> params) throws IOException, JSONException {
        return new JSONObject(readAll(url, params));
    }

    public void Query(String SQL) {
        try {
            Statement stmt = mySQL.getConnection().createStatement();
            stmt.execute(SQL);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Logger.logDatabaseMessage(ColorUtils.color("&cNie udało się wykonać zapytania"));
        }
    }

    // Global User
    public boolean userExists(UUID uuid) throws SQLException {
        String SQL = "SELECT `UUID` FROM `" + mySQL.usersTable + "` WHERE `UUID`=?";
        PreparedStatement preparedStatement = mySQL.getConnection().prepareStatement(SQL);
        preparedStatement.setString(1, uuid.toString());
        ResultSet rs = preparedStatement.executeQuery();
        return rs.next();
    }

    public void insertUser(User user) throws SQLException {
        String SQLWPLN = "INSERT INTO `" + mySQL.virtualPln + "`(`NICK`,`AMOUNT`) VALUES (?,?)";
        PreparedStatement stmtWPLN = mySQL.getConnection().prepareStatement(SQLWPLN);
        stmtWPLN.setString(1, user.getName());
        stmtWPLN.setDouble(2, 0);
        stmtWPLN.execute();
        PreparedStatement endCoins = mySQL.getConnection().prepareStatement("SELECT `ENDCOINS` FROM `players` WHERE `NAME`=?");
        endCoins.setString(1, user.getName());
        ResultSet rs = endCoins.executeQuery();
        if (rs.next()) {
            user.setSeasonOneCoins(rs.getInt("ENDCOINS") * RandomUtils.getRandomInt(1, 3));
        }
        String SQL = "INSERT INTO `" + mySQL.usersTable + "`(`UUID`,`NAME`,`MONEY`,`SEASON_ONE_COINS`,`RANK`,`STUFF_RANK`,`LEVELS`,`QUEST_DATA`,`LAST_SEEN`,`REGISTERED`,`SIDEBAR`,`PLAYING_TIME`,`VERSION`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = mySQL.getConnection().prepareStatement(SQL);
        Date now = new Date(Calendar.getInstance().getTime().getTime());
        preparedStatement.setString(1, user.getUniqueId().toString());
        preparedStatement.setString(2, user.getName());
        preparedStatement.setDouble(3, user.getMoney());
        preparedStatement.setInt(4, user.getSeasonOneCoins());
        preparedStatement.setString(5, user.getRank().toString());
        preparedStatement.setString(6, user.getStuffRank().toString());
        preparedStatement.setString(7, user.getUserLevel().toString());
        preparedStatement.setString(8, user.getNPCSData().toString());
        preparedStatement.setDate(9, now);
        preparedStatement.setDate(10, now);
        preparedStatement.setBoolean(11, true);
        preparedStatement.setInt(12, 0);
        preparedStatement.setString(13, user.getVersion());
        preparedStatement.execute();
        Logger.logDatabaseMessage("Utworzono nowego użytkownika");
    }

    public User getUser(UUID uuid) throws SQLException {
        String SQL = "SELECT * FROM `" + mySQL.usersTable + "` WHERE `UUID`=?";
        PreparedStatement preparedStatement = mySQL.getConnection().prepareStatement(SQL);
        preparedStatement.setString(1, uuid.toString());
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return new User(
                    UUID.fromString(rs.getString("UUID")),
                    rs.getString("NAME"),
                    rs.getDouble("MONEY"),
                    rs.getInt("SEASON_ONE_COINS"),
                    Rank.valueOf(rs.getString("RANK")),
                    StuffRank.valueOf(rs.getString("STUFF_RANK")),
                    new UserLevel(rs.getString("LEVELS")),
                    new PlayerNPCSData(rs.getString("QUEST_DATA")),
                    rs.getDate("REGISTERED"),
                    rs.getBoolean("SIDEBAR"),
                    rs.getInt("PLAYING_TIME"),
                    rs.getString("VERSION")
            );
        } else {
            return null;
        }
    }

    public void updateUser(User user) throws SQLException {
        String SQL = "UPDATE `" + mySQL.usersTable + "` SET `MONEY`=?, `SEASON_ONE_COINS`=?, `RANK`=?, `STUFF_RANK`=?,`LEVELS`=?, `QUEST_DATA`=?, `SIDEBAR`=?, `PLAYING_TIME`=?, `VERSION`=? WHERE `UUID`=?";
        PreparedStatement preparedStatement = mySQL.getConnection().prepareStatement(SQL);
        preparedStatement.setDouble(1, user.getMoney());
        preparedStatement.setInt(2, user.getSeasonOneCoins());
        preparedStatement.setString(3, user.getRank().toString());
        preparedStatement.setString(4, user.getStuffRank().toString());
        preparedStatement.setString(5, user.getUserLevel().toString());
        preparedStatement.setString(6, user.getNPCSData().ToJson());
        preparedStatement.setBoolean(7, user.isSidebar());
        preparedStatement.setInt(8, user.getPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE));
        preparedStatement.setString(9, user.getVersion());
        preparedStatement.setString(10, user.getUniqueId().toString());
        preparedStatement.execute();
    }

    // Last seen date
    public void updateLastSeen(UUID uuid) throws SQLException {
        String SQL = "UPDATE `" + mySQL.usersTable + "` SET `LAST_SEEN`=? WHERE `UUID`=?";
        PreparedStatement preparedStatement = mySQL.getConnection().prepareStatement(SQL);
        preparedStatement.setDate(1, new Date(Calendar.getInstance().getTime().getTime()));
        preparedStatement.setString(2, uuid.toString());
        preparedStatement.executeUpdate();
    }

    // MONEY
    public void setMoney(UUID uuid, double money) throws SQLException {
        String SQL = "UPDATE `" + mySQL.usersTable + "` SET `MONEY`=? WHERE `UUID`=?";
        PreparedStatement preparedStatement = mySQL.getConnection().prepareStatement(SQL);
        preparedStatement.setDouble(1, money);
        preparedStatement.setString(2, uuid.toString());
        preparedStatement.executeUpdate();
    }

    public double getMoney(UUID uuid) throws SQLException {
        String SQL = "SELECT `MONEY` FROM `" + mySQL.usersTable + "` WHERE `UUID`=?";
        PreparedStatement preparedStatement = mySQL.getConnection().prepareStatement(SQL);
        preparedStatement.setString(1, uuid.toString());
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getDouble("MONEY");
        } else {
            return 0;
        }
    }

    // VIRTUALPLN
    public void setWPLN(String nick, double wPLN) throws SQLException {
        String SQL = "UPDATE `" + mySQL.virtualPln + "` SET `AMOUNT`=? WHERE `NICK`=?";
        PreparedStatement preparedStatement = mySQL.getConnection().prepareStatement(SQL);
        preparedStatement.setDouble(1, wPLN);
        preparedStatement.setString(2, nick);
        preparedStatement.executeUpdate();
    }

    public double getWPLN(String nick) throws SQLException {
        String SQL = "SELECT `AMOUNT` FROM `" + mySQL.virtualPln + "` WHERE `NICK`=?";
        PreparedStatement preparedStatement = mySQL.getConnection().prepareStatement(SQL);
        preparedStatement.setString(1, nick);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getDouble("AMOUNT");
        } else {
            String SQLWPLN = "INSERT INTO `" + mySQL.virtualPln + "`(`NICK`,`AMOUNT`) VALUES (?,?)";
            PreparedStatement stmtWPLN = mySQL.getConnection().prepareStatement(SQLWPLN);
            stmtWPLN.setString(1, nick);
            stmtWPLN.setDouble(2, 0);
            stmtWPLN.execute();
            return 0;
        }
    }

    public int getCountOfUsers() {
        try (Statement statement = mySQL.getConnection().createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM `" + mySQL.usersTable + "`");
            rs.next();
            return rs.getInt(1);
        } catch (Exception e) {
            return 0;
        }
    }
}
