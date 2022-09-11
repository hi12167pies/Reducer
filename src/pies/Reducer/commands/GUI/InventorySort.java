package pies.Reducer.commands.GUI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import pies.Reducer.api.InvItems;
import pies.Reducer.commands.Reducer;
import pies.Reducer.commands.ReducerGUI;

public class InventorySort {

    // Special for this class only
    public static void InvClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        if (player.getInventory().getHolder() == e.getInventory().getHolder()) {
            Inventory inv = player.getInventory();
            int stick = inv.first(InvItems.getStickMaterial(player));
            int sandstone = inv.first(InvItems.getBlockMaterial(player));
            boolean changed = false;
            if (stick != InvItems.getStickSlot(player)) {
                InvItems.StickSlot.put(player, stick);
                changed = true;
            } else if (sandstone != InvItems.getBlockSlot(player)) {
                InvItems.BlockSlot.put(player, sandstone);
                changed = true;
            }
            if (changed) {
                Reducer.setItems(player, player.getInventory());
                player.sendMessage("§aYour inventory sorting has been updated!");
            }
        }
    }


    public static Inventory inv(Player player) {
        // This method will create the inventory that players use to open
        Inventory inv = Bukkit.createInventory(ReducerGUI.INVENTORY_SORT, 54, "Cosmetics");
        UpdateSlots(player, inv);
        ReducerGUI.DefaultGUIFormat(player, inv);
        return inv;
    }
    public static void exe(Player player, InventoryClickEvent e) {
        // This method runs whenever inventory click event is fired
    }
    public static void UpdateSlots(Player player, Inventory inv) {
        // This method wll set the inventory slots
    }
}
