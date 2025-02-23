package com.kw.gdx.g3.math;


import com.kw.gdx.g3.utils.Printer;

public abstract class ComparableNumber extends Number {
    private static final long serialVersionUID = 31941563675690416L;

    public int compareTo(Number number) {
        float current = this.floatValue();
        float other = number.floatValue();
        return (int) (current - other);
    }

    @Override
    public abstract double doubleValue();

    @Override
    public abstract float floatValue();

    @Override
    public abstract int intValue();

    @Override
    public abstract long longValue();

    @Override
    public String toString()
    {
        return new Printer(getClass(),
                new Printer.Label(floatValue())).toString();
    }
}
