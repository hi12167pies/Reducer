package pies.Reducer.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pies.Reducer.Config;
import pies.Reducer.api.Knockback;
import pies.Reducer.events.Chat;
import pies.Reducer.utils.ConvertNumber;
import pies.Reducer.utils.ItemBuilder;

public class ReducerGUI implements Listener {
    @EventHandler
    public void Interact(InventoryClickEvent e) {
        if (e.getInventory().getHolder() != GUI) return;
        Player player = (Player) e.getWhoClicked();
        e.setCancelled(true);

        if (e.getRawSlot() == 16) {
            if (e.getClick() == ClickType.LEFT) {
                Knockback.Reach.put(player, Knockback.getReach(player) + 0.5);
                UpdateSettingsSlot(player, e.getInventory());
            } else if (e.getClick() == ClickType.RIGHT) {
                Knockback.Reach.put(player, Knockback.getReach(player) - 0.5);
                UpdateSettingsSlot(player, e.getInventory());
            }
        } else if (e.getRawSlot() == 14) {
            if (e.getClick() == ClickType.LEFT) {
                Knockback.HKB.put(player, Knockback.getHKB(player) + 0.05);
                UpdateSettingsSlot(player, e.getInventory());
            } else if (e.getClick() == ClickType.RIGHT) {
                Knockback.HKB.put(player, Knockback.getHKB(player) - 0.05);
                UpdateSettingsSlot(player, e.getInventory());
            }
        } else if (e.getRawSlot() == 12) {
            if (e.getClick() == ClickType.LEFT) {
                Knockback.VKB.put(player, Knockback.getVKB(player) + 0.001);
                UpdateSettingsSlot(player, e.getInventory());
            } else if (e.getClick() == ClickType.RIGHT) {
                Knockback.VKB.put(player, Knockback.getVKB(player) - 0.001);
                UpdateSettingsSlot(player, e.getInventory());
            }
        } else if (e.getRawSlot() == 21) {
            player.sendMessage("§cYour vertical knockback has been reset.");
            Knockback.VKB.remove(player);
            UpdateSettingsSlot(player, e.getClickedInventory());
        } else if (e.getRawSlot() == 23) {
            player.sendMessage("§cYour horizontal knockback has been reset.");
            Knockback.HKB.remove(player);
            UpdateSettingsSlot(player, e.getClickedInventory());
        } else if (e.getRawSlot() == 25) {
            player.sendMessage("§cYour reach multiplier has been reset.");
            Knockback.Reach.remove(player);
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
            if (Knockback.Bouncy.contains(player))
                Knockback.Bouncy.remove(player);
            else
                Knockback.Bouncy.add(player);
            UpdateSettingsSlot(player, e.getClickedInventory());
        }
    }

    public static void UpdateSettingsSlot(Player player, Inventory inv) {
        String VKB = ConvertNumber.Clean(Knockback.getVKB(player).toString());
        String HKB = ConvertNumber.Clean(Knockback.getHKB(player).toString());
        String Reach = ConvertNumber.Clean(Knockback.getReach(player).toString());

        inv.setItem(12, new ItemBuilder(Material.STICK).name("§aVertical Knockback").lore("§eValue: §7" + VKB).build());
        inv.setItem(14, new ItemBuilder(Material.STICK).name("§aHorizontal Knockback").lore("§eValue: §7" + HKB).build());
        inv.setItem(16, new ItemBuilder(Material.STICK).name("§aReach multiplier").lore("§eValue: §7" + Reach).build());

        inv.setItem(21, new ItemBuilder(Material.REDSTONE).name("§cReset Vertical Knockback").lore("§eTo: " + ConvertNumber.Clean(Config.DefaultVKB.toString())).build());
        inv.setItem(23, new ItemBuilder(Material.REDSTONE).name("§cReset Horizontal Knockback").lore("§eTo: " + ConvertNumber.Clean(Config.DefaultHKB.toString())).build());
        inv.setItem(25, new ItemBuilder(Material.REDSTONE).name("§cReset Reach multiplier").lore("§eTo: " + ConvertNumber.Clean(Config.DefaultReach.toString())).build());

        inv.setItem(39, new ItemBuilder(Material.NAME_TAG).name("§aSet NPC Name").build());
        inv.setItem(40, new ItemBuilder(Material.NAME_TAG).name("§aSet NPC Skin").build());
        inv.setItem(43, new ItemBuilder(Material.STICK).name("§bReducer Knockback").lore("§7When you hit the reducer you will", "§7The bot also takes knockback.", "§7", "§eValue: §7" + Knockback.Bouncy.contains(player)).build());
    }

    public static void openGUI(Player player) {
        Inventory inv = Bukkit.createInventory(GUI, 54, "Reducer GUI");

        inv.setItem(10, new ItemBuilder(Material.IRON_INGOT).name("§aSettings").build());
        inv.setItem(19, new ItemBuilder(Material.DIAMOND_SWORD).name("§aCosmetics §7(Soon)").hideAttr().build());
        inv.setItem(28, new ItemBuilder(Material.BOOK).name("§eIslands §7(Soon)").build());
        inv.setItem(37, new ItemBuilder(Material.ARMOR_STAND).name("§9Inventory Sort §7(Soon)").build());

        UpdateSettingsSlot(player, inv);

        player.openInventory(inv);
    }

    public static InventoryHolder GUI = () -> null;
}
