package com.kw.gdx.exporter;

public class PsdExportSettings extends RenderExportSettings {
    public String encoding = "";
    public int compression;

    public PsdExportSettings() {
        super("PSD");
    }

    @Override
    public ExportFormat format() {
        return ExportFormat.PSD;
    }
}
