package tech.adelemphii.zeus;

import org.bukkit.Location;

import java.util.*;

public class PlayerPointsManager {

    private List<UUID> activePlayers = new ArrayList<>();
    private Map<UUID, List<Location>> clickPoints = new HashMap<>();

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
}
