package pies.Reducer.arena;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import pies.Reducer.Config;
import pies.Reducer.api.ScoreboardMang;
import pies.Reducer.commands.GUI.InventorySort;
import pies.Reducer.commands.Reducer;
import pies.Reducer.commands.ReducerGUI;

import static pies.Reducer.Config.MainClass;

public class ArenaListener implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) return;

        if (!Arena.playerInGame(player)) return;

        String game = Arena.getPlayerArena(player);

        int XBorder = MainClass.getConfig().getInt("border.x");
        int ZBorder = MainClass.getConfig().getInt("border.z");

        if (player.getLocation().getY() < Arena.getArenaSpawn(game).getY() - 5)
            resetPlayer(player, game, "resety -y");
        else if (player.getLocation().getZ() > Arena.getArenaSpawn(game).getZ() + ZBorder)
            resetPlayer(player, game, "spawn +z");
        else if (player.getLocation().getZ() < Arena.getArenaSpawn(game).getZ() - ZBorder)
            resetPlayer(player, game, "spawn -z");
        else if (player.getLocation().getX() > Arena.getArenaSpawn(game).getX() + XBorder)
            resetPlayer(player, game, "spawn +x");
        else if (player.getLocation().getX() < Arena.getArenaSpawn(game).getX() - XBorder)
            resetPlayer(player, game, "spawn -x");
    }
    // the reason is for when i was debugging
    public static void resetPlayer(Player player, String arena, String reason) {
        player.teleport(Arena.getArenaSpawn(arena));
        Reducer.setItems(player, player.getInventory());
        if (MainClass.getConfig().getString("reset-animation").equals("fall")) {
            Arena.ResetBlocksAnimate(player);
        } else {
            Arena.ResetBlocks(player);
        }
    }
    @EventHandler
    public void interaction(PlayerInteractEvent e) {
        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)) return;
        Player player = e.getPlayer();

        if (player.getItemInHand().getType() == Material.AIR || player.getItemInHand().getItemMeta().getDisplayName() == null) return;
        if (player.getItemInHand().getItemMeta().getDisplayName().equals("§2Settings")) ReducerGUI.openGUI(player);
        if (player.getItemInHand().getItemMeta().getDisplayName().equals("§cLeave")) {
            if (Arena.playerInGame(player)) {
                Arena.leaveArena(player);
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) return;

        if (!Arena.playerInGame(player)) return;

        e.getPlayer().getItemInHand().setAmount(64);
        ScoreboardMang.ShowScoreboard(player);

        Arena.Block.get(player).add(e.getBlock());
    }
    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        if (!Arena.playerInGame((Player) e.getPlayer())) return;
        InventorySort.InvClose(e);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) return;

        if (!Arena.playerInGame(player)) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) return;

        if (!Arena.playerInGame(player)) return;
        if (!Arena.Block.get(player).contains(e.getBlock()))
            e.setCancelled(true);
    }
}
