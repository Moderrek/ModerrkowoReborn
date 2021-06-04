package pl.moderr.moderrkowo.reborn.villagers;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import pl.moderr.moderrkowo.reborn.Main;
import pl.moderr.moderrkowo.reborn.cuboids.CuboidsManager;
import pl.moderr.moderrkowo.reborn.mysql.User;
import pl.moderr.moderrkowo.reborn.mysql.UserManager;
import pl.moderr.moderrkowo.reborn.opening.data.ModerrCaseEnum;
import pl.moderr.moderrkowo.reborn.opening.data.StorageItem;
import pl.moderr.moderrkowo.reborn.opening.data.StorageItemType;
import pl.moderr.moderrkowo.reborn.utils.*;
import pl.moderr.moderrkowo.reborn.villagers.data.*;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class VillagerManager implements Listener {

    public final Map<String, VillagerData> villagers = new HashMap<>();
    private final IdentityHashMap<UUID, VillagerDelayData> questDelay = new IdentityHashMap<>();

    public VillagerManager() {
        //<editor-fold> Villagers
        AddVillager(new VillagerData("Lowca", new ArrayList<Quest>() {
            {
                //<editor-fold> 1. Ekwipunek
                add(new Quest(
                        "Ekwipunek",
                        "Potrzebuje pilnie miecz.\nPomożesz mi?",
                        QuestDifficulty.EASY, new ArrayList<IQuestItem>() {
                            {
                                add(new IQuestItemCraft() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.WOODEN_SWORD;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 1;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "1";
                                    }
                                });
                                add(new IQuestItemGive() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.WOODEN_SWORD;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 1;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "2";
                                    }
                                });
                            }
                        }, new ArrayList<IQuestReward>() {
                    {
                        add((IQuestRewardMoney) () -> 25);
                        add(new IQuestRewardCustom() {
                            @Override
                            public String label() {
                                return "Skrzynia ZWYKŁA";
                            }

                            @Override
                            public void Action(Player p, User u) {
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), false);
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Key, ModerrCaseEnum.ZWYKLA), false);
                            }
                        });
                    }
                }
                ));
                //</editor-fold>
                //<editor-fold> 2. Potyczki 2
                add(new Quest(
                        "Potyczki 1",
                        "Masz już ekwipunek\nwięc możesz zabić parę potworów!",
                        QuestDifficulty.EASY, new ArrayList<IQuestItem>() {
                            {
                                add(new IQuestItemKill() {
                                    @Override
                                    public EntityType getEntityType() {
                                        return EntityType.ZOMBIE;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 15;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "1";
                                    }
                                });
                                add(new IQuestItemKill() {
                                    @Override
                                    public EntityType getEntityType() {
                                        return EntityType.CREEPER;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 1;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "2";
                                    }
                                });
                            }
                        }, new ArrayList<IQuestReward>() {
                    {
                        add((IQuestRewardMoney) () -> 65);
                        add((IQuestRewardExp) () -> 2);
                        add(new IQuestRewardCustom() {
                            @Override
                            public String label() {
                                return "Skrzynia ZWYKŁA";
                            }

                            @Override
                            public void Action(Player p, User u) {
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), false);
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Key, ModerrCaseEnum.ZWYKLA), false);
                            }
                        });
                    }
                }));
                //</editor-fold>
                //<editor-fold> 3. Przygotowania do działki
                add(new Quest(
                        "Płomyk",
                        "Słyszałem że brakuje ci\nswojego własnego terenu.\nPomogę Ci!\nAle musisz coś dla mnie zrobić.\nZnam alchemika ale nie mam materiałow\naby zrobił kości płomyka\n/craftingdzialka",
                        QuestDifficulty.NORMAL, new ArrayList<IQuestItem>() {
                            {
                                add(new IQuestItemKill() {
                                    @Override
                                    public EntityType getEntityType() {
                                        return EntityType.SKELETON;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 20;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "1";
                                    }
                                });
                                add(new IQuestItemGive() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.LAVA_BUCKET;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 1;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "2";
                                    }
                                });
                                add(new IQuestItemGive() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.BONE;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 6;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "3";
                                    }
                                });
                            }
                        }, new ArrayList<IQuestReward>() {
                    {
                        add((IQuestRewardItemStack) () -> new ItemStack(Material.BLAZE_ROD, 5));
                        add((IQuestRewardExp) () -> 4);
                        add(new IQuestRewardCustom() {
                            @Override
                            public String label() {
                                return "Skrzynia ZWYKŁA";
                            }

                            @Override
                            public void Action(Player p, User u) {
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), false);
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Key, ModerrCaseEnum.ZWYKLA), false);
                            }
                        });
                    }
                }
                ));
                //</editor-fold>
                //<editor-fold> 4. Bagna
                add(new Quest(
                        "Bagna",
                        "Stary.. nikt nie chce się\n podjąć tej brudnej roboty\nwierze w Ciebie!\nOgarnij mi troche szlamu",
                        QuestDifficulty.HARD, new ArrayList<IQuestItem>() {
                            {
                                add(new IQuestItemGive() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.SLIME_BALL;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 20;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "1";
                                    }
                                });
                            }
                        }, new ArrayList<IQuestReward>() {
                    {
                        add((IQuestRewardMoney) () -> 1500);
                        add((IQuestRewardExp) () -> 12);
                        add(new IQuestRewardCustom() {
                            @Override
                            public String label() {
                                return "Skrzynia ZWYKŁA";
                            }

                            @Override
                            public void Action(Player p, User u) {
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), false);
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Key, ModerrCaseEnum.ZWYKLA), false);
                            }
                        });
                    }
                }
                ));
                //</editor-fold>
                //<editor-fold> 5. Przeróbka
                add(new Quest(
                        "Przeróbka",
                        "Csii... nie mogę zdradzić",
                        QuestDifficulty.NORMAL, new ArrayList<IQuestItem>() {
                            {
                                add(new IQuestItemGive() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.FERMENTED_SPIDER_EYE;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 30;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "1";
                                    }
                                });
                                add(new IQuestItemCraft() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.GLISTERING_MELON_SLICE;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 5;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "2";
                                    }
                                });
                                add(new IQuestItemGive() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.MAGMA_CREAM;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 10;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "3";
                                    }
                                });
                            }
                        }, new ArrayList<IQuestReward>() {
                    {
                        add((IQuestRewardMoney) () -> 500);
                        add((IQuestRewardItemStack) () -> new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1));
                        add((IQuestRewardExp) () -> 31);
                        add(new IQuestRewardCustom() {
                            @Override
                            public String label() {
                                return "Skrzynia ZWYKŁA";
                            }

                            @Override
                            public void Action(Player p, User u) {
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), false);
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Key, ModerrCaseEnum.ZWYKLA), false);
                            }
                        });
                    }
                }
                ));
                add(new Quest("Tarcza", "Przygotuj się do\nużywania tarczy", QuestDifficulty.NORMAL, new ArrayList<IQuestItem>(){
                    {
                        add(new IQuestItemCraft() {
                            @Override
                            public Material getMaterial() {
                                return Material.ARROW;
                            }

                            @Override
                            public int getCount() {
                                return 128;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.ARROW;
                            }

                            @Override
                            public int getCount() {
                                return 32;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.OAK_PLANKS;
                            }

                            @Override
                            public int getCount() {
                                return 8;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "3";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.EMERALD;
                            }

                            @Override
                            public int getCount() {
                                return 1;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "4";
                            }
                        });
                    }
                }, new ArrayList<IQuestReward>(){
                    {
                        add((IQuestRewardExp) () -> 18);
                        add(new IQuestRewardCustom() {
                            @Override
                            public String label() {
                                return "Odblokowanie Tarczy";
                            }

                            @Override
                            public void Action(Player p, User u) {

                            }
                        });
                        add(new IQuestRewardCustom() {
                            @Override
                            public String label() {
                                return "Skrzynia ZWYKŁA";
                            }

                            @Override
                            public void Action(Player p, User u) {
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), false);
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Key, ModerrCaseEnum.ZWYKLA), false);
                            }
                        });
                    }
                }));
                //</editor-fold>
            }
        }, new ArrayList<VillagerShopItem>() {
            {
                add(new VillagerShopItem(new ItemStack(Material.FEATHER, 8), 1, 1, "Nie uszkodzone piórko kury", 30, 27, false));
                add(new VillagerShopItem(new ItemStack(Material.ROTTEN_FLESH, 8), 1, 1, "Mięso potwora", 11, 9, false));

                add(new VillagerShopItem(new ItemStack(Material.GUNPOWDER, 1), 2, 1, "Proch strzelniczy", 90, 38, false));
                add(new VillagerShopItem(new ItemStack(Material.POISONOUS_POTATO, 8), 2, 1, "Zepsuty ziemniak", 55, 55, false));

                add(new VillagerShopItem(new ItemStack(Material.STRING, 4), 3, 1, "Jedwabna nitka", 33, 10, false));
                add(new VillagerShopItem(new ItemStack(Material.SPIDER_EYE, 5), 3, 1, "Oko sześcionoga", 25, 14, false));

                add(new VillagerShopItem(new ItemStack(Material.ARROW, 16), 4, 1, "Naostrzona kamienna strzała", 100, 25, false));
                add(new VillagerShopItem(new ItemStack(Material.BONE, 2), 4, 1, "Wapienna kość", 12000, 39, true));
                add(new VillagerShopItem(new ItemStack(Material.BLAZE_ROD, 1), 4, 1, "Kość płomyka", 1520, 650, false));

                add(new VillagerShopItem(new ItemStack(Material.SLIME_BALL), 5, 1, "Kleista maź", 275, 99, false));
                add(new VillagerShopItem(new ItemStack(Material.RABBIT_FOOT, 2), 5, 1, "Noga rzadkiego królika", 120, 70, false));

                add(new VillagerShopItem(new ItemStack(Material.BONE_MEAL, 1), 6, 4, "Wapienna mąka", 2000, 200, true));
                add(new VillagerShopItem(new ItemStack(Material.FERMENTED_SPIDER_EYE), 6, 1, "Fermentowane oko pająka", 75, 48, false));

                add(new VillagerShopItem(new ItemStack(Material.SHIELD), 7, 4, "Tarcza Łowcy", 1000));
            }
        }));
        AddVillager(new VillagerData("Elektryk", new ArrayList<Quest>() {
            {
                add(new Quest("Redstone", "Proszę wykop mi\ntrochę redstona.", QuestDifficulty.NORMAL, new ArrayList<IQuestItem>(){
                    {
                        add(new IQuestItemBreak() {
                            @Override
                            public Material getMaterial() {
                                return Material.REDSTONE_ORE;
                            }

                            @Override
                            public int getCount() {
                                return 50;
                            }

                            @Override
                            public boolean blockSilk() {
                                return true;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.REDSTONE;
                            }

                            @Override
                            public int getCount() {
                                return 100;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }
                        });
                    }
                }, new ArrayList<IQuestReward>(){
                    {
                        add((IQuestRewardMoney) () -> 1250);
                        add((IQuestRewardExp) () -> 8);
                        add(new IQuestRewardCustom() {
                            @Override
                            public String label() {
                                return "Skrzynia ZWYKŁA";
                            }

                            @Override
                            public void Action(Player p, User u) {
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), false);
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Key, ModerrCaseEnum.ZWYKLA), false);
                            }
                        });
                    }
                }));
                add(new Quest("Dostęp technologii", "Zapłać mi a dam ci\ndostęp do technologi.", QuestDifficulty.EASY, new ArrayList<IQuestItem>(){
                    {
                        add(new IQuestItemPay() {
                            @Override
                            public int getCount() {
                                return 20000;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }
                        });
                    }
                }, new ArrayList<IQuestReward>(){
                    {
                        add((IQuestRewardExp) () -> 11);
                        add(new IQuestRewardCustom() {
                            @Override
                            public String label() {
                                return "Skrzynia ZWYKŁA x10";
                            }

                            @Override
                            public void Action(Player p, User u) {
                                u.getUserChestStorage().addItem(new StorageItem(10, StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), false);
                                u.getUserChestStorage().addItem(new StorageItem(10, StorageItemType.Key, ModerrCaseEnum.ZWYKLA), false);
                            }
                        });
                    }
                }));
            }
        }, new ArrayList<VillagerShopItem>() {
            {
                add(new VillagerShopItem(new ItemStack(Material.HOPPER), 1, 3, "Lej na przedmioty", 9500));
                add(new VillagerShopItem(new ItemStack(Material.OBSERVER), 3, 4, "Ten potężny przedmiot\npatrzy na Ciebie", 15250));
            }
        }));
        AddVillager(new VillagerData("Farmer", new ArrayList<Quest>(){
            {
                add(new Quest("Towary Farmera", "Chcesz coś kupić?", QuestDifficulty.EASY, new ArrayList<IQuestItem>(){
                    {
                        add(new IQuestItemPay() {
                            @Override
                            public int getCount() {
                                return 200;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }
                        });
                    }
                }, new ArrayList<IQuestReward>(){
                    {
                        add((IQuestRewardItemStack) () -> new ItemStack(Material.BEETROOT,64));
                        add((IQuestRewardItemStack) () -> new ItemStack(Material.BEETROOT_SEEDS, 2));
                        add((IQuestRewardItemStack) () -> new ItemStack(Material.WHEAT, 48));
                        add(new IQuestRewardCustom() {
                            @Override
                            public String label() {
                                return "Skrzynia ZWYKŁA";
                            }

                            @Override
                            public void Action(Player p, User u) {
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), false);
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Key, ModerrCaseEnum.ZWYKLA), false);
                            }
                        });
                    }
                }));
                add(new Quest("Buraki cukrowe", "Mam dobre kontrakty,\nale potrzebuje pomocnika.\nPodzielimy się?", QuestDifficulty.HARD, new ArrayList<IQuestItem>(){
                    {
                        add(new IQuestItemCollect() {
                            @Override
                            public Material getMaterial() {
                                return Material.BEETROOT_SEEDS;
                            }

                            @Override
                            public int getCount() {
                                return 256;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.BEETROOT;
                            }

                            @Override
                            public int getCount() {
                                return 256;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }
                        });
                    }
                }, new ArrayList<IQuestReward>(){
                    {
                        add((IQuestRewardMoney) () -> 814);
                        add((IQuestRewardExp) () -> 25);
                        add(new IQuestRewardCustom() {
                            @Override
                            public String label() {
                                return "Skrzynia ZWYKŁA";
                            }

                            @Override
                            public void Action(Player p, User u) {
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), false);
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Key, ModerrCaseEnum.ZWYKLA), false);
                            }
                        });
                    }
                }));
                add(new Quest("Zbiór Marchwi", "Zlecam Ci to zadanie.", QuestDifficulty.NORMAL, new ArrayList<IQuestItem>(){
                    {
                        add(new IQuestItemCollect() {
                            @Override
                            public Material getMaterial() {
                                return Material.CARROT;
                            }

                            @Override
                            public int getCount() {
                                return 128;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }
                        });
                    }
                }, new ArrayList<IQuestReward>(){
                    {
                        add((IQuestRewardMoney) () -> 414);
                        add((IQuestRewardExp) () -> 25);
                        add(new IQuestRewardCustom() {
                            @Override
                            public String label() {
                                return "Skrzynia ZWYKŁA";
                            }

                            @Override
                            public void Action(Player p, User u) {
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), false);
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Key, ModerrCaseEnum.ZWYKLA), false);
                            }
                        });
                    }
                }));
                add(new Quest("Pszenica", "", QuestDifficulty.NORMAL, new ArrayList<IQuestItem>(){
                    {
                        add(new IQuestItemCollect() {
                            @Override
                            public Material getMaterial() {
                                return Material.WHEAT_SEEDS;
                            }

                            @Override
                            public int getCount() {
                                return 128;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }
                        });
                    }
                }, new ArrayList<IQuestReward>(){
                    {
                        add((IQuestRewardMoney) () -> 1550);
                        add((IQuestRewardExp) () -> 22);
                        add(new IQuestRewardCustom() {
                            @Override
                            public String label() {
                                return "Skrzynia ZWYKŁA";
                            }

                            @Override
                            public void Action(Player p, User u) {
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), false);
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Key, ModerrCaseEnum.ZWYKLA), false);
                            }
                        });
                    }
                }));
                add(new Quest("Trzcina", "Dostałęm zlecenie na\ntwórstwo papieru", QuestDifficulty.NORMAL, new ArrayList<IQuestItem>(){
                    {
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.SUGAR_CANE;
                            }

                            @Override
                            public int getCount() {
                                return 224;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }
                        });
                    }
                }, new ArrayList<IQuestReward>(){
                    {
                        add((IQuestRewardMoney) () -> 1050);
                        add((IQuestRewardExp) () -> 10);
                        add(new IQuestRewardCustom() {
                            @Override
                            public String label() {
                                return "Skrzynia ZWYKŁA";
                            }

                            @Override
                            public void Action(Player p, User u) {
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), false);
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Key, ModerrCaseEnum.ZWYKLA), false);
                            }
                        });
                    }
                }));
                add(new Quest("Pustynia", "", QuestDifficulty.HARD, new ArrayList<IQuestItem>(){
                    {
                        add(new IQuestItemBreak() {
                            @Override
                            public Material getMaterial() {
                                return Material.CACTUS;
                            }

                            @Override
                            public int getCount() {
                                return 32;
                            }

                            @Override
                            public boolean blockSilk() {
                                return false;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.CACTUS;
                            }

                            @Override
                            public int getCount() {
                                return 128;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.SAND;
                            }

                            @Override
                            public int getCount() {
                                return 256;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "3";
                            }
                        });
                    }
                }, new ArrayList<IQuestReward>(){
                    {
                        add((IQuestRewardMoney) () -> 15550);
                        add((IQuestRewardExp) () -> 32);
                        add(new IQuestRewardCustom() {
                            @Override
                            public String label() {
                                return "Skrzynia ZWYKŁA x10";
                            }

                            @Override
                            public void Action(Player p, User u) {
                                u.getUserChestStorage().addItem(new StorageItem(10, StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), false);
                                u.getUserChestStorage().addItem(new StorageItem(10, StorageItemType.Key, ModerrCaseEnum.ZWYKLA), false);
                            }
                        });
                    }
                }));
                add(new Quest("Vines", "", QuestDifficulty.HARD, new ArrayList<IQuestItem>(){
                    {
                        add(new IQuestItemBreak() {
                            @Override
                            public Material getMaterial() {
                                return Material.VINE;
                            }

                            @Override
                            public int getCount() {
                                return 32;
                            }

                            @Override
                            public boolean blockSilk() {
                                return false;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.VINE;
                            }

                            @Override
                            public int getCount() {
                                return 32;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.JUNGLE_LOG;
                            }

                            @Override
                            public int getCount() {
                                return 128;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "3";
                            }
                        });
                    }
                }, new ArrayList<IQuestReward>(){
                    {
                        add((IQuestRewardMoney) () -> 8000);
                        add((IQuestRewardExp) () -> 52);
                        add(new IQuestRewardCustom() {
                            @Override
                            public String label() {
                                return "Skrzynia ZWYKŁA";
                            }

                            @Override
                            public void Action(Player p, User u) {
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), false);
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Key, ModerrCaseEnum.ZWYKLA), false);
                            }
                        });
                    }
                }));
                add(new Quest("Egzotyka", "", QuestDifficulty.HARD, new ArrayList<IQuestItem>(){
                    {
                        add(new IQuestItemBreak() {
                            @Override
                            public Material getMaterial() {
                                return Material.MELON;
                            }

                            @Override
                            public int getCount() {
                                return 96;
                            }

                            @Override
                            public boolean blockSilk() {
                                return false;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.COCOA_BEANS;
                            }

                            @Override
                            public int getCount() {
                                return 128;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }
                        });
                        add(new IQuestItemPay() {
                            @Override
                            public int getCount() {
                                return 30000;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "3";
                            }
                        });
                        add(new IQuestItemCraft() {
                            @Override
                            public Material getMaterial() {
                                return Material.COOKIE;
                            }

                            @Override
                            public int getCount() {
                                return 2000;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "4";
                            }
                        });
                    }
                }, new ArrayList<IQuestReward>(){
                    {
                        add((IQuestRewardMoney) () -> 75000);
                        add((IQuestRewardExp) () -> 122);
                    }
                }));
            }
        }, new ArrayList<VillagerShopItem>(){
            {
                add(new VillagerShopItem(new ItemStack(Material.WHEAT, 16), 1, 1, "Pospolita roślina", 1200, 9, false));
                add(new VillagerShopItem(new ItemStack(Material.WHEAT_SEEDS, 8), 1, 1, "Nasiona pospolitej rośliny", 12, 3, false));
                add(new VillagerShopItem(new ItemStack(Material.BEETROOT_SEEDS, 16), 3, 3, "Nasiona buraka", 96, 8, false));
                add(new VillagerShopItem(new ItemStack(Material.BEETROOT, 16), 4, 4, "Sprzedaje na kontrakcie", 180, 45, false));
                add(new VillagerShopItem(new ItemStack(Material.CARROT, 32), 4, 3, "Umyta marchewka", 500, 125, false));
                add(new VillagerShopItem(new ItemStack(Material.SUGAR_CANE, 24), 5, 1, "Nadaje się do papieru", 1000, 150, false));
                add(new VillagerShopItem(new ItemStack(Material.CACTUS, 32), 7, 1, "Kaktus", 896, 224, false));
                add(new VillagerShopItem(new ItemStack(Material.VINE, 16), 8, 1, "", 704, 176, true));
                add(new VillagerShopItem(new ItemStack(Material.COCOA_BEANS, 16), 9, 5, "Kakao", 32000, 3200, true));
                add(new VillagerShopItem(new ItemStack(Material.MELON_SLICE, 8), 9, 4, "", 2384, 288, true));
                add(new VillagerShopItem(new ItemStack(Material.COOKIE, 64), 9, 6, "", 34920, 3984, false));
                add(new VillagerShopItem(new ItemStack(Material.NETHER_WART, 1), 9, 6, "", 0, 9120, true));
                add(new VillagerShopItem(new ItemStack(Material.GOLDEN_APPLE, 4), 9, 6, "", 0, 5000, false));
                add(new VillagerShopItem(new ItemStack(Material.WEEPING_VINES, 1), 9, 6, "", 0, 500, true));
                add(new VillagerShopItem(new ItemStack(Material.TWISTING_VINES, 1), 9, 6, "", 0, 750, false));
            }
        }));
        AddVillager(new VillagerData("Handlarz", new ArrayList<>(), new ArrayList<VillagerShopItem>(){
            {
                add(new VillagerShopItem(CuboidsManager.getCuboidItem(1), 0,1,"Zakup swoją działkę!",8000));
                add(new VillagerShopItemCustom(ItemStackUtils.createGuiItem(Material.CHEST,1,ColorUtils.color("&7Skrzynia &a&lZWYKŁA")), 0,0, "Kup skrzynie do otwierania", 400, VillagerShopItemCustomType.CHESTZWYKLA));
                add(new VillagerShopItemCustom(ItemStackUtils.createGuiItem(Material.TRIPWIRE_HOOK,1,ColorUtils.color("&7Klucz &a&lZWYKŁA")), 0,0, "Kup klucz do otwierania", 1250, VillagerShopItemCustomType.KEYZWYKLA));

                    add(new VillagerShopItemCustom(ItemStackUtils.createGuiItem(Material.CRIMSON_NYLIUM,1,ColorUtils.color("&7Skrzynia &c&lSKAŻENIA")), 0,0, "Kup skrzynie do otwierania", 3500, VillagerShopItemCustomType.CHESTDNIADZIECKA));
                add(new VillagerShopItemCustom(ItemStackUtils.createGuiItem(Material.TRIPWIRE_HOOK,1,ColorUtils.color("&7Klucz &c&lSKAŻENIA")), 0,0, "Kup klucz do otwierania", 12500, VillagerShopItemCustomType.KEYDNIADZIECKA));
            }
        }));
        AddVillager(new VillagerData("Rybak", new ArrayList<Quest>(){
            {
                add(new Quest("Wędka 1", "Niedługo opis", QuestDifficulty.EASY, new ArrayList<IQuestItem>(){
                    {
                        add(new IQuestItemCraft() {
                            @Override
                            public Material getMaterial() {
                                return Material.FISHING_ROD;
                            }

                            @Override
                            public int getCount() {
                                return 4;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.FISHING_ROD;
                            }

                            @Override
                            public int getCount() {
                                return 4;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }
                        });
                        add(new IQuestItemPay() {
                            @Override
                            public int getCount() {
                                return 20;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "3";
                            }
                        });
                    }
                }, new ArrayList<IQuestReward>(){
                    {
                        add((IQuestRewardItemStack) () -> new ItemStack(Material.FISHING_ROD,1));
                        add((IQuestRewardExp) () -> 8);
                        add(new IQuestRewardCustom() {
                            @Override
                            public String label() {
                                return "Skrzynia ZWYKŁA";
                            }

                            @Override
                            public void Action(Player p, User u) {
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), false);
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Key, ModerrCaseEnum.ZWYKLA), false);
                            }
                        });
                    }
                }));
                add(new Quest("Glony", "Niedługo opis", QuestDifficulty.HARD, new ArrayList<IQuestItem>(){
                    {
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.KELP;
                            }

                            @Override
                            public int getCount() {
                                return 256;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }
                        });
                        add(new IQuestItemKill() {
                            @Override
                            public EntityType getEntityType() {
                                return EntityType.DROWNED;
                            }

                            @Override
                            public int getCount() {
                                return 1;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }
                        });
                    }
                }, new ArrayList<IQuestReward>(){
                    {
                        add((IQuestRewardItemStack) () -> new ItemStack(Material.KELP,256));
                        add((IQuestRewardExp) () -> 10);
                        add((IQuestRewardMoney) () -> 275);
                        add(new IQuestRewardCustom() {
                            @Override
                            public String label() {
                                return "Skrzynia ZWYKŁA";
                            }

                            @Override
                            public void Action(Player p, User u) {
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), false);
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Key, ModerrCaseEnum.ZWYKLA), false);
                            }
                        });
                    }
                }));
                add(new Quest("Suszenie glonów", "Niedługo opis", QuestDifficulty.HARD, new ArrayList<IQuestItem>(){
                    {
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.DRIED_KELP_BLOCK;
                            }

                            @Override
                            public int getCount() {
                                return 32;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }
                        });
                        add(new IQuestItemCraft() {
                            @Override
                            public Material getMaterial() {
                                return Material.DRIED_KELP_BLOCK;
                            }

                            @Override
                            public int getCount() {
                                return 32;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }
                        });
                    }
                }, new ArrayList<IQuestReward>(){
                    {
                        add((IQuestRewardItemStack) () -> ItemStackUtils.addEnchantment(new ItemStack(Material.FISHING_ROD,1), Enchantment.LURE,2));
                        add((IQuestRewardExp) () -> 15);
                        add((IQuestRewardMoney) () -> 3375);
                        add(new IQuestRewardCustom() {
                            @Override
                            public String label() {
                                return "Skrzynia ZWYKŁA";
                            }

                            @Override
                            public void Action(Player p, User u) {
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), false);
                                u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Key, ModerrCaseEnum.ZWYKLA), false);
                            }
                        });
                    }
                }));
            }
        }, new ArrayList<VillagerShopItem>() {
            {
                add(new VillagerShopItem(new ItemStack(Material.STRING, 4), 0, 0, "", 0, 10, false));
            }
        }));
        AddVillager(new VillagerData("Gornik", new ArrayList<>(), new ArrayList<VillagerShopItem>() {
            {
                add(new VillagerShopItem(new ItemStack(Material.STONE, 16), 0, 0, "", 300, 48, false));
                add(new VillagerShopItem(new ItemStack(Material.GRANITE, 16), 0, 0, "", 300, 80, false));
                add(new VillagerShopItem(new ItemStack(Material.ANDESITE, 16), 0, 0, "", 300, 80, false));
                add(new VillagerShopItem(new ItemStack(Material.DIORITE, 16), 0, 0, "", 300, 80, false));
                add(new VillagerShopItem(new ItemStack(Material.GRAVEL, 32), 0, 0, "", 300, 80, false));
                add(new VillagerShopItem(new ItemStack(Material.FLINT, 32), 0, 0, "", 1000, 256, false));
                add(new VillagerShopItem(new ItemStack(Material.COAL, 1), 0, 0, "", 275, 55, false));
                add(new VillagerShopItem(new ItemStack(Material.CHARCOAL, 1), 0, 0, "", 275, 57, false));
                add(new VillagerShopItem(new ItemStack(Material.IRON_INGOT, 1), 0, 0, "", 2550, 150, false));
                add(new VillagerShopItem(new ItemStack(Material.NETHER_BRICK, 1), 0, 0, "", 1000, 250, false));
                add(new VillagerShopItem(new ItemStack(Material.GOLD_INGOT, 1), 0, 0, "", 26500, 500, false));
                add(new VillagerShopItem(new ItemStack(Material.DIAMOND, 1), 0, 0, "", 75000, 2575, false));
                add(new VillagerShopItem(new ItemStack(Material.QUARTZ, 1), 0, 0, "", 0, 165, false));
                add(new VillagerShopItem(new ItemStack(Material.NAME_TAG, 2), 0, 0, "", 10000, 1000, false));
                add(new VillagerShopItem(new ItemStack(Material.CREEPER_HEAD, 1), 0, 0, "", 0, 10000, false));
                add(new VillagerShopItem(new ItemStack(Material.ZOMBIE_HEAD, 1), 0, 0, "", 0, 10000, false));
                add(new VillagerShopItem(new ItemStack(Material.SKELETON_SKULL, 1), 0, 0, "", 0, 10000, false));
                add(new VillagerShopItem(new ItemStack(Material.WITHER_SKELETON_SKULL, 1), 0, 0, "", 0, 200000, false));
                add(new VillagerShopItem(ItemStackUtils.addEnchantment(ItemStackUtils.createItem(Material.NETHERITE_PICKAXE, ColorUtils.color("&cMłot"), ""), Main.getInstance().hammerEnchantment, 1), 0, 6, "Kilof 3x3 [Połącz z kilofem]", 3000000));
            }
        }));
        //</editor-fold> Villagers
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }
    public void AddVillager(VillagerData shop) {
        villagers.put(shop.getId().toLowerCase(), shop);
        ModerrkowoLog.LogAdmin("Zarejestrowano nowy sklep " + shop.getName());
    }

    public static int fits(ItemStack stack, Inventory inv) {
        ItemStack[] contents = inv.getContents();
        int result = 0;

        for (ItemStack is : contents)
            if (is == null)
                result += stack.getMaxStackSize();
            else if (is.isSimilar(stack))
                result += Math.max(stack.getMaxStackSize() - is.getAmount(), 0);

        return result;
    }
    public static int getMaxCraftAmount(CraftingInventory inv) {
        if (inv.getResult() == null)
            return 0;

        int resultCount = inv.getResult().getAmount();
        int materialCount = Integer.MAX_VALUE;

        for (ItemStack is : inv.getMatrix())
            if (is != null && is.getAmount() < materialCount)
                materialCount = is.getAmount();

        return resultCount * materialCount;
    }
    public ItemStack getItemOfShop(User u, VillagerData villagerData, VillagerShopItem item) {
        PlayerVillagersData data = u.getVillagersData();
        PlayerVillagerData playerVillagerData = data.getVillagersData().get(villagerData.getId());
        Material mat;
        String name;
        boolean unlocked = playerVillagerData.getQuestIndex() + 1 >= item.getRequiredQuestLevel() && u.getLevel() >= item.getRequiredPlayerLevel();
        if (unlocked) {
            mat = item.getItem().getType();
            if(item.getItem().getItemMeta().hasDisplayName()){
                name = ColorUtils.color("&6" + item.getItem().getItemMeta().getDisplayName() + " &8[&9Poziom " + item.getRequiredQuestLevel() + "&8]");
            }else{
                name = ColorUtils.color("&6" + ChatUtil.materialName(item.getItem().getType()) + " &8[&9Poziom " + item.getRequiredQuestLevel() + "&8]");
            }
        } else {
            mat = Material.IRON_BARS;
            name = ColorUtils.color("&c&k" + ChatUtil.materialName(item.getItem().getType()));
        }
        ItemStack i = item.getItem().clone();
        i.setType(mat);
        if(mat.equals(Material.IRON_BARS)){
            i.setAmount(1);
        }
        if (!unlocked) {
            for (Enchantment enchs : i.getEnchantments().keySet()) {
                i.removeEnchantment(enchs);
            }
        }
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> lore = new ArrayList<>();
        if (unlocked) {
            //lore.add(ColorUtils.color("&eOpis"));
            if(item.isBoostedSell()){
                lore.add(ColorUtils.color("&a⬆ &9Wzmocniona sprzedaż &a⬆"));
                meta.addEnchant(Enchantment.DURABILITY,1,true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            lore.add(ColorUtils.color(ColorUtils.color("&8" + item.getDescription())));
            lore.add(" ");
            if (item.canSell()) {
                if(item.getCost() != 0){
                    if (item.getCost() <= u.getMoney()) {
                        lore.add(ColorUtils.color("&7Cena kupna: &a" + ChatUtil.getMoney(item.getCost())));
                    } else {
                        lore.add(ColorUtils.color("&7Cena kupna: &c" + ChatUtil.getMoney(item.getCost())));
                    }
                }
                if(ItemStackUtils.getCountOfMaterial(u.getPlayer(), item.getItem().getType()) >= item.getItem().getAmount()){
                    if(item.isBoostedSell()){
                        lore.add(ColorUtils.color("&7Cena sprzedaży: &8&n&m" + ChatUtil.getMoney(item.getSellCost()) + "&a " + ChatUtil.getMoney(item.getFinalCost()) + " &a⬆"));
                    }else{
                        lore.add(ColorUtils.color("&7Cena sprzedaży: &a" + ChatUtil.getMoney(item.getFinalCost())));
                    }
                }else{
                    if(item.isBoostedSell()){
                        lore.add(ColorUtils.color("&7Cena sprzedaży: &8&n&m" + ChatUtil.getMoney(item.getSellCost()) + "&c " + ChatUtil.getMoney(item.getFinalCost()) + " &a⬆"));
                    }else{
                        lore.add(ColorUtils.color("&7Cena sprzedaży: &c" + ChatUtil.getMoney(item.getFinalCost())));
                    }
                }
            } else {
                if (item.getCost() <= u.getMoney()) {
                    lore.add(ColorUtils.color("&7Cena: &a" + ChatUtil.getMoney(item.getCost())));
                } else {
                    lore.add(ColorUtils.color("&7Cena: &c" + ChatUtil.getMoney(item.getCost())));
                }
            }
            if(item.getCost() != 0){
                if (item.canSell()) {
                    lore.add(ColorUtils.color("&7LPM aby zakupić &8| &cPPM aby sprzedać"));
                    lore.add(ColorUtils.color("&8Sprzedaj z SHIFTem aby sprzedać stack"));
                } else {
                    lore.add(ColorUtils.color("&7Kliknij aby zakupić"));
                }
            }else{
                if (item.canSell()) {
                    lore.add(ColorUtils.color("&7PPM aby sprzedać"));
                    lore.add(ColorUtils.color("&8Sprzedaj z SHIFTem aby sprzedać stack"));
                } else {
                    lore.add(ColorUtils.color("&7Brak dostępnych akcji"));
                }
            }
        } else {
            if(playerVillagerData.getQuestIndex() + 1 < item.getRequiredQuestLevel()){
                lore.add(ColorUtils.color("&c✘ &eZostanie odblokowane na " + item.getRequiredQuestLevel() + " poz. zadań"));
            }else{
                lore.add(ColorUtils.color("&a✔ Posiadasz wystarczający poziom zadań"));
            }
            if(u.getLevel() < item.getRequiredPlayerLevel()){
                lore.add(ColorUtils.color("&c✘ &eZostanie odblokowane na " + item.getRequiredPlayerLevel() + " poz. postaci"));
            }else{
                lore.add(ColorUtils.color("&a✔ Posiadasz wystarczający poziom postaci"));
            }
        }
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }
    public void loreCheck(ArrayList<String> lore, final int count, final boolean isActiveQuest, final int questProgress, final String itemPrefix, final String suffix) {
        int value = count;
        if (isActiveQuest) {
            value = count - questProgress;
        }
        boolean hasItem = value <= 0;
        if (hasItem) {
            String countS = "";
            if (count != 1) {
                countS = count + "";
            }
            lore.add(ColorUtils.color("&a✔ " + itemPrefix + " " + count + " " + suffix));
        } else {
            lore.add(ColorUtils.color("&c✘ " + itemPrefix + " " + value + " " + suffix));
        }
    }
    public void loreCheckMoney(ArrayList<String> lore, final int count, final boolean isActiveQuest, final int questProgress, final String itemPrefix) {
        int value = count;
        if (isActiveQuest) {
            value = count - questProgress;
        }
        boolean hasItem = value <= 0;
        if (hasItem) {
            String countS = "";
            if (count != 1) {
                countS = count + "";
            }
            lore.add(ColorUtils.color("&a✔ " + itemPrefix + " " + ChatUtil.getMoney(count)));
        } else {
            lore.add(ColorUtils.color("&c✘ " + itemPrefix + " " + ChatUtil.getMoney(value)));
        }
    }
    public ItemStack getItemOfQuest(Player p, VillagerData villagerData, PlayerVillagerData playerVillagerData) {
        Material mat;
        String name;
        String details;
        if (villagerData.getQuests().size() == 0) {
            return ItemStackUtils.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, " ");
        }
        if (playerVillagerData.getQuestIndex() >= villagerData.getQuests().size()) {
            mat = Material.DIAMOND;
            name = ColorUtils.color("&aZakończono wszystkie zadania");
            details = ColorUtils.color("&8Gratulacje! Zakończyłeś już wszystkie zadania");
            ItemStack item = new ItemStack(mat, playerVillagerData.getQuestIndex() + 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            ArrayList<String> lore = new ArrayList<String>() {
                {
                    add(details);
                }
            };
            meta.setLore(lore);
            item.setItemMeta(meta);
            return item;
        }
        AtomicBoolean delay = new AtomicBoolean(false);
        final Instant now = Instant.now();
        questDelay.compute(p.getUniqueId(), (uuid, instant) -> {
            if(instant == null){
                return new VillagerDelayData(new IdentityHashMap<>());
            }
            if(instant.getTimers().get(villagerData.getId()) == null){
                instant.getTimers().put(villagerData.getId(), now);
            }else{
                if (now.isBefore(instant.getTimers().get(villagerData.getId()))) {
                    p.sendActionBar(ColorUtils.color("&cOdczekaj chwilę miedzy zadaniami"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    delay.set(true);
                    return instant;
                }
            }
            // no return
            return instant;
        });
        if(delay.get()){
            ItemStack item = new ItemStack(Material.CLOCK, playerVillagerData.getQuestIndex() + 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ColorUtils.color("&ePoczekaj na kolejne zlecenia"));
            ArrayList<String> lore = new ArrayList<String>() {
                {
                    add(ColorUtils.color("&7Wykonałeś zadanie, poczekaj na kolejne"));
                }
            };
            meta.setLore(lore);
            item.setItemMeta(meta);
            return item;
        }
        Quest q = villagerData.getQuests().get(playerVillagerData.getQuestIndex());
        if (playerVillagerData.isActiveQuest()) {
            mat = Material.GOLD_INGOT;
            name = ColorUtils.color("&a" + q.getName());
            details = ColorUtils.color("&7LPM aby oddać &8| &cPPM aby anulować");
            //
        } else {
            mat = Material.BOOK;
            name = ColorUtils.color("&c" + q.getName());
            details = ColorUtils.color("&7Kliknij aby zaakceptować");
        }
        int amount = playerVillagerData.getQuestIndex();
        if (amount == 0) {
            amount = 1;
        }
        ItemStack item = new ItemStack(mat, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> lore = new ArrayList<>();
        String[] lines = q.getDescription().split("\\n");
        for (String line : lines) {
            lore.add(ColorUtils.color("&e" + line));
        }
        lore.add(" ");
        lore.add(ColorUtils.color("&eZadanie &8(" + q.getDifficulty() + "&8)"));
        for (IQuestItem qItem : q.getQuestItems()) {
            if(qItem instanceof IQuestItemCollect){
                IQuestItemCollect questItem = (IQuestItemCollect) qItem;
                int progress = 0;
                if (playerVillagerData.getQuestItemData().get(questItem.getQuestItemDataId()) != null) {
                    progress = playerVillagerData.getQuestItemData().get(questItem.getQuestItemDataId());
                }
                loreCheck(lore,
                        questItem.getCount(),
                        playerVillagerData.isActiveQuest(),
                        progress,
                        questItem.getQuestItemPrefix(),
                        ChatUtil.materialName(questItem.getMaterial())
                );
            }
            if(qItem instanceof IQuestItemPay){
                IQuestItemPay questItem = (IQuestItemPay) qItem;
                int progress = 0;
                if (playerVillagerData.getQuestItemData().get(questItem.getQuestItemDataId()) != null) {
                    progress = playerVillagerData.getQuestItemData().get(questItem.getQuestItemDataId());
                }
                loreCheckMoney(lore,
                        questItem.getCount(),
                        playerVillagerData.isActiveQuest(),
                        progress,
                        questItem.getQuestItemPrefix()
                );
            }
            if (qItem instanceof IQuestItemGive) {
                IQuestItemGive questItem = (IQuestItemGive) qItem;
                int progress = 0;
                if (playerVillagerData.getQuestItemData().get(questItem.getQuestItemDataId()) != null) {
                    progress = playerVillagerData.getQuestItemData().get(questItem.getQuestItemDataId());
                }
                loreCheck(
                        lore,
                        questItem.getCount(),
                        playerVillagerData.isActiveQuest(),
                        progress,
                        questItem.getQuestItemPrefix(),
                        ChatUtil.materialName(questItem.getMaterial())
                );
            }
            if (qItem instanceof IQuestItemCraft) {
                IQuestItemCraft questItem = (IQuestItemCraft) qItem;
                int progress = 0;
                if (playerVillagerData.getQuestItemData().get(questItem.getQuestItemDataId()) != null) {
                    progress = playerVillagerData.getQuestItemData().get(questItem.getQuestItemDataId());
                }
                loreCheck(
                        lore,
                        questItem.getCount(),
                        playerVillagerData.isActiveQuest(),
                        progress,
                        questItem.getQuestItemPrefix(),
                        ChatUtil.materialName(questItem.getMaterial())
                );
            }
            if(qItem instanceof IQuestItemBreak){
                IQuestItemBreak questItem = (IQuestItemBreak) qItem;
                int progress = 0;
                if (playerVillagerData.getQuestItemData().get(questItem.getQuestItemDataId()) != null) {
                    progress = playerVillagerData.getQuestItemData().get(questItem.getQuestItemDataId());
                }
                loreCheck(lore,
                        questItem.getCount(),
                        playerVillagerData.isActiveQuest(),
                        progress,
                        questItem.getQuestItemPrefix(),
                        ChatUtil.materialName(questItem.getMaterial())
                );
            }
            if (qItem instanceof IQuestItemKill) {
                IQuestItemKill questItem = (IQuestItemKill) qItem;
                int progress = 0;
                if (playerVillagerData.getQuestItemData().get(questItem.getQuestItemDataId()) != null) {
                    progress = playerVillagerData.getQuestItemData().get(questItem.getQuestItemDataId());
                }
                loreCheck(lore,
                        questItem.getCount(),
                        playerVillagerData.isActiveQuest(),
                        progress,
                        questItem.getQuestItemPrefix(),
                        ChatUtil.materialName(questItem.getEntityType())
                );
            }

        }
        lore.add(" ");
        lore.add(ColorUtils.color("&eNagroda:"));
        for (IQuestReward reward : q.getRewardItems()) {
            if (reward instanceof IQuestRewardItemStack) {
                ItemStack przedmiot = ((IQuestRewardItemStack) reward).item();
                int przedmiotAmount = przedmiot.getAmount();
                String przedmiotName = ChatUtil.materialName(przedmiot.getType());
                String prefix = "";
                if (przedmiot.getEnchantments().size() > 0) {
                    prefix = "Enchanted ";
                }
                if (przedmiot.hasItemMeta() && przedmiot.getItemMeta().hasDisplayName()) {
                    przedmiotName = przedmiot.getItemMeta().getDisplayName();
                }

                if (przedmiotAmount > 1) {
                    lore.add(ColorUtils.color("&a" + prefix + przedmiotName + " x" + przedmiotAmount));
                } else {
                    lore.add(ColorUtils.color("&a" + prefix + przedmiotName));
                }
            }
            if(reward instanceof IQuestRewardCustom){
                lore.add(ColorUtils.color("&a" + ((IQuestRewardCustom) reward).label()));
            }
            if (reward instanceof IQuestRewardMoney) {
                lore.add(ColorUtils.color("&a" + ChatUtil.getMoney(((IQuestRewardMoney) reward).money())));
            }
            if (reward instanceof IQuestRewardExp) {
                lore.add(ColorUtils.color("&a" + ((IQuestRewardExp) reward).exp() + " pd."));
            }
        }
        if (!details.equals("")) {
            lore.add(" ");
            lore.add(details);
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public Inventory getInventoryOfVillager(VillagerData villager, Player player) {
        User u = UserManager.getUser(player.getUniqueId());
        try {
            if (!u.getVillagersData().getVillagersData().containsKey(villager.getId())) {
                u.getVillagersData().getVillagersData().put(villager.getId(), new PlayerVillagerData(villager.getId(), 0, false, new HashMap<>()));
            }
        } catch (Exception e) {
            System.out.println("Exception on fixing");
            e.printStackTrace();
        }
        int size = 54 - 1;

        Inventory inv;
        if (villager.isShop()) {
            inv = Bukkit.createInventory(null, 54, villager.getName());
            for (int i = 0; i != size - 1; i++) {
                inv.setItem(i, ItemStackUtils.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            }
            for (int i = size - 8; i != size + 1; i++) {
                inv.setItem(i, ItemStackUtils.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, " "));
            }
            inv.setItem(size - 4, getItemOfQuest(player, villager, u.getVillagersData().getVillagersData().get(villager.getId())));
            if (villager.getShopItems().size() > 0) {
                for (int i = 0; i != villager.getShopItems().size(); i++) {
                    inv.setItem(i, getItemOfShop(u, villager, villager.getShopItems().get(i)));
                }
            }
        } else {
            inv = Bukkit.createInventory(null, 9, villager.getName());
            for (int i = 0; i != size - 1; i++) {
                inv.setItem(i, ItemStackUtils.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            }
            size = 9 - 1;
            for (int i = 0; i != size + 1; i++) {
                inv.setItem(i, ItemStackUtils.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, " "));
            }
            inv.setItem(size - 4, getItemOfQuest(player, villager, u.getVillagersData().getVillagersData().get(villager.getId())));
        }
        return inv;
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        if (e.getClickedInventory() == null) {
            return;
        }
        if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            if (e.getView().getTitle().contains(ColorUtils.color("&c&lQ"))) {
                e.setCancelled(true);
                return;
            }
            return;
        }
        if (e.getView().getTitle().contains(ColorUtils.color("&c&lQ"))) {
            e.setCancelled(true);
            String villagerName = e.getView().getTitle().replace(ColorUtils.color("&c&lQ"), "").replace(ColorUtils.color("&9&lS"), "").replace(ColorUtils.color("&7"), "").replace(" ", "");
            if (!villagers.containsKey(villagerName.toLowerCase())) {
                System.out.println("Sklep nie istnieje!");
            }
            // Varibales
            VillagerData villagerData = villagers.get(villagerName.toLowerCase());
            Player p = (Player) e.getWhoClicked();
            User u = UserManager.getUser(p.getUniqueId());
            // Shop
            PlayerVillagerData data = u.getVillagersData().getVillagersData().get(villagerData.getId());
            int questSlot;
            if (villagerData.isShop()) {
                // SHOP
                if (e.getSlot() > -1 && e.getSlot() < 45) {
                    // SHOP
                    if (e.getSlot() > villagerData.getShopItems().size()) {
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return;
                    }
                    VillagerShopItem shopItem = villagerData.getShopItems().get(e.getSlot());
                    if (e.getAction() == InventoryAction.PICKUP_ALL) {
                        boolean unlocked = data.getQuestIndex() + 1 >= shopItem.getRequiredQuestLevel() && u.getLevel() >= shopItem.getRequiredPlayerLevel();
                        if (unlocked) {
                            if(shopItem.getCost() == 0){
                                return;
                            }
                            if (u.hasMoney(shopItem.getCost())) {
                                u.subtractMoney(shopItem.getCost());
                                //p.sendMessage(ColorUtils.color(villagerData.getName() + " &6> &c- " + ChatUtil.getMoney(shopItem.getCost())));
                                //p.sendMessage(ColorUtils.color(villagerData.getName() + " &6> &a+ " + ChatUtil.materialName(shopItem.getItem().getType()) + " x" + shopItem.getItem().getAmount()));
                                shopItem.onBuy(p);
                                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                ModerrkowoLog.LogAdmin(ColorUtils.color("&6" + p.getName() + " &7zakupił &6" + ChatUtil.materialName(shopItem.getItem().getType()) + " &7od " + villagerData.getName()));
                            } else {
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                                p.sendMessage(ColorUtils.color(villagerData.getName() + " &6> &cNiestety nie dogadamy się. Nie posiadasz tyle pieniędzy."));
                            }
                        } else {
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            p.sendMessage(ColorUtils.color(villagerData.getName() + " &6> &cNie, tego nie sprzedaję."));
                        }
                        return;
                    }
                    if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                        if (e.isShiftClick() && e.isRightClick()) {
                            if (shopItem.canSell()) {
                                boolean unlocked = data.getQuestIndex() + 1 >= shopItem.getRequiredQuestLevel() && u.getLevel() >= shopItem.getRequiredPlayerLevel();

                                // item 8
                                // 8 x 8 = 64

                                int max = 0;
                                int temp = 0;
                                for (int j = 0; j != 64; j++) {
                                    if (temp >= shopItem.getItem().getMaxStackSize()) {
                                        temp = shopItem.getItem().getMaxStackSize();
                                        max = temp / shopItem.getItem().getAmount();
                                        break;
                                    } else {
                                        temp += shopItem.getItem().getAmount();
                                        max += 1;
                                    }
                                }

                                if (unlocked) {
                                    if (ItemStackUtils.getCountOfMaterial(p, shopItem.getItem().getType()) >= shopItem.getItem().getAmount() * max) {
                                        ItemStackUtils.consumeItem(p, shopItem.getItem().getAmount() * max, shopItem.getItem().getType());
                                        u.addMoney(shopItem.getFinalCost() * max);
                                        p.sendMessage(ColorUtils.color(villagerData.getName() + " &6> &c- " + ChatUtil.materialName(shopItem.getItem().getType()) + " x" + shopItem.getItem().getAmount() * max));
                                        p.sendMessage(ColorUtils.color(villagerData.getName() + " &6> &a+ " + ChatUtil.getMoney(shopItem.getFinalCost() * max)));
                                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1, 1);
                                        ModerrkowoLog.LogAdmin(ColorUtils.color("&6" + p.getName() + " &7sprzedał &6" + ChatUtil.materialName(shopItem.getItem().getType()) + " &7do " + villagerData.getName()));
                                    } else {
                                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                                        p.sendMessage(ColorUtils.color(villagerData.getName() + " &6> &cChcesz sprzedać coś czego nie masz?"));
                                        return;
                                    }
                                } else {
                                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                                    p.sendMessage(ColorUtils.color(villagerData.getName() + " &6> &cNiestety, tego nie kupuję."));
                                    return;
                                }
                            }
                        }
                    }
                    if (e.getAction() == InventoryAction.PICKUP_HALF) {
                        if (shopItem.canSell()) {
                            boolean unlocked = data.getQuestIndex() + 1 >= shopItem.getRequiredQuestLevel() && u.getLevel() >= shopItem.getRequiredPlayerLevel();
                            if (unlocked) {
                                if (ItemStackUtils.getCountOfMaterial(p, shopItem.getItem().getType()) >= shopItem.getItem().getAmount()) {
                                    ItemStackUtils.consumeItem(p, shopItem.getItem().getAmount(), shopItem.getItem().getType());
                                    u.addMoney(shopItem.getFinalCost());
                                    p.sendMessage(ColorUtils.color(villagerData.getName() + " &6> &c- " + ChatUtil.materialName(shopItem.getItem().getType()) + " x" + shopItem.getItem().getAmount()));
                                    p.sendMessage(ColorUtils.color(villagerData.getName() + " &6> &a+ " + ChatUtil.getMoney(shopItem.getFinalCost())));
                                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1, 1);
                                    ModerrkowoLog.LogAdmin(ColorUtils.color("&6" + p.getName() + " &7sprzedał &6" + ChatUtil.materialName(shopItem.getItem().getType()) + " &7do " + villagerData.getName()));
                                } else {
                                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                                    p.sendMessage(ColorUtils.color(villagerData.getName() + " &6> &cChcesz sprzedać coś czego nie masz?"));
                                    return;
                                }
                            } else {
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                                p.sendMessage(ColorUtils.color(villagerData.getName() + " &6> &cNiestety, tego nie kupuję."));
                                return;
                            }
                        }
                    }
                }
                // QuestSlot
                questSlot = 49;
            } else {
                // QuestSlot
                questSlot = 4;
            }
            if (e.getSlot() == questSlot) {
                // IF MAX QUEST
                if (data.getQuestIndex() >= villagerData.getQuests().size()) {
                    p.sendMessage(ColorUtils.color(villagerData.getName() + " &6> &7Niestety nie mam już nic dla Ciebie..."));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1, 1);
                    p.openInventory(getInventoryOfVillager(villagerData, p));
                    return;
                }
                // IF HAS ACTIVE QUEST
                boolean hasQuest = false;
                String otherVillagerName = null;
                for (PlayerVillagerData villagers : u.getVillagersData().getVillagersData().values()) {
                    if (villagers.getVillagerId().equals(villagerData.getId())) {
                        continue;
                    }
                    if (villagers.isActiveQuest()) {
                        otherVillagerName = this.villagers.get(villagers.getVillagerId().toLowerCase()).getName();
                        hasQuest = true;
                    }
                }
                if (hasQuest) {
                    p.sendMessage(ColorUtils.color(villagerData.getName() + " &6> &7Najpierw zakończ zadanie u " + otherVillagerName));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return;
                }
                Quest activeQuest = villagerData.getQuests().get(data.getQuestIndex());
                if (data.isActiveQuest()) {
                    // Jeżeli chce anulować quest
                    if (e.getAction() == InventoryAction.PICKUP_HALF) {
                        for (IQuestItem item : activeQuest.getQuestItems()) {
                            if (item instanceof IQuestItemGive) {
                                if (data.getQuestItemData().get(item.getQuestItemDataId()) > 0) {
                                    ItemStackUtils.addItemStackToPlayer(p, new ItemStack(((IQuestItemGive) item).getMaterial(), data.getQuestItemData().get(item.getQuestItemDataId())));
                                }
                            }
                            if(item instanceof IQuestItemPay){
                                int money = data.getQuestItemData().get(item.getQuestItemDataId());
                                if(money > 0){
                                    u.addMoney(money);
                                    p.sendMessage(ColorUtils.color(villagerData.getName() + " &6> &aZwracam twoje " + ChatUtil.getMoney(money) + ", które wpłaciłeś"));
                                }
                            }
                        }
                        data.setActiveQuest(false);
                        if (data.getQuestItemData() == null) {
                            data.setQuestItemData(new HashMap<>());
                        } else {
                            data.getQuestItemData().clear();
                        }
                        u.UpdateScoreboard();
                        p.sendMessage(ColorUtils.color(villagerData.getName() + " &6> &7Szkoda, że się rozmyśliłeś"));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        p.openInventory(getInventoryOfVillager(villagerData, p));
                        return;
                    }
                    // Jeżeli chce oddać itemki
                    if (e.getAction() == InventoryAction.PICKUP_ALL) {
                        int haveItem = 0;
                        for (IQuestItem item : activeQuest.getQuestItems()) {
                            if (item instanceof IQuestItemGive) {

                                /*
                                IQuestItemGive itemGive = (IQuestItemGive) item;
                                int items = data.getQuestItemData().get(item.getQuestItemDataId());
                                int temp = items;
                                if (temp >= itemGive.getCount()) {
                                    haveItem++;
                                } else {
                                    int required = itemGive.getCount();
                                    required = required - temp;
                                    int have = ItemStackUtils.getCountOfMaterial(p, itemGive.getMaterial());
                                    if (have > required) {
                                        have = required;
                                    }
                                    ItemStackUtils.consumeItem(p, have, itemGive.getMaterial());
                                    temp += have;
                                    data.getQuestItemData().replace(item.getQuestItemDataId(), items, temp);
                                    int count = itemGive.getCount() - temp;
                                    if (temp >= itemGive.getCount()) {
                                        p.sendMessage(ColorUtils.color("&cMusisz jeszcze przynieść " + count + " " + ChatUtil.materialName(itemGive.getMaterial())));
                                    } else {
                                        haveItem++;
                                    }
                                    u.UpdateScoreboard();
                                }*/
                                IQuestItemGive itemGive = (IQuestItemGive) item;
                                int ileJuzWplacil = data.getQuestItemData().get(item.getQuestItemDataId());
                                int ileJestPotrzebne = itemGive.getCount();
                                int ileMa = ItemStackUtils.getCountOfMaterial(p, itemGive.getMaterial());
                                int ileBrakuje = ileJestPotrzebne - ileJuzWplacil;
                                if(ileJuzWplacil == ileJestPotrzebne){
                                    // jezeli nic nie brakuje do zaplaty
                                    haveItem++;
                                }else{
                                    // jezeli cos brakuje do zaplaty
                                    // zabiera mu pieniadze tyle ile moze i sprawdza czy brakuje nadal
                                    if(ileBrakuje > ileMa){
                                        ItemStackUtils.consumeItem(p, ileMa, itemGive.getMaterial());
                                        data.getQuestItemData().replace(item.getQuestItemDataId(), ileJuzWplacil, ileJuzWplacil+ileMa);
                                        p.sendMessage(ColorUtils.color("&cPrzyniesiono " + ileMa + " ale brakuje jeszcze " + (ileJestPotrzebne-ileJuzWplacil-ileMa) + "x " + ChatUtil.materialName(itemGive.getMaterial())));
                                        // wplacil tyle ile moze
                                    }else{
                                        ItemStackUtils.consumeItem(p, ileBrakuje, itemGive.getMaterial());
                                        data.getQuestItemData().replace(item.getQuestItemDataId(), ileJuzWplacil, ileJuzWplacil+ileBrakuje);
                                        // wplacil calosc
                                        haveItem++;
                                    }
                                }
                                u.UpdateScoreboard();
                            }
                            if(item instanceof IQuestItemPay){
                                IQuestItemPay itemGive = (IQuestItemPay) item;
                                int ileJuzWplacil = data.getQuestItemData().get(item.getQuestItemDataId());
                                int ileJestPotrzebne = itemGive.getCount();
                                int ileMa = u.getMoney();
                                int ileBrakuje = ileJestPotrzebne - ileJuzWplacil;
                                if(ileJuzWplacil == ileJestPotrzebne){
                                    // jezeli nic nie brakuje do zaplaty
                                    haveItem++;
                                }else{
                                    // jezeli cos brakuje do zaplaty
                                    // zabiera mu pieniadze tyle ile moze i sprawdza czy brakuje nadal
                                    if(ileBrakuje > ileMa){
                                        u.subtractMoney(ileMa);
                                        data.getQuestItemData().replace(item.getQuestItemDataId(), ileJuzWplacil, ileJuzWplacil+ileMa);
                                        p.sendMessage(ColorUtils.color("&cZapłacono " + ChatUtil.getMoney(ileMa) + " ale brakuje jeszcze " + ChatUtil.getMoney(ileJestPotrzebne-ileJuzWplacil-ileMa)));
                                        // wplacil tyle ile moze
                                    }else{
                                        u.subtractMoney(ileBrakuje);
                                        data.getQuestItemData().replace(item.getQuestItemDataId(), ileJuzWplacil, ileJuzWplacil+ileBrakuje);
                                        // wplacil calosc
                                        haveItem++;
                                    }
                                }
                                u.UpdateScoreboard();

                                /*int items = data.getQuestItemData().get(item.getQuestItemDataId());
                                int temp = items;
                                if (temp >= itemGive.getCount()) {
                                    haveItem++;
                                } else {
                                    int required = itemGive.getCount();
                                    required = required - temp;
                                    int have = u.getMoney();
                                    if (have > required) {
                                        have = required;
                                    }
                                    //ItemStackUtils.consumeItem(p, have, itemGive.getMaterial());
                                    u.subtractMoney(have);
                                    temp += have;
                                    data.getQuestItemData().replace(item.getQuestItemDataId(), items, temp);
                                    int count = itemGive.getCount() - temp;
                                    if (temp >= itemGive.getCount()) {
                                        p.sendMessage(ColorUtils.color("&cMusisz jeszcze zapłacić " + ChatUtil.getMoney(count)));
                                    } else {
                                        haveItem++;
                                    }
                                    u.UpdateScoreboard();
                                }*/
                            }
                            if (item instanceof IQuestItemCraft) {
                                IQuestItemCraft itemCraft = (IQuestItemCraft) item;
                                int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                                if (temp >= itemCraft.getCount()) {
                                    haveItem++;
                                } else {
                                    int count = itemCraft.getCount() - temp;
                                    p.sendMessage(ColorUtils.color("&cMusisz jeszcze wytworzyć " + count + " " + ChatUtil.materialName(itemCraft.getMaterial())));
                                }
                            }
                            if (item instanceof IQuestItemKill) {
                                IQuestItemKill itemKill = (IQuestItemKill) item;
                                int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                                if (temp >= itemKill.getCount()) {
                                    haveItem++;
                                } else {
                                    int count = itemKill.getCount() - temp;
                                    p.sendMessage(ColorUtils.color("&cMusisz jeszcze zabić " + count + " " + ChatUtil.materialName(itemKill.getEntityType())));
                                }
                            }
                            if(item instanceof IQuestItemCollect){
                                IQuestItemCollect itemBreak = (IQuestItemCollect) item;
                                int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                                if (temp >= itemBreak.getCount()) {
                                    haveItem++;
                                } else {
                                    int count = itemBreak.getCount() - temp;
                                    p.sendMessage(ColorUtils.color("&cMusisz jeszcze zebrać " + count + " " + ChatUtil.materialName(itemBreak.getMaterial())));
                                }
                            }
                            if(item instanceof IQuestItemBreak){
                                IQuestItemBreak itemBreak = (IQuestItemBreak) item;
                                int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                                if (temp >= itemBreak.getCount()) {
                                    haveItem++;
                                } else {
                                    int count = itemBreak.getCount() - temp;
                                    p.sendMessage(ColorUtils.color("&cMusisz jeszcze znisczyć " + count + " " + ChatUtil.materialName(itemBreak.getMaterial())));
                                }
                            }
                        }
                        boolean haveItems = haveItem == activeQuest.getQuestItems().size();
                        if (haveItems) {
                            p.sendMessage(" ");
                            p.sendMessage(" ");
                            p.sendMessage(ColorUtils.color("  &eGratulacje odblokowałeś &c" + (data.getQuestIndex()+2) + " poziom&e " + villagerName +"!"));
                            p.sendMessage(" ");
                            p.sendMessage(" ");
                            final Instant now = Instant.now();
                            questDelay.compute(p.getUniqueId(), (uuid, instant) -> {
                                if(instant == null){
                                    return new VillagerDelayData(new IdentityHashMap<>());
                                }
                                if(instant.getTimers().get(villagerData.getId()) == null){
                                    instant.getTimers().put(villagerData.getId(), now.plusSeconds(60*10));
                                }else{
                                    instant.getTimers().replace(villagerData.getId(), now.plusSeconds(60*10));
                                }
                                return instant;
                            });
                            spawnFireworks(p.getLocation(), 1);
                            Bukkit.broadcastMessage(ColorUtils.color("  &e&l" + p.getName() + " &7zakończył zadanie &6" + activeQuest.getName()));
                            for(Player temp : Bukkit.getOnlinePlayers()){
                                temp.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP,2,1);
                            }
                            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP,2,1);
                            p.sendMessage(ColorUtils.color(villagerData.getName() + " &6> &aInteresy z tobą to przyjemność!"));
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1);
                            data.setActiveQuest(false);
                            if (data.getQuestItemData() == null) {
                                data.setQuestItemData(new HashMap<>());
                            } else {
                                data.getQuestItemData().clear();
                            }
                            data.setQuestIndex(data.getQuestIndex() + 1);
                            p.openInventory(getInventoryOfVillager(villagerData, p));
                            for (IQuestReward reward : activeQuest.getRewardItems()) {
                                if (reward instanceof IQuestRewardItemStack) {
                                    ItemStack item = ((IQuestRewardItemStack) reward).item();
                                    if (p.getInventory().firstEmpty() != -1) {
                                        p.getInventory().addItem(item);
                                    } else {
                                        p.getWorld().dropItem(p.getLocation(), item);
                                        p.sendMessage(ColorUtils.color("&cMasz pełny ekwipunek. Więc przedmiot wyleciał z Ciebie"));
                                    }
                                }
                                if(reward instanceof IQuestRewardCustom){
                                    IQuestRewardCustom custom = (IQuestRewardCustom) reward;
                                    custom.Action(p, u);
                                }
                                if (reward instanceof IQuestRewardMoney) {
                                    u.addMoney(((IQuestRewardMoney) reward).money());
                                }
                                if (reward instanceof IQuestRewardExp) {
                                    u.addExp(((IQuestRewardExp) reward).exp());
                                }
                            }
                            ModerrkowoLog.LogAdmin(ColorUtils.color("&6" + p.getName() + " &7zakończył Questa &6" + activeQuest.getName() + " &7od " + villagerData.getName()));
                            u.UpdateScoreboard();
                        } else {
                            p.sendMessage(ColorUtils.color(villagerData.getName() + " &6> &7Nie wykonałeś wszystkich zadań.."));
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            p.openInventory(getInventoryOfVillager(villagerData, p));
                        }
                        return;
                    }
                } else {
                    AtomicBoolean blocked = new AtomicBoolean(false);
                    final Instant now = Instant.now();
                    questDelay.compute(p.getUniqueId(), (uuid, instant) -> {
                        if(instant == null){
                            return new VillagerDelayData(new IdentityHashMap<>());
                        }
                        if(instant.getTimers().get(villagerData.getId()) == null){
                            instant.getTimers().put(villagerData.getId(), now);
                        }else{
                            if (now.isBefore(instant.getTimers().get(villagerData.getId()))) {
                                p.sendActionBar(ColorUtils.color("&cOdczekaj chwilę miedzy zadaniami"));
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                                blocked.set(true);
                                return instant;
                            }
                        }
                        // no return
                        return instant;
                    });
                    if(blocked.get()){
                        return;
                    }
                    // Przyjmuje zadanie
                    if (data.getQuestItemData() == null) {
                        data.setQuestItemData(new HashMap<>());
                    } else {
                        data.getQuestItemData().clear();
                    }
                    data.setActiveQuest(true);
                    for (IQuestItem item : activeQuest.getQuestItems()) {
                        data.getQuestItemData().put(item.getQuestItemDataId(), 0);
                    }
                    p.sendMessage(ColorUtils.color(villagerData.getName() + " &6> &aDzięki, że przyjąłeś zadanie."));
                    u.UpdateScoreboard();
                    p.openInventory(getInventoryOfVillager(villagerData, p));
                    p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 2, 1);
                }
            }
            e.setCancelled(true);
        }
    }
    private final Map<UUID, Instant> commandDelay = new IdentityHashMap<>();

    @EventHandler
    public void villagerClick(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        Entity entity = e.getRightClicked();

        if (entity.getType() == EntityType.VILLAGER) {
            if (entity.isCustomNameVisible()) {
                if (entity.getCustomName() == null) {
                    return;
                }
                if (entity.getCustomName().equals(ColorUtils.color("&aLosowy teleport"))) {
                    final Instant now = Instant.now();
                    commandDelay.compute(e.getPlayer().getUniqueId(), (uuid, instant) -> {
                        if (instant != null && now.isBefore(instant)) {
                            e.getPlayer().sendActionBar(ColorUtils.color("&cOdczekaj chwilę miedzy losowym teleportem"));
                            return instant;
                        }
                        p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 0.5f);
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                        Location loc = RandomUtils.getRandom(p.getWorld());
                        p.teleport(loc);
                        p.sendActionBar(ColorUtils.color("&6x " + loc.getBlockX() + " y " + loc.getBlockY() + " z " + loc.getBlockZ()));
                        Logger.logPluginMessage(p.getName() + " użył losowego teleportu.");
                        return now.plusSeconds(30);
                    });
                }
                if (entity.getCustomName().contains(ColorUtils.color("&c&lQ"))) {
                    String villagerName =
                            entity.getCustomName()
                                    .replace(ColorUtils.color("&c&lQ"), "")
                                    .replace(ColorUtils.color("&9&lS"), "")
                                    .replace(ColorUtils.color("&7"), "")
                                    .replace(" ", "");

                    if (villagers.containsKey(villagerName.toLowerCase())) {
                        VillagerData villagerData = villagers.get(villagerName.toLowerCase());
                        e.setCancelled(true);
                        p.openInventory(
                                getInventoryOfVillager(villagerData, p)
                        );
                        p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 2, 1);
                        double data = 0.1;
                    }
                }
            }
        }
    }
    //IQuestItemKill
    @EventHandler(priority = EventPriority.MONITOR)
    public void kill(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player) {
            return;
        }
        if (e.getEntity().getKiller() == null) {
            return;
        }
        if(e.isCancelled()){
            return;
        }
        try {
            Player p = e.getEntity().getKiller();
            User u = UserManager.getUser(p.getUniqueId());
            PlayerVillagerData data = null;
            for (PlayerVillagerData villagers : u.getVillagersData().getVillagersData().values()) {
                if (villagers.isActiveQuest()) {
                    data = villagers;
                    break;
                }
            }
            if (data == null) {
                return;
            }
            VillagerData villager = villagers.get(data.getVillagerId().toLowerCase());
            Quest quest = villager.getQuests().get(data.getQuestIndex());
            for (IQuestItem item : quest.getQuestItems()) {
                if (item instanceof IQuestItemKill) {
                    IQuestItemKill craftItem = (IQuestItemKill) item;
                    if (craftItem.getEntityType().equals(e.getEntityType())) {
                        int recipeAmount = 1;
                        int items = data.getQuestItemData().get(craftItem.getQuestItemDataId());
                        int temp = items;
                        temp += recipeAmount;
                        data.getQuestItemData().replace(craftItem.getQuestItemDataId(), items, temp);
                        p.sendMessage(ColorUtils.color(villager.getName() + " &6> &aZabito " + ChatUtil.materialName(e.getEntityType())));
                        u.UpdateScoreboard();
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    //IQuestItemBreak
    @EventHandler(priority = EventPriority.MONITOR)
    public void breakBlock(BlockBreakEvent e){
        if(e.isCancelled()){
            return;
        }
        try {
            Player p = e.getPlayer();
            User u = UserManager.getUser(p.getUniqueId());
            PlayerVillagerData data = null;
            for (PlayerVillagerData villagers : u.getVillagersData().getVillagersData().values()) {
                if (villagers.isActiveQuest()) {
                    data = villagers;
                    break;
                }
            }
            if (data == null) {
                return;
            }
            VillagerData villager = villagers.get(data.getVillagerId().toLowerCase());
            Quest quest = villager.getQuests().get(data.getQuestIndex());
            for (IQuestItem item : quest.getQuestItems()) {
                if (item instanceof IQuestItemBreak) {
                    IQuestItemBreak breakItem = (IQuestItemBreak) item;
                    if (e.getBlock().getType().equals(breakItem.getMaterial())) {
                        if(p.getInventory().getItemInMainHand().hasEnchant(Enchantment.SILK_TOUCH) && breakItem.blockSilk()){
                            return;
                        }
                        int recipeAmount = 1;
                        int items = data.getQuestItemData().get(breakItem.getQuestItemDataId());
                        int temp = items;
                        temp += recipeAmount;
                        data.getQuestItemData().replace(breakItem.getQuestItemDataId(), items, temp);
                        p.sendMessage(ColorUtils.color(villager.getName() + " &6> &aWydobyto " + ChatUtil.materialName(e.getBlock().getType())));
                        u.UpdateScoreboard();
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    //IQuestItemCraft
    @EventHandler(priority = EventPriority.MONITOR)
    public void craft(CraftItemEvent e) {
        if (e.getInventory().getResult() == null || e.getInventory().getResult().getType().equals(Material.AIR)) {
            return;
        }
        try {
            Player p = (Player) e.getWhoClicked();
            User u = UserManager.getUser(p.getUniqueId());
            PlayerVillagerData data = null;
            for (PlayerVillagerData villagers : u.getVillagersData().getVillagersData().values()) {
                if (villagers.isActiveQuest()) {
                    data = villagers;
                    break;
                }
            }
            if (data == null) {
                return;
            }
            VillagerData villager = villagers.get(data.getVillagerId().toLowerCase());
            Quest quest = villager.getQuests().get(data.getQuestIndex());
            for (IQuestItem item : quest.getQuestItems()) {
                if (item instanceof IQuestItemCraft) {
                    IQuestItemCraft craftItem = (IQuestItemCraft) item;
                    if (craftItem.getMaterial().equals(e.getInventory().getResult().getType())) {
                        int recipeAmount = e.getInventory().getResult().getAmount();
                        ClickType click = e.getClick();
                        switch (click) {
                            case NUMBER_KEY:
                                // If hotbar slot selected is full, crafting fails (vanilla behavior, even when
                                // items match)
                                if (e.getWhoClicked().getInventory().getItem(e.getHotbarButton()) != null)
                                    recipeAmount = 0;
                                break;

                            case DROP:
                            case CONTROL_DROP:
                                // If we are holding items, craft-via-drop fails (vanilla behavior)
                                ItemStack cursor = e.getCursor();
                                // Apparently, rather than null, an empty cursor is AIR. I don't think that's
                                // intended.
                                if (cursor != null && cursor.getType().equals(Material.AIR)) recipeAmount = 0;
                                break;

                            case SHIFT_RIGHT:
                            case SHIFT_LEFT:
                                if (recipeAmount == 0) {
                                    break;
                                }
                                int maxCraftable = getMaxCraftAmount(e.getInventory());
                                int capacity = fits(e.getInventory().getResult(), e.getView().getBottomInventory());
                                if (capacity < maxCraftable) {
                                    maxCraftable = ((capacity + recipeAmount - 1) / recipeAmount) * recipeAmount;
                                }
                                recipeAmount = maxCraftable;
                                break;
                            default:
                        }
                        int items = data.getQuestItemData().get(craftItem.getQuestItemDataId());
                        int temp = items;
                        temp += recipeAmount;
                        data.getQuestItemData().replace(craftItem.getQuestItemDataId(), items, temp);
                        p.sendMessage(ColorUtils.color(villager.getName() + " &6> &aWytworzono " + recipeAmount + " " + ChatUtil.materialName(e.getInventory().getResult().getType())));
                        u.UpdateScoreboard();
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    // Fireworks effect
    public static void spawnFireworks(Location location, int amount){
        Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();

        for(int i = 0;i<amount; i++){
            Firework fw2 = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }

}
