package pl.moderr.moderrkowo.reborn.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bukkit.entity.Player;
import pl.moderr.moderrkowo.reborn.Main;
import pl.moderr.moderrkowo.reborn.mysql.UserManager;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;

import javax.security.auth.login.LoginException;
import java.awt.*;

public class DiscordManager extends ListenerAdapter {

    private final String token = Main.getInstance().getConfig().getString("discord-token");
    private JDA jda;

    public DiscordManager() {

    }

    public void EndBot() {
        jda.shutdownNow();
    }

    public void StartBot() throws LoginException {
        JDABuilder builder = JDABuilder.createDefault(token);
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        builder.setMemberCachePolicy(MemberCachePolicy.OWNER);
        builder.setChunkingFilter(ChunkingFilter.NONE);
        builder.setActivity(Activity.playing("moderrkowo.pl"));
        jda = builder.build();
        jda.addEventListener(this);
    }

    public void sendHelpop(Player p, String message){



        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(ColorUtils.BLUE);
        embedBuilder.setTitle("Wiadomość do administracji");
        embedBuilder.setFooter(p.getName());
        embedBuilder.addField("", message, false);
        embedBuilder.addField("Wer", Main.getVersion(), false);
        embedBuilder.addField("UUID", p.getUniqueId().toString(), false);
        jda.getGuildById(827530115402825748L).getTextChannelById(850252515810344980L).sendMessage(embedBuilder.build()).queue();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.PRIVATE)) {
            System.out.printf("[DC] >> [PM] %s: %s\n", event.getAuthor().getName(),
                    event.getMessage().getContentDisplay());
        } else {
            if (event.getGuild().getIdLong() == 827530115402825748L) {
                System.out.printf("[DC] >> [%s][%s] %s: %s\n", event.getGuild().getName(),
                        event.getTextChannel().getName(), event.getMember().getEffectiveName(),
                        event.getMessage().getContentDisplay());

                if (event.getTextChannel().getIdLong() == 850252360009121812L) {
                    event.getMessage().delete().complete();
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setColor(ColorUtils.RED);
                    embedBuilder.setTitle("Zgłoszenie błędu");
                    embedBuilder.setFooter(event.getAuthor().getName(), event.getAuthor().getAvatarUrl());
                    embedBuilder.addField("", event.getMessage().getContentDisplay(), false);
                    embedBuilder.addField("Wer", Main.getVersion(), false);
                    event.getGuild().getTextChannelById(850252515810344980L).sendMessage(embedBuilder.build()).queue();
                }
            }
        }
    }

    public JDA getJda() {
        return jda;
    }
}
