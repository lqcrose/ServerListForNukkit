package net.lacrose;

import cn.nukkit.scheduler.PluginTask;

public class ServerListTask extends PluginTask<ServerListForNukkit> {

    private int time = 0;

    public ServerListTask(ServerListForNukkit owner) {
        super(owner);
    }

    @Override
    public void onRun(int currentTick) {
        if ((int) (System.currentTimeMillis() / 1000L) - this.time > 60) {
            this.getOwner().api.updateTime();
            this.time = (int) (System.currentTimeMillis() / 1000L);
        }
    }
}
