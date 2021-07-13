package pl.moderr.moderrkowo.core.antylogout;

import org.bukkit.boss.BossBar;
import org.jetbrains.annotations.Contract;

public class AntyLogoutItem {

    private final BossBar bossBar;
    private int seconds;

    @Contract(pure = true)
    public AntyLogoutItem(BossBar bossBar, int seconds) {
        this.seconds = seconds;
        this.bossBar = bossBar;
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
