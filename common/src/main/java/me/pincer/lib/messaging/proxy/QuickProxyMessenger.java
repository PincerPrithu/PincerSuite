package me.pincer.lib.messaging.proxy;

public class QuickProxyMessenger {
    private static ProxyMessageListenerHandler handler;

    public static void init(ProxyMessageListenerHandler implementation) {
        handler = implementation;
    }

    public static void registerListener(Object listener) {
        if (handler != null) {
            handler.registerListener(listener);
        }
    }
}
