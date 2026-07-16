package com.kw.gdx.exporter;

public class PngExportSettings extends RenderExportSettings {
    public int compression = 6;

    public PngExportSettings() {
        super("PNG");
    }

    @Override
    public ExportFormat format() {
        return ExportFormat.PNG;
    }
}
