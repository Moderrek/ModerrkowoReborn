package pl.moderr.moderrkowo.reborn;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.Contract;
import pl.moderr.moderrkowo.reborn.antylogout.AntyLogoutManager;
import pl.moderr.moderrkowo.reborn.automessage.ModerrkowoAutoMessage;
import pl.moderr.moderrkowo.reborn.commands.RankingCommand;
import pl.moderr.moderrkowo.reborn.commands.admin.*;
import pl.moderr.moderrkowo.reborn.commands.user.information.CraftingDzialkaCommand;
import pl.moderr.moderrkowo.reborn.commands.user.information.DiscordCommand;
import pl.moderr.moderrkowo.reborn.commands.user.messages.HelpopCommand;
import pl.moderr.moderrkowo.reborn.commands.user.messages.MessageCommand;
import pl.moderr.moderrkowo.reborn.commands.user.messages.ReplyCommand;
import pl.moderr.moderrkowo.reborn.commands.user.teleportation.TPACommand;
import pl.moderr.moderrkowo.reborn.commands.user.teleportation.TPAccept;
import pl.moderr.moderrkowo.reborn.commands.user.teleportation.TPDeny;
import pl.moderr.moderrkowo.reborn.commands.user.weather.PogodaCommand;
import pl.moderr.moderrkowo.reborn.cuboids.CuboidsManager;
import pl.moderr.moderrkowo.reborn.listeners.*;
import pl.moderr.moderrkowo.reborn.timevoter.TimeVoter;
import pl.moderr.moderrkowo.reborn.utils.ChatUtil;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;
import pl.moderr.moderrkowo.reborn.utils.HexResolver;
import pl.moderr.moderrkowo.reborn.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public final class Main extends JavaPlugin {

    private static Main instance;
    public TimeVoter timeVoter;
    public CuboidsManager cuboidsManager;
    //<editor-fold> AntyLogout
    public AntyLogoutManager instanceAntyLogout;
    //</editor-fold>
    //<editor-fold> YML
    public File dataFile = new File(getDataFolder(), "data.yml");
    public FileConfiguration dataConfig;
    //</editor-fold>
    //<editor-fold> AutoMessage
    public ModerrkowoAutoMessage autoMessage;

    @Contract(pure = true)
    public static Main getInstance() {
        return instance;
    }

    public static String getServerName() {
        return ColorUtils.color(HexResolver.parseHexString(Main.getInstance().getConfig().getString("servername")));
    }

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        Logger.logPluginMessage("Wczytywanie wtyczki Main");
        instance = this;
        LoadPluginConfig();
        LoadDataYML();
        initializeListeners();
        initializeAntyLogout();
        initializeAutoMessage();
        initializeCommands();
        startScoreboard();
        timeVoter = new TimeVoter();
        Logger.logPluginMessage("Wczytano głosowanie czasu");
        cuboidsManager = new CuboidsManager();
        cuboidsManager.Start();
        Logger.logPluginMessage("Wczytano działki");
        Logger.logPluginMessage("Wczytano plugin w &8(&a" + (System.currentTimeMillis() - start) + "ms&8)");
    }

    @Override
    public void onDisable() {
        SaveDataYML();
        Logger.logPluginMessage("Wyłączono plugin");
    }

    private void initializeAntyLogout() {
        instanceAntyLogout = new AntyLogoutManager();
        Bukkit.getPluginManager().registerEvents(instanceAntyLogout, this);
        Logger.logPluginMessage("Wczytano AntyLogout");
    }

    //</editor-fold>
    //<editor-fold> Config
    private void LoadPluginConfig() {
        //<editor-fold> Config
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveConfig();
        Logger.logPluginMessage("Wczytano config");
        //</editor-fold> Config
    }

    //</editor-fold>
    //<editor-fold> Commands
    public void initializeCommands() {
        Objects.requireNonNull(getCommand("playerid")).setExecutor(new PlayerIDCommand());
        Objects.requireNonNull(getCommand("adminchat")).setExecutor(new AdminChatCommand());
        Objects.requireNonNull(getCommand("ahelpop")).setExecutor(new AHelpopCommand());
        Objects.requireNonNull(getCommand("chat")).setExecutor(new ChatCommand());
        Objects.requireNonNull(getCommand("endersee")).setExecutor(new EnderseeCommand());
        Objects.requireNonNull(getCommand("fly")).setExecutor(new FlyCommand());
        Objects.requireNonNull(getCommand("gamemode")).setExecutor(new GameModeCommand());
        Objects.requireNonNull(getCommand("invsee")).setExecutor(new InvseeCommand());
        Objects.requireNonNull(getCommand("mban")).setExecutor(new MBanCommand());
        Objects.requireNonNull(getCommand("mkick")).setExecutor(new MKickCommand());
        Objects.requireNonNull(getCommand("nazwa")).setExecutor(new NazwaCommand());
        Objects.requireNonNull(getCommand("say")).setExecutor(new SayCommand());
        Objects.requireNonNull(getCommand("sendalert")).setExecutor(new SendAlertCommand());
        Objects.requireNonNull(getCommand("vanish")).setExecutor(new VanishCommand());

        Objects.requireNonNull(getCommand("ranking")).setExecutor(new RankingCommand());
        Objects.requireNonNull(getCommand("craftingdzialka")).setExecutor(new CraftingDzialkaCommand());
        Objects.requireNonNull(getCommand("discord")).setExecutor(new DiscordCommand());
        Objects.requireNonNull(getCommand("helpop")).setExecutor(new HelpopCommand());
        Objects.requireNonNull(getCommand("message")).setExecutor(new MessageCommand());
        Objects.requireNonNull(getCommand("reply")).setExecutor(new ReplyCommand());
        Objects.requireNonNull(getCommand("tpa")).setExecutor(new TPACommand());
        Objects.requireNonNull(getCommand("tpaccept")).setExecutor(new TPAccept());
        Objects.requireNonNull(getCommand("tpdeny")).setExecutor(new TPDeny());
        Objects.requireNonNull(getCommand("pogoda")).setExecutor(new PogodaCommand());

        Logger.logPluginMessage("Wczytano komendy");
    }

    //</editor-fold>
    //<editor-fold> Listeners
    public void initializeListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new MotdListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new CropBreakListener(), this);
        Bukkit.getPluginManager().registerEvents(new PogodaCommand(), this);
        Bukkit.getPluginManager().registerEvents(new TNTListener(), this);
        Logger.logPluginMessage("Wczytano listenery");
    }

    private void LoadDataYML() {
        //<editor-fold> Data.yml
        if (!dataFile.exists()) {
            saveResource("data.yml", false);
        }
        dataFile = new File(getDataFolder(), "data.yml");
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        Logger.logPluginMessage("Wczytano rekord graczy");
        //</editor-fold> Data.yml
    }

    private void SaveDataYML() {
        try {
            dataConfig.save(dataFile);
            Logger.logAdminLog("Zapisano data.yml");
        } catch (IOException e) {
            Logger.logAdminLog("Wystąpił błąd podczas zapisywania data.yml");
        }
    }
    //</editor-fold>

    //</editor-fold> YML
    //<editor-fold> Scoreboard
    public void startScoreboard() {
        //<editor-fold> Scoreboard
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                ScoreboardManager sm = Bukkit.getScoreboardManager();
                Scoreboard scoreboard = sm.getNewScoreboard();
                Objective objective;
                objective = scoreboard.registerNewObjective(p.getName(), "dummy", ColorUtils.color("&6&lModerrkowo"));
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                Score score1 = objective.getScore(" ");
                Score score2 = objective.getScore(ColorUtils.color("&9" + p.getName()));
                Score score3 = objective.getScore(ColorUtils.color("&fPortfel: &6" + ChatUtil.getMoney(0)));
                Score score4 = objective.getScore(ColorUtils.color("&fCzas gry: &a" + ChatUtil.getTicksToTime(p.getStatistic(Statistic.PLAY_ONE_MINUTE))));
                Score score5 = objective.getScore("  ");
                Score score6 = objective.getScore(ColorUtils.color("&6moderrkowo.pl"));
                score1.setScore(-1);
                score2.setScore(-2);
                score3.setScore(-3);
                score4.setScore(-4);
                score5.setScore(-5);
                score6.setScore(-6);
                p.setScoreboard(scoreboard);
            }
        }, 0, 20 * 15);
        Logger.logPluginMessage("Wczytano scoreboard");
        //</editor-fold> Scoreboard
    }

    public void initializeAutoMessage() {
        autoMessage = new ModerrkowoAutoMessage(this, 60 * 5, new ArrayList<String>() {
            {
                add("Wszystko może być zbugowane/posiadać błędy");
                add("Przewidywana nowa edycja 1 Maj");
                add("Jeżeli zobaczysz jakiś błąd zgłoś go na /report");
                add("Twój kolega jest daleko? Możesz się do niego teleportować! /tpa");
            }
        });
        Logger.logPluginMessage("Wczytano AutoMessage");
    }
}
