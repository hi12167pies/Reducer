package pies.Reducer.api;

import org.bukkit.entity.Player;
import pies.Reducer.Config;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerSettings {
    public static HashMap<Player, Double> HKB = new HashMap<>();
    public static HashMap<Player, Double> VKB = new HashMap<>();
    public static HashMap<Player, Double> Reach = new HashMap<>();
    public static HashMap<Player, Integer> TickDelay = new HashMap<>();

    public static ArrayList<Player> Bouncy = new ArrayList<>();

    public static Double getHKB(Player player) {
        if (HKB.containsKey(player)) return HKB.get(player);
        return Config.DefaultHKB;
     }

     public static Double getVKB(Player player) {
         if (VKB.containsKey(player)) return VKB.get(player);
         return Config.DefaultVKB;
     }

    public static Double getReach(Player player) {
        if (Reach.containsKey(player)) return Reach.get(player);
        return Config.DefaultReach;
    }

    public static int getTickDelay(Player player) {
        if (TickDelay.containsKey(player)) return TickDelay.get(player);
        return Config.DefaultTickDelay;
    }
    public static Double getRealReach(Player player) {
        return Config.Reach * getReach(player);
    }
}
