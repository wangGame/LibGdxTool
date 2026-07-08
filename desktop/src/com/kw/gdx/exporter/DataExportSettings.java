package com.kw.gdx.exporter;

public abstract class DataExportSettings extends ExportSettings {
    public boolean nonessential;
    public boolean cleanUp;
    public boolean warnings = true;
    public TextureAtlasPackSettings packAtlas;
    public String packSource;
    public String packTarget;

    protected DataExportSettings() {
    }

    protected DataExportSettings(String name) {
        super(name);
    }
}
