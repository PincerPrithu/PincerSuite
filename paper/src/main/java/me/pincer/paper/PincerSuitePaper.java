package me.pincer.paper;

import me.pincer.lib.database.QuickDatabase;
import me.pincer.lib.messaging.redis.QuickRedisMessenger;
import me.pincer.paper.adapters.PaperPlatformAdapter;
import me.pincer.paper.listener.PaperPluginMessageListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class PincerSuitePaper {

    private final JavaPlugin plugin;
    private final QuickDatabase database;
    private final QuickRedisMessenger redisMessenger;
    private final PaperPluginMessageListener messageListener;
    private final PaperPlatformAdapter platformAdapter;

    public PincerSuitePaper(JavaPlugin plugin) {
        this.plugin = plugin;
        this.platformAdapter = new PaperPlatformAdapter();

        // Setup database (consider singleton or per-plugin instance)
        this.database = new QuickDatabase();

        // Setup Redis
        this.redisMessenger = new QuickRedisMessenger();

        // Setup Plugin Message Channel
        this.messageListener = new PaperPluginMessageListener(plugin, platformAdapter);

        plugin.getLogger().info("PincerSuite initialized for Paper.");
    }

    public void shutdown() {
        // Clean shutdown tasks
        messageListener.unregister();  // Needs to be implemented
        plugin.getLogger().info("PincerSuite shutdown complete.");
    }

}
