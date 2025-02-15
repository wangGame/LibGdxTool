package com.libGdx.test.model.g3.math;

import com.libGdx.test.model.g3.utils.Printer;

public class Rectangle implements Comparable<Rectangle> {
    private int width;
    private int height;

    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public float area() {
        return width() * height();
    }

    public float aspectRatio() {
        return (float) width() / (float) height();
    }

    @Override
    public int compareTo(Rectangle other) {
        return (width() - other.width()) * (height() - other.height());
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Rectangle)) return false;
        Rectangle rectangle = (Rectangle) other;
        return width() == rectangle.width() && height() == rectangle.height();
    }

    public int height() {
        return height;
    }

    @Override
    public String toString() {
        return new Printer(getClass(),
                new Printer.Label("Width", width()),
                new Printer.Label("Height", height()),
                new Printer.Label("Aspect Ratio", aspectRatio())).toString();
    }

    public int width() {
        return width;
    }
}
