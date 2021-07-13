package pl.moderr.moderrkowo.core.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.moderr.moderrkowo.core.mysql.User;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ABankCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) sender;
        User u = UserManager.getUser(Objects.requireNonNull(Bukkit.getPlayer(args[1])).getUniqueId());
        if(args[0].equalsIgnoreCase("odejmij")){
            u.subtractMoney(Double.parseDouble(args[2]));
            p.sendMessage(ColorUtils.color("&8[!] &2" + u.getName() + " &aposiada " + ChatUtil.getMoney(u.getMoney())));
        }
        if(args[0].equalsIgnoreCase("dodaj")){
            u.addMoney(Double.parseDouble(args[2]));
            p.sendMessage(ColorUtils.color("&8[!] &2" + u.getName() + " &aposiada " + ChatUtil.getMoney(u.getMoney())));
        }
        if(args[0].equalsIgnoreCase("ustaw")){
            u.setMoney(Double.parseDouble(args[2]));
            p.sendMessage(ColorUtils.color("&8[!] &2" + u.getName() + " &aposiada " + ChatUtil.getMoney(u.getMoney())));
        }
        if(args[0].equalsIgnoreCase("sprawdz")){
            p.sendMessage(ColorUtils.color("&8[!] &2" + u.getName() + " &aposiada " + ChatUtil.getMoney(u.getMoney())));
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 1){
            return new ArrayList<String>(){
                {
                    add("odejmij");
                    add("dodaj");
                    add("ustaw");
                    add("sprawdz");
                }
            };
        }
        return null;
    }
}
