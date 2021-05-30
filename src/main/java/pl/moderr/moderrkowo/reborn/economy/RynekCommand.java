package pl.moderr.moderrkowo.reborn.economy;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.reborn.Main;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;
import pl.moderr.moderrkowo.reborn.utils.ModerrkowoLog;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RynekCommand implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("wystaw")) {
                    if (args.length > 1) {
                        int cost;
                        try {
                            cost = Integer.parseInt(args[1]);
                        } catch (Exception e) {
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            p.sendMessage(ColorUtils.color("&cPodano niepoprawną kwotę!"));
                            return false;
                        }
                        if (p.getInventory().getItemInMainHand().getType() == Material.AIR) {
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            p.sendMessage(ColorUtils.color("&cPrzedmiot, który chcesz wystawić musisz trzymać w ręku!"));
                            return false;
                        }
                        if (cost > 1000000 || cost < 0) {
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            p.sendMessage(ColorUtils.color("&cCena przedmiotu wychodzi poza wykres 0-100 000"));
                            return false;
                        }
                        Main.getInstance().instanceRynekManager.addItem(new RynekItem(p.getUniqueId(), p.getInventory().getItemInMainHand().clone(), cost));
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            if (players.getOpenInventory().getTitle().contains(Main.getInstance().instanceRynekManager.RynekGui_WithoutPage)) {
                                players.openInventory(Main.getInstance().instanceRynekManager.getRynekInventory(Integer.parseInt(players.getOpenInventory().getTitle().replace(Main.getInstance().instanceRynekManager.RynekGui_WithoutPage, "")), p));
                            }
                        }
                        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pl-PL"));
                        nf.setMaximumFractionDigits(2);
                        ModerrkowoLog.LogAdmin(ColorUtils.color("&6" + p.getName() + " &7wystawił &6" + p.getInventory().getItemInMainHand().getType() + " &7za &6" + nf.format(cost) + " zł"));
                        p.getInventory().getItemInMainHand().setAmount(0);
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1);
                        p.sendMessage(ColorUtils.color("&aPomyślnie wystawiono twój przedmiot na rynku!"));
                        return false;
                    }
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    p.sendMessage(ColorUtils.color("&cAby wystawić przedmiot musisz podać kwotę!"));
                    return false;
                }
                p.sendMessage(ColorUtils.color("&cUżyj: /rynek wystaw <kwota>"));
                return false;
            }
            p.openInventory(Main.getInstance().instanceRynekManager.getRynekInventory(1, p));
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
            p.sendMessage(ColorUtils.color("&aOtworzono rynek"));
            return false;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            ArrayList<String> list = new ArrayList<>();
            list.add("wystaw");
            return list;
        }
        return null;
    }
}
