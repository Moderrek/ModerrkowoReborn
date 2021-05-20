package pl.moderr.moderrkowo.reborn.antylogout;

import org.bukkit.boss.BossBar;
import org.jetbrains.annotations.Contract;

public class AntyLogoutItem {

    public final BossBar bossBar;
    public int seconds;

    @Contract(pure = true)
    public AntyLogoutItem(int seconds, BossBar bossBar) {
        this.seconds = seconds;
        this.bossBar = bossBar;
    }

}
