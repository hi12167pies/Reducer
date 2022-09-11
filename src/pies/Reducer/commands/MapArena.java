package pies.Reducer.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pies.Reducer.arena.Arena;

public class MapArena implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        // Usage: /reducermap <add/remove|create/delete> <map>
        if (!(sender instanceof Player)) return false;

        if (!sender.hasPermission(command.getPermission())) {
            sender.sendMessage(command.getPermissionMessage());
            return true;
        }
        if (args.length < 2) return false;


        if (args[0].equals("create") || args[0].equals("add")) {
            String map = args[1];
            Location location = ((Player) sender).getLocation();
            Arena.setArenaSpawn(map, location);
            String loc = "X: %x | Y: %y | Z: %z".replace("%x", ""+location.getX())
                    .replace("%y", ""+location.getY())
                    .replace("%z", ""+location.getZ());

            sender.sendMessage("§aYou have set " + map + "'s spawn to " + loc);
        } else if (args[0].equals("remove") || args[0].equals("delete")) {
            String map = args[1];
            if (!Arena.checkArenaExist(map)) {
                sender.sendMessage("§cThat map doesn't exist.");
                return true;
            }
            Arena.delete(map);
            sender.sendMessage("§cYou deleted " + map);
        } else sender.sendMessage(command.getUsage());

        return true;
    }
}
