package pies.Reducer.commands.GUI.SubMenus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pies.Reducer.api.InvItems;
import pies.Reducer.commands.GUI.Cosmetics;
import pies.Reducer.commands.Reducer;
import pies.Reducer.commands.ReducerGUI;
import pies.Reducer.utils.ItemBuilder;

public class Glass {
    public static Inventory inv(Player player) {
        Inventory inv = Bukkit.createInventory(ReducerGUI.COSMETICS_GLASS, 27, "Cosmetics");
        UpdateSlots(player, inv);
        return inv;
    }
    public static void exe(Player player, InventoryClickEvent e) {

        if (e.getRawSlot() == 21) {
            player.openInventory(Cosmetics.inv(player));
            return;
        }
        if (e.getRawSlot() == 22) {
            player.closeInventory();
            return;
        }

        ItemStack item = e.getCurrentItem();

        if (item == null) return;

        Material met = item.getType();
        if (met == Material.AIR) return;
        byte b = item.getData().getData();

        InvItems.setBlockMaterial(player, met, b);
        Reducer.setItems(player, player.getInventory());
    }
    public static void UpdateSlots(Player player, Inventory inv) {
        for (int i = 0; i < 15; i++) {
            inv.setItem(i, new ItemBuilder(Material.STAINED_GLASS, (byte)i).build());
        }
        inv.setItem(21, new ItemBuilder(Material.ARROW).name("§cGo back").build());
        inv.setItem(22, new ItemBuilder(Material.BARRIER).name("§cClose").build());
    }
}
