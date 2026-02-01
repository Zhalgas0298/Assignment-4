package utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {

    public static void inspect(Object obj) {
        Class<?> c = obj.getClass();

        System.out.println("=== REFLECTION INSPECT ===");
        System.out.println("Class: " + c.getName());

        System.out.println("Fields:");
        for (Field f : c.getDeclaredFields()) {
            System.out.println(" - " + f.getType().getSimpleName() + " " + f.getName());
        }

        System.out.println("Methods:");
        for (Method m : c.getDeclaredMethods()) {
            System.out.println(" - " + m.getName());
        }
        System.out.println("==========================");
    }
}
