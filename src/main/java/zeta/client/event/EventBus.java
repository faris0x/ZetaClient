package zeta.client.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Subscribe {
    }

    private final Map<Class<?>, List<Listener>> listeners = new HashMap<>();

    public void register(Object object) {
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Subscribe.class) && method.getParameterCount() == 1) {
                Class<?> eventType = method.getParameterTypes()[0];
                method.setAccessible(true);
                listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
                    .add(new Listener(object, method));
            }
        }
    }

    public void unregister(Object object) {
        listeners.values().forEach(list -> list.removeIf(l -> l.instance == object));
    }

    public void post(Event event) {
        List<Listener> list = listeners.get(event.getClass());
        if (list != null) {
            for (Listener listener : list) {
                try {
                    listener.method.invoke(listener.instance, event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class Listener {
        final Object instance;
        final Method method;
        Listener(Object instance, Method method) {
            this.instance = instance;
            this.method = method;
        }
    }
}
