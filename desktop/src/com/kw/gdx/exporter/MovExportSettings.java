package com.kw.gdx.exporter;

public class MovExportSettings extends VideoExportSettings {
    public MovExportSettings() {
        super("MOV");
    }

    @Override
    public ExportFormat format() {
        return ExportFormat.MOV;
    }
}
