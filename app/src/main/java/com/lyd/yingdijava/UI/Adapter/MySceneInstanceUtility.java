package com.lyd.yingdijava.UI.Adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.SimpleArrayMap;

import com.bytedance.scene.Scene;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

public class MySceneInstanceUtility {
    private static final SimpleArrayMap<String, Class<?>> sClassMap =
            new SimpleArrayMap<String, Class<?>>();

    @NonNull
    public static Scene getInstanceFromClassName(Context context, String clazzName, Bundle arguments) {
        Class<?> clazz = sClassMap.get(clazzName);
        try {
            if (clazz == null) {
                clazz = context.getClassLoader().loadClass(clazzName);
                sClassMap.put(clazzName, clazz);
            }
            return getInstanceFromClass(clazz, arguments);
        } catch (ClassNotFoundException e) {
            throw new InstantiationException("Unable to instantiate scene " + clazzName
                    + ": make sure class name exists, is public, and has an"
                    + " empty constructor that is public", e);
        }
    }

    @NonNull
    public static Scene getInstanceFromClass(@NonNull Class<?> clazz, @Nullable Bundle arguments) {
        try {
            Scene scene = (Scene) clazz.getConstructor().newInstance();
            if (arguments != null) {
                arguments.setClassLoader(scene.getClass().getClassLoader());
                scene.setArguments(arguments);
            }
            return scene;
        } catch (java.lang.InstantiationException e) {
            throw new InstantiationException("Unable to instantiate scene " + clazz
                    + ": make sure class name exists, is public, and has an"
                    + " empty constructor that is public", e);
        } catch (IllegalAccessException e) {
            throw new InstantiationException("Unable to instantiate scene " + clazz
                    + ": make sure class name exists, is public, and has an"
                    + " empty constructor that is public", e);
        } catch (NoSuchMethodException e) {
            throw new InstantiationException("Unable to instantiate scene " + clazz
                    + ": could not find Scene constructor", e);
        } catch (InvocationTargetException e) {
            throw new InstantiationException("Unable to instantiate scene " + clazz
                    + ": calling Scene constructor caused an exception", e);
        }
    }

    public static boolean isSupportRestore(Scene scene) {
        Class<? extends Scene> clazz = scene.getClass();
        if (clazz.isAnonymousClass() || clazz.isLocalClass()) {
            return false;
        }

        final int modifiers = clazz.getModifiers();
        if (!Modifier.isPublic(modifiers)) {
            return false;
        }
        if (clazz.isMemberClass() && !Modifier.isStatic(modifiers)) {
            return false;
        }

        for (Constructor<?> constructor : clazz.getConstructors()) {
            Class<?>[] types = constructor.getParameterTypes();
            if (types.length > 0) {
                return false;
            }
        }
        return true;
    }

    static public class InstantiationException extends RuntimeException {
        InstantiationException(String msg, Exception cause) {
            super(msg, cause);
        }
    }
}