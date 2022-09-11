package pies.Reducer;

import org.bukkit.configuration.file.FileConfiguration;
import pies.Reducer.utils.files.ArenaData;
import pies.Reducer.utils.files.PlayerData;

public class Config {
    public static void InitConfig() {
        Main main = Main.getPlugin(Main.class);
        FileConfiguration Config = main.getConfig();

        MainClass = main;

        DefaultHKB = Config.getDouble("Default_HKB");
        DefaultVKB = Config.getDouble("Default_VKB");
        DefaultReach = Config.getDouble("Default_Reach");
        Reach = Config.getDouble("BotReach");
        DisableFallDamage = Config.getBoolean("FallDamage");
        DefaultName = Config.getString("npc.name");
        DefaultSkin = Config.getString("npc.skin");
        arenaFile = new ArenaData();
        dataFile = new PlayerData();
    }


    public static ArenaData arenaFile;
    public static PlayerData dataFile;

    public static String DefaultName;
    public static String DefaultSkin;

    public static Main MainClass;

    public static Boolean DisableFallDamage;
    public static Double DefaultHKB;
    public static Double DefaultVKB;
    public static Double DefaultReach;
    public static Double Reach;
    public static int DefaultTickDelay = 10;
}
