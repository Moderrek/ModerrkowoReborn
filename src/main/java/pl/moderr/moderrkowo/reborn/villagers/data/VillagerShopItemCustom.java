package pl.moderr.moderrkowo.reborn.villagers.data;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.reborn.mysql.User;
import pl.moderr.moderrkowo.reborn.mysql.UserManager;
import pl.moderr.moderrkowo.reborn.opening.data.ModerrCaseEnum;
import pl.moderr.moderrkowo.reborn.opening.data.StorageItem;
import pl.moderr.moderrkowo.reborn.opening.data.StorageItemType;

public class VillagerShopItemCustom extends VillagerShopItem{

    private final VillagerShopItemCustomType type;
    public VillagerShopItemCustom(ItemStack item, int requiredQuestLevel, int requiredPlayerLevel, String description, int cost, VillagerShopItemCustomType type) {
        super(item, requiredQuestLevel, requiredPlayerLevel, description, cost);
        this.type = type;
    }

    @Override
    public void onBuy(Player p) {
        User u = UserManager.getUser(p.getUniqueId());
        if(type.equals(VillagerShopItemCustomType.KEYZWYKLA)){
            u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Key, ModerrCaseEnum.ZWYKLA), false);
        }
        if(type.equals(VillagerShopItemCustomType.KEYDNIADZIECKA)){
            u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Key, ModerrCaseEnum.SKAZENIA), false);
        }
        if(type.equals(VillagerShopItemCustomType.CHESTDNIADZIECKA)){
            u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Chest, ModerrCaseEnum.SKAZENIA), false);
        }
        if(type.equals(VillagerShopItemCustomType.CHESTZWYKLA)){
            u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), false);
        }
    }
}
