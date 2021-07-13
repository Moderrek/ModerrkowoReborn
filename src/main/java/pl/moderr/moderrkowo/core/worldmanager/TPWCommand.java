package pl.moderr.moderrkowo.core.worldmanager;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

import java.util.ArrayList;
import java.util.List;

public class TPWCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 1) {
                WorldManager.TeleportWorld(args[0], p);
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                p.sendTitle("", ColorUtils.color("&a" + args[0]));
                p.sendMessage(ColorUtils.color("&aPomyślnie zostałeś przeteleportowany na inny świat"));
            } else {
                p.sendMessage(ColorUtils.color("&cPodaj nazwę świata!"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            ArrayList<String> list = new ArrayList<>();
            Bukkit.getWorlds().forEach(world -> list.add(world.getName()));
            return list;
        }
        return null;
    }
}
