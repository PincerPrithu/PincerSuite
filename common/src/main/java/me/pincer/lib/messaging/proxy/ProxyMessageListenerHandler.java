package me.pincer.lib.messaging.proxy;

public interface ProxyMessageListenerHandler {
    void registerListener(Object listener);
    void unregisterListener(Object listener);
}
