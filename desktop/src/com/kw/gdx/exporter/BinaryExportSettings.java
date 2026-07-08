package com.kw.gdx.exporter;

public class BinaryExportSettings extends DataExportSettings {
    public String extension = ".skel";

    public BinaryExportSettings() {
        super("Binary");
    }

    @Override
    public ExportFormat format() {
        return ExportFormat.BINARY;
    }

    @Override
    public String defaultExtension() {
        return extension == null || extension.length() == 0 ? ".skel" : extension;
    }
}
