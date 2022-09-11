package pies.Reducer.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pies.Reducer.api.InvItems;
import pies.Reducer.api.PlayerSettings;
import pies.Reducer.arena.Arena;

import java.util.HashSet;

import static pies.Reducer.Config.MainClass;
import static pies.Reducer.Config.dataFile;

public class JoinLeave implements Listener {
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        Player player = e.getPlayer();
        if (MainClass.getConfig().getBoolean("BungeeCord")) {
            String start = MainClass.getConfig().getString("bungee.default_map.start");
            String end = MainClass.getConfig().getString("bungee.default_map.end");

            if (!Arena.findArenaPossible(player, start, end)) {
                e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                e.setKickMessage("Reducer is currently full.");
            }
        }
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (!Arena.PlayerPlaying.containsKey(player)) return;
        Arena.leaveArenaFinal(player);
        Arena.Block.remove(player);
        savePlayerQuitData(player);
        if (Damage.taskIDS.containsKey(player)) {
            Bukkit.getScheduler().cancelTask(Damage.taskIDS.get(player));
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        joinEvents(e.getPlayer());
    }
    public static void joinEvents(Player player) {
        Arena.Block.put(player, new HashSet<>());
        loadPlayerData(player);
        if (MainClass.getConfig().getBoolean("BungeeCord")) {
            String start = MainClass.getConfig().getString("bungee.default_map.start");
            String end = MainClass.getConfig().getString("bungee.default_map.end");

            Arena.joinNextArenaFilter(player, start, end);
        }
    }

    public static void savePlayerQuitData(Player player) {
        String uuid = player.getUniqueId().toString();

        dataFile.getConfig().set("playerdata."+uuid+".knockback.reach", PlayerSettings.getReach(player));
        dataFile.getConfig().set("playerdata."+uuid+".knockback.hkb", PlayerSettings.getHKB(player));
        dataFile.getConfig().set("playerdata."+uuid+".knockback.vkb", PlayerSettings.getVKB(player));

        dataFile.getConfig().set("playerdata."+uuid+".bot.tickdelay", PlayerSettings.getTickDelay(player));
        dataFile.getConfig().set("playerdata."+uuid+".bot.bounce", PlayerSettings.Bouncy.contains(player));

        dataFile.getConfig().set("playerdata."+uuid+".cosmetic.block", InvItems.getBlockMaterial(player).toString());
        dataFile.getConfig().set("playerdata."+uuid+".cosmetic.blockdata", (int) InvItems.getBlockData(player));
        dataFile.getConfig().set("playerdata."+uuid+".cosmetic.stick", InvItems.getStickMaterial(player).toString());

        dataFile.getConfig().set("playerdata."+uuid+".invsort.block", InvItems.getBlockSlot(player));
        dataFile.getConfig().set("playerdata."+uuid+".invsort.stick", InvItems.getStickSlot(player));

        dataFile.save();
    }
    public static void loadPlayerData(Player player) {
        String uuid = player.getUniqueId().toString();

        // If player does not have data saved
        if (dataFile.getConfig().getConfigurationSection("playerdata") == null
                ||!dataFile.getConfig().getConfigurationSection("playerdata").getKeys(false).contains(uuid)) return;

        PlayerSettings.Reach.put(player, dataFile.getConfig().getDouble("playerdata."+uuid+".knockback.reach"));
        PlayerSettings.VKB.put(player, dataFile.getConfig().getDouble("playerdata."+uuid+".knockback.vkb"));
        PlayerSettings.HKB.put(player, dataFile.getConfig().getDouble("playerdata."+uuid+".knockback.hkb"));

        PlayerSettings.TickDelay.put(player, dataFile.getConfig().getInt("playerdata."+uuid+".bot.tickdelay"));

        if (dataFile.getConfig().getBoolean("playerdata."+uuid+".bot.bounce")) {
            PlayerSettings.Bouncy.add(player);
        }

        InvItems.Block.put(player, Material.getMaterial(dataFile.getConfig().getString("playerdata."+uuid+".cosmetic.block")));
        InvItems.BlockData.put(player, (byte) dataFile.getConfig().getInt("playerdata."+uuid+".cosmetic.blockdata"));
        InvItems.Stick.put(player, Material.getMaterial(dataFile.getConfig().getString("playerdata."+uuid+".cosmetic.stick")));

        InvItems.BlockSlot.put(player, dataFile.getConfig().getInt("playerdata."+uuid+".invsort.block"));
        InvItems.StickSlot.put(player, dataFile.getConfig().getInt("playerdata."+uuid+".invsort.stick"));
    }
}
