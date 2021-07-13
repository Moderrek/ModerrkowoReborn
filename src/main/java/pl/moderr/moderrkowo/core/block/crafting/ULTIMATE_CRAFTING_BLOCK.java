package pl.moderr.moderrkowo.core.block.crafting;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.customitems.CustomItemsManager;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.ItemStackUtils;

import java.util.*;

public class ULTIMATE_CRAFTING_BLOCK implements Workbench, Listener {

    private final Main main;

    public ULTIMATE_CRAFTING_BLOCK(Main main){
        this.main = main;
    }
    private Inventory inv;
    private final Material BLOCK_MATERIAL = Material.CRAFTING_TABLE;
    private final String BLOCK_NAME = "&b&lNajlepsze Rzemiosło";

    private final ArrayList<WorkbenchRecipe> craftingi = new ArrayList<>();

    private final HashMap<Player, Integer> craftingTask = new HashMap<Player, Integer>();


    @Override
    public Material getMaterial() {
        return BLOCK_MATERIAL;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    @Override
    public String getBlockName() {
        return BLOCK_NAME;
    }

    @Override
    public ArrayList<WorkbenchRecipe> getCraftingi() {
        return craftingi;
    }

    public void setCraftingsList(){
        craftingi.clear();
        ItemStack is =  CustomItemsManager.getFragmentEnd();
        is.setAmount(4);
        ItemStack is2 = CustomItemsManager.getWejsciowkaEnd();
        is2.setAmount(1);
        List<ItemStack> PRZYKLADOWY_CRAFTING = Arrays.asList(new ItemStack(Material.DIAMOND,2), new ItemStack(Material.LAPIS_LAZULI,4), new ItemStack(Material.REDSTONE,8),is);
        craftingi.add(new WorkbenchRecipe(PRZYKLADOWY_CRAFTING, is2,0,30,"Umożliwia teleport do kresu"));
        /*List<ItemStack> ZAKLETA_MARCHEWKA_CL = Arrays.asList(new ItemStack(Material.GOLD_BLOCK, 8), ItemGenerator.generate(ItemEnum.ZAKLETA_MARCHEWKA, 1), new ItemStack(Material.CARROT, 1));
        craftingi.add(new WorkbenchRecipe(ZAKLETA_MARCHEWKA_CL, ItemGenerator.generate(ItemEnum.ZAKLETA_ZLOTA_MARCHEWKA, 1), 0,2, "Po zjedzeniu tej marchewki jesteś najedzony na 10 minut!"));

        List<ItemStack> GENERATOR_CL = Arrays.asList(
                new ItemStack(Material.FURNACE),
                new ItemStack(Material.IRON_BLOCK, 2),
                new ItemStack(Material.BLAZE_POWDER, 9),
                new ItemStack(Material.LAPIS_LAZULI, 4),
                new ItemStack(Material.REDSTONE, 48)
        );

        craftingi.add(new WorkbenchRecipe(GENERATOR_CL, ItemGenerator.generate(ItemEnum.GENERATOR,1), 1, 15, "Brak opisu"));

        List<ItemStack> MACHINE_CORE_CL = Arrays.asList(
                new ItemStack(Material.REDSTONE_BLOCK, 6),
                new ItemStack(Material.IRON_BLOCK, 5),
                new ItemStack(Material.GLOWSTONE_DUST, 8),
                new ItemStack(Material.DIAMOND_BLOCK, 1)
        );

        craftingi.add(new WorkbenchRecipe(MACHINE_CORE_CL, ItemGenerator.generate(ItemEnum.MACHINE_CORE,1), 2, 5, "Blok potrzebny do produkcji maszyn."));

        List<ItemStack> MACERATOR_CL = Arrays.asList(
                new ItemStack(Material.STONE, 64),
                ItemGenerator.generate(ItemEnum.NETHER, 1),
                ItemGenerator.generate(ItemEnum.MACHINE_CORE,1),
                ItemGenerator.generate(ItemEnum.GENERATOR,1)
        );


        craftingi.add(new WorkbenchRecipe(MACERATOR_CL, ItemGenerator.generate(ItemEnum.MACERATOR, 1), 3,10, "Ta maszyna działa na węgiel i potrafi mielić przedmioty"));*/
    }

    @Override
    public HashMap<Player, Integer> getCraftingTask() {
        return craftingTask;
    }
    

    @Override
    @EventHandler
    public void openCraftingEvent(PlayerInteractEvent e) {
        openBlockInventory(e, false);
    }

    @Override
    public Inventory getCraftingInventory(Player p) {
        inv = Bukkit.createInventory(p, 54, ColorUtils.color(BLOCK_NAME));
        setCraftingsList();
        initializeItemsToCraftingInventory();
        return inv;
    }

    @Override
    public void initializeItemsToCraftingInventory() {
        for (int i = 18; i < 27; i++) {
            inv.setItem(i, ItemStackUtils.changeName(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "));
        }
        addCraftingsToInventory();
    }

    @Override
    public void addCraftingsToInventory() {
        for (WorkbenchRecipe crafting : craftingi) {
            ItemStack item = new ItemStack(crafting.getEndItem());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(crafting.getEndItem().getItemMeta().getDisplayName());
            ArrayList<String> lore = new ArrayList<String>();
            lore.add(" ");
            lore.add(ColorUtils.color("&cAby wybrać kliknij"));
            lore.add(" ");
            lore.add(ColorUtils.color("&8Opis"));
            lore.add(ColorUtils.color("&7" + crafting.getDescription()));
            lore.add(" ");
            lore.add(ColorUtils.color("&ePotrzebne: "));
            for (ItemStack is : crafting.getMaterialsRequiredToItem()) {
                if(is.getItemMeta().hasDisplayName()){
                    lore.add(ColorUtils.color(Objects.requireNonNull(is.getItemMeta()).getDisplayName() + " &7x&e" + is.getAmount()));
                }else{
                    lore.add(ColorUtils.color(Objects.requireNonNull("&7" + is.getType().toString() + " &7x&e" + is.getAmount())));
                }
            }
            lore.add(" ");
            assert meta != null;
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(crafting.getSlot(), item);
        }
    }


    @Override
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        craftingClick(e);
    }

}
