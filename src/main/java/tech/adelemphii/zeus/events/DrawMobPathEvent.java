package tech.adelemphii.zeus.events;

import com.destroystokyo.paper.entity.Pathfinder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.RayTraceResult;
import tech.adelemphii.zeus.Zeus;
import tech.adelemphii.zeus.utility.MobUtility;

import java.util.*;

public class DrawMobPathEvent implements Listener {

    private final Zeus plugin;

    Map<UUID, LivingEntity> selectedMobs = new HashMap<>();

    public DrawMobPathEvent(Zeus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
        if(event.getHand() == EquipmentSlot.OFF_HAND) return;
        if(!event.getPlayer().isSneaking()) return;

        Player player = event.getPlayer();

        Entity interactedEntity = event.getRightClicked();

        if(!(interactedEntity instanceof Mob)) return;
        Mob livingEntity = (Mob) interactedEntity;

        selectedMobs.put(player.getUniqueId(), livingEntity);

        List<Location> locations = plugin.getPlayerPointsManager().getClickPoints()
                .get(player.getUniqueId());

        MobUtility.moveMob(player, livingEntity, locations);
    }
}
