package pies.Reducer.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pies.Reducer.Config;
import pies.Reducer.api.PlayerBot;
import pies.Reducer.arena.Arena;

import java.util.HashSet;

import static pies.Reducer.Config.MainClass;
import static pies.Reducer.api.PlayerBot.NPCOwner;

public class JoinLeave implements Listener {
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        if (Arena.PlayerPlaying.containsKey(player)) Arena.PlayerPlaying.remove(player);

        Arena.ResetBlocks(player);
        Arena.Block.remove(player);

        if (NPCOwner.containsKey(player)) {
            PlayerBot.setName(player, Config.DefaultName);
            PlayerBot.setSkin(player, Config.DefaultSkin);
        }

    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        Arena.Block.put(player, new HashSet<>());

        if (MainClass.getConfig().getBoolean("BungeeCord")) {
            Arena.joinNextArena(player);
        }
    }
}
