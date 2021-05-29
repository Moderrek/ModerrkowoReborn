package pl.moderr.moderrkowo.reborn.commands.user.weather;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;
import pl.moderr.moderrkowo.reborn.utils.ModerrkowoLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PogodaCommand implements CommandExecutor, Listener, TabCompleter {


    ArrayList<UUID> votedList = new ArrayList<>();
    boolean rain = false;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.isOp()) {
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("clear")) {
                        World w = Bukkit.getWorld("world");
                        assert w != null;
                        w.setStorm(false);
                        w.setThundering(false);
                        p.sendMessage(ColorUtils.color("&aZmieniono pogodę na ładną"));
                        ModerrkowoLog.LogAdmin(ColorUtils.color("&6" + p.getName() + " &7zmienił pogodę na ładną"));
                        return false;
                    }
                    if (args[0].equalsIgnoreCase("rain")) {
                        World w = Bukkit.getWorld("world");
                        assert w != null;
                        w.setStorm(true);
                        w.setThundering(false);
                        p.sendMessage(ColorUtils.color("&aZmieniono pogodę na deszcz"));
                        ModerrkowoLog.LogAdmin(ColorUtils.color("&6" + p.getName() + " &7zmienił pogodę na deszczową"));
                        return false;
                    }
                    if (args[0].equalsIgnoreCase("thunder")) {
                        World w = Bukkit.getWorld("world");
                        assert w != null;
                        w.setStorm(true);
                        w.setThundering(false);
                        p.sendMessage(ColorUtils.color("&aZmieniono pogodę na burzę"));
                        ModerrkowoLog.LogAdmin(ColorUtils.color("&6" + p.getName() + " &7zmienił pogodę na burzową"));
                        return false;
                    }
                }
            }
            if (Objects.requireNonNull(Bukkit.getWorld("world")).hasStorm()) {
                if (votedList.contains(p.getUniqueId())) {
                    p.sendMessage(ColorUtils.color("&cJuż zagłosowałeś!"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return false;
                } else {
                    votedList.add(p.getUniqueId());
                    int i = (int) (Bukkit.getOnlinePlayers().size() / 1.5);
                    if (i == 0) {
                        i = 1;
                    }
                    p.sendMessage(ColorUtils.color("&aZagłosowano pomyślnie! " + "&8(&a" + votedList.size() + "&8/&7" + i + "&8)"));
                    Bukkit.broadcastMessage(ColorUtils.color("&6" + p.getName() + " &7zagłosował na zmianę pogody! " + "&8(&a" + votedList.size() + "&8/&7" + i + "&8)"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                    if (votedList.size() >= i) {
                        Objects.requireNonNull(Bukkit.getWorld("world")).setStorm(false);
                        Bukkit.broadcastMessage(ColorUtils.color("&8[!] &7Przegłosowano na zmianę pogody! Pogoda została zmieniona."));
                        rain = false;
                        votedList = new ArrayList<>();
                    }
                    return true;
                }
            } else {
                p.sendMessage(ColorUtils.color("&cAktualnie jest ładna pogoda nie można głosować!"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
        }
        return false;
    }

    @EventHandler
    public void onRain(WeatherChangeEvent e) {
        if (e.getWorld() != Bukkit.getWorld("world")) {
            return;
        }
        if (e.toWeatherState()) {
            votedList = new ArrayList<>();
            Bukkit.broadcastMessage(ColorUtils.color("&8[!] &7Rozpoczęto głosowanie na zmianę pogody! &7/pogoda &7aby zagłosować."));
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            }
            rain = true;
        } else {
            votedList = new ArrayList<>();
            rain = false;
        }
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (sender.isOp()) {
                ArrayList<String> list = new ArrayList<>();
                list.add("clear");
                list.add("rain");
                list.add("thunder");
                return list;
            }
        }
        return null;
    }
}
