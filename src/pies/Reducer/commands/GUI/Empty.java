package pies.Reducer.commands.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pies.Reducer.commands.ReducerGUI;

public class Empty {
    public static Inventory inv(Player player) {
        // This method will create the inventory that players use to open
        Inventory inv = Bukkit.createInventory(ReducerGUI.COSMETICS_WOOL, 27, "Cosmetics");
        UpdateSlots(player, inv);
        return inv;
    }
    public static void exe(Player player, InventoryClickEvent e) {
        // This method runs whenever inventory click event is fired
        if (e.getRawSlot() == 0) player.sendMessage("You clicked.");
    }
    public static void UpdateSlots(Player player, Inventory inv) {
        // This method wll set the inventory slots
        inv.setItem(0, new ItemStack(Material.DIAMOND_SWORD));
    }
}
