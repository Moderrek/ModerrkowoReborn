package pl.moderr.moderrkowo.reborn.timevoter.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import pl.moderr.moderrkowo.reborn.timevoter.TimeVoter;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;

public class VoteEvent implements Listener {

    private final TimeVoter timeVoter;

    public VoteEvent(TimeVoter timeVoter) {
        this.timeVoter = timeVoter;
    }

    @EventHandler
    public void sleepVote(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        if (Bukkit.getOnlinePlayers().size() == 1) {
            return;
        }
        if (!timeVoter.isVoteActive) {
            if (!timeVoter.voteUtil.isOverworld(player)) {
                return;
            }
            timeVoter.voteUtil.StartVote(player);
        } else {
            if (!timeVoter.getYesVote().contains(player.getUniqueId())) {
                timeVoter.getYesVote().add(player.getUniqueId());
                player.sendMessage(ColorUtils.color("&aPonieważ zdecydowałeś się spać, rozpoczynasz głosowanie"));
            }
        }

    }
}
