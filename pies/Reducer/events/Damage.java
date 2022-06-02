package pies.Reducer.events;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.npc.CitizensNPC;
import net.citizensnpcs.npc.CitizensNPCRegistry;
import net.citizensnpcs.util.PlayerAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;
import pies.Reducer.Config;
import pies.Reducer.Main;
import pies.Reducer.api.Knockback;
import pies.Reducer.api.PlayerBot;

import java.util.ArrayList;
import java.util.HashMap;

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
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        if (!(e.getEntity() instanceof Player)) return;
        if (MainClass.getConfig().getBoolean("world.enabled")) {
            if (e.getEntity().getLocation().getWorld().getName().equals(MainClass.getConfig().getString("world.name")))
                return;
        }
        Player attacker = (Player) e.getDamager();
        Player victim = (Player) e.getEntity();

        if (AllowReduce.contains(attacker)) {
            e.setCancelled(true);
            return;
        }
        if (!(victim.hasMetadata("NPC"))) return;
        e.setCancelled(true);
        victim.damage(0);
        victim.setHealth(20);

        // Usage later for NPC.setName(123)
        if (!NPCOwner.containsKey(victim))
            NPCOwner.put(attacker, CitizensAPI.getNPCRegistry().getNPC(victim));

        if (Knockback.Bouncy.contains(attacker))
            victim.setVelocity(new Vector(0, Knockback.getVKB(attacker), 0));

        if (!Reduced.contains(attacker)) {
            Reduced.add(attacker);
            startHit(attacker, victim);
        }

        AllowReduce.add(attacker);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() { @Override public void run() {
            AllowReduce.remove(attacker);
        }}, 10);
    }

    public static void startHit(Player attacker, Player victim) {
        if (!Reduced.contains(attacker)) return;
        if (attacker.getLocation().distance(victim.getLocation()) >= Knockback.getRealReach(attacker)) {
            Reduced.remove(attacker);
            return;
        }

        Vector vect = victim.getLocation().getDirection();

        vect = vect.multiply(Knockback.getHKB(attacker));
        vect.setY(Knockback.getVKB(attacker));

        PlayerAnimation.ARM_SWING.play(victim);

        attacker.setVelocity(vect);
        attacker.damage(0);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() { @Override public void run() {
            startHit(attacker, victim);
        }}, 10);
    }
}
