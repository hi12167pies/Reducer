package pies.Reducer.api;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.skin.SkinnableEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerBot {

    public static HashMap<Player, NPC> NPCOwner = new HashMap<>();

    public static void setName(Player player, String name) {
        if (!NPCOwner.containsKey(player)) {
            player.sendMessage("§cPlease hit a NPC to claim it as yours.");
            return;
        }
        if (name.length() < 3 || name.length() > 16) {
            player.sendMessage("§cYour name must be between 3 and 16 charters.");
            return;
        }
        if (Filter.checkProfanity(name)) {
            player.sendMessage("§cYour reducer name must be a family friendly name.");
            return;
        }

        NPC npc = NPCOwner.get(player);
        npc.setName(name);
    }

    public static void setSkin(Player player, String name) {
        if (!NPCOwner.containsKey(player)) {
            player.sendMessage("§cPlease hit a NPC to claim it as yours.");
            return;
        }

        if (name.length() < 3 || name.length() > 16) {
            player.sendMessage("§cYour skin must be between 3 and 16 charters.");
            return;
        }

        NPC npc = NPCOwner.get(player);

        npc.data().setPersistent(NPC.PLAYER_SKIN_UUID_METADATA, name);
        SkinnableEntity skinnable = npc.getEntity() instanceof SkinnableEntity ? (SkinnableEntity) npc.getEntity() : null;

        skinnable.setSkinName(name);
    }

}
