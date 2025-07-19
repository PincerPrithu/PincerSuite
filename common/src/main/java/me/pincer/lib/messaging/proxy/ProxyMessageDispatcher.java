package me.pincer.lib.messaging.proxy;

import me.pincer.lib.annotation.ProxyMessageListener;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ProxyMessageDispatcher {
    private final Map<String, Method> handlers = new HashMap<>();
    private final Object listenerInstance;

    public ProxyMessageDispatcher(Object listenerInstance) {
        this.listenerInstance = listenerInstance;
        scanAnnotations();
    }

    private void scanAnnotations() {
        for (Method method : listenerInstance.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(ProxyMessageListener.class)) {
                ProxyMessageListener annotation = method.getAnnotation(ProxyMessageListener.class);
                method.setAccessible(true);
                handlers.put(annotation.subchannel(), method);
            }
        }
    }

    public void dispatch(String subchannel, Object... args) {
        Method method = handlers.get(subchannel);
        if (method != null) {
            try {
                method.invoke(listenerInstance, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
