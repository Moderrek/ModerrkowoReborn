package pl.moderr.moderrkowo.reborn.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.moderr.moderrkowo.reborn.Main;
import pl.moderr.moderrkowo.reborn.utils.ItemStackUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AEnchantment implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ItemStack item = ItemStackUtils.generateEnchantmentBook(new HashMap<Enchantment, Integer>() {
            {
                put(Main.getInstance().customEnchants.get(args[0]), Integer.parseInt(args[1]));
            }
        });
        ((Player) sender).getInventory().addItem(item);
        ((Player) sender).getInventory().getItemInMainHand().addUnsafeEnchantment(Main.getInstance().hammerEnchantment, 1);
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(Main.getInstance().customEnchants.keySet());
        }
        return null;
    }
}
