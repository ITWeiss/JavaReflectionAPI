package ioc;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IoCContainer {
    private final Map<Class<?>, Object> singletons = new HashMap<>();

    private final Set<Class<?>> prototypes = new HashSet<>();

    public IoCContainer() {
        scanComponents();
        injectDependencies();
    }

    private void scanComponents() {
        try {
            for (Class<?> clazz : new Class[]{Engine.class, Car.class}) {

                if (clazz.isAnnotationPresent(Component.class)) {
                    if (clazz.isAnnotationPresent(Scope.class) &&
                            clazz.getAnnotation(Scope.class).value().equals("singleton")) {
                        singletons.put(clazz, clazz.getDeclaredConstructor().newInstance());
                    }
                    if (clazz.isAnnotationPresent(Scope.class) &&
                            clazz.getAnnotation(Scope.class).value().equals("prototype")) {
                        prototypes.add(clazz);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating components", e);
        }
    }


    private void injectDependencies() {
        singletons.values().forEach(instance -> {
            for (Field field : instance.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Inject.class)) {
                    try {
                        field.setAccessible(true);
                        field.set(instance, singletons.get(field.getType()));
                    } catch (Exception e) {
                        throw new RuntimeException("Error injecting dependencies", e);
                    }
                }
                if (field.isAnnotationPresent(Value.class)) {
                    try {
                        field.setAccessible(true);
                        Value valueAnnotation = field.getAnnotation(Value.class);
                        String value = valueAnnotation.value();
                        if (field.getType() == int.class) {
                            field.set(instance, Integer.parseInt(value));
                        } else if (field.getType() == String.class) {
                            field.set(instance, value);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("Error injecting dependencies", e);
                    }
                }
            }
        });
    }


    public <T> T getInstance(Class<T> clazz) {
        if (prototypes.contains(clazz)) {
            try {
                return clazz.cast(clazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return clazz.cast(singletons.get(clazz));
    }
}