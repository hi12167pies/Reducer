package pies.Reducer.api;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static pies.Reducer.Config.MainClass;

public class InvItems {
    public static HashMap<Player, Material> Stick = new HashMap<>();
    public static HashMap<Player, Material> Block = new HashMap<>();
    public static HashMap<Player, Byte> BlockData = new HashMap<>();

    public static Material getStickMaterial(Player player) {
        if (Stick.containsKey(player)) return Stick.get(player);
        return Material.STICK;
    }

    public static void setBlockMaterial(Player player, Material met, Byte b) {
        Block.put(player, met);
        BlockData.put(player, b);
    }

    public static byte getBlockData(Player player) {
        if (BlockData.containsKey(player)) return BlockData.get(player);
        return (byte) 0;
    }
    public static Material getBlockMaterial(Player player) {
        if (Block.containsKey(player)) return Block.get(player);
        return Material.SANDSTONE;
    }
    public static HashMap<Player, Integer> BlockSlot = new HashMap<>();
    public static HashMap<Player, Integer> StickSlot = new HashMap<>();

    public static int getBlockSlot(Player player) {
        int d = 2;
        if (BlockSlot.containsKey(player)) {
            int value = BlockSlot.get(player);
            return (value == -1 || value >= (MainClass.getConfig().getBoolean("BungeeCord") ? 8 : 7)) ? d : value;
        }
        return d;
    }
    public static int getStickSlot(Player player) {
        int d = 0;
        if (StickSlot.containsKey(player)) {
            int value = StickSlot.get(player);
            return (value == -1 || value >= (MainClass.getConfig().getBoolean("BungeeCord") ? 8 : 7)) ? d : value;
        }
        return d;
    }
}