package me.pincer.lib.event;

import java.util.function.Consumer;

public class PluginMessageEventDispatcher {

    private static Consumer<PluginMessageReceiveEvent> listener;

    public static void registerListener(Consumer<PluginMessageReceiveEvent> handler) {
        listener = handler;
    }

    public static void dispatch(PluginMessageReceiveEvent event) {
        if (listener != null) {
            listener.accept(event);
        }
    }
}
