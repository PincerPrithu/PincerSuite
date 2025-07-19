package me.pincer.lib.event;

import me.pincer.lib.model.Player;

public class PluginMessageReceiveEvent {
    private final Player player;
    private final byte[] data;
    private final String subchannel;

    public PluginMessageReceiveEvent(me.pincer.lib.model.Player player, byte[] data, String subchannel) {
        this.player = player;
        this.data = data;
        this.subchannel = subchannel;
    }

    // Add getters
    public me.pincer.lib.model.Player getPlayer() { return player; }
    public byte[] getData() { return data; }
    public String getSubchannel() { return subchannel; }
}