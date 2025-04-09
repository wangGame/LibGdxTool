//package com.kw.gdx.resource.introspector;
//
//import com.badlogic.gdx.files.FileHandle;
//import com.badlogic.gdx.utils.DelayedRemovalArray;
//import com.badlogic.gdx.utils.XmlReader;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Modifier;
//import java.util.Objects;
//
//public class Introspector {
//    public interface Listener{
//        void onModified();
//    }
//
//    private final Class mClass;
//    private final Object mReference;
//    private final Object mObject;
//    private final FileHandle mFileHandle;
//    private final DelayedRemovalArray<Listener> mListeners = new DelayedRemovalArray<>();
//
//    public Introspector(Object mObject,Object reference,FileHandle fileHandle){
//        this.mClass = mObject.getClass();
//        this.mObject = mObject;
//        this.mReference = reference;
//        this.mFileHandle = fileHandle;
//    }
//
//    public static Introspector fromInstance(Object instance,FileHandle fileHandle){
//        Object reference;
//        try {
//            reference = instance.getClass().getDeclaredConstructor().newInstance();
//        } catch (InstantiationException
//                |IllegalAccessException
//                |NoSuchMethodException
//                |InvocationTargetException e) {
//            e.printStackTrace();
//            throw new RuntimeException();
//        }
//        return new Introspector(instance,reference,fileHandle);
//    }
//
//    public void addListener(Listener listener) {
//        mListeners.add(listener);
//    }
//
//    public void load(){
//        if (!mFileHandle.exists()){
//            return;
//        }
//
//        XmlReader.Element root = FileUtils.parseXml(mFileHandle);
//        if (root == null) {
//            return;
//        }
//        for (XmlReader.Element keyElement : root.getChildrenByName("key")) {
//            String name = keyElement.getAttribute("name");
//            String type = keyElement.getAttribute("type");
//            String value = keyElement.getText();
//            Field field;
//            try {
//                field = mClass.getField(name);
//            } catch (NoSuchFieldException e) {
//                NLog.e("No field named '%s', skipping", name);
//                continue;
//            }
//            String fieldType = field.getType().toString();
//            if (!fieldType.equals(type)) {
//                NLog.e(
//                        "Field '%s' is of type '%s', but XML expected '%s', skipping",
//                        name, fieldType, type);
//                continue;
//            }
//            switch (type) {
//                case "int":
//                    set(name, Integer.valueOf(value));
//                    break;
//                case "boolean":
//                    set(name, Boolean.valueOf(value));
//                    break;
//                case "float":
//                    set(name, Float.valueOf(value));
//                    break;
//            }
//        }
//    }
//
//    public <T> T get(String key) {
//        return getFrom(mObject, key);
//    }
//
//    public <T> T getReference(String key) {
//        return getFrom(mReference, key);
//    }
//
//    private <T> T getFrom(Object object, String key) {
//        try {
//            Field field = mClass.getField(key);
//            //noinspection unchecked
//            return (T) field.get(object);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//            throw new RuntimeException("get(" + key + ") failed. " + e);
//        }
//    }
//
//
//    public int getInt(String key) {
//        try {
//            Field field = mClass.getField(key);
//            return field.getInt(mObject);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//            throw new RuntimeException("getInt(" + key + ") failed. " + e);
//        }
//    }
//
//    public void setInt(String key, int value) {
//        try {
//            Field field = mClass.getField(key);
//            field.setInt(mObject, value);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//            throw new RuntimeException("setInt(" + key + ") failed. " + e);
//        }
//        notifyModified();
//    }
//
//    public <T> void set(String key, T value) {
//        try {
//            Field field = mClass.getField(key);
//            field.set(mObject, value);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//            throw new RuntimeException("set(" + key + ") failed. " + e);
//        }
//        notifyModified();
//    }
//
//    public float getFloat(String key) {
//        try {
//            Field field = mClass.getField(key);
//            return field.getFloat(mObject);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//            throw new RuntimeException("getFloat(" + key + ") failed. " + e);
//        }
//    }
//
//    public void setFloat(String key, float value) {
//        try {
//            Field field = mClass.getField(key);
//            field.setFloat(mObject, value);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//            throw new RuntimeException("setFloat(" + key + ") failed. " + e);
//        }
//        notifyModified();
//    }
//
//
//    public boolean hasBeenModified() {
//        for (Field field : mClass.getDeclaredFields()) {
//            if (Modifier.isStatic(field.getModifiers())) {
//                continue;
//            }
//            try {
//                if (!Objects.equals(field.get(mObject), field.get(mReference))) {
//                    return true;
//                }
//            } catch (IllegalAccessException e) {
//                // This should really not happen
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }
//
//    private void notifyModified() {
//        mListeners.begin();
//        for (Listener listener : mListeners) {
//            listener.onModified();
//        }
//        mListeners.end();
//    }
//}
