package pl.moderr.moderrkowo.core.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.Compression;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.listeners.MotdListener;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

import javax.security.auth.login.LoginException;
import java.util.Objects;

public class DiscordManager extends ListenerAdapter {

    private final String token = "ODUwMjU0MzY2NTE4ODA0NTEx.YLnDFQ.XkOxCJiSaSL7Zati6ulDy4K9_EM";
    private JDA jda;

    public DiscordManager() {

    }

    public void EndBot() {
        jda.shutdownNow();
    }

    public void StartBot() throws LoginException {
        JDABuilder builder = JDABuilder.createDefault(token);
        builder.setActivity(Activity.playing("Moderrkowo.PL"));
        //builder.disableCache(CacheFlag.MEMBER_OVERRIDES,CacheFlag.VOICE_STATE);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setCompression(Compression.NONE);
        //builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.setLargeThreshold(50);
        jda = builder.build();
        jda.addEventListener(new DiscordJoinQuitListener(jda), this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            Guild guild = jda.getGuildById(827530115402825748L);
            assert guild != null;
            try {
                Objects.requireNonNull(guild.getVoiceChannelById(859406576226271233L)).getManager().setName("\uD83D\uDC93 Rekord online: " + Main.getInstance().dataConfig.getInt("MaxPlayer")).complete();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Objects.requireNonNull(guild.getVoiceChannelById(859406217808838676L)).getManager().setName("\uD83D\uDFE2 Online: " + Bukkit.getOnlinePlayers().size()).complete();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Objects.requireNonNull(guild.getVoiceChannelById(859406464418709504L)).getManager().setName("\uD83C\uDFAE Zarejestrowanych: " + Main.getMySQL().getQuery().getCountOfUsers()).complete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 20 * 60 * 5);
        Logger.logDiscordMessage("Załadowano bota");
        //Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> jda.getPresence().setActivity(Activity.watching(ChatUtil.getTime(MotdListener.localDate))), 0, 20 * 60);
    }

    public void sendHelpop(Player p, String message, boolean b) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(ColorUtils.BLUE);
        embedBuilder.setTitle("Wiadomość do administracji");
        embedBuilder.setFooter(p.getName());
        embedBuilder.addField("", message, false);
        embedBuilder.addField("Wer", Main.getVersion(), false);
        embedBuilder.addField("UUID", p.getUniqueId().toString(), false);
        if (b) {
            int i = (int) Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).count();
            embedBuilder.addField("Admin Online", i + "", false);
        } else {
            embedBuilder.addField("Admin Offline", "", false);
        }
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

    public void sendTryJoin(String name) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(ColorUtils.GOLD);
        embedBuilder.setTitle("Próba wejścia przed startem");
        embedBuilder.setFooter(ChatUtil.getTime(MotdListener.localDate));
        embedBuilder.addField("", name, false);
        jda.getGuildById(827530115402825748L).getTextChannelById(850252515810344980L).sendMessage(embedBuilder.build()).queue();
    }
}
