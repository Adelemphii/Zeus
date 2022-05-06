package tech.adelemphii.zeus.events;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitScheduler;
import tech.adelemphii.zeus.Zeus;
import tech.adelemphii.zeus.objects.ZeusMob;
import tech.adelemphii.zeus.utility.ParticleUtility;

import java.util.*;

public class RaycastLineEvent implements Listener {

    private Zeus plugin;

    Map<UUID, BukkitScheduler> schedulerMap = new HashMap<>();

    public RaycastLineEvent(Zeus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        if(!plugin.getPlayerPointsManager().getActivePlayers().contains(event.getPlayer().getUniqueId())) return;
        if(event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_AIR) return;

        Player player = event.getPlayer();
        Location interactionPoint = player.getWorld().rayTraceBlocks(player.getEyeLocation(), player.getEyeLocation().getDirection(),
                30).getHitPosition().toLocation(player.getWorld());

        List<Location> locations = plugin.getPlayerPointsManager().getClickPoints()
                .getOrDefault(player.getUniqueId(), new ArrayList<>());

        Location previous = locations.size() > 0 ? locations.get(locations.size() - 1) : null;

        if(plugin.getPlayerPointsManager().getSelectedMobs().containsKey(player.getUniqueId())) {
            ZeusMob zeusMob = plugin.getPlayerPointsManager().getSelectedMobs().get(player.getUniqueId());

            zeusMob.addWayPoint(interactionPoint);

            locations.add(interactionPoint);
            if(previous == null) {
                player.sendMessage(Component.text("Added point to selected mob"));
            } else {
                player.sendMessage(Component.text("Added point to selected mob with previous"));
                startRunnable(player.getUniqueId());
            }
            plugin.getPlayerPointsManager().addClickPoints(player.getUniqueId(), locations);
            return;
        }

        locations.add(interactionPoint);
        if(previous == null) {
            player.sendMessage(Component.text("Added point"));
        } else {
            player.sendMessage(Component.text("Added point with previous"));
            startRunnable(player.getUniqueId());
        }
        plugin.getPlayerPointsManager().addClickPoints(player.getUniqueId(), locations);
    }

    private void startRunnable(UUID uuid) {
        if(schedulerMap.containsKey(uuid)) return;

        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskTimer(plugin, () -> {
            List<Location> locations = plugin.getPlayerPointsManager().getClickPoints().get(uuid);

            if(locations == null) {
                return;
            }

            Location p = null;

            for(Location location : locations) {
                if(p == null) {
                    p = location;
                    continue;
                }

                ParticleUtility.spawnParticleAlongLine(p, location,
                        Particle.FIREWORKS_SPARK,
                        10, 10, 0, 0, 0, 0, null,
                        true, null);
                p = location;
            }
        }, 0, 10);

        schedulerMap.put(uuid, scheduler);
    }
}
