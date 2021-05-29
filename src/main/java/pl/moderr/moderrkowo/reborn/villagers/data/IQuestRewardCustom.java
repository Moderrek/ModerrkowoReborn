package pl.moderr.moderrkowo.reborn.villagers.data;

import org.bukkit.entity.Player;
import pl.moderr.moderrkowo.reborn.mysql.User;

public interface IQuestRewardCustom extends IQuestReward{
    String label();
    void Action(Player p, User u);
}
