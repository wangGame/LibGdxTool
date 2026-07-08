package com.kw.gdx.exporter;

public abstract class VideoExportSettings extends RenderExportSettings {
    public String encoding = "";
    public int quality = 90;
    public int compression;
    public boolean audio;

    protected VideoExportSettings() {
    }

    protected VideoExportSettings(String name) {
        super(name);
    }
}
