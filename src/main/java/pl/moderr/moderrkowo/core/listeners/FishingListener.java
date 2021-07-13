package pl.moderr.moderrkowo.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.customitems.CustomItemsManager;
import pl.moderr.moderrkowo.core.economy.WithdrawCommand;
import pl.moderr.moderrkowo.core.mysql.LevelCategory;
import pl.moderr.moderrkowo.core.mysql.User;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.npc.data.data.PlayerNPCData;
import pl.moderr.moderrkowo.core.npc.data.npc.NPCData;
import pl.moderr.moderrkowo.core.npc.data.quest.Quest;
import pl.moderr.moderrkowo.core.npc.data.tasks.IQuestItem;
import pl.moderr.moderrkowo.core.npc.data.tasks.IQuestItemFish;
import pl.moderr.moderrkowo.core.npc.data.tasks.IQuestItemFishingRod;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.RandomUtils;
import pl.moderr.moderrkowo.core.utils.WeightedList;

import java.util.Objects;
import java.util.Random;

interface FishDrop {
    ItemStack getItemStack();
}

interface FishDropItemStack extends FishDrop {
    Material getMaterial();

    @Override
    default ItemStack getItemStack() {
        return new ItemStack(getMaterial(), 1);
    }
}

interface FishDropShulkerBox extends FishDrop {
    @Override
    default ItemStack getItemStack() {
        Bukkit.broadcastMessage(ColorUtils.color("&8[!] &eKtoś wyłowił skrzynkę"));
        return Main.shulkerDropBox.getRandomShulker();
    }
}

interface FishDropBanknot extends FishDrop {
    RandomRange getRange();

    @Override
    default ItemStack getItemStack() {
        return WithdrawCommand.generateItemStatic(1, getRange().getRandom());
    }
}

interface FishDropRandomItemStack extends FishDrop {
    RandomRange getRange();

    Material getMaterial();

    @Override
    default ItemStack getItemStack() {
        return new ItemStack(getMaterial(), getRange().getMax());
    }
}

public class FishingListener implements Listener {

    private final WeightedList<FishDrop> randomDrop = new WeightedList<>() {
        {

        }
    };

    public FishingListener() {
        randomDrop.put((FishDropItemStack) () -> Material.CHARCOAL, 24);
        randomDrop.put((FishDropItemStack) () -> Material.GOLD_INGOT, 4);
        randomDrop.put((FishDropItemStack) () -> Material.BOWL, 8);
        randomDrop.put((FishDropItemStack) () -> Material.STRING, 8);
        randomDrop.put((FishDropItemStack) () -> Material.GUNPOWDER, 10);
        randomDrop.put((FishDropItemStack) () -> Material.EMERALD, 4);
        randomDrop.put((FishDropItemStack) () -> Material.SADDLE, 2);
        randomDrop.put((FishDropItemStack) () -> Material.LEATHER, 12);
        randomDrop.put((FishDropItemStack) () -> Material.CLAY_BALL, 12);
        randomDrop.put((FishDropItemStack) () -> Material.LILY_PAD, 12);
        randomDrop.put((FishDropItemStack) () -> Material.STICK, 12);
        randomDrop.put((FishDropItemStack) () -> Material.TRIPWIRE_HOOK, 12);
        randomDrop.put(CustomItemsManager::getZwyklaChest, 3);
        randomDrop.put((FishDropItemStack) () -> Material.BOOK, 4);
        randomDrop.put((FishDropItemStack) () -> Material.COD, 120);
        randomDrop.put((FishDropItemStack) () -> Material.SALMON, 48);
        randomDrop.put((FishDropItemStack) () -> Material.TROPICAL_FISH, 20);
        randomDrop.put((FishDropItemStack) () -> Material.PUFFERFISH, 14);
        randomDrop.put((FishDropItemStack) () -> Material.BONE, 6);
        randomDrop.put((FishDropItemStack) () -> Material.EXPERIENCE_BOTTLE, 3);
        randomDrop.put((FishDropItemStack) () -> Material.NAME_TAG, 2);
        randomDrop.put((FishDropItemStack) () -> Material.ENDER_PEARL, 2);
        randomDrop.put((FishDropItemStack) () -> Material.COD, 8);
        randomDrop.put(new FishDropShulkerBox() {
        }, 1);
        randomDrop.put((FishDropBanknot) () -> new RandomRange(1, 100), 10);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFish(PlayerFishEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
            e.setExpToDrop(e.getExpToDrop() * 2);
            tryAddQuestData(e);
            try {
                ItemStack drop = randomDrop.get(new Random()).getItemStack();
                ((Item) Objects.requireNonNull(e.getCaught())).setItemStack(drop);
                UserManager.getUser(e.getPlayer().getUniqueId()).addExp(LevelCategory.Lowienie, getExp(drop, e.getPlayer().getInventory().getItemInMainHand()));
                double d = ((double) RandomUtils.getRandomInt(1, 9) / 15) * UserManager.getUser(e.getPlayer().getUniqueId()).getUserLevel().get(LevelCategory.Lowienie).getLevel();
                UserManager.getUser(e.getPlayer().getUniqueId()).addMoney(d);
                e.getPlayer().sendActionBar(ColorUtils.color("&a+" + ChatUtil.getMoney(d)));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void tryAddQuestData(PlayerFishEvent e) {
        if (e.getCaught() == null) {
            return;
        }
        Item is = (Item) e.getCaught();
        User u = UserManager.getUser(e.getPlayer().getUniqueId());
        PlayerNPCData data = null;
        for (PlayerNPCData villagers : u.getNPCSData().getNPCSData().values()) {
            if (villagers.isActiveQuest()) {
                data = villagers;
                break;
            }
        }
        if (data == null) {
            return;
        }
        NPCData villager = Main.getInstance().NPCManager.npcs.get(data.getNpcId());
        Quest quest = villager.getQuests().get(data.getQuestIndex());
        for (IQuestItem item : quest.getQuestItems()) {
            if (item instanceof IQuestItemFishingRod) {
                IQuestItemFishingRod craftItem = (IQuestItemFishingRod) item;
                int recipeAmount = 1;
                int items = data.getQuestItemData().get(craftItem.getQuestItemDataId());
                int temp = items;
                temp += recipeAmount;
                data.getQuestItemData().replace(craftItem.getQuestItemDataId(), items, temp);
                e.getPlayer().sendMessage(ColorUtils.color("&c&lQ &6» &2Zarzucono wędkę"));
                u.UpdateScoreboard();
            }
            if (item instanceof IQuestItemFish) {
                IQuestItemFish craftItem = (IQuestItemFish) item;
                if (craftItem.getMaterial().equals(is.getItemStack().getType())) {
                    int recipeAmount = 1;
                    int items = data.getQuestItemData().get(craftItem.getQuestItemDataId());
                    int temp = items;
                    temp += recipeAmount;
                    data.getQuestItemData().replace(craftItem.getQuestItemDataId(), items, temp);
                    e.getPlayer().sendMessage(ColorUtils.color("&c&lQ &6» &aZłowiono &2" + ChatUtil.materialName(is.getItemStack().getType())));
                    u.UpdateScoreboard();
                }
            }
        }
    }

    private double getExp(ItemStack e, ItemStack fishingRod) {
        if (e == null) {
            return 0;
        }
        int multiply = 1;
        if (fishingRod.hasEnchant(Enchantment.LUCK)) {
            multiply = fishingRod.getEnchantmentLevel(Enchantment.LUCK);
        }
        double value = 1.5;
        switch (e.getType()) {
            case COD:
                value = 0.3;
                break;
            case SALMON:
                value = 0.7;
                break;
            case PUFFERFISH:
                value = 1.2;
                break;
            case TROPICAL_FISH:
                value = 0.5;
                break;
        }
        return value * multiply;
    }

}
