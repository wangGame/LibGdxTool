package com.kw.gdx.exporter;

public final class ExportSettingsFactory {
    private ExportSettingsFactory() {
    }

    public static ExportSettings create(ExportFormat format) {
        switch (format) {
            case JSON:
                return new JsonExportSettings();
            case BINARY:
                return new BinaryExportSettings();
            case PNG:
                return new PngExportSettings();
            case JPEG:
                return new JpegExportSettings();
            case GIF:
                return new GifExportSettings();
            case APNG:
                return new ApngExportSettings();
            case PSD:
                return new PsdExportSettings();
            case AVI:
                return new AviExportSettings();
            case MOV:
                return new MovExportSettings();
            default:
                throw new IllegalArgumentException("Unsupported format: " + format);
        }
    }
}
