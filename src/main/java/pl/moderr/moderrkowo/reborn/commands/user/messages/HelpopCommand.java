package pl.moderr.moderrkowo.reborn.commands.user.messages;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import com.destroystokyo.paper.Title;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;
import pl.moderr.moderrkowo.reborn.utils.Logger;

public class HelpopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length > 0) {
                Logger.logHelpMessage(p, Logger.getMessage(args, 0, true));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                p.sendMessage(ColorUtils.color("&8[!] &aPomyślnie wysłano!"));
                p.sendMessage(ColorUtils.color("&8[&9Pomoc&8] &7" + p.getName() + "&8: &e" + Logger.getMessage(args, 0, true)));
                p.sendTitle(new Title(ColorUtils.color("&6&lModerrkowo"), ColorUtils.color("&aWysłano wiadomość.")));
                /*WebhookClient webhookClient = WebhookClient.withUrl("https://discord.com/api/webhooks/847363621004115969/rpH-tCedU8v0nR57W6PSOm8q9oqS3iLHftvt6t3OJH-U-Tkj2uBQ82O_LLVHhqFbxCNP");
                WebhookEmbedBuilder embedBuilder = new WebhookEmbedBuilder()
                        .setTitle(new WebhookEmbed.EmbedTitle("Helop - Moderrkowo", null))
                        .setAuthor(new WebhookEmbed.EmbedAuthor(p.getName(), "https://minotar.net/avatar/" + p.getName() + "/50.png",null))
                        .setDescription(Logger.getMessage(args, 0, true)).setColor(0xFFFFFF);
                webhookClient.send(embedBuilder.build());
                webhookClient.close();*/
                return true;
            } else {
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                p.sendMessage(ColorUtils.color("&cUżycie: &e/helpop <wiadomość>"));
                return false;
            }
        } else {
            sender.sendMessage("Nie jesteś graczem!");
            return false;
        }
    }
}
