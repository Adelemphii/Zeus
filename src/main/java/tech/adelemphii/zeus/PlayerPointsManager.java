package tech.adelemphii.zeus;

import org.bukkit.Location;
import tech.adelemphii.zeus.objects.ZeusMob;

import java.util.*;

public class PlayerPointsManager {

    private List<UUID> activePlayers = new ArrayList<>();
    private Map<UUID, List<Location>> clickPoints = new HashMap<>();

    private Map<UUID, ZeusMob> selectedMobs = new HashMap<>();

    public Map<UUID, List<Location>> getClickPoints() {
        return clickPoints;
    }

    public void setClickPoints(Map<UUID, List<Location>> clickPoints) {
        this.clickPoints = clickPoints;
    }

    public void addClickPoints(UUID uuid, List<Location> locations) {
        clickPoints.put(uuid, locations);
    }

    public void removePlayerPoints(UUID uuid) {
        this.clickPoints.remove(uuid);
    }

    public List<UUID> getActivePlayers() {
        return activePlayers;
    }

    public void setActivePlayers(List<UUID> activePlayers) {
        this.activePlayers = activePlayers;
    }

    public void addActivePlayer(UUID uuid) {
        this.activePlayers.add(uuid);
    }

    public boolean removeActivePlayer(UUID uuid) {
        return this.activePlayers.remove(uuid);
    }

    public Map<UUID, ZeusMob> getSelectedMobs() {
        return selectedMobs;
    }

    public void setSelectedMobs(Map<UUID, ZeusMob> selectedMobs) {
        this.selectedMobs = selectedMobs;
    }

    public void addSelectedMob(UUID uuid, ZeusMob mob) {
        selectedMobs.put(uuid, mob);
    }

    public void removeSelectedMob(UUID uuid) {
        selectedMobs.remove(uuid);
    }

    public UUID removeSelectedMob(ZeusMob mob) {
        UUID uuid = null;

        for(UUID u : selectedMobs.keySet()) {
            if(selectedMobs.get(u).getMob().getUniqueId().equals(mob.getMob().getUniqueId())) {
                uuid = u;
                break;
            }
        }
        if(uuid == null) return null;

        selectedMobs.remove(uuid);
        return uuid;
    }
}
