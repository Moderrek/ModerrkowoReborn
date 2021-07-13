package pl.moderr.moderrkowo.core.utils;

import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.PacketPlayOutScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class NameTagUtils implements Listener {

    private static Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    private static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    private static void setField(Object packet, Field field, Object value) {
        field.setAccessible(true);

        try {
            field.set(packet, value);
        } catch (IllegalAccessException | IllegalArgumentException var4) {
            var4.printStackTrace();
        }

        field.setAccessible(!field.isAccessible());
    }

    private static Field getField(Class<?> classs, String fieldname) {
        try {
            return classs.getDeclaredField(fieldname);
        } catch (SecurityException | NoSuchFieldException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static void sendNameTagPlayers(Player player, String prefix, String suffix, Player players, int priority) {
        String pname = player.getName();
        String name = "" + priority + pname.charAt(0) + UUID.randomUUID().toString().substring(0, 10);
        List<String> pl = new ArrayList<>();
        pl.add(player.getName());
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
        Class<? extends PacketPlayOutScoreboardTeam> clas = packet.getClass();

        setField(packet, Objects.requireNonNull(getField(clas, "a")), name);
        setField(packet, Objects.requireNonNull(getField(clas, "b")), new ChatComponentText(player.getName()));
        setField(packet, Objects.requireNonNull(getField(clas, "c")), new ChatComponentText(prefix));
        setField(packet, Objects.requireNonNull(getField(clas, "d")), new ChatComponentText(suffix));
        setField(packet, Objects.requireNonNull(getField(clas, "e")), "ALWAYS");
        setField(packet, Objects.requireNonNull(getField(clas, "h")), pl);
        setField(packet, Objects.requireNonNull(getField(clas, "i")), 0);

        sendPacket(players, packet);
    }

    public static void sendNameTags(Player player, String prefix, String suffix, int priority) {

        for (Player all : Bukkit.getOnlinePlayers()) {
            sendNameTagPlayers(player, prefix, suffix, all, priority);
        }

    }


}

