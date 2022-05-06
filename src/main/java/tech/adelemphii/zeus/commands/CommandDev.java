package tech.adelemphii.zeus.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import tech.adelemphii.zeus.Zeus;

@CommandAlias("dev|developer")
public class CommandDev extends BaseCommand {

    private final Zeus plugin;
    public CommandDev(Zeus plugin) {
        this.plugin = plugin;
    }

    @Subcommand("clear")
    @Description("Clear the locations")
    public void onClear(Player player) {
        plugin.getPlayerPointsManager().removePlayerPoints(player.getUniqueId());
        player.sendMessage(Component.text("Done!"));
    }

    @Subcommand("point")
    public void onPoint(Player player) {
        if(plugin.getPlayerPointsManager().removeActivePlayer(player.getUniqueId())) {
            player.sendMessage(Component.text("disabled points"));
        } else {
            player.sendMessage(Component.text("enabled points"));
            plugin.getPlayerPointsManager().addActivePlayer(player.getUniqueId());
        }
    }
}
