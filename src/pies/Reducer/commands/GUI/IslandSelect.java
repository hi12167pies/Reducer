package pies.Reducer.commands.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import pies.Reducer.arena.Arena;
import pies.Reducer.commands.ReducerGUI;
import pies.Reducer.utils.ItemBuilder;

import java.util.Set;

import static pies.Reducer.Config.MainClass;

public class IslandSelect {
    public static Inventory inv(Player player) {
        Inventory inv = Bukkit.createInventory(ReducerGUI.ISLAND, 54, "Island select");
        UpdateSlots(player, inv);
        ReducerGUI.DefaultGUIFormat(player, inv);
        return inv;
    }
    public static void exe(Player player, InventoryClickEvent e) {
        // This method runs whenever inventory click event is fired

        if (e.getCurrentItem().getType() == Material.BOOK) {
            String name = e.getCurrentItem().getItemMeta().getDisplayName()
                    // Convert to original string
                    .substring(2).replaceAll("§", "&");
            for (String slot : MainClass.getConfig().getConfigurationSection("islands").getKeys(false)) {
                String loop_name = MainClass.getConfig().getString("islands."+slot+".name");
                if (name.equals(loop_name)) {
                    String mapkey = MainClass.getConfig().getString("islands."+slot+".join");
                    Arena.leaveArenaFinal(player);
                    Arena.joinNextArenaFilter(player, mapkey, "");
                    if (!Arena.playerInGame(player)) {
                        if (player.isOnline())
                            Arena.runEndCommands(player);
                    }
                    break;
                }
            }
        }
    }
    public static void UpdateSlots(Player player, Inventory inv) {
        // This method wll set the inventory slots
        Set<String> keys = MainClass.getConfig().getConfigurationSection("islands").getKeys(false);

        for (String slot : keys) {
            String name = MainClass.getConfig().getString("islands."+slot+".name").replace("&", "§");
            inv.setItem(12 + Integer.parseInt(slot), new ItemBuilder(Material.BOOK).name("§f"+name).build());
        }
    }
}
