package tech.adelemphii.zeus.utility;

import com.destroystokyo.paper.entity.Pathfinder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import tech.adelemphii.zeus.Zeus;

import java.util.List;

public class MobUtility {

    public static void moveMob(Player player, Mob mob, List<Location> locations) {
        if(locations == null || locations.size() <= 0) {
            Zeus.getInstance().getPlayerPointsManager().addActivePlayer(player.getUniqueId());
            player.sendMessage(Component.text("Mark some points first with left click"));
        } else {
            delayedPath(mob, locations, 40);
            player.sendMessage(Component.text("Setting path."));
        }
    }

    private static void delayedPath(Mob mob, List<Location> locations, long delay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(locations == null || locations.size() <= 0) {
                    Bukkit.broadcast(Component.text("Locations null/0"));
                    this.cancel();
                    return;
                }

                if(mob.getLocation().distanceSquared(locations.get(0)) < 1) {
                    mob.getPathfinder().stopPathfinding();
                    locations.remove(0);

                    if(locations.size() <= 0) {
                        Bukkit.broadcast(Component.text("Locations null/0"));
                        this.cancel();
                        return;
                    }
                }

                if(!mob.getPathfinder().hasPath()) {
                    Pathfinder.PathResult pathResult = mob.getPathfinder().findPath(locations.get(0));

                    if(pathResult == null) {
                        Bukkit.broadcast(Component.text("Pathresult null"));
                        this.cancel();
                        return;
                    }

                    mob.getPathfinder().moveTo(pathResult);
                }
            }
        }.runTaskTimer(Zeus.getInstance(), 0, delay);
    }
}
