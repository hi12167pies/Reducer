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
            Arena.setArenaSpawn(map, ((Player) sender).getLocation());
            sender.sendMessage("Map set (OVERWROTE OR SET)");
        }

        return true;
    }
}
