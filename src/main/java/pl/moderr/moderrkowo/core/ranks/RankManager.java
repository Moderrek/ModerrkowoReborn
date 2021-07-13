package pl.moderr.moderrkowo.core.ranks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

import java.util.ArrayList;

public class RankManager {
    public static ChatColor getRankColor(Rank rank) {
        switch (rank) {
            case Zelazo:
                return ChatColor.WHITE;
            case Zloto:
                return ChatColor.GOLD;
            case Diament:
                return ChatColor.AQUA;
            case Emerald:
                return ChatColor.GREEN;
            default:
                return ChatColor.RED;
        }
    }

    public static String getPreNick(Rank rank, StuffRank stuffRank, boolean space) {
        String s = "";
        switch (rank) {
            case None:
                return ColorUtils.color("&7");
            case Zelazo:
                s = ColorUtils.color("&7");
                break;
            case Zloto:
                s = ColorUtils.color("&e");
                break;
            case Diament:
                s = ColorUtils.color("&6");
                break;
            case Emerald:
                s = ColorUtils.color("&2");
                break;
        }
        if (stuffRank.equals(StuffRank.Administrator) || stuffRank.equals(StuffRank.Moderator)) {
            s = ColorUtils.color("&6");
        }
        if (space && !s.equals("")) {
            return s + " ";
        } else {
            return s;
        }
    }

    public static String getRankNameShort(Rank rank, boolean space) {
        String s = "";
        switch (rank) {
            case None:
                return "";
            case Zelazo:
                s = ColorUtils.color("&f&lŻ");
                break;
            case Zloto:
                s = ColorUtils.color("&6&lZ");
                break;
            case Diament:
                s = ColorUtils.color("&b&lD");
                break;
            case Emerald:
                s = ColorUtils.color("&a&lE");
                break;
        }
        if (space && !s.equals("")) {
            return s + " ";
        } else {
            return s;
        }
    }

    public static String getRankName(Rank rank, boolean space) {
        String s = "";
        switch (rank) {
            case None:
                return "";
            case Zelazo:
                s = ColorUtils.color("&f&lŻELAZO");
                break;
            case Zloto:
                s = ColorUtils.color("&6&lZŁOTO");
                break;
            case Diament:
                s = ColorUtils.color("&b&lDIAMENT");
                break;
            case Emerald:
                s = ColorUtils.color("&a&lEMERALD");
        }
        if (space && !s.equals("")) {
            return s + " ";
        } else {
            return s;
        }
    }

    public static String getStuffRankNameShort(StuffRank rank, boolean space) {
        String s = "";
        switch (rank) {
            case None:
                return "";
            case Tester:
                s = ColorUtils.color("&a&lT");
                break;
            case CommunityManager:
                s = ColorUtils.color("&9&lCM");
                break;
            case Moderator:
                s = ColorUtils.color("&2&lMOD");
                break;
            case Administrator:
                s = ColorUtils.color("&c&lADM");
                break;
        }
        if (space && !s.equals("")) {
            return s + " ";
        } else {
            return s;
        }
    }

    public static String getStuffRankName(StuffRank rank, boolean space) {
        String s = "";
        switch (rank) {
            case None:
                return "";
            case Tester:
                s = ColorUtils.color("&a&lTESTER");
                break;
            case CommunityManager:
                s = ColorUtils.color("&9&lCommunityManager");
                break;
            case Moderator:
                s = ColorUtils.color("&2&lMODERATOR");
                break;
            case Administrator:
                s = ColorUtils.color("&c&lADMIN");
                break;
        }
        if (space && !s.equals("")) {
            return s + " ";
        } else {
            return s;
        }
    }

    public static String getChat(Rank rank, StuffRank stuffRank) {
        if (stuffRank.equals(StuffRank.None)) {
            return getRankNameShort(rank, true) + getPreNick(rank, stuffRank, false);
        } else {
            return getStuffRankNameShort(stuffRank, true) + getRankNameShort(rank, true) + getPreNick(rank, stuffRank, false);
        }
    }

    public static Material getMaterial(Rank rank) {
        switch (rank) {
            case Zelazo:
                return Material.IRON_BLOCK;
            case Zloto:
                return Material.GOLD_BLOCK;
            case Diament:
                return Material.DIAMOND_BLOCK;
            case Emerald:
                return Material.EMERALD_BLOCK;
            default:
                return Material.DIRT;
        }
    }

    public static int getPriority(Rank rank) {
        switch (rank) {
            case Zelazo:
                return 1;
            case Zloto:
                return 2;
            case Diament:
                return 3;
            case Emerald:
                return 4;
            default:
                return 0;
        }
    }

    public static double getCost(Rank rank) {
        switch (rank) {
            case Zelazo:
                return 10;
            case Zloto:
                return 20;
            case Diament:
                return 50;
            case Emerald:
                return 100;
            default:
                return 0;
        }
    }

    public static ArrayList<String> getBonus(Rank rank) {
        switch (rank) {
            case Zelazo:
                return new ArrayList<>() {
                    {
                        add("Jedno razowe +" + ChatUtil.getMoney(5000)); // done
                        add("Jedno razowe +" + ChatUtil.getSeasonCoins(150)); // done
                        add("Brak opłaty przy wystawianiu na rynku"); // done
                        add("Większy limit przedmiotów na rynku (15 -> 20)"); // done
                        add("Brak oczekiwania na RTP"); // done
                        add("Wyróżnienie nad głową, na chacie, na TAB"); // done
                        add("Komunikat przy wejściu i wyjściu na serwer"); // done
                        add("Szybsze pisanie na chacie 1.5s -> 0.7s"); // done
                        add("Szybsze używanie komend 3s -> 2s"); // done
                        add("Mniejszy delay na zadania 10m -> 6m"); // done
                        add("Mnożnik x1,5 otrzymanego doświadczenia"); // done
                        add("Możliwość posiadania 2 domów"); // done
                        add("2x większy dochód za czas gry"); // done
                        add("Brak opłaty za anulowania zadania"); // done
                        add("Możliwość używania ustawień działki"); // done
                    }
                };
            case Zloto:
                return new ArrayList<>() {
                    {
                        add("Jedno razowe +" + ChatUtil.getMoney(10000)); // done
                        add("Jedno razowe +" + ChatUtil.getSeasonCoins(275)); // done
                        add("Brak opłaty przy wystawianiu na rynku"); // done
                        add("Większy limit przedmiotów na rynku (15 -> 25)"); // done
                        add("Brak oczekiwania na RTP"); // done
                        add("Wyróżnienie nad głową, na chacie, na TAB"); // done
                        add("Komunikat przy wejściu i wyjściu na serwer"); // done
                        add("Szybsze pisanie na chacie 1.5s -> 0.5s"); // done
                        add("Szybsze używanie komend 3s -> 2s"); // done
                        add("Mniejszy delay na zadania 10m -> 5m"); // done
                        add("Mnożnik x1,5 otrzymanego doświadczenia"); // done
                        add("Możliwość posiadania 2 domów"); // done
                        add("3x większy dochód za czas gry"); // done
                        add("Brak opłaty za anulowania zadania"); // done
                        add("Możliwość używania ustawień działki"); // done
                    }
                };
            case Diament:
                return new ArrayList<>() {
                    {
                        add("Jedno razowe +" + ChatUtil.getMoney(50000)); // done
                        add("Jedno razowe +" + ChatUtil.getSeasonCoins(400)); // done
                        add("Możliwość posiadania 2 działek na świat"); // done
                        add("Brak opłaty przy wystawianiu na rynku"); // done
                        add("Większy limit przedmiotów na rynku (15 -> 30)"); // done
                        add("Brak oczekiwania na RTP"); // done
                        add("Wyróżnienie nad głową, na chacie, na TAB"); // done
                        add("Komunikat przy wejściu i wyjściu na serwer"); // done
                        add("Szybsze pisanie na chacie 1.5s -> 0.3s"); // done
                        add("Szybsze używanie komend 3s -> 1s"); // done
                        add("Mniejszy delay na zadania 10m -> 2m"); // done
                        add("Mnożnik x2 otrzymanego doświadczenia"); // done
                        add("Możliwość posiadania 4 domów"); // done
                        add("5x większy dochód za czas gry"); // done
                        add("Brak opłaty za anulowania zadania"); // done
                        add("Możliwość używania Enderchesta /enderchest");
                        add("Pisanie kolorami na chacie"); // done
                        add("Możliwość używania ustawień działki"); // done
                    }
                };
            case Emerald:
                return new ArrayList<>() {
                    {
                        add("Jedno razowe +" + ChatUtil.getMoney(100000)); // done
                        add("Jedno razowe +" + ChatUtil.getSeasonCoins(600)); // done
                        add("Możliwość posiadania 3 działek na świat");
                        add("Brak opłaty przy wystawianiu na rynku"); // done
                        add("Większy limit przedmiotów na rynku (15 -> 50)"); // done
                        add("Brak oczekiwania na RTP"); // done
                        add("Wyróżnienie nad głową, na chacie, na TAB"); // done
                        add("Komunikat przy wejściu i wyjściu na serwer"); // done
                        add("Szybsze pisanie na chacie 1.5s -> 0.2s"); // done
                        add("Szybsze używanie komend 3s -> 0.5s"); // done
                        add("Mniejszy delay na zadania 10m -> brak"); // done
                        add("Mnożnik x3 otrzymanego doświadczenia"); // done
                        add("Możliwość posiadania 6 domów"); // done
                        add("8x większy dochód za czas gry"); // done
                        add("Brak opłaty za anulowania zadania"); // done
                        add("Możliwość używania Enderchesta /enderchest");
                        add("Możliwość używania craftingu /crafting");
                        add("Pisanie kolorami i gradientem na chacie"); // done
                        add("Możliwość używania ustawień działki");
                    }
                };
            default:
                return new ArrayList<>() {
                    {
                        add("Wystąpił błąd");
                    }
                };
        }
    }
}
