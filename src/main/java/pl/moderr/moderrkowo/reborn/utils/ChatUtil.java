package pl.moderr.moderrkowo.reborn.utils;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ChatUtil {

    public static void clearChat(Player player) {
        for (int i = 0; i < 100; i++) {
            player.sendMessage(" ");
        }
    }

    public static String getTicksToTime(int ticks) {
        if (ticks > 20 * 60) {
            if (ticks > 20 * 60 * 60) {
                DecimalFormat df2 = new DecimalFormat("#.##");
                if (ticks > 20 * 60 * 60 * 24) {
                    return (df2.format((double) ticks / 20 / 60 / 60 / 24)) + " dni";
                } else {
                    return (df2.format((double) ticks / 20 / 60 / 60)) + " godz.";
                }
            } else {
                return (ticks / 20 / 60) + " min";
            }
        } else {
            return (ticks / 20) + " sek.";
        }
    }

    public static String materialName(Material material) {
        String materialName = material.toString();
        materialName = materialName.replaceAll("_", " ");
        materialName = materialName.toLowerCase();
        return WordUtils.capitalizeFully(materialName);
    }

    public static String materialName(EntityType material) {
        String materialName = material.toString();
        materialName = materialName.replaceAll("_", " ");
        materialName = materialName.toLowerCase();
        return WordUtils.capitalizeFully(materialName);
    }

    /**
     * @param money kwota pieniędzy
     * @return 1 = 1 zł ; money = money zł; 1235 = 1,235 zł
     */
    public static String getMoney(int money) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pl-PL"));
        nf.setMaximumFractionDigits(2);
        return nf.format(money) + " zł";
    }

}
