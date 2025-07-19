package me.pincer.paper.listener;

import me.pincer.lib.adapter.PlatformAdapter;
import me.pincer.lib.event.PluginMessageReceiveEvent;
import me.pincer.lib.messaging.proxy.ListenerDispatcher;
import me.pincer.lib.messaging.proxy.ProxyMessageListenerHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PaperProxyMessageListener implements ProxyMessageListenerHandler, PluginMessageListener {
    private final JavaPlugin plugin;
    private final PlatformAdapter platformAdapter;

    public PaperProxyMessageListener(JavaPlugin plugin, PlatformAdapter platformAdapter) {
        this.plugin = plugin;
        this.platformAdapter = platformAdapter;
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", this);
    }

    @Override
    public void registerListener(Object listener) {
        ListenerDispatcher.registerListeners(listener);
    }

    @Override
    public void unregisterListener(Object listener) {
        // Currently no unregister method in ListenerDispatcher
    }

    @Override
    public void onPluginMessageReceived(String channel, org.bukkit.entity.Player player, byte[] data) {
        try {
            String subchannel = parseSubchannel(data);
            PluginMessageReceiveEvent event = new PluginMessageReceiveEvent(
                    platformAdapter.convertPlayer(player),
                    data,
                    subchannel
            );
            ListenerDispatcher.dispatchMessage(subchannel, event);
        } catch (Exception e) {
            plugin.getLogger().severe("Error handling proxy message: " + e.getMessage());
        }
    }

    private String parseSubchannel(byte[] data) {
        if (data.length < 2) return "";
        int length = data[0] & 0xFF;
        return new String(data, 1, Math.min(length, data.length - 1)).trim();
    }

    public void unregister() {
        plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, "BungeeCord", this);
    }
}