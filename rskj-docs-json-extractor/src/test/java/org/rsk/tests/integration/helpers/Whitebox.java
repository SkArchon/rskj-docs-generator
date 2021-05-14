package org.rsk.tests.integration.helpers; /**
 * This was taken from Mockito an older version since it was deprecated however
 * we require this in our situation
 */
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

public class Whitebox {

    public static void setInternalState(Object target, String field, Object value) {
        Class<?> c = target.getClass();
        try {
            Field f = getFieldFromHierarchy(c, field);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Unable to set internal state on a private field. Please report to mockito mailing list.", e);
        }
    }

    private static Field getFieldFromHierarchy(Class<?> clazz, String field) {
        Field f = getField(clazz, field);
        while (f == null && clazz != Object.class) {
            clazz = clazz.getSuperclass();
            f = getField(clazz, field);
        }
        if (f == null) {
            throw new RuntimeException(
                    "You want me to get this field: '" + field +
                    "' on this class: '" + clazz.getSimpleName() +
                    "' but this field is not declared within the hierarchy of this class!");
        }
        return f;
    }

    private static Field getField(Class<?> clazz, String field) {
        try {
            return clazz.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    public static <T> T constructClassFromPrivateNoArgsConstructor(Class<T> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            T instance = (T) constructor.newInstance();
            return instance;
        }
        catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            return null;
        }
    }

    public static void setFinalStatic(Object target, String fieldName, Object newValue) {
        try {
            Class<?> c = target.getClass();
            Field field = getFieldFromHierarchy(c, fieldName);
            field.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.set(null, newValue);
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
