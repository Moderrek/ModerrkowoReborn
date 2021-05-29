package pl.moderr.moderrkowo.reborn.listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.reborn.Main;
import pl.moderr.moderrkowo.reborn.commands.admin.ChatCommand;
import pl.moderr.moderrkowo.reborn.mysql.User;
import pl.moderr.moderrkowo.reborn.mysql.UserManager;
import pl.moderr.moderrkowo.reborn.utils.ChatUtil;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;
import pl.moderr.moderrkowo.reborn.utils.Logger;

import java.time.Instant;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.UUID;

public class ChatListener implements Listener {

    private final ArrayList<String> blockedCmds = new ArrayList<String>() {
        {
            add("help");
            add("bukkit:help");
            add("plugins");
            add("bukkit:plugins");
            add("about");
            add("bukkit:about");
            add("pl");
            add("bukkit:pl");
            add("ver");
            add("bukkit:ver");
            add("version");
            add("bukkit:version");
            add("minecraft:msg");
            add("minecraft:help");
            add("seed");
            add("minecraft:seed");
            add("?");
            add("bukkit:?");
            add("icanhasbukkit");
        }
    };

    @EventHandler
    public void chat(@NotNull final AsyncPlayerChatEvent e) {
        if (!e.getPlayer().isOp() && !ChatCommand.canChat) {
            e.getPlayer().sendMessage(ColorUtils.color("&cChat jest wyłączony!"));
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            e.setCancelled(true);
            return;
        }
        e.setMessage(e.getMessage().replace("%", "%%"));
        if (e.getPlayer().hasPermission("moderr.admin")) {
            e.setMessage(ColorUtils.color(e.getMessage()));
            e.setCancelled(true);
            User u = UserManager.getUser(e.getPlayer().getUniqueId());
            TextComponent msg = new TextComponent();
            msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + e.getPlayer().getName() + " "));
            msg.setText(e.getPlayer().getName());
            msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ColorUtils.color(
                    "&f" + e.getPlayer().getName() +
                            "\n \n&fPoziom: &c" + u.getLevel() + "\n" +
                            "&fPortfel: &6" + ChatUtil.getMoney(u.getMoney()) +
                            "\n&fPd: &a" + u.getExp() + "\n"
            ))));
            Main.getInstance().getServer().broadcast(new ComponentBuilder().color(ChatColor.GOLD).append(msg).append(" ").color(ChatColor.YELLOW).append(e.getMessage()).create());
        } else {
            e.setCancelled(true);
            User u = UserManager.getUser(e.getPlayer().getUniqueId());
            TextComponent msg = new TextComponent();
            msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + e.getPlayer().getName() + " "));
            msg.setText(e.getPlayer().getName());
            msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ColorUtils.color(
                    "&f" + e.getPlayer().getName() +
                            "\n \n&fPoziom: &c" + u.getLevel() + "\n" +
                            "&fPortfel: &6" + ChatUtil.getMoney(u.getMoney()) +
                            "\n&fPd: &a" + u.getExp() + "\n"
            ))));
            Main.getInstance().getServer().broadcast(new ComponentBuilder().color(ChatColor.GRAY).append(msg).append(" ").color(ChatColor.WHITE).append(e.getMessage()).create());
        }
        //TODO Cenzura
    }

    private final Map<UUID, Instant> commandDelay = new IdentityHashMap<>();

    @EventHandler
    public void preCommand(PlayerCommandPreprocessEvent e) {
        if (e.getPlayer().isOp()) {
            return;
        }
        boolean blocked = false;
        String command = e.getMessage().toLowerCase();
        for (String s : blockedCmds) {
            if (command.startsWith("/" + s)) {
                if(command.startsWith("/helpop")){
                    e.setCancelled(false);
                    continue;
                }
                blocked = true;
                Logger.logAdminLog(e.getPlayer().getName() + " chciał wpisać nielegalną komendę. " + e.getMessage());
            }
        }
        e.setCancelled(true);
        if(blocked){
            return;
        }
        final Instant now = Instant.now();
        commandDelay.compute(e.getPlayer().getUniqueId(), (uuid, instant) -> {
            if (instant != null && now.isBefore(instant)) {
                e.getPlayer().sendActionBar(ColorUtils.color("&cOdczekaj chwilę miedzy wpisywaniem komend"));
                return instant;
            }
            e.setCancelled(false);
            return now.plusSeconds(3);
        });
    }

}
