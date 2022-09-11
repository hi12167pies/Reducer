package pies.Reducer.events;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.util.PlayerAnimation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;
import pies.Reducer.Config;
import pies.Reducer.Main;
import pies.Reducer.api.PlayerSettings;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.sqrt;
import static pies.Reducer.Config.MainClass;
import static pies.Reducer.api.PlayerBot.NPCOwner;

public class Damage implements Listener {
    public static ArrayList<Player> AllowReduce = new ArrayList<>();
    public static ArrayList<Player> Reduced = new ArrayList<>();

    @EventHandler
    public void d(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL && Config.DisableFallDamage == true) {
            e.setCancelled(true);
            return;
        }
    }

    public static boolean inworld(Entity e) {
        if (!MainClass.getConfig().getBoolean("world.enabled")) return true;
        return  e.getLocation().getWorld().getName().equals(MainClass.getConfig().getString("world.name"));
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        if (!(e.getEntity() instanceof Player)) return;
        if (!inworld(e.getDamager()))
            return;

        Player attacker = (Player) e.getDamager();
        Player victim = (Player) e.getEntity();

        if (!(victim.hasMetadata("NPC"))) return;

        if (AllowReduce.contains(attacker)) {
            e.setCancelled(true);
            return;
        }
        e.setCancelled(true);
        victim.damage(0);
        victim.setHealth(20);

        if (PlayerSettings.Bouncy.contains(attacker))
            victim.setVelocity(new Vector(0, PlayerSettings.getVKB(attacker), 0));

        if (!Reduced.contains(attacker)) {
            Reduced.add(attacker);
            startHit(attacker, victim);
        }

        // Usage later for NPC.setName(123)
        NPCOwner.put(attacker, CitizensAPI.getNPCRegistry().getNPC(victim));

        AllowReduce.add(attacker);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() { @Override public void run() {
            AllowReduce.remove(attacker);
        }}, PlayerSettings.getTickDelay(attacker));
    }
    public static Vector getVector(Player p, Player e){
        double x = p.getLocation().getX() - e.getLocation().getX();
        double y = 0.0;
        double z = p.getLocation().getZ() - e.getLocation().getZ();
        double dist = sqrt(x*x + y*y + z*z);
        double mod = (PlayerSettings.getHKB(p)/dist);
        return new Vector(mod*x, PlayerSettings.getVKB(p), mod*z);
    }

    public static void startHit(Player attacker, Player victim) {
        if (!Reduced.contains(attacker)) return;
        double distance = attacker.getLocation().distance(victim.getLocation());
        if (distance >= PlayerSettings.getRealReach(attacker)) {
            Reduced.remove(attacker);
            return;
        }
        Vector vect = getVector(attacker, victim);

        PlayerAnimation.ARM_SWING.play(victim);

        attacker.setVelocity(vect);
        attacker.setNoDamageTicks(0);
        attacker.damage(0);

        int a = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() { @Override public void run() {
            // this will find the bot your hit last, if you own a npc it will get that entity or the bot u hit
            Player vict = NPCOwner.containsKey(attacker) ? (Player) NPCOwner.get(attacker).getEntity() : victim;
            startHit(attacker, vict);
        }}, PlayerSettings.getTickDelay(attacker));
        taskIDS.put(attacker, a);
    }
    public static HashMap<Player, Integer> taskIDS = new HashMap<>();
}
