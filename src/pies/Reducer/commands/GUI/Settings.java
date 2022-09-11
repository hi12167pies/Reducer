package pies.Reducer.commands.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import pies.Reducer.Config;
import pies.Reducer.api.PlayerSettings;
import pies.Reducer.commands.ReducerGUI;
import pies.Reducer.events.Chat;
import pies.Reducer.utils.ConvertNumber;
import pies.Reducer.utils.ItemBuilder;

public class Settings {
    public static Inventory SettingsINV(Player player) {
        Inventory inv = Bukkit.createInventory(ReducerGUI.SETTINGS, 54, "Settings");
        UpdateSettingsSlot(player, inv);
        ReducerGUI.DefaultGUIFormat(player, inv);
        return inv;
    }
    public static void exe(Player player, InventoryClickEvent e) {
        if (e.getRawSlot() == 16) {
            if (e.getClick() == ClickType.LEFT) {
                PlayerSettings.Reach.put(player, PlayerSettings.getReach(player) + 0.5);
                UpdateSettingsSlot(player, e.getInventory());
            } else if (e.getClick() == ClickType.RIGHT) {
                PlayerSettings.Reach.put(player, PlayerSettings.getReach(player) - 0.5);
                UpdateSettingsSlot(player, e.getInventory());
            }
        } else if (e.getRawSlot() == 14) {
            if (e.getClick() == ClickType.LEFT) {
                PlayerSettings.HKB.put(player, PlayerSettings.getHKB(player) + 0.05);
                UpdateSettingsSlot(player, e.getInventory());
            } else if (e.getClick() == ClickType.RIGHT) {
                PlayerSettings.HKB.put(player, PlayerSettings.getHKB(player) - 0.05);
                UpdateSettingsSlot(player, e.getInventory());
            }
        } else if (e.getRawSlot() == 12) {
            if (e.getClick() == ClickType.LEFT) {
                PlayerSettings.VKB.put(player, PlayerSettings.getVKB(player) + 0.001);
                UpdateSettingsSlot(player, e.getInventory());
            } else if (e.getClick() == ClickType.RIGHT) {
                PlayerSettings.VKB.put(player, PlayerSettings.getVKB(player) - 0.001);
                UpdateSettingsSlot(player, e.getInventory());
            }
        } else if (e.getRawSlot() == 21) {
            player.sendMessage("§cYour vertical knockback has been reset.");
            PlayerSettings.VKB.remove(player);
            UpdateSettingsSlot(player, e.getClickedInventory());
        } else if (e.getRawSlot() == 23) {
            player.sendMessage("§cYour horizontal knockback has been reset.");
            PlayerSettings.HKB.remove(player);
            UpdateSettingsSlot(player, e.getClickedInventory());
        } else if (e.getRawSlot() == 25) {
            player.sendMessage("§cYour reach multiplier has been reset.");
            PlayerSettings.Reach.remove(player);
            UpdateSettingsSlot(player, e.getClickedInventory());
        } else if (e.getRawSlot() == 39) {
            player.closeInventory();
            player.sendMessage("§aPlease chose a new reducer name.");
            Chat.ChatScope.put(player, 0);
        } else if (e.getRawSlot() == 40) {
            player.closeInventory();
            player.sendMessage("§aPlease chose a new reducer skin.");
            Chat.ChatScope.put(player, 1);
        } else if (e.getRawSlot() == 43) {
            if (PlayerSettings.Bouncy.contains(player))
                PlayerSettings.Bouncy.remove(player);
            else
                PlayerSettings.Bouncy.add(player);
            UpdateSettingsSlot(player, e.getClickedInventory());
        } else if (e.getRawSlot() == 42) {
            int delay = PlayerSettings.getTickDelay(player);
            if (e.getClick() == ClickType.LEFT) PlayerSettings.TickDelay.put(player, delay >= 20 ? 20 : delay + 1);
            else if (e.getClick() == ClickType.RIGHT) PlayerSettings.TickDelay.put(player, delay <= 5 ? 5 : delay - 1);
            UpdateSettingsSlot(player, e.getInventory());
        }
    }
    public static void UpdateSettingsSlot(Player player, Inventory inv) {
        String VKB = ConvertNumber.Clean(PlayerSettings.getVKB(player).toString());
        String HKB = ConvertNumber.Clean(PlayerSettings.getHKB(player).toString());
        String Reach = ConvertNumber.Clean(PlayerSettings.getReach(player).toString());
        String TickDelay = ConvertNumber.Clean(""+PlayerSettings.getTickDelay(player));

        inv.setItem(12, new ItemBuilder(Material.STICK).name("§aVertical Knockback").lore("§eValue: §7" + VKB).build());
        inv.setItem(14, new ItemBuilder(Material.STICK).name("§aHorizontal Knockback").lore("§eValue: §7" + HKB).build());
        inv.setItem(16, new ItemBuilder(Material.STICK).name("§aReach multiplier").lore("§eValue: §7" + Reach).build());

        inv.setItem(21, new ItemBuilder(Material.REDSTONE).name("§cReset Vertical Knockback").lore("§eTo: " + ConvertNumber.Clean(Config.DefaultVKB.toString())).build());
        inv.setItem(23, new ItemBuilder(Material.REDSTONE).name("§cReset Horizontal Knockback").lore("§eTo: " + ConvertNumber.Clean(Config.DefaultHKB.toString())).build());
        inv.setItem(25, new ItemBuilder(Material.REDSTONE).name("§cReset Reach multiplier").lore("§eTo: " + ConvertNumber.Clean(Config.DefaultReach.toString())).build());

        inv.setItem(39, new ItemBuilder(Material.NAME_TAG).name("§aSet NPC Name").build());
        inv.setItem(40, new ItemBuilder(Material.NAME_TAG).name("§aSet NPC Skin").build());
        inv.setItem(42, new ItemBuilder(Material.REDSTONE_COMPARATOR).name("§6Tick delay").lore("§eValue: §7" + TickDelay).build());
        inv.setItem(43, new ItemBuilder(Material.STICK).name("§bReducer Knockback").lore("§7When you hit the reducer you and", "§7the bot will take knockback.", "§7", "§eValue: §7" + PlayerSettings.Bouncy.contains(player)).build());
    }
}
