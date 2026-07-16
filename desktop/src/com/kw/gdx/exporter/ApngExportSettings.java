package com.kw.gdx.exporter;

public class ApngExportSettings extends RenderExportSettings {
    public int compression = 6;
    public int repeat;

    public ApngExportSettings() {
        super("APNG");
    }

    @Override
    public ExportFormat format() {
        return ExportFormat.APNG;
    }
}
