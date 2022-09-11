package pies.Reducer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pies.Reducer.api.ReducerAPI;
import pies.Reducer.arena.ArenaListener;
import pies.Reducer.commands.MapArena;
import pies.Reducer.commands.Reducer;
import pies.Reducer.commands.ReducerGUI;
import pies.Reducer.events.Chat;
import pies.Reducer.events.Damage;
import pies.Reducer.events.JoinLeave;

public class Main extends JavaPlugin {
    public ReducerAPI api = new ReducerAPI();
    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        pies.Reducer.Config.InitConfig();

        if (this.getConfig().getBoolean("BungeeCord") && this.getConfig().getBoolean("BungeeCord_fallback.enabled"))
            Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        Bukkit.getPluginManager().registerEvents(new Damage(), this);
        Bukkit.getPluginManager().registerEvents(new ReducerGUI(), this);
        Bukkit.getPluginManager().registerEvents(new ArenaListener(), this);
        Bukkit.getPluginManager().registerEvents(new Chat(), this);
        Bukkit.getPluginManager().registerEvents(new JoinLeave(), this);
        Bukkit.getPluginCommand("reducer").setExecutor(new Reducer());
        Bukkit.getPluginCommand("reducermap").setExecutor(new MapArena());
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }
}