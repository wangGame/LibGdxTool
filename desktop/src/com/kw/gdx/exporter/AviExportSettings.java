package com.kw.gdx.exporter;

public class AviExportSettings extends VideoExportSettings {
    public AviExportSettings() {
        super("AVI");
    }

    @Override
    public ExportFormat format() {
        return ExportFormat.AVI;
    }
}
