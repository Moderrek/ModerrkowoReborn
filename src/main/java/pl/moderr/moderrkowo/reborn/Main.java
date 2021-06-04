package pl.moderr.moderrkowo.reborn;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import pl.moderr.moderrkowo.reborn.antylogout.AntyLogoutManager;
import pl.moderr.moderrkowo.reborn.automessage.ModerrkowoAutoMessage;
import pl.moderr.moderrkowo.reborn.commands.admin.*;
import pl.moderr.moderrkowo.reborn.commands.user.RankingCommand;
import pl.moderr.moderrkowo.reborn.commands.user.information.CraftingDzialkaCommand;
import pl.moderr.moderrkowo.reborn.commands.user.information.DiscordCommand;
import pl.moderr.moderrkowo.reborn.commands.user.information.RegulaminCommand;
import pl.moderr.moderrkowo.reborn.commands.user.messages.HelpopCommand;
import pl.moderr.moderrkowo.reborn.commands.user.messages.MessageCommand;
import pl.moderr.moderrkowo.reborn.commands.user.messages.ReplyCommand;
import pl.moderr.moderrkowo.reborn.commands.user.teleportation.*;
import pl.moderr.moderrkowo.reborn.commands.user.weather.PogodaCommand;
import pl.moderr.moderrkowo.reborn.cuboids.CuboidsManager;
import pl.moderr.moderrkowo.reborn.discord.DiscordManager;
import pl.moderr.moderrkowo.reborn.economy.*;
import pl.moderr.moderrkowo.reborn.enchantments.HammerEnchantment;
import pl.moderr.moderrkowo.reborn.events.EventManager;
import pl.moderr.moderrkowo.reborn.listeners.*;
import pl.moderr.moderrkowo.reborn.mysql.MySQL;
import pl.moderr.moderrkowo.reborn.opening.OpeningManager;
import pl.moderr.moderrkowo.reborn.timevoter.TimeVoter;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;
import pl.moderr.moderrkowo.reborn.utils.HexResolver;
import pl.moderr.moderrkowo.reborn.utils.Logger;
import pl.moderr.moderrkowo.reborn.villagers.VillagerManager;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public final class Main extends JavaPlugin {


    public HammerEnchantment hammerEnchantment = null;

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
    private static MySQL mySQL;
    public RynekManager instanceRynekManager;
    public EventManager eventManager;
    public HashMap<String, Enchantment> customEnchants = new HashMap<>();
    public DiscordManager discordManager;

    public static String getVersion() {
        return "v1.4.5 (SHOP UPDATE)";
    }

    @Contract(pure = true)
    public static Main getInstance() {
        return instance;
    }

    public static String getServerName() {
        return ColorUtils.color(HexResolver.parseHexString(Main.getInstance().getConfig().getString("servername")));
    }

    public VillagerManager villagerManager;

    public static MySQL getMySQL() {
        return mySQL;
    }

    public static void loadEnchantments(Enchantment enchantment) {
        boolean registered = false;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
            registered = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (registered) {
            Main.getInstance().customEnchants.put(enchantment.getName().replace(" ", "_"), enchantment);
            Logger.logPluginMessage("Zarejestrowano " + enchantment.getKey() + " [" + enchantment.getName() + "]");
        } else {
            //Main.getInstance().customEnchants.put(enchantment.getName().replace(" ", "_"), enchantment);
            Logger.logPluginMessage("Wystąpił błąd przy rejestrowaniu " + enchantment.getKey());
        }
    }

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        Logger.logPluginMessage("Wczytywanie wtyczki Main");
        instance = this;
        LoadPluginConfig();

        hammerEnchantment = new HammerEnchantment();
        loadEnchantments(hammerEnchantment);

        LoadDataYML();
        initializeListeners();
        initializeAntyLogout();
        initializeAutoMessage();
        initializeCommands();
        new OpeningManager();

        villagerManager = new VillagerManager();
        timeVoter = new TimeVoter();
        Logger.logPluginMessage("Wczytano głosowanie czasu");
        cuboidsManager = new CuboidsManager();
        cuboidsManager.Start();
        initializeMySQL();
        instanceRynekManager = new RynekManager();
        eventManager = new EventManager();
        //eventLoop();

        discordManager = new DiscordManager();
        try {
            discordManager.StartBot();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        Bukkit.getPluginManager().registerEvents(instanceRynekManager, this);
        Logger.logPluginMessage("Wczytano działki");
        Logger.logPluginMessage("Wczytano plugin w &8(&a" + (System.currentTimeMillis() - start) + "ms&8)");
    }

    private void eventLoop() {
        BossBar bossBar = Bukkit.createBossBar(ColorUtils.color("&eTrwa wydarzenie"), BarColor.RED, BarStyle.SOLID);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);
            Bukkit.getScheduler().runTaskLater(this, () -> bossBar.getPlayers().forEach(bossBar::removePlayer), 20 * 10);
        }, 0, 20 * 60 * 15);
    }

    private void initializeMySQL() {
        mySQL = new MySQL();
        mySQL.mysqlSetup();
    }

    @Override
    public void onDisable() {
        discordManager.EndBot();
        for (Enchantment enchantment : customEnchants.values()) {
            try {
                Field byIdField = Enchantment.class.getDeclaredField("byKey");
                Field byNameField = Enchantment.class.getDeclaredField("byName");

                byIdField.setAccessible(true);
                byNameField.setAccessible(true);

                HashMap<Integer, Enchantment> byId = (HashMap<Integer, Enchantment>) byIdField.get(null);
                HashMap<Integer, Enchantment> byName = (HashMap<Integer, Enchantment>) byNameField.get(null);

                byId.remove(enchantment.getKey());
                byName.remove(enchantment.getName());
            } catch (Exception ignored) {

            }
        }
        try {
            instanceRynekManager.save();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        mySQL.disable();
        SaveDataYML();
        Logger.logPluginMessage("Wyłączono plugin");
    }

    private void initializeAntyLogout() {
        instanceAntyLogout = new AntyLogoutManager();
        Bukkit.getPluginManager().registerEvents(instanceAntyLogout, this);
        Logger.logPluginMessage("Wczytano AntyLogout");
    }

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
        Objects.requireNonNull(getCommand("villager")).setExecutor(new VillagerCommand());
        Objects.requireNonNull(getCommand("setspawn")).setExecutor(new SetSpawnCommand());
        Objects.requireNonNull(getCommand("saveusers")).setExecutor(new SaveUsersCommand());
        Objects.requireNonNull(getCommand("holo")).setExecutor(new HoloCommand());
        Objects.requireNonNull(getCommand("abank")).setExecutor(new ABankCommand());
        Objects.requireNonNull(getCommand("aenchant")).setExecutor(new AEnchantment());

        Objects.requireNonNull(getCommand("ranking")).setExecutor(new RankingCommand());
        Objects.requireNonNull(getCommand("craftingdzialka")).setExecutor(new CraftingDzialkaCommand());
        Objects.requireNonNull(getCommand("discord")).setExecutor(new DiscordCommand());
        Objects.requireNonNull(getCommand("helpop")).setExecutor(new HelpopCommand());
        Objects.requireNonNull(getCommand("message")).setExecutor(new MessageCommand());
        Objects.requireNonNull(getCommand("reply")).setExecutor(new ReplyCommand());
        Objects.requireNonNull(getCommand("tpr")).setExecutor(new TPACommand());
        Objects.requireNonNull(getCommand("tpaccept")).setExecutor(new TPAccept());
        Objects.requireNonNull(getCommand("tpdeny")).setExecutor(new TPDeny());
        Objects.requireNonNull(getCommand("pogoda")).setExecutor(new PogodaCommand());
        Objects.requireNonNull(getCommand("regulamin")).setExecutor(new RegulaminCommand());
        Objects.requireNonNull(getCommand("spawn")).setExecutor(new SpawnCommand());
        Objects.requireNonNull(getCommand("wyplac")).setExecutor(new WithdrawCommand());
        Objects.requireNonNull(getCommand("portfel")).setExecutor(new PortfelCommand());
        Objects.requireNonNull(getCommand("sethome")).setExecutor(new SetHomeCommand());
        Objects.requireNonNull(getCommand("home")).setExecutor(new HomeCommand());
        Objects.requireNonNull(getCommand("acheststorage")).setExecutor(new AChestStorageCommand());
        Objects.requireNonNull(getCommand("przelej")).setExecutor(new PrzelejCommand());
        Objects.requireNonNull(getCommand("rynek")).setExecutor(new RynekCommand());

        Logger.logPluginMessage("Wczytano komendy");
    }
    //</editor-fold>
    //<editor-fold> Listeners
    public void initializeListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new MotdListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new CropBreakListener(), this);
        Bukkit.getPluginManager().registerEvents(new PogodaCommand(), this);
        Bukkit.getPluginManager().registerEvents(new TNTListener(), this);
        Bukkit.getPluginManager().registerEvents(new WithdrawCommand(), this);
        Bukkit.getPluginManager().registerEvents(new PortalListener(), this);
        Logger.logPluginMessage("Wczytano listenery");
    }
    //</editor-fold>
    //<editor-fold> YML
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
    public void initializeAutoMessage() {
        autoMessage = new ModerrkowoAutoMessage(this, 60 * 5, new ArrayList<String>() {
            {
                add("Aby zdobyć &6unikatowe przedmioty w &cnetherze &flub &dendzie &fbędziesz musiał zdobyć przepustke!");
                add("Na tym serwerze umożliwiamy prostą prośbę o teleportacje! &6/tpr <nick>");
                add("Jeżeli zobaczysz jakiś błąd zgłoś! &6/helpop");
                add("Nie wiesz jak zarobić? Wpisz &6/sprzedaz &ftam sprzedasz swoje przedmioty!");
                add("Możesz wypłacić pieniądze komendą &6/wyplac <kwota>");
                add("Znasz serwerowe zasady? &6/regulamin");
                add("Sklepy znajdują się na spawnie! &6/spawn");
                add("Questy możesz znaleść na &6/spawn");
                add("Eventy są &cwyłączone!");
                add("Chcesz swojego cuboida? &6/craftingdzialka");
                add("Aby chronić swoje dobytki stwórz działke!");
                add("Jeżeli nie wiesz co się zmieniło &6/zmiany");
                add("Dołącz na naszego &9&lDISCORD&f'a! &6/discord");
                add("Chcesz zobaczyć &6swoją&f pozycje w rankingu? &6/ranking");
                add("Jeżeli jest brzydka pogoda wpisz &6/pogoda");
                add("Możesz wypłacić pieniądze do banknotu! &6/wyplac");
            }
        });
        Logger.logPluginMessage("Wczytano AutoMessage");
    }
}
