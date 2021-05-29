package pl.moderr.moderrkowo.reborn.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.reborn.mysql.User;
import pl.moderr.moderrkowo.reborn.mysql.UserManager;
import pl.moderr.moderrkowo.reborn.utils.Logger;

public class SaveUsersCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            int i = 0;
            int players =  Bukkit.getOnlinePlayers().size();
            for(User u : UserManager.loadedUsers.values()){
                UserManager.saveUser(u);
                i++;
                Logger.logAdminLog("Zapisano " + u.getPlayer() + "&8[" + i +"/" + players +"]");
            }
        }
        return false;
    }
}
