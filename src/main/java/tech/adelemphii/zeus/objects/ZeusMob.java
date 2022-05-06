package tech.adelemphii.zeus.objects;

import com.destroystokyo.paper.entity.Pathfinder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Mob;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import tech.adelemphii.zeus.Zeus;
import tech.adelemphii.zeus.utility.ParticleUtility;

import java.util.ArrayList;
import java.util.List;

public class ZeusMob {

    private Mob mob;
    private boolean selected;

    private final BukkitTask internalClock;

    private List<Location> waypoints = new ArrayList<>();

    public ZeusMob(Mob mob, boolean selected) {
        this.mob = mob;
        this.selected = selected;

        internalClock = startInternalClock(this);
    }

    public void addWayPoints(List<Location> waypoints) {
        if(waypoints == null) return;
        // TODO: A bit buggy if you select a new mob
        this.waypoints.addAll(waypoints);
    }

    public void addWayPoint(Location location) {
        waypoints.add(location);
    }

    public Mob getMob() {
        return mob;
    }

    public void setMob(Mob mob) {
        this.mob = mob;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        // TODO: make the glow only show up to the person(s) who have it selected
        if(selected) {
            this.mob.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 10000, 1));
        } else {
            this.mob.removePotionEffect(PotionEffectType.GLOWING);
        }
        this.selected = selected;
    }

    public List<Location> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Location> waypoints) {
        this.waypoints = waypoints;
    }

    public boolean startPathfinding() {
        if(internalClock.isCancelled()) {
            startInternalClock(this);
            return true;
        }
        return false;
    }

    public boolean stopPathfinding() {
        if(!internalClock.isCancelled()) {
            internalClock.cancel();
            return true;
        }
        return false;
    }

    private BukkitTask startInternalClock(ZeusMob zeusMob) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if(mob.isDead()) {
                    this.cancel();
                    Zeus.getInstance().getPlayerPointsManager().removeSelectedMob(zeusMob);
                    return;
                }

                if(waypoints == null || waypoints.size() <= 0) {
                    return;
                }

                if(mob.getLocation().distanceSquared(waypoints.get(0)) < 2) {
                    mob.getPathfinder().stopPathfinding();
                    waypoints.remove(0);

                    if(waypoints.size() <= 0) {
                        return;
                    }
                }

                if(!mob.getPathfinder().hasPath()) {
                    Pathfinder.PathResult pathResult = mob.getPathfinder().findPath(waypoints.get(0));

                    if(pathResult == null) {
                        Bukkit.broadcast(Component.text("Pathresult null"));
                        this.cancel();
                        return;
                    }

                    mob.getPathfinder().moveTo(pathResult);
                } else {
                    ParticleUtility.spawnParticleAlongLine(mob.getLocation(), waypoints.get(0),
                            Particle.FLAME,
                            30, 20, 0, 0, 0, 0, null,
                            true, null);
                }
            }
        }.runTaskTimer(Zeus.getInstance(), 0, 40);
    }
}
