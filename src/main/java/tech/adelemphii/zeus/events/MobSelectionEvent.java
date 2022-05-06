package tech.adelemphii.zeus.events;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import tech.adelemphii.zeus.Zeus;
import tech.adelemphii.zeus.objects.ZeusMob;
import tech.adelemphii.zeus.utility.MobUtility;

import java.util.*;

public class MobSelectionEvent implements Listener {

    private final Zeus plugin;

    public MobSelectionEvent(Zeus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
        if(event.getHand() == EquipmentSlot.OFF_HAND) return;
        if(!event.getPlayer().isSneaking()) return;

        Player player = event.getPlayer();

        Entity interactedEntity = event.getRightClicked();

        if(!(interactedEntity instanceof Mob)) return;

        ZeusMob zeusMob = new ZeusMob((Mob) interactedEntity, true);

        if(plugin.getPlayerPointsManager().getSelectedMobs().containsKey(player.getUniqueId())) {
            ZeusMob selectedMob = plugin.getPlayerPointsManager().getSelectedMobs().get(player.getUniqueId());

            if(selectedMob.getMob().getUniqueId().toString().equals(zeusMob.getMob().getUniqueId().toString())) {
                selectedMob.setSelected(false);
                player.sendMessage(Component.text("Mob no longer selected"));
                plugin.getPlayerPointsManager().removeSelectedMob(player.getUniqueId());
                return;
            } else {
                selectedMob.setSelected(false);
                player.sendMessage(Component.text("Mob changed"));
                plugin.getPlayerPointsManager().removeSelectedMob(player.getUniqueId());
            }
        }

        player.sendMessage(Component.text("Mob selected"));
        plugin.getPlayerPointsManager().addSelectedMob(player.getUniqueId(), zeusMob);
        zeusMob.setSelected(true);

        List<Location> locations = plugin.getPlayerPointsManager().getClickPoints()
                .get(player.getUniqueId());

        zeusMob.addWayPoints(locations);

        MobUtility.moveMob(player, zeusMob, locations);
    }
}
