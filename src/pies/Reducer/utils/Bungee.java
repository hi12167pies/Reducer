package pies.Reducer.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import static pies.Reducer.Config.MainClass;

public class Bungee {
    public static void send(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        sendPluginMessage(player, "BungeeCord", out.toByteArray(), MainClass);
    }
    public static void sendPluginMessage(Player player, String channel, byte[] out, Plugin plugin) {
        if (player == null)
            Bukkit.getServer().sendPluginMessage(plugin, channel, out);
        else
            player.sendPluginMessage(plugin, channel, out);
    }
}