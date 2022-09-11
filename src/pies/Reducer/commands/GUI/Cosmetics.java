package pies.Reducer.commands.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import pies.Reducer.api.InvItems;
import pies.Reducer.commands.GUI.SubMenus.Glass;
import pies.Reducer.commands.GUI.SubMenus.Wool;
import pies.Reducer.commands.Reducer;
import pies.Reducer.commands.ReducerGUI;
import pies.Reducer.utils.ItemBuilder;

public class Cosmetics {
    public static Inventory inv(Player player) {
        Inventory inv = Bukkit.createInventory(ReducerGUI.COSMETICS, 54, "Cosmetics");
        UpdateSlots(player, inv);
        ReducerGUI.DefaultGUIFormat(player, inv);
        return inv;
    }

    public static void UpdateStickType(Player player, Material met, Inventory i) {
        InvItems.Stick.put(player, met);
        Reducer.setItems(player, player.getInventory());
        UpdateSlots(player, i);
    }
    public static void UpdateBlockType(Player player, Material met, Inventory i) {
        InvItems.setBlockMaterial(player, met, (byte) 0);
        Reducer.setItems(player, player.getInventory());
        UpdateSlots(player, i);
    }
    public static void exe(Player player, InventoryClickEvent e) {
        if (e.getRawSlot() == 12) UpdateStickType(player, Material.STICK, e.getClickedInventory());
        else if (e.getRawSlot() == 13) UpdateStickType(player, Material.BLAZE_ROD, e.getClickedInventory());
        else if (e.getRawSlot() == 14) UpdateStickType(player, Material.DIAMOND_SWORD, e.getClickedInventory());
        else if (e.getRawSlot() == 15) UpdateStickType(player, Material.BONE, e.getClickedInventory());
        else if (e.getRawSlot() == 16) UpdateStickType(player, Material.DIAMOND_HOE, e.getClickedInventory());

        else if (e.getRawSlot() == 30) UpdateBlockType(player, Material.SANDSTONE, e.getClickedInventory());
        else if (e.getRawSlot() == 31) UpdateBlockType(player, Material.RED_SANDSTONE, e.getClickedInventory());
        else if (e.getRawSlot() == 32) player.openInventory(Wool.inv(player));
        else if (e.getRawSlot() == 33) UpdateBlockType(player, Material.PACKED_ICE, e.getClickedInventory());
        else if (e.getRawSlot() == 34) UpdateBlockType(player, Material.GLASS, e.getClickedInventory());
        // New line
        else if (e.getRawSlot() == 39) player.openInventory(Glass.inv(player));

    }
    public static void UpdateSlots(Player player, Inventory inv) {
        inv.setItem(12, new ItemBuilder(Material.STICK).name("§cStick").lore("§eCosmetic: §7STICK", getSelectedLore(InvItems.getStickMaterial(player), Material.STICK)).build());
        inv.setItem(13, new ItemBuilder(Material.BLAZE_ROD).name("§6Blaze rod").lore("§eCosmetic: §7STICK", getSelectedLore(InvItems.getStickMaterial(player), Material.BLAZE_ROD)).build());
        inv.setItem(14, new ItemBuilder(Material.DIAMOND_SWORD).name("§bDiamond sword").lore("§eCosmetic: §7STICK", getSelectedLore(InvItems.getStickMaterial(player), Material.DIAMOND_SWORD)).hideAttr().build());
        inv.setItem(15, new ItemBuilder(Material.BONE).name("§fBone").lore("§eCosmetic: §7STICK", getSelectedLore(InvItems.getStickMaterial(player), Material.BONE)).build());
        inv.setItem(16, new ItemBuilder(Material.DIAMOND_HOE).name("§bHoe").lore("§eCosmetic: §7STICK", getSelectedLore(InvItems.getStickMaterial(player), Material.DIAMOND_HOE)).hideAttr().build());

        // LINE 1
        inv.setItem(30, new ItemBuilder(Material.SANDSTONE).name("§eSandstone").lore("§eCosmetic: §7BLOCK", getSelectedLore(InvItems.getBlockMaterial(player), Material.SANDSTONE)).build());
        inv.setItem(31, new ItemBuilder(Material.RED_SANDSTONE).name("§cRed sandstone").lore("§eCosmetic: §7BLOCK", getSelectedLore(InvItems.getBlockMaterial(player), Material.RED_SANDSTONE)).build());
        inv.setItem(32, new ItemBuilder(Material.WOOL).name("§fWool").lore("§eCosmetic: §7BLOCK", getSelectedLore(InvItems.getBlockMaterial(player), Material.WOOL)).build());
        inv.setItem(33, new ItemBuilder(Material.PACKED_ICE).name("§bPacked ice").lore("§eCosmetic: §7BLOCK", getSelectedLore(InvItems.getBlockMaterial(player), Material.PACKED_ICE)).build());
        inv.setItem(34, new ItemBuilder(Material.GLASS).name("§fGlass").lore("§eCosmetic: §7BLOCK", getSelectedLore(InvItems.getBlockMaterial(player), Material.GLASS)).build());
        // LINE 2
        inv.setItem(39, new ItemBuilder(Material.STAINED_GLASS).name("§bStained Glass").lore("§eCosmetic: §7BLOCK", getSelectedLore(InvItems.getBlockMaterial(player), Material.STAINED_GLASS)).build());
    }
    public static String getSelectedLore(Material m, Material i) {
        if (m == i) return "§aSELECTED";
        return "§7Click to select";
    }
}
