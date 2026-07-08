package com.kw.gdx.exporter;

public class JsonExportSettings extends DataExportSettings {
    public String extension = ".json";
    public String dataFormat = "JSON";
    public boolean prettyPrint;

    public JsonExportSettings() {
        super("JSON");
        nonessential = true;
    }

    @Override
    public ExportFormat format() {
        return ExportFormat.JSON;
    }

    @Override
    public String defaultExtension() {
        return extension == null || extension.length() == 0 ? ".json" : extension;
    }
}
