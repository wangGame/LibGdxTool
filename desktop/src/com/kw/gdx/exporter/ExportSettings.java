package com.kw.gdx.exporter;

public abstract class ExportSettings {
    public String name;
    public String project;
    public String output;
    public boolean open;

    protected ExportSettings() {
    }

    protected ExportSettings(String name) {
        this.name = name;
    }

    public abstract ExportFormat format();

    public String defaultExtension() {
        return "." + format().name().toLowerCase();
    }

    public void normalize() {
        if (name == null || name.length() == 0) {
            name = format().name();
        }
    }
}
