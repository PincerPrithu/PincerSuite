package me.pincer.paper;

import me.pincer.lib.annotation.ProxyMessageListener;
import me.pincer.lib.database.QuickDatabase;
import me.pincer.lib.messaging.redis.QuickRedisMessenger;
import me.pincer.lib.util.PackageScanner;
import me.pincer.paper.adapters.PaperPlatformAdapter;
import me.pincer.paper.listener.PaperProxyMessageListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.List;

public final class PincerSuitePaper {
    private final JavaPlugin plugin;
    private final QuickDatabase database;
    private final QuickRedisMessenger redisMessenger;
    private final PaperProxyMessageListener messageListener;
    private final PaperPlatformAdapter platformAdapter;

    public PincerSuitePaper(JavaPlugin plugin) {
        this.plugin = plugin;
        this.platformAdapter = new PaperPlatformAdapter();
        this.database = new QuickDatabase();
        this.redisMessenger = new QuickRedisMessenger();
        this.messageListener = new PaperProxyMessageListener(plugin, platformAdapter);

        // Automatically scan and register listeners
        String packageName = plugin.getClass().getPackage().getName();
        registerListenersInPackage(packageName);

        plugin.getLogger().info("PincerSuite initialized for Paper.");
    }

    private void registerListenersInPackage(String packageName) {
        List<Class<?>> classes = PackageScanner.findClasses(packageName, plugin.getClass().getClassLoader());

        for (Class<?> clazz : classes) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(ProxyMessageListener.class)) {
                    try {
                        Object instance = clazz.getDeclaredConstructor().newInstance();
                        messageListener.registerListener(instance);
                        break; // Register instance only once
                    } catch (Exception e) {
                        plugin.getLogger().warning("Failed to register listener in class: " + clazz.getName());
                    }
                }
            }
        }
    }

    public void shutdown() {
        messageListener.unregister();
        plugin.getLogger().info("PincerSuite shutdown complete.");
    }
}