package pl.moderr.moderrkowo.reborn.villagers.data;

import org.jetbrains.annotations.Contract;
import pl.moderr.moderrkowo.reborn.utils.ColorUtils;

import java.util.ArrayList;

public class VillagerData {

    private final String villagerName;
    private final ArrayList<Quest> quests;
    private boolean shop = false;
    private ArrayList<VillagerShopItem> shopItems;

    @Contract(pure = true)
    public VillagerData(String villagerName, ArrayList<Quest> quests) {
        this.villagerName = villagerName;
        this.quests = quests;
    }

    @Contract(pure = true)
    public VillagerData(String villagerName, ArrayList<Quest> quests, ArrayList<VillagerShopItem> items) {
        this.villagerName = villagerName;
        this.quests = quests;
        this.shopItems = items;
        shop = true;
    }

    public ArrayList<Quest> getQuests() {
        return quests;
    }

    public String getId() {
        return villagerName.toLowerCase();
    }

    public ArrayList<VillagerShopItem> getShopItems() {
        return shopItems;
    }

    public void setShopItems(ArrayList<VillagerShopItem> shopItems) {
        this.shopItems = shopItems;
    }

    public boolean isShop() {
        return shop;
    }

    public void setShop(boolean shop) {
        this.shop = shop;
    }

    public String getName() {
        StringBuilder prefix = new StringBuilder("&c&lQ ");
        if (shop) {
            prefix.append("&9&lS ");
        }
        return ColorUtils.color(prefix.toString() + "&7" + villagerName);
    }

    public String getCommandSpawnName() {
        StringBuilder prefix = new StringBuilder("&c&lQ ");
        if (shop) {
            prefix.append("&9&lS ");
        }
        return prefix.toString() + "&7" + villagerName;
    }

}
