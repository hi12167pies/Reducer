package pies.Reducer.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import pies.Reducer.api.InvItems;
import pies.Reducer.arena.Arena;
import pies.Reducer.utils.ItemBuilder;

import static pies.Reducer.Config.MainClass;

public class Reducer implements CommandExecutor {
    public static void setItems(Player player, Inventory i) {
        i.clear();
        Enchantment[] e = {Enchantment.KNOCKBACK}; int[] n = {1};
        i.setItem(InvItems.getStickSlot(player), new ItemBuilder(InvItems.getStickMaterial(player)).ench(e, n).unBreak().hideUnBreak().hideAttr().hideEnch().build());
        i.setItem(InvItems.getBlockSlot(player), new ItemBuilder(InvItems.getBlockMaterial(player), InvItems.getBlockData(player)).amount(64).build());

        if (MainClass.getConfig().getBoolean("BungeeCord")) {
            i.setItem(8, new ItemBuilder(Material.EMERALD).name( "§2Settings").build());
        } else {
            i.setItem(7, new ItemBuilder(Material.EMERALD).name( "§2Settings").build());
            i.setItem(8, new ItemBuilder(Material.EYE_OF_ENDER).name("§cLeave").build());
        }
    }

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
                if (args.length >= 2) {
                    if (args.length >= 3) {
                        // This now means they have done /reducer join -s something
                        String type = args[1];
                        String modifier = args[2];
                        if (type.equals("-s") || type.equals("-start")) {
                            Arena.joinNextArenaFilter(player, modifier, "");
                        } else if (type.equals("-e") || type.equals("-end")) {
                            // Run end code
                            Arena.joinNextArenaFilter(player, "", modifier);
                        } else {
                            player.sendMessage("§cInvalid type");
                        }
                    } else {
                        // Here is /reducer join <map>
                        String map = args[1];
                        if (!Arena.checkArenaExist(map)) {
                            sender.sendMessage("§cThat map doesn't exist.");
                            return true;
                        }
                        if (Arena.ArenaInUse(map)) {
                            sender.sendMessage("§cArena in use" + map);
                            return true;
                        }
                        Arena.joinArena(player, map);
                    }
                    // Otherwise it's /reducer join
                } else Arena.joinNextArena(player);
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
            sender.sendMessage("");
        }

        return true;
    }
}
