package pies.Reducer.api;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.skin.SkinnableEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pies.Reducer.arena.Arena;
import pies.Reducer.arena.ArenaListener;
import pies.Reducer.commands.Reducer;

public class ReducerAPI {
    // Player bot
    public void setPlayerBotSkin(Player player, String skin) {
        PlayerBot.setSkin(player, skin);
    }
    public void setPlayerBotName(Player player, String name) {
        PlayerBot.setName(player, name);
    }
    public String getPlayerBotName(Player player) {
        if (!PlayerBot.NPCOwner.containsKey(player)) return null;
        NPC npc = PlayerBot.NPCOwner.get(player);
        return npc.getName();
    }
    public String getPlayerBotSkin(Player player) {
        if (!PlayerBot.NPCOwner.containsKey(player)) return null;
        NPC npc = PlayerBot.NPCOwner.get(player);
        SkinnableEntity skinnable = npc.getEntity() instanceof SkinnableEntity ? (SkinnableEntity) npc.getEntity() : null;
        return skinnable.getSkinName();
    }

    // Player inventory options
    public void setPlayerStick(Player player, Material m) {
        InvItems.Stick.put(player, m);
    }
    public void setPlayerBlock(Player player, Material m, Byte type) {
        InvItems.Block.put(player, m);
        InvItems.BlockData.put(player, type);
    }
    public ItemStack getPlayerBlock(Player player) {
        return new ItemStack(InvItems.getBlockMaterial(player), 64, InvItems.getBlockData(player));
    }
    public ItemStack getPlayerStick(Player player) {
        return new ItemStack(InvItems.getStickMaterial(player), 1, (byte)0);
    }
    public void UpdateHotbar(Player player) {
        Reducer.setItems(player, player.getInventory());
    }

    // Player knockback

    public double getHKB(Player player) { return PlayerSettings.getHKB(player); }
    public double getVKB(Player player) { return PlayerSettings.getVKB(player); }
    public double getReachMultiplier(Player player) { return PlayerSettings.getReach(player); }
    public double getReachInBlocks(Player player) { return PlayerSettings.getRealReach(player); }

    public void setHKB(Player player, double value) {
        PlayerSettings.HKB.put(player, value);
    }
    public void setVKB(Player player, double value) {
        PlayerSettings.HKB.put(player, value);
    }
    public void setReachMultiplier(Player player, double value) {
        PlayerSettings.Reach.put(player, value);
    }

    // Scoreboard stuff

    public void setPlayerScoreboard(Player player) {
        ScoreboardMang.ShowScoreboard(player);
    }
    public String parseScoreboardLine(Player player, String line) {
        return ScoreboardMang.parseLine(line, player);
    }

    // Arena

    public boolean isPlaying(Player player) {
        return Arena.playerInGame(player);
    }
    public String getPlayerArena(Player player) {
        if (isPlaying(player)) return Arena.getPlayerArena(player);
        return null;
    }
    public boolean mapExists(String arena) {
        return Arena.checkArenaExist(arena);
    }

    public void joinPlayerToArena(Player player, String arena) {
        Arena.joinArena(player, arena);
    }
    public void joinNextArena(Player player) {
        Arena.joinNextArena(player);
    }
    public void leave(Player player) {
        if (isPlaying(player)) Arena.leaveArena(player);
    }

    public void setMapSpawn(String arena, Location l) {
        Arena.setArenaSpawn(arena, l);
    }
    public Location getMapSpawn(String arena) {
        return Arena.getArenaSpawn(arena);
    }
    public void deleteMap(String arena) {
        Arena.delete(arena);
    }
    public void resetPlayer(Player player) {
        if (!isPlaying(player)) return;
        ArenaListener.resetPlayer(player, getPlayerArena(player), "api request");
    }
    public void resetPlacedBlocks(Player player, boolean animate) {
        if (!isPlaying(player)) return;
        if (animate) Arena.ResetBlocksAnimate(player);
        else Arena.ResetBlocks(player);
    }
}
