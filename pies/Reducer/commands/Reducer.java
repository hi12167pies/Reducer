package pies.Reducer.commands;

import net.minecraft.server.v1_8_R3.ItemStack;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import pies.Reducer.Main;
import pies.Reducer.arena.Arena;
import pies.Reducer.utils.ItemBuilder;

import static pies.Reducer.Config.MainClass;

public class Reducer implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length >= 1) {
            if (!(sender instanceof Player)) return false;
            Player player = (Player) sender;
            if (args[0].equals("gui")) ReducerGUI.openGUI(player);
            else if (args[0].equals("join")) {
                if (Arena.PlayerPlaying.containsKey(player)) {
                    player.sendMessage("You're in a game.");
                    return true;
                }
                Arena.joinNextArena(player);
            } else if (args[0].equals("leave")) {
                if (!Arena.PlayerPlaying.containsKey(player)) {
                    player.sendMessage("You're not in a game.");
                    return true;
                } else if (MainClass.getConfig().getBoolean("BungeeCord")) {
                    if (player.hasPermission("reducer.leave"))
                        Arena.leaveArena(player);
                    else
                        player.sendMessage("You do have permission to execute this command.");
                } else Arena.leaveArena(player);
            }
            else player.sendMessage(command.getUsage());
        } else {
            sender.sendMessage("");
            sender.sendMessage("§aCitizensReducer §7by §dhi12167pies");
            sender.sendMessage("§e§oHint: §7§o/reducer gui for options.");
            sender.sendMessage("");
        }

        return true;
    }

    public static void setItems(Inventory i) {
        Enchantment[] e = {Enchantment.KNOCKBACK}; int[] n = {1};
        i.setItem(0, new ItemBuilder(Material.STICK).ench(e, n).hideEnch().build());
        i.setItem(1, new ItemBuilder(Material.SANDSTONE).amount(64).build());
        i.setItem(2, new ItemBuilder(Material.SANDSTONE).amount(64).build());
        i.setItem(8, new ItemBuilder(Material.EMERALD).name("§2Settings").build());
    }
}
