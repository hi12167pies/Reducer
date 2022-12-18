package pies.Reducer.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pies.Reducer.Config;
import pies.Reducer.api.PlayerBot;

import java.util.HashMap;

public class Chat implements Listener {
    public static HashMap<Player, Integer> ChatScope = new HashMap<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (!ChatScope.containsKey(player)) return;
        int scope = ChatScope.get(player);

        if (scope == 0 || scope == 1) {
            e.setCancelled(true);
            ChatScope.remove(player);
        }

        if (scope == 0) Bukkit.getScheduler().runTask(Config.MainClass,()->{ PlayerBot.setName(player, e.getMessage()); });
        else if (scope == 1) Bukkit.getScheduler().runTask(Config.MainClass,()->{ PlayerBot.setSkin(player, e.getMessage()); });
    }
}
