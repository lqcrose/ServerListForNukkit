package net.lacrose;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.PluginBase;

public class ServerListForNukkit extends PluginBase implements Listener {

    public final ServerListAPI api = new ServerListAPI();

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getScheduler().scheduleRepeatingTask(new ServerListTask(this), 20);
        this.getLogger().info(this.api.login());
    }

    @Override
    public void onDisable() {
        this.getLogger().info(this.api.logout());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (command.getName().trim()) {
            case "event":
                if (args.length >= 1) {
                    this.api.event(args[0]);
                }
                return true;
            default:
                return false;
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.api.updatePlayers("join");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.api.updatePlayers("quit");
    }
}