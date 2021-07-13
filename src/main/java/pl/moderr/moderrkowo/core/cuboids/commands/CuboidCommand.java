package pl.moderr.moderrkowo.core.cuboids.commands;

import com.destroystokyo.paper.Title;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.cuboids.CuboidsManager;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.ranks.Rank;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CuboidCommand implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length > 0) {
                // Administrators
                if (p.hasPermission("moderr.cuboids.admin")) {
                    if (args[0].equalsIgnoreCase("admin-give")) {
                        p.getInventory().addItem(Objects.requireNonNull(CuboidsManager.getCuboidItem(1)));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                        p.sendTitle(new Title(Main.getServerName(), ColorUtils.color(" &cOtrzymałeś działke!")));
                        return true;
                    }
                }
                // Players
                if (args[0].equalsIgnoreCase("dodaj")) {
                    if (args.length > 1) {
                        OfflinePlayer addPlayer = Bukkit.getOfflinePlayer(args[1]);
                        if (addPlayer != null) {
                            if (p.getUniqueId().equals(addPlayer.getUniqueId())) {
                                p.sendMessage(Main.getServerName() + ColorUtils.color(" &cNie możesz dodać siebie!"));
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                                return false;
                            }
                            RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
                            RegionManager regionManager = regionContainer.get(BukkitAdapter.adapt(p.getWorld()));
                            assert regionManager != null;
                            ApplicableRegionSet set = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(p.getLocation()));
                            ProtectedRegion cuboid = null;
                            for (ProtectedRegion cub : set) {
                                if (cub.getId().startsWith(CuboidsManager.getCuboidNamePrefix().toLowerCase())) {
                                    cuboid = cub;
                                }
                            }
                            if (cuboid != null) {
                                if (!cuboid.getOwners().contains(p.getUniqueId())) {
                                    p.sendMessage(Main.getServerName() + ColorUtils.color(" &cNie jesteś właścicielem tego cuboida!"));
                                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                                    return false;
                                }
                                cuboid.getMembers().addPlayer(addPlayer.getUniqueId());
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                                p.sendTitle(new Title(Main.getServerName(), ColorUtils.color("&aPomyślnie dodano!")));
                                p.sendMessage(Main.getServerName() + ColorUtils.color(String.format(" &aDodałeś&2 %s &ado swojej działki!", addPlayer.getName())));
                                //addPlayer.sendMessage(ColorUtils.color("&aZostałeś dodany do działki gracza &2" + p.getName()));
                                return true;
                            } else {
                                p.sendMessage(Main.getServerName() + ColorUtils.color(" &cMusisz stać na swojej działce!"));
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                                return false;
                            }
                        } else {
                            p.sendMessage(Main.getServerName() + ColorUtils.color(" &cGracz jest offline!"));
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            return false;
                        }
                    } else {
                        p.sendMessage(Main.getServerName() + ColorUtils.color(" &cPodaj nazwę gracza, którego chcesz dodać!"));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("usun")) {
                    if (args.length > 1) {
                        OfflinePlayer addPlayer = Bukkit.getOfflinePlayer(args[1]);
                        if (addPlayer != null) {
                            if (p.getUniqueId().equals(addPlayer.getUniqueId())) {
                                p.sendMessage(Main.getServerName() + ColorUtils.color(" &cNie możesz usunąć siebie!"));
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                                return false;
                            }
                            RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
                            RegionManager regionManager = regionContainer.get(BukkitAdapter.adapt(p.getWorld()));
                            assert regionManager != null;
                            ApplicableRegionSet set = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(p.getLocation()));
                            ProtectedRegion cuboid = null;
                            for (ProtectedRegion cub : set) {
                                if (cub.getId().startsWith(CuboidsManager.getCuboidNamePrefix().toLowerCase())) {
                                    cuboid = cub;
                                }
                            }
                            if (cuboid != null) {
                                if (!cuboid.getOwners().contains(p.getUniqueId())) {
                                    p.sendMessage(Main.getServerName() + ColorUtils.color(" &cNie jesteś właścicielem tego cuboida!"));
                                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                                    return false;
                                }
                                if (cuboid.getMembers().getUniqueIds().contains(addPlayer.getUniqueId())) {
                                    cuboid.getMembers().removePlayer(addPlayer.getUniqueId());
                                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                                    p.sendTitle(new Title(Main.getServerName(), ColorUtils.color("&aPomyślnie usunięto!")));
                                    p.sendMessage(Main.getServerName() + ColorUtils.color(String.format(" &aUsunąłeś&2 %s &az swojej działki!", addPlayer.getName())));
                                    return true;
                                } else {
                                    p.sendMessage(Main.getServerName() + ColorUtils.color(" &cGracz nie jest dodany!"));
                                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                                    return false;
                                }
                            } else {
                                p.sendMessage(Main.getServerName() + ColorUtils.color(" &cMusisz stać na swojej działce!"));
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                                return false;
                            }
                        } else {
                            p.sendMessage(Main.getServerName() + ColorUtils.color(" &cGracz jest offline!"));
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            return false;
                        }
                    } else {
                        p.sendMessage(ColorUtils.color(Main.getServerName() + " &cPodaj nazwę gracza, którego chcesz usunąć!"));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("info")) {
                    RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
                    RegionManager regionManager = regionContainer.get(BukkitAdapter.adapt(p.getWorld()));
                    assert regionManager != null;
                    ApplicableRegionSet set = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(p.getLocation()));
                    ProtectedRegion cub = null;
                    for (ProtectedRegion cuboid : set) {
                        if (cuboid.getId().startsWith(CuboidsManager.getCuboidNamePrefix().toLowerCase())) {
                            cub = cuboid;
                        }
                    }
                    if (cub != null) {
                        p.sendMessage(ColorUtils.color("&6Informacje o działce &e" + cub.getId().replaceFirst(CuboidsManager.getCuboidNamePrefix().toLowerCase(), "").toUpperCase()));
                        p.sendMessage(ColorUtils.color("&6Właściciel &f" + cub.getId().replaceFirst(CuboidsManager.getCuboidNamePrefix().toLowerCase(), "").toUpperCase()));
                        String playerName = cub.getId().replace(CuboidsManager.getCuboidNamePrefix(), "");
                        Location loc = Main.getInstance().dataConfig.getLocation("cuboid." + p.getWorld().getName() + "." + CuboidsManager.getCuboidNamePrefix() + playerName);
                        if (loc == null) {
                            p.sendMessage(ColorUtils.color("&cTA DZIAŁKA JEST STARA/ZEPSUTA zgłoś się do administracji"));
                        }
                        if (loc != null) {
                            p.sendMessage(ColorUtils.color("&6Lokalizacja &f x " + loc.getBlockX() + " y " + loc.getBlockY() + " z " + loc.getBlockZ()));
                        }
                        StringBuilder members = new StringBuilder();
                        if (cub.getMembers().getUniqueIds().size() == 0) {
                            members.append(" Nikt nie jest dodany");
                        } else {
                            for (UUID uuid : cub.getMembers().getUniqueIds()) {
                                members.append(", ").append(Bukkit.getOfflinePlayer(uuid).getName());
                            }
                        }
                        p.sendMessage(ColorUtils.color("&6Dodani:&f" + members.substring(1)));
                        return true;
                    } else {
                        p.sendMessage(Main.getServerName() + ColorUtils.color(" &cAby pobrać informacje o działce najpierw musisz na niej stać!"));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return false;
                    }
                }
                if (args[0].equalsIgnoreCase("ustawienie") && UserManager.getUser(p.getUniqueId()).hasRank(Rank.Zelazo)) {
                    if (args.length == 3) {
                        if (args[1].equalsIgnoreCase("PvP")) {
                            RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
                            RegionManager regionManager = regionContainer.get(BukkitAdapter.adapt(p.getWorld()));
                            assert regionManager != null;
                            ApplicableRegionSet set = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(p.getLocation()));
                            ProtectedRegion cub = null;
                            for (ProtectedRegion cuboid : set) {
                                if (cuboid.getId().startsWith(CuboidsManager.getCuboidNamePrefix().toLowerCase())) {
                                    cub = cuboid;
                                }
                            }
                            if (cub != null) {
                                if (args[2].equalsIgnoreCase("tak")) {
                                    cub.setFlag(Flags.PVP, StateFlag.State.ALLOW);
                                }
                                if (args[2].equalsIgnoreCase("nie")) {
                                    cub.setFlag(Flags.PVP, StateFlag.State.DENY);
                                }
                            }
                        }
                    }
                    return false;
                }
                // If null
                p.sendMessage(Main.getServerName() + ColorUtils.color(" &cBłąd! Błędny argument"));
            } else {
                p.sendMessage(Main.getServerName() + ColorUtils.color(" &cBłąd! Brak argumentów"));
            }
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return false;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            ArrayList<String> hints = new ArrayList<>();
            hints.add("info");
            hints.add("dodaj");
            hints.add("usun");
            if (UserManager.getUser(((Player) sender).getUniqueId()).hasRank(Rank.Zelazo)) {
                hints.add("ustawienie");
            }

            if (sender.hasPermission("moderr.cuboids.admin")) {
                hints.add("admin-give");
            }
            return hints;
        }
        if (args[0].equalsIgnoreCase("ustawienie") && args.length == 2) {
            ArrayList<String> hints = new ArrayList<>();
            hints.add("PvP");
            return hints;
        }
        if (args[0].equalsIgnoreCase("ustawienie") && args.length == 3) {
            ArrayList<String> hints = new ArrayList<>();
            hints.add("Tak");
            hints.add("Nie");
            return hints;
        }
        return null;
    }
}
