package me.pincer.lib.messaging.proxy;

import me.pincer.lib.annotation.ProxyMessageListener;
import me.pincer.lib.event.PluginMessageReceiveEvent;

import java.lang.reflect.Method;
import java.util.*;

public class ListenerDispatcher {

    private static final Map<String, List<Method>> listeners = new HashMap<>();
    private static final Map<Method, Object> listenerInstances = new HashMap<>();

    // Register an object that has @ProxyMessageListener methods
    public static void registerListeners(Object instance) {
        for (Method method : instance.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(ProxyMessageListener.class)) {
                ProxyMessageListener annotation = method.getAnnotation(ProxyMessageListener.class);
                String subchannel = annotation.subchannel().toUpperCase();

                method.setAccessible(true);

                listeners.computeIfAbsent(subchannel, key -> new ArrayList<>()).add(method);
                listenerInstances.put(method, instance);
            }
        }
    }

    // Call all matching methods when a message is received
    public static void dispatchMessage(String subchannel, PluginMessageReceiveEvent event) {
        String key = subchannel.toUpperCase();

        List<Method> methods = listeners.get(key);
        if (methods == null) return;

        for (Method method : methods) {
            Object instance = listenerInstances.get(method);
            try {
                method.invoke(instance, event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
