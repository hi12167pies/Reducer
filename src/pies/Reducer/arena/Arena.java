package pies.Reducer.arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import pies.Reducer.Config;
import pies.Reducer.api.PlayerBot;
import pies.Reducer.api.ScoreboardMang;
import pies.Reducer.commands.Reducer;
import pies.Reducer.utils.Bungee;

import java.util.*;

import static pies.Reducer.Config.MainClass;
import static pies.Reducer.Config.arenaFile;
import static pies.Reducer.api.PlayerBot.NPCOwner;

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
        float p = loc.getPitch();
        float ya = loc.getYaw();
        Double yaw = (double) ya;
        Double pitch = (double) p;

        arenaFile.getConfig().set("arena." + arena + ".spawn.x", x);
        arenaFile.getConfig().set("arena." + arena + ".spawn.y", y);
        arenaFile.getConfig().set("arena." + arena + ".spawn.z", z);
        arenaFile.getConfig().set("arena." + arena + ".spawn.world", world);
        arenaFile.getConfig().set("arena." + arena + ".spawn.yaw", yaw);
        arenaFile.getConfig().set("arena." + arena + ".spawn.pitch", pitch);

        arenaFile.save();
    }

    public static void delete(String arena) {
        arenaFile.getConfig().set("arena."+arena, null);
        arenaFile.save();
    }

    public static boolean ArenaInUse(String arena) {
        return Arena.PlayerPlaying.containsValue(arena);
    }
    public static Location getArenaSpawn(String arena) {
        double x = arenaFile.getConfig().getDouble("arena."+arena+".spawn.x");
        double y = arenaFile.getConfig().getDouble("arena."+arena+".spawn.y");
        double z = arenaFile.getConfig().getDouble("arena."+arena+".spawn.z");
        double pitch = arenaFile.getConfig().getDouble("arena."+arena+".spawn.pitch");
        double yaw = arenaFile.getConfig().getDouble("arena."+arena+".spawn.yaw");
        String world = arenaFile.getConfig().getString("arena."+arena+".spawn.world");

        World w = Bukkit.getWorld(world);

        return new Location(w,x,y,z, (float) yaw, (float) pitch);
    }

    public static void DefaultJoinErrorFallback(Player player) {
        player.sendMessage("§cThe arena was not found. If you believe this is a error please contact the server admins.");
        if (MainClass.getConfig().getBoolean("BungeeCord") && MainClass.getConfig().getBoolean("BungeeCord_fallback.enabled"))
            Bungee.send(player, MainClass.getConfig().getString("BungeeCord_fallback.server"));
    }

    public static void joinNextArena(Player player) {
        if (arenaFile.getConfig().getConfigurationSection("arena") == null) {
            DefaultJoinErrorFallback(player);
            return;
        }
        Set<String> arenalist = arenaFile.getConfig().getConfigurationSection("arena").getKeys(false);
        if (arenalist.toArray().length == 0) {
            DefaultJoinErrorFallback(player);
            return;
        }
        joinArenaListed(player, arenalist);
    }

    public static boolean findArenaPossible(Player player, String start, String end) {
        if (arenaFile.getConfig().getConfigurationSection("arena") == null) {
            return false;
        }
        Set<String> arenalist = arenaFile.getConfig().getConfigurationSection("arena").getKeys(false);
        if (arenalist.toArray().length == 0) {
            DefaultJoinErrorFallback(player);
            return false;
        }
        Set<String> filtered = new HashSet<>();
        for (String cur : arenalist) {
            if (cur.startsWith(start) && cur.endsWith(end)) {
                filtered.add(cur);
            }
        }

        boolean ingame = false;
        for (String i : filtered) {
            if (!PlayerPlaying.containsValue(i)) {
                ingame = true;
                break;
            }
        }
        return ingame;
    }

    public static void joinNextArenaFilter(Player player, String start, String end) {
        if (arenaFile.getConfig().getConfigurationSection("arena") == null) {
            DefaultJoinErrorFallback(player);
            return;
        }
        Set<String> arenalist = arenaFile.getConfig().getConfigurationSection("arena").getKeys(false);
        if (arenalist.toArray().length == 0) {
            DefaultJoinErrorFallback(player);
            return;
        }
        Set<String> filtered = new HashSet<>();
        for (String cur : arenalist) {
            if (cur.startsWith(start) && cur.endsWith(end)) {
                filtered.add(cur);
            }
        }
        joinArenaListed(player, filtered);
    }

    public static void joinArenaListed(Player player, Set<String> arenalist) {
        boolean ingame = false;
        for (String i : arenalist) {
            if (!PlayerPlaying.containsValue(i)) {
                joinArena(player, i);
                ingame = true;
                break;
            }
        }
        if (!ingame) DefaultJoinErrorFallback(player);
    }

    public static void joinArena(Player player, String arena) {
        if (PlayerPlaying.containsValue(arena)) {
            player.sendMessage("§cArena in use.");
            return;
        }
        PlayerPlaying.put(player, arena);
        player.getInventory().clear();
        Reducer.setItems(player, player.getInventory());
        ScoreboardMang.ShowScoreboard(player);
        player.teleport(getArenaSpawn(arena));
        runStartCommands(player);
    }

    public static void leaveArena(Player player) {
        if (!PlayerPlaying.containsKey(player)) {
            player.sendMessage("§cYou're not in a game");
            return;
        }
        leaveArenaFinal(player);
        runEndCommands(player);
    }
    public static void leaveArenaFinal(Player player) {
        Arena.ResetBlocks(player);
        PlayerPlaying.remove(player);
        player.getInventory().clear();
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        if (NPCOwner.containsKey(player)) {
            PlayerBot.setName(player, Config.DefaultName);
            PlayerBot.setSkin(player, Config.DefaultSkin);
            NPCOwner.remove(player);
        }
    }
    public static boolean checkArenaExist(String arena) {
        return arenaFile.getConfig().getConfigurationSection("arena").getKeys(false).contains(arena);
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
            byte b = block.getData();
            block.setType(Material.AIR);

            FallingBlock bl = block.getLocation().getWorld().spawnFallingBlock(block.getLocation(), type, b);
            Bukkit.getScheduler().scheduleSyncDelayedTask(MainClass, new Runnable() { @Override public void run() {
                bl.remove();
                if (bl.getLocation().getBlock().getType() == type) {
                    bl.getLocation().getBlock().setType(Material.AIR);
                }
            }}, 20);
        }
        Block.get(player).clear();
    }

    public static void runStartCommands(Player player) {
        if (!MainClass.getConfig().getBoolean("commands.enabled.join")) return;
        for (String s : MainClass.getConfig().getStringList("commands.join_arena")) {
            String cmd = s;
            if (cmd.startsWith("/")) cmd = s.substring(1);
            player.performCommand(cmd);
        }
    }
    public static void runEndCommands(Player player) {
        if (!MainClass.getConfig().getBoolean("commands.enabled.leave")) return;
        for (String s: MainClass.getConfig().getStringList("commands.leave_arena")) {
            String cmd = s;
            if (cmd.startsWith("/")) cmd = s.substring(1);
            player.performCommand(cmd);
        }
    }
}