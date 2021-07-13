package pl.moderr.moderrkowo.core.commands.user.messages;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

import java.util.HashMap;

public class MessageCommand implements CommandExecutor {
    public final static HashMap<Player, Player> lastMessageSender = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Player p2;
            if (args.length == 0) {
                p.sendMessage(ColorUtils.color("&cUżycie: /msg <nick> <treść>"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
            if (Bukkit.getPlayer(args[0]) != null) {
                p2 = Bukkit.getPlayer(args[0]);
                if (args.length >= 2) {
                    StringBuilder message = new StringBuilder();
                    for (int i = 1; i != args.length; i++) {
                        message.append(" ").append(args[i]);
                    }
                    // SEND MESSAGE
                    assert p2 != null;
                    p.sendMessage(ColorUtils.color("&6Ja -> " + p2.getDisplayName() + "&8:&e" + message));
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                    setLastMessageSender(p, p2);


                    p2.sendMessage(ColorUtils.color("&6" + p.getDisplayName() + " &6-> Ja&8:&e" + message));
                    p2.playSound(p2.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                    setLastMessageSender(p2, p);
                } else {
                    p.sendMessage(ColorUtils.color("&cPodaj jeszcze treść wiadomości!"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return false;
                }
            } else {
                p.sendMessage(ColorUtils.color("&cGracz jest offline!"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
        } else {
            sender.sendMessage("Nie jesteś graczem!");
        }
        return false;
    }

    public void setLastMessageSender(Player p1, Player p2) {
        lastMessageSender.remove(p1);
        lastMessageSender.put(p1, p2);
    }
}
