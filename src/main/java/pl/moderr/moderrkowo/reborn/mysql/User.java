package pl.moderr.moderrkowo.reborn.mysql;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.Contract;
import pl.moderr.moderrkowo.reborn.Main;
import pl.moderr.moderrkowo.reborn.utils.ChatUtil;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;
import pl.moderr.moderrkowo.reborn.villagers.data.*;

import java.util.UUID;

public class User {

    private final UUID uuid;
    private final PlayerVillagersData villagersData;
    private int money;
    private int exp;

    @Contract(pure = true)
    public User(UUID uuid, int money, int exp, PlayerVillagersData villagersData) {
        this.uuid = uuid;
        this.money = money;
        this.exp = exp;
        this.oldLevel = getLevel();
        this.villagersData = villagersData;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
        UpdateScoreboard();
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
        UpdateScoreboard();
    }

    public void addExp(int exp) {
        this.exp += exp;
        if(getLevel() > oldLevel){
            oldLevel = getLevel();
            getPlayer().sendMessage(ColorUtils.color("  "));
            getPlayer().sendMessage(ColorUtils.color("  "));
            getPlayer().sendMessage(ColorUtils.color("  &eGratulacje odblokowałeś &c" + getLevel() + " poziom &epostaci!"));
            getPlayer().sendMessage(ColorUtils.color("  "));
            getPlayer().sendMessage(ColorUtils.color("  "));
            getPlayer().spawnParticle(Particle.TOTEM, getPlayer().getLocation().getX(), getPlayer().getLocation().getY(), getPlayer().getLocation().getZ(), 20, 1, 1, 1, 0.1f);
            getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_WITHER_SHOOT, 1,2);
        }
        UpdateScoreboard();
    }

    public void subtractExp(int exp) {
        this.exp += exp;
        UpdateScoreboard();
    }

    public void addMoney(int money) {
        this.money += money;
        UpdateScoreboard();
    }

    public void subtractMoney(int money) {
        this.money -= money;
        UpdateScoreboard();
    }

    public boolean hasMoney(int money) {
        return this.money >= money;
    }

    public PlayerVillagersData getVillagersData() {
        return villagersData;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public String getName() {
        return getPlayer().getName();
    }

    public void UpdateScoreboard(){
        ScoreboardManager sm = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = sm.getNewScoreboard();
        Objective objective;
        objective = scoreboard.registerNewObjective(getName(), "dummy", ColorUtils.color("&e&lModerrkowo"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        PlayerVillagerData data = null;
        try {
            for (PlayerVillagerData villagers : getVillagersData().getVillagersData().values()) {
                if (villagers.isActiveQuest()) {
                    data = villagers;
                    break;
                }
            }
        } catch (Exception ignored) {

        }
        Score score1 = objective.getScore(" ");
        Score score2 = objective.getScore(ColorUtils.color("&9" + getName()));
        Score score3 = objective.getScore(ColorUtils.color("&fPoziom: &c" + getLevel()));
        Score score4 = objective.getScore(ColorUtils.color("&fPortfel: &6" + ChatUtil.getMoney(getMoney())));
        Score score5 = objective.getScore(ColorUtils.color("&fCzas gry: &a" + ChatUtil.getTicksToTime(getPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE))));
        Score score8 = objective.getScore(ColorUtils.color("&6moderrkowo.pl"));
        if (data == null) {
            Score score6 = objective.getScore(ColorUtils.color("&fAktywny quest: &abrak"));
            Score score7 = objective.getScore("  ");
            score1.setScore(-1);
            score2.setScore(-2);
            score3.setScore(-3);
            score4.setScore(-4);
            score5.setScore(-5);
            score6.setScore(-6);
            score7.setScore(-7);
            score8.setScore(-8);
        } else {
            if (!Main.getInstance().villagerManager.villagers.containsKey(data.getVillagerId())) {
                Score score6 = objective.getScore(ColorUtils.color("&fAktywny quest: &abrak"));
                Score score7 = objective.getScore("  ");
                score1.setScore(-1);
                score2.setScore(-2);
                score3.setScore(-3);
                score4.setScore(-4);
                score5.setScore(-5);
                score6.setScore(-6);
                score7.setScore(-7);
                score8.setScore(-8);
                getPlayer().setScoreboard(scoreboard);
                return;
            }
            Quest q = Main.getInstance().villagerManager.villagers.get(data.getVillagerId()).getQuests().get(data.getQuestIndex());
            Score score7 = objective.getScore(ColorUtils.color("&fAktywny quest: &a" + q.getName()));
            int itemI = 0;
            int last = 0;
            for (int i = -7; i != -7 - Main.getInstance().villagerManager.villagers.get(data.getVillagerId()).getQuests().get(data.getQuestIndex()).getQuestItems().size(); i--) {
                try {
                    IQuestItem iQuestItem = q.getQuestItems().get(itemI);
                    if(iQuestItem instanceof IQuestItemCollect){
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
                    if(iQuestItem instanceof IQuestItemPay){
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
            score5.setScore(-5);
            score7.setScore(last-1);
            score9.setScore(last-2);
            score8.setScore(last-3);
        }
        getPlayer().setScoreboard(scoreboard);
    }

    private int oldLevel;
    public int getLevel(int pd){
        if(pd >= 244){
            return 5;
        }
        if(pd >= 100){
            return 4;
        }
        if(pd >= 75){
            return 3;
        }
        if(pd >= 49){
            return 2;
        }
        return 1;
    }
    public int getLevel(){
        return getLevel(this.exp);
    }
}
