package pies.Reducer.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import pies.Reducer.arena.Arena;

import static pies.Reducer.Config.MainClass;

public class ScoreboardMang {
    public static void ShowScoreboard(Player player) {

        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("reducersidebar", "side");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        obj.setDisplayName(parseLine(MainClass.getConfig().getString("scoreboard.title"), player));

        for (String st : MainClass.getConfig().getConfigurationSection("scoreboard.lines").getKeys(false)) {
            obj.getScore(parseLine(MainClass.getConfig().getString("scoreboard.lines."+st), player)).setScore(Integer.valueOf(st));
        }

        player.setScoreboard(board);
    }

    public static String parseLine(String s, Player player) {
        String arena = Arena.getPlayerArena(player);
        Integer blocks = Arena.Block.get(player).toArray().length;
        blocks += 1;
        return s.replace("<arena>", arena)
                .replace("<blocks>", (blocks).toString())
                .replace("&", "§");
    }
}
