package pies.Reducer.arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import pies.Reducer.api.ScoreboardMang;
import pies.Reducer.commands.Reducer;
import pies.Reducer.utils.Bungee;

import java.util.*;

import static pies.Reducer.Config.MainClass;

public class Arena {
    // Owner, Map
    public static HashMap<Player, String> PlayerPlaying = new HashMap<>();
    // Block, Owner
    public static HashMap<Player, Set<Block>> Block = new HashMap<>();

    public static boolean playerInGame(Player player) {
        return PlayerPlaying.containsKey(player);
    }

    public static String getPlayerArena(Player player) {
        return PlayerPlaying.get(player);
    }

    public static void setArenaSpawn(String arena, Location loc) {
        Double x = loc.getX();
        Double y = loc.getY();
        Double z = loc.getZ();
        String world = loc.getWorld().getName();
        float p = loc.getPitch(); float ya = loc.getYaw();
        Double yaw = (double) ya;
        Double pitch = (double) p;


        MainClass.getConfig().set("arena." + arena + ".spawn.x", x);
        MainClass.getConfig().set("arena." + arena + ".spawn.y", y);
        MainClass.getConfig().set("arena." + arena + ".spawn.z", z);
        MainClass.getConfig().set("arena." + arena + ".spawn.world", world);
        MainClass.getConfig().set("arena." + arena + ".spawn.yaw", yaw);
        MainClass.getConfig().set("arena." + arena + ".spawn.pitch", pitch);

        MainClass.saveConfig();
    }

    public static Location getArenaSpawn(String arena) {
        double x = MainClass.getConfig().getDouble("arena."+arena+".spawn.x");
        double y = MainClass.getConfig().getDouble("arena."+arena+".spawn.y");
        double z = MainClass.getConfig().getDouble("arena."+arena+".spawn.z");
        double pitch = MainClass.getConfig().getDouble("arena."+arena+".spawn.pitch");
        double yaw = MainClass.getConfig().getDouble("arena."+arena+".spawn.yaw");
        String world = MainClass.getConfig().getString("arena."+arena+".spawn.world");

        World w = Bukkit.getWorld(world);

        return new Location(w,x,y,z, (float) yaw, (float) pitch);
    }

    public static void joinNextArena(Player player) {
        boolean ingame = false;
        for (String i : MainClass.getConfig().getConfigurationSection("arena").getKeys(false)) {
            if (!PlayerPlaying.containsValue(i)) {
                joinArena(player, i);
                ingame = true;
                break;
            }
        }
        if (ingame == false) {
            player.sendMessage("Could not find a arena.");
            if (MainClass.getConfig().getBoolean("BungeeCord") && MainClass.getConfig().getBoolean("BungeeCord_fallback.enabled"))
                Bungee.send(player, MainClass.getConfig().getString("BungeeCord_fallback.server"));
        }
    }

    public static void joinArena(Player player, String arena) {
        if (PlayerPlaying.containsValue(arena)) {
            player.sendMessage("§cArena in use.");
            return;
        }
        PlayerPlaying.put(player, arena);
        player.getInventory().clear();
        Reducer.setItems(player.getInventory());
        ScoreboardMang.ShowScoreboard(player);
        player.teleport(getArenaSpawn(arena));
    }

    public static void leaveArena(Player player) {
        if (!PlayerPlaying.containsKey(player)) {
            player.sendMessage("§cYou're not in a game");
            return;
        }
        Arena.ResetBlocks(player);
        PlayerPlaying.remove(player);
        player.getInventory().clear();
    }

    public static void ResetBlocks(Player player) {
        for (Block b : Block.get(player)) {
            b.setType(Material.AIR);
        }
        Block.get(player).clear();
    }
    public static void ResetBlocksAnimate(Player player) {
        for (Block block : Block.get(player)) {

            Material type = block.getType();
            block.setType(Material.AIR);

            FallingBlock bl = block.getLocation().getWorld().spawnFallingBlock(block.getLocation(), type, (byte) 0);
            Bukkit.getScheduler().scheduleSyncDelayedTask(MainClass, new Runnable() { @Override public void run() {
                bl.remove();
                if (bl.getLocation().getBlock().getType() == type) {
                    bl.getLocation().getBlock().setType(Material.AIR);
                }
            }}, 20);
        }
        Block.get(player).clear();
    }
}