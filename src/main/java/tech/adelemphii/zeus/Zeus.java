package tech.adelemphii.zeus;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.plugin.java.JavaPlugin;
import tech.adelemphii.zeus.commands.CommandDev;
import tech.adelemphii.zeus.events.DrawMobPathEvent;
import tech.adelemphii.zeus.events.RaycastLineEvent;

public final class Zeus extends JavaPlugin {

    private PlayerPointsManager playerPointsManager;

    private static Zeus plugin;

    @Override
    public void onEnable() {
        PaperCommandManager manager = new PaperCommandManager(this);

        manager.registerCommand(new CommandDev(this));

        getServer().getPluginManager().registerEvents(new RaycastLineEvent(this), this);
        getServer().getPluginManager().registerEvents(new DrawMobPathEvent(this), this);

        playerPointsManager = new PlayerPointsManager();
        plugin = this;
    }

    @Override
    public void onDisable() {
    }

    public PlayerPointsManager getPlayerPointsManager() {
        return playerPointsManager;
    }

    public static Zeus getInstance() {
        return plugin;
    }
}
