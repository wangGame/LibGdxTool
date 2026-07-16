package com.kw.gdx.exporter;

public class GifExportSettings extends RenderExportSettings {
    public int colors = 256;
    public float colorDither;
    public int alphaThreshold;
    public float alphaDither;
    public int quality = 10;
    public boolean transparency = true;
    public int repeat;

    public GifExportSettings() {
        super("GIF");
    }

    @Override
    public ExportFormat format() {
        return ExportFormat.GIF;
    }
}
