package com.kw.gdx.exporter;

public class JpegExportSettings extends RenderExportSettings {
    public int quality = 90;

    public JpegExportSettings() {
        super("JPEG");
        transparentBackground = false;
    }

    @Override
    public ExportFormat format() {
        return ExportFormat.JPEG;
    }
}
