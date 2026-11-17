package com.kw.property;

public  abstract class Property<T, V> {
    private final String mName;
    private final Class<V> mType;

    public Property(Class<V> type, String name) {
        mName = name;
        mType = type;
    }

    public boolean isReadOnly() {
        return false;
    }

    public void set(T object, V value) {
        throw new UnsupportedOperationException("Property " + getName() +" is read-only");
    }

    /**
     * Returns the current value that this property represents on the given <code>object</code>.
     */
    public abstract V get(T object);

    /**
     * Returns the name for this property.
     */
    public String getName() {
        return mName;
    }

    /**
     * Returns the type for this property.
     */
    public Class<V> getType() {
        return mType;
    }
}
