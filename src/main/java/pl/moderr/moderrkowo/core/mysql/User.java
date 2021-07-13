package pl.moderr.moderrkowo.core.mysql;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.notifications.MNotification;
import pl.moderr.moderrkowo.core.notifications.NotifycationType;
import pl.moderr.moderrkowo.core.npc.data.data.PlayerNPCData;
import pl.moderr.moderrkowo.core.npc.data.data.PlayerNPCSData;
import pl.moderr.moderrkowo.core.npc.data.quest.Quest;
import pl.moderr.moderrkowo.core.npc.data.tasks.*;
import pl.moderr.moderrkowo.core.ranks.Rank;
import pl.moderr.moderrkowo.core.ranks.RankManager;
import pl.moderr.moderrkowo.core.ranks.StuffRank;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.UUID;

public class User {
    // VARIABLES
    private final UUID _UUID;
    private final String _NAME;
    private final PlayerNPCSData _QUEST_DATA;
    private final Date _REGISTERED;
    private double _MONEY;
    private int _SEASON_ONE_COINS;
    private Rank _RANK;
    private StuffRank _STUFF_RANK;
    private UserLevel _LEVELS;
    private boolean _SIDEBAR;
    private int _PLAYING_TIME;
    private String _VERSION;

    // CONSTRUCTOR
    public User(UUID _UUID, String _NAME, double _MONEY, int _SEASON_ONE_COINS, Rank _RANK, StuffRank _STUFF_RANK, UserLevel _LEVELS, PlayerNPCSData _QUEST_DATA, Date _REGISTERED, boolean _SIDEBAR, int _PLAYING_TIME, String _VERSION) {
        this._UUID = _UUID;
        this._NAME = _NAME;
        this._MONEY = _MONEY;
        this._SEASON_ONE_COINS = _SEASON_ONE_COINS;
        this._RANK = _RANK;
        this._STUFF_RANK = _STUFF_RANK;
        this._LEVELS = _LEVELS;
        if (_LEVELS.getOwner() == null) {
            _LEVELS.setOwner(this);
        }
        this._QUEST_DATA = _QUEST_DATA;
        this._REGISTERED = _REGISTERED;
        this._SIDEBAR = _SIDEBAR;
        this._PLAYING_TIME = _PLAYING_TIME;
        this._VERSION = _VERSION;
    }

    //Notifications
    public void LoadNotifications() {
        try {
            String sqlGet = "SELECT * FROM `" + Main.getMySQL().notificationTable + "` WHERE `OWNER`=?";
            PreparedStatement stmt = Main.getMySQL().getConnection().prepareStatement(sqlGet);
            stmt.setString(1, _UUID.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs == null) {
                return;
            }
            while (rs.next()) {
                try {
                    MNotification notify = new MNotification(UUID.fromString(rs.getString("ID")), true, UUID.fromString(rs.getString("OWNER")), NotifycationType.valueOf(rs.getString("TYPE")), rs.getString("DATA"));
                    getPlayer().sendMessage(ColorUtils.color(notify.getData()));
                    notify.Delete();
                } catch (Exception e) {
                    Logger.logAdminLog("Wystąpił problem z powiadomieniem");
                    e.printStackTrace();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            Logger.logAdminLog("Wystąpił problem podczas ładowania powiadomieniu");
        }
    }

    // MONEY
    public double getMoney() {
        return _MONEY;
    }

    public void setMoney(double money) {
        this._MONEY = money;
        if (_SIDEBAR) {
            UpdateScoreboard();
        } else {
            getPlayer().sendMessage(ColorUtils.color("&7= " + ChatUtil.getMoney(money)));
        }
    }

    public void addMoney(double money) {
        this._MONEY += money;
        if (_SIDEBAR) {
            UpdateScoreboard();
        } else {
            getPlayer().sendMessage(ColorUtils.color("&a+ " + ChatUtil.getMoney(money)));
        }
    }

    public void subtractMoney(double money) {
        this._MONEY -= money;
        if (_SIDEBAR) {
            UpdateScoreboard();
        } else {
            getPlayer().sendMessage(ColorUtils.color("&c- " + ChatUtil.getMoney(money)));
        }
    }

    public boolean hasMoney(double money) {
        return this._MONEY >= money;
    }

    public void addExp(LevelCategory category, double exp) {
        _LEVELS.get(category).addExp(exp);
    }

    /*// EXP
    public int getExp() {
        return Exp;
    }
    public void setExp(int exp) {
        this.Exp = exp;
        UpdateScoreboard();
    }
    public void addExp(int exp) {
        this.Exp += exp;
        if(getLevel() > oldLevel){
            oldLevel = getLevel();
            getPlayer().sendMessage(ColorUtils.color("  "));
            getPlayer().sendMessage(ColorUtils.color("  "));
            getPlayer().sendMessage(ColorUtils.color("  &fGratulacje odblokowałeś &a" + getLevel() + " poziom &fpostaci!"));
            getPlayer().sendMessage(ColorUtils.color("  "));
            getPlayer().sendMessage(ColorUtils.color("  "));
            getPlayer().sendTitle(ColorUtils.color("&6POSTAĆ"), ColorUtils.color("&7[&a" + (oldLevel-1) + "&7] → [&a" + oldLevel + "&7]"));
            getPlayer().spawnParticle(Particle.TOTEM, getPlayer().getLocation().getX(), getPlayer().getLocation().getY(), getPlayer().getLocation().getZ(), 20, 1, 1, 1, 0.1f);
            getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_WITHER_SHOOT, 1,2);
        }
        UpdateScoreboard();
    }
    public void subtractExp(int exp) {
        this.Exp += exp;
        UpdateScoreboard();
    }
    // LEVEL
    public int getLevel(int pd) {
        if (pd >= 390) {
            return 6;
        }
        if (pd >= 244) {
            return 5;
        }
        if (pd >= 100) {
            return 4;
        }
        if (pd >= 75) {
            return 3;
        }
        if (pd >= 49) {
            return 2;
        }
        return 1;
    }
    public int getLevel(){
        return getLevel(this.Exp);
    }*/
    // SEASON ONE COINS
    public int getSeasonOneCoins() {
        return _SEASON_ONE_COINS;
    }

    public void setSeasonOneCoins(int seasonOneCoins) {
        this._SEASON_ONE_COINS = seasonOneCoins;
    }

    public void addSeasonOneCoins(int seasonOneCoins) {
        this._SEASON_ONE_COINS += seasonOneCoins;
    }

    public void subtractSeasonOneCoins(int seasonOneCoins) {
        this._SEASON_ONE_COINS -= seasonOneCoins;
    }

    public boolean hasSeasonOneCoins(int seasonOneCoins) {
        return this._SEASON_ONE_COINS >= seasonOneCoins;
    }

    // QUEST DATA
    public PlayerNPCSData getNPCSData() {
        return _QUEST_DATA;
    }

    // PLAYER
    public UUID getUniqueId() {
        return _UUID;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(_UUID);
    }

    public String getName() {
        return _NAME;
    }

    // SCOREBOARD
    public void UpdateScoreboard() {
        if (!_SIDEBAR) {
            return;
        }
        ScoreboardManager sm = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = sm.getNewScoreboard();
        Objective objective;
        objective = scoreboard.registerNewObjective("Moderrkowo", "dummy", ColorUtils.color("&6⚔ Moderrkowo ⚔"), RenderType.INTEGER);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        PlayerNPCData data = null;
        try {
            for (PlayerNPCData villagers : getNPCSData().getNPCSData().values()) {
                if (villagers.isActiveQuest()) {
                    data = villagers;
                    break;
                }
            }
        } catch (Exception ignored) {

        }
        Score score1 = objective.getScore(ColorUtils.color(RankManager.getRankNameShort(_RANK, true) + "&9" + _NAME));
        Score score2 = objective.getScore(ColorUtils.color("&fPostać: &c" + _LEVELS.playerLevel() + " lvl"));
        Score score3 = objective.getScore(ColorUtils.color("&fPortfel: &6" + ChatUtil.getMoney(getMoney())));
        Score score4 = objective.getScore(ColorUtils.color("&fCzas gry: &a" + ChatUtil.getTicksToTime(getPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE))));
        Score score7 = objective.getScore(ColorUtils.color("&6moderrkowo.pl"));
        if (data == null) {
            Score score5 = objective.getScore(ColorUtils.color("&fAktywny quest: &abrak"));
            Score score6 = objective.getScore("  ");
            score1.setScore(-1);
            score2.setScore(-2);
            score3.setScore(-3);
            score4.setScore(-4);
            score5.setScore(-5);
            score6.setScore(-6);
            score7.setScore(-7);
        } else {
            if (!Main.getInstance().NPCManager.npcs.containsKey(data.getNpcId())) {
                Score score5 = objective.getScore(ColorUtils.color("&fAktywny quest: &abrak"));
                Score score6 = objective.getScore("  ");
                score1.setScore(-1);
                score2.setScore(-2);
                score3.setScore(-3);
                score4.setScore(-4);
                score5.setScore(-5);
                score6.setScore(-6);
                score7.setScore(-7);
                getPlayer().setScoreboard(scoreboard);
                return;
            }
            Quest q = Main.getInstance().NPCManager.npcs.get(data.getNpcId()).getQuests().get(data.getQuestIndex());
            Score score6 = objective.getScore(ColorUtils.color("&fAktywny quest: &a" + q.getName()));
            int itemI = 0;
            int last = 0;
            for (int i = -6; i != -6 - Main.getInstance().NPCManager.npcs.get(data.getNpcId()).getQuests().get(data.getQuestIndex()).getQuestItems().size(); i--) {
                try {
                    IQuestItem iQuestItem = q.getQuestItems().get(itemI);
                    if (iQuestItem instanceof IQuestItemCollect) {
                        IQuestItemCollect item = (IQuestItemCollect) iQuestItem;
                        int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                        Score tempScore;
                        if (temp >= item.getCount()) {
                            tempScore = objective.getScore(ColorUtils.color("&a✔ " + item.getQuestItemPrefix() + " " + item.getCount() + " " + ChatUtil.materialName(item.getMaterial())));
                        } else {
                            int count = item.getCount() - temp;
                            if (count > item.getCount()) {
                                count = item.getCount();
                            }
                            tempScore = objective.getScore(ColorUtils.color("&c✘ " + item.getQuestItemPrefix() + " " + count + " " + ChatUtil.materialName(item.getMaterial())));
                        }
                        tempScore.setScore(i);
                    }
                    if (iQuestItem instanceof IQuestItemFish) {
                        IQuestItemFish item = (IQuestItemFish) iQuestItem;
                        int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                        Score tempScore;
                        if (temp >= item.getCount()) {
                            tempScore = objective.getScore(ColorUtils.color("&a✔ " + item.getQuestItemPrefix() + " " + item.getCount() + " " + ChatUtil.materialName(item.getMaterial())));
                        } else {
                            int count = item.getCount() - temp;
                            if (count > item.getCount()) {
                                count = item.getCount();
                            }
                            tempScore = objective.getScore(ColorUtils.color("&c✘ " + item.getQuestItemPrefix() + " " + count + " " + ChatUtil.materialName(item.getMaterial())));
                        }
                        tempScore.setScore(i);
                    }
                    if (iQuestItem instanceof IQuestItemPay) {
                        IQuestItemPay item = (IQuestItemPay) iQuestItem;
                        int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                        Score tempScore;
                        if (temp >= item.getCount()) {
                            tempScore = objective.getScore(ColorUtils.color("&a✔ " + item.getQuestItemPrefix() + " " + ChatUtil.getMoney(item.getCount())));
                        } else {
                            int count = item.getCount() - temp;
                            if (count > item.getCount()) {
                                count = item.getCount();
                            }
                            tempScore = objective.getScore(ColorUtils.color("&c✘ " + item.getQuestItemPrefix() + " " + ChatUtil.getMoney(count)));
                        }
                        tempScore.setScore(i);
                    }
                    if(iQuestItem instanceof IQuestItemBreak){
                        IQuestItemBreak item = (IQuestItemBreak) iQuestItem;
                        int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                        Score tempScore;
                        if (temp >= item.getCount()) {
                            tempScore = objective.getScore(ColorUtils.color("&a✔ " + item.getQuestItemPrefix() + " " + item.getCount() + " " + ChatUtil.materialName(item.getMaterial())));
                        } else {
                            int count = item.getCount() - temp;
                            if (count > item.getCount()) {
                                count = item.getCount();
                            }
                            tempScore = objective.getScore(ColorUtils.color("&c✘ " + item.getQuestItemPrefix() + " " + count + " " + ChatUtil.materialName(item.getMaterial())));
                        }
                        tempScore.setScore(i);
                    }
                    if (iQuestItem instanceof IQuestItemGive) {
                        IQuestItemGive item = (IQuestItemGive) iQuestItem;
                        int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                        Score tempScore;
                        if (temp >= item.getCount()) {
                            tempScore = objective.getScore(ColorUtils.color("&a✔ " + item.getQuestItemPrefix() + " " + item.getCount() + " " + ChatUtil.materialName(item.getMaterial())));
                        } else {
                            int count = item.getCount() - temp;
                            if (count > item.getCount()) {
                                count = item.getCount();
                            }
                            tempScore = objective.getScore(ColorUtils.color("&c✘ " + item.getQuestItemPrefix() + " " + count + " " + ChatUtil.materialName(item.getMaterial())));
                        }
                        tempScore.setScore(i);
                    }
                    if (iQuestItem instanceof IQuestItemCraft) {
                        IQuestItemCraft item = (IQuestItemCraft) iQuestItem;
                        int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                        Score tempScore;
                        if (temp >= item.getCount()) {
                            tempScore = objective.getScore(ColorUtils.color("&a✔ " + item.getQuestItemPrefix() + " " + item.getCount() + " " + ChatUtil.materialName(item.getMaterial())));
                        } else {
                            int count = item.getCount() - temp;
                            if (count > item.getCount()) {
                                count = item.getCount();
                            }
                            tempScore = objective.getScore(ColorUtils.color("&c✘ " + item.getQuestItemPrefix() + " " + count + " " + ChatUtil.materialName(item.getMaterial())));
                        }
                        tempScore.setScore(i);
                    }
                    if (iQuestItem instanceof IQuestItemKill) {
                        IQuestItemKill item = (IQuestItemKill) iQuestItem;
                        int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                        Score tempScore;
                        if (temp >= item.getCount()) {
                            tempScore = objective.getScore(ColorUtils.color("&a✔ " + item.getQuestItemPrefix() + " " + item.getCount() + " " + ChatUtil.materialName(item.getEntityType())));
                        } else {
                            int count = item.getCount() - temp;
                            if (count > item.getCount()) {
                                count = item.getCount();
                            }
                            tempScore = objective.getScore(ColorUtils.color("&c✘ " + item.getQuestItemPrefix() + " " + count + " " + ChatUtil.materialName(item.getEntityType())));
                        }
                        tempScore.setScore(i);
                    }
                    if (iQuestItem instanceof IQuestItemBreed) {
                        IQuestItemBreed item = (IQuestItemBreed) iQuestItem;
                        int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                        Score tempScore;
                        if (temp >= item.getCount()) {
                            tempScore = objective.getScore(ColorUtils.color("&a✔ " + item.getQuestItemPrefix() + " " + item.getCount() + " " + ChatUtil.materialName(item.getEntityType().toEntity())));
                        } else {
                            int count = item.getCount() - temp;
                            if (count > item.getCount()) {
                                count = item.getCount();
                            }
                            tempScore = objective.getScore(ColorUtils.color("&c✘ " + item.getQuestItemPrefix() + " " + count + " " + ChatUtil.materialName(item.getEntityType().toEntity())));
                        }
                        tempScore.setScore(i);
                    }
                    if (iQuestItem instanceof IQuestItemFishingRod) {
                        IQuestItemFishingRod item = (IQuestItemFishingRod) iQuestItem;
                        int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                        Score tempScore;
                        if (temp >= item.getCount()) {
                            tempScore = objective.getScore(ColorUtils.color("&a✔ " + item.getQuestItemPrefix() + " " + item.getCount() + " razy"));
                        } else {
                            int count = item.getCount() - temp;
                            if (count > item.getCount()) {
                                count = item.getCount();
                            }
                            tempScore = objective.getScore(ColorUtils.color("&c✘ " + item.getQuestItemPrefix() + " " + count + " razy"));
                        }
                        tempScore.setScore(i);
                    }
                    if (iQuestItem instanceof IQuestItemVisit) {
                        IQuestItemVisit item = (IQuestItemVisit) iQuestItem;
                        int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                        Score tempScore;
                        if (temp >= 1) {
                            tempScore = objective.getScore(ColorUtils.color("&a✔ " + item.getQuestItemPrefix() + " " + ChatUtil.materialName(item.getBiome())));
                        } else {
                            int count = 1 - temp;
                            if (count > 1) {
                                count = 1;
                            }
                            tempScore = objective.getScore(ColorUtils.color("&c✘ " + item.getQuestItemPrefix() + " " + ChatUtil.materialName(item.getBiome())));
                        }
                        tempScore.setScore(i);
                    }
                    itemI++;
                    last = i;
                } catch (Exception exception) {
                    System.out.println("Exception  >> " + getName());
                    exception.printStackTrace();
                }
            }
            Score score9 = objective.getScore("  ");
            score1.setScore(-1);
            score2.setScore(-2);
            score3.setScore(-3);
            score4.setScore(-4);
            score6.setScore(-5);
            score9.setScore(last - 1);
            score7.setScore(last - 2);
        }
        getPlayer().setScoreboard(scoreboard);
    }

    // Rank
    public Rank getRank() {
        return _RANK;
    }

    public void setRank(Rank _RANK) {
        this._RANK = _RANK;
    }

    // StuffRank
    public StuffRank getStuffRank() {
        return _STUFF_RANK;
    }

    public void setStuffRank(StuffRank _STUFF_RANK) {
        this._STUFF_RANK = _STUFF_RANK;
    }

    // UserLevel
    public UserLevel getUserLevel() {
        return _LEVELS;
    }

    public void setUserLevel(UserLevel _LEVELS) {
        this._LEVELS = _LEVELS;
    }

    // Registered
    public Date getRegistered() {
        return _REGISTERED;
    }

    // Sidebar
    public boolean isSidebar() {
        return _SIDEBAR;
    }

    public void setSidebar(boolean _SIDEBAR) {
        this._SIDEBAR = _SIDEBAR;
    }

    // Playing Time
    public int getPlayingTime() {
        return _PLAYING_TIME;
    }

    public void setPlayingTime(int _PLAYING_TIME) {
        this._PLAYING_TIME = _PLAYING_TIME;
    }

    // Version
    public String getVersion() {
        return _VERSION;
    }

    public void setVersion(String _VERSION) {
        this._VERSION = _VERSION;
    }

    public boolean hasRank(Rank rank) {
        return RankManager.getPriority(this._RANK) >= RankManager.getPriority(rank);
    }

    public boolean hasRank(int rank) {
        return RankManager.getPriority(this._RANK) >= rank;
    }
}
