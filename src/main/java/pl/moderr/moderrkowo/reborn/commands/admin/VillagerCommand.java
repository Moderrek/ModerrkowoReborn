package pl.moderr.moderrkowo.reborn.commands.admin;

import com.destroystokyo.paper.Title;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.moderr.moderrkowo.reborn.Main;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;
import pl.moderr.moderrkowo.reborn.utils.Logger;
import pl.moderr.moderrkowo.reborn.utils.ModerrkowoLog;
import pl.moderr.moderrkowo.reborn.utils.RandomUtils;
import pl.moderr.moderrkowo.reborn.villagers.data.VillagerData;

import java.util.ArrayList;
import java.util.List;

public class VillagerCommand implements CommandExecutor, TabCompleter, Listener {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Villager e = p.getWorld().spawn(p.getLocation(), Villager.class);
            e.setInvulnerable(true);
            e.setAI(false);
            e.setCustomNameVisible(true);
            e.setCustomName(ColorUtils.color(Logger.getMessage(args, 0, true)));
            e.setSilent(true);
            e.setRemoveWhenFarAway(false);
            ModerrkowoLog.LogAdmin(ColorUtils.color("&6" + p.getName() + " &7postawił nowego Villager'a &8(&f" + Logger.getMessage(args, 0, true) + "&8)"));
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("&aLosowy teleport");
        list.add("&c&lQ &7???");
        for (VillagerData data : Main.getInstance().villagerManager.villagers.values()) {
            list.add(data.getCommandSpawnName());
        }
        return list;
    }

}
