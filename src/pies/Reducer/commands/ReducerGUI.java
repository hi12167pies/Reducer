package pies.Reducer.commands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pies.Reducer.arena.Arena;
import pies.Reducer.commands.GUI.Cosmetics;
import pies.Reducer.commands.GUI.InventorySort;
import pies.Reducer.commands.GUI.IslandSelect;
import pies.Reducer.commands.GUI.Settings;
import pies.Reducer.commands.GUI.SubMenus.Glass;
import pies.Reducer.commands.GUI.SubMenus.Wool;
import pies.Reducer.utils.ItemBuilder;

public class ReducerGUI implements Listener {
    @EventHandler
    public void Interact(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getInventory().getHolder() == SETTINGS ||
                e.getInventory().getHolder() == COSMETICS ||
                e.getInventory().getHolder() == INVENTORY_SORT ||
                e.getInventory().getHolder() == ISLAND) e.setCancelled(true);
        if (e.getClickedInventory() == null) return;
        InventoryHolder holder = e.getClickedInventory().getHolder();
        if (holder == SETTINGS || holder == COSMETICS || holder == INVENTORY_SORT || holder == ISLAND) {
            if (e.getRawSlot() == 19) {
                player.openInventory(Cosmetics.inv(player));
            } else if (e.getRawSlot() == 10) {
                player.openInventory(Settings.SettingsINV(player));
            } else if (e.getRawSlot() == 28) {
                player.openInventory(IslandSelect.inv(player));
            } else if (e.getRawSlot() == 37) {
                //player.openInventory(InventorySort.inv(player));
                player.openInventory(player.getInventory());
            }
        }
        if (holder == SETTINGS) {
            Settings.exe(player, e);
        } else if (holder == COSMETICS) {
            Cosmetics.exe(player, e);
        } else if (holder == INVENTORY_SORT) {
            InventorySort.exe(player, e);
        } else if (holder == ISLAND) {
            IslandSelect.exe(player, e);
        } else if (holder == COSMETICS_WOOL) {
            e.setCancelled(true);
            Wool.exe(player, e);
        } else if (holder == COSMETICS_GLASS) {
            e.setCancelled(true);
            Glass.exe(player, e);
        }
    }

    public static void openGUI(Player player) {
        if (!Arena.playerInGame(player)) {
            player.sendMessage("§cYou are not in game.");
            return;
        }
        player.openInventory(Settings.SettingsINV(player));
    }

    public static void DefaultGUIFormat(Player p, Inventory inv) {
        inv.setItem(10, new ItemBuilder(Material.IRON_INGOT).name("§aSettings").build());
        inv.setItem(19, new ItemBuilder(Material.DIAMOND_SWORD).name("§aCosmetics").hideAttr().build());
        inv.setItem(28, new ItemBuilder(Material.BOOK).name("§eIslands").build());
        inv.setItem(37, new ItemBuilder(Material.ARMOR_STAND).name("§9Inventory Sort").build());
    }

    public static InventoryHolder SETTINGS = () -> null;
    public static InventoryHolder COSMETICS = () -> null;
    public static InventoryHolder COSMETICS_WOOL = () -> null;
    public static InventoryHolder COSMETICS_GLASS = () -> null;
    public static InventoryHolder INVENTORY_SORT = () -> null;
    public static InventoryHolder ISLAND = () -> null;
}
