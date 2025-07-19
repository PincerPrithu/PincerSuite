package me.pincer.paper.listener;

import me.pincer.lib.adapter.PlatformAdapter;
import me.pincer.lib.event.PluginMessageReceiveEvent;
import me.pincer.lib.messaging.proxy.ProxyMessageDispatcher;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.HashMap;
import java.util.Map;

public class PaperPluginMessageListener implements PluginMessageListener {
    private final JavaPlugin plugin;
    private final PlatformAdapter platformAdapter;
    private final Map<Object, ProxyMessageDispatcher> dispatchers = new HashMap<>();
    public PaperPluginMessageListener(JavaPlugin plugin, PlatformAdapter platformAdapter) {
        this.plugin = plugin;
        this.platformAdapter = platformAdapter;
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", this);
    }

    @Override
    public void onPluginMessageReceived(String channel, org.bukkit.entity.Player player, byte[] data) {
        try {
            String subchannel = readSubchannel(data);
            PluginMessageReceiveEvent event = new PluginMessageReceiveEvent(
                    platformAdapter.convertPlayer(player),
                    data,
                    subchannel
            );
            dispatchers.values().forEach(d -> d.dispatch(subchannel, event));
        } catch (Exception e) {
            plugin.getLogger().severe("Error handling plugin message: " + e.getMessage());
        }
    }

    private String readSubchannel(byte[] data) {
        if (data.length < 2) return "";
        int length = data[0] & 0xFF;
        return new String(data, 1, Math.min(length, data.length - 1)).trim();
    }

    public void unregister() {
        plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, "BungeeCord", this);
    }
}